package com.example.smk7.Guru.Model;

import com.google.gson.annotations.SerializedName;

public class MateriModel {
    @SerializedName("id_materi")
    private int idMateri;

    @SerializedName("id_guru")
    private int idGuru;

    @SerializedName("id_mapel")
    private int idMapel;

    @SerializedName("id_kelas")
    private int idKelas;

    @SerializedName("judul_materi")
    private String judulMateri;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("file_materi")
    private String fileMateri;

    @SerializedName("nama_guru")
    private String namaGuru;

    @SerializedName("nama_mapel")
    private String namaMapel;

    @SerializedName("nama_kelas")
    private String namaKelas;

    @SerializedName("tanggal_dibuat")
    private String tanggalDibuat;

    @SerializedName("is_active")
    private boolean isActive;

    // Constructor
    public MateriModel(int idMateri, int idGuru, int idMapel, int idKelas,
                       String judulMateri, String deskripsi, String fileMateri,
                       String namaGuru, String namaMapel, String namaKelas,
                       String tanggalDibuat, boolean isActive) {
        this.idMateri = idMateri;
        this.idGuru = idGuru;
        this.idMapel = idMapel;
        this.idKelas = idKelas;
        this.judulMateri = judulMateri;
        this.deskripsi = deskripsi;
        this.fileMateri = fileMateri;
        this.namaGuru = namaGuru;
        this.namaMapel = namaMapel;
        this.namaKelas = namaKelas;
        this.tanggalDibuat = tanggalDibuat;
        this.isActive = isActive;
    }

    // Getters
    public int getIdMateri() { return idMateri; }
    public int getIdGuru() { return idGuru; }
    public int getIdMapel() { return idMapel; }
    public int getIdKelas() { return idKelas; }
    public String getJudulMateri() { return judulMateri; }
    public String getDeskripsi() { return deskripsi; }
    public String getFileMateri() { return fileMateri; }
    public String getNamaGuru() { return namaGuru; }
    public String getNamaMapel() { return namaMapel; }
    public String getNamaKelas() { return namaKelas; }
    public String getTanggalDibuat() { return tanggalDibuat; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setIdMateri(int idMateri) { this.idMateri = idMateri; }
    public void setIdGuru(int idGuru) { this.idGuru = idGuru; }
    public void setIdMapel(int idMapel) { this.idMapel = idMapel; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }
    public void setJudulMateri(String judulMateri) { this.judulMateri = judulMateri; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setFileMateri(String fileMateri) { this.fileMateri = fileMateri; }
    public void setNamaGuru(String namaGuru) { this.namaGuru = namaGuru; }
    public void setNamaMapel(String namaMapel) { this.namaMapel = namaMapel; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }
    public void setTanggalDibuat(String tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }
    public void setActive(boolean active) { isActive = active; }
}