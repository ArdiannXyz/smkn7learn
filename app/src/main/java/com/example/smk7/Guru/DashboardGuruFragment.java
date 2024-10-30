package com.example.smk7.Guru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.smk7.R;


public class DashboardGuruFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashgurufragment, container, false);
        LinearLayout linearLayoutmateriguru = view.findViewById(R.id.uploadmateriguru);
        LinearLayout linearLayouttugasguru = view.findViewById(R.id.uploadtugasguru);
        LinearLayout linearLayoutdatatugas = view.findViewById(R.id.datatugas);
        LinearLayout linearLayoutbankmateri = view.findViewById(R.id.Bankmateri);

        linearLayoutmateriguru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(2); // Misalnya fragment minuman
            }
        });
        linearLayouttugasguru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(3); // Misalnya fragment minuman
            }
        });
        linearLayoutdatatugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(4); // Misalnya fragment minuman
            }
        });
        linearLayoutbankmateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(5); // Misalnya fragment minuman
            }
        });
        return view;
    }
    private void openFragment(int position) {
        // Pindah ke fragment yang diinginkan di ViewPager2
        ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(position);
        // Misalnya, untuk berpindah ke fragment kedua (List Makanan)
    }
}