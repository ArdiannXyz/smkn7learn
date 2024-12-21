package com.example.smk7.Siswa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.LoginActivity;
import com.example.smk7.LupaPasswordActivity; // Tambahkan import untuk LupaPasswordActivity
import com.example.smk7.R;

public class Profil_Siswa extends Fragment {

    private Button ViewButton, EditButton, LogoutButton;
    private TextView textView_gantipw_siswa;
    private BottomNavigationHandler navigationHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_siswa, container, false);

        // Initialize buttons and text views
        ViewButton = view.findViewById(R.id.btn_view);
        LogoutButton = view.findViewById(R.id.btn_Logout);
        EditButton = view.findViewById(R.id.btn_edt);
        textView_gantipw_siswa = view.findViewById(R.id.textView_gantipw_siswa);

        // Set up button click listeners
        ViewButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(5,false);
            }
        });

        EditButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(6,false);
            }
        });

        // Logout button listener
        LogoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // Change password listener
        textView_gantipw_siswa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LupaPasswordActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Attach BottomNavigationHandler
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationHandler = (BottomNavigationHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomNavigationHandler");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }

}
