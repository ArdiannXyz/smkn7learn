package com.example.smk7.RecycleTugasGuru;

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
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Guru.Adapter.TugasAdapter;
import com.example.smk7.Guru.Model.TugasModel;
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleViewTugasGuru extends Fragment {

    private RecyclerView recyclerView;
    private TugasAdapter tugasAdapter;
    private List<TugasModel> tugasList;
    private ImageView backButton;
    private BottomNavigationHandler navigationHandler;
    private FloatingActionButton fabAddTugas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view_tugas_guru, container, false);

        // Initialize back button and its click listener
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;


                viewPager.setCurrentItem(9, false);  // false berarti tanpa animasi untuk perpindahan langsung

            }
        });



        fabAddTugas = view.findViewById(R.id.fabAddTugas);
        fabAddTugas.setOnClickListener(v -> {
            if (getActivity() instanceof BottomNavigationHandler) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getContext(), UploadTugas_guru.class);
                startActivity(intent);
            }, 200);
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch Tugas data
        fetchTugasData();

        return view;
    }

    private void fetchTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getTugasData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Response mentah: " + new Gson().toJson(response.body()));
                }
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    // Check if the response status is successful
                    if ("success".equals(apiResponse.getStatus())) {
                        tugasList = apiResponse.getTugasModel(); // Fetch tugasModel here
                        if (tugasList != null && !tugasList.isEmpty()) {
                            // Setup the RecyclerView with Tugas data
                            setupRecyclerView(tugasList);
                        } else {
                            Log.e("API Response", "tugasModel is null or empty");
                            Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "API error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Log.e("API Error", "Response failed with code: " + response.code() +
                            ", message: " + response.message() +
                            ", errorBody: " + errorBody);
                    Toast.makeText(getContext(), "API error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", "Request failed: " + t.getMessage(), t);
            }
        });
    }

    private void setupRecyclerView(List<TugasModel> tugasList) {
        if (tugasList == null) {
            Log.e("RecyclerView", "tugasList kosong");
            return;
        }

        Log.d("RecyclerView", "Menyiapkan " + tugasList.size() + " item");

        tugasAdapter = new TugasAdapter(getContext(), tugasList, new TugasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String judulTugas, int idTugas) {
                Intent intent = new Intent(getContext(), UploadTugas_guru.class);
                intent.putExtra("nama_tugas", judulTugas);
                intent.putExtra("id_tugas", idTugas);
                startActivity(intent);
            }

            @Override
            public void onDeleteSuccess() {
                // Refresh data setelah delete
                fetchTugasData();
            }
        });
        recyclerView.setAdapter(tugasAdapter);
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