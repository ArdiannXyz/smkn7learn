package com.example.smk7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MapelSiswa extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelMapelSiswa> arrayList;
    private RecycleSiswa recycleSiswaAdapter;

    public MapelSiswa() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inisialisasi data di sini
        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_mapelsiswa, container, false);

        // Inisialisasi RecyclerView
        initializeRecyclerView(view);

        return view;
    }

    private void initializeData() {
        // Inisialisasi ArrayList
        arrayList = new ArrayList<>();

        // Tambahkan data
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
        arrayList.add(new ModelMapelSiswa(R.drawable.a, "Farhan", "900000"));
    }

    private void initializeRecyclerView(View view) {
        // Find RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Set fixed size
        recyclerView.setHasFixedSize(true);

        // Create and set adapter
        recycleSiswaAdapter = new RecycleSiswa(getContext(), arrayList);
        recyclerView.setAdapter(recycleSiswaAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up resources
        recyclerView.setAdapter(null);
        recyclerView = null;
    }
}