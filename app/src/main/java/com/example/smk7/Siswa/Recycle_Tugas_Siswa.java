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
import com.example.smk7.R;
import com.example.smk7.Siswa.Adapter.TugasSiswaAdapter;
import com.example.smk7.Siswa.Model.TugasSiswaModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recycle_Tugas_Siswa extends Fragment {

    private ImageView backButton;
    private BottomNavigationHandler navigationHandler;
    private RecyclerView recyclerView;
    private ViewPager2 viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tugas_siswa, container, false);

        // Initializing backButton and its click listener
        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof DashboardSiswa) {
                ViewPager2 viewPager = ((DashboardSiswa) getActivity()).viewPager2;
                if (viewPager != null) {
                    viewPager.setCurrentItem(0, false);  // Navigate to the first item
                }
            }
        });

        // Initializing RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch the tasks
        fetchTugasData();

        return view;
    }

    private void fetchTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getTugasData2();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", "Full Response: " + apiResponse.toString());

                    if ("success".equals(apiResponse.getStatus())) {
                        List<TugasSiswaModel> tugasList = apiResponse.getTugasSiswaModel();

                        if (tugasList != null && !tugasList.isEmpty()) {
                            if (isAdded() && getActivity() != null) {
                                // Initialize the ViewPager2 from the activity
                                viewPager = getActivity().findViewById(R.id.Viewpagersiswa);
                                if (viewPager == null) {
                                    Log.e("Error", "ViewPager2 not found!");
                                }

                                Fragment currentFragment = getParentFragment() != null ? getParentFragment() : Recycle_Tugas_Siswa.this;
                                TugasSiswaAdapter tugasSiswaAdapter = new TugasSiswaAdapter(tugasList, viewPager, currentFragment);
                                recyclerView.setAdapter(tugasSiswaAdapter);
                            }
                        } else {
                            Log.e("API Response", "No tasks available");
                            Toast.makeText(getContext(), "No tasks available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "API error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Response", "Response unsuccessful or body is null");
                    Toast.makeText(getContext(), "Response not successful or body is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("API Failure", "Failed to fetch data: " + t.getMessage());
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
            throw new ClassCastException(context.toString() + " must implement BottomNavigationHandler");
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
