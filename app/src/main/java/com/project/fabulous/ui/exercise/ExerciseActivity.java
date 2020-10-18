package com.project.fabulous.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.project.fabulous.R;
import com.project.fabulous.adapters.ExerciseAdapter;
import com.project.fabulous.models.Exercise;
import com.project.fabulous.ui.blog.BlogDetailActivity;
import com.project.fabulous.ui.habit_detail.HabitDetailActivity;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity implements ExerciseAdapter.exerciseItemListener {
    RecyclerView rc;
    ExerciseAdapter adapter;
    ArrayList<Exercise> exercises;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercie);
        initView();
        initData();
    }

    private void initView() {
        rc = findViewById(R.id.rcExercise);
        adapter = new ExerciseAdapter(getLayoutInflater());
        adapter.setExerciseItemListener(this);
        toolbar = findViewById(R.id.tbExercise);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        rc.setAdapter(adapter);
    }

    private void initData() {
        exercises = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            exercises.add(new Exercise(i, "Push -ups enough 100 pice", "stronger stronger stronger"));
        }
        adapter.setData(exercises);
    }

    @Override
    public void onClickExercise(Exercise exercise) {
        Intent intent = new Intent(this, HabitDetailActivity.class);
        startActivity(intent);
    }
}
