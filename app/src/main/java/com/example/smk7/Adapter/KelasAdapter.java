package com.example.smk7.Adapter;

import android.content.Context;
import android.os.Handler;
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
import com.example.smk7.RecycleTugasGuru.UploadTugas_guru;
import com.example.smk7.Recyclemateriguru.UploadMateriKelas_Guru;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {

    private List<KelasModel> kelasList;
    private ViewPager2 viewPager;
    private boolean isViewPagerRequired;
    private Fragment currentFragment;

    // Constructor untuk menerima data kelas dan parameter terkait ViewPager dan fragmen aktif
    public KelasAdapter(List<KelasModel> kelasList, ViewPager2 viewPager, boolean isViewPagerRequired, Fragment currentFragment) {
        this.kelasList = kelasList;
        this.viewPager = viewPager;
        this.isViewPagerRequired = isViewPagerRequired;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menginflate layout carditem_kelas
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        // Menentukan data yang akan ditampilkan di RecyclerView
        KelasModel kelas = kelasList.get(position);
        holder.namaKelas.setText(kelas.getNamaKelas());
        holder.waliKelas.setText(kelas.getWaliKelas());

        // Mengatur click listener untuk item RecyclerView
        holder.itemView.setOnClickListener(v -> {
            if (isViewPagerRequired && viewPager != null) {
                // Pastikan currentFragment adalah fragmen yang aktif dan benar
                if (currentFragment != null) {
                    Log.d("Fragment Check", "Current Fragment: " + currentFragment.getClass().getSimpleName());

                    // Periksa fragment aktif dan arahkan ke halaman yang sesuai
                    if (currentFragment instanceof UploadMateriKelas_Guru) {
                        Log.d("FragmentA", "Pindah ke halaman 11...");
                        viewPager.setCurrentItem(11, false); // Pindah ke halaman 11 untuk Fragment A
                    } else if (currentFragment instanceof UploadTugasKelas_Guru) {
                        Log.d("FragmentB", "Pindah ke halaman 12...");
                        viewPager.setCurrentItem(12, false); // Pindah ke halaman 12 untuk Fragment B
                    } else if (currentFragment instanceof BankTugasKelas_Guru) {
                        Log.d("FragmentC", "Pindah ke halaman 13...");
                        viewPager.setCurrentItem(13, false); // Pindah ke halaman 13 untuk Fragment C
                    } else {
                        // Menangani fragmen yang tidak dikenali
                        Log.e("Fragment Error", "Fragment tidak dikenali!");
                        Toast.makeText(v.getContext(), "Fragment tidak dikenali", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.e("Fragment Error", "currentFragment is null!");
                    Toast.makeText(v.getContext(), "Fragment aktif tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Mengembalikan jumlah data kelas
        return kelasList.size();
    }

    // ViewHolder untuk menampilkan item di RecyclerView
    public static class KelasViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKelas;
        public TextView waliKelas;

        // Constructor untuk menyambungkan itemView dengan komponen UI
        public KelasViewHolder(View view) {
            super(view);
            namaKelas = view.findViewById(R.id.nama_kelas);
            waliKelas = view.findViewById(R.id.wali_kelas);
        }
    }
}