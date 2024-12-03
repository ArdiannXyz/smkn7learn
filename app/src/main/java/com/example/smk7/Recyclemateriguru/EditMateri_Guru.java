package com.example.smk7.Recyclemateriguru;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditMateri_Guru extends AppCompatActivity {
    private static final String TAG = "EditMateri_Guru";
    private static final int REQUEST_CODE_FILE_PICKER = 1001;
    private static final int PERMISSION_REQUEST_STORAGE = 2001;

    private EditText edtJudulMateri, edtLampiran, edtKomentar;
    private TextView txtNamaKelas;
    private Button btnSimpanEdit;
    private ImageView backButton;
    private String idKelas, namaKelas, idMateri;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_materi_guru);

        // Inisialisasi komponen
        initializeComponents();

        // Terima data dari intent
        receiveIntentData();

        // Muat data materi
        loadMateriData();
    }

    private void initializeComponents() {
        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        txtNamaKelas = findViewById(R.id.NamaKelas);
        btnSimpanEdit = findViewById(R.id.Btn_simpan);
        backButton = findViewById(R.id.back_Button);

        // Setup listener
        backButton.setOnClickListener(v -> finish());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        btnSimpanEdit.setOnClickListener(v -> editMateri());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Ubah sesuai dengan cara passing data di adapter sebelumnya
            idMateri = String.valueOf(intent.getIntExtra("id_tugas", -1));
            idKelas = String.valueOf(intent.getIntExtra("id_kelas", -1));
            namaKelas = intent.getStringExtra("nama_kelas");

            // Set nama kelas di TextView
            if (txtNamaKelas != null && namaKelas != null) {
                txtNamaKelas.setText(namaKelas);
            }

            // Validasi data
            if (idMateri.equals("-1") || idKelas.equals("-1")) {
                Toast.makeText(this, "Data materi tidak valid", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    private void loadMateriData() {
        String url = Db_Contract.urlApiEditMateri;

        // Debug log
        Log.d(TAG, "Sending id_materi: " + idMateri);
        Log.d(TAG, "Sending id_kelas: " + idKelas);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("API_FULL_RESPONSE", "Raw Response: " + response);

                    try {
                        // Pastikan response adalah JSON yang valid
                        if (!response.trim().startsWith("{")) {
                            throw new JSONException("Invalid JSON response: " + response);
                        }

                        JSONObject jsonResponse = new JSONObject(response);

                        Log.d("API_RESPONSE_DETAILS",
                                "Success: " + jsonResponse.getBoolean("success") +
                                        ", Full Response: " + jsonResponse.toString(2)
                        );

                        if (jsonResponse.getBoolean("success")) {
                            JSONObject materiData = jsonResponse.getJSONObject("data");

                            // Set data ke field
                            edtJudulMateri.setText(materiData.optString("judul_materi", ""));
                            edtLampiran.setText(materiData.optString("jenis_materi", ""));
                            edtKomentar.setText(materiData.optString("komentar", ""));
                        } else {
                            // Log detail error
                            String errorMessage = jsonResponse.optString("message", "Gagal memuat data");
                            String errorDetails = jsonResponse.has("details")
                                    ? jsonResponse.getJSONObject("details").toString()
                                    : "Tidak ada detail error";

                            Log.e(TAG, "API Error Message: " + errorMessage);
                            Log.e(TAG, "API Error Details: " + errorDetails);

                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing Error", e);
                        Log.e(TAG, "Raw Response causing error: " + response);
                        Toast.makeText(this, "Gagal memproses data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network Error", error);
                    Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tugas", idMateri);
                params.put("id_kelas", idKelas);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Semua tipe file
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Pilih File"),
                    REQUEST_CODE_FILE_PICKER
            );
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Tidak ada file manager yang terinstall.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();

                if (selectedFileUri != null) {
                    String filePath = getFilePathFromUri(selectedFileUri);
                    edtLampiran.setText(filePath);
                }
            }
        }
    }

    private String getFilePathFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void editMateri() {
        // Validasi input
        String judulMateri = edtJudulMateri.getText().toString().trim();
        String jenisMateri = edtLampiran.getText().toString().trim();
        String komentar = edtKomentar.getText().toString().trim();

        if (TextUtils.isEmpty(judulMateri) ||
                TextUtils.isEmpty(jenisMateri) ||
                TextUtils.isEmpty(komentar)) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tampilkan progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupdate materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Proses edit di background thread
        new Thread(() -> {
            try {
                // Siapkan MultipartBody untuk upload file
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                // Tambahkan parameter text
                builder.addFormDataPart("id_tugas", idMateri);
                builder.addFormDataPart("judul_materi", judulMateri);
                builder.addFormDataPart("komentar", komentar);
                builder.addFormDataPart("id_kelas", idKelas);

                // Tambahkan file jika ada
                if (selectedFileUri != null) {
                    File file = getFileFromUri(selectedFileUri);
                    if (file != null) {
                        RequestBody fileBody = RequestBody.create(
                                MediaType.parse(getContentResolver().getType(selectedFileUri)),
                                file
                        );
                        builder.addFormDataPart(
                                "jenis_materi",
                                file.getName(),
                                fileBody
                        );
                    }
                }

                // Buat request body
                RequestBody requestBody = builder.build();

                // Buat request
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Db_Contract.urlApiEditMateri)
                        .put(requestBody)
                        .build();

                // Kirim request menggunakan OkHttp
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                // Proses response di main thread
                runOnUiThread(() -> {
                    progressDialog.dismiss();

                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(
                                    EditMateri_Guru.this,
                                    "Materi berhasil diupdate",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Kembali ke halaman sebelumnya
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(
                                    EditMateri_Guru.this,
                                    "Gagal update: " + jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(
                                EditMateri_Guru.this,
                                "Terjadi kesalahan",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            } catch (Exception e) {
                // Tangani error upload
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Update error", e);
                    Toast.makeText(
                            EditMateri_Guru.this,
                            "Gagal mengupdate: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }).start();
    }

    // Metode untuk konversi URI ke File
    private File getFileFromUri(Uri uri) {
        try {
            // Buat file temporary
            File file = new File(getCacheDir(),
                    getFilePathFromUri(uri));

            // Salin file dari Uri ke file temporary
            FileInputStream inputStream =
                    (FileInputStream) getContentResolver().openInputStream(uri);

            // Proses copy file
            org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);

            return file;
        } catch (IOException e) {
            Log.e(TAG, "Error membuat file", e);
            return null;
        }
    }

    // Metode untuk handle permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
}