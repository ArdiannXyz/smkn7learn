package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class TugasModel {

    @SerializedName("id_tugas")
    private int idTugas;

    @SerializedName("id_kelas")
    private int idKelas;

    @SerializedName("id_mapel")
    private int idMapel;

    @SerializedName("judul_tugas")
    private String judulTugas;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("file_tugas")
    private String fileTugas;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("nama_guru")
    private String namaGuru;

    @SerializedName("nama_mapel")
    private String namaMapel;

    @SerializedName("nama_kelas")
    private String namaKelas;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("statistik_pengumpulan")
    private StatistikPengumpulan statistikPengumpulan;

    // Inner class untuk statistik
    public static class StatistikPengumpulan {
        @SerializedName("total_siswa")
        private int totalSiswa;

        @SerializedName("sudah_mengumpulkan")
        private int sudahMengumpulkan;

        @SerializedName("tepat_waktu")
        private int tepatWaktu;

        @SerializedName("terlambat")
        private int terlambat;

        @SerializedName("belum_mengumpulkan")
        private int belumMengumpulkan;

        @SerializedName("persentase_pengumpulan")
        private double persentasePengumpulan;

        // Getters
        public int getTotalSiswa() { return totalSiswa; }
        public int getSudahMengumpulkan() { return sudahMengumpulkan; }
        public int getTepatWaktu() { return tepatWaktu; }
        public int getTerlambat() { return terlambat; }
        public int getBelumMengumpulkan() { return belumMengumpulkan; }
        public double getPersentasePengumpulan() { return persentasePengumpulan; }
    }

    // Constructor kosong untuk Gson
    public TugasModel() {}

    // Getters
    public int getIdTugas() { return idTugas; }
    public int getIdKelas() { return idKelas; }
    public int getIdMapel() { return idMapel; }
    public String getJudulTugas() { return judulTugas; }
    public String getDeskripsi() { return deskripsi; }
    public String getFileTugas() { return fileTugas; }
    public String getDeadline() { return deadline; }
    public String getNamaGuru() { return namaGuru; }
    public String getNamaMapel() { return namaMapel; }
    public String getNamaKelas() { return namaKelas; }
    public String getCreatedAt() { return createdAt; }
    public StatistikPengumpulan getStatistikPengumpulan() { return statistikPengumpulan; }
}