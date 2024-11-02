package com.example.smk7.Guru;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.R;
import com.example.smk7.ViewPagerAdapterGuru;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardGuru extends AppCompatActivity {

    ViewPager2 viewPager2;
    ViewPagerAdapterGuru viewPagerAdapterguru;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboardguru);
        LayoutInflater inflater = getLayoutInflater();
        bottomNavigationView = findViewById(R.id.bottomnav);
        viewPager2 = findViewById(R.id.Viewpagerguru);
        viewPagerAdapterguru = new ViewPagerAdapterGuru(this);
        viewPager2.setAdapter(viewPagerAdapterguru);
        viewPager2.setUserInputEnabled(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.b_homeguru) {
                    viewPager2.setCurrentItem(0);
                } else if (id == R.id.b_profilguru) {
                    viewPager2.setCurrentItem(1);
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
                    bottomNavigationView.getMenu().findItem(R.id.b_profilguru).setChecked(true);
                }
                viewPager2.setUserInputEnabled(false);
                super.onPageSelected(position);
            }
        });
    }
}