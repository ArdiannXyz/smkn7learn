package com.example.smk7.Siswa;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mapel_Siswa extends Fragment {
    private RecyclerView recyclerView;
    private MapelSiswaAdapter mapelAdapter;
    private List<MapelSiswaModel> mapelList = new ArrayList<>();
    private ViewPager2 viewPager;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapel_siswa, container, false);

        // Mendapatkan ViewPager2 dari activity
        Activity activity = getActivity();
        if (activity != null) {
            viewPager = activity.findViewById(R.id.Viewpagersiswa);
        }
        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Memanggil fetch data
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

                    // Pastikan apiResponse.getMapelSiswaModel() tidak null
                    if ("success".equals(apiResponse.getStatus())) {
                        List<MapelSiswaModel> mapelSiswaList = apiResponse.getMapelSiswaModel();

                        if (mapelSiswaList != null && !mapelSiswaList.isEmpty()) {
                            // Set adapter
                            MapelSiswaAdapter mapelSiswaAdapter = new MapelSiswaAdapter(mapelSiswaList, viewPager, getParentFragment());
                            recyclerView.setAdapter(mapelSiswaAdapter);
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