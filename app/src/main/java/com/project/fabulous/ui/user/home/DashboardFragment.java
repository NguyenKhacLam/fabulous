package com.project.fabulous.ui.user.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.adapters.DailyHabitsAdapter;
import com.project.fabulous.adapters.TodosAdapter;
import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.Todo;

import java.util.ArrayList;

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

        ArrayList<Todo> data2 = new ArrayList<>();
        data2.add(new Todo("hasgdhasdas", "Send Email"));
        data2.add(new Todo("aasdwdwfsdf", "Shopping"));
        data2.add(new Todo("aduhrjfhsds", "Homework"));
        todosAdapter.setData(data2);
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
