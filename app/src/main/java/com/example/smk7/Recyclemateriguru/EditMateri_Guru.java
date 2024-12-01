package com.example.smk7.Recyclemateriguru;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.R;

public class EditMateri_Guru extends AppCompatActivity {

    private EditText edtJudulMateri, edtLampiran;
    private TextView txtIdKelas;  // TextView untuk menampilkan id_kelas
    private String idKelas, materiId;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_materi_guru);  // Pastikan ini sesuai dengan layout Activity

        // Initialize the UI components
        edtJudulMateri = findViewById(R.id.Edt_JudulMateri);
        edtLampiran = findViewById(R.id.Edt_Lampiran);
        txtIdKelas = findViewById(R.id.NamaKelas); // Assuming you have this TextView in your layout
        backButton = findViewById(R.id.back_Button); // Button untuk kembali

        // Setup back button action
        backButton.setOnClickListener(v -> {
            // Bisa kembali ke DashboardGuru
            finish(); // Menutup activity ini dan kembali ke activity sebelumnya
        });

        // Get the Intent that started this activity
        Intent intent = getIntent();
        if (intent != null) {
            materiId = intent.getStringExtra("materi_id");  // Get materi ID from Intent
            idKelas = intent.getStringExtra("id_kelas");  // Get id_kelas from Intent

            // Set the id_kelas to the TextView
            txtIdKelas.setText("ID Kelas: " + idKelas);
        }
    }
}
