package com.example.smk7.Guru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.R;
import com.example.smk7.classdataguru;

import java.util.List;


public class Adapterguru extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<classdataguru> dataList;
    public Adapterguru(Context context, List<classdataguru> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_dataguru, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Nip.setText(dataList.get(position).getDataNip());
        holder.NamaGuru.setText(dataList.get(position).getDatanama());



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(List<classdataguru> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView Nip, NamaGuru;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recCard = itemView.findViewById(R.id.recCard);
        Nip = itemView.findViewById(R.id.nip);
        NamaGuru = itemView.findViewById(R.id.namaguru);
    }
}


