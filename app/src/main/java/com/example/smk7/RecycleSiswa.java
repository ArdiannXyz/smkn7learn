package com.example.smk7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleSiswa extends RecyclerView.Adapter<RecycleSiswa.ViewHolder> {

    Context context;
    ArrayList<ModelMapelSiswa> arrayList = new ArrayList<>();


    public RecycleSiswa(Context context, ArrayList<ModelMapelSiswa> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.carditem_mapelsiswa,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.imageView.setImageResource(arrayList.get(position).getImg());
        holder.name.setText(arrayList.get(position).getName());
        holder.number.setText(arrayList.get(position).getNumber());

    }
    @Override
    public int getItemCount() {
        // Mengembalikan ukuran dari arrayList
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, number;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.Mapel);
            number = itemView.findViewById(R.id.golongan);

            animation(itemView);

        }
    }

    public void animation(View view) {

        Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
        view.setAnimation(animation);


    }


}

