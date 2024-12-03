package com.example.smk7.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;
import com.example.smk7.Recyclemateriguru.EditMateri_Guru;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {
    private Context context;
    private List<MateriModel> materiList;
    private OnItemClickListener onItemClickListener;
    private ApiServiceInterface apiService; // Menambahkan ApiServiceInterface

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(MateriModel materiModel);
    }

    // Constructor accepts the list and the listener
    public MateriAdapter(Context context, List<MateriModel> materiList, OnItemClickListener listener, ApiServiceInterface apiService) {
        this.context = context;
        this.materiList = materiList;
        this.onItemClickListener = listener;
        this.apiService = apiService; // Inisialisasi ApiServiceInterface
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout for RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_edit_hapus_materi_guru, parent, false);
        return new MateriViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);

        // Set the text for the item view (name of materi)
        holder.nama_materi.setText(materi.getJudulTugas());

        // Handle item click to open details page
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(materi);
            }
        });

        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMateri_Guru.class);
            intent.putExtra("materi_id", materi.getIdTugas());
            context.startActivity(intent);
        });

        // Handle delete button click
        holder.btnHps.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus materi ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> deleteMateri(materi.getIdTugas()))
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    // ViewHolder to bind the item view
    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_materi;
        public ImageView btnEdit;
        public ImageView btnHps;

        public MateriViewHolder(View view) {
            super(view);
            nama_materi = view.findViewById(R.id.txtnama_mapel);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnHps = view.findViewById(R.id.btn_hps);
        }
    }

    // Method to delete the materi from the server
    private void deleteMateri(int idTugas) {
        apiService.hapusMateri(idTugas).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Hapus item dari daftar dan beri tahu adapter
                    int position = -1;
                    for (int i = 0; i < materiList.size(); i++) {
                        if (materiList.get(i).getIdTugas() == idTugas) {
                            position = i;
                            break;
                        }
                    }
                    if (position != -1) {
                        materiList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Materi berhasil dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Materi tidak ditemukan dalam daftar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tangani error API dengan menampilkan pesan error yang lebih detail
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API Error", "Error deleting materi: " + errorBody);
                        Toast.makeText(context, "Gagal menghapus materi: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("API Error", "Error reading error body: " + e.getMessage());
                        Toast.makeText(context, "Gagal menghapus materi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Tangani error jaringan dengan menampilkan pesan error yang lebih detail
                Log.e("API Error", "Error deleting materi: " + t.getMessage(), t);
                Toast.makeText(context, "Error deleting materi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}