package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class TugasModel {
    @SerializedName("id_tugas")
    private int idTugas;

    @SerializedName("id_guru")
    private int idGuru;

    @SerializedName("jenis_materi")
    private byte[] jenisMateri; // Tipe BLOB, disimpan sebagai byte array

    @SerializedName("judul_tugas")
    private String judulTugas;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("id_kelas")
    private int idKelas;

    @SerializedName("tanggal_dibuat")
    private String tanggalDibuat; // Tipe datetime, bisa disimpan sebagai String

    @SerializedName("deadline")
    private String deadline; // Tipe datetime, bisa disimpan sebagai String

    @SerializedName("video_url")
    private String videoUrl;


    @SerializedName("nama_kelas")
    private String namaKelas;

    // Konstruktor
    public TugasModel(int idTugas, int idGuru, byte[] jenisMateri, String judulTugas, String deskripsi, int idKelas, String tanggalDibuat, String deadline, String videoUrl, String NamaKelas) {
        this.idTugas = idTugas;
        this.idGuru = idGuru;
        this.jenisMateri = jenisMateri;
        this.judulTugas = judulTugas;
        this.deskripsi = deskripsi;
        this.idKelas = idKelas;
        this.tanggalDibuat = tanggalDibuat;
        this.deadline = deadline;
        this.videoUrl = videoUrl;
        this.namaKelas = NamaKelas;
    }

    // Getter dan Setter
    public int getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(int idTugas) {
        this.idTugas = idTugas;
    }

    public int getIdGuru() {
        return idGuru;
    }

    public void setIdGuru(int idGuru) {
        this.idGuru = idGuru;
    }

    public byte[] getJenisMateri() {
        return jenisMateri;
    }

    public void setJenisMateri(byte[] jenisMateri) {
        this.jenisMateri = jenisMateri;
    }

    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getTanggalDibuat() {
        return tanggalDibuat;
    }

    public void setTanggalDibuat(String tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getNamaKelas() { return namaKelas;}
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas;}

}