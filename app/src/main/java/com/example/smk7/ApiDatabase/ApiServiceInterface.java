package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.MateriModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiServiceInterface {
    // Metode untuk mengambil materi berdasarkan ID kelas
    @GET("api_get_materi_by_id.php")
    Call<ResponseBody> getMateriById(@Query("id_materi") int idMateri);

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



    //api untuk tugas
    @GET("api-get_materi_by_id.php")
    Call<ResponseBody> getTugasById(@Query("id_tugas") int idTugas);

    @FormUrlEncoded
    @POST("api-update_materi.php")
    Call<ResponseBody> updateTugas(
            @Field("id_tugas") int idTugas,
            @Field("id_guru") int idGuru,
            @Field("judul_tugas") String judulTugas,
            @Field("keterangan") String keterangan,
            @Field("id_kelas") int idKelas,
            @Field("deadline") String deadline
    );

    // Untuk upload dengan file
    @Multipart
    @POST("api-update_materi.php")
    Call<ResponseBody> updateTugasWithFile(
            @Part("id_tugas") RequestBody idTugas,
            @Part("id_guru") RequestBody idGuru,
            @Part("judul_tugas") RequestBody judulTugas,
            @Part("keterangan") RequestBody keterangan,
            @Part("id_kelas") RequestBody idKelas,
            @Part("deadline") RequestBody deadline,
            @Part MultipartBody.Part file
    );

//    @FormUrlEncoded
//    @POST("api-update_nilai.php")
//    Call<ApiResponse> postNilai(
//            @Field("lampiran") String lampiran,
//            @Field("nilai") float nilai
//    );

    @Multipart
    @POST("api-update_nilai.php")
    Call<ApiResponse> uploadFileAndNilai(
            @Part("id_pengumpulan") RequestBody idPengumpulan,  // Ganti nama parameter sesuai API
            @Part("nilai") RequestBody nilai,
            @Part MultipartBody.Part file
    );

    @POST("api-update_nilai.php")
    Call<ApiResponse> postNilai(
            @Query("file_tugas") String fileTugas,
            @Query("nilai") float nilai
    );

    //@FormUrlEncoded
    @POST("api-hapus_tugas.php")
    Call<ApiResponse> hapusTugas(@Body Map<String, Integer> body);

}









