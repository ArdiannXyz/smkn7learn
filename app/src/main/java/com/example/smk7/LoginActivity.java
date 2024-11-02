package com.example.smk7;

// Mengimpor kelas-kelas yang dibutuhkan
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smk7.Admin.Dashboard;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.RegisterActivity;
import com.example.smk7.LupaPasswordActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db; // Inisialisasi FirebaseFirestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi variabel dan Firebase instance
        loginEmail = findViewById(R.id.email_login);
        loginPassword = findViewById(R.id.pw_login);
        loginButton = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();

            loginUser(email, password);
        });

        // Set up TextView for register and forgot password
        TextView registerTextView = findViewById(R.id.txt_registertext);
        registerTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);
        lupaPasswordTextView.setOnClickListener(v -> {
            try {
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            } catch (Exception e) {
                Log.e("LoginActivity", "Error navigating to LupaPasswordActivity", e);
                Toast.makeText(LoginActivity.this, "Terjadi error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        if (firebaseUser != null) {
                            checkUserRole(firebaseUser.getUid());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Login gagal: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        // Cek di koleksi "users" untuk role
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) { // Memeriksa apakah dokumen ada
                        String role = documentSnapshot.getString("role");

                        if ("admin".equals(role)) {
                            startActivity(new Intent(LoginActivity.this, Dashboard.class));
                            finish();
                        } else if ("guru".equals(role)) {
                            startActivity(new Intent(LoginActivity.this, DashboardGuru.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Role tidak valid di koleksi users", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User tidak ditemukan di koleksi users", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());




        // Mencari TextView untuk registrasi dan menambahkan listener untuk event klik
        TextView registerTextView = findViewById(R.id.txt_registertext);
        registerTextView.setOnClickListener(v -> {
            // Membuat Intent untuk pindah ke RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent); // Memulai RegisterActivity
        });

        // Mencari TextView untuk lupa password dan menambahkan listener untuk event klik
        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);
        lupaPasswordTextView.setOnClickListener(v -> {try {
            // Membuat Intent untuk pindah ke LupaPasswordActivity
            Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
            startActivity(intent); // Memulai LupaPasswordActivity
        } catch (Exception e) {
            // Menangani error, misalnya menampilkan pesan error ke pengguna
            Log.e("LoginActivity", "Error navigating to LupaPasswordActivity", e);
            Toast.makeText(LoginActivity.this, "Terjadi error", Toast.LENGTH_SHORT).show();
        }
        });

        // Set padding for insets (e.g., status bar or navigation bar)

    }
}