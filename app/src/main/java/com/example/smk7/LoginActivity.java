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
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Siswa.DashboardSiswa;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText txIdentifier, txpassword;
    private Button btn_login;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txIdentifier = findViewById(R.id.edt_nama);
        txpassword = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        TextView lupaPasswordTextView = findViewById(R.id.txt_lupapass);
        lupaPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identifier = txIdentifier.getText().toString().trim();
                String password = txpassword.getText().toString();

                if (!(identifier.isEmpty() || password.isEmpty())) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlLogin,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "Response from server: " + response);
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response.trim());
                                        Log.d("json_response", jsonResponse.toString());

                                        boolean success = jsonResponse.getBoolean("success");
                                        String message = jsonResponse.getString("message");

                                        if (success) {
                                            String role = jsonResponse.getString("role");
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                            // Store user data if needed
                                            String userId = jsonResponse.getString("user_id");
                                            String nama = jsonResponse.getString("nama");
                                            String email = jsonResponse.getString("email");

                                            if (role.equalsIgnoreCase("guru")) {
                                                startActivity(new Intent(getApplicationContext(), DashboardGuru.class));
                                                finish(); // Menambahkan finish() agar tidak bisa kembali ke halaman login
                                            } else if (role.equalsIgnoreCase("siswa")) {
                                                startActivity(new Intent(getApplicationContext(), DashboardSiswa.class));
                                                finish(); // Menambahkan finish() agar tidak bisa kembali ke halaman login
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Role tidak dikenal", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "VolleyError: " + error.toString());
                                }
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
                } else {
                    Toast.makeText(getApplicationContext(), "Email/Nama dan Password harus diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}