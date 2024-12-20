package com.example.smk7.Siswa.Model;

import com.google.gson.annotations.SerializedName;

public class TugasSiswaModel {
    @SerializedName("id")
    private String id;

    @SerializedName("judul_tugas")
    private String judulTugas;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("tanggal_mulai")
    private String tanggalMulai;

    @SerializedName("tanggal_selesai")
    private String tanggalSelesai;

    @SerializedName("file_tugas")
    private String fileTugas;

    @SerializedName("nilai")
    private String nilai;

    @SerializedName("status")
    private String status;

    @SerializedName("mapel")
    private String mapel;

    @SerializedName("kelas")
    private String kelas;

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(String tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getFileTugas() {
        return fileTugas;
    }

    public void setFileTugas(String fileTugas) {
        this.fileTugas = fileTugas;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}

