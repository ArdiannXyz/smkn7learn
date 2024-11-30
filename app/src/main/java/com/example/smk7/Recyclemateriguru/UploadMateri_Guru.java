package com.example.smk7.Recyclemateriguru;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smk7.ApiDatabase.ApiHelper;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.R;

import org.json.JSONObject;

import java.util.HashMap;

public class UploadMateri_Guru extends AppCompatActivity {

    private static final String TAG = "UploadMateri_Guru";

    private EditText edtJudulMateri, edtLampiran, edtKomentar;
    private TextView tvNamaKelas;
    private Button btnSimpan;
    private ImageView backButton;
    private String idKelas;
    private String namaKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_materi_guru);

        // Menyambungkan view ke komponen UI
        backButton = findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> onBackPressed());

        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        edtKomentar = findViewById(R.id.Edt_Komentar);
        tvNamaKelas = findViewById(R.id.NamaKelas);
        btnSimpan = findViewById(R.id.Btn_simpan);

        // Menerima data dari Intent
        Intent intent = getIntent();
        idKelas = intent.getStringExtra("id_kelas");
        namaKelas = intent.getStringExtra("nama_kelas");

        Log.d(TAG, "ID Kelas diterima: " + idKelas);
        Log.d(TAG, "Nama Kelas diterima: " + namaKelas);

        // Validasi ID Kelas
        if (idKelas == null || idKelas.trim().isEmpty()) {
            idKelas = "0"; // Default value jika ID kelas tidak ditemukan
            Log.e(TAG, "ID Kelas null atau kosong, menggunakan nilai default: " + idKelas);
        }

        // Menampilkan nama kelas
        if (namaKelas != null) {
            tvNamaKelas.setText(namaKelas);
        } else {
            tvNamaKelas.setText("Nama Kelas Tidak Diketahui");
            Log.e(TAG, "Nama kelas tidak ditemukan dalam Intent.");
        }

        // Setup listener untuk tombol Simpan
        btnSimpan.setOnClickListener(v -> uploadMateri());
    }

    private void uploadMateri() {
        // Mendapatkan data dari EditText
        String judulMateri = edtJudulMateri.getText().toString().trim();
        String jenisMateri = edtLampiran.getText().toString().trim();
        String komentar = edtKomentar.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(judulMateri) || TextUtils.isEmpty(jenisMateri) || TextUtils.isEmpty(komentar)) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Field kosong saat validasi.");
            return;
        }

        // Validasi ID Kelas
        if (idKelas == null || idKelas.trim().isEmpty()) {
            Toast.makeText(this, "ID Kelas tidak valid!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ID Kelas null atau kosong: " + idKelas);
            return;
        }

        // Menyiapkan parameter untuk dikirim ke API
        HashMap<String, String> params = new HashMap<>();
        params.put("judul_materi", judulMateri);
        params.put("jenis_materi", jenisMateri);
        params.put("komentar", komentar);
        params.put("id_guru", "1"); // Misalnya ID guru hardcoded sebagai 1
        params.put("id_kelas", idKelas);

        Log.d(TAG, "Parameter yang dikirim: " + params);

        // Mengirim data ke server API menggunakan thread terpisah
        new Thread(() -> {
            try {
                String ApiUploadmateri = ApiHelper.post(Db_Contract.urlApiUploadMateri, params);
                Log.d(TAG, "Respons mentah dari API: " + ApiUploadmateri);

                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(ApiUploadmateri);
                        String status = jsonResponse.getString("status");

                        if ("success".equals(status)) {
                            Toast.makeText(this, "Materi berhasil diupload", Toast.LENGTH_SHORT).show();
                            // Kembali ke activity sebelumnya
                            onBackPressed();
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error uploading materi: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Gagal mengupload materi", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
