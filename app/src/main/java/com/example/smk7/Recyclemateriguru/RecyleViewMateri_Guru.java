package com.example.smk7.Recyclemateriguru;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Adapter.MateriAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;
import com.example.smk7.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyleViewMateri_Guru extends Fragment {
    private static final String TAG = "RecycleViewMateri_Guru";
    private RecyclerView recyclerView;
    private MateriAdapter materiAdapter;
    private List<MateriModel> materiList;
    private ImageView backButton;
    private FloatingActionButton fabAddMateri;
    private BottomNavigationHandler navigationHandler;
    private String idGuru; // Variable untuk menyimpan ID Guru

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview_materi_guru, container, false);


        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;


                viewPager.setCurrentItem(8, false);  // false berarti tanpa animasi untuk perpindahan langsung

            }
        });

        // Ambil ID Guru dari SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        idGuru = prefs.getString("id_guru", "");

        // Debug log untuk memeriksa ID Guru
        Log.d(TAG, "ID Guru from SharedPreferences: " + idGuru);

//        if (idGuru.isEmpty()) {
//            Log.e(TAG, "ID Guru not found in SharedPreferences");
//            Toast.makeText(getContext(), "Error: ID Guru tidak ditemukan", Toast.LENGTH_SHORT).show();
//        }

        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize FAB dengan SessionManager
        fabAddMateri = view.findViewById(R.id.fabAddMateri);
        fabAddMateri.setOnClickListener(v -> {
            if (getActivity() == null) return;

            // Gunakan SessionManager untuk mendapatkan ID Guru
            SessionManager sessionManager = new SessionManager(getContext());
            int idGuru = sessionManager.getIdGuru();

            if (idGuru == -1) {
                Toast.makeText(getContext(), "Error: ID Guru tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getContext(), UploadMateri_Guru.class);

            // Convert ID Guru ke String karena UploadMateri_Guru menerima String
            intent.putExtra("id_guru", String.valueOf(idGuru));

            if (materiList != null && !materiList.isEmpty()) {
                MateriModel firstMateri = materiList.get(0);
                intent.putExtra("id_kelas", String.valueOf(firstMateri.getIdKelas()));
                intent.putExtra("id_mapel", String.valueOf(firstMateri.getIdMapel()));
                intent.putExtra("nama_kelas", firstMateri.getNamaKelas());
            }

            // Debug log
            Log.d(TAG, "Sending ID Guru to UploadMateri_Guru: " + idGuru);

            startActivity(intent);
        });

        fetchMateriData();
    }

    private void fetchMateriData() {
        if (getContext() == null) return;

        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getMateriData();
        Log.d(TAG, "Starting to fetch materi data");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d(TAG, "API Response received: " + apiResponse);

                    if (apiResponse.isSuccess() || "success".equals(apiResponse.getStatus())) {
                        materiList = apiResponse.getMateriModel();
                        if (materiList != null && !materiList.isEmpty()) {
                            setupRecyclerView(materiList, apiService);
                            Log.d(TAG, "Found " + materiList.size() + " materi items");
                        } else {
                            showEmptyState();
                            Log.w(TAG, "No materi data found");
                        }
                    } else {
                        handleError("API error: " + apiResponse.getMessage());
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                    }
                    handleError("Response failed with code: " + response.code() +
                            ", message: " + response.message() +
                            ", errorBody: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (getContext() == null) return;
                handleError("Network error: " + t.getMessage());
            }
        });
    }

    private void setupRecyclerView(List<MateriModel> materiList, ApiServiceInterface apiService) {
        if (getContext() == null) return;

        materiAdapter = new MateriAdapter(
                getContext(),
                materiList,
                materiModel -> {
                    Log.d(TAG, "Materi clicked: " + materiModel.getJudulMateri());
                    // Handle item click if needed
                },
                apiService
        );

        recyclerView.setAdapter(materiAdapter);
    }

    private void showEmptyState() {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Tidak ada data materi tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(String message) {
        Log.e(TAG, message);
        if (getContext() != null) {
            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationHandler = (BottomNavigationHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomNavigationHandler");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
        if (getActivity() != null) {
            ((DashboardGuru) getActivity()).setSwipeEnabled(false);
        }
        // Refresh data when resuming
        fetchMateriData();
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