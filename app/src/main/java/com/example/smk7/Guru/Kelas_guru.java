package com.example.smk7.Guru;

import android.content.Intent;
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
import com.example.smk7.Adapter.KelasAdapter;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.R;
import com.example.smk7.Recyclemateriguru.UploadMateri_Guru;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas_guru extends Fragment {

    private RecyclerView recyclerView;
    private List<KelasModel> kelasList;

    public Kelas_guru() {
        // Default constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelas_guru, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchKelasData();
        return view;
    }

    private void fetchKelasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getKelasData();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && "success".equals(apiResponse.getStatus())) {
                        kelasList = apiResponse.getData();
                        for (KelasModel kelas : kelasList) {
                            Log.d("Kelas_guru", "Data Kelas: ID = " + kelas.getId_kelas() + ", Nama = " + kelas.getNama_kelas());
                        }
                        setupRecyclerView(kelasList);
                    } else {
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response failed: " + response.message());
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
        KelasAdapter adapter = new KelasAdapter(kelasList, (idKelas, namaKelas) -> {
            Log.d("Kelas_guru", "Mengirim ID Kelas: " + idKelas + ", Nama Kelas: " + namaKelas);
            Intent intent = new Intent(getContext(), UploadMateri_Guru.class);
            intent.putExtra("id_kelas", idKelas);
            intent.putExtra("nama_kelas", namaKelas);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
