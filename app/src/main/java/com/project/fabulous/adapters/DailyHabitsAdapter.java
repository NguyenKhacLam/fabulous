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

import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.R;


import java.util.ArrayList;

public class DailyHabitsAdapter extends RecyclerView.Adapter<DailyHabitsAdapter.HolderDailyHabits> {

    private LayoutInflater layoutInflater;
    private ArrayList<DailyHabit> data;
    private OnClickDailyHabitsListener listener;

    public DailyHabitsAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setListener(OnClickDailyHabitsListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<DailyHabit> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public ArrayList<DailyHabit> getData() {
        return data;
    }

    @NonNull
    @Override
    public HolderDailyHabits onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_daily_habit, parent, false);
        return new HolderDailyHabits(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDailyHabits holder, int position) {
        final DailyHabit dailyHabit = data.get(position);
        holder.bindView(dailyHabit);
        if (listener != null) {
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onClickDailyHabits(dailyHabit, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClickDailyHabits(dailyHabit, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class HolderDailyHabits extends RecyclerView.ViewHolder {
        private AppCompatCheckBox checkBox;

        public HolderDailyHabits(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.itemDailyHabitCheckBox);
        }

        private void bindView(DailyHabit dailyHabit) {
            checkBox.setText(dailyHabit.getName());
        }
    }

    public interface OnClickDailyHabitsListener {
        void onClickDailyHabits(DailyHabit dailyHabit, int position);
        void onLongClickDailyHabits(DailyHabit dailyHabit, int position);
    }

}
