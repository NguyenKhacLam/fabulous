package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.SubTask;
import com.project.fabulous.ui.send_mail.SendMailActivity;

import java.util.ArrayList;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.subTaskHolder> {
    LayoutInflater inflater;
    ArrayList<SubTask> data;

    public SubTaskAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<SubTask> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public subTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sub_task, parent, false);
        return new subTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subTaskHolder holder, int position) {
        final SubTask subTask = data.get(position);
        holder.bindData(subTask);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class subTaskHolder extends RecyclerView.ViewHolder {
        TextView tv_sub_task;
        Button btn_add_sub_task;

        public subTaskHolder(@NonNull View itemView) {
            super(itemView);
            tv_sub_task = itemView.findViewById(R.id.itemSubTask);
            btn_add_sub_task = itemView.findViewById(R.id.btnAddSubTask);
//            btn_add_sub_task.setOnClickListener(this);
        }

        public void bindData(SubTask subTask) {
            tv_sub_task.setText(subTask.getName());
        }

//        @Override
//        public void onClick(View view) {
//        }
    }
}
