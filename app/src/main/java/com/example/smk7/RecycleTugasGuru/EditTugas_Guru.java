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

    private ImageView backButton;
    private TextView txtNamaKelas;
    private EditText edtJudulTugas, edtLampiran, edtTanggal, edtKomentar;
    private Button btnSimpanTugas;

    private Calendar calendar;
    private SimpleDateFormat dateTimeFormat;

    private Uri selectedFileUri;
    private File selectedFile;
    private String idKelas, namaKelas, idTugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_tugas_guru);

        initializeComponents();
        receiveIntentData();
        setupListeners();
        loadTugasData();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.back_Button);
        txtNamaKelas = findViewById(R.id.NamaKelasLayout).findViewById(R.id.NamaKelas);

        edtJudulTugas = findViewById(R.id.Edt_JudulTugas);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtTanggal = findViewById(R.id.Edt_Tanggal);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        btnSimpanTugas = findViewById(R.id.btn_simpanTugas);

        calendar = Calendar.getInstance();
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Tambahkan log untuk melihat nilai yang diterima
            Log.d(TAG, "ID Tugas dari Intent: " + intent.getIntExtra("id_tugas", -1));
            Log.d(TAG, "ID Kelas dari Intent: " + intent.getIntExtra("id_kelas", -1));
            Log.d(TAG, "Nama Kelas dari Intent: " + intent.getStringExtra("nama_kelas"));

            idTugas = String.valueOf(intent.getIntExtra("id_tugas", -1));
            idKelas = String.valueOf(intent.getIntExtra("id_kelas", -1));
            namaKelas = intent.getStringExtra("nama_kelas");

            if (txtNamaKelas != null && namaKelas != null) {
                txtNamaKelas.setText(namaKelas);
            }

            if (idTugas.equals("-1") || idKelas.equals("-1")) {
                Toast.makeText(this, "Data tugas tidak valid", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void showDateTimePickerDialog() {
        // Dapatkan tanggal saat ini
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Buat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDayOfMonth) {
                        // Buat objek Calendar untuk tanggal yang dipilih
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        // Format tanggal
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(selectedDate.getTime());

                        // Set teks ke EditText
                        edtTanggal.setText(formattedDate);
                    }
                },
                year, month, day
        );

        // Tampilkan DatePicker
        datePickerDialog.show();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        edtTanggal.setOnClickListener(v -> showDateTimePickerDialog());
        btnSimpanTugas.setOnClickListener(v -> validateAndSaveTugas());
        edtLampiran.setOnClickListener(v -> checkStoragePermission());
    }

    private void loadTugasData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat data tugas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Tambahkan log untuk melihat ID yang dikirim ke API
        Log.d(TAG, "Mengirim request dengan ID Tugas: " + idTugas);

        ApiServiceInterface apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.getTugasById(Integer.parseInt(idTugas));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.d(TAG, "Full Response: " + responseString);

                        // Tambahkan logging URL request
                        Log.d(TAG, "Request URL: " + call.request().url());

                        JSONObject jsonResponse = new JSONObject(responseString);

                        if (jsonResponse.optBoolean("success", false)) {
                            JSONObject tugasData = jsonResponse.getJSONObject("data");

                            String judulTugas = tugasData.optString("judul_tugas", "");
                            String deskripsi = tugasData.optString("deskripsi", "");  // Gunakan deskripsi, bukan keterangan
                            String deadline = tugasData.optString("deadline", "");

                            edtJudulTugas.setText(judulTugas);
                            edtKomentar.setText(deskripsi);  // Tetap menggunakan edtKomentar untuk UI
                            edtTanggal.setText(deadline);
                        } else {
                            String errorMessage = jsonResponse.optString("message", "Terjadi kesalahan");
                            Log.e(TAG, "API Error: " + errorMessage);
                            Toast.makeText(EditTugas_Guru.this, "Gagal memuat data: " + errorMessage, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.e(TAG, "Response tidak sukses: " + response.code());
                        Toast.makeText(EditTugas_Guru.this, "Gagal memuat data: " + response.code(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    Toast.makeText(EditTugas_Guru.this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Network error", t);
                Toast.makeText(EditTugas_Guru.this, "Gagal terhubung: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        edtTanggal.setText(dateTimeFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Izin diperlukan untuk memilih file", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Tidak ada file manager yang terinstall.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                selectedFile = new File(getFilePathFromUri(selectedFileUri));
                String filePath = selectedFile.getName();
                edtLampiran.setText(filePath);
            }
        }
    }

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        try {
            String[] projection = {android.provider.MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
                cursor.close();
            }

            if (filePath == null) {
                filePath = uri.getPath();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting file path", e);
        }
        return filePath;
    }

    private void validateAndSaveTugas() {
        // Ambil data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        int idGuru = sharedPreferences.getInt("id_guru", -1);

        // Validasi input
        String judulTugas = edtJudulTugas.getText().toString().trim();
        String lampiran = edtLampiran.getText().toString().trim();
        String tanggal = edtTanggal.getText().toString().trim();
        String komentar = edtKomentar.getText().toString().trim();

        // Cek apakah semua field terisi
        if (TextUtils.isEmpty(judulTugas)) {
            edtJudulTugas.setError("Judul tugas harus diisi");
            return;
        }

        if (TextUtils.isEmpty(tanggal)) {
            edtTanggal.setError("Tanggal harus diisi");
            return;
        }

        // Tampilkan progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memperbarui tugas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiServiceInterface apiService = ApiClient.getApiService();

        // Jika ada file yang dipilih
        if (selectedFileUri != null) {
            // Persiapkan file untuk upload
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedFileUri)), selectedFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", selectedFile.getName(), fileRequestBody);

            // Request body untuk parameter lainnya
            RequestBody idTugasBody = RequestBody.create(MultipartBody.FORM, this.idTugas);
            RequestBody idGuruBody = RequestBody.create(MultipartBody.FORM, String.valueOf(idGuru));
            RequestBody judulTugasBody = RequestBody.create(MultipartBody.FORM, judulTugas);
            RequestBody keteranganBody = RequestBody.create(MultipartBody.FORM, komentar);
            RequestBody idKelasBody = RequestBody.create(MultipartBody.FORM, this.idKelas);
            RequestBody deadlineBody = RequestBody.create(MultipartBody.FORM, tanggal);

            // Panggil method update dengan file
            Call<ResponseBody> call = apiService.updateTugasWithFile(
                    idTugasBody,
                    idGuruBody,
                    judulTugasBody,
                    keteranganBody,
                    idKelasBody,
                    deadlineBody,
                    filePart
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    handleTugasUpdateResponse(response, progressDialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    handleTugasUpdateFailure(t, progressDialog);
                }
            });
        } else {
            // Update tanpa file
            Call<ResponseBody> call = apiService.updateTugas(
                    Integer.parseInt(idTugas),
                    idGuru,
                    judulTugas,
                    komentar,
                    Integer.parseInt(idKelas),
                    tanggal
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    handleTugasUpdateResponse(response, progressDialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    handleTugasUpdateFailure(t, progressDialog);
                }
            });
        }
    }

    private void handleTugasUpdateResponse(Response<ResponseBody> response, ProgressDialog progressDialog) {
        progressDialog.dismiss();

        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                Log.d(                TAG, "Full Response: " + responseString);

                JSONObject jsonResponse = new JSONObject(responseString);

                if (jsonResponse.optBoolean("success", false)) {
                    Toast.makeText(
                            EditTugas_Guru.this,
                            "Tugas berhasil diperbarui",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Kembali ke halaman sebelumnya
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(
                            EditTugas_Guru.this,
                            "Gagal memperbarui tugas: " +
                                    jsonResponse.optString("message", "Terjadi kesalahan"),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                Toast.makeText(
                        EditTugas_Guru.this,
                        "Gagal memperbarui tugas: " + response.code(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            Toast.makeText(
                    EditTugas_Guru.this,
                    "Terjadi kesalahan: " + e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void handleTugasUpdateFailure(Throwable t, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Log.e(TAG, "Network error", t);
        Toast.makeText(
                EditTugas_Guru.this,
                "Gagal terhubung: " + t.getMessage(),
                Toast.LENGTH_SHORT
        ).show();
    }

    // Method utilitas tambahan
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method untuk mendapatkan ekstensi file
    private String getFileExtension(Uri uri) {
        String extension = null;
        try {
            // Metode 1: Coba dengan MimeType
            ContentResolver contentResolver = getContentResolver();
            String mimeType = contentResolver.getType(uri);
            if (mimeType != null) {
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            }

            // Metode 2: Jika mime type gagal, ekstrak dari nama file
            if (extension == null) {
                extension = extractExtensionFromUri(uri);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting file extension", e);
        }
        return extension;
    }

    private String extractExtensionFromUri(Uri uri) {
        String result = null;

        // Coba dari nama file dalam content resolver
        if ("content".equals(uri.getScheme())) {
            String[] projection = {OpenableColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(columnIndex);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Column not found", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        // Jika masih null, gunakan path
        if (result == null) {
            result = uri.getPath();
        }

        // Ekstrak ekstensi
        if (result != null) {
            int dotIndex = result.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < result.length() - 1) {
                return result.substring(dotIndex + 1).toLowerCase();
            }
        }

        return null;
    }

    // Method untuk validasi ukuran file
    private boolean isFileSizeValid(File file) {
        long fileSizeInBytes = file.length();
        long fileSizeInMB = fileSizeInBytes / (1024 * 1024);

        // Batasi ukuran file maksimal 10 MB
        return fileSizeInMB <= 10;
    }

    // Method untuk menampilkan dialog konfirmasi
    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin memperbarui tugas?")
                .setPositiveButton("Ya", (dialog, which) -> validateAndSaveTugas())
                .setNegativeButton("Tidak", null)
                .show();
    }

    // Override method untuk menangani back press
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Batalkan perubahan?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    setResult(RESULT_CANCELED);
                    super.onBackPressed(); // Tambahkan ini
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    // Method untuk membersihkan sumber daya
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Membersihkan referensi untuk mencegah memory leak
        if (selectedFile != null) {
            selectedFile = null;
        }
        if (selectedFileUri != null) {
            selectedFileUri = null;
        }
    }

    // Getter dan setter tambahan jika diperlukan
    public String getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(String idTugas) {
        this.idTugas = idTugas;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }
}