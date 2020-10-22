package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.HabitDetail;

import java.util.ArrayList;

public class HaBitDetailAdapter extends RecyclerView.Adapter<HaBitDetailAdapter.habitDetailHolder> {
    LayoutInflater inflater;
    ArrayList<HabitDetail> data;

    public HaBitDetailAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<HabitDetail> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public habitDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_habit_detail, parent, false);
        return new habitDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull habitDetailHolder holder, int position) {
        final HabitDetail habitDetail = data.get(position);
        holder.bindData(habitDetail);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class habitDetailHolder extends RecyclerView.ViewHolder {
        TextView tv_des, tv_content;

        public habitDetailHolder(@NonNull View itemView) {
            super(itemView);
            tv_des = itemView.findViewById(R.id.tvDes);
            tv_content = itemView.findViewById(R.id.tvContent);
        }

        public void bindData(HabitDetail habitDetail) {
            tv_des.setText(habitDetail.getDescription());
            tv_content.setText(habitDetail.getContent());
        }
    }
}
