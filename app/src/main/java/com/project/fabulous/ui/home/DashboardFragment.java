package com.project.fabulous.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.project.fabulous.R;
import com.project.fabulous.adapters.DailyHabitsAdapter;
import com.project.fabulous.adapters.TodosAdapter;
import com.project.fabulous.api.ApiBuilder;
import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.Todo;
import com.project.fabulous.ui.todos.CreateTodoActivity;
import com.project.fabulous.ui.todos.TodoBottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements DailyHabitsAdapter.OnClickDailyHabitsListener, View.OnClickListener, TodosAdapter.OnClickTodosListener {
    private RecyclerView habitRecyclerView;
    private DailyHabitsAdapter dailyHabitsAdapter;

    private RecyclerView todoRecyclerView;
    private TodosAdapter todosAdapter;

    private FloatingActionButton createHabitBtn, createTodoBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        loadData();
    }

    private void loadData() {
        ArrayList<DailyHabit> data = new ArrayList<>();
        data.add(new DailyHabit("hasgdhasdas", "Send Email"));
        data.add(new DailyHabit("aasdwdwfsdf", "Shopping"));
        data.add(new DailyHabit("aduhrjfhsds", "Homework"));
        dailyHabitsAdapter.setData(data);

        loadTodosData();
    }

    private void loadTodosData() {
        ApiBuilder.getInstance().getTodayTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                ArrayList<Todo> todos = (ArrayList<Todo>) response.body();
                todosAdapter.setData(todos);
//                Log.d("res", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage() );
            }
        });
    }

    private void initViews() {
        habitRecyclerView = getActivity().findViewById(R.id.rcDailyHabit);
        todoRecyclerView = getActivity().findViewById(R.id.rcTodos);

        createTodoBtn = getActivity().findViewById(R.id.fabCreateTask);
        createTodoBtn.setOnClickListener(this);

        dailyHabitsAdapter = new DailyHabitsAdapter(getLayoutInflater());
        dailyHabitsAdapter.setListener(this);
        habitRecyclerView.setAdapter(dailyHabitsAdapter);

        todosAdapter = new TodosAdapter(getLayoutInflater());
        todosAdapter.setListener(this);
        todoRecyclerView.setAdapter(todosAdapter);
    }
    
    // Click to daily habit
    @Override
    public void onClickDailyHabits(final DailyHabit dailyHabit) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dailyHabitsAdapter.getData().remove(dailyHabit);
                dailyHabitsAdapter.notifyDataSetChanged();
            }
        },1500);
    }

    
    // Click to daily task
    @Override
    public void onClickCheckBoxTodos(Todo todo, int position) {
        todo.setStatus(true);
        todosAdapter.notifyItemChanged(position);
    }

    @Override
    public void onClickTodos(Todo todo) {
        TodoBottomSheetDialogFragment todoBottomSheetDialogFragment = TodoBottomSheetDialogFragment.newInstance();
        todoBottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "todoBottomSheetDialogFragment");
    }

    
    // Click Event
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabCreateTask:
                startActivity(new Intent(getContext(), CreateTodoActivity.class));
                break;
        }
    }
}
