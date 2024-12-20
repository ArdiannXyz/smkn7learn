package com.example.smk7.ApiDatabase;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServiceInterface {

    // API untuk Recyleview
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

    //API Untuk Siswa
    @GET("api_mapelsiswa.php")
    Call<ApiResponse> getMapelData2();

    @GET("api_materisiswa.php")
    Call<ApiResponse> getMateriData2();

    @GET("api_tugassiswa.php")
    Call<ApiResponse> getTugasData2();

    @GET("api_siswa.php")
    Call<ApiResponse> getSiswa();


    //API untuk Materi Guru
    @GET("api-get_materi_by_id.php")
    Call<ResponseBody> getMateriById(@Query("id_materi") int idMateri);

    @Multipart
    @POST("api-tambah_materi.php")
    Call<ResponseBody> tambahMateri(
            @Part("id_guru") RequestBody idGuru,
            @Part("id_mapel") RequestBody idMapel,
            @Part("id_kelas") RequestBody idKelas,
            @Part("judul_materi") RequestBody judulMateri,
            @Part("deskripsi") RequestBody deskripsi,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("api-update_materi.php")
    Call<ResponseBody> updateMateri(
            @Part("id_materi") RequestBody idMateri,
            @Part("id_mapel") RequestBody idMapel,
            @Part("id_kelas") RequestBody idKelas,
            @Part("id_guru") RequestBody idGuru,
            @Part("judul_materi") RequestBody judulMateri,
            @Part("deskripsi") RequestBody deskripsi,
            @Part MultipartBody.Part file
    );

    @POST("api-hapus_materi.php")
    Call<ResponseBody> hapusMateri(@Body RequestBody requestBody
    );


    //API untuk Tugas Guru
    @GET("api-get_tugas_by_id.php")
    Call<ResponseBody> getTugasById(@Query("id_tugas") int idTugas);

    @Multipart
    @POST("api-tambah_tugas.php")
    Call<ResponseBody> uploadTugas(
            @Part("id_guru") RequestBody idGuru,
            @Part("id_mapel") RequestBody idMapel,
            @Part("id_kelas") RequestBody idKelas,
            @Part("judul_tugas") RequestBody judulTugas,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("deadline") RequestBody deadline,
            @Part MultipartBody.Part file
    );

    //tanpa file
    @FormUrlEncoded
    @POST("api-updateTugas.php")
    Call<ResponseBody> updateTugas(
            @Field("id_tugas") int idTugas,
            @Field("id_guru") int idGuru,
            @Field("judul_tugas") String judulTugas,
            @Field("keterangan") String keterangan,
            @Field("id_kelas") int idKelas,
            @Field("deadline") String deadline
    );

    //dengan file
    @Multipart
    @POST("api-updateTugas.php")
    Call<ResponseBody> updateTugasWithFile(
            @Part("id_tugas") RequestBody idTugas,
            @Part("id_guru") RequestBody idGuru,
            @Part("judul_tugas") RequestBody judulTugas,
            @Part("deskripsi") RequestBody deskripsi,  // Gunakan deskripsi
            @Part("id_kelas") RequestBody idKelas,
            @Part("deadline") RequestBody deadline,
            @Part MultipartBody.Part file
    );

    //@FormUrlEncoded
    @POST("api-hapus_tugas.php")
    Call<ApiResponse> hapusTugas(@Body Map<String, Integer> body);


    //CRUD Bank Tugas Guru Buat Update Nilai
    @Multipart
    @POST("api-update_nilai.php")
    Call<ApiResponse> uploadFileAndNilai(
            @Part("id_pengumpulan") RequestBody idPengumpulan,
            @Part("nilai") RequestBody nilai,
            @Part MultipartBody.Part file
    );


















//    @FormUrlEncoded
//    @POST("update_tugas.php")
//    Call<ResponseBody> updateTugas(
//            @Field("id_tugas") int idTugas,
//            @Field("id_guru") int idGuru,
//            @Field("judul_tugas") String judulTugas,
//            @Field("deskripsi") String deskripsi,    // Gunakan deskripsi
//            @Field("id_kelas") int idKelas,
//            @Field("deadline") String deadline
//    );
//
//
//    @Multipart
//    @POST("api-update_materi.php")
//    Call<ResponseBody> updateTugasWithFile(
//            @Part("id_tugas") RequestBody idTugas,
//            @Part("id_guru") RequestBody idGuru,
//            @Part("judul_tugas") RequestBody judulTugas,
//            @Part("keterangan") RequestBody keterangan,
//            @Part("id_kelas") RequestBody idKelas,
//            @Part("deadline") RequestBody deadline,
//            @Part MultipartBody.Part file
//    );
//
//    @FormUrlEncoded
//    @POST("api-update_nilai.php")
//    Call<ApiResponse> postNilai(
//            @Field("lampiran") String lampiran,
//            @Field("nilai") float nilai
//    );



//    @FormUrlEncoded
//    @POST("api-kelas_guru.php") // Ganti dengan endpoint yang sesuai
//    Call<ResponseBody> tambahKelas(
//            @Field("id_kelas") String idKelas,
//            @Field("nama_kelas") String namaKelas
//    );
//
//    @GET("api-get_materi.php")
//    Call<List<MateriModel>> getMateri();
//
//
//    @POST("api-update_nilai.php")
//    Call<ApiResponse> postNilai(
//            @Query("file_tugas") String fileTugas,
//            @Query("nilai") float nilai
//    );


}
