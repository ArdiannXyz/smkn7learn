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
    private String idMapel;
    private String idGuru;
    private String namaKelas;
    private Uri selectedFileUri;
    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_materi_guru);
        initializeComponents();
        receiveIntentData();
    }

    private void initializeComponents() {
        // Initialize views using your existing XML IDs
        backButton = findViewById(R.id.back_Button);
        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar); // Using existing Komentar field
        tvNamaKelas = findViewById(R.id.NamaKelas);
        btnSimpan = findViewById(R.id.Btn_simpan);

        // Set up click listeners
        backButton.setOnClickListener(v -> onBackPressed());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        btnSimpan.setOnClickListener(v -> uploadMateri());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();

        // Log semua extras untuk debugging
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Log.d(TAG, key + ": " + extras.get(key));
            }
        }

        // Ambil data dengan nilai default
        idKelas = intent.getStringExtra("id_kelas");
        idMapel = intent.getStringExtra("id_mapel");
        idGuru = intent.getStringExtra("id_guru");
        namaKelas = intent.getStringExtra("nama_kelas");

        // Validasi data
        if (idGuru == null || idGuru.isEmpty()) {
            Log.e(TAG, "ID Guru tidak ditemukan");
            Toast.makeText(this, "Error: ID Guru tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set nama kelas dengan pengecekan
        if (namaKelas != null && !namaKelas.isEmpty()) {
            tvNamaKelas.setText(namaKelas);
        } else {
            tvNamaKelas.setText("Pilih Kelas");
            Log.w(TAG, "Nama kelas kosong, menggunakan default");
        }
    }

    private void uploadMateri() {
        String judulMateri = edtJudulMateri.getText().toString().trim();
        String deskripsi = edtKomentar.getText().toString().trim(); // Using komentar as deskripsi

        if (TextUtils.isEmpty(judulMateri) || TextUtils.isEmpty(deskripsi)) {
            Toast.makeText(this, "Judul materi dan komentar harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupload materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                // Add required fields for PHP API
                builder.addFormDataPart("judul_materi", judulMateri);
                builder.addFormDataPart("deskripsi", deskripsi); // Send komentar as deskripsi
                builder.addFormDataPart("id_guru", idGuru);
                builder.addFormDataPart("id_mapel", idMapel);
                builder.addFormDataPart("id_kelas", idKelas);

                // Add file if selected
                if (selectedFileUri != null) {
                    File file = getFileFromUri(selectedFileUri);
                    if (file != null) {
                        RequestBody fileBody = RequestBody.create(
                                MediaType.parse(getContentResolver().getType(selectedFileUri)),
                                file
                        );
                        builder.addFormDataPart(
                                "file_materi",
                                file.getName(),
                                fileBody
                        );
                    }
                }

                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(Db_Contract.urlApiUploadMateri)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                            Toast.makeText(
                                    UploadMateri_Guru.this,
                                    jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        } else {
                            String errorMessage = jsonResponse.has("error") ?
                                    jsonResponse.getString("error") :
                                    "Gagal mengupload materi";
                            Toast.makeText(
                                    UploadMateri_Guru.this,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(
                                UploadMateri_Guru.this,
                                "Terjadi kesalahan saat memproses respons",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            } catch (Exception e) {
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
        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                String fileName = getFilePathFromUri(selectedFileUri);
                edtLampiran.setText(fileName);
            }
        }
    }

    private String getFilePathFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
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

    private File getFileFromUri(Uri uri) {
        try {
            File file = new File(getCacheDir(), getFilePathFromUri(uri));
            FileInputStream inputStream = (FileInputStream) getContentResolver().openInputStream(uri);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            Log.e(TAG, "Error creating file", e);
            return null;
        }
    }

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