package com.example.smk7.ApiDatabase;

import android.util.Log;

import com.example.smk7.Model.BankTugasModel;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.Model.TugasModel;
import com.example.smk7.Siswa.Model.MapelSiswaModel;
import com.example.smk7.Siswa.Model.MateriSiswaModel;
import com.example.smk7.Siswa.Model.TugasSiswaModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    private static final String TAG = "ApiResponse";

    // Status API
    @SerializedName("status")
    private String status;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("total_data")
    private int totalData;

    // Model Data with both old and new response formats
    @SerializedName("data")
    private List<KelasModel> data;  // For new API format

    @SerializedName("tugas_data")
    private List<TugasModel> tugasDataList;

    @SerializedName("kelasModel")
    private List<KelasModel> kelasModel;  // For old API format

    @SerializedName("tugas_model")
    private List<TugasModel> tugasModel;

    @SerializedName("mapel_model")
    private List<MapelModel> mapelModel;

    @SerializedName("materi_model")
    private List<MateriModel> materiModel;

    @SerializedName("bank_tugas_model")
    private List<BankTugasModel> bankTugasModel;

    @SerializedName("mapel_siswa_model")
    private List<MapelSiswaModel> mapelSiswaModel;

    @SerializedName("tugas_siswa_model")
    private List<TugasSiswaModel> tugasSiswaModel;

    @SerializedName("materi_siswa_model")
    private List<MateriSiswaModel> materiSiswaModel;

    // Bank Tugas Response
    public static class BankTugasResponse {
        @SerializedName("status")
        private String status;

        @SerializedName("pesan")
        private String pesan;

        @SerializedName("data")
        private WrapperResponse data;

        public String getStatus() { return status; }
        public String getPesan() { return pesan; }
        public WrapperResponse getData() { return data; }
    }

    public static class WrapperResponse {
        @SerializedName("statistik")
        private Statistik statistik;

        @SerializedName("data")
        private List<BankTugasModel> data;

        public Statistik getStatistik() { return statistik; }
        public List<BankTugasModel> getData() { return data; }
    }

    public static class Statistik {
        @SerializedName("total_siswa")
        private int totalSiswa;

        @SerializedName("sudah_mengumpulkan")
        private int sudahMengumpulkan;

        @SerializedName("terlambat")
        private int terlambat;

        @SerializedName("tepat_waktu")
        private int tepatWaktu;

        public int getTotalSiswa() { return totalSiswa; }
        public int getSudahMengumpulkan() { return sudahMengumpulkan; }
        public int getTerlambat() { return terlambat; }
        public int getTepatWaktu() { return tepatWaktu; }
    }

    // Getter dan Setter untuk Status API
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    // Tambahkan method getter dan setter untuk tugasDataList
    public List<TugasModel> getTugasData() {
        if (tugasDataList == null) {
            Log.w(TAG, "getTugasData: Returning null");
        }
        return tugasDataList;
    }

    public void setTugasData(List<TugasModel> tugasDataList) {
        this.tugasDataList = tugasDataList;
        Log.d(TAG, "setTugasData: Size = " + (tugasDataList != null ? tugasDataList.size() : 0));
    }

    public boolean isTugasDataEmpty() {
        boolean isEmpty = tugasDataList == null || tugasDataList.isEmpty();
        Log.d(TAG, "isTugasDataEmpty: " + isEmpty);
        return isEmpty;
    }

    // Getter dan Setter untuk Model Data
    public List<KelasModel> getData() {
        return data;
    }

    public void setData(List<KelasModel> data) {
        this.data = data;
        Log.d(TAG, "setData: Size = " + (data != null ? data.size() : 0));
    }

    public List<KelasModel> getKelasModel() {
        if (data != null) {
            return data;
        }
        if (kelasModel == null) {
            Log.w(TAG, "getKelasModel: Returning null");
        }
        return kelasModel;
    }

    public void setKelasModel(List<KelasModel> kelasModel) {
        this.kelasModel = kelasModel;
        Log.d(TAG, "setKelasModel: Size = " + (kelasModel != null ? kelasModel.size() : 0));
    }

    public List<MapelModel> getMapelModel() {
        if (mapelModel == null) {
            Log.w(TAG, "getMapelModel: Returning null");
        }
        return mapelModel;
    }

    public void setMapelModel(List<MapelModel> mapelModel) {
        this.mapelModel = mapelModel;
        Log.d(TAG, "setMapelModel: Size = " + (mapelModel != null ? mapelModel.size() : 0));
    }

    public List<MateriModel> getMateriModel() {
        if (materiModel == null) {
            Log.w(TAG, "getMateriModel: Returning null");
        }
        return materiModel;
    }

    public void setMateriModel(List<MateriModel> materiModel) {
        this.materiModel = materiModel;
        Log.d(TAG, "setMateriModel: Size = " + (materiModel != null ? materiModel.size() : 0));
    }

    public List<TugasModel> getTugasModel() {
        if (tugasModel == null) {
            Log.w(TAG, "getTugasModel: Returning null");
        }
        return tugasModel;
    }

    public void setTugasModel(List<TugasModel> tugasModel) {
        this.tugasModel = tugasModel;
        Log.d(TAG, "setTugasModel: Size = " + (tugasModel != null ? tugasModel.size() : 0));
    }

    public List<BankTugasModel> getBankTugasModel() {
        if (bankTugasModel == null) {
            Log.w(TAG, "getBankTugasModel: Returning null");
        }
        return bankTugasModel;
    }

    public void setBankTugasModel(List<BankTugasModel> bankTugasModel) {
        this.bankTugasModel = bankTugasModel;
        if (bankTugasModel != null) {
            Log.d(TAG, "setBankTugasModel: Size = " + bankTugasModel.size());
            for (int i = 0; i < bankTugasModel.size(); i++) {
                BankTugasModel model = bankTugasModel.get(i);
                Log.d(TAG, "BankTugas[" + i + "]: nama=" + model.getNama()
                        + ", status=" + model.getStatus()
                        + ", file_tugas=" + model.getFileTugas());
            }
        } else {
            Log.w(TAG, "setBankTugasModel: Received null list");
        }
    }

    public List<MapelSiswaModel> getMapelSiswaModel() {
        if (mapelSiswaModel == null) {
            Log.w(TAG, "getMapelSiswaModel: Returning null");
        }
        return mapelSiswaModel;
    }

    public void setMapelSiswaModel(List<MapelSiswaModel> mapelSiswaModel) {
        this.mapelSiswaModel = mapelSiswaModel;
        Log.d(TAG, "setMapelSiswaModel: Size = " + (mapelSiswaModel != null ? mapelSiswaModel.size() : 0));
    }

    public List<TugasSiswaModel> getTugasSiswaModel() {
        if (tugasSiswaModel == null) {
            Log.w(TAG, "getTugasSiswaModel: Returning null");
        }
        return tugasSiswaModel;
    }

    public void setTugasSiswaModel(List<TugasSiswaModel> tugasSiswaModel) {
        this.tugasSiswaModel = tugasSiswaModel;
        Log.d(TAG, "setTugasSiswaModel: Size = " + (tugasSiswaModel != null ? tugasSiswaModel.size() : 0));
    }

    public List<MateriSiswaModel> getMateriSiswaModel() {
        if (materiSiswaModel == null) {
            Log.w(TAG, "getMateriSiswaModel: Returning null");
        }
        return materiSiswaModel;
    }

    public void setMateriSiswaModel(List<MateriSiswaModel> materiSiswaModel) {
        this.materiSiswaModel = materiSiswaModel;
        Log.d(TAG, "setMateriSiswaModel: Size = " + (materiSiswaModel != null ? materiSiswaModel.size() : 0));
    }

    // Method untuk memeriksa data kosong dengan logging
    public boolean isDataEmpty() {
        boolean isEmpty = data == null || data.isEmpty();
        Log.d(TAG, "isDataEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isKelasModelEmpty() {
        boolean isNewFormatEmpty = data == null || data.isEmpty();
        boolean isOldFormatEmpty = kelasModel == null || kelasModel.isEmpty();
        boolean isEmpty = isNewFormatEmpty && isOldFormatEmpty;
        Log.d(TAG, "isKelasModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isMapelModelEmpty() {
        boolean isEmpty = mapelModel == null || mapelModel.isEmpty();
        Log.d(TAG, "isMapelModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isMateriModelEmpty() {
        boolean isEmpty = materiModel == null || materiModel.isEmpty();
        Log.d(TAG, "isMateriModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isTugasModelEmpty() {
        boolean isEmpty = tugasModel == null || tugasModel.isEmpty();
        Log.d(TAG, "isTugasModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isBankTugasModelEmpty() {
        boolean isEmpty = bankTugasModel == null || bankTugasModel.isEmpty();
        Log.d(TAG, "isBankTugasModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isMapelSiswaModelEmpty() {
        boolean isEmpty = mapelSiswaModel == null || mapelSiswaModel.isEmpty();
        Log.d(TAG, "isMapelSiswaModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isTugasSiswaModelEmpty() {
        boolean isEmpty = tugasSiswaModel == null || tugasSiswaModel.isEmpty();
        Log.d(TAG, "isTugasSiswaModelEmpty: " + isEmpty);
        return isEmpty;
    }

    public boolean isMateriSiswaModelEmpty() {
        boolean isEmpty = materiSiswaModel == null || materiSiswaModel.isEmpty();
        Log.d(TAG, "isMateriSiswaModelEmpty: " + isEmpty);
        return isEmpty;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", totalData=" + totalData +
                ", data size=" + (data != null ? data.size() : 0) +
                ", kelasModel size=" + (kelasModel != null ? kelasModel.size() : 0) +
                ", bankTugasModel size=" + (bankTugasModel != null ? bankTugasModel.size() : 0) +
                ", mapelSiswaModel size=" + (mapelSiswaModel != null ? mapelSiswaModel.size() : 0) +
                ", tugasSiswaModel size=" + (tugasSiswaModel != null ? tugasSiswaModel.size() : 0) +
                ", materiSiswaModel size=" + (materiSiswaModel != null ? materiSiswaModel.size() : 0) +
                ", tugasData size=" + (tugasDataList != null ? tugasDataList.size() : 0) +
                '}';
    }
}