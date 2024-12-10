package com.example.smk7.Model;

public class BankTugasModel {
    private String nama;
    private String status;

    // Constructor
    public BankTugasModel(String nama, String status) {
        this.nama = nama;
        this.status = status;
    }

    // Getter methods
    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }
}
