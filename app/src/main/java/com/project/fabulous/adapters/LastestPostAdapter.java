package com.project.fabulous.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.models.Blog;

import java.util.ArrayList;

public class LastestPostAdapter extends RecyclerView.Adapter<LastestPostAdapter.lastestPostHolder> {
    private LayoutInflater inflater;
    private ArrayList<Blog> data;
    private lastestPostItemListener lastestPostItemListener;

    public void setLastestPostItemListener(LastestPostAdapter.lastestPostItemListener lastestPostItemListener) {
        this.lastestPostItemListener = lastestPostItemListener;
    }

    public LastestPostAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Blog> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public lastestPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_blog, parent, false);
        return new lastestPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lastestPostHolder holder, int position) {
        final Blog blog = data.get(position);
        holder.bindData(blog);
        if (lastestPostItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastestPostItemListener.onClickItem(blog);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class lastestPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title_post;
        Button btn_more;

        public lastestPostHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_post = itemView.findViewById(R.id.tvTitlePost);
            btn_more = itemView.findViewById(R.id.btnReadMore);
            btn_more.setOnClickListener(this);
        }

        public void bindData(Blog blog) {
            tv_title_post.setText(blog.getTitle());
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface lastestPostItemListener {
        void onClickItem(Blog blog);
    }
}
