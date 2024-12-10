package com.example.smk7.Siswa.Model;

import com.google.gson.annotations.SerializedName;

public class MapelSiswaModel {
    @SerializedName("nama_mapel")
    private String namaMapel;

    // Getter dan Setter
    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }
}


