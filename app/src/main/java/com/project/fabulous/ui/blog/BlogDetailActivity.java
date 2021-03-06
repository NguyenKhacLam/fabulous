package com.project.fabulous.ui.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.fabulous.R;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class BlogDetailActivity extends AppCompatActivity {
    private static final String TAG = "BlogDetailActivity";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tv_response;
    private String postId = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private WebView mWebView ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        initView();
        loadData();
    }

    private void loadData() {
        final WebView[] webView = {new WebView(this)};
        db.collection("posts").document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            collapsingToolbarLayout.setTitle(snapshot.get("title").toString());
                            webView[0] = mWebView;
                            webView[0].loadDataWithBaseURL(null, snapshot.get("content").toString(), "text/HTML", "UTF-8", null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    private void initView() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        collapsingToolbarLayout = findViewById(R.id.ctBlog);
        tv_response = findViewById(R.id.tvResponse);
        mWebView = findViewById(R.id.wvPost);
    }
}
