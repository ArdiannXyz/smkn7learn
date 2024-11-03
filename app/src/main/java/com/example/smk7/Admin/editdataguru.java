package com.example.smk7.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class editdataguru extends Fragment {

    private EditText nip, nama, email;
    private Button btnSimpan;
    private FirebaseFirestore db;
    private String uid;
    private ImageView backbutton;
    private ProgressDialog progressDialog;
    private BottomNavigationHandler navigationHandler;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editdataguru, container, false);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        nip = view.findViewById(R.id.Nipguru);
        nama = view.findViewById(R.id.namaguru);
        btnSimpan = view.findViewById(R.id.btn_simpan);
        backbutton = view.findViewById(R.id.back_Buttonedit);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Menyimpan perubahan...");

        backbutton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).viewPager2.setCurrentItem(4);
            }
        });

        // Ambil data yang akan diedit
        loadDataToEdit();

        btnSimpan.setOnClickListener(v -> updateData());

        return view;

    }

    private void loadDataToEdit() {
        // Menggunakan SharedPreferences
        if (getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences("EditData", Context.MODE_PRIVATE);
            uid = prefs.getString("uid", "");
            String nipguru = prefs.getString("nip", "");
            String namaguru = prefs.getString("nama", "");

            // Set data ke EditText
            nip.setText(nipguru);
            nama.setText(namaguru);


        }
    }
        private void updateData() {
            // Validasi input
            String nipguru = nip.getText().toString().trim();
            String namaguru = nama.getText().toString().trim();

            if (nipguru.isEmpty() || namaguru.isEmpty()){
                Toast.makeText(getContext(), "Mohon isi semua field", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            // Buat Map untuk data yang akan diupdate
            Map<String, Object> updates = new HashMap<>();
            updates.put("nip", nipguru);
            updates.put("nama", namaguru);

            // Update ke Firestore
            db.collection("teachers") // Ganti dengan nama collection Anda
                    .document(uid)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show();

                        // Kembali ke fragment list (sesuaikan dengan posisi fragment list di ViewPager)
                        if (getActivity() instanceof HomeActivity) {
                            ((HomeActivity) getActivity()).viewPager2.setCurrentItem(4, true); // angka 0 sesuaikan dengan posisi fragment list Anda
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
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
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }
    }


