package com.example.smk7.Guru.Adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Guru.Model.BankTugasModel;
import com.example.smk7.R;

import java.util.List;

public class BankTugasAdapter extends RecyclerView.Adapter<BankTugasAdapter.BanktugasViewHolder> {

    private List<BankTugasModel> bankTugasList;  // Perbaiki penamaan variabel sesuai constructor
    private ViewPager2 viewPager;

    // Constructor tanpa listener
    public BankTugasAdapter(List<BankTugasModel> bankTugasList, ViewPager2 viewPager) {
        this.bankTugasList = bankTugasList;
        this.viewPager = viewPager;
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
        BankTugasModel tugas = bankTugasList.get(position);  // Gunakan bankTugasList di sini
        holder.tvNama.setText(tugas.getNama());
        holder.tvStatus.setText(tugas.getStatus());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            // Disable swipe sementara pada ViewPager2 untuk mencegah interaksi selama perpindahan fragment
            viewPager.setUserInputEnabled(false);
            new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);

            // Pindah ke fragment sesuai dengan index yang ditentukan
            viewPager.setCurrentItem(14, false);  // false berarti tanpa animasi
        });
    }

    @Override
    public int getItemCount() {
        return bankTugasList != null ? bankTugasList.size() : 0;  // Menambahkan pengecekan null
    }

    // ViewHolder
    public static class BanktugasViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
        TextView tvStatus;

        public BanktugasViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.txtnama);
            tvStatus = itemView.findViewById(R.id.txtstatus);
        }
    }
}
