package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Button Btnmasuk;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(this);
        // Clear any existing session
        sessionManager.clearSession();

        Btnmasuk = findViewById(R.id.buttonmasuk);
        Btnmasuk.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Pastikan session dibersihkan setiap kali activity dimulai
        if (sessionManager != null) {
            sessionManager.clearSession();
        }
    }
}