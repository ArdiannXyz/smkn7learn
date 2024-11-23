package com.example.smk7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {
    private List<ClassModel> kelasList;

    public KelasAdapter(List<ClassModel> kelasList) {
        this.kelasList = kelasList;
    }
    @Override
    public KelasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KelasViewHolder holder, int position) {
        ClassModel kelas = kelasList.get(position);
        holder.namaKelas.setText(kelas.getnama_kelas());
        holder.waliKelas.setText(kelas.getwali_kelas());
    }

    @Override
    public int getItemCount() {
        return kelasList.size();
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