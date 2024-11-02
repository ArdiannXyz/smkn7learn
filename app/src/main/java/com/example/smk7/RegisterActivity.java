package com.example.smk7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inisialisasi View
        signupEmail = findViewById(R.id.email_register);
        signupPassword = findViewById(R.id.pw_register);
        signupButton = findViewById(R.id.btn_register);
        loginText = findViewById(R.id.txt_lupapass);

        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            // Validasi input
            if (email.isEmpty()) {
                signupEmail.setError("Email tidak boleh kosong");
                signupEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                signupPassword.setError("Password tidak boleh kosong");
                signupPassword.requestFocus();
                return;
            }

            // Minimal panjang password
            if (password.length() < 6) {
                signupPassword.setError("Password minimal 6 karakter");
                signupPassword.requestFocus();
                return;
            }

            registerAdmin(email, password);
        });

        loginText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerAdmin(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // Menggunakan Map alih-alih objek User
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", uid);
                            userData.put("email", email);
                            userData.put("role", "admin");
                            userData.put("nama", "Admin");

                            // Simpan ke Firestore
                            db.collection("users")
                                    .document(uid)
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this,
                                                "Admin berhasil didaftarkan",
                                                Toast.LENGTH_SHORT).show();
                                        // Pindah ke Login Activity
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this,
                                                "Error menyimpan data: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        // Log error untuk debugging
                                        Log.e("RegisterActivity", "Error saving to Firestore", e);
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registrasi gagal: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}