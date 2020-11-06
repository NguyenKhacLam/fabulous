package com.project.fabulous.ui.habit_category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.HabitCategoryAdapter;
import com.project.fabulous.models.HabitCategory;
import com.project.fabulous.ui.exercise.ExerciseActivity;

import java.util.ArrayList;

public class HabitCategoryActivity extends AppCompatActivity implements HabitCategoryAdapter.HabitCategoryItemListener {
    private RecyclerView rc;
    private ProgressBar progressBar;
    private HabitCategoryAdapter adapter;
    private ArrayList<HabitCategory> arrayList;
    private MaterialToolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_habit_category);
        initViews();
        initData();
    }

    private void initViews() {
        progressBar = findViewById(R.id.HabitprogressBar);
        progressBar.setVisibility(View.GONE);
        rc = findViewById(R.id.rcHabitCate);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new HabitCategoryAdapter(getLayoutInflater());
        adapter.setHabitCategoryItemListener(this);
        toolbar = findViewById(R.id.menuBar);
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
        rc.setAdapter(adapter);

    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("categories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            ArrayList<HabitCategory> habitCategories = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                HabitCategory habitCategory = new HabitCategory();
                                habitCategory.setId(snapshot.getId());
                                habitCategory.setCategory(snapshot.get("name").toString());
                                habitCategory.setImgCategory(snapshot.get("image").toString());

                                habitCategories.add(habitCategory);
                            }

                            adapter.setData(habitCategories);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage() );
                    }
                });
    }

    @Override
    public void onClickItem(HabitCategory habitCategory) {
        Intent intent = new Intent(this, ExerciseActivity.class);

        intent.putExtra("category", habitCategory.getCategory());
        startActivity(intent);
    }
}
