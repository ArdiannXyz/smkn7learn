package com.example.smk7.Siswa.Model;

import com.google.gson.annotations.SerializedName;

public class MapelSiswaModel {
    @SerializedName("id")
    private String id;

    @SerializedName("nama_mapel")
    private String namaMapel;

    @SerializedName("kode_mapel")
    private String kodeMapel;

    @SerializedName("kelas")
    private String kelas;

    @SerializedName("guru")
    private String guru;

    @SerializedName("status")
    private String status;

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public String getKodeMapel() {
        return kodeMapel;
    }

    public void setKodeMapel(String kodeMapel) {
        this.kodeMapel = kodeMapel;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getGuru() {
        return guru;
    }

    public void setGuru(String guru) {
        this.guru = guru;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}