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
import java.util.List;

public class BankTugasAdapter extends RecyclerView.Adapter<BankTugasAdapter.BanktugasViewHolder> {
    private static final String TAG = "BankTugasAdapter";
    private List<BankTugasModel> banktugasList;
    private ViewPager2 viewPager;
    private Context context;

    // Constructor
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

            // Set teks dengan format yang lebih informatif
            String nama = tugas.getNama() != null ? tugas.getNama() : "Tidak ada nama";
            String status = tugas.getStatus() != null ? tugas.getStatus() : "Belum dinilai";

            // Tambahkan info tambahan jika ada
            if (tugas.getInfoTambahan() != null) {
                String judulTugas = tugas.getInfoTambahan().getJudulTugas();
                String statusPengumpulan = tugas.getInfoTambahan().getStatusPengumpulan();

                holder.tvNama.setText(String.format("%s - %s", nama, judulTugas));
                holder.tvStatus.setText(String.format("%s (%s)", status, statusPengumpulan));
            } else {
                holder.tvNama.setText(nama);
                holder.tvStatus.setText(status);
            }

            // Log untuk debugging
            Log.d(TAG, String.format("Data pada posisi %d: nama='%s', status='%s'",
                    position, nama, status));

            holder.itemView.setOnClickListener(v -> {
                if (viewPager != null) {
                    viewPager.setUserInputEnabled(false);
                    new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);
                }

                if (v.getContext() instanceof DashboardGuru) {
                    DashboardGuru activity = (DashboardGuru) v.getContext();

                    Bundle bundle = new Bundle();
                    bundle.putString("nama", tugas.getNama());
                    bundle.putString("status", tugas.getStatus());
                    bundle.putString("file_tugas", tugas.getFileTugas());
                    bundle.putInt("id_pengumpulan", tugas.getIdPengumpulan());

                    // Tambahkan info tambahan ke bundle
                    if (tugas.getInfoTambahan() != null) {
                        bundle.putString("judul_tugas", tugas.getInfoTambahan().getJudulTugas());
                        bundle.putString("deadline", tugas.getInfoTambahan().getDeadline());
                        bundle.putString("kelas", tugas.getInfoTambahan().getKelas());
                        bundle.putString("mapel", tugas.getInfoTambahan().getMapel());
                        bundle.putString("status_pengumpulan", tugas.getInfoTambahan().getStatusPengumpulan());
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

    // Method untuk update data
    public void updateData(List<BankTugasModel> newData) {
        this.banktugasList = newData != null ? newData : new ArrayList<>();
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