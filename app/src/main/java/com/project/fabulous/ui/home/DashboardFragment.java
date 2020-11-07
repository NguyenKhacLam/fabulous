package com.project.fabulous.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.DailyHabitsAdapter;
import com.project.fabulous.adapters.TodosAdapter;
import com.project.fabulous.api.ApiBuilder;
import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.SubTask;
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

    private AlertDialog dialog;

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

    public void loadsData() {
//        progressBar.setVisibility(View.VISIBLE);
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
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
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

    public void loadDailyHabitData() {
        db.collection("user_habit").document(currentUser.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<HashMap<Object, Object>> dailyHabits = (ArrayList<HashMap<Object, Object>>) value.get("habits");
                        ArrayList<DailyHabit> habits = new ArrayList<>();
                        if (!value.exists()){
                            ArrayList<DailyHabit> dailyHabits1 = new ArrayList<>();
                            db.collection("user_habit").document(currentUser.getUid()).update("habits",dailyHabits1);
                        }
                        for (int i = 0; i < dailyHabits.size(); i++) {
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(dailyHabits.get(i));
                            DailyHabit habit = gson.fromJson(jsonElement, DailyHabit.class);
                            if (!habit.isTodayStatus()) {
                                habits.add(habit);
                            }
                        }
                        dailyHabitsAdapter.setData(habits);
                    }
                });
    }

    public void loadTodosData(){
            db.collection("todos")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            ArrayList<Todo> todos = new ArrayList<>();
                            for (QueryDocumentSnapshot item : value) {
                                Todo todo = new Todo();
                                todo.setId(item.getId());
                                todo.setTitle(item.get("title").toString());
                                todo.setDescription(item.get("description").toString());
                                todo.setStatus((Boolean) item.get("status"));

                                // Convert HashMap to ArrayList<SubTask>
                                ArrayList<HashMap<Object, Object>> subTasksHash = (ArrayList<HashMap<Object, Object>>) item.get("subTasks");

                                ArrayList<SubTask> subTasks = new ArrayList<>();
                                for (int i = 0; i < subTasksHash.size(); i++) {
                                    Gson gson = new Gson();
                                    JsonElement jsonElement = gson.toJsonTree(subTasksHash.get(i));
                                    SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
                                    subTasks.add(subTask);
                                }

                                todo.setSubTasks(subTasks);

                                if (item.get("userId").toString().equals(currentUser.getUid()) && (Boolean) item.get("status") == false) {
                                    todos.add(todo);
                                }
                            }
                            todosAdapter.setData(todos);
                        }
                    });
    }

    private void initViews(){
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

    @Override
    public void onLongClickDailyHabits(DailyHabit dailyHabit, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to quit this habit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dailyHabitsAdapter.getData().remove(position);
                        dailyHabitsAdapter.notifyDataSetChanged();

                        HashMap<String, Object> dailyHabitNeedDelete = new HashMap<>();
                        dailyHabitNeedDelete.put("name", dailyHabit.getName());
                        dailyHabitNeedDelete.put("todayStatus", dailyHabit.isTodayStatus());
                        db.collection("user_habit").document(currentUser.getUid())
                                .update("habits", FieldValue.arrayRemove(dailyHabitNeedDelete))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "You've quit " + dailyHabit.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
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
        Toast.makeText(getContext(), "You've done " + todo.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickTodos(Todo todo) {
        Intent intent = new Intent(getContext(), CreateTodoActivity.class);
        intent.putExtra("todo", todo);
        startActivity(intent);
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
