package com.example.smk7.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Model.TugasModel;
import com.example.smk7.R;
import com.example.smk7.RecycleTugasGuru.EditTugas_Guru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.TugasViewHolder> {

    private List<TugasModel> tugasList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(String judulTugas, int idTugas);
        void onDeleteSuccess();
    }

    public TugasAdapter(Context context, List<TugasModel> tugasList, OnItemClickListener listener) {
        this.context = context;
        this.tugasList = tugasList != null ? tugasList : new ArrayList<>();
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carditem_tugas_guru, parent, false);
        return new TugasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasViewHolder holder, int position) {
        TugasModel tugas = tugasList.get(position);

        // Set judul tugas
        holder.txtnamaTugas.setText(tugas.getJudulTugas());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(tugas.getJudulTugas(), tugas.getIdTugas());
            }
        });

        // Handle edit click
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTugas_Guru.class);
            intent.putExtra("id_tugas", tugas.getIdTugas());
            intent.putExtra("judul_tugas", tugas.getJudulTugas());
            intent.putExtra("nama_kelas", tugas.getNamaKelas());
            intent.putExtra("deskripsi", tugas.getDeskripsi());
            intent.putExtra("deadline", tugas.getDeadline());
            intent.putExtra("file_tugas", tugas.getFileTugas());
            context.startActivity(intent);
        });

        // Handle delete click
        holder.btnHapus.setOnClickListener(v -> {
            showDeleteConfirmation(tugas.getIdTugas(), position);
        });
    }

    private void showDeleteConfirmation(int idTugas, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus tugas ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    deleteTugas(idTugas, position);
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteTugas(int idTugas, int position) {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("id_tugas", idTugas);

        Call<ApiResponse> call = apiService.hapusTugas(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("sukses".equals(response.body().getStatus())) {
                        tugasList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tugasList.size());
                        if (onItemClickListener != null) {
                            onItemClickListener.onDeleteSuccess();
                        }
                        Toast.makeText(context, "Tugas berhasil dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Gagal menghapus tugas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tugasList.size();
    }

    public static class TugasViewHolder extends RecyclerView.ViewHolder {
        TextView txtnamaTugas;
        ImageView btnEdit, btnHapus;

        public TugasViewHolder(View view) {
            super(view);
            txtnamaTugas = view.findViewById(R.id.txtnama_tugas);
            btnEdit = view.findViewById(R.id.btn_editTugas);
            btnHapus = view.findViewById(R.id.btn_hapusTugas);
        }
    }
}