package com.example.smk7.Siswa;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;

//untuk menampilkan mapel sebelum masuk ke recycletugassiswa

public class BehindTugas_Siswa extends Fragment {

    private ImageView BackButton;
    private BottomNavigationHandler navigationHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_behind_tugas_siswa, container, false);


        BackButton = view.findViewById(R.id.back_button);

        BackButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ViewPager2 viewPager = ((DashboardSiswa) getActivity()).viewPager2;
                viewPager.setCurrentItem(0, false);

            }
        });

        return view;


    }

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
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }


}