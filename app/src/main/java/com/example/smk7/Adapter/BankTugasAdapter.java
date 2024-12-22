package com.example.smk7.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.BankTugasModel;
import com.example.smk7.R;
import com.example.smk7.RecycleBankTugas.BehindBankTugas_Guru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BankTugasAdapter extends RecyclerView.Adapter<BankTugasAdapter.BanktugasViewHolder> {
    private static final String TAG = "BankTugasAdapter";
    private List<BankTugasModel> banktugasList;
    private ViewPager2 viewPager;
    private Context context;

    public BankTugasAdapter(List<BankTugasModel> tugasList, ViewPager2 viewPager, Context context) {
        this.banktugasList = tugasList != null ? tugasList : new ArrayList<>();
        this.viewPager = viewPager;
        this.context = context;
    }

    @NonNull
    @Override
    public BanktugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carditem_bank_tugas, parent, false);
        return new BanktugasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanktugasViewHolder holder, int position) {
        try {
            BankTugasModel tugas = banktugasList.get(position);

            // Set nama siswa
            String namaSiswa = tugas.getNamaSiswa();
            holder.tvNama.setText(namaSiswa);

            // Set status
            String nilai = tugas.getNilai();
            String status = tugas.getStatusPengumpulan();
            holder.tvStatus.setText(status);

            // Log untuk debugging
            Log.d(TAG, String.format("Data pada posisi %d: nama='%s', status='%s', nilai='%s'",
                    position + 1, namaSiswa, status, nilai));

            holder.itemView.setOnClickListener(v -> {
                if (viewPager != null) {
                    viewPager.setUserInputEnabled(false);
                    new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);
                }

                if (v.getContext() instanceof DashboardGuru) {
                    DashboardGuru activity = (DashboardGuru) v.getContext();

                    Bundle bundle = new Bundle();
                    bundle.putInt("id_pengumpulan", tugas.getIdPengumpulan());
                    bundle.putInt("id_siswa", tugas.getIdSiswa());
                    bundle.putString("nama_siswa", tugas.getNamaSiswa());
                    bundle.putString("nilai", tugas.getNilai());
                    bundle.putString("komentar", tugas.getKomentar());
                    bundle.putString("status_pengumpulan", tugas.getStatusPengumpulan());
                    bundle.putString("file_tugas", tugas.getFileTugas());

                    // Tambahkan info tugas
                    if (tugas.getTugas() != null) {
                        bundle.putInt("id_tugas", tugas.getTugas().getIdTugas());
                        bundle.putString("judul_tugas", tugas.getTugas().getJudulTugas());
                        bundle.putString("deadline", tugas.getTugas().getDeadline());
                    }

                    // Tambahkan info kelas
                    if (tugas.getKelas() != null) {
                        bundle.putInt("id_kelas", tugas.getKelas().getIdKelas());
                        bundle.putString("nama_kelas", tugas.getKelas().getNamaKelas());
                    }

                    // Tambahkan info mapel
                    if (tugas.getMapel() != null) {
                        bundle.putInt("id_mapel", tugas.getMapel().getIdMapel());
                        bundle.putString("kode_mapel", tugas.getMapel().getKodeMapel());
                        bundle.putString("nama_mapel", tugas.getMapel().getNamaMapel());
                    }

                    Fragment behindFragment = new BehindBankTugas_Guru();
                    behindFragment.setArguments(bundle);

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, behindFragment)
                            .addToBackStack(null)
                            .commit();

                    if (viewPager != null) {
                        viewPager.setCurrentItem(14, false);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error di onBindViewHolder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return banktugasList != null ? banktugasList.size() : 0;
    }

    public void updateData(List<BankTugasModel> newData) {
        this.banktugasList = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Method baru untuk menghilangkan duplikat
    public void setData(List<BankTugasModel> newData) {
        if (newData == null) {
            this.banktugasList = new ArrayList<>();
        } else {
            // Menghilangkan duplikat menggunakan LinkedHashMap untuk mempertahankan urutan
            Map<String, BankTugasModel> uniqueMap = new LinkedHashMap<>();

            for (BankTugasModel data : newData) {
                // Skip jika data null
                if (data == null || data.getNamaSiswa() == null) continue;

                // Gunakan nama siswa sebagai key
                String key = data.getNamaSiswa();
                BankTugasModel existing = uniqueMap.get(key);

                // Logika untuk memutuskan data mana yang dipertahankan
                boolean shouldUpdate = false;
                if (existing == null) {
                    shouldUpdate = true;
                } else if (existing.getNilai().equals("Belum dinilai") && !data.getNilai().equals("Belum dinilai")) {
                    shouldUpdate = true;
                } else if (data.getIdPengumpulan() > existing.getIdPengumpulan()) {
                    shouldUpdate = true;
                }

                if (shouldUpdate) {
                    uniqueMap.put(key, data);
                    // Log untuk debugging
                    Log.d(TAG, String.format("Update data: nama='%s', nilai='%s', id_pengumpulan='%d'",
                            data.getNamaSiswa(), data.getNilai(), data.getIdPengumpulan()));
                }
            }

            // Convert map values ke list
            this.banktugasList = new ArrayList<>(uniqueMap.values());

            // Log hasil akhir untuk debugging dengan posisi dimulai dari 1
            for (int i = 0; i < banktugasList.size(); i++) {
                BankTugasModel tugas = banktugasList.get(i);
                Log.d(TAG, String.format("Data pada posisi %d: nama='%s', status='%s', nilai='%s'",
                        i + 1, tugas.getNamaSiswa(), tugas.getStatusPengumpulan(), tugas.getNilai()));
            }

            Log.d(TAG, String.format("Total data setelah remove duplikat: %d", banktugasList.size()));
        }

        notifyDataSetChanged();
    }

    public static class BanktugasViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvStatus;

        public BanktugasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.txtnama);
            tvStatus = itemView.findViewById(R.id.txtstatus);
        }
    }
}