package com.project.fabulous.ui.home;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.fabulous.R;
import com.project.fabulous.ui.blog.BlogActivity;
import com.project.fabulous.ui.habit_category.HabitCategoryActivity;

import com.project.fabulous.ui.focusMode.FocusModeActivity;

import com.project.fabulous.ui.journal.JournalActivity;
import com.project.fabulous.ui.note.NoteActivity;
import com.project.fabulous.ui.statistic.StatisticActivity;
import com.project.fabulous.ui.user.ProfileActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int mMenuID;

    private BottomNavigationView bottomNavigationView;

    // Fragment
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private BlogFragment blogFragment = new BlogFragment();
    private AboutAppFragment aboutAppFragment = new AboutAppFragment();
    private HabitCategoryActivity habitCategoryActivity = new HabitCategoryActivity();
    private JournalActivity journalActivity = new JournalActivity();

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
        bottomNavigationView.getMenu().findItem(R.id.navHome).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
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
        switch (item.getItemId()) {
            case R.id.userSetting:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
        return true;
    }

    public DashboardFragment getDashboardFragment() {
        return dashboardFragment;
    }

    public BlogFragment getBlogFragment() {
        return blogFragment;
    }

    public AboutAppFragment getAboutAppFragment() {
        return aboutAppFragment;
    }

    public HabitCategoryActivity getHabitCategoryFragment() {
        return habitCategoryActivity;
    }

    public JournalActivity getJournalActivity() {
        return journalActivity;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuID = item.getItemId();
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }
        switch (item.getItemId()) {
            case R.id.navDashboard:
                showFragment(dashboardFragment);
                break;
            case R.id.navBlog:
//                showFragment(blogFragment);
                startActivity(new Intent(this, BlogActivity.class));
                break;
            case R.id.navNote:
//                showFragment(blogFragment);
                startActivity(new Intent(this, NoteActivity.class));
                break;
            case R.id.navAboutApp:
                showFragment(aboutAppFragment);
                break;
            case R.id.navFocusMode:
                startActivity(new Intent(this, FocusModeActivity.class));
                break;
            case R.id.navHome:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.navJournal:
                showFragment(journalActivity);
                break;
            case R.id.navStatistic:
                startActivity(new Intent(this, StatisticActivity.class));
                break;

        }
        return true;
    }
}