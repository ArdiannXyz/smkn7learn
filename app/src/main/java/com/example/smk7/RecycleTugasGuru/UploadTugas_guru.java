package com.example.smk7.RecycleTugasGuru;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smk7.ApiDatabase.ApiClient;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.R;

import java.io.File;

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

    private EditText edtJudulTugas, edtLampiran, edtKomentar;
    private Button btnSimpan;
    private ImageView backButton;
    private Uri selectedFileUri;
    private DashboardGuru dashboardGuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_tugas_guru); // Change to your activity layout
        initializeComponents();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> onBackPressed());

        // Assuming you have a way to get the DashboardGuru instance
        if (getParent() instanceof DashboardGuru) {
            dashboardGuru = (DashboardGuru) getParent();
            dashboardGuru.setSwipeEnabled(false);
        }

        edtJudulTugas = findViewById(R.id.Edt_JudulTugas);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        btnSimpan = findViewById(R.id.btn_upTugas);

        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        btnSimpan.setOnClickListener(v -> uploadTugas());
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
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(nameIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uri.getScheme() != null && uri.getScheme().equals("file")) {
            result = uri.getPath();
        }
        return result;
    }

    private void uploadTugas() {
        String judulTugas = edtJudulTugas.getText().toString().trim();
        String komentar = edtKomentar.getText().toString().trim();

        // Misalkan Anda memiliki ID guru, jenis materi, ID kelas, dan deadline
        int idGuru = 1; // Ganti dengan ID guru yang sesuai
        String jenisMateri = "Tugas"; // Ganti dengan jenis materi yang sesuai
        int idKelas = 1; // Ganti dengan ID kelas yang sesuai
        String deadline = "2023-12-31 23:59:59"; // Ganti dengan deadline yang sesuai
        String videoUrl = ""; // Ganti dengan URL video jika ada

        if (TextUtils.isEmpty(judulTugas) || TextUtils.isEmpty(komentar) || selectedFileUri == null) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupload tugas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Prepare the file for upload
        File file = new File(getFilePathFromUri(selectedFileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Create request bodies for other fields
        RequestBody idGuruPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idGuru));
        RequestBody jenisMateriPart = RequestBody.create(MediaType.parse("text/plain"), jenisMateri);
        RequestBody judulPart = RequestBody.create(MediaType.parse("text/plain"), judulTugas);
        RequestBody deskripsiPart = RequestBody.create(MediaType.parse("text/plain"), komentar);
        RequestBody idKelasPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idKelas));
        RequestBody deadlinePart = RequestBody.create(MediaType.parse("text/plain"), deadline);
        RequestBody videoUrlPart = RequestBody.create(MediaType.parse("text/plain"), videoUrl);

        // Create the API call
        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.tambahMateri(
                idGuru,
                jenisMateri,
                judulTugas,
                komentar,
                idKelas,
                deadline,
                videoUrl
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(UploadTugas_guru.this, "Upload berhasil!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadTugas_guru.this, "Upload gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadTugas_guru.this, "Upload gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dashboardGuru != null) {
            dashboardGuru.hideBottomNav(); // Pastikan Anda memiliki metode ini di DashboardGuru
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dashboardGuru != null) {
            dashboardGuru.showBottomNav(); // Pastikan Anda memiliki metode ini di DashboardGuru
        }
    }
}