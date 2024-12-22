package com.example.smk7.RecycleBankTugas;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
        Call<ApiResponse> call = apiService.getBankTugasData(); // Perhatikan perubahan tipe ke ApiResponse
        call.enqueue(new Callback<ApiResponse>() { // Perubahan tipe callback
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();

                        if ("sukses".equals(apiResponse.getStatus())) {
                            List<BankTugasModel> bankTugasList = apiResponse.getBankTugasModel();

                            if (bankTugasList != null && !bankTugasList.isEmpty()) {
                                setupRecyclerView(bankTugasList);
                            } else {
                                Log.e(TAG, "Daftar bank tugas kosong");
                            }
                        } else {
                            Log.e(TAG, "API error: " + apiResponse.getMessage());
                        }
                    } else {
                        handleErrorResponse(response);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error saat memproses response: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Log.e(TAG, "Error response body: " + errorBody);
            }
            Log.e(TAG, "Error code: " + response.code());
        } catch (IOException e) {
            Log.e(TAG, "Error reading error body: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupRecyclerView(List<BankTugasModel> banktugasList) {
        // Pastikan viewPager diambil dari aktivitas utama atau instance fragment
        ViewPager2 viewPager = ((DashboardGuru) getActivity()).viewPager2;

        // Tambahkan context
        Context context = requireContext(); // Atau getActivity()

        // Buat adapter dengan meneruskan viewPager dan context
        BankTugasAdapter bankTugasAdapter = new BankTugasAdapter(banktugasList, viewPager, context);
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
