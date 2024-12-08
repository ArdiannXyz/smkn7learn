package com.example.smk7.RecycleTugasGuru;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.R;

public class EditTugas_Guru extends AppCompatActivity implements BottomNavigationHandler {

    private DashboardGuru dashboardGuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_tugas_guru); // Gunakan layout fragment yang sama

        // Inisialisasi komponen jika diperlukan
        initializeComponents();
    }

    private void initializeComponents() {
        // Tambahkan inisialisasi komponen di sini
        // Misalnya: findViewById, setOnClickListener, dll.
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomNav();

        // Menonaktifkan swipe di Activity
        if (dashboardGuru != null) {
            dashboardGuru.setSwipeEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        showBottomNav();
    }

    @Override
    public void hideBottomNav() {
        // Implementasi untuk menyembunyikan bottom navigation
        if (dashboardGuru != null) {
            dashboardGuru.hideBottomNav();
        }
    }

    @Override
    public void showBottomNav() {
        // Implementasi untuk menampilkan bottom navigation
        if (dashboardGuru != null) {
            dashboardGuru.showBottomNav();
        }
    }
}