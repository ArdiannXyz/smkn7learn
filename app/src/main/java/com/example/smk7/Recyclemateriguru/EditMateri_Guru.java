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
import com.example.smk7.LoginActivity;
import com.example.smk7.R;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMateri_Guru extends AppCompatActivity {
    private static final String TAG = "EditMateri_Guru";
    private static final int REQUEST_CODE_FILE_PICKER = 1001;
    private static final int PERMISSION_REQUEST_STORAGE = 2001;
    private static final String PREF_NAME = "UserData";
    private static final String KEY_ID_GURU = "id_guru";

    private EditText edtJudulMateri, edtLampiran, edtKomentar;
    private TextView txtNamaKelas;
    private Button btnSimpanEdit;
    private ImageView backButton;
    private String idKelas, namaKelas, idMateri;
    private Uri selectedFileUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_materi_guru);

        // Cek status login di awal
        if (getIdGuru() == -1) {
            return; // Method getIdGuru sudah menangani redirect
        }

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        backButton.setOnClickListener(v -> finish());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        btnSimpanEdit.setOnClickListener(v -> editMateri());
    }

    private int getIdGuru() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Tambahkan log untuk melihat semua data di SharedPreferences
        Map<String, ?> allPrefs = prefs.getAll();
        Log.d(TAG, "All SharedPreferences data: " + allPrefs.toString());

        int idGuru = prefs.getInt(KEY_ID_GURU, -1);
        Log.d(TAG, "Retrieved ID Guru: " + idGuru);

        if (idGuru == -1) {
            Log.w(TAG, "ID Guru tidak ditemukan di SharedPreferences");
            Toast.makeText(this, "Sesi guru tidak ditemukan. Silakan login ulang.",
                    Toast.LENGTH_LONG).show();

            // Redirect ke halaman login
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }

        return idGuru;
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            idMateri = String.valueOf(intent.getIntExtra("id_materi", -1));
            idKelas = String.valueOf(intent.getIntExtra("id_kelas", -1));
            namaKelas = intent.getStringExtra("nama_kelas");

            if (txtNamaKelas != null && namaKelas != null) {
                txtNamaKelas.setText(namaKelas);
            }

            Log.d(TAG, "Received ID Materi: " + idMateri);
            Log.d(TAG, "Received ID Kelas: " + idKelas);

            if (idMateri.equals("-1") || idKelas.equals("-1")) {
                Log.e(TAG, "Invalid ID received: idMateri=" + idMateri + ", idKelas=" + idKelas);
                Toast.makeText(this, "Data materi tidak valid", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void loadMateriData() {
        progressDialog.setMessage("Memuat data materi...");
        progressDialog.show();

        ApiServiceInterface apiService = ApiClient.getApiService();

        try {
            int idMateriInt = Integer.parseInt(idMateri);
            if (idMateriInt <= 0) {
                throw new NumberFormatException("ID tidak valid");
            }

            Call<ResponseBody> call = apiService.getMateriById(idMateriInt);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    handleMateriDataResponse(response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    handleNetworkError(t);
                }
            });
        } catch (NumberFormatException e) {
            progressDialog.dismiss();
            Toast.makeText(this, "ID Materi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void handleMateriDataResponse(Response<ResponseBody> response) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                Log.d(TAG, "Full Response: " + responseString);

                JSONObject jsonResponse = new JSONObject(responseString);
                if (jsonResponse.optBoolean("success", false)) {
                    JSONObject materiData = jsonResponse.getJSONObject("data");
                    updateUIWithMateriData(materiData);
                } else {
                    showError("Gagal memuat data: " + jsonResponse.optString("message", "Terjadi kesalahan"));
                }
            } else {
                showError("Gagal memuat data: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void updateUIWithMateriData(JSONObject materiData) {
        String judulMateri = materiData.optString("judul_materi", "");
        String jenisMateri = materiData.optString("jenis_materi", "");
        String komentar = materiData.optString("komentar", "");

        edtJudulMateri.setText(judulMateri);
        edtLampiran.setText(jenisMateri);
        edtKomentar.setText(komentar);
    }

    private void editMateri() {
        String judulMateri = edtJudulMateri.getText().toString().trim();
        String deskripsi = edtKomentar.getText().toString().trim();

        if (TextUtils.isEmpty(judulMateri) || TextUtils.isEmpty(deskripsi)) {
            Toast.makeText(this, "Judul dan deskripsi harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int idGuru = getIdGuru();
        if (idGuru == -1) return;

        Log.d(TAG, "ID Guru from SharedPreferences: " + idGuru);

        progressDialog.setMessage("Mengupdate materi...");
        progressDialog.show();

        // Prepare request bodies
        RequestBody idMateriBody = RequestBody.create(MediaType.parse("text/plain"), idMateri);
        RequestBody idMapelBody = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody idKelasBody = RequestBody.create(MediaType.parse("text/plain"), idKelas);
        RequestBody idGuruBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idGuru));
        RequestBody judulMateriBody = RequestBody.create(MediaType.parse("text/plain"), judulMateri);
        RequestBody deskripsiBody = RequestBody.create(MediaType.parse("text/plain"), deskripsi);

        // Handle file upload
        MultipartBody.Part filePart = prepareFilePart();

        // Make API call
        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.updateMateri(
                idMateriBody, idMapelBody, idKelasBody, idGuruBody,
                judulMateriBody, deskripsiBody, filePart
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                handleUpdateResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                handleNetworkError(t);
            }
        });
    }

    private MultipartBody.Part prepareFilePart() {
        if (selectedFileUri != null) {
            try {
                String fileName = getFilePathFromUri(selectedFileUri);
                File file = new File(fileName);
                RequestBody requestFile = RequestBody.create(
                        MediaType.parse(getContentResolver().getType(selectedFileUri)),
                        file
                );
                return MultipartBody.Part.createFormData("file", fileName, requestFile);
            } catch (Exception e) {
                Log.e(TAG, "Error preparing file: ", e);
                Toast.makeText(this, "Error mempersiapkan file", Toast.LENGTH_SHORT).show();
            }
        }
        // Return empty part if no file selected
        RequestBody emptyBody = RequestBody.create(MediaType.parse("text/plain"), "");
        return MultipartBody.Part.createFormData("file", "", emptyBody);
    }

    private void handleUpdateResponse(Response<ResponseBody> response) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                Log.d(TAG, "Server Response: " + responseString);

                JSONObject jsonResponse = new JSONObject(responseString);
                if (jsonResponse.optBoolean("success", false)) {
                    Toast.makeText(this, "Materi berhasil diupdate", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showError(jsonResponse.optString("message", "Gagal update materi"));
                }
            } else {
                String errorBody = response.errorBody() != null ?
                        response.errorBody().string() : "Unknown error";
                Log.e(TAG, "Error Response: " + errorBody);
                showError("Gagal update: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void handleNetworkError(Throwable t) {
        Log.e(TAG, "Network error", t);
        showError("Gagal terhubung ke server: " + t.getMessage());
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Tidak ada file manager yang terinstall.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                String filePath = getFilePathFromUri(selectedFileUri);
                edtLampiran.setText(filePath);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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

    private boolean validateInput() {
        if (TextUtils.isEmpty(edtJudulMateri.getText())) {
            edtJudulMateri.setError("Judul materi tidak boleh kosong");
            return false;
        }

        if (TextUtils.isEmpty(edtKomentar.getText())) {
            edtKomentar.setError("Deskripsi tidak boleh kosong");
            return false;
        }

        return true;
    }

    private void resetForm() {
        edtJudulMateri.setText("");
        edtLampiran.setText("");
        edtKomentar.setText("");
        selectedFileUri = null;
    }
}