package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.exerciseHolder> {
    private LayoutInflater inflater;
    private ArrayList<Exercise> data;
    private exerciseItemListener exerciseItemListener;

    public void setExerciseItemListener(ExerciseAdapter.exerciseItemListener exerciseItemListener) {
        this.exerciseItemListener = exerciseItemListener;
    }

    public ExerciseAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Exercise> data) {
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
        final Exercise exercise = data.get(position);
        holder.bindData(exercise);
        if (exerciseItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseItemListener.onClickExercise(exercise);
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

        public exerciseHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            tv_summary = itemView.findViewById(R.id.tvSummary);
        }

        public void bindData(Exercise exercise) {
            tv_title.setText(exercise.getTitle());
            tv_summary.setText(exercise.getSummary());
        }
    }

    public interface exerciseItemListener {
        void onClickExercise(Exercise exercise);
    }
}
