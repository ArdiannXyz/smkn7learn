package com.example.smk7.Guru.Model;

import com.google.gson.annotations.SerializedName;

public class KelasModel {

    // Sesuaikan dengan nama field di JSON
    @SerializedName("nama_kelas")
    private String namaKelas;

    @SerializedName("wali_kelas")
    private String waliKelas;

    @SerializedName("id_kelas")
    private String idKelas;

    // Constructor
    public KelasModel(String idKelas, String namaKelas, String waliKelas) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.waliKelas = waliKelas;
    }

    // Getter dan Setter
    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getWaliKelas() {
        return waliKelas;
    }

    public void setWaliKelas(String waliKelas) {
        this.waliKelas = waliKelas;
    }
}