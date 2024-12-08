package com.example.smk7.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.Model.TugasModel;
import com.example.smk7.R;

import java.util.ArrayList;
import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.TugasViewHolder> {

    private List<TugasModel> tugasList;
    private OnItemClickListener onItemClickListener;

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(String judulTugas, int idTugas); // Mengirim judul tugas dan ID tugas
    }

    // Constructor accepts the list and the listener
    public TugasAdapter(List<TugasModel> tugasList, OnItemClickListener listener) {
        // Jika tugasList null, inisialisasi dengan list kosong
        if (tugasList == null) {
            tugasList = new ArrayList<>(); // Menghindari NullPointerException
        }
        this.tugasList = tugasList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout untuk RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_tugas_guru, parent, false);
        return new TugasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasViewHolder holder, int position) {
        TugasModel tugas = tugasList.get(position);

        // Set teks untuk item view (judul tugas)
        holder.tugas.setText(tugas.getJudulTugas());  // Set text menggunakan getJudulTugas()

        // Handle klik item
        holder.itemView.setOnClickListener(v -> {
            // Panggil listener untuk meneruskan judul tugas dan ID tugas yang diklik
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(tugas.getJudulTugas(), tugas.getIdTugas());  // Kirim judul tugas dan ID tugas ke listener
            }
        });
    }

    @Override
    public int getItemCount() {
        // Pastikan tugasList tidak null sebelum memanggil size()
        return tugasList != null ? tugasList.size() : 0;  // Menghindari NullPointerException
    }

    // ViewHolder untuk mengikat item view
    public static class TugasViewHolder extends RecyclerView.ViewHolder {
        public TextView tugas;

        public TugasViewHolder(View view) {
            super(view);
            tugas = view.findViewById(R.id.txtnama_mapel);  // Asumsi ini adalah ID TextView di layout Anda
        }
    }

}
