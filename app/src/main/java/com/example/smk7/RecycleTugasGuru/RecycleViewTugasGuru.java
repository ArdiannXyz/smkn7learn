package com.example.smk7.RecycleTugasGuru;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Adapter.TugasAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.TugasModel;
import com.example.smk7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleViewTugasGuru extends Fragment {

    private RecyclerView recyclerView;
    private TugasAdapter tugasAdapter;
    private List<TugasModel> tugasList;
    private ImageView backButton;
    private BottomNavigationHandler navigationHandler;
    private FloatingActionButton fabAddTugas;
    private static final String TAG = "RecycleViewTugasGuru";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view_tugas_guru, container, false);


        // Initialize back button
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(9, false);
            }
        });

        // Initialize FAB
        fabAddTugas = view.findViewById(R.id.fabAddTugas);
        fabAddTugas.setOnClickListener(v -> {
            if (getActivity() instanceof BottomNavigationHandler) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnav);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getContext(), UploadTugas_guru.class);
                startActivity(intent);
            }, 200);
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Fetch data
        fetchTugasData();

        return view;
    }

    private void fetchTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getTugasData();

        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d(TAG, "Raw Response: " + new Gson().toJson(apiResponse));

                    if ("sukses".equals(apiResponse.getStatus())) {
                        List<TugasModel> tugasList = apiResponse.getData() != null ?
                                new Gson().fromJson(
                                        new Gson().toJsonTree(apiResponse.getData()),
                                        new TypeToken<List<TugasModel>>(){}.getType()
                                ) : null;

                        // Log data yang diterima
                        Log.d(TAG, "Received data: " + new Gson().toJson(tugasList));

                        if (tugasList != null && !tugasList.isEmpty()) {
                            Log.d(TAG, "Tugas list size: " + tugasList.size());
                            setupRecyclerView(tugasList);
                        } else {
                            Log.e(TAG, "Tugas list is null or empty");
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Tidak ada data tugas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "Network Error: " + t.getMessage(), t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView(List<TugasModel> tugasList) {
        if (tugasList == null) {
            Log.e(TAG, "tugasList is null");
            return;
        }

        Log.d(TAG, "Setting up RecyclerView with " + tugasList.size() + " items");

        if (getContext() != null) {
            // Set layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Set adapter
            tugasAdapter = new TugasAdapter(getContext(), tugasList, new TugasAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String judulTugas, int idTugas) {
                    Intent intent = new Intent(getContext(), UploadTugas_guru.class);
                    intent.putExtra("judul_tugas", judulTugas);
                    intent.putExtra("id_tugas", idTugas);
                    startActivity(intent);
                }

                @Override
                public void onDeleteSuccess() {
                    fetchTugasData();
                }
            });

            recyclerView.setAdapter(tugasAdapter);

            // Notify adapter
            tugasAdapter.notifyDataSetChanged();

            // Log untuk memastikan adapter terpasang
            Log.d(TAG, "Adapter set to RecyclerView");
        }
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
                ((DashboardGuru) getActivity()).setSwipeEnabled(false);
            }
        }
        fetchTugasData(); // Refresh data when resuming
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