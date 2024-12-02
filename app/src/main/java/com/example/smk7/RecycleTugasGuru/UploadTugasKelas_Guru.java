package com.example.smk7.RecycleTugasGuru;

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
import com.example.smk7.Recyclemateriguru.UploadMateriKelas_Guru;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadTugasKelas_Guru extends Fragment {

    private RecyclerView recyclerView;
    private ImageView backButton;
    private List<KelasModel> kelasList;
    private KelasAdapter kelasAdapter;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_tugas_kelas_guru, container, false);

        // Back button listener
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(4, false);  // false berarti tanpa animasi untuk perpindahan langsung

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
                        kelasList = apiResponse.getKelasModel();  // Pastikan data kelas diambil dengan benar

                        // Pastikan kelasList valid dan tidak kosong
                        if (kelasList != null && !kelasList.isEmpty()) {
                            // Ambil ViewPager2 dari activity
                            ViewPager2 viewPager = requireActivity().findViewById(R.id.Viewpagerguru);

                            if (viewPager != null) {
                                // Pastikan currentFragment sesuai dengan kondisi ini
                                Fragment currentFragment = UploadTugasKelas_Guru.this;  // Gunakan fragment yang aktif

                                // Panggil adapter dengan parameter yang benar
                                kelasAdapter = new KelasAdapter(kelasList, viewPager, true, currentFragment);
                                recyclerView.setAdapter(kelasAdapter);  // Set adapter ke RecyclerView

                                // Jika RecyclerView di-click, maka pindah ke halaman 12 di ViewPager2
                                recyclerView.setOnClickListener(v -> {
                                    if (currentFragment instanceof UploadTugasKelas_Guru) {
                                        Log.d("FragmentB", "Pindah ke halaman 12...");
                                        viewPager.setCurrentItem(12, true);  // Pindah ke halaman 12 untuk Fragment B
                                    }
                                });
                            }
                        }
                    } else {
                        Log.e("API Error", "Error: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e("API Error", "Response not successful or body is null");
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
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }
}