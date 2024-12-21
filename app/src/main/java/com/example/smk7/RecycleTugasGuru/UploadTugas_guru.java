package com.example.smk7.RecycleTugasGuru;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;
import com.example.smk7.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadTugas_guru extends AppCompatActivity {
    private static final String TAG = "UploadTugas_guru";
    private static final int REQUEST_CODE_FILE_PICKER = 1001;
    private static final int PERMISSION_REQUEST_STORAGE = 2001;

    private EditText edtJudulTugas, edtLampiran, edtTanggal, edtKomentar;
    private TextView tvNamaKelas;
    private Button btnSimpan;
    private ImageView backButton;
    private String idKelas, idMapel, idGuru, namaKelas;
    private Uri selectedFileUri;
    private File selectedFile;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_tugas_guru);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        initializeComponents();
        receiveIntentData();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.back_Button);
        tvNamaKelas = findViewById(R.id.NamaKelas);
        edtJudulTugas = findViewById(R.id.Edt_JudulTugas);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtTanggal = findViewById(R.id.Edt_Tanggal);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        btnSimpan = findViewById(R.id.Btn_simpan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        calendar = Calendar.getInstance();

        setupListeners();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        edtTanggal.setOnClickListener(v -> showDateTimePicker());
        btnSimpan.setOnClickListener(v -> validateAndUploadTugas());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();

        // Get ID Guru from SessionManager
        idGuru = String.valueOf(sessionManager.getIdGuru());
        if (idGuru.equals("-1")) {
            Log.e(TAG, "ID Guru tidak ditemukan");
            Toast.makeText(this, "Error: ID Guru tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get other data from intent
        idKelas = intent.getStringExtra("id_kelas");
        idMapel = intent.getStringExtra("id_mapel");
        namaKelas = intent.getStringExtra("nama_kelas");

        if (namaKelas == null) namaKelas = "Pilih Kelas";
        tvNamaKelas.setText(namaKelas);

        Log.d(TAG, "Data received - ID Guru: " + idGuru +
                ", ID Kelas: " + idKelas +
                ", ID Mapel: " + idMapel +
                ", Nama Kelas: " + namaKelas);
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Show Time Picker after Date is selected
                    new TimePickerDialog(
                            this,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                edtTanggal.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndUploadTugas() {
        String judulTugas = edtJudulTugas.getText().toString().trim();
        String tanggal = edtTanggal.getText().toString().trim();
        String deskripsi = edtKomentar.getText().toString().trim();

        if (TextUtils.isEmpty(judulTugas)) {
            edtJudulTugas.setError("Judul tugas harus diisi");
            return;
        }

        if (TextUtils.isEmpty(tanggal)) {
            edtTanggal.setError("Tanggal deadline harus diisi");
            return;
        }

        if (TextUtils.isEmpty(deskripsi)) {
            edtKomentar.setError("Deskripsi tidak boleh kosong");
            return;
        }

        uploadTugas(judulTugas, tanggal, deskripsi);
    }

    private void uploadTugas(String judulTugas, String tanggal, String deskripsi) {
        showLoading("Mengupload tugas...");

        // Prepare request bodies
        RequestBody idGuruBody = RequestBody.create(MediaType.parse("text/plain"), idGuru);
        RequestBody idMapelBody = RequestBody.create(MediaType.parse("text/plain"), idMapel);
        RequestBody idKelasBody = RequestBody.create(MediaType.parse("text/plain"), idKelas);
        RequestBody judulTugasBody = RequestBody.create(MediaType.parse("text/plain"), judulTugas);
        RequestBody deskripsiBody = RequestBody.create(MediaType.parse("text/plain"), deskripsi);
        RequestBody deadlineBody = RequestBody.create(MediaType.parse("text/plain"), tanggal);

        // Prepare file part
        MultipartBody.Part filePart = null;
        if (selectedFileUri != null) {
            try {
                File file = new File(getFilePathFromUri(selectedFileUri));
                RequestBody requestFile = RequestBody.create(
                        MediaType.parse(getContentResolver().getType(selectedFileUri)),
                        file
                );
                filePart = MultipartBody.Part.createFormData("file_tugas", file.getName(), requestFile);
            } catch (Exception e) {
                hideLoading();
                showError("Error mempersiapkan file: " + e.getMessage());
                return;
            }
        } else {
            // Create empty part if no file selected
            RequestBody emptyBody = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("file_tugas", "", emptyBody);
        }

        // Make API call
        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.uploadTugas(
                idGuruBody,
                idMapelBody,
                idKelasBody,
                judulTugasBody,
                deskripsiBody,
                deadlineBody,
                filePart
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                handleUploadResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                handleNetworkError(t);
            }
        });
    }

    private void handleUploadResponse(Response<ResponseBody> response) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseString);

                if (jsonResponse.optBoolean("success", false)) {
                    Toast.makeText(this, "Tugas berhasil diupload", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showError(jsonResponse.optString("message", "Gagal upload tugas"));
                }
            } else {
                showError("Gagal upload tugas: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                showError("Izin akses penyimpanan ditolak");
            }
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
            showError("Tidak ada file manager yang terinstall");
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

    private void handleNetworkError(Throwable t) {
        Log.e(TAG, "Network error", t);
        showError("Gagal terhubung ke server: " + t.getMessage());
    }

    private void showError(String message) {
        if (!isFinishing()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(String message) {
        if (!isFinishing()) {
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}