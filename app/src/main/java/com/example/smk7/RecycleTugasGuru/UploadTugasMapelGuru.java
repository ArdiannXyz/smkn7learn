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

import com.example.smk7.ApiResponse;
import com.example.smk7.ApiService;
import com.example.smk7.ApiServiceInterface;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Adapter.MapelAdapter;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadTugasMapelGuru extends Fragment {

    private RecyclerView recyclerView;
    private MapelAdapter mapelAdapter;
    private List<MapelModel> mapelList;
    private ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materi_mapel_guru, container, false);

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(0);
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
                            ViewPager2 viewPager = requireActivity().findViewById(R.id.Viewpagerguru);
                            mapelAdapter = new MapelAdapter(mapelList , viewPager);
                            recyclerView.setAdapter(mapelAdapter);
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
}