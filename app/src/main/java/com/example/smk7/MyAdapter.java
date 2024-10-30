package com.example.smk7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_mapelsiswa, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.imageview);
        holder.mapel.setText(dataList.get(position).getDataTitle());
        holder.jurusan.setText(dataList.get(position).getDataDesc());
        holder.Golongan.setText(dataList.get(position).getDataLang());



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView imageview;
    TextView mapel, jurusan, Golongan;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageview = itemView.findViewById(R.id.imageView);
        recCard = itemView.findViewById(R.id.recCard);
        jurusan = itemView.findViewById(R.id.Jurusan);
        Golongan = itemView.findViewById(R.id.golongan);
        mapel = itemView.findViewById(R.id.Mapel);
    }
}


