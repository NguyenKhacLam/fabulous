package com.project.fabulous.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.DailyHabitsAdapter;
import com.project.fabulous.adapters.TodosAdapter;
import com.project.fabulous.api.ApiBuilder;
import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.Quotes;
import com.project.fabulous.models.Todo;
import com.project.fabulous.ui.habit_category.HabitCategoryActivity;
import com.project.fabulous.ui.todos.CreateTodoActivity;
import com.project.fabulous.ui.todos.TodoBottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements DailyHabitsAdapter.OnClickDailyHabitsListener, View.OnClickListener, TodosAdapter.OnClickTodosListener {
    private RecyclerView habitRecyclerView;
    private DailyHabitsAdapter dailyHabitsAdapter;

    private RecyclerView todoRecyclerView;
    private TodosAdapter todosAdapter;

    private ProgressBar progressBar;

    private FloatingActionButton createHabitBtn, createTodoBtn;
    private TextView tv_author, tv_challenge;

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initViews();
        loadsData();
    }

    private void loadsData() {
        progressBar.setVisibility(View.VISIBLE);
        loadDailyHabitData();
        loadTodosData();
        loadQuotes();
    }

    private void loadQuotes() {
        db.collection("quotes")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            Log.d("TAG", "onComplete: size" + task.getResult());
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.d("TAG", "onComplete: " + snapshot.getId());
                                Quotes quotes = new Quotes();
                                quotes.setId(snapshot.getId());
                                quotes.setAuthor(snapshot.get("author").toString());
                                quotes.setChallenge(snapshot.get("content").toString());
                                tv_author.setText(quotes.getAuthor());
                                tv_challenge.setText(quotes.getChallenge());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage());
                    }
                });

    }

    private void loadDailyHabitData() {
        ApiBuilder.getInstance().getHabitByUser(currentUser.getUid()).enqueue(new Callback<List<DailyHabit>>() {
            @Override
            public void onResponse(Call<List<DailyHabit>> call, Response<List<DailyHabit>> response) {
                ArrayList<DailyHabit> dailyHabits = (ArrayList<DailyHabit>) response.body();
                dailyHabitsAdapter.setData(dailyHabits);
            }

            @Override
            public void onFailure(Call<List<DailyHabit>> call, Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadTodosData() {
        ApiBuilder.getInstance().getTodayTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                ArrayList<Todo> todos = (ArrayList<Todo>) response.body();
                todosAdapter.setData(todos);
                progressBar.setVisibility(View.GONE);
//                Log.d("res", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initViews() {
        progressBar = getActivity().findViewById(R.id.progressBarHome);
        progressBar.setVisibility(View.GONE);

        habitRecyclerView = getActivity().findViewById(R.id.rcDailyHabit);
        todoRecyclerView = getActivity().findViewById(R.id.rcTodos);

        createTodoBtn = getActivity().findViewById(R.id.fabCreateTask);
        createTodoBtn.setOnClickListener(this);

        createHabitBtn = getActivity().findViewById(R.id.fabCreateNewHabit);
        createHabitBtn.setOnClickListener(this);

        dailyHabitsAdapter = new DailyHabitsAdapter(getLayoutInflater());
        dailyHabitsAdapter.setListener(this);
        habitRecyclerView.setAdapter(dailyHabitsAdapter);

        todosAdapter = new TodosAdapter(getLayoutInflater());
        todosAdapter.setListener(this);
        todoRecyclerView.setAdapter(todosAdapter);

        tv_author = getActivity().findViewById(R.id.author);
        tv_challenge = getActivity().findViewById(R.id.challenge);
    }

    // Click to daily habit
    @Override
    public void onClickDailyHabits(final DailyHabit dailyHabit, int position) {
        Map<Object, Object> habitNeedDelete = new HashMap<>();
        habitNeedDelete.put("name", dailyHabit.getName());
        habitNeedDelete.put("todayStatus", false);

        Map<Object, Object> habitNeedUpdate = new HashMap<>();
        habitNeedUpdate.put("name", dailyHabit.getName());
        habitNeedUpdate.put("todayStatus", true);

        db.collection("user_habit").document(currentUser.getUid())
                .update("habits", FieldValue.arrayRemove(habitNeedDelete))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Delete successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage());
                    }
                });

        db.collection("user_habit").document(currentUser.getUid())
                .update("habits", FieldValue.arrayUnion(habitNeedUpdate))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Delete successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage());
                    }
                });

        dailyHabitsAdapter.getData().remove(position);
        dailyHabitsAdapter.notifyItemRemoved(position);
        dailyHabitsAdapter.notifyItemRangeChanged(position, dailyHabitsAdapter.getData().size());
    }

    // Click to daily task
    @Override
    public void onClickCheckBoxTodos(Todo todo, int position) {
        db.collection("todos").document(todo.getId())
                .update("status", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Tag", "Updated!");
                    }
                });
        todo.setStatus(true);
        todosAdapter.notifyItemChanged(position);
    }

    @Override
    public void onClickTodos(Todo todo) {
//        TodoBottomSheetDialogFragment todoBottomSheetDialogFragment = TodoBottomSheetDialogFragment.newInstance();
//        todoBottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "todoBottomSheetDialogFragment");
    }


    // Click Event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCreateTask:
                startActivity(new Intent(getContext(), CreateTodoActivity.class));
                break;
            case R.id.fabCreateNewHabit:
                startActivity(new Intent(getContext(), HabitCategoryActivity.class));
                break;
        }
    }
}
