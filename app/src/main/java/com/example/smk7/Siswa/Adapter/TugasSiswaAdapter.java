package com.example.smk7.Siswa.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.R;
import com.example.smk7.Siswa.Model.TugasSiswaModel;
import com.example.smk7.Siswa.Tugas_Siswa;

import java.util.List;

public class TugasSiswaAdapter extends RecyclerView.Adapter<TugasSiswaAdapter.TugasViewHolder> {

    private List<TugasSiswaModel> tugasList;
    private ViewPager2 viewPager;
    private Fragment currentFragment;

    // Konstruktor untuk fragment yang memerlukan ViewPager2 dan Fragment
    public TugasSiswaAdapter(List<TugasSiswaModel> tugasList, ViewPager2 viewPager, Fragment currentFragment) {
        this.tugasList = tugasList;
        this.viewPager = viewPager;
        this.currentFragment = currentFragment;
    }

    // Konstruktor alternatif untuk fragment yang tidak memerlukan ViewPager2 dan Fragment
    public TugasSiswaAdapter(List<TugasSiswaModel> tugasList) {
        this(tugasList, null, null);  // Mengirim null untuk ViewPager2 dan Fragment
    }

    @NonNull
    @Override
    public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout untuk RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_tugas_siswa, parent, false);
        return new TugasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasViewHolder holder, int position) {
        TugasSiswaModel tugas = tugasList.get(position);

        // Set teks untuk item view (nama tugas atau deskripsi tugas)
        holder.tugas.setText(tugas.getDeskripsi());  // Pastikan getDeskripsi() ada dalam TugasSiswaModel

        holder.itemView.setOnClickListener(v -> {
            // Hanya melakukan perpindahan halaman jika fragment adalah Tugas_Siswa dan viewPager ada
            if (currentFragment != null && viewPager != null) {
                Log.d("Fragment Check", "Current Fragment: " + currentFragment.getClass().getSimpleName());

                // Hanya melakukan perpindahan halaman jika fragment adalah Tugas_Siswa
                if (currentFragment instanceof Tugas_Siswa) {
                    Log.d("FragmentA", "Pindah ke halaman 10...");
                    viewPager.setCurrentItem(9, false);  // false berarti tanpa animasi
                } else {
                    // Tidak melakukan apapun jika fragment yang aktif bukan Tugas_Siswa
                    Log.d("FragmentB", "Tidak pindah halaman karena berada di fragment lain.");
                }
            } else {
                Log.e("Fragment Error", "Fragment atau ViewPager tidak valid!");
            }
        });
    }

    @Override
    public int getItemCount() {
        // Pastikan tugasList tidak null sebelum memanggil size()
        return tugasList != null ? tugasList.size() : 0;  // Menghindari NullPointerException
    }

    // ViewHolder untuk mengikat item view
    public static class TugasViewHolder extends RecyclerView.ViewHolder {
        public TextView tugas;

        public TugasViewHolder(View view) {
            super(view);
            tugas = view.findViewById(R.id.txt_namatugas);  // Pastikan ini ID yang sesuai dengan layout carditem_tugas_siswa.xml
        }
    }
}
