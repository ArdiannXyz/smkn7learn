package com.example.smk7.Siswa.Model;

public class SiswaModel {
    private boolean success;
    private String message;
    private Siswa data;

    public static class Siswa {
        private String nama;
        private String nisn;

        // Getter dan Setter
        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getNisn() {
            return nisn;
        }

        public void setNisn(String nisn) {
            this.nisn = nisn;
        }
    }

    // Getter dan Setter untuk SiswaResponse
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Siswa getData() {
        return data;
    }

    public void setData(Siswa data) {
        this.data = data;
    }
}
