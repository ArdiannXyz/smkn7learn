package com.example.smk7.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.smk7.R;


public class Dashboard extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        LinearLayout linearLayoutsiswakelas = view.findViewById(R.id.master_siswa);
        LinearLayout linearLayoutgurumapel = view.findViewById(R.id.masterguru);
        ConstraintLayout constraintLayoutsiswa = view.findViewById(R.id.bt_datasiswa);
        ConstraintLayout constraintLayoutguru = view.findViewById(R.id.bt_dataguru);
        ConstraintLayout constraintLayoutkelas = view.findViewById(R.id.bt_datakelas);
        ConstraintLayout constraintLayoutmapel = view.findViewById(R.id.bt_datamapel);

        constraintLayoutsiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(3); // Misalnya fragment minuman
            }
        });
        constraintLayoutguru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(4); // Misalnya fragment minuman
            }
        });
        constraintLayoutkelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(5); // Misalnya fragment minuman
            }
        });
        constraintLayoutmapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(6); // Misalnya fragment minuman
            }
        });
        linearLayoutsiswakelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(7); // Misalnya fragment minuman
            }
        });
        linearLayoutgurumapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(8); // Misalnya fragment minuman
            }
        });
        return view;
    }
    private void openFragment(int position) {
        // Pindah ke fragment yang diinginkan di ViewPager2
        ((HomeActivity) getActivity()).viewPager2.setCurrentItem(position);
        // Misalnya, untuk berpindah ke fragment kedua (List Makanan)
    }
    }
