package com.example.smk7.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.Adapterguru;
import com.example.smk7.R;
import com.example.smk7.classdataguru;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataGuru extends Fragment {

    private FloatingActionButton fab;
    private BottomNavigationHandler navigationHandler;
    private ImageView backbutton, imageedit;
    private AlertDialog loadingDialog;
    private RecyclerView RecyclerView;
    private Adapterguru adapterguru;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<classdataguru> datalist = new ArrayList<>();

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
        setupRecyclerView();
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
        RecyclerView = view.findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");
        imageedit = view.findViewById(R.id.Editimage);
        List<classdataguru> dataList = new ArrayList<>();



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

    private void setupRecyclerView() {
        if (RecyclerView != null && getContext() != null) {
            // Use LinearLayoutManager instead of GridLayoutManager for better list presentation
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            RecyclerView.setLayoutManager(layoutManager);

            // Add divider between items (optional)
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    RecyclerView.getContext(),
                    layoutManager.getOrientation()
            );
            RecyclerView.addItemDecoration(dividerItemDecoration);

            ViewPager2 viewPager2 = null;
            if (getActivity() instanceof HomeActivity) {
                viewPager2 = ((HomeActivity) getActivity()).viewPager2;
            }
            Adapterguru adapter = new Adapterguru(getContext(), datalist, viewPager2);
        datalist = new ArrayList<>();
        adapterguru = new Adapterguru(getContext(), datalist, viewPager2);
        RecyclerView.setAdapter(adapterguru);
    }}

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        progressDialog.show();
        db.collection("teachers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        datalist.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                classdataguru dataguru = new classdataguru(document.getString("nip"), document.getString("nama"), document.getString("email"), document.getString("uid"), document.getString("role"));
                                dataguru.setKey(document.getId());
                                datalist.add(dataguru);
                            }
                            adapterguru.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getActivity(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}