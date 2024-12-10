package com.example.smk7.Siswa.Model;

import com.google.gson.annotations.SerializedName;

public class MateriSiswaModel {

    @SerializedName("judul_tugas")
    private String judulTugas;

    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }
}

