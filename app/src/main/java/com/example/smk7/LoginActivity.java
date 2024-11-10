package com.example.smk7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smk7.Guru.DashboardGuru;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView lupaPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi views
        initializeViews();
        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        loginButton = findViewById(R.id.btn_login); // Pastikan ID sesuai dengan layout
        lupaPasswordTextView = findViewById(R.id.txt_lupapass);
    }

    private void setupClickListeners() {
        // Setup listener untuk lupa password
        lupaPasswordTextView.setOnClickListener(v -> {
            navigateToLupaPassword();
        });

        // Setup listener untuk login button (jika diperlukan)
        loginButton.setOnClickListener(v -> {
            // Tambahkan logika login di sini
        });
    }

    private void navigateToLupaPassword() {
        try {
            Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("LoginActivity", "Error navigating to LupaPasswordActivity", e);
            Toast.makeText(LoginActivity.this, "Terjadi error saat membuka halaman lupa password", Toast.LENGTH_SHORT).show();
        }
    }
}