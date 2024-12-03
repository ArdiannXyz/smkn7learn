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

import com.example.smk7.ApiDatabase.ApiHelper;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadMateri_Guru extends AppCompatActivity {

    private static final String TAG = "UploadMateri_Guru";
    private static final int REQUEST_CODE_FILE_PICKER = 1001;
    private static final int PERMISSION_REQUEST_STORAGE = 2001;

    private EditText edtJudulMateri, edtLampiran, edtKomentar;
    private TextView tvNamaKelas;
    private Button btnSimpan;
    private ImageView backButton;
    private String idKelas;
    private String namaKelas;
    private Uri selectedFileUri;
    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_materi_guru);

        // Inisialisasi komponen UI
        initializeComponents();

        // Menerima data dari Intent
        receiveIntentData();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> onBackPressed());

        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        tvNamaKelas = findViewById(R.id.NamaKelas);
        btnSimpan = findViewById(R.id.Btn_simpan);

        // Tambahkan listener untuk membuka file picker
        edtLampiran.setOnClickListener(v -> checkStoragePermission());

        // Setup listener untuk tombol Simpan
        btnSimpan.setOnClickListener(v -> uploadMateri());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        idKelas = intent.getStringExtra("id_kelas");
        namaKelas = intent.getStringExtra("nama_kelas");

        Log.d(TAG, "ID Kelas diterima: " + idKelas);
        Log.d(TAG, "Nama Kelas diterima: " + namaKelas);

        // Validasi dan set nama kelas
        if (namaKelas != null) {
            tvNamaKelas.setText(namaKelas);
        } else {
            tvNamaKelas.setText("Nama Kelas Tidak Diketahui");
            Log.e(TAG, "Nama kelas tidak ditemukan dalam Intent.");
        }
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

    private void uploadMateri() {
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
        progressDialog.setMessage("Mengupload materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Proses upload di background thread
        new Thread(() -> {
            try {
                // Siapkan MultipartBody untuk upload file
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                // Tambahkan parameter text
                builder.addFormDataPart("judul_materi", judulMateri);
                builder.addFormDataPart("komentar", komentar);
                builder.addFormDataPart("id_guru", "1"); // ID guru hardcode
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
                Request request = new Request.Builder()
                        .url(Db_Contract.urlApiUploadMateri)
                        .post(requestBody)
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
                                    UploadMateri_Guru.this,
                                    "Materi berhasil diupload",
                                    Toast.LENGTH_SHORT
                            ).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(
                                    UploadMateri_Guru.this,
                                    "Gagal upload: " + jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(
                                UploadMateri_Guru.this,
                                "Terjadi kesalahan",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            } catch (Exception e) {
                // Tangani error upload
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Upload error", e);
                    Toast.makeText(
                            UploadMateri_Guru.this,
                            "Gagal mengupload: " + e.getMessage(),
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

    // Metode lifecycle lainnya
    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }
}