package com.example.smk7.Siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.LoginActivity;
import com.example.smk7.R;


public class Profil_Siswa extends Fragment {

    private Button ViewButton, EditButton, LogoutButton ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_siswa, container, false);

        ViewButton = view.findViewById(R.id.btn_view);
        LogoutButton = view.findViewById(R.id.btn_Logout);
        EditButton = view.findViewById(R.id.btn_edt);


        ViewButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(5);
            }
        });

        EditButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(6);
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);

                startActivity(intent);
            }
        });
        return view;

    }
}