package com.example.smk7.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;

import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private List<MateriModel> materiList;
    private OnItemClickListener onItemClickListener;

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(String namaMateri);
    }

    // Constructor accepts the list and the listener
    public MateriAdapter(List<MateriModel> materiList, OnItemClickListener listener) {
        this.materiList = materiList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout for RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_upload_materi, parent, false);
        return new MateriViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);

        // Set the text for the item view (name of materi)
        holder.nama_materi.setText(materi.getJudulTugas());  // Set text using getJudulTugas()

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            // Call the listener to pass the clicked materi name
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(materi.getJudulTugas());  // Pass name to listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    // ViewHolder to bind the item view
    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_materi;

        public MateriViewHolder(View view) {
            super(view);
            nama_materi = view.findViewById(R.id.txtnama_mapel);  // Assuming this is the TextView ID in your layout
        }
    }
}
