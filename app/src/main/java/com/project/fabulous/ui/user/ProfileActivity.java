package com.project.fabulous.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.fabulous.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Toolbar toolbar;
    private ImageView imgUser;
    private TextView tvUser,tvEmail;

    private LinearLayout userLogout;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        Glide.with(this).load(user.getPhotoUrl()).into(imgUser);
        tvUser.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.topToolBar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imgUser = findViewById(R.id.userProfileImage);
        tvUser = findViewById(R.id.userProfileName);
        tvEmail = findViewById(R.id.userProfileEmail);

        userLogout = findViewById(R.id.userLogout);
        userLogout.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userLogout:
                mAuth.signOut();
                finish();
                break;
        }
    }
}