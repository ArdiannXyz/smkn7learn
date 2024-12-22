package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

public class BankTugasModel {
    @SerializedName("nama")
    private String nama;

    @SerializedName("status")
    private String status;

    @SerializedName("file_tugas")
    private String file_tugas;

    @SerializedName("id_pengumpulan")
    private Integer id_pengumpulan;

    @SerializedName("info_tambahan")
    private InfoTambahan info_tambahan;

    // Inner class untuk info tambahan
    public static class InfoTambahan {
        @SerializedName("judul_tugas")
        private String judul_tugas;

        @SerializedName("deadline")
        private String deadline;

        @SerializedName("kelas")
        private String kelas;

        @SerializedName("mapel")
        private String mapel;

        @SerializedName("status_pengumpulan")
        private String status_pengumpulan;

        // Constructor
        public InfoTambahan() {}

        // Getters dengan null check
        public String getJudulTugas() {
            return judul_tugas != null ? judul_tugas : "";
        }

        public String getDeadline() {
            return deadline != null ? deadline : "";
        }

        public String getKelas() {
            return kelas != null ? kelas : "";
        }

        public String getMapel() {
            return mapel != null ? mapel : "";
        }

        public String getStatusPengumpulan() {
            return status_pengumpulan != null ? status_pengumpulan : "";
        }

        // Setters
        public void setJudulTugas(String judul_tugas) {
            this.judul_tugas = judul_tugas;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public void setKelas(String kelas) {
            this.kelas = kelas;
        }

        public void setMapel(String mapel) {
            this.mapel = mapel;
        }

        public void setStatusPengumpulan(String status_pengumpulan) {
            this.status_pengumpulan = status_pengumpulan;
        }

        @Override
        public String toString() {
            return "InfoTambahan{" +
                    "judul_tugas='" + (judul_tugas != null ? judul_tugas : "null") + '\'' +
                    ", deadline='" + (deadline != null ? deadline : "null") + '\'' +
                    ", kelas='" + (kelas != null ? kelas : "null") + '\'' +
                    ", mapel='" + (mapel != null ? mapel : "null") + '\'' +
                    ", status_pengumpulan='" + (status_pengumpulan != null ? status_pengumpulan : "null") + '\'' +
                    '}';
        }
    }

    // Constructor kosong untuk Gson
    public BankTugasModel() {
    }

    // Constructor dengan parameter
    public BankTugasModel(String nama, String status, String file_tugas, Integer id_pengumpulan, InfoTambahan info_tambahan) {
        this.nama = nama;
        this.status = status;
        this.file_tugas = file_tugas;
        this.id_pengumpulan = id_pengumpulan;
        this.info_tambahan = info_tambahan;
    }

    // Getters dengan null check
    public String getNama() {
        return nama != null ? nama : "";
    }

    public String getStatus() {
        return status != null ? status : "Belum dinilai";
    }

    public String getFileTugas() {
        return file_tugas != null ? file_tugas : "";
    }

    public Integer getIdPengumpulan() {
        return id_pengumpulan;
    }

    public InfoTambahan getInfoTambahan() {
        return info_tambahan != null ? info_tambahan : new InfoTambahan();
    }

    // Setters
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFileTugas(String file_tugas) {
        this.file_tugas = file_tugas;
    }

    public void setIdPengumpulan(Integer id_pengumpulan) {
        this.id_pengumpulan = id_pengumpulan;
    }

    public void setInfoTambahan(InfoTambahan info_tambahan) {
        this.info_tambahan = info_tambahan;
    }

    @Override
    public String toString() {
        return "BankTugasModel{" +
                "nama='" + (nama != null ? nama : "null") + '\'' +
                ", status='" + (status != null ? status : "null") + '\'' +
                ", file_tugas='" + (file_tugas != null ? file_tugas : "null") + '\'' +
                ", id_pengumpulan=" + id_pengumpulan +
                ", info_tambahan=" + (info_tambahan != null ? info_tambahan.toString() : "null") +
                '}';
    }
}