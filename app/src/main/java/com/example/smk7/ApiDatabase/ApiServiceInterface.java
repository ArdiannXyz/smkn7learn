package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.MateriModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiServiceInterface {

    // Endpoint untuk mendapatkan data kelas
    @GET("api_kelas_guru.php")
    Call<ApiResponse> getKelasData();

    // Endpoint untuk mendapatkan data mapel
    @GET("api_mapelguru.php")
    Call<ApiResponse> getMapelData();

    // Endpoint untuk mendapatkan data materi
    @GET("api_materiguru.php")
    Call<ApiResponse> getMateriData();

    @GET("api_tugasguru.php")
    Call<ApiResponse> getTugasData();




//    // Endpoint untuk mendapatkan materi berdasarkan ID kelas
//    @GET("api-crud.php?action=getMateriByKelas")
//    Call<List<MateriModel>> getMateriByKelas(@Query("id_kelas") String id_kelas);
//
//    // Endpoint untuk update materi
//    @POST("api-crud.php?action=updateMateri")
//    Call<ApiResponse> updateMateri(@Body MateriModel materi);
//
//    // Endpoint untuk delete materi
//    @POST("api-crud.php?action=deleteMateri")
//    Call<ApiResponse> deleteMateri(@Body MateriModel materi); // Kirim MateriModel atau ID Tugas tergantung API


    // API untuk Create Materi
    @POST("api-crud.php?action=createMateri")
    Call<ApiResponse> createMateri(@Body MateriModel materi);

    // API untuk Update Materi
    @PUT("api-crud.php?action=updateMateri")
    Call<ApiResponse> updateMateri(@Body MateriModel materi);

    // API untuk Delete Materi
    @DELETE("api-crud.php?action=deleteMateri")
    Call<ApiResponse> deleteMateri(@Query("id_tugas") String idTugas);
}
