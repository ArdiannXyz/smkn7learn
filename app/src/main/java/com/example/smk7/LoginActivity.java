package com.example.smk7;

// Mengimpor kelas-kelas yang dibutuhkan
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

// Kelas LoginActivity, merupakan Activity untuk login pengguna
public class LoginActivity extends AppCompatActivity{

    // Deklarasi variabel untuk elemen UI
    private EditText loginEmail, loginPassword; // EditText untuk input email dan password
    private TextView signupRedirectText; // TextView untuk mengarahkan ke halaman registrasi
    private Button loginButton; // Button untuk tombol login
    private FirebaseAuth auth; // Objek FirebaseAuth untuk autentikasi Firebase

    // Method onCreate() dipanggil saat Activity dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Menampilkan layout activity_login.xml

        // Mencari elemen UI berdasarkan ID dan menyimpannya ke variabel
        loginEmail = findViewById(R.id.email_login);
        loginPassword = findViewById(R.id.pw_login);
        loginButton = findViewById(R.id.btn_login);

        // Mendapatkan instance FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Menambahkan listener untuk event klik pada tombol login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil email dan password dari EditText
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                // Validasi input
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // Jika email tidak kosong dan format email benar
                    if (!pass.isEmpty()) { // Jika password tidak kosong
                        // Login dengan email dan password menggunakan Firebase Authentication
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // Login berhasil
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show(); // Menampilkan pesan "Login Successful"
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class)); // Pindah ke HomeActivity
                                        finish(); // Menutup LoginActivity
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Login gagal
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show(); // Menampilkan pesan "Login Failed"
                                    }
                                });
                    } else {
                        // Password kosong
                        loginPassword.setError("Empty fields are not allowed"); // Menampilkan pesan error pada EditText password
                    }
                } else if (email.isEmpty()) {
                    // Email kosong
                    loginEmail.setError("Empty fields are not allowed"); // Menampilkan pesan error pada EditText email
                } else {
                    // Format email salah
                    loginEmail.setError("Please enter correct email"); // Menampilkan pesan error pada EditText email
                }
            }
        });

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