package com.example.smk7.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.MapelModel;
import com.example.smk7.R;

import java.util.List;

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.MapelViewHolder> {

    private List<MapelModel> mapelList;
    private ViewPager2 viewPager;

    public MapelAdapter(List<MapelModel> mapelList, ViewPager2 viewPager) {
        this.mapelList = mapelList;
        this.viewPager = viewPager;
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
        holder.nama_mapel.setText(mapel.getNama_mapel());
        holder.itemView.setOnClickListener(v -> {
            viewPager.setCurrentItem(8, true);
        });
    }

    @Override
    public int getItemCount() {
        return mapelList.size();
    }

    public static class MapelViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_mapel;

        public MapelViewHolder(View view) {
            super(view);
            nama_mapel = view.findViewById(R.id.txtnama_materi);
        }
    }
}