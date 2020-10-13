package com.project.fabulous.ui.habit_detail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.adapters.HaBitDetailAdapter;
import com.project.fabulous.models.HabitDetail;

import java.util.ArrayList;

public class HabitDetailActivity extends AppCompatActivity {
    private RecyclerView rc;
    private HaBitDetailAdapter adapter;
    private ArrayList<HabitDetail> details;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_detail_activity);
        initView();
        initData();
    }

    private void initView() {
        rc = findViewById(R.id.rcHabitDetail);
        adapter = new HaBitDetailAdapter(getLayoutInflater());
        rc.setAdapter(adapter);
    }

    private void initData() {
        details = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            details.add(new HabitDetail("Description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));
        }
        adapter.setData(details);
    }
}
