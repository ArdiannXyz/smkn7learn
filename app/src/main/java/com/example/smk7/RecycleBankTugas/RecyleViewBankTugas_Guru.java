package com.example.smk7.RecycleBankTugas;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.example.smk7.Adapter.BankTugasAdapter;
import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.Guru.DashboardGuru;
import com.example.smk7.Model.BankTugasModel;
import com.example.smk7.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyleViewBankTugas_Guru extends Fragment {

    private RecyclerView recyclerView;
    private BankTugasAdapter bankTugasAdapter;
    private List<BankTugasModel> bankTugasList;
    private ImageView backButton;
    private BottomNavigationHandler navigationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_tugas_view_guru, container, false);

        backButton = view.findViewById(R.id.back_Button);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardGuru) {
                ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

                // Pindahkan langsung ke halaman DashboardGuruFragment (halaman 0)
                viewPager.setCurrentItem(10, false);  // false berarti tanpa animasi untuk perpindahan langsung

                // Aktifkan kembali swipe setelah perpindahan selesai
            }
        });



        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchBankTugasData();

        return view;
    }

    private void fetchBankTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getBankTugasData();  // Pastikan ini sesuai dengan endpoint Anda
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse.toString());

                    if ("success".equals(apiResponse.getStatus())) {
                        bankTugasList = apiResponse.getBankTugasModel();
                        if (bankTugasList != null && !bankTugasList.isEmpty()) {
                            setupRecyclerView(bankTugasList);
                        } else {
                            Log.e("API Response", "bankTugasList is null or empty");
                        }
                    } else {
                        Log.e("API Response", "API error: " + apiResponse.getMessage());
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage(), t);
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
            Log.e("API Error", "Error reading error body: " + e.getMessage());
        }
        Log.e("API Error", "Response failed with code: " + response.code() +
                ", message: " + response.message() +
                ", errorBody: " + errorBody);
    }

    private void setupRecyclerView(List<BankTugasModel> banktugasList) {
        // Pastikan viewPager diambil dari aktivitas utama atau instance fragment
        ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

        // Buat adapter dengan meneruskan viewPager
        BankTugasAdapter bankTugasAdapter = new BankTugasAdapter(banktugasList, viewPager);
        recyclerView.setAdapter(bankTugasAdapter);
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
