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
import com.example.smk7.Siswa.Model.MapelSiswaModel;
import com.example.smk7.Siswa.Mapel_Siswa;
import com.example.smk7.Siswa.RecycleViewMapelSiswa;

import java.util.List;

public class MapelSiswaAdapter extends RecyclerView.Adapter<MapelSiswaAdapter.MapelSiswaViewHolder> {

    private List<MapelSiswaModel> mapelList;
    private ViewPager2 viewPager;
    private Fragment currentFragment;

    // Konstruktor yang menerima list MapelSiswaModel dan ViewPager2 untuk navigasi
    public MapelSiswaAdapter(List<MapelSiswaModel> mapelList, ViewPager2 viewPager, Fragment currentFragment) {
        this.mapelList = mapelList;
        this.viewPager = viewPager;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public MapelSiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout carditem_mapel untuk tiap item dalam RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_mapel, parent, false);
        return new MapelSiswaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MapelSiswaViewHolder holder, int position) {
        MapelSiswaModel mapel = mapelList.get(position);

        // Set nama mata pelajaran ke TextView
        holder.nama_mapel.setText(mapel.getNamaMapel());
        holder.itemView.setOnClickListener(v -> {
            if (currentFragment != null && viewPager != null) {
                Log.d("Fragment Check", "Current Fragment: " + currentFragment.getClass().getSimpleName());
                // Pindah langsung ke halaman yang sesuai berdasarkan fragment aktif
                if (currentFragment instanceof RecycleViewMapelSiswa) {
                    Log.d("FragmentA", "Pindah ke halaman 8...");
                    viewPager.setCurrentItem(7, false);  // false berarti tanpa animasi
                } else {
                    // Jangan pindah halaman jika berada di Fragment lain
                    Log.d("FragmentB", "Tidak pindah halaman karena berada di Fragment B.");
                }
            } else {
                Log.e("Fragment Error", "Fragment atau ViewPager tidak valid!");
            }
        });
    }
    @Override
    public int getItemCount() {
        // Return jumlah item dalam list mapel
        return mapelList != null ? mapelList.size() : 0;
    }

    // ViewHolder untuk mapel item
    public static class MapelSiswaViewHolder extends RecyclerView.ViewHolder {
        TextView nama_mapel;

        public MapelSiswaViewHolder(View itemView) {
            super(itemView);
            // Menghubungkan TextView dengan ID yang ada di layout item
            nama_mapel = itemView.findViewById(R.id.txtnama_mapel);
        }
    }
}
