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

import com.example.smk7.ApiResponse;
import com.example.smk7.ApiService;
import com.example.smk7.ApiServiceInterface;
import com.example.smk7.kelasadapter;
import com.example.smk7.kelasmodel;
import com.example.smk7.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas_guru extends Fragment {

    private RecyclerView recyclerView;
    private kelasadapter kelasAdapter;
    private List<kelasmodel> kelasList;

    public Kelas_guru() {
        // Required empty public constructor
    }

    public static Kelas_guru newInstance(String param1, String param2) {
        Kelas_guru fragment = new Kelas_guru();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kelas_guru, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch data from API
        fetchKelasData();

        return view;
    }

    private void fetchKelasData() {
        // Access ApiService to get Retrofit instance
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);

        // Call the API to fetch kelas data
        Call<ApiResponse> call = apiService.getKelasData();

        // Execute API request asynchronously
        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", apiResponse != null ? apiResponse.toString() : "No response body");
                    if (apiResponse != null && "success".equals(apiResponse.getStatus())) {
                        kelasList = apiResponse.getData();
                        kelasAdapter = new kelasadapter(kelasList);
                        recyclerView.setAdapter(kelasAdapter);
                    } else {
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the full response details for debugging
                    Log.e("API Error", "Response failed with code: " + response.code());
                    Toast.makeText(getContext(), "API error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage());
            }
        });
    }
}
