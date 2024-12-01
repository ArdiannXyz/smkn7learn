package com.example.smk7.Recyclemateriguru;

import android.content.Context;
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

import com.example.smk7.Adapter.KelasAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMateriKelas_Guru extends Fragment {

    private RecyclerView recyclerView;
    private ImageView backButton;
    private List<KelasModel> kelasList;
    private KelasAdapter kelasAdapter;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_materi_kelas_guru, container, false);

        // Back button listener
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara
                viewPager.setUserInputEnabled(false);

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(5, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);  // 300 ms cukup untuk memastikan transisi selesai
            }
        });



        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Fetch data from API
        fetchKelasData();

        return view;
    }

    private void fetchKelasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getKelasData();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    if ("success".equals(apiResponse.getStatus())) {
                        kelasList = apiResponse.getKelasModel();  // Pastikan ini adalah metode yang benar untuk mendapatkan data kelas

                        // Jika kelasList valid dan tidak kosong
                        if (kelasList != null && !kelasList.isEmpty()) {
                            // Ambil ViewPager2 dari layout
                            ViewPager2 viewPager = requireActivity().findViewById(R.id.Viewpagerguru);
                            // Kirimkan kelasList, viewPager, dan flag true ke konstruktor KelasAdapter
                            kelasAdapter = new KelasAdapter(kelasList, viewPager, true);
                            recyclerView.setAdapter(kelasAdapter);
                        } else {
                            Log.e("API Response", "kelasModel is null or empty");
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
