package com.example.smk7;

import android.os.Bundle;
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

public class GantiPasswordActivity extends AppCompatActivity {
    private EditText edt_buat_password, edt_konfirmasi_password;
    private Button btn_masuk_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        edt_buat_password = findViewById(R.id.edt_buat_password);
        edt_konfirmasi_password = findViewById(R.id.edt_konfirmasi_password);
        btn_masuk_Login = findViewById(R.id.btn_masuk_Login);

        // Mendapatkan email dari aktivitas sebelumnya
        String email = getIntent().getStringExtra("email");

        btn_masuk_Login.setOnClickListener(v -> {
            String password = edt_buat_password.getText().toString().trim();
            String confirmPassword = edt_konfirmasi_password.getText().toString().trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, password);
        });
    }

    private void resetPassword(String email, String password) {
        String url = Db_Contract.urlGantiPassword + "?email=" + email + "&password=" + password; // Perbaikan URL
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean status = jsonResponse.getBoolean("status");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            // Kembali ke halaman login
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Gagal mereset password", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}