package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.fabulous.R;
import com.project.fabulous.models.Habit;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.exerciseHolder> {
    private LayoutInflater inflater;
    private ArrayList<Habit> data;
    private exerciseItemListener exerciseItemListener;

    public void setExerciseItemListener(ExerciseAdapter.exerciseItemListener exerciseItemListener) {
        this.exerciseItemListener = exerciseItemListener;
    }

    public ExerciseAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Habit> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public exerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_exercise, parent, false);
        return new exerciseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull exerciseHolder holder, int position) {
        final Habit habit = data.get(position);
        holder.bindData(habit);
        if (exerciseItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseItemListener.onClickExercise(habit);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class exerciseHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_summary;
        ImageView imageView;

        public exerciseHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            imageView = itemView.findViewById(R.id.imgExercise);
        }

        public void bindData(Habit habit) {
            tv_title.setText(habit.getTitle());
            Glide.with(imageView).load(habit.getImageUrl()).into(imageView);
        }
    }

    public interface exerciseItemListener {
        void onClickExercise(Habit habit);
    }
}
