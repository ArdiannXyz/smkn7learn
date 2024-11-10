package com.example.smk7.Guru;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smk7.R;

public class UploadMateriView_Guru extends Fragment {

    private ImageView BackButton ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materi_kelas__guru, container, false);

        BackButton = view.findViewById(R.id.back_Button);




        BackButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(0);
            }
        });
        return view;

    }
}