package com.example.smk7;

import android.content.Intent; // Tambahkan import untuk Intent
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smk7.ApiDatabase.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GantiPasswordActivity extends AppCompatActivity {

    private EditText edt_buat_password, edt_konfirmasi_password;
    private Button btn_masuk_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        // Ambil email dari Intent
        String email = getIntent().getStringExtra("email");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email is missing", Toast.LENGTH_SHORT).show();
            finish(); // Tutup aktivitas jika email tidak ditemukan
            return;
        }

        edt_buat_password = findViewById(R.id.edt_buat_password);
        edt_konfirmasi_password = findViewById(R.id.edt_konfirmasi_password);
        btn_masuk_Login = findViewById(R.id.btn_masuk_Login);

        btn_masuk_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edt_buat_password.getText().toString().trim();
                String confirmPassword = edt_konfirmasi_password.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(GantiPasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(GantiPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email, newPassword);
                }
            }
        });
    }

    private void resetPassword(String email, String newPassword) {
        String urlResetPassword = Db_Contract.urlGantiPassword;
        RequestQueue queue = Volley.newRequestQueue(this);

        // Tambah validasi password
        if (newPassword.length() < 8) {
            Toast.makeText(this, "Password harus minimal 8 karakter", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            Toast.makeText(this, "Password harus memiliki minimal 1 huruf besar", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.matches(".*[a-z].*")) {
            Toast.makeText(this, "Password harus memiliki minimal 1 huruf kecil", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.matches(".*\\d.*")) {
            Toast.makeText(this, "Password harus memiliki minimal 1 angka", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, urlResetPassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_RESPONSE", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                Toast.makeText(GantiPasswordActivity.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(GantiPasswordActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(GantiPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GantiPasswordActivity.this, "Format response tidak valid", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Penanganan error yang lebih baik
                        String pesanError = "Terjadi kesalahan jaringan";
                        if (error.networkResponse != null) {
                            pesanError = "Error " + error.networkResponse.statusCode + ": ";
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                pesanError += data.getString("message");
                            } catch (Exception e) {
                                pesanError += error.getMessage();
                            }
                        }
                        Log.e("API_ERROR", pesanError);
                        Toast.makeText(GantiPasswordActivity.this, pesanError, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPassword);
                params.put("confirm_password", newPassword); // Tambahan parameter yang dibutuhkan PHP
                return params;
            }
        };

        queue.add(request);
    }
}
