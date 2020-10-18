package com.project.fabulous.ui.habit_category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.project.fabulous.R;
import com.project.fabulous.adapters.HabitCategoryAdapter;
import com.project.fabulous.models.HabitCategory;
import com.project.fabulous.ui.exercise.ExerciseActivity;

import java.util.ArrayList;

public class HabitCategoryFragment extends AppCompatActivity implements HabitCategoryAdapter.HabitCategoryItemListener {
    private RecyclerView rc;
    private HabitCategoryAdapter adapter;
    private ArrayList<HabitCategory> arrayList;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_habit_category);
        initViews();
        initData();
    }

    private void initViews() {
        rc = findViewById(R.id.rcHabitCate);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new HabitCategoryAdapter(getLayoutInflater());
        adapter.setHabitCategoryItemListener(this);
        toolbar = findViewById(R.id.menuBar);
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
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(new HabitCategory(i, "EXCERCICE"));
        }
        adapter.setData(arrayList);
    }

    @Override
    public void onClickItem(HabitCategory habitCategory) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
}
