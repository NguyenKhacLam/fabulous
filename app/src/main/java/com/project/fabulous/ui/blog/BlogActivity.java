package com.project.fabulous.ui.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.fabulous.R;
import com.project.fabulous.adapters.LastestPostAdapter;
import com.project.fabulous.adapters.TrendingAdapter;
import com.project.fabulous.models.Blog;

import java.util.ArrayList;

public class BlogActivity extends AppCompatActivity implements LastestPostAdapter.lastestPostItemListener, View.OnClickListener, TrendingAdapter.trendingItemListener {
    private RecyclerView rc_lastestPost;
    private LastestPostAdapter lastestPostAdapter;
    private RecyclerView rc_trending;
    private TrendingAdapter trendingAdapter;
    private Button btnMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);
        initView();
        initData();
    }

    private void initView() {
        rc_lastestPost = findViewById(R.id.rcBlog);
        lastestPostAdapter = new LastestPostAdapter(getLayoutInflater());
        lastestPostAdapter.setLastestPostItemListener(this);
        rc_lastestPost.setAdapter(lastestPostAdapter);

        rc_trending = findViewById(R.id.rcTrending);
        trendingAdapter = new TrendingAdapter(getLayoutInflater());
        trendingAdapter.setTrendingItemListener(this);
        rc_trending.setAdapter(trendingAdapter);

    }

    private void initData() {
        ArrayList<Blog> dataLastest = new ArrayList<>();
        dataLastest.add(new Blog("How push -up  can make you stronger"));
        lastestPostAdapter.setData(dataLastest);
        ArrayList<Blog> dataTrending = new ArrayList<>();
        dataTrending.add(new Blog("How push-up can make your stronger"));
        dataTrending.add(new Blog("How push-up can make your stronger"));
        dataTrending.add(new Blog("How push-up can make your stronger"));
        dataTrending.add(new Blog("How push-up can make your stronger"));
        trendingAdapter.setData(dataTrending);
    }

    @Override
    public void onClickItem(Blog blog) {
        Intent intent = new Intent(this, BlogDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, BlogDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickTrending(Blog blog) {
        Intent intent = new Intent(this, BlogDetailActivity.class);
        startActivity(intent);
    }
}
