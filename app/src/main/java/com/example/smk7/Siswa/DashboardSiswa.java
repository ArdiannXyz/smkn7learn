package com.example.smk7.Siswa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardSiswa extends AppCompatActivity implements BottomNavigationHandler {

    ViewPager2 viewPager2;
    ViewPagerAdaptersiswa viewPagerAdaptersiswa;
    BottomNavigationView bottomNavigationView;
    private BottomNavigationHandler navigationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboardsiswa);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomnav);
        viewPager2 = findViewById(R.id.Viewpagersiswa);
        viewPagerAdaptersiswa = new ViewPagerAdaptersiswa(this);

        // Set up ViewPager2 with adapter
        viewPager2.setAdapter(viewPagerAdaptersiswa);
        viewPager2.setUserInputEnabled(true);  // Enable swipe for ViewPager2

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                // Change current page based on selected bottom nav item
                if (id == R.id.b_homesiswa) {
                    viewPager2.setCurrentItem(0);
                } else if (id == R.id.b_mapelsiswa) {
                    viewPager2.setCurrentItem(1);
                } else if (id == R.id.b_setting) {
                    viewPager2.setCurrentItem(2);
                }
                return true;
            }
        });

        // Set up page change listener to sync with bottom navigation
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Update selected item in bottom navigation based on page position
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.b_homesiswa).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.b_mapelsiswa).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.b_setting).setChecked(true);
                        break;
                }
                // Disable swipe when the page is selected
                setSwipeEnabled(false);
                super.onPageSelected(position);
            }
        });
    }

    // Method to enable or disable swipe gesture on ViewPager2
    public void setSwipeEnabled(boolean enabled) {
        viewPager2.setUserInputEnabled(enabled);  // Enable or disable swipe
    }

    @Override
    public void hideBottomNav() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBottomNav() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Cek jika Activity kembali dari fragment, tampilkan Bottom Navigation
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);  // Menampilkan kembali Bottom Navigation
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (navigationHandler != null) {
            // Tampilkan kembali Bottom Navigation saat fragment berhenti
            navigationHandler.showBottomNav();  // Memastikan Bottom Navigation muncul tanpa delay
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        setSwipeEnabled(false);

    }
}

