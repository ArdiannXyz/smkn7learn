package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class MateriModel {

    @SerializedName("id_tugas")  // Sesuaikan dengan JSON, pastikan nama field JSON sesuai
    private String idTugas;  // Menambahkan properti untuk ID tugas

    @SerializedName("judul_tugas")  // Sesuaikan dengan JSON
    private String judulTugas;

    // Getter dan Setter untuk idTugas
    public String getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(String idTugas) {
        this.idTugas = idTugas;
    }

    // Getter dan Setter untuk judulTugas
    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }
}