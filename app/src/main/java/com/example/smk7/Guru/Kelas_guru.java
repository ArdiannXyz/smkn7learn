package com.example.smk7.Guru;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Adapter.KelasAdapter;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas_guru extends Fragment {

    private RecyclerView recyclerView;
    private List<KelasModel> kelasList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelas_guru, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch kelas data from API
        fetchKelasData();

        return view;
    }

    private void fetchKelasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getKelasData();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", "Status: " + apiResponse.getStatus());
                    Log.d("API Response", "Message: " + apiResponse.getMessage());

                    if ("success".equals(apiResponse.getStatus())) {
                        kelasList = apiResponse.getKelasModel();
                        if (kelasList != null && !kelasList.isEmpty()) {
                            setupRecyclerView(kelasList);
                        } else {
                            Log.e("API Response", "Kelas data is empty");
                            Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API Response", "API status is not success");
                        Toast.makeText(getContext(), "API error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response failed: " + response.code() + ", message: " + response.message());
                    Toast.makeText(getContext(), "API error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<KelasModel> kelasList) {
        // Pastikan adapter hanya dipasang jika kelasList tidak null atau kosong
        if (kelasList != null && !kelasList.isEmpty()) {
            // Pass null for viewPager and false for isViewPagerRequired in this fragment
            KelasAdapter adapter = new KelasAdapter(kelasList, null, false, null);
            recyclerView.setAdapter(adapter);
        }
    }
}
