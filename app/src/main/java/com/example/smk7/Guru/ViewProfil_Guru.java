package com.example.smk7.Guru;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;


public class ViewProfil_Guru extends Fragment {
    private Button BackButton , Editimg, EditProfil;
    private BottomNavigationHandler navigationHandler;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profil_guru, container, false);

        EditProfil = view.findViewById(R.id.btn_edit);
        BackButton = view.findViewById(R.id.btn_kembali);
        Editimg = view.findViewById(R.id.editprofil);

        Editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileDialog();
            }
        });

        BackButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(2, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                // 300 ms cukup untuk memastikan transisi selesai
            }
        });

        EditProfil.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(7,false);
            }
        });
        return view;


    }

    private void showProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih Aksi");

        String[] options = {"Edit Profil", "Hapus Profil"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Edit Profil
                        openImagePicker();
                        break;
                    case 1: // Hapus Profil
                        deleteProfile();
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void deleteProfile() {
        Toast.makeText(requireContext(), "Profile Deleted", Toast.LENGTH_SHORT).show();
        // Add logic to delete the profile
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Toast.makeText(requireContext(), "Selected Image: " + selectedImageUri, Toast.LENGTH_SHORT).show();
            // Add logic to handle the selected image (e.g., upload or display)
        }
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
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }
}