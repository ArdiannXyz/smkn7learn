package com.example.smk7.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DataGuru extends Fragment {

    private FloatingActionButton fab;
    private BottomNavigationHandler navigationHandler;
    private ImageView backbutton;
    private AlertDialog loadingDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dataguru, container, false);

        initializeViews(view);
        setupFabClickListener();
        setupLoadingDialog();
        backbuttonlistener();

        return view;
    }

    private void backbuttonlistener() {
        if (backbutton != null) {
            backbutton.setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).viewPager2.setCurrentItem(0);
                }
            });
        }
    }
    private void initializeViews(View view) {
        fab = view.findViewById(R.id.fab);
        backbutton = view.findViewById(R.id.backButton);
    }

    private void setupFabClickListener() {
        if (fab != null) {
            fab.setOnClickListener(view -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).viewPager2.setCurrentItem(10);
                }
            });
        }
    }

    private void setupLoadingDialog() {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            loadingDialog = builder.create();
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
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}