package com.example.smk7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private List<MateriModel> materiList;

    public MateriAdapter(List<MateriModel> materiList) {
        this.materiList = materiList;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout untuk item RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_mapel, parent, false);
        return new MateriViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {

        MateriModel materi = materiList.get(position);
        holder.nama_mapel.setText(materi.getNama_mapel());
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_mapel;

        public MateriViewHolder(View view) {
            super(view);

            nama_mapel = view.findViewById(R.id.txtnama_materi);
        }
    }
}
