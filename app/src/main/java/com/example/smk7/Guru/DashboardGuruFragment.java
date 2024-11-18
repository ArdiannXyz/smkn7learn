package com.example.smk7.Guru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.smk7.R;

import org.json.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardGuruFragment extends Fragment {

    private TextView txtKelas, txtMateri, txtTugas, txtSiswa;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashgurufragment, container, false);

        // Mengakses TextView dari layout fragment
        txtKelas = view.findViewById(R.id.txtkelas);
        txtMateri = view.findViewById(R.id.txtmateri);
        txtTugas = view.findViewById(R.id.txttugas);
        txtSiswa = view.findViewById(R.id.txtsiswa);

        // Menambahkan event listener untuk linearLayouts
        LinearLayout linearLayoutmateriguru = view.findViewById(R.id.uploadmateriguru);
        LinearLayout linearLayouttugasguru = view.findViewById(R.id.uploadtugasguru);
        LinearLayout linearLayoutdatatugas = view.findViewById(R.id.datatugas);

        linearLayoutmateriguru.setOnClickListener(v -> openFragment(3));
        linearLayouttugasguru.setOnClickListener(v -> openFragment(4));
        linearLayoutdatatugas.setOnClickListener(v -> openFragment(5));

        // Memanggil fungsi untuk mengambil data dari server
        fetchDataFromServer();

        return view;
    }

    private void fetchDataFromServer() {
        // Menjalankan pengambilan data dari API di background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Ganti URL ini dengan endpoint API Anda
                String apiUrl = "http://192.168.25.105/WebNewbieTeam/api_dashboard.php";  // Pastikan API URL sesuai

                // Mengirim request GET dan mengambil respons
                String response = HttpRequest.get(apiUrl).body();

                // Parsing response JSON
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");

                // Ambil jumlah data dari setiap kategori
                int kelasCount = data.getJSONObject("kelas").getInt("total_records");
                int materiCount = data.getJSONObject("materi").getInt("total_records");
                int tugasCount = data.getJSONObject("tugas").getInt("total_records");
                int siswaCount = data.getJSONObject("siswa").getInt("total_records");

                getActivity().runOnUiThread(() -> {

                    if (txtKelas != null) {
                        txtKelas.setText(String.valueOf(kelasCount));  // Pastikan menggunakan String.valueOf untuk int
                    }
                    if (txtMateri != null) {
                        txtMateri.setText(String.valueOf(materiCount));  // Pastikan menggunakan String.valueOf untuk int
                    }
                    if (txtTugas != null) {
                        txtTugas.setText(String.valueOf(tugasCount));  // Pastikan menggunakan String.valueOf untuk int
                    }
                    if (txtSiswa != null) {
                        txtSiswa.setText(String.valueOf(siswaCount));  // Pastikan menggunakan String.valueOf untuk int
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();  // Menangani error jika ada masalah dengan request atau parsing JSON
            }
        });
    }

    private void openFragment(int position) {
        // Pindah ke fragment yang diinginkan di ViewPager2
        ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(position);
    }
}
