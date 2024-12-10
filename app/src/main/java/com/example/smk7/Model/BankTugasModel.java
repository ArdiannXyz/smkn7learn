package com.example.smk7.Model;

public class BankTugasModel {
    private String nama;
    private String status;
    private String file_tugas;
    private String idPengumpulan;

    // Constructor
    public BankTugasModel(String nama, String status, String file_tugas , String idPengumpulan) {
        this.nama = nama;
        this.status = status;
        this.file_tugas = file_tugas;
        this.idPengumpulan = idPengumpulan;
    }

    // Getters
    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }

    public String getFileTugas() {
        return file_tugas;
    }

    // Setters jika diperlukan
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFileTugas(String file_tugas) {
        this.file_tugas = file_tugas;
    }

    public String getIdPengumpulan() { return idPengumpulan; }
    public void setIdPengumpulan(String idPengumpulan) { this.idPengumpulan = idPengumpulan; }

    // Untuk debugging
    @Override
    public String toString() {
        return "BankTugasModel{" +
                "nama='" + nama + '\'' +
                ", status='" + status + '\'' +
                ", file_tugas='" + file_tugas + '\'' +
                '}';
    }
}