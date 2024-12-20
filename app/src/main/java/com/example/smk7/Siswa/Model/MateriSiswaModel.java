package com.example.smk7.Siswa.Model;


import com.google.gson.annotations.SerializedName;

public class MateriSiswaModel {
    @SerializedName("id_materi")
    private String idMateri;

    @SerializedName("judul_materi")
    private String judulMateri;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("file_materi")
    private String fileMateri;

    @SerializedName("original_filename")
    private String originalFilename;

    @SerializedName("nama_mapel")
    private String namaMapel;

    @SerializedName("nama_kelas")
    private String namaKelas;

    @SerializedName("nama_guru")
    private String namaGuru;

    // Getters and Setters
    public String getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(String idMateri) {
        this.idMateri = idMateri;
    }

    public String getJudulMateri() {
        return judulMateri;
    }

    public void setJudulMateri(String judulMateri) {
        this.judulMateri = judulMateri;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFileMateri() {
        return fileMateri;
    }

    public void setFileMateri(String fileMateri) {
        this.fileMateri = fileMateri;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }
}

