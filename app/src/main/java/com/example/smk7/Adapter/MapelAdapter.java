package com.example.smk7.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.MapelModel;
import com.example.smk7.R;
import com.example.smk7.RecycleBankTugas.BankTugasMapel_Guru;
import com.example.smk7.RecycleTugasGuru.UploadTugasMapelGuru;
import com.example.smk7.Recyclemateriguru.UploadMateriMapel_Guru;

import java.util.List;

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.MapelViewHolder> {

    private List<MapelModel> mapelList;
    private ViewPager2 viewPager;
    private Fragment currentFragment;

    public MapelAdapter(List<MapelModel> mapelList, ViewPager2 viewPager, Fragment currentFragment) {
        this.mapelList = mapelList;
        this.viewPager = viewPager;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public MapelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_mapel, parent, false);
        return new MapelViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MapelViewHolder holder, int position) {
        MapelModel mapel = mapelList.get(position);
        if (mapel != null && mapel.getNamaMapel() != null) {
            holder.nama_mapel.setText(mapel.getNamaMapel());
        } else {
            holder.nama_mapel.setText("Nama mapel tidak tersedia");
        }

        holder.itemView.setOnClickListener(v -> {
            if (currentFragment != null && viewPager != null) {
                Log.d("Fragment Check", "Current Fragment: " +
                        currentFragment.getClass().getSimpleName());

                try {
                    if (currentFragment instanceof UploadMateriMapel_Guru) {
                        viewPager.setCurrentItem(8, false);
                    } else if (currentFragment instanceof UploadTugasMapelGuru) {
                        viewPager.setCurrentItem(9, false);
                    } else if (currentFragment instanceof BankTugasMapel_Guru) {
                        viewPager.setCurrentItem(10, false);
                    } else {
                        Log.e("Fragment Error", "Fragment tidak dikenali");
                    }
                } catch (Exception e) {
                    Log.e("ViewPager Error", "Error saat navigasi: " + e.getMessage());
                }
            } else {
                Log.e("Fragment Error", "currentFragment atau viewPager null!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapelList != null ? mapelList.size() : 0;
    }

    public static class MapelViewHolder extends RecyclerView.ViewHolder {
        TextView nama_mapel;

        public MapelViewHolder(View itemView) {
            super(itemView);
            nama_mapel = itemView.findViewById(R.id.txtnama_mapel);
        }
    }
}