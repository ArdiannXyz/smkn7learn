package com.example.smk7.Recyclemateriguru;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.smk7.ApiDatabase.ApiClient;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        initializeComponents();
        receiveIntentData();
        loadMateriData();
    }

    private void initializeComponents() {
        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        txtNamaKelas = findViewById(R.id.NamaKelas);
        btnSimpanEdit = findViewById(R.id.Btn_simpan);
        backButton = findViewById(R.id.back_Button);

        backButton.setOnClickListener(v -> finish());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        btnSimpanEdit.setOnClickListener(v -> editMateri());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            idMateri = String.valueOf(intent.getIntExtra("id_tugas", -1));
            idKelas = String.valueOf(intent.getIntExtra("id_kelas", -1));
            namaKelas = intent.getStringExtra("nama_kelas");

            if (txtNamaKelas != null && namaKelas != null) {
                txtNamaKelas.setText(namaKelas);
            }

            if (idMateri.equals("-1") || idKelas.equals("-1")) {
                Toast.makeText(this, "Data materi tidak valid", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void loadMateriData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat data materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.getMateriById(Integer.parseInt(idMateri));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.d(TAG, "Full Response: " + responseString);

                        JSONObject jsonResponse = new JSONObject(responseString);

                        if (jsonResponse.optBoolean("success", false)) {
                            // Parsing data materi
                            JSONObject materiData = jsonResponse.getJSONObject("data");

                            // Set data ke field-field edit
                            String judulMateri = materiData.optString("judul_materi", "");
                            String jenisMateri = materiData.optString("jenis_materi", "");
                            String komentar = materiData.optString("komentar", "");

                            // Update UI
                            edtJudulMateri.setText(judulMateri);
                            edtLampiran.setText(jenisMateri);
                            edtKomentar.setText(komentar);
                        } else {
                            // Tampilkan pesan error jika data tidak ditemukan
                            Toast.makeText(
                                    EditMateri_Guru.this,
                                    "Gagal memuat data: " + jsonResponse.optString("message", "Terjadi kesalahan"),
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish(); // Tutup activity jika data tidak ditemukan
                        }
                    } else {
                        // Tampilkan pesan error jika response tidak berhasil
                        Toast.makeText(
                                EditMateri_Guru.this,
                                "Gagal memuat data: " + response.code(),
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    Toast.makeText(
                            EditMateri_Guru.this,
                            "Terjadi kesalahan: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Network error", t);
                Toast.makeText(
                        EditMateri_Guru.this,
                        "Gagal terhubung: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }
        });
    }

    private void editMateri() {
        String judulMateri = edtJudulMateri.getText().toString().trim();
        String jenisMateri = edtLampiran.getText().toString().trim();
        String komentar = edtKomentar.getText().toString().trim();

        int idGuru = getCurrentLoggedInTeacherId();
        String deadline = getCurrentDateTime();
        String videoUrl = "";

        if (TextUtils.isEmpty(judulMateri) ||
                TextUtils.isEmpty(jenisMateri) ||
                TextUtils.isEmpty(komentar)) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupdate materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.updateMateri(
                Integer.parseInt(idMateri),
                idGuru,
                jenisMateri,
                judulMateri,
                komentar,
                Integer.parseInt(idKelas),
                deadline,
                videoUrl
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();

                        Log.d(TAG, "Full Response: " + responseString);

                        JSONObject jsonResponse = new JSONObject(responseString);

                        if (jsonResponse.optBoolean("success", false)) {
                            Toast.makeText(
                                    EditMateri_Guru.this,
                                    "Materi berhasil diupdate",
                                    Toast.LENGTH_SHORT
                            ).show();

                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(
                                    EditMateri_Guru.this,
                                    "Gagal update: " + jsonResponse.optString("message", "Terjadi kesalahan"),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } else {
                        Toast.makeText(
                                EditMateri_Guru.this,
                                "Gagal update: " + response.code(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    Toast.makeText(
                            EditMateri_Guru.this,
                            "Terjadi kesalahan: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Network error", t);
                Toast.makeText(
                        EditMateri_Guru.this,
                        "Gagal terhubung: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
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
        intent.setType("*/*");
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
                    // Tambahkan pengecekan indeks kolom
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
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

    private int getCurrentLoggedInTeacherId() {
        SharedPreferences prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return prefs.getInt("id_guru", -1);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
}