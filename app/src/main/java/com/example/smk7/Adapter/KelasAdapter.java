package com.example.smk7.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;
import com.example.smk7.RecycleBankTugas.BankTugasKelas_Guru;
import com.example.smk7.RecycleTugasGuru.UploadTugasKelas_Guru;
import com.example.smk7.Recyclemateriguru.UploadMateriKelas_Guru;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {
    private static final String TAG = "KelasAdapter";
    private List<KelasModel> kelasList;
    private ViewPager2 viewPager;
    private boolean isViewPagerRequired;
    private Fragment currentFragment;

    public KelasAdapter(List<KelasModel> kelasList, ViewPager2 viewPager, boolean isViewPagerRequired, Fragment currentFragment) {
        this.kelasList = kelasList;
        this.viewPager = viewPager;
        this.isViewPagerRequired = isViewPagerRequired;
        this.currentFragment = currentFragment;
        Log.d(TAG, "KelasAdapter initialized with " + (kelasList != null ? kelasList.size() : 0) + " items");
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        KelasModel kelas = kelasList.get(position);

        // Set nama kelas
        holder.namaKelas.setText(kelas.getNamaKelas());

        // Handle wali kelas display
        KelasModel.WaliKelas waliKelas = kelas.getWaliKelas();
        if (waliKelas != null && waliKelas.getNama() != null) {
            String waliKelasText = waliKelas.getNama();
            if (waliKelas.getNip() != null && !waliKelas.getNip().isEmpty()) {
                waliKelasText += " (" + waliKelas.getNip() + ")";
            }
            holder.waliKelas.setText(waliKelasText);
            Log.d(TAG, "Setting wali kelas: " + waliKelasText);
        } else {
            holder.waliKelas.setText("Belum ada wali kelas");
            Log.d(TAG, "No wali kelas data available");
        }

        holder.itemView.setOnClickListener(v -> {
            if (isViewPagerRequired && viewPager != null) {
                if (currentFragment != null) {
                    Log.d(TAG, "Current Fragment: " + currentFragment.getClass().getSimpleName());

                    // Simpan ID Kelas untuk digunakan di fragment berikutnya
                    if (kelas.getIdKelas() != null) {
                        Log.d(TAG, "Selected Kelas ID: " + kelas.getIdKelas());
                    }

                    if (currentFragment instanceof UploadMateriKelas_Guru) {
                        Log.d(TAG, "Navigating to Upload Materi page");
                        viewPager.setCurrentItem(11, false);
                    } else if (currentFragment instanceof UploadTugasKelas_Guru) {
                        Log.d(TAG, "Navigating to Upload Tugas page");
                        viewPager.setCurrentItem(12, false);
                    } else if (currentFragment instanceof BankTugasKelas_Guru) {
                        Log.d(TAG, "Navigating to Bank Tugas page");
                        viewPager.setCurrentItem(13, false);
                    } else {
                        Log.e(TAG, "Unknown fragment type: " + currentFragment.getClass().getSimpleName());
                        Toast.makeText(v.getContext(), "Fragment tidak dikenali", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Current fragment is null!");
                    Toast.makeText(v.getContext(), "Fragment aktif tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kelasList != null ? kelasList.size() : 0;
    }

    public static class KelasViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKelas;
        public TextView waliKelas;

        public KelasViewHolder(View view) {
            super(view);
            namaKelas = view.findViewById(R.id.nama_kelas);
            waliKelas = view.findViewById(R.id.wali_kelas);
        }
    }
}