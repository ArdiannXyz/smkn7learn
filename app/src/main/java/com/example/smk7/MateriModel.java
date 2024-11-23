package com.example.smk7;

import com.google.gson.annotations.SerializedName;

public class MateriModel {

    @SerializedName("nama_mapel") // Sesuai dengan JSON
    private String nama_mapel;

    public String getNama_mapel() {
        return nama_mapel;
    }
    public void setNama_mapel(String namaMapel) {
        this.nama_mapel = namaMapel;
    }

}
