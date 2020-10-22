package com.project.fabulous.ui.send_mail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.adapters.SubTaskAdapter;
import com.project.fabulous.models.SubTask;

import java.util.ArrayList;

public class SendMailActivity extends AppCompatActivity {
    RecyclerView rc;
    SubTaskAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_mail);
        initView();
        initData();
    }


    private void initView() {
        rc = findViewById(R.id.rcSubTask);
        adapter = new SubTaskAdapter(getLayoutInflater());
        rc.setAdapter(adapter);
    }

    private void initData() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(new SubTask("email for me"));
        subTasks.add(new SubTask("email for me"));
        subTasks.add(new SubTask("email for me"));
        adapter.setData(subTasks);
    }
}
