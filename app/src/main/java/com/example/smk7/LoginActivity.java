package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.example.smk7.Admin.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.email_login);
        loginPassword = findViewById(R.id.pw_login);
        loginButton = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassword.setError("Empty fields are not allowed");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Empty fields are not allowed");
                } else {
                    loginEmail.setError("Please enter correct email");
                }
            }
        });

        // Get references to TextViews and set click listeners
        TextView registerTextView = findViewById(R.id.txt_registertext);
        registerTextView.setOnClickListener(v -> {
            // Intent to navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);
        lupaPasswordTextView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                // Tangani error, misalnya tampilkan pesan error ke pengguna
                Log.e("LoginActivity", "Error navigating to LupaPasswordActivity", e);
                Toast.makeText(LoginActivity.this, "Terjadi error", Toast.LENGTH_SHORT).show();
            }
        });


        // Set padding for insets (e.g., status bar or navigation bar)

    }
}