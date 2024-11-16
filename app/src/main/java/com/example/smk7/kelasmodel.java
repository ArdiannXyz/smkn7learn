package com.example.smk7;

import java.util.List;

public class kelasmodel {

    private String id_kelas;
    private String nama_kelas;
    private String tahun_ajaran;
    private String wali_kelas;

    // Getter and Setter for id_kelas
    public String getid_kelas() {
        return id_kelas;
    }

    public void setId_kelas(String id_kelas) {
        this.id_kelas = id_kelas;
    }

    // Getter and Setter for nama_kelas
    public String getnama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }

    // Getter and Setter for tahun_ajar
    public String gettahun_ajar() {
        return tahun_ajaran;
    }

    public void setTahun_ajar(String tahun_ajar) {
        this.tahun_ajaran = tahun_ajar;
    }

    // Getter and Setter for wali_kelas
    public String getwali_kelas() {
        return wali_kelas;
    }

    public void setWali_kelas(String wali_kelas) {
        this.wali_kelas = wali_kelas;
    }
}
