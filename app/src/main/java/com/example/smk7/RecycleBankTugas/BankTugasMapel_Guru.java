package com.example.smk7.RecycleBankTugas;

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
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Adapter.MapelAdapter;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.R;
import com.example.smk7.RecycleTugasGuru.UploadTugasMapelGuru;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankTugasMapel_Guru extends Fragment {

    private RecyclerView recyclerView;
    private MapelAdapter mapelAdapter;
    private List<MapelModel> mapelList = new ArrayList<>();
    private ImageView backButton;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_tugas_mapel_guru, container, false);

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Nonaktifkan input swipe sementara
                viewPager.setUserInputEnabled(false);

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(0, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
                new Handler().postDelayed(() -> viewPager.setUserInputEnabled(true), 300);  // 300 ms cukup untuk memastikan transisi selesai
            }
        });

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchMapelData();

        return view;
    }

    private void fetchMapelData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getMapelData();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    if ("success".equals(apiResponse.getStatus())) {
                        mapelList = apiResponse.getMapelModel();
                        if (mapelList != null && !mapelList.isEmpty()) {
                            // Mendapatkan ViewPager2 dari activity
                            viewPager = requireActivity().findViewById(R.id.Viewpagerguru);
                            if (viewPager == null) {
                                Log.e("Error", "ViewPager2 tidak ditemukan!");
                            }

                            // Menyediakan fragment saat ini untuk adapter
                            Fragment currentFragment = getParentFragment() != null ? getParentFragment() : BankTugasMapel_Guru.this;

                            // Menyesuaikan adapter dengan fragment yang aktif
                            mapelAdapter = new MapelAdapter(mapelList, viewPager, currentFragment);
                            recyclerView.setAdapter(mapelAdapter);
                            mapelAdapter.notifyDataSetChanged();  // Update UI
                        } else {
                            Log.e("API Response", "mapelModel is null or empty");
                            Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "API error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Response not successful or body is null", Toast.LENGTH_SHORT).show();
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