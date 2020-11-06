package com.project.fabulous.ui.habit_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.fabulous.R;
import com.project.fabulous.adapters.HaBitDetailAdapter;
import com.project.fabulous.models.HabitDetail;
import com.project.fabulous.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HabitDetailActivity extends AppCompatActivity {
    private ImageView bg;
    private TextView description;
    private Button button;
    private MaterialToolbar toolbar;
    private String habitId;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_detail_activity);

        Bundle extras = getIntent().getExtras();
        habitId = extras.getString("habitId");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initView();
        initData();
    }

    private void initView() {
        toolbar = findViewById(R.id.tbDetail);
        toolbar.setTitle("");
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

        bg = findViewById(R.id.imageViewBg);
        description = findViewById(R.id.tvContent);
        button = findViewById(R.id.btnChallenge);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHabitToUser();
            }
        });
    }

    private void addHabitToUser() {
        Map<Object,Object> data = new HashMap<>();
        data.put("name", toolbar.getTitle());
        data.put("todayStatus", false);

        db.collection("user_habit").document(currentUser.getUid())
                .update("habits", FieldValue.arrayUnion(data))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(HabitDetailActivity.this, "Habit added!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HabitDetailActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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

    private void initData() {
        db.collection("habits").document(habitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            toolbar.setTitle(snapshot.get("name").toString());
                            description.setText(snapshot.get("description").toString());
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
}
