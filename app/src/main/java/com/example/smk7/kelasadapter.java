package com.example.smk7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class kelasadapter extends RecyclerView.Adapter<kelasadapter.KelasViewHolder> {
    private List<kelasmodel> kelasList;

    public kelasadapter(List<kelasmodel> kelasList) {
        this.kelasList = kelasList;
    }

    @Override
    public KelasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KelasViewHolder holder, int position) {
        kelasmodel kelas = kelasList.get(position);
        holder.id_Kelas.setText(kelas.getid_kelas());
        holder.namaKelas.setText(kelas.getnama_kelas());
        holder.tahunAjaran.setText(kelas.gettahun_ajar());
        holder.waliKelas.setText(kelas.getwali_kelas());
    }

    @Override
    public int getItemCount() {
        return kelasList.size();
    }

    public static class KelasViewHolder extends RecyclerView.ViewHolder {
        public TextView id_Kelas;
        public TextView namaKelas;
        public TextView tahunAjaran;
        public TextView waliKelas;

        public KelasViewHolder(View view) {
            super(view);
            id_Kelas = view.findViewById(R.id.id_kelas);
            namaKelas = view.findViewById(R.id.nama_kelas);
            tahunAjaran = view.findViewById(R.id.tahun_ajaran);
            waliKelas = view.findViewById(R.id.wali_kelas);
        }
    }
}
