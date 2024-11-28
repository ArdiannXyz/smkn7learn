package com.example.smk7.Guru;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.R;
import com.example.smk7.ClassDataGuru;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;


public class Adapterguru extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<ClassDataGuru> dataList;
    private ViewPager2 viewPager2;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public Adapterguru(Context context, List<ClassDataGuru> dataList, ViewPager2 viewPager2) {
        this.context = context;
        this.dataList = dataList;
        this.viewPager2 = viewPager2;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_data_guru, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClassDataGuru data = dataList.get(position);
        holder.Nip.setText(dataList.get(position).getDataNip());
        holder.NamaGuru.setText(dataList.get(position).getDatanama());
        holder.Email.setText(dataList.get(position).getDataEmail());

        holder.deleteimage.setOnClickListener(v -> deleteData(position));

        holder.imagedit.setOnClickListener(v -> {
            // Menyimpan data ke dalam Bundle
            saveDataToEdit(data);
            // Pindah ke fragment edit (sesuaikan posisi fragment di ViewPager)
            viewPager2.setCurrentItem(11, true); // angka 10 sesuaikan dengan posisi fragment edit Anda
        });


    }

    private void saveDataToEdit(ClassDataGuru data) {
        // Menggunakan SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("EditData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("uid", data.getKey());
        editor.putString("nip", data.getDataNip());
        editor.putString("nama", data.getDatanama());
        editor.putString("email", data.getDataEmail());
        editor.apply();


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void deleteData(int position) {
        ClassDataGuru model = dataList.get(position);
        String uid = model.getDataUid();

        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Tampilkan loading dialog
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Menghapus data...");
                    progressDialog.show();

                    // Gunakan batch write untuk atomic operation
                    WriteBatch batch = db.batch();

                    // Reference untuk dokumen yang akan dihapus
                    DocumentReference userRef = db.collection("users").document(uid);
                    DocumentReference teacherRef = db.collection("teachers").document(uid);

                    // Tambahkan operasi delete ke batch
                    batch.delete(userRef);
                    batch.delete(teacherRef);

                    // Eksekusi batch
                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                // Setelah dokumen Firestore terhapus, hapus dari Authentication
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null && user.getUid().equals(uid)) {
                                    user.delete()
                                            .addOnSuccessListener(aVoid1 -> {
                                                // Update UI
                                                dataList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, dataList.size());

                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Gagal menghapus user: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                }
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Gagal menghapus data: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Tidak", null)
                .show();
    }


    public void searchDataList(List<ClassDataGuru> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView Nip, NamaGuru, Email;
    CardView recCard;
    ImageView imagedit, deleteimage;
    ViewPager viewPager2;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recCard = itemView.findViewById(R.id.recCard);
        Nip = itemView.findViewById(R.id.nip);
        NamaGuru = itemView.findViewById(R.id.namaguru);
        imagedit = itemView.findViewById(R.id.Editimage);
        viewPager2 = itemView.findViewById(R.id.Viewpagerguru);
        Email = itemView.findViewById(R.id.email);
        deleteimage = itemView.findViewById(R.id.Deleteimage);

    }
}


