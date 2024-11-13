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
import com.example.smk7.Siswa.DashboardSiswa;

import org.json.JSONObject;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private EditText txnama, txpassword;
    private Button btn_login;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txnama = findViewById(R.id.edt_nama);
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

                String nama = txnama.getText().toString();
                String password = txpassword.getText().toString();
                if (!(nama.isEmpty() || password.isEmpty())) {

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = Db_Contract.urlLogin + "?nama=" + nama + "&password=" + password;
                    Log.d(TAG, "Login URL: " + url);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Response from server: " + response);
                            try {
                                String cleanedResponse = response.trim();
                                if (cleanedResponse.startsWith("{")) {
                                    JSONObject jsonResponse = new JSONObject(cleanedResponse);

                                    if (jsonResponse.has("message") && jsonResponse.has("role")) {
                                        String message = jsonResponse.getString("message");
                                        String role = jsonResponse.getString("role");

                                        if (message.trim().equalsIgnoreCase("Selamat Datang")) {
                                            Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                            if (role.equalsIgnoreCase("guru")) {
                                                startActivity(new Intent(getApplicationContext(), DashboardGuru.class));
                                            } else if (role.equalsIgnoreCase("siswa")) {
                                                startActivity(new Intent(getApplicationContext(), DashboardSiswa.class));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Role tidak dikenal", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Format JSON tidak sesuai", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Unexpected JSON format: " + response);
                                    }
                                } else if (cleanedResponse.contains("koneksi berhasil")) {
                                    Toast.makeText(getApplicationContext(), "Koneksi berhasil, tetapi respons tidak valid untuk login", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Non-JSON response: " + cleanedResponse);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Respon server tidak dikenali", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Unrecognized response: " + cleanedResponse);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "VolleyError: " + error.toString());
                            if (error.getCause() != null) {
                                Log.e(TAG, "Error Cause: " + error.getCause());
                            }
                            error.printStackTrace();
                        }

                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000, // timeout dalam milidetik
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Password Atau Email Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
