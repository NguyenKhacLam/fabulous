package com.project.fabulous.adapters;


import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.models.SubTask;
import com.project.fabulous.R;


import java.util.ArrayList;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.HolderSubTask> {

    private LayoutInflater layoutInflater;
    private ArrayList<SubTask> data;
    private OnClickSubTaskListener listener;

    public SubTaskAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setListener(OnClickSubTaskListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<SubTask> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolderSubTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_subtask, parent, false);
        return new HolderSubTask(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSubTask holder, int position) {
        final SubTask subTask = data.get(position);
        holder.bindView(subTask);
        if (listener != null) {
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickSubTask(subTask);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClickSubTask(subTask, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class HolderSubTask extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public HolderSubTask(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxSubTask);
        }

        private void bindView(SubTask subTask) {
            checkBox.setText(subTask.getTitle());
            if (subTask.getStatus()){
                checkBox.setChecked(true);
                checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    public interface OnClickSubTaskListener {
        void onClickSubTask(SubTask subTask);
        void onLongClickSubTask(SubTask subTask, int position);
    }

}
