package com.example.smk7.Guru;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class DashboardGuru extends AppCompatActivity implements BottomNavigationHandler {

    public ViewPager2 viewPager2;
    ViewPagerAdapterGuru viewPagerAdapterguru;
    BottomNavigationView bottomNavigationView;
    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboardguru);

        // Inisialisasi elemen-elemen UI
        bottomNavigationView = findViewById(R.id.bottomnav);
        viewPager2 = findViewById(R.id.Viewpagerguru);
        viewPagerAdapterguru = new ViewPagerAdapterGuru(this);
        viewPager2.setAdapter(viewPagerAdapterguru);
        viewPager2.setUserInputEnabled(true);



        // Setup BottomNavigationView untuk navigasi antar halaman
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.b_homeguru) {
                    viewPager2.setCurrentItem(0);
                } else if (id == R.id.b_kelas) {
                    viewPager2.setCurrentItem(1);
                } else if (id == R.id.b_pb_setting) {
                    viewPager2.setCurrentItem(2);
                }
                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.b_homeguru).setChecked(true);
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.b_kelas).setChecked(true);
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.b_pb_setting).setChecked(true);
                }
                super.onPageSelected(position);
            }
        });
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



    }
}
