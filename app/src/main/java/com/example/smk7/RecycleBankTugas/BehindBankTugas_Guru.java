package com.example.smk7.RecycleBankTugas;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Guru.Model.BankTugasModel;
import com.example.smk7.R;

public class BehindBankTugas_Guru extends Fragment {

    private TextView tvTugasNama, tvTugasStatus;
    private BottomNavigationHandler navigationHandler;

    // Membuat newInstance untuk menerima data BankTugasModel
    public static BehindBankTugas_Guru newInstance(BankTugasModel tugasModel) {
        BehindBankTugas_Guru fragment = new BehindBankTugas_Guru();
        Bundle args = new Bundle();
        args.putParcelable("tugas", tugasModel);  // Menyimpan data dalam Bundle
        fragment.setArguments(args);  // Mengirim data ke Fragment
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_behind_bank_tugas_guru, container, false);

        tvTugasNama = rootView.findViewById(R.id.txtnama);
        tvTugasStatus = rootView.findViewById(R.id.txtstatus);

        // Ambil data yang diteruskan melalui Bundle
        if (getArguments() != null) {
            BankTugasModel tugas = getArguments().getParcelable("tugas");
            if (tugas != null) {
                tvTugasNama.setText(tugas.getNama());
                tvTugasStatus.setText(tugas.getStatus());
            }
        }

        return rootView;
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
            if (getActivity() != null) {
                // Menonaktifkan swipe di Activity
                ((DashboardGuru) getActivity()).setSwipeEnabled(false);
            }
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
