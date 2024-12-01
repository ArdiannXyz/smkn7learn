package com.example.smk7.RecycleTugasGuru;

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
    private ImageView back_Button_kelas_guru;
    private List<KelasModel> kelasList;
    private KelasAdapter kelasAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_tugas_kelas_guru, container, false);

        // Back button listener
        back_Button_kelas_guru = view.findViewById(R.id.back_Button);
        back_Button_kelas_guru.setOnClickListener(v -> {

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

                                kelasAdapter = new KelasAdapter(kelasList, viewPager, true); // Hapus currentFragment
                                recyclerView.setAdapter(kelasAdapter);

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
}
