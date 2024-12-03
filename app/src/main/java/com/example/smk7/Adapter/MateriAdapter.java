package com.example.smk7.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smk7.ApiDatabase.Db_Contract;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;
import com.example.smk7.Recyclemateriguru.EditMateri_Guru;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private List<MateriModel> materiList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(MateriModel materiModel);

    }

    // Constructor accepts the list and the listener
    public MateriAdapter(Context context, List<MateriModel> materiList, OnItemClickListener listener) {
        this.context = context;
        this.materiList = materiList;
        this.onItemClickListener = listener;
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
        holder.nama_materi.setText(materi.getJudulTugas());  // Assuming "getJudulTugas()" is available

        // Handle item click to open details page
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(materi);
            }
        });

        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            // Pass the clicked MateriModel to EditMateri_Guru Activity
            Intent intent = new Intent(context, EditMateri_Guru.class);
            // Assuming the correct method for ID is getIdTugas(), otherwise change accordingly
            intent.putExtra("materi_id", materi.getIdTugas());  // Replace with the actual getter method name for idTugas
            context.startActivity(intent);
        });

        // Handle delete button click
        holder.btnHps.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus materi ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> deleteMateri(materi.getIdTugas()))  // Replace with the actual getter method name for idTugas
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
            nama_materi = view.findViewById(R.id.txtnama_mapel);  // Assuming this is the TextView ID in your layout
            btnEdit = view.findViewById(R.id.btn_edit);  // ImageView for the edit button
            btnHps = view.findViewById(R.id.btn_hps);  // ImageView for the delete button
        }
    }

    // Method to delete the materi from the server
    private void deleteMateri(String idTugas) {
        // Periksa apakah idTugas valid
        if (idTugas == null || idTugas.isEmpty()) {
            Toast.makeText(context, "ID Tugas tidak valid", Toast.LENGTH_SHORT).show(); // Tambahkan show()
            return;  // Jangan lanjutkan jika idTugas tidak valid
        }

        // Menggunakan URL dari Db_Contract untuk API deleteMateri
        String ApiCrud = Db_Contract.urlApiCrudMateri + "?action=deleteMateri";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiCrud,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            // Hapus item dari daftar dan beri tahu adapter
                            materiList.removeIf(materi -> materi.getIdTugas().equals(idTugas));  // Sesuaikan pengecekan ID
                            notifyDataSetChanged();
                            Toast.makeText(context, "Materi berhasil dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus materi", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Terjadi kesalahan saat menghapus materi", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Log kesalahan untuk debugging
                    error.printStackTrace();
                    Toast.makeText(context, "Error deleting materi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tugas", idTugas);  // Pastikan idTugas tidak null di sini
                return params;
            }
        };

        // Tambahkan permintaan ke antrean permintaan
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}