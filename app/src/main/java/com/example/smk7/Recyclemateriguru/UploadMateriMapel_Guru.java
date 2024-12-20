package com.example.smk7.Recyclemateriguru;

import android.app.Activity;
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

import com.example.smk7.Adapter.MapelAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMateriMapel_Guru extends Fragment {

    private RecyclerView recyclerView;
    private MapelAdapter mapelAdapter;
    private List<MapelModel> mapelList = new ArrayList<>();
    private ImageView backButton;
    private ViewPager2 viewPager;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materi_mapel_guru, container, false);

        Activity activity = getActivity();
        if (activity != null) {
            ViewPager2 viewPager2 = activity.findViewById(R.id.Viewpagerguru);

        }
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;


                viewPager.setCurrentItem(0, false);  // false berarti tanpa animasi untuk perpindahan langsung

            }
        });




        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Memanggil fetch data
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
                    Log.d("API Response", "Status: " + apiResponse.getStatus());
                    Log.d("API Response", "Message: " + apiResponse.getMessage());
                    Log.d("API Response", "Success: " + apiResponse.isSuccess());

                    if ("success".equals(apiResponse.getStatus())) {
                        mapelList = apiResponse.getMapelModel(); // Menggunakan getMapelModel() sesuai ApiResponse
                        if (mapelList != null && !mapelList.isEmpty()) {
                            // Debug log untuk melihat data
                            for (MapelModel mapel : mapelList) {
                                Log.d("Mapel Data", "Nama Mapel: " + mapel.getNamaMapel());
                            }

                            viewPager = requireActivity().findViewById(R.id.Viewpagerguru);

                            // Inisialisasi adapter
                            mapelAdapter = new MapelAdapter(mapelList, viewPager, UploadMateriMapel_Guru.this);
                            recyclerView.setAdapter(mapelAdapter);

                            // Pastikan recyclerView memiliki LayoutManager
                            if (recyclerView.getLayoutManager() == null) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            }

                            mapelAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("API Response", "Data is null or empty");
                            Toast.makeText(getContext(), "Tidak ada data mata pelajaran", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Response tidak berhasil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Gagal mengambil data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            navigationHandler.showBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }
}
