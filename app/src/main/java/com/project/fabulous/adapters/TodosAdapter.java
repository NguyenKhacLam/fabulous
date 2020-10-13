package com.project.fabulous.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.models.Todo;
import com.project.fabulous.R;


import java.util.ArrayList;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.HolderTodos> {

    private LayoutInflater layoutInflater;
    private ArrayList<Todo> data;
    private OnClickTodosListener listener;

    public TodosAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setListener(OnClickTodosListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<Todo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<Todo> getData() {
        return data;
    }

    @NonNull
    @Override
    public HolderTodos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_todo, parent, false);
        return new HolderTodos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTodos holder, int position) {
        final Todo todo = data.get(position);
        holder.bindView(todo);
        if (listener != null) {
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onClickTodos(todo);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickTodos(todo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class HolderTodos extends RecyclerView.ViewHolder {
        private AppCompatCheckBox checkBox;

        public HolderTodos(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.itemDailyHabitCheckBox);
        }

        private void bindView(Todo todo) {
            checkBox.setText(todo.getTitle());
        }
    }

    public interface OnClickTodosListener {
        void onClickTodos(Todo todo);
    }

}
