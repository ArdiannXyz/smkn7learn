package com.example.smk7;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceInterface {
    @GET("api_kelas_guru.php")
    Call<ApiResponse> getKelasData();
}
