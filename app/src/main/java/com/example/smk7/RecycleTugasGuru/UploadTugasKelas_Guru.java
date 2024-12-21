package com.example.smk7.RecycleTugasGuru;

import android.content.Context;
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

import com.example.smk7.Adapter.KelasAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;

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


                viewPager.setCurrentItem(4, false);  // false berarti tanpa animasi untuk perpindahan langsung

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
                        // Menggunakan getData() karena API mengembalikan dalam format "data"
                        kelasList = apiResponse.getData();

                        if (kelasList != null && !kelasList.isEmpty()) {
                            Log.d("API Success", "Jumlah data kelas: " + kelasList.size());

                            ViewPager2 viewPager = requireActivity().findViewById(R.id.Viewpagerguru);
                            if (viewPager != null) {
                                Fragment currentFragment = UploadTugasKelas_Guru.this;

                                kelasAdapter = new KelasAdapter(kelasList, viewPager, true, currentFragment);
                                recyclerView.setAdapter(kelasAdapter);
                                kelasAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("ViewPager Error", "ViewPager tidak ditemukan");
                            }
                        } else {
                            String message = apiResponse.getMessage() != null ?
                                    apiResponse.getMessage() : "Tidak ada data kelas";
                            Log.w("API Warning", message);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMessage = apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : "Gagal mengambil data kelas";
                        Log.e("API Error", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Gagal mengambil data dari server";
                    Log.e("API Error", errorMessage);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage = "Gagal terhubung ke server: " + t.getMessage();
                Log.e("API Error", errorMessage, t);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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