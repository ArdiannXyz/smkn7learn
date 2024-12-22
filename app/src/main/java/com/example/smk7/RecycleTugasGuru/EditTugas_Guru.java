package com.example.smk7.RecycleTugasGuru;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smk7.ApiDatabase.ApiClient;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
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

public class EditTugas_Guru extends AppCompatActivity {
    private static final String TAG = "EditTugas_Guru";
    private static final int REQUEST_CODE_FILE_PICKER = 1001;
    private static final int PERMISSION_REQUEST_STORAGE = 2001;

    private EditText edtJudulTugas, edtLampiran, edtTanggal, edtKomentar;
    private TextView tvNamaKelas;
    private Button btnSimpan;
    private ImageView backButton;
    private String idKelas, namaKelas, idTugas, idMapel;
    private Uri selectedFileUri;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_tugas_guru);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        initializeComponents();
        receiveIntentData();
        loadTugasData();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.back_Button);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.NamaKelasLayout);
        tvNamaKelas = findViewById(R.id.tv_nama_kelas);  // Tambahkan ini
        edtJudulTugas = findViewById(R.id.Edt_JudulTugas);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtTanggal = findViewById(R.id.Edt_Tanggal);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        btnSimpan = findViewById(R.id.btn_simpanTugas);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setupListeners();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
        edtTanggal.setOnClickListener(v -> showDateTimePicker());
        btnSimpan.setOnClickListener(v -> validateAndUpdateTugas());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            idTugas = String.valueOf(intent.getIntExtra("id_tugas", -1));
            idKelas = String.valueOf(intent.getIntExtra("id_kelas", -1));
            idMapel = String.valueOf(intent.getIntExtra("id_mapel", -1)); // Tambahkan ini
            namaKelas = intent.getStringExtra("nama_kelas");

            // Debug log
            Log.d(TAG, "Received idTugas: " + idTugas);
            Log.d(TAG, "Received idKelas: " + idKelas);
            Log.d(TAG, "Received idMapel: " + idMapel);
            Log.d(TAG, "Received namaKelas: " + namaKelas);

            // Periksa TextView dan nama kelas
            if (tvNamaKelas != null) {
                if (namaKelas != null && !namaKelas.isEmpty()) {
                    tvNamaKelas.setText(namaKelas);
                } else {
                    Log.e(TAG, "Nama kelas kosong atau null");
                    tvNamaKelas.setText("Nama Kelas tidak tersedia");
                }
            } else {
                Log.e(TAG, "TextView nama kelas tidak ditemukan!");
            }

            // Validasi data
            if (idTugas.equals("-1") || idKelas.equals("-1") || idMapel.equals("-1")) {
                // Jika ada data yang tidak valid, coba ambil dari API
                loadTugasData();
                return;
            }
            // Set nama kelas jika tersedia
            if (tvNamaKelas != null && namaKelas != null && !namaKelas.isEmpty()) {
                tvNamaKelas.setText(namaKelas);
            }
        } else {
            showError("Tidak ada data yang diterima");
            finish();
        }
    }

    private void loadTugasData() {
        showLoading("Memuat data tugas...");

        ApiServiceInterface apiService = ApiClient.getApiService();
        try {
            int idTugasInt = Integer.parseInt(idTugas);
            Call<ResponseBody> call = apiService.getTugasById(idTugasInt);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideLoading();
                    handleTugasDataResponse(response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideLoading();
                    handleNetworkError(t);
                }
            });
        } catch (NumberFormatException e) {
            hideLoading();
            showError("ID Tugas tidak valid");
            finish();
        }
    }

    private void handleTugasDataResponse(Response<ResponseBody> response) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseString);

                if (jsonResponse.optBoolean("success", false)) {
                    JSONObject tugasData = jsonResponse.getJSONObject("data");
                    updateUIWithTugasData(tugasData);
                } else {
                    showError("Gagal memuat data: " + jsonResponse.optString("message"));
                }
            } else {
                showError("Gagal memuat data: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void updateUIWithTugasData(JSONObject tugasData) {
        try {
            edtJudulTugas.setText(tugasData.optString("judul_tugas", ""));
            edtKomentar.setText(tugasData.optString("deskripsi", ""));
            edtTanggal.setText(tugasData.optString("deadline", ""));
            edtLampiran.setText(tugasData.optString("file_tugas", ""));

            // Update data yang hilang
            if (idKelas.equals("-1")) {
                idKelas = String.valueOf(tugasData.optInt("id_kelas", -1));
            }
            if (idMapel.equals("-1")) {
                idMapel = String.valueOf(tugasData.optInt("id_mapel", -1));
            }
            if (namaKelas == null || namaKelas.isEmpty()) {
                namaKelas = tugasData.optString("nama_kelas", "Nama Kelas tidak tersedia");
                if (tvNamaKelas != null) {
                    tvNamaKelas.setText(namaKelas);
                }
            }

            // Validasi final
            if (idKelas.equals("-1") || idMapel.equals("-1")) {
                showError("Data kelas atau mapel tidak tersedia");
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating UI", e);
            showError("Terjadi kesalahan saat memuat data");
            finish();
        }
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
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
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndUpdateTugas() {
        if (!validateInput()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin memperbarui tugas ini?")
                .setPositiveButton("Ya", (dialog, which) -> updateTugas())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(edtJudulTugas.getText())) {
            edtJudulTugas.setError("Judul tugas tidak boleh kosong");
            return false;
        }
        if (TextUtils.isEmpty(edtTanggal.getText())) {
            edtTanggal.setError("Tanggal deadline harus diisi");
            return false;
        }
        if (TextUtils.isEmpty(edtKomentar.getText())) {
            edtKomentar.setError("Deskripsi tidak boleh kosong");
            return false;
        }
        return true;
    }

    private void updateTugas() {
        showLoading("Memperbarui tugas...");

        String judulTugas = edtJudulTugas.getText().toString();
        String tanggal = edtTanggal.getText().toString();
        String deskripsi = edtKomentar.getText().toString();
        int idGuru = sessionManager.getIdGuru();
        int idTugasInt = Integer.parseInt(idTugas);
        int idKelasInt = Integer.parseInt(idKelas);
        int idMapelInt = Integer.parseInt(idMapel);

        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call;

        // Cek apakah ada file baru yang dipilih
        if (selectedFileUri != null) {
            try {
                // Prepare request bodies untuk multipart
                RequestBody idTugasBody = RequestBody.create(MediaType.parse("text/plain"), idTugas);
                RequestBody idGuruBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idGuru));
                RequestBody judulTugasBody = RequestBody.create(MediaType.parse("text/plain"), judulTugas);
                RequestBody deskripsiBody = RequestBody.create(MediaType.parse("text/plain"), deskripsi);
                RequestBody idKelasBody = RequestBody.create(MediaType.parse("text/plain"), idKelas);
                RequestBody idMapelBody = RequestBody.create(MediaType.parse("text/plain"), idMapel); // Tambahkan ini
                RequestBody deadlineBody = RequestBody.create(MediaType.parse("text/plain"), tanggal);

                // Prepare file
                File file = new File(getFilePathFromUri(selectedFileUri));
                RequestBody requestFile = RequestBody.create(
                        MediaType.parse(getContentResolver().getType(selectedFileUri)),
                        file
                );
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file_tugas", file.getName(), requestFile);

                // Panggil API dengan file
                call = apiService.updateTugasWithFile(
                        idTugasBody,
                        idGuruBody,
                        judulTugasBody,
                        deskripsiBody,
                        idKelasBody,
                        idMapelBody, // Tambahkan ini
                        deadlineBody,
                        filePart
                );
            } catch (Exception e) {
                hideLoading();
                showError("Error mempersiapkan file: " + e.getMessage());
                return;
            }
        } else {
            // Panggil API tanpa file
            call = apiService.updateTugas(
                    idTugasInt,
                    idGuru,
                    judulTugas,
                    deskripsi,
                    idKelasInt,
                    idMapelInt, // Tambahkan ini
                    tanggal
            );
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                handleUpdateResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                handleNetworkError(t);
            }
        });
    }

    private void handleUpdateResponse(Response<ResponseBody> response) {
        try {
            String responseString = response.body() != null ? response.body().string() : "";
            JSONObject jsonResponse = new JSONObject(responseString);

            // Ambil status dan pesan dari response
            String status = jsonResponse.optString("status");
            String pesan = jsonResponse.optString("pesan");

            if ("sukses".equals(status)) {
                Toast.makeText(this, "Tugas berhasil diperbarui", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id_tugas", idTugas);
                resultIntent.putExtra("judul_tugas", edtJudulTugas.getText().toString());
                resultIntent.putExtra("deskripsi", edtKomentar.getText().toString());
                resultIntent.putExtra("deadline", edtTanggal.getText().toString());

                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Tampilkan pesan error dari server menggunakan AlertDialog
                new AlertDialog.Builder(this)
                        .setTitle("Gagal Memperbarui Tugas")
                        .setMessage(pesan)
                        .setPositiveButton("OK", null)
                        .show();
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