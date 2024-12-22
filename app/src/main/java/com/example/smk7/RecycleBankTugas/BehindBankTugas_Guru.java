package com.example.smk7.RecycleBankTugas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BehindBankTugas_Guru extends Fragment {

    private BottomNavigationHandler navigationHandler;
    private TextView tvNamaSiswa;
    private TextView tvStatus;
    private EditText edtLampiran;
    private EditText edtTambahNilai;
    private Button btnBerikaNilai;
    private ImageView backButton;
    private Uri selectedFileUri;
    private String selectedFileName;

    private final ActivityResultLauncher<Intent> filePicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedFileUri = uri;
                        selectedFileName = getFileName(uri);
                        edtLampiran.setText(selectedFileName);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_behind_bank_tugas_guru, container, false);

        initializeViews(view);
        setupDataFromArguments();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        backButton = view.findViewById(R.id.back_Button);
        tvNamaSiswa = view.findViewById(R.id.txtnama);
        tvStatus = view.findViewById(R.id.txtstatus);
        edtLampiran = view.findViewById(R.id.Edt_Lampiran);
        edtTambahNilai = view.findViewById(R.id.Edt_tambahNilai);
        btnBerikaNilai = view.findViewById(R.id.Btn_berikanNilai);

        // Set EditText agar tidak bisa diketik manual
        edtLampiran.setFocusable(false);
        edtLampiran.setClickable(true);
    }

    private void setupDataFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            // Set data dasar menggunakan key yang baru sesuai model
            String nama = args.getString("nama_siswa", ""); // ganti dari "nama" ke "nama_siswa"
            String status = args.getString("status_pengumpulan", "Belum dinilai"); // ganti ke "status_pengumpulan"
            String fileTugas = args.getString("file_tugas", "");
            int idPengumpulan = args.getInt("id_pengumpulan", 0);
            String nilai = args.getString("nilai", "Belum dinilai");

            // Debug log
            Log.d("Fragment Debug", String.format(
                    "Received data: nama_siswa=%s, status=%s, file_tugas=%s, id_pengumpulan=%d, nilai=%s",
                    nama, status, fileTugas, idPengumpulan, nilai
            ));

            // Set text untuk textview
            tvNamaSiswa.setText(nama.isEmpty() ? "Tidak ada nama" : nama);
            tvStatus.setText(status.isEmpty() ? "Belum dinilai" : status);

            // Set nilai jika sudah ada
            if (!nilai.equals("Belum dinilai")) {
                edtTambahNilai.setText(nilai);
                edtTambahNilai.setEnabled(false);
                btnBerikaNilai.setEnabled(false);
                edtLampiran.setEnabled(false);
            }

            // Disable tombol jika id tidak valid
            if (idPengumpulan == 0) {
                Log.e("Fragment Debug", "ID Pengumpulan is invalid!");
                Toast.makeText(getContext(), "Data pengumpulan tidak lengkap", Toast.LENGTH_SHORT).show();
                btnBerikaNilai.setEnabled(false);
            }
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(13, false);
            }
        });

        edtLampiran.setOnClickListener(v -> openFilePicker());

        btnBerikaNilai.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(getContext(), "Pilih file terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            String nilaiStr = edtTambahNilai.getText().toString();
            if (!nilaiStr.isEmpty()) {
                try {
                    float nilai = Float.parseFloat(nilaiStr);
                    if (nilai >= 0 && nilai <= 100) {
                        uploadFileAndNilai(selectedFileUri, nilai);
                    } else {
                        Toast.makeText(getContext(), "Nilai harus antara 0-100", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Masukkan nilai yang valid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Harap isi nilai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (getContext() != null && uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Change mime type to accept all files
        intent.setType("*/*");
        // Add common mime types to filter
        String[] mimeTypes = {
                "application/pdf",
                "image/*",  // All image types
                "application/msword",  // DOC
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOCX
                "application/vnd.ms-excel", // XLS
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // XLSX
                "application/vnd.ms-powerpoint", // PPT
                "application/vnd.openxmlformats-officedocument.presentationml.presentation", // PPTX
                "text/*",  // Text files
                "application/zip" // ZIP files
        };
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            filePicker.launch(Intent.createChooser(intent, "Pilih File"));
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Silakan instal file manager", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidFileType(Uri fileUri) {
        Context context = getContext();
        if (context == null) return false;

        String mimeType = context.getContentResolver().getType(fileUri);
        if (mimeType == null) return false;

        // List of allowed mime type prefixes
        String[] allowedTypes = {
                "application/pdf",
                "image/",
                "application/msword",
                "application/vnd.openxmlformats-officedocument",
                "application/vnd.ms-excel",
                "application/vnd.ms-powerpoint",
                "text/",
                "application/zip"
        };

        for (String type : allowedTypes) {
            if (mimeType.startsWith(type)) {
                return true;
            }
        }
        return false;
    }

    // BehindBankTugas_Guru.java - method uploadFileAndNilai
    private void uploadFileAndNilai(Uri fileUri, float nilai) {
        if (getContext() == null) return;

        try {
            // Get id_pengumpulan from arguments
            Bundle args = getArguments();
            int idPengumpulan = 0; // nilai default untuk integer
            if (args != null) {
                idPengumpulan = args.getInt("id_pengumpulan", 0);
            }

            if (idPengumpulan == 0) {  // cek nilai default
                Toast.makeText(getContext(), "ID Pengumpulan tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create file part if file is selected
            MultipartBody.Part filePart = null;
            if (fileUri != null) {
                File fileToUpload = createTempFileFromUri(fileUri);
                if (fileToUpload != null) {
                    RequestBody requestFile = RequestBody.create(
                            MediaType.parse(getContext().getContentResolver().getType(fileUri)),
                            fileToUpload
                    );
                    filePart = MultipartBody.Part.createFormData("file_nilai", fileToUpload.getName(), requestFile);
                }
            }

            // Create id_pengumpulan part
            RequestBody idPengumpulanPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    String.valueOf(idPengumpulan)    // Konversi ke String
            );

            // Create nilai part
            RequestBody nilaiPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    String.valueOf(nilai)
            );

            // Debug log
            Log.d("Upload Debug", "Sending parameters:");
            Log.d("Upload Debug", "id_pengumpulan: " + idPengumpulan);
            Log.d("Upload Debug", "nilai: " + nilai);
            if (filePart != null) {
                Log.d("Upload Debug", "file included");
            }

            // Make API call
            ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
            Call<ApiResponse> call = apiService.uploadFileAndNilai(idPengumpulanPart, nilaiPart, filePart);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Log.d("API Response", "Code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.d("API Response", "Error: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if ("success".equals(apiResponse.getStatus())) {
                            Toast.makeText(getContext(), "Berhasil memberikan nilai", Toast.LENGTH_SHORT).show();

                            // Update UI
                            tvStatus.setText("Sudah dinilai");
                            edtTambahNilai.setEnabled(false);
                            btnBerikaNilai.setEnabled(false);
                            edtLampiran.setEnabled(false);

                            // Navigate back after delay
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if (getActivity() instanceof DashboardGuru) {
                                    ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                                    viewPager.setCurrentItem(10, false);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(getContext(), "Gagal: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API Error", "Request failed: " + t.getMessage(), t);
                    Toast.makeText(getContext(), "Gagal mengirim, cek koneksi internet", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("Upload Error", "Error: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Gagal mempersiapkan upload", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        if (getContext() == null) return null;

        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        if (inputStream == null) return null;

        // Get the file extension from the Uri
        String fileName = getFileName(uri);
        String extension = "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot >= 0) {
            extension = fileName.substring(lastDot);
        }

        // Create temp file with original extension
        File tempFile = File.createTempFile("upload", extension, getContext().getCacheDir());
        tempFile.deleteOnExit();

        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        String errorBody = "";
        try {
            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
            }
        } catch (IOException e) {
            Log.e("API Error", "Error reading error body: " + e.getMessage());
        }
        Log.e("API Error", "Response failed with code: " + response.code() +
                ", message: " + response.message() +
                ", errorBody: " + errorBody);
        Toast.makeText(getContext(), "Gagal mengirim, silakan coba lagi", Toast.LENGTH_SHORT).show();
    }

    private void handleSuccessResponse() {
        Toast.makeText(getContext(), "Berhasil memberikan nilai", Toast.LENGTH_SHORT).show();

        // Update UI
        tvStatus.setText("Sudah dinilai");
        edtTambahNilai.setEnabled(false);
        btnBerikaNilai.setEnabled(false);
        edtLampiran.setEnabled(false);

        // Navigate back after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(10, false);
            }
        }, 1000);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationHandler = (BottomNavigationHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomNavigationHandler");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
            if (getActivity() != null) {
                ((DashboardGuru) getActivity()).setSwipeEnabled(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }
}