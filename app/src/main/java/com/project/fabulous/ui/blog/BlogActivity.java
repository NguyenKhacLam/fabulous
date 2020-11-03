package com.project.fabulous.ui.blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.LastestPostAdapter;
import com.project.fabulous.adapters.TrendingAdapter;
import com.project.fabulous.models.Blog;

import java.util.ArrayList;

public class BlogActivity extends AppCompatActivity implements LastestPostAdapter.lastestPostItemListener, View.OnClickListener, TrendingAdapter.trendingItemListener {
    private static final String TAG = "BlogActivity";
    public static final String EXTRA_RESULT ="extra.RESULT" ;
    private RecyclerView rc_lastestPost;
    private LastestPostAdapter lastestPostAdapter;
    private RecyclerView rc_trending;
    private TrendingAdapter trendingAdapter;
    private Button btnMore;
    private MaterialToolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        btnMore = findViewById(R.id.btnReadMore);

        toolbar = findViewById(R.id.tbBlog);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initData() {
        db.collection("posts")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<Blog> blogs = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                Blog blog = new Blog();
                                blog.setId(snapshot.getId());
                                blog.setContent(snapshot.get("content").toString());
                                blog.setTitle(snapshot.get("title").toString());
                                blog.setImageCover(snapshot.get("imageCover").toString());
                                blog.setViewCount(snapshot.get("viewCount").toString());

                                blogs.add(blog);
                            }
                            trendingAdapter.setData(blogs);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage() );
                    }
                });
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
        intent.putExtra("postId", blog.getId());
        startActivity(intent);
    }
}
