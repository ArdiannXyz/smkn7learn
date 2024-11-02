package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GantiPasswordActivity extends AppCompatActivity {

    private EditText edt_pw_login, edt_konfirpwbaru;
    private Button btn_masuk_kelogin;
    private ProgressBar progressBar2;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        edt_pw_login = (EditText) findViewById(R.id.edt_pw_login);
        edt_konfirpwbaru = (EditText) findViewById(R.id.edt_konfirpwbaru);
        btn_masuk_kelogin = (Button) findViewById(R.id.btn_masuk_kelogin);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        auth = FirebaseAuth.getInstance();

        btn_masuk_kelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordBaru = edt_pw_login.getText().toString().trim();
                String konfirmasiPassword = edt_konfirpwbaru.getText().toString().trim();

                if (TextUtils.isEmpty(passwordBaru)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordBaru.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordBaru.equals(konfirmasiPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);
                //get email from lupa password activity
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");

                if (email != null && !email.isEmpty()) {
                    auth.confirmPasswordReset(email, passwordBaru)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(GantiPasswordActivity.this, "Password has been changed successfully!", Toast.LENGTH_SHORT).show();
                                        // Arahkan ke LoginActivity
                                        Intent intentToLogin = new Intent(GantiPasswordActivity.this, LoginActivity.class);
                                        startActivity(intentToLogin);
                                        finish(); // Tutup GantiPasswordActivity
                                    } else {
                                        Toast.makeText(GantiPasswordActivity.this, "Failed to change password!", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar2.setVisibility(View.GONE);
                                }
                            });
                } else {
                    Toast.makeText(GantiPasswordActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }
}