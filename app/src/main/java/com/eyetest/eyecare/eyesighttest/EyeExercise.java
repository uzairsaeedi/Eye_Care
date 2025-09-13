package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class EyeExercise extends BaseActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RecyclerView listRecyclerView;
    private TextView tvSeeAll;
    private BottomNavigationView bottomNav;
    private int currentNavItemId = R.id.nav_exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_exercise);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        listRecyclerView = findViewById(R.id.rvItems);
        tvSeeAll = findViewById(R.id.tvSeeAllGames);

        tvSeeAll.setOnClickListener(v -> {
            Intent i = new Intent(EyeExercise.this, EyeGames.class);
            startActivity(i);
        });

        TextView tvPercent = findViewById(R.id.tvBannerPercent);
        tvPercent.setText("0%");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent i = new Intent(EyeExercise.this, MainActivity.class);
                    startActivity(i);
                }
                else if (id == R.id.nav_remove_ads) {
                    CustomDialogUtil.showRemoveAdsDialog(EyeExercise.this);
                }
                else if (id == R.id.nav_rate_us) {
                    CustomDialogUtil.showRateUsDialog(EyeExercise.this);
                }
                else if (id == R.id.nav_share_app) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareBody = "Check out this awesome Eye Care app: https://play.google.com/store/apps/details?id=" + getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Eye Care App");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }

                else if (id == R.id.nav_privacy_policy) {
                    Intent intent = new Intent(EyeExercise.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        ArrayList<ListItem> itemList = new ArrayList<>();
        itemList.add(new ListItem("Eye Muscle Training", "20 exercises . 20 min", R.mipmap.listitem1_foreground ));
        itemList.add(new ListItem("Hyperopia Prevention", "20 exercises . 20 min", R.mipmap.listitem2_foreground));
        itemList.add(new ListItem("Focus Shift", "20 exercises . 20 min", R.mipmap.listitem1_foreground));
        itemList.add(new ListItem("Lazy Eye", "20 exercises . 20 min", R.mipmap.listitem2_foreground));
        itemList.add(new ListItem("Closed Eye Movements", "20 exercises . 20 min", R.mipmap.listitem1_foreground));
        itemList.add(new ListItem("Kaleidoscope", "20 exercises . 20 min", R.mipmap.listitem2_foreground));
        itemList.add(new ListItem("Palming", "20 exercises . 20 min", R.mipmap.listitem1_foreground));

        ListItemAdapter itemAdapter = new ListItemAdapter(this, itemList);
        listRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        listRecyclerView.setAdapter(itemAdapter);

        bottomNav = findViewById(R.id.bottom_nav_include);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_exercise);
            currentNavItemId = R.id.nav_exercise;

            bottomNav.setOnItemSelectedListener(menuItem -> {
                int id = menuItem.getItemId();

                if (id == currentNavItemId) {
                    return true;
                }

                Intent intent = null;

                if (id == R.id.nav_test) {
                    intent = new Intent(EyeExercise.this, MainActivity.class)
                            .putExtra("startTab", 0);
                } else if (id == R.id.nav_exercise) {
                    return true;
                } else if (id == R.id.nav_games) {
                    intent = new Intent(EyeExercise.this, EyeGames.class)
                            .putExtra("startTab", 0);
                } else if (id == R.id.nav_progress) {
                    intent = new Intent(EyeExercise.this, Progress.class)
                            .putExtra("startTab", 0);
                } else if (id == R.id.nav_camera) {
                    intent = new Intent(EyeExercise.this, CameraActivity.class)
                            .putExtra("startTab", 0);
                }

                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                    currentNavItemId = id;
                    return true;
                }

                return false;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressPercentAnimated();
    }

    private void updateProgressPercentAnimated() {
        int completed = ProgressUtils.getCompletedOverall(this);
        int total = QuestionBank.getTotalExercises();
        int newPercent = ProgressUtils.calculatePercent(completed, total);

        TextView tvPercent = findViewById(R.id.tvBannerPercent);
        String oldText = tvPercent.getText().toString().replace("%","").trim();
        int oldPercent = 0;
        try { oldPercent = Integer.parseInt(oldText); } catch (Exception e) { oldPercent = 0; }

        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(oldPercent, newPercent);
        animator.setDuration(400);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            tvPercent.setText(animatedValue + "%");
        });
        animator.start();
    }

}