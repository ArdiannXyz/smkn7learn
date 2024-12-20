package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class KelasModel {
    @SerializedName("id_kelas")
    private String idKelas;

    @SerializedName("nama_kelas")
    private String namaKelas;

    @SerializedName("tahun_ajaran")
    private String tahunAjaran;

    @SerializedName("wali_kelas")
    private WaliKelas waliKelas;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Nested WaliKelas class
    public static class WaliKelas {
        @SerializedName("id")
        private String id;

        @SerializedName("nama")
        private String nama;

        @SerializedName("email")
        private String email;

        @SerializedName("nip")
        private String nip;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNip() { return nip; }
        public void setNip(String nip) { this.nip = nip; }
    }

    // Getters and setters for KelasModel
    public String getIdKelas() { return idKelas; }
    public void setIdKelas(String idKelas) { this.idKelas = idKelas; }

    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

    public String getTahunAjaran() { return tahunAjaran; }
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran = tahunAjaran; }

    public WaliKelas getWaliKelas() { return waliKelas; }
    public void setWaliKelas(WaliKelas waliKelas) { this.waliKelas = waliKelas; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}