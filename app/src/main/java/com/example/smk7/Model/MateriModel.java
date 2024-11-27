package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class MateriModel {
    @SerializedName("judul_tugas")  // Sesuaikan dengan JSON
    private String judulTugas;

    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }
}
