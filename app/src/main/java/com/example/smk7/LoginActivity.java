package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Siswa.DashboardSiswa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText txIdentifier, txpassword;
    private Button btn_login;
    private static final String TAG = "LoginActivity";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        sessionManager.clearSession();

        // Cek jika sudah login
        if (sessionManager.isLoggedIn()) {
            redirectBasedOnRole(sessionManager.getRole());
            finish();
            return;
        }

        txIdentifier = findViewById(R.id.edt_nama);
        txpassword = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);
        lupaPasswordTextView.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class)));

        btn_login.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {
        String identifier = txIdentifier.getText().toString().trim();
        String password = txpassword.getText().toString();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Email/Nama dan Password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlLogin,
                response -> {
                    Log.d(TAG, "Response from server: " + response);
                    handleLoginResponse(response);
                },
                error -> {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "VolleyError: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("identifier", identifier);
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest);
    }

    private void handleLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response.trim());
            Log.d("json_response", jsonResponse.toString());

            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");

            if (success) {
                String role = jsonResponse.getString("role");
                String userId = jsonResponse.getString("user_id"); // Ubah dari user_id ke id
                String nama = jsonResponse.getString("nama");
                String email = jsonResponse.getString("email");

                // Simpan data user
                sessionManager.saveUserLoginData(userId, role, nama, email);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                redirectBasedOnRole(role);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            Toast.makeText(getApplicationContext(),
                    "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (role.equalsIgnoreCase("guru")) {
            intent = new Intent(getApplicationContext(), DashboardGuru.class);
        } else if (role.equalsIgnoreCase("siswa")) {
            intent = new Intent(getApplicationContext(), DashboardSiswa.class);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Role tidak dikenal", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }
}