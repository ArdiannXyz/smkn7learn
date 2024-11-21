package com.example.smk7.Siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.LoginActivity;
import com.example.smk7.LupaPasswordActivity; // Tambahkan import untuk LupaPasswordActivity
import com.example.smk7.R;


public class Profil_Siswa extends Fragment {

    private Button ViewButton,EditButton, LogoutButton;
    private TextView textView_gantipw_siswa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil_siswa, container, false);

        ViewButton = view.findViewById(R.id.btn_view);
        LogoutButton = view.findViewById(R.id.btn_Logout);
        EditButton = view.findViewById(R.id.btn_edt);
        textView_gantipw_siswa = view.findViewById(R.id.textView_gantipw_siswa);

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

        // Listener untuk TextView ganti password
        textView_gantipw_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LupaPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}