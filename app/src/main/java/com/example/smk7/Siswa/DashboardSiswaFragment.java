package com.example.smk7.Siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.R;

public class DashboardSiswaFragment extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboardsiswafragment, container, false);
        LinearLayout linearLayoutmaterisiswa = view.findViewById(R.id.materisiswa);
        LinearLayout linearLayouttugassiswa = view.findViewById(R.id.tugassiswa);

        linearLayoutmaterisiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(3); // Misalnya fragment minuman
            }
        });
        linearLayouttugassiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(7); // Misalnya fragment minuman
            }
        });
        return  view;
    }
    private void openFragment(int position) {
        // Pindah ke fragment yang diinginkan di ViewPager2
        ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(position);
        // Misalnya, untuk berpindah ke fragment kedua (List Makanan)
    }
}