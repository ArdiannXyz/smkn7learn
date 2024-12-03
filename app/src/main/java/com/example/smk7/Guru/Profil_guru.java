package com.example.smk7.Guru;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView; // Tambahkan import untuk TextView

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.GantiPasswordActivity;
import com.example.smk7.LoginActivity;
import com.example.smk7.LupaPasswordActivity; // Tambahkan import untuk LupaPasswordActivity
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Profil_guru extends Fragment {
    private Button ViewButton, LogoutButton;
    private TextView textView_gantipw;
    BottomNavigationView bottomNavigationView;
    private BottomNavigationHandler navigationHandler;// Deklarasi TextView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_guru, container, false);

        ViewButton = view.findViewById(R.id.btn_view);
        LogoutButton = view.findViewById(R.id.btn_Logout);
        textView_gantipw = view.findViewById(R.id.textView_gantipw); // Inisialisasi TextView

        ViewButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(6, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                  // 300 ms cukup untuk memastikan transisi selesai
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
                Intent intent = new Intent(getActivity(), GantiPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
        if (navigationHandler != null) {
            // Tampilkan kembali Bottom Navigation saat fragment berhenti
            navigationHandler.showBottomNav();  // Memastikan Bottom Navigation muncul tanpa delay
        }
    }
}