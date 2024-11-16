package com.example.smk7;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceInterface {

    // Define the endpoint to fetch kelas data (GET request)
    @GET("api_kelas_guru.php")  // Replace with your actual API endpoint path
    Call<ApiResponse> getKelasData();
}
