package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.MateriModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServiceInterface {
    @GET("api_kelas_guru.php")
    Call<ApiResponse> getKelasData();
    @GET("api_mapelguru.php")
    Call<ApiResponse> getMapelData();
    @GET("api_materiguru.php")
    Call<ApiResponse> getMateriData();

    // Endpoint untuk update materi
    @POST("api-materi.php?action=updateMateri")
    Call<ApiResponse> updateMateri(@Body MateriModel materi);

    // Endpoint untuk delete materi
    @POST("api-materi.php?action=deleteMateri")
    Call<ApiResponse> deleteMateri(@Body String idTugas);
}
