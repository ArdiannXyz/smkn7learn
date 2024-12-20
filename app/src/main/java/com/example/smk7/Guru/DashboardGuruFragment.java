package com.example.smk7.Guru;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.R;

import org.json.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardGuruFragment extends Fragment {

    private TextView txtKelas, txtMateri, txtMapel, txtSiswa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_guru_fragment, container, false);

        // Inisialisasi TextView
        txtKelas = view.findViewById(R.id.txtkelas);
        txtMateri = view.findViewById(R.id.txtmateri);
        txtMapel = view.findViewById(R.id.txtmapel);
        txtSiswa = view.findViewById(R.id.txtsiswa);

        // Inisialisasi LinearLayout untuk tombol-tombol
        LinearLayout linearLayoutMateriGuru = view.findViewById(R.id.uploadmateriguru);
        LinearLayout linearLayoutTugasGuru = view.findViewById(R.id.uploadtugasguru);
        LinearLayout linearLayoutDataTugas = view.findViewById(R.id.datatugas);

        // Set OnClickListener untuk setiap tombol
        linearLayoutMateriGuru.setOnClickListener(v -> {
            Log.d("DashboardGuru", "Tombol Upload Materi diklik");
            openFragment(3);
        });

        linearLayoutTugasGuru.setOnClickListener(v -> {
            Log.d("DashboardGuru", "Tombol Upload Tugas diklik");
            openFragment(4);
        });

        linearLayoutDataTugas.setOnClickListener(v -> {
            Log.d("DashboardGuru", "Tombol Bank Tugas diklik");
            openFragment(5);
        });

        // Ambil data dari server saat fragment dibuat
        fetchDataFromServer();

        return view;
    }

    private void fetchDataFromServer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Menggunakan URL dari Db_Contract
                String apiDashboardUrl = Db_Contract.urlApiDashboard;
                Log.d("DashboardGuru", "URL API: " + apiDashboardUrl);

                // Mengambil response dari API
                String response = HttpRequest.get(apiDashboardUrl).body();
                Log.d("DashboardGuru", "Response API: " + response);

                // Parse response JSON
                JSONObject jsonObject = new JSONObject(response);

                // Cek status response
                String status = jsonObject.getString("status");
                if (!status.equals("sukses")) {
                    throw new Exception("Status API tidak sukses: " + status);
                }

                // Ambil data dari response
                JSONObject data = jsonObject.getJSONObject("data");

                // Ambil jumlah dari masing-masing kategori
                int kelasCount = data.getJSONObject("kelas").getInt("total");
                int materiCount = data.getJSONObject("materi").getInt("total");
                int mapelCount = data.getJSONObject("mapel").getInt("total");
                int siswaCount = data.getJSONObject("users").getInt("total_siswa");

                // Log data yang didapat
                Log.d("DashboardGuru", "Data diterima - Kelas: " + kelasCount +
                        ", Materi: " + materiCount +
                        ", Mapel: " + mapelCount +
                        ", Siswa: " + siswaCount);

                // Update UI di thread utama
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        updateDashboardUI(kelasCount, materiCount, mapelCount, siswaCount);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DashboardGuru", "Error mengambil data: " + e.getMessage());

                // Update UI dengan pesan error jika perlu
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Bisa ditambahkan handling error di sini
                        // Misalnya menampilkan pesan error ke user
                    });
                }
            }
        });
    }

    private void updateDashboardUI(int kelas, int materi, int mapel, int siswa) {
        // Update TextView dengan nilai yang diterima
        if (txtKelas != null) txtKelas.setText(String.valueOf(kelas));
        if (txtMateri != null) txtMateri.setText(String.valueOf(materi));
        if (txtMapel != null) txtMapel.setText(String.valueOf(mapel));
        if (txtSiswa != null) txtSiswa.setText(String.valueOf(siswa));

        Log.d("DashboardGuru", "UI berhasil diupdate");
    }

    private void openFragment(int position) {
        Log.d("DashboardGuru", "Membuka fragment posisi: " + position);
        if (getActivity() instanceof DashboardGuru) {
            // Navigasi ke fragment dengan posisi tertentu tanpa animasi
            ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(position, false);
        }
    }
}