package com.example.smk7.Siswa;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Siswa.Adapter.MapelSiswaAdapter;
import com.example.smk7.R;
import com.example.smk7.Siswa.Model.MapelSiswaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleViewMapelSiswa extends Fragment {

    private ImageView backButton;
    private RecyclerView recyclerView;
    private ViewPager2 viewPager;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view_mapel_siswa, container, false);

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ViewPager2 viewPager = ((DashboardSiswa) getActivity()).viewPager2;
                viewPager.setCurrentItem(0, false);  // Navigasi kembali ke item pertama
            }
        });

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Memanggil data dari API
        fetchMapelData2();

        return view;
    }
    private void fetchMapelData2() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getMapelData2();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    // Mengecek status dari API response
                    if ("success".equals(apiResponse.getStatus())) {
                        List<MapelSiswaModel> mapelList = apiResponse.getMapelSiswaModel();

                        if (mapelList != null && !mapelList.isEmpty()) {
                            if (isAdded() && getActivity() != null) {
                                // Mendapatkan ViewPager2 dari activity
                                viewPager = getActivity().findViewById(R.id.Viewpagersiswa);
                                if (viewPager == null) {
                                    Log.e("Error", "ViewPager2 tidak ditemukan!");
                                }
                                Fragment currentFragment = getParentFragment() != null ? getParentFragment() : RecycleViewMapelSiswa.this;

                                // Menyesuaikan adapter dengan fragment yang aktif
                                MapelSiswaAdapter mapelAdapter = new MapelSiswaAdapter(mapelList, viewPager, currentFragment);
                                recyclerView.setAdapter(mapelAdapter);
                                mapelAdapter.notifyDataSetChanged();  // Update UI
                            } else {
                                Log.e("Fragment Error", "Fragment not attached to activity.");
                            }
                        } else {
                            Log.e("API Response", "mapelSiswaModel is null or empty");
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
                Toast.makeText(getContext(), "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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