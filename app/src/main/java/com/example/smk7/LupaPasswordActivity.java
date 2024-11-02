package com.example.smk7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPasswordActivity extends AppCompatActivity {

    private EditText edt_username_login;
    private Button btn_masuk_dashboard;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        edt_username_login = findViewById(R.id.edt_username_login);
        btn_masuk_dashboard = findViewById(R.id.btn_masuk_dashboard);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btn_masuk_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = edt_username_login.getText().toString().trim();

        if (email.isEmpty()) {
            edt_username_login.setError("Email is required!");
            edt_username_login.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_username_login.setError("Please provide valid email!");
            edt_username_login.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Email reset password berhasil terkirim
                    // Arahkan ke GantiPasswordActivity
                    Intent intent = new Intent(LupaPasswordActivity.this, GantiPasswordActivity.class);
                    intent.putExtra("email", email); // Kirim email ke GantiPasswordActivity
                    startActivity(intent);
                    finish(); // Tutup LupaPasswordActivity
                } else {
                    Toast.makeText(LupaPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}