package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.HabitCategory;

import java.util.ArrayList;

public class HabitCategoryAdapter extends RecyclerView.Adapter<HabitCategoryAdapter.habitCategoryHolder> {
    private LayoutInflater inflater;
    private ArrayList<HabitCategory> data;

    public void setHabitCategoryItemListener(HabitCategoryItemListener habitCategoryItemListener) {
        this.habitCategoryItemListener = habitCategoryItemListener;
    }

    private HabitCategoryItemListener habitCategoryItemListener;

    public void setData(ArrayList<HabitCategory> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public HabitCategoryAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public habitCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_habit_category, parent, false);
        return new habitCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull habitCategoryHolder holder, int position) {
        final HabitCategory habitCategory = data.get(position);
        holder.bindData(habitCategory);
        if (habitCategoryItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    habitCategoryItemListener.onClickItem(habitCategory);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class habitCategoryHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        ImageView imageView;

        public habitCategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvHabitCate);
        }

        public void bindData(HabitCategory habitCategory) {
            tvCategory.setText(habitCategory.getCategory());
        }
    }

    public interface HabitCategoryItemListener {
        void onClickItem(HabitCategory habitCategory);
    }
}
