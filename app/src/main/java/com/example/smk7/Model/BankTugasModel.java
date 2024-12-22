package com.example.smk7.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankTugasModel {
    @SerializedName("id_pengumpulan")
    private Integer idPengumpulan;

    @SerializedName("id_siswa")
    private Integer idSiswa;

    @SerializedName("nama_siswa")
    private String namaSiswa;

    @SerializedName("nilai")
    private String nilai;

    @SerializedName("komentar")
    private String komentar;

    @SerializedName("status_pengumpulan")
    private String statusPengumpulan;

    @SerializedName("file_tugas")
    private String fileTugas;

    @SerializedName("tugas")
    private TugasInfo tugas;

    @SerializedName("kelas")
    private KelasInfo kelas;

    @SerializedName("mapel")
    private MapelInfo mapel;

    // Nested class untuk informasi tugas
    public static class TugasInfo {
        @SerializedName("id_tugas")
        private Integer idTugas;

        @SerializedName("judul_tugas")
        private String judulTugas;

        @SerializedName("deadline")
        private String deadline;

        // Getters
        public Integer getIdTugas() {
            return idTugas;
        }

        public String getJudulTugas() {
            return judulTugas != null ? judulTugas : "";
        }

        public String getDeadline() {
            return deadline != null ? deadline : "";
        }

        // Setters
        public void setIdTugas(Integer idTugas) {
            this.idTugas = idTugas;
        }

        public void setJudulTugas(String judulTugas) {
            this.judulTugas = judulTugas;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }
    }

    // Nested class untuk informasi kelas
    public static class KelasInfo {
        @SerializedName("id_kelas")
        private Integer idKelas;

        @SerializedName("nama_kelas")
        private String namaKelas;

        // Getters
        public Integer getIdKelas() {
            return idKelas;
        }

        public String getNamaKelas() {
            return namaKelas != null ? namaKelas : "";
        }

        // Setters
        public void setIdKelas(Integer idKelas) {
            this.idKelas = idKelas;
        }

        public void setNamaKelas(String namaKelas) {
            this.namaKelas = namaKelas;
        }
    }

    // Nested class untuk informasi mapel
    public static class MapelInfo {
        @SerializedName("id_mapel")
        private Integer idMapel;

        @SerializedName("kode_mapel")
        private String kodeMapel;

        @SerializedName("nama_mapel")
        private String namaMapel;

        // Getters
        public Integer getIdMapel() {
            return idMapel;
        }

        public String getKodeMapel() {
            return kodeMapel != null ? kodeMapel : "";
        }

        public String getNamaMapel() {
            return namaMapel != null ? namaMapel : "";
        }

        // Setters
        public void setIdMapel(Integer idMapel) {
            this.idMapel = idMapel;
        }

        public void setKodeMapel(String kodeMapel) {
            this.kodeMapel = kodeMapel;
        }

        public void setNamaMapel(String namaMapel) {
            this.namaMapel = namaMapel;
        }
    }

    // Constructor kosong untuk Gson
    public BankTugasModel() {
    }

    // Constructor dengan parameter
    public BankTugasModel(Integer idPengumpulan, Integer idSiswa, String namaSiswa,
                          String nilai, String komentar, String statusPengumpulan,
                          String fileTugas, TugasInfo tugas, KelasInfo kelas, MapelInfo mapel) {
        this.idPengumpulan = idPengumpulan;
        this.idSiswa = idSiswa;
        this.namaSiswa = namaSiswa;
        this.nilai = nilai;
        this.komentar = komentar;
        this.statusPengumpulan = statusPengumpulan;
        this.fileTugas = fileTugas;
        this.tugas = tugas;
        this.kelas = kelas;
        this.mapel = mapel;
    }

    // Getters dengan null check
    public Integer getIdPengumpulan() {
        return idPengumpulan;
    }

    public Integer getIdSiswa() {
        return idSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa != null ? namaSiswa : "";
    }

    public String getNilai() {
        return nilai != null ? nilai : "Belum dinilai";
    }

    public String getKomentar() {
        return komentar != null ? komentar : "";
    }

    public String getStatusPengumpulan() {
        return statusPengumpulan != null ? statusPengumpulan : "Belum mengumpulkan";
    }

    public String getFileTugas() {
        return fileTugas != null ? fileTugas : "";
    }

    public TugasInfo getTugas() {
        return tugas;
    }

    public KelasInfo getKelas() {
        return kelas;
    }

    public MapelInfo getMapel() {
        return mapel;
    }

    // Setters
    public void setIdPengumpulan(Integer idPengumpulan) {
        this.idPengumpulan = idPengumpulan;
    }

    public void setIdSiswa(Integer idSiswa) {
        this.idSiswa = idSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public void setStatusPengumpulan(String statusPengumpulan) {
        this.statusPengumpulan = statusPengumpulan;
    }

    public void setFileTugas(String fileTugas) {
        this.fileTugas = fileTugas;
    }

    public void setTugas(TugasInfo tugas) {
        this.tugas = tugas;
    }

    public void setKelas(KelasInfo kelas) {
        this.kelas = kelas;
    }

    public void setMapel(MapelInfo mapel) {
        this.mapel = mapel;
    }

    // Di BankTugasModel.java
    public static List<BankTugasModel> removeDuplicates(List<BankTugasModel> originalList) {
        if (originalList == null) return new ArrayList<>();

        Map<String, BankTugasModel> uniqueMap = new HashMap<>();

        for (BankTugasModel data : originalList) {
            // Skip jika data null
            if (data == null || data.getNamaSiswa() == null) continue;

            // Gunakan nama sebagai key
            String key = data.getNamaSiswa();
            BankTugasModel existing = uniqueMap.get(key);

            // Logika pemilihan data
            boolean shouldUpdate = false;
            if (existing == null) {
                shouldUpdate = true;
            } else if (existing.getNilai().equals("Belum dinilai") && !data.getNilai().equals("Belum dinilai")) {
                shouldUpdate = true;
            }

            if (shouldUpdate) {
                uniqueMap.put(key, data);
            }
        }

        return new ArrayList<>(uniqueMap.values());
    }

    @Override
    public String toString() {
        return "BankTugasModel{" +
                "idPengumpulan=" + idPengumpulan +
                ", idSiswa=" + idSiswa +
                ", namaSiswa='" + namaSiswa + '\'' +
                ", nilai='" + nilai + '\'' +
                ", komentar='" + komentar + '\'' +
                ", statusPengumpulan='" + statusPengumpulan + '\'' +
                ", fileTugas='" + fileTugas + '\'' +
                ", tugas=" + (tugas != null ? tugas.getJudulTugas() : "null") +
                ", kelas=" + (kelas != null ? kelas.getNamaKelas() : "null") +
                ", mapel=" + (mapel != null ? mapel.getNamaMapel() : "null") +
                '}';
    }
}
