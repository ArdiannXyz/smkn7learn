package com.example.smk7;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceInterface {
    @GET("api_kelas_guru.php")
    Call<ApiResponse> getKelasData();
    @GET("api_mapelguru.php")
    Call<ApiResponse> getMapelData();
    @GET("api_materiguru.php")
    Call<ApiResponse> getMateriData();
}
