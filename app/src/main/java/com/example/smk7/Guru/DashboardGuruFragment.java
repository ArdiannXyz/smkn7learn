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

        txtKelas = view.findViewById(R.id.txtkelas);
        txtMateri = view.findViewById(R.id.txtmateri);
        txtMapel = view.findViewById(R.id.txtmapel);
        txtSiswa = view.findViewById(R.id.txtsiswa);

        LinearLayout linearLayoutMateriGuru = view.findViewById(R.id.uploadmateriguru);
        LinearLayout linearLayoutTugasGuru = view.findViewById(R.id.uploadtugasguru);
        LinearLayout linearLayoutDataTugas = view.findViewById(R.id.datatugas);

        linearLayoutMateriGuru.setOnClickListener(v -> {
            System.out.println("Upload Materi button clicked");
            openFragment(5);
        });

        linearLayoutTugasGuru.setOnClickListener(v -> {
            System.out.println("Upload Tugas button clicked");
            openFragment(8);
        });

        linearLayoutDataTugas.setOnClickListener(v -> {
            System.out.println("Bank Tugas button clicked");
            openFragment(9);
        });

        fetchDataFromServer();

        return view;
    }

    private void fetchDataFromServer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {

                // Menggunakan URL dari Db_Contract
                String apiDashboardUrl = Db_Contract.urlApiDashboard;
                Log.d("URL_Dashboard", apiDashboardUrl);

                String response = HttpRequest.get(apiDashboardUrl).body();

                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");

                int kelasCount = data.getJSONObject("kelas").getInt("total_records");
                int materiCount = data.getJSONObject("materi").getInt("total_records");
                int mapelCount = data.getJSONObject("mapel").getInt("total_records");
                int siswaCount = data.getJSONObject("siswa").getInt("total_records");

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        updateDashboardUI(kelasCount, materiCount, mapelCount, siswaCount);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("Error fetching data: " + e.getMessage());
            }
        });
    }


    private void updateDashboardUI(int kelas, int materi, int mapel, int siswa) {
        if (txtKelas != null) txtKelas.setText(String.valueOf(kelas));
        if (txtMateri != null) txtMateri.setText(String.valueOf(materi));
        if (txtMapel != null) txtMapel.setText(String.valueOf(mapel));
        if (txtSiswa != null) txtSiswa.setText(String.valueOf(siswa));
    }

    private void openFragment(int position) {
        System.out.println("Navigating to fragment at position: " + position);
        if (getActivity() instanceof DashboardGuru) {
            // Navigasi ke fragment dengan posisi tertentu tanpa animasi
            ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(position, false);
        }
    }
}
