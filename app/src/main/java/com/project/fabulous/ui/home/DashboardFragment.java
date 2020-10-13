package com.project.fabulous.ui.home;

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

import com.project.fabulous.R;
import com.project.fabulous.adapters.DailyHabitsAdapter;
import com.project.fabulous.adapters.TodosAdapter;
import com.project.fabulous.api.ApiBuilder;
import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.Todo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements DailyHabitsAdapter.OnClickDailyHabitsListener, TodosAdapter.OnClickTodosListener {
    private RecyclerView habitRecyclerView;
    private DailyHabitsAdapter dailyHabitsAdapter;

    private RecyclerView todoRecyclerView;
    private TodosAdapter todosAdapter;

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

        dailyHabitsAdapter = new DailyHabitsAdapter(getLayoutInflater());
        dailyHabitsAdapter.setListener(this);
        habitRecyclerView.setAdapter(dailyHabitsAdapter);

        todosAdapter = new TodosAdapter(getLayoutInflater());
        todosAdapter.setListener(this);
        todoRecyclerView.setAdapter(todosAdapter);
    }

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

    @Override
    public void onClickTodos(final Todo todo) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                todosAdapter.getData().remove(todo);
                todosAdapter.notifyDataSetChanged();
            }
        },1500);
    }
}
