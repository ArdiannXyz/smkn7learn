package com.example.smk7.Recyclemateriguru;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Guru.Adapter.MateriAdapter;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Guru.Model.MateriModel;
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview_materi_guru, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Back Button
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(8, false);
            }
        });

        // Initialize FAB
        fabAddMateri = view.findViewById(R.id.fabAddMateri);
        fabAddMateri.setOnClickListener(v -> {
            if (getActivity() instanceof BottomNavigationHandler) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getContext(), UploadMateri_Guru.class);
                startActivity(intent);
            }, 200);
        });

        // Fetch data
        fetchMateriData();
    }

    private void fetchMateriData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getMateriData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    // Ubah pengecekan status menjadi "success" atau cek success boolean
                    if (apiResponse.isSuccess() || "success".equals(apiResponse.getStatus())) {
                        materiList = apiResponse.getMateriModel();
                        if (materiList != null && !materiList.isEmpty()) {
                            setupRecyclerView(materiList, apiService);
                            Log.d("API Response", "Ditemukan " + materiList.size() + " materi");
                        } else {
                            Log.e("API Response", "materiModel kosong");
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Tidak ada data materi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("API Response", "API error: " + apiResponse.getMessage());
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API Error", "Error reading error body: " + e.getMessage());
                    }
                    Log.e("API Error", "Response gagal dengan kode: " + response.code() +
                            ", pesan: " + response.message() +
                            ", errorBody: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView(List<MateriModel> materiList, ApiServiceInterface apiService) {
        if (getContext() == null) return;
        materiAdapter = new MateriAdapter(getContext(), materiList,
                new MateriAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MateriModel materiModel) {
                        Log.d(TAG, "Item diklik: " + materiModel.getJudulMateri());
                        // Tambahkan logika handling klik di sini
                    }
                },
                apiService);  // Tambahkan parameter keempat

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