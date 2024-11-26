package com.example.smk7.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {

    private List<KelasModel> kelasList;
    private OnItemClickListener listener;

    // Interface untuk menangkap klik item
    public interface OnItemClickListener {
        void onItemClick(String idKelas, String namaKelas);
    }

    // Constructor untuk menerima list data dan listener
    public KelasAdapter(List<KelasModel> kelasList, OnItemClickListener listener) {
        this.kelasList = kelasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        KelasModel kelas = kelasList.get(position);

        // Log untuk memastikan data yang ada di adapter
        Log.d("KelasAdapter", "ID Kelas: " + kelas.getId_kelas() + ", Nama Kelas: " + kelas.getNama_kelas());

        // Set data ke view
        holder.namaKelas.setText(kelas.getNama_kelas());
        holder.waliKelas.setText(kelas.getWali_kelas());

        // Listener untuk item RecyclerView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.d("KelasAdapter", "Mengirim ID Kelas: " + kelas.getId_kelas() + ", Nama Kelas: " + kelas.getNama_kelas());
                listener.onItemClick(kelas.getId_kelas(), kelas.getNama_kelas());
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

        public KelasViewHolder(@NonNull View view) {
            super(view);
            namaKelas = view.findViewById(R.id.nama_kelas);
            waliKelas = view.findViewById(R.id.wali_kelas);
        }
    }
}
