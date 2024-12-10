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
import com.example.smk7.Siswa.Model.MateriSiswaModel;
import com.example.smk7.Siswa.Recycle_Materi_Siswa;

import java.util.List;

public class MateriSiswaAdapter extends RecyclerView.Adapter<MateriSiswaAdapter.MateriSiswaViewHolder> {

    private List<MateriSiswaModel> materiList;
    private ViewPager2 viewPager;
    private Fragment currentFragment;

    // Konstruktor yang menerima list MateriSiswaModel dan ViewPager2 untuk navigasi
    public MateriSiswaAdapter(List<MateriSiswaModel> materiList, ViewPager2 viewPager, Fragment currentFragment) {
        this.materiList = materiList;
        this.viewPager = viewPager;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public MateriSiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout carditem_materi_siswa untuk tiap item dalam RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_materi_siswa, parent, false);
        return new MateriSiswaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriSiswaViewHolder holder, int position) {
        MateriSiswaModel materi = materiList.get(position);

        // Set judul tugas ke TextView
        holder.judul_tugas.setText(materi.getJudulTugas());  // Pastikan getJudulTugas() ada di MateriSiswaModel
        holder.itemView.setOnClickListener(v -> {
            if (currentFragment != null && viewPager != null) {
                Log.d("Fragment Check", "Current Fragment: " + currentFragment.getClass().getSimpleName());
                // Pindah langsung ke halaman yang sesuai berdasarkan fragment aktif
                if (currentFragment instanceof Recycle_Materi_Siswa) {
                    Log.d("FragmentA", "Pindah ke halaman 10...");
                    viewPager.setCurrentItem(10, false);  // false berarti tanpa animasi
                } else {
                    // Jangan pindah halaman jika berada di Fragment lain
                    Log.d("FragmentB", "Tidak pindah halaman karena berada di Fragment lain.");
                }
            } else {
                Log.e("Fragment Error", "Fragment atau ViewPager tidak valid!");
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return jumlah item dalam list materi
        return materiList != null ? materiList.size() : 0;
    }

    // ViewHolder untuk materi item
    public static class MateriSiswaViewHolder extends RecyclerView.ViewHolder {
        TextView judul_tugas;

        public MateriSiswaViewHolder(View itemView) {
            super(itemView);
            // Menghubungkan TextView dengan ID yang ada di layout item
            judul_tugas = itemView.findViewById(R.id.txt_namamateri);  // Pastikan ini ID yang sesuai di carditem_materi_siswa.xml
        }
    }
}
