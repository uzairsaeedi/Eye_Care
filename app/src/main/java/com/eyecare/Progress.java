package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Progress extends BaseActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    TextView tvTotalPerformance;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tvTotalPerformance = findViewById(R.id.tvTotalPerformance);

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
                    Intent i = new Intent(Progress.this, MainActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_remove_ads) {
                    CustomDialogUtil.showRemoveAdsDialog(Progress.this);
                } else if (id == R.id.nav_rate_us) {
                    CustomDialogUtil.showRateUsDialog(Progress.this);
                } else if (id == R.id.nav_share_app) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareBody = "Check out this awesome Eye Care app: https://play.google.com/store/apps/details?id=" + getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Eye Care App");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                } else if (id == R.id.nav_privacy_policy) {
                    Intent intent = new Intent(Progress.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Daily Performance");
            else tab.setText("History");
        }).attach();

        ImageView ivBanner = findViewById(R.id.ivBanner);

        int startTab = getIntent().getIntExtra("startTab", 0); // 0 = Daily Performance, 1 = History
        viewPager.setCurrentItem(startTab, false); // set initial tab

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                updateBannerForTab(pos, ivBanner);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        int initial = tabLayout.getSelectedTabPosition();
        if (initial < 0) initial = startTab; // fallback
        updateBannerForTab(initial, ivBanner);


        int total = (0 + 0 + 9 + 0 + 0) / 5;
        tvTotalPerformance.setText(total + "%");


        bottomNav = findViewById(R.id.bottom_nav_include);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_exercise);

            bottomNav.setOnItemSelectedListener(menuItem -> {
                int selectedId = menuItem.getItemId();

                if (selectedId == bottomNav.getSelectedItemId()) {
                    if (selectedId == R.id.nav_progress) {
                        viewPager.setCurrentItem(0, true);
                    }
                    return true;
                }
                if (selectedId == R.id.nav_test) {
                    startActivity(new Intent(Progress.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                } else if (selectedId == R.id.nav_exercise) {
                    startActivity(new Intent(Progress.this, EyeExercise.class)
                            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                } else if (selectedId == R.id.nav_games) {
                    startActivity(new Intent(Progress.this, EyeGames.class)
                            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                } else if (selectedId == R.id.nav_progress) {
                    viewPager.setCurrentItem(0, true);
                }

                return true;
            });
        }
    }

    private void updateBannerForTab(int tabPosition, ImageView ivBanner) {
        // determine image resource in a single assignment (so it's effectively final)
        final int newRes = (tabPosition == 1) ? R.mipmap.eye_history_foreground : R.mipmap.eye_progress_foreground;

        // Cross-fade animation
        ivBanner.animate().alpha(0f).setDuration(120).withEndAction(() -> {
            ivBanner.setImageResource(newRes);
            ivBanner.animate().alpha(1f).setDuration(120).start();
        }).start();
    }

}