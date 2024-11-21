package com.example.smk7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
        setContentView(R.layout.activity_lupa_password); // Pastikan layout ini ada

        edt_username_login = findViewById(R.id.edt_username_login);
        btn_masuk_dashboard = findViewById(R.id.btn_masuk_dashboard);

        btn_masuk_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_username_login.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LupaPasswordActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else {
                    checkEmail(email);
                }
            }
        });
    }

    private void checkEmail(String email) {
        String url = "http://192.168.140.109/WebNewbieTeam/check_email.php";
        Log.d("LupaPassword", "Checking email: " + email);  // Log email yang sedang dicek

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LupaPassword", "Response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Log.d("LupaPassword", "Email Berhasil Diverifikasi: " + message);
                                Toast.makeText(LupaPasswordActivity.this, message, Toast.LENGTH_SHORT).show();

                                // Kirim email ke GantiPasswordActivity
                                Intent intent = new Intent(LupaPasswordActivity.this, GantiPasswordActivity.class);
                                intent.putExtra("email", email); // Kirim email yang sudah diverifikasi
                                startActivity(intent);
                            } else {
                                Log.d("LupaPassword", "Email Tidak Ditemukan: " + message);
                                Toast.makeText(LupaPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("LupaPassword", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(LupaPasswordActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            // Menampilkan status code dari response error
                            Log.e("LupaPassword", "Volley Error Code: " + error.networkResponse.statusCode);
                            Log.e("LupaPassword", "Volley Error Response: " + new String(error.networkResponse.data));
                        }
                        Log.e("LupaPassword", "Volley Error: " + error.getMessage());
                        Toast.makeText(LupaPasswordActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(request);
    }
}
