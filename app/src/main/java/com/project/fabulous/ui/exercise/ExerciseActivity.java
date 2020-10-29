package com.project.fabulous.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.ExerciseAdapter;
import com.project.fabulous.models.Habit;
import com.project.fabulous.models.HabitCategory;
import com.project.fabulous.ui.habit_detail.HabitDetailActivity;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity implements ExerciseAdapter.exerciseItemListener {
    private static final String TAG = "ExerciseActivity";
    RecyclerView rc;
    ExerciseAdapter adapter;
    ArrayList<Habit> habits;
    MaterialToolbar toolbar;
    private String categoryName = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercie);

        Bundle extras = getIntent().getExtras();
        categoryName = extras.getString("category");

        initView();
        initData();
    }

    private void initView() {
        rc = findViewById(R.id.rcExercise);
        adapter = new ExerciseAdapter(getLayoutInflater());
        adapter.setExerciseItemListener(this);
        toolbar = findViewById(R.id.tbExercise);
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
        db.collection("habits")
                .whereEqualTo("categories", categoryName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<Habit> habits = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                Habit habit = new Habit();
                                habit.setId(snapshot.getId());
                                habit.setTitle(snapshot.get("name").toString());
                                habit.setImageUrl(snapshot.get("imageUrl").toString());

                                habits.add(habit);
                            }

                            adapter.setData(habits);
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
    public void onClickExercise(Habit habit) {
        Intent intent = new Intent(this, HabitDetailActivity.class);
        intent.putExtra("habitId", habit.getId());
        startActivity(intent);
    }
}
