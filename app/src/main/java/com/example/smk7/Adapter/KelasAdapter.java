package com.example.smk7.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;

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
        holder.namaKelas.setText(kelas.getNama_kelas());
        holder.waliKelas.setText(kelas.getWali_kelas());

        holder.itemView.setOnClickListener(v -> {
            if (isViewPagerRequired && viewPager != null) {
                // Jika ViewPager diperlukan, pindahkan ke halaman yang sesuai
                viewPager.setCurrentItem(9, true);
            } else {
                // Tidak ada aksi saat item diklik jika ViewPager tidak diperlukan
                // Kamu bisa menambahkan aksi lain jika diperlukan
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
