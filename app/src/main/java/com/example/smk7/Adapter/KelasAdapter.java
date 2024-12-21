package com.example.smk7.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;
import com.example.smk7.RecycleBankTugas.BankTugasKelas_Guru;
import com.example.smk7.RecycleTugasGuru.UploadTugasKelas_Guru;
import com.example.smk7.Recyclemateriguru.UploadMateriKelas_Guru;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {
    private static final String TAG = "KelasAdapter";
    private List<KelasModel> kelasList;
    private ViewPager2 viewPager;
    private boolean isViewPagerRequired;
    private Fragment currentFragment;

    public KelasAdapter(List<KelasModel> kelasList, ViewPager2 viewPager, boolean isViewPagerRequired, Fragment currentFragment) {
        this.kelasList = kelasList;
        this.viewPager = viewPager;
        this.isViewPagerRequired = isViewPagerRequired;
        this.currentFragment = currentFragment;
        Log.d(TAG, "KelasAdapter initialized with " + (kelasList != null ? kelasList.size() : 0) + " items");
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_kelas, parent, false);
        return new KelasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        KelasModel kelas = kelasList.get(position);

        // Set nama kelas
        if (kelas.getNamaKelas() != null) {
            holder.namaKelas.setText(kelas.getNamaKelas() +
                    (kelas.getTahunAjaran() != null ? " (" + kelas.getTahunAjaran() + ")" : ""));
        } else {
            holder.namaKelas.setText("Nama Kelas Tidak Tersedia");
        }

        // Handle wali kelas display
        KelasModel.WaliKelas waliKelas = kelas.getWaliKelas();
        if (waliKelas != null) {
            StringBuilder waliKelasText = new StringBuilder();

            if (waliKelas.getNama() != null && !waliKelas.getNama().isEmpty()) {
                waliKelasText.append(waliKelas.getNama());

                if (waliKelas.getNip() != null && !waliKelas.getNip().isEmpty()) {
                    waliKelasText.append(" (").append(waliKelas.getNip()).append(")");
                }

                holder.waliKelas.setText(waliKelasText.toString());
                Log.d(TAG, "Wali Kelas: " + waliKelasText);
            } else {
                holder.waliKelas.setText("Wali Kelas Tidak Tersedia");
            }
        } else {
            holder.waliKelas.setText("Belum Ada Wali Kelas");
            Log.d(TAG, "Data wali kelas kosong");
        }

        holder.itemView.setOnClickListener(v -> {
            if (isViewPagerRequired && viewPager != null && currentFragment != null) {
                try {
                    String idKelas = kelas.getIdKelas();
                    if (idKelas != null) {
                        Log.d(TAG, "Kelas terpilih - ID: " + idKelas);
                        // Simpan ID kelas ke preferences atau bundle jika diperlukan

                        navigateBasedOnFragment(currentFragment);
                    } else {
                        Log.e(TAG, "ID Kelas tidak valid");
                        Toast.makeText(v.getContext(), "Data kelas tidak valid",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error saat navigasi: " + e.getMessage());
                    Toast.makeText(v.getContext(), "Terjadi kesalahan",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateBasedOnFragment(Fragment fragment) {
        if (fragment instanceof UploadMateriKelas_Guru) {
            viewPager.setCurrentItem(11, false);
        } else if (fragment instanceof UploadTugasKelas_Guru) {
            viewPager.setCurrentItem(12, false);
        } else if (fragment instanceof BankTugasKelas_Guru) {
            viewPager.setCurrentItem(13, false);
        } else {
            Log.e(TAG, "Tipe fragment tidak dikenali: " + fragment.getClass().getSimpleName());
        }
    }

    @Override
    public int getItemCount() {
        return kelasList != null ? kelasList.size() : 0;
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