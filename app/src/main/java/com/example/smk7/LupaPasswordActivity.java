package com.example.smk7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LupaPasswordActivity extends AppCompatActivity {private EditText edtEmail;
    private Button btnNext;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        edtEmail = findViewById(R.id.edt_username_login);
        btnNext = findViewById(R.id.btn_masuk_dashboard);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Email is required!");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please providevalid email!");
            edtEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        CollectionReference usersRef = db.collection("users"); // Ganti "users" dengan nama koleksi Anda

        usersRef.whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Email ditemukan di database Firestore
                                // Kirim email reset password
                                auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(LupaPasswordActivity.this, "Email reset password telah dikirim!", Toast.LENGTH_SHORT).show();
                                                    // Arahkan ke LoginActivity
                                                    Intent intentToLogin = new Intent(LupaPasswordActivity.this, LoginActivity.class);
                                                    startActivity(intentToLogin);
                                                    finish(); // Tutup LupaPasswordActivity
                                                } else {
                                                    Toast.makeText(LupaPasswordActivity.this, "Gagal mengirim email reset password!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Email tidak ditemukan di database Firestore
                                Toast.makeText(LupaPasswordActivity.this, "Email tidak ada atau belum ditambahkan", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LupaPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}