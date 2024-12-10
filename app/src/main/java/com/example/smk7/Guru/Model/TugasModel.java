package com.example.smk7.Guru.Model;

import com.google.gson.annotations.SerializedName;

public class TugasModel {
    @SerializedName("deskripsi")  // Sesuaikan dengan JSON
    private String deskripsi;

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
