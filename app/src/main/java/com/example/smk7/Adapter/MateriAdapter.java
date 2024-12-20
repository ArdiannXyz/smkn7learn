package com.example.smk7.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.smk7.Recyclemateriguru.UploadMateri_Guru;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {
    private static final String TAG = "MateriAdapter";
    private Context context;
    private List<MateriModel> materiList;
    private OnItemClickListener onItemClickListener;
    private ApiServiceInterface apiService;

    public interface OnItemClickListener {
        void onItemClick(MateriModel materiModel);
    }

    public MateriAdapter(Context context, List<MateriModel> materiList, OnItemClickListener listener, ApiServiceInterface apiService) {
        this.context = context;
        this.materiList = materiList;
        this.onItemClickListener = listener;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carditem_edit_hapus_materi_guru, parent, false);
        return new MateriViewHolder(itemView);
    }

    // Di dalam MateriAdapter.java, ubah method getMateriById:

    private void showDeleteDialog(int idMateri) {
        if (idMateri <= 0) {
            Log.e(TAG, "Invalid ID Materi: " + idMateri);
            Toast.makeText(context, "ID Materi tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus materi ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    Log.d(TAG, "Attempting to delete materi with ID: " + idMateri);
                    deleteMateri(idMateri);
                })
                .setNegativeButton("Batal", null)
                .show();
    }
    private void deleteMateri(int idMateri) {
        if (idMateri <= 0) {
            Log.e(TAG, "Invalid ID Materi");
            Toast.makeText(context, "ID Materi tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tampilkan loading dialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Menghapus materi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id_materi", idMateri);
            Log.d(TAG, "Delete request params: " + jsonParams.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON", e);
            progressDialog.dismiss();
            Toast.makeText(context, "Terjadi kesalahan sistem", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonParams.toString()
        );

        apiService.hapusMateri(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseStr = response.body().string();
                        Log.d(TAG, "Server response: " + responseStr);

                        JSONObject jsonResponse = new JSONObject(responseStr);
                        if (jsonResponse.optBoolean("success", false)) {
                            // Remove the item from the list and update RecyclerView
                            int position = findMateriPositionById(idMateri);
                            if (position != -1) {
                                materiList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, materiList.size());
                            }
                            Toast.makeText(context, jsonResponse.optString("message", "Materi berhasil dihapus"), Toast.LENGTH_SHORT).show();
                        } else {
                            String message = jsonResponse.optString("message", "Gagal menghapus materi");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error response: " + errorBody);
                        Toast.makeText(context, "Gagal menghapus materi", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response", e);
                    Toast.makeText(context, "Terjadi kesalahan sistem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Network error", t);
                Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);

        // Set data ke views
        holder.nama_materi.setText(materi.getJudulMateri());
        holder.nama_kelas.setText(materi.getNamaKelas());

        // Edit button click - langsung ke EditMateri_Guru
        holder.btnEdit.setOnClickListener(v -> {
            launchEditMateri(materi);
        });

        // Upload button click - langsung ke UploadMateri_Guru
        holder.btnHps.setOnClickListener(v -> {
            Log.d(TAG, "Delete clicked for ID: " + materi.getIdMateri());
            showDeleteDialog(materi.getIdMateri());
        });
    }

//    private void showEditOptionsDialog(MateriModel materi) {
//        String[] options = {"Edit Materi", "Upload Ulang Materi"};
//
//        new AlertDialog.Builder(context)
//                .setTitle("Pilih Aksi")
//                .setItems(options, (dialog, which) -> {
//                    switch (which) {
//                        case 0: // Edit Materi
//                            launchEditMateri(materi);
//                            break;
//                        case 1: // Upload Ulang
//                            launchUploadMateri(materi);
//                            break;
//                    }
//                })
//                .show();
//    }

    private void launchEditMateri(MateriModel materi) {
        Intent intent = new Intent(context, EditMateri_Guru.class);
        intent.putExtra("id_materi", materi.getIdMateri());
        intent.putExtra("id_mapel", materi.getIdMapel());
        intent.putExtra("id_kelas", materi.getIdKelas());
        intent.putExtra("id_guru", materi.getIdGuru());
        intent.putExtra("judul_materi", materi.getJudulMateri());
        intent.putExtra("deskripsi", materi.getDeskripsi());
        intent.putExtra("file_materi", materi.getFileMateri());
        intent.putExtra("nama_kelas", materi.getNamaKelas());

        Log.d(TAG, "Launching EditMateri_Guru - ID: " + materi.getIdMateri() +
                ", Judul: " + materi.getJudulMateri());

        context.startActivity(intent);
    }

    private void launchUploadMateri(MateriModel materi) {
        // Convert int to String when getting ID Guru
        String idGuru = String.valueOf(materi.getIdGuru());

        if (idGuru == null || idGuru.isEmpty()) {
            Toast.makeText(context, "Error: ID Guru tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(context, UploadMateri_Guru.class);
        intent.putExtra("id_guru", idGuru);
        intent.putExtra("id_kelas", String.valueOf(materi.getIdKelas()));
        intent.putExtra("id_mapel", String.valueOf(materi.getIdMapel()));
        intent.putExtra("nama_kelas", materi.getNamaKelas());
        intent.putExtra("is_edit_mode", true);
        intent.putExtra("id_materi", materi.getIdMateri());

        Log.d(TAG, "Launching UploadMateri_Guru for edit - ID Guru: " + idGuru +
                ", ID Materi: " + materi.getIdMateri());

        context.startActivity(intent);
    }

    private void handleDeleteResponse(Response<ResponseBody> response, int idMateri) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Log.d(TAG, "Response Body: " + responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);
                String status = jsonResponse.getString("status");

                if ("success".equals(status)) {
                    int position = findMateriPositionById(idMateri);
                    if (position != -1) {
                        materiList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, materiList.size());
                        Toast.makeText(context, "Berhasil menghapus materi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String message = jsonResponse.optString("message", "Gagal menghapus materi");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                Log.e(TAG, "Error Response: " + errorBody);
                Toast.makeText(context, "Gagal menghapus materi", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            Toast.makeText(context, "Terjadi kesalahan sistem", Toast.LENGTH_SHORT).show();
        }
    }

    private int findMateriPositionById(int idMateri) {
        for (int i = 0; i < materiList.size(); i++) {
            if (materiList.get(i).getIdMateri() == idMateri) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return materiList != null ? materiList.size() : 0;
    }

    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_materi;
        public TextView nama_kelas;
        public ImageView btnEdit;
        public ImageView btnHps;

        public MateriViewHolder(View view) {
            super(view);
            nama_materi = view.findViewById(R.id.txtnama_mapel);
            nama_kelas = view.findViewById(R.id.txt_nama_kelas);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnHps = view.findViewById(R.id.btn_hps);
        }
    }
}