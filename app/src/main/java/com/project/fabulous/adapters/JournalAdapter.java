package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.Exercise;
import com.project.fabulous.models.Journal;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.journalHolder> {
    private LayoutInflater inflater;
    private ArrayList<Journal> data;

    public JournalAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Journal> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public journalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_journal, parent, false);
        return new journalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull journalHolder holder, int position) {
        final Journal journal = data.get(position);
        holder.bindData(journal);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class journalHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_title_post, tv_content_post;

        public journalHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tvDate);
            tv_title_post = itemView.findViewById(R.id.tvTitlePost);
            tv_content_post = itemView.findViewById(R.id.tvContentPost);
        }

        public void bindData(Journal journal) {
            tv_date.setText(journal.getDate());
            tv_content_post.setText(journal.getContentPost());
            tv_title_post.setText(journal.getTitlePost());
        }
    }
}
