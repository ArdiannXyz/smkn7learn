package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Mengatur delay untuk 2 detik (1000 milidetik)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Memperbaiki penggunaan Intent
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000); // Ubah delay menjadi 2000 milidetik
    }
}