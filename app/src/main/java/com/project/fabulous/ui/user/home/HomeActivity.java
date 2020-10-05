package com.project.fabulous.ui.user.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.fabulous.R;
import com.project.fabulous.ui.user.journal.JournalActivity;
import com.project.fabulous.ui.user.statistic.StatisticActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;

    // Fragment
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private AboutAppFragment aboutAppFragment = new AboutAppFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        showFragment(dashboardFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.mainToolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.userSetting:
                Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public DashboardFragment getDashboardFragment() {
        return dashboardFragment;
    }

    public SettingFragment getSettingFragment() {
        return settingFragment;
    }

    public AboutAppFragment getAboutAppFragment() {
        return aboutAppFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navDashboard:
                showFragment(dashboardFragment);
                break;
            case R.id.navSettings:
                showFragment(settingFragment);
                break;
            case R.id.navAboutApp:
                showFragment(aboutAppFragment);
                break;
            case R.id.navHome:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.navJournal:
                startActivity(new Intent(this, JournalActivity.class));
                break;
            case R.id.navStatistic:
                startActivity(new Intent(this, StatisticActivity.class));
                break;
        }
        return false;
    }
}