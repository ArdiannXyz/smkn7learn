package com.example.smk7.Adapter;

import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
        holder.nama_kelas.setText(materi.getNamaKelas());

        // Handle item click to open details page
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(materi);
            }
        });

        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMateri_Guru.class);
            intent.putExtra("id_tugas", materi.getIdTugas());
            intent.putExtra("id_kelas", materi.getIdKelas());
            intent.putExtra("nama_kelas", materi.getNamaKelas());
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
        public TextView nama_kelas;
        public ImageView btnEdit;
        public ImageView btnHps;

        public MateriViewHolder(View view) {
            super(view);
            nama_materi = view.findViewById(R.id.txtnama_mapel);
            nama_kelas = view.findViewById(R.id.txt_nama_mapel);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnHps = view.findViewById(R.id.btn_hps);
        }
    }

    // Method to delete the materi from the server
    private void deleteMateri(int idTugas) {
        Log.d("Delete Materi", "Menghapus Materi dengan ID: " + idTugas);
        // Validasi ID di sisi client
        if (idTugas <= 0) {
            Log.e("Delete Materi", "Invalid ID: " + idTugas);
            Toast.makeText(context, "ID Materi tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }
        // Buat objek JSON untuk dikirim
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id_tugas", idTugas);
        } catch (JSONException e) {
            Log.e("Delete Materi", "Error membuat JSON", e);
            return;
        }

        // Convert JSONObject to RequestBody
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                jsonParams.toString()
        );

        // Tambahkan loading indicator jika perlu
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Menghapus materi...");
        progressDialog.show();

        apiService.hapusMateri(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.d("Delete Materi", "Response Code: " + response.code());

                try {
                    String responseBodyString = response.body() != null ? response.body().string() : "Body kosong";
                    Log.d("Delete Materi", "Response Body: " + responseBodyString);

                    if (response.isSuccessful()) {
                        // Parse response JSON
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String message = jsonResponse.getString("message");

                        // Cari dan hapus item dari list
                        int position = findMateriPositionById(idTugas);
                        if (position != -1) {
                            materiList.remove(position);
                            notifyItemRemoved(position);

                            // Logging
                            Log.i("Delete Materi", "Materi berhasil dihapus: " + idTugas);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorBodyString = response.errorBody() != null ? response.errorBody().string() : "Error body kosong";
                            Log.e("Delete Materi", "Error Body: " + errorBodyString);
                            Log.w("Delete Materi", "Materi tidak ditemukan di list: " + idTugas);
                            Toast.makeText(context, "Materi tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Tangani error response dari server
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("error");

                        Log.e("Delete Materi", "Server Error: " + errorMessage);
                        Toast.makeText(context, "Gagal menghapus: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Delete Materi", "Parsing Error", e);
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

                // Logging error jaringan
                Log.e("Delete Materi", "Network Error", t);
                Toast.makeText(context, "Gagal terhubung: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode tambahan untuk mencari posisi materi berdasarkan ID
    private int findMateriPositionById(int idTugas) {
        for (int i = 0; i < materiList.size(); i++) {
            Log.d("Delete Materi", "Comparing: " + materiList.get(i).getIdTugas() + " with " + idTugas);
            if (materiList.get(i).getIdTugas() == idTugas) {
                return i;
            }
        }
        return -1;
    }
}