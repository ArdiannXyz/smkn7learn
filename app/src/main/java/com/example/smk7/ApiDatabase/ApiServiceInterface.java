package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.MateriModel;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiServiceInterface {
    // Metode untuk mengambil materi berdasarkan ID kelas
//    @GET("api-get_materi_by_kelas.php")
//    Call<ApiResponse> getMateriByKelas(@Query("id_kelas") int idKelas);

    // Endpoint untuk mendapatkan data kelas
    @GET("api_kelas_guru.php")
    Call<ApiResponse> getKelasData();

    @GET("api_mapelguru.php")
    Call<ApiResponse> getMapelData();

    @GET("api_materiguru.php")
    Call<ApiResponse> getMateriData();

    @GET("api_tugasguru.php")
    Call<ApiResponse> getTugasData();

    @GET("api_banktugasguru.php")
    Call<ApiResponse> getBankTugasData();


    @GET("api-get_materi.php")
    Call<List<MateriModel>> getMateri();

    @FormUrlEncoded
    @POST("api-tambah_materi.php")
    Call<ResponseBody> tambahMateri(
            @Field("id_guru") int idGuru,
            @Field("jenis_materi") String jenisMateri, // Pastikan ini sesuai dengan format BLOB
            @Field("judul_tugas") String judulTugas,
            @Field("deskripsi") String deskripsi,
            @Field("id_kelas") int idKelas,
            @Field("deadline") String deadline, // Pastikan ini dalam format datetime yang benar
            @Field("video_url") String videoUrl
    );

    @FormUrlEncoded
    @POST("api-update_materi.php")
    Call<ResponseBody> updateMateri(
            @Field("id_tugas") int idTugas,
            @Field("id_guru") int idGuru,
            @Field("jenis_materi") String jenisMateri, // Pastikan ini sesuai dengan format BLOB
            @Field("judul_tugas") String judulTugas,
            @Field("deskripsi") String deskripsi,
            @Field("id_kelas") int idKelas,
            @Field("deadline") String deadline, // Pastikan ini dalam format datetime yang benar
            @Field("video_url") String videoUrl
    );

    @FormUrlEncoded
    @POST("api-hapus_materi.php")
    Call<ResponseBody> hapusMateri(
            @Body RequestBody requestBody
//            @Field("log_info") String logInfo // Tambahan untuk debugging
    );

    @FormUrlEncoded
    @POST("api-kelas_guru.php") // Ganti dengan endpoint yang sesuai
    Call<ResponseBody> tambahKelas(
            @Field("id_kelas") String idKelas,
            @Field("nama_kelas") String namaKelas
    );

}









//    // API
//    @POST("api-crud.php?action=createMateri")
//    Call<ApiResponse> createMateri(@Body MateriModel materi);
//
//    @PUT("api-crud.php?action=updateMateri")
//    Call<ApiResponse> updateMateri(@Body MateriModel materi);
//
//    @DELETE("api-crud.php?action=deleteMateri")
//    Call<ApiResponse> deleteMateri(@Query("id_tugas") String idTugas);


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
//

//}