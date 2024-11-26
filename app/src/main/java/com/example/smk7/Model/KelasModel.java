    package com.example.smk7.Model;

    import com.google.gson.annotations.SerializedName;

    public class KelasModel {

        @SerializedName("nama_kelas") // Sesuaikan dengan nama JSON field
        private String nama_kelas;

        @SerializedName("wali_kelas") // Sesuaikan dengan nama JSON field
        private String wali_kelas;

        @SerializedName("id_kelas") // Sesuaikan dengan nama field di API/JSON
        private String id_kelas;


        public String getId_kelas() {
            return id_kelas;
        }


        public String getNama_kelas() {
            return nama_kelas;
        }

        public void setNama_kelas(String nama_kelas) {
            this.nama_kelas = nama_kelas;
        }

        public String getWali_kelas() {
            return wali_kelas;
        }

        public void setWali_kelas(String wali_kelas) {
            this.wali_kelas = wali_kelas;
        }
    }
