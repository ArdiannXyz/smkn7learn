package com.example.smk7.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;
import com.example.smk7.Recyclemateriguru.UploadMateri_Guru;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {

    private List<KelasModel> kelasList;
    private ViewPager2 viewPager;
    private boolean isViewPagerRequired;  // Flag untuk menentukan apakah ViewPager diperlukan

    // Constructor dengan flag isViewPagerRequired
    public KelasAdapter(List<KelasModel> kelasList, ViewPager2 viewPager, boolean isViewPagerRequired) {
        this.kelasList = kelasList;
        this.viewPager = viewPager;
        this.isViewPagerRequired = isViewPagerRequired;
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
        holder.namaKelas.setText(kelas.getNamaKelas());
        holder.waliKelas.setText(kelas.getWaliKelas());

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            if (context instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) context).viewPager2;

                // Nonaktifkan input swipe sementara
                viewPager.setUserInputEnabled(false);

                // Pindahkan langsung ke halaman yang diinginkan (halaman 6)
                viewPager.setCurrentItem(7, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);  // 300 ms cukup untuk memastikan transisi selesai
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
