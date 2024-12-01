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
import com.example.smk7.Adapter.MateriAdapter;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyleViewMateri_Guru extends Fragment {

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

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara
                viewPager.setUserInputEnabled(false);

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(6, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);  // 300 ms cukup untuk memastikan transisi selesai
            }
        });

        // Setup FAB button
        fabAddMateri = view.findViewById(R.id.fabAddMateri);
        fabAddMateri.setOnClickListener(v -> {
            // Menyembunyikan Bottom Navigation saat FAB ditekan
            if (getActivity() instanceof BottomNavigationHandler) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.GONE); // Menyembunyikan BottomNavigation
                }
            }

            // Menunda pemindahan activity agar perubahan UI selesai
            new Handler().postDelayed(() -> {
                // Pindah ke Activity UploadMateri_Guru
                Intent intent = new Intent(getContext(), UploadMateri_Guru.class);
                startActivity(intent);
            }, 200); // Menunggu 200ms untuk memastikan animasi transisi selesai
        });


        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchMateriData();

        return view;
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

                    if ("success".equals(apiResponse.getStatus())) {
                        materiList = apiResponse.getMateriModel();
                        if (materiList != null && !materiList.isEmpty()) {
                            // Call the method to setup the RecyclerView with the data
                            setupRecyclerView(materiList);
                        } else {
                            Log.e("API Response", "materiModel is null or empty");
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

    private void setupRecyclerView(List<MateriModel> materiList) {
        // Create an instance of the adapter and set it on the RecyclerView
        materiAdapter = new MateriAdapter(getContext(), materiList, new MateriAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MateriModel materiModel) {
                // Remove the code that navigates to another activity
                // You can implement your own logic here if needed
                Toast.makeText(getContext(), "Item clicked: " + materiModel.getJudulTugas(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(materiAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Pastikan Bottom Navigation disembunyikan saat fragment di-attach ke Activity
        if (getActivity() instanceof BottomNavigationHandler) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BottomNavigationHandler) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);  // Menyembunyikan Bottom Navigation
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof BottomNavigationHandler) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.VISIBLE);  // Menampilkan kembali Bottom Navigation
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        // Menampilkan Bottom Navigation kembali saat fragment di-detach
        if (getActivity() instanceof BottomNavigationHandler) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }
}
