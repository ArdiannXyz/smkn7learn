package com.example.smk7.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;

import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private List<MateriModel> materiList;
    private FragmentActivity activity;
    private ViewPager2 viewPager;

    public MateriAdapter(List<MateriModel> materiList, ViewPager2 viewPager) {
        this.materiList = materiList;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_mapel, parent, false);
        return new MateriViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);

        holder.nama_mapel.setText(materi.getNama_mapel());
        holder.nama_mapel.setText(materi.getNama_mapel());
        holder.itemView.setOnClickListener(v -> {
            viewPager.setCurrentItem(8, true);
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_mapel;

        public MateriViewHolder(View view) {
            super(view);
            nama_mapel = view.findViewById(R.id.txtnama_materi);
        }
    }
}
