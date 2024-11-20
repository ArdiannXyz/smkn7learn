package com.example.smk7;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPasswordActivity extends AppCompatActivity {

    private EditText edt_username_login;
    private Button btn_masuk_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        edt_username_login = findViewById(R.id.edt_username_login);
        btn_masuk_dashboard = findViewById(R.id.btn_masuk_dashboard);

        btn_masuk_dashboard.setOnClickListener(v -> {
            String email = edt_username_login.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Periksa koneksi internet
            if (isNetworkConnected()) {
                cekEmail(email);
            } else {
                Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi untuk memeriksa koneksi internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // Fungsi untuk mengecek email ke server
    private void cekEmail(String email) {
        String url = Db_Contract.urlLupaPassword;
        Log.d("LupaPasswordActivity", "Mengirim request ke: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.d("LupaPasswordActivity", "Response: " + response);
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean status = jsonResponse.getBoolean("status");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            // Beralih ke halaman ganti password
                            Intent intent = new Intent(this, GantiPasswordActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        Log.e("LupaPasswordActivity", "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Kesalahan dalam memproses data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LupaPasswordActivity", "Volley Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("LupaPasswordActivity", "HTTP Status Code: " + error.networkResponse.statusCode);
                    }
                    Toast.makeText(this, "Terjadi kesalahan jaringan, coba lagi", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
