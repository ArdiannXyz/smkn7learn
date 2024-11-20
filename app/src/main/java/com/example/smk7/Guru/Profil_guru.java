package com.example.smk7.Guru;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView; // Tambahkan import untuk TextView

import com.example.smk7.LoginActivity;
import com.example.smk7.LupaPasswordActivity; // Tambahkan import untuk LupaPasswordActivity
import com.example.smk7.R;


public class Profil_guru extends Fragment {
    private Button ViewButton, EditButton, LogoutButton;
    private TextView textView_gantipw; // Deklarasi TextView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_guru, container, false);

        ViewButton = view.findViewById(R.id.btn_view);
        LogoutButton = view.findViewById(R.id.btn_Logout);
        EditButton = view.findViewById(R.id.btn_edt);
        textView_gantipw = view.findViewById(R.id.textView_gantipw); // Inisialisasi TextView

        ViewButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(7);
            }
        });

        EditButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(8);
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        textView_gantipw.setOnClickListener(new View.OnClickListener() { // Listener untuk TextView
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LupaPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}