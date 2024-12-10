package com.example.smk7.RecycleBankTugas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.Guru.Adapter.BankTugasAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Guru.Model.BankTugasModel;
import com.example.smk7.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyleViewBankTugas_Guru extends Fragment {

    private RecyclerView recyclerView;
    private List<BankTugasModel> bankTugasList;
    private ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_tugas_view_guru, container, false);

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;
                viewPager.setCurrentItem(10, false);
            }
        });

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Memanggil method untuk mengambil data dari API
        fetchBankTugasData();

        return view;
    }

    private void fetchBankTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getBankTugasData();  // Sesuaikan dengan endpoint API Anda
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if ("success".equals(apiResponse.getStatus())) {
                        bankTugasList = apiResponse.getBankTugasModel();  // Pastikan ini sesuai dengan model API Anda
                        if (bankTugasList != null && !bankTugasList.isEmpty()) {
                            setupRecyclerView(bankTugasList);
                        } else {
                            // Handle empty list
                        }
                    } else {
                        // Handle API error
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        String errorBody = "";
        try {
            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log error response
    }

    private void setupRecyclerView(List<BankTugasModel> bankTugasList) {
        // Pastikan ViewPager2 diambil dari aktivitas utama atau fragment yang sesuai
        ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

        // Buat adapter dan set pada RecyclerView
        BankTugasAdapter bankTugasAdapter = new BankTugasAdapter(bankTugasList, viewPager);
        recyclerView.setAdapter(bankTugasAdapter);
    }
}
