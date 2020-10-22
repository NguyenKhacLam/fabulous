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

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.trendingHolder> {
    private LayoutInflater inflater;
    private ArrayList<Blog> data;
    private trendingItemListener trendingItemListener;

    public void setTrendingItemListener(TrendingAdapter.trendingItemListener trendingItemListener) {
        this.trendingItemListener = trendingItemListener;
    }

    public TrendingAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Blog> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public trendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_blog, parent, false);
        return new trendingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull trendingHolder holder, int position) {
        final Blog blog = data.get(position);
        holder.bindData(blog);
        if (trendingItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trendingItemListener.onClickTrending(blog);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class trendingHolder extends RecyclerView.ViewHolder {
        TextView tv_title_post;
        Button btn_more;

        public trendingHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_post = itemView.findViewById(R.id.tvTitlePost);
            btn_more = itemView.findViewById(R.id.btnReadMore);
        }

        public void bindData(Blog blog) {
            tv_title_post.setText(blog.getTitle());
        }
    }

    public interface trendingItemListener {
        void onClickTrending(Blog blog);
    }
}
