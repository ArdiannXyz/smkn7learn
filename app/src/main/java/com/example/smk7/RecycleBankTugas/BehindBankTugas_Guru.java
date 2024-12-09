package com.example.smk7.RecycleBankTugas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
            String nama = args.getString("nama", "");
            String status = args.getString("status", "Belum dinilai");

            tvNamaSiswa.setText(nama);
            tvStatus.setText(status);
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(10, false);
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            filePicker.launch(Intent.createChooser(intent, "Pilih file PDF"));
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Silakan instal file manager", Toast.LENGTH_SHORT).show();
        }
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

    private void uploadFileAndNilai(Uri fileUri, float nilai) {
        if (getContext() == null) return;

        try {
            File fileToUpload = createTempFileFromUri(fileUri);
            if (fileToUpload == null) {
                Toast.makeText(getContext(), "Gagal mempersiapkan file", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(getContext().getContentResolver().getType(fileUri)),
                    fileToUpload
            );

            MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                    "file",
                    fileToUpload.getName(),
                    requestFile
            );

            RequestBody nilaiPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    String.valueOf(nilai)
            );

            ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
            Call<ApiResponse> call = apiService.uploadFileAndNilai(filePart, nilaiPart);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if ("success".equals(apiResponse.getStatus())) {
                            Toast.makeText(getContext(), "File dan nilai berhasil dikirim", Toast.LENGTH_SHORT).show();
                            tvStatus.setText("Sudah dinilai");
                            edtTambahNilai.setEnabled(false);
                            btnBerikaNilai.setEnabled(false);
                            edtLampiran.setEnabled(false);
                        } else {
                            Toast.makeText(getContext(), "Gagal mengirim: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API Error", "Gagal mengirim: " + t.getMessage(), t);
                    Toast.makeText(getContext(), "Gagal mengirim, silakan coba lagi", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("Upload Error", "Error preparing upload: " + e.getMessage());
            Toast.makeText(getContext(), "Gagal mempersiapkan upload", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        if (getContext() == null) return null;

        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        if (inputStream == null) return null;

        File tempFile = File.createTempFile("upload", ".pdf", getContext().getCacheDir());
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

    // Lifecycle methods remain the same
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