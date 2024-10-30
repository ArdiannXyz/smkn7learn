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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.DataClass;
import com.example.smk7.MyAdapter;
import com.example.smk7.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataMapel extends Fragment {
    private FloatingActionButton fab;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private RecyclerView recyclerView;
    private List<DataClass> dataList;
    private MyAdapter adapter;
    private AlertDialog loadingDialog;
    private BottomNavigationHandler navigationHandler;
    private ImageView backbutton;

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
        View view = inflater.inflate(R.layout.fragment_datamapel, container, false);
        initializeViews(view);
        setupRecyclerView();
        setupLoadingDialog();
        fetchDataFromFirebase();
        setupFabClickListener();
        backbuttonlistener();

        return view;

    }

    private void backbuttonlistener() {
        backbutton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).viewPager2.setCurrentItem(0);
            }
        });
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        backbutton = view.findViewById(R.id.backButton);
    }

    private void setupRecyclerView() {
        Context context = requireContext();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new MyAdapter(context, dataList);
        recyclerView.setAdapter(adapter);
    }

    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        loadingDialog = builder.create();
    }

    private void fetchDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Smk7");
        loadingDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setupFabClickListener() {
        fab.setOnClickListener(view -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).viewPager2.setCurrentItem(9);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventListener != null && databaseReference != null) {
            databaseReference.removeEventListener(eventListener);
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
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
}