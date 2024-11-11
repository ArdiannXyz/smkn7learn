package com.example.smk7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Siswa.DashboardSiswa;  // Tambahkan import untuk DashboardSiswa

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText email_login, pw_login;
    private Button btn_login;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        pw_login = findViewById(R.id.pw_login);
        btn_login = findViewById(R.id.btn_login);

        // Inisialisasi TextView lupa password
        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);

        // Tambahkan OnClickListener untuk TextView lupa password
        lupaPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_login.getText().toString();
                String password = pw_login.getText().toString();

                if (!(email.isEmpty() || password.isEmpty())) {

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    // Perbaiki URL API, pastikan URL sudah benar
                    String url = Db_Contract.urlLogin + "?email=" + email + "&password=" + password;

                    // Log URL untuk memastikan URL sudah benar
                    Log.d("LoginActivity", "Login URL: " + url);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log respons dari server
                            Log.d(TAG, "Response from server: " + response);

                            try {
                                // Parsing respons JSON
                                JSONObject jsonResponse = new JSONObject(response);
                                String message = jsonResponse.getString("message");
                                String role = jsonResponse.getString("role");

                                // Cek respons dari server
                                if (message.trim().equals("Selamat Datang")) {
                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                    // Pindah ke dashboard sesuai role
                                    if (role.equals("guru")) {
                                        startActivity(new Intent(getApplicationContext(), DashboardGuru.class));
                                    } else if (role.equals("siswa")) {
                                        startActivity(new Intent(getApplicationContext(), DashboardSiswa.class));
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Tampilkan pesan error dan catat ke Logcat
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "VolleyError: " + error.toString());
                            if (error.getCause() != null) {
                                Log.e(TAG, "Error Cause: " + error.getCause());
                            }
                            error.printStackTrace(); // Untuk debugging yang lebih mendetail
                        }
                    });

                    // Set timeout dan retry policy untuk StringRequest
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000, // timeout dalam milidetik
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    // Tambahkan request ke RequestQueue
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Password Atau Email Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
