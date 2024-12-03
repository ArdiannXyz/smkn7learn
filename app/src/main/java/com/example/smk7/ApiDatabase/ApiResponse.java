package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.BankTugasModel;
import com.example.smk7.Model.KelasModel;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.Model.MateriModel;
import com.example.smk7.Model.TugasModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    // Status API
    @SerializedName("status")
    private String status;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Model Data
    @SerializedName("tugas_model")
    private List<TugasModel> tugasModel;

    @SerializedName("kelasModel")
    private List<KelasModel> kelasModel;

    @SerializedName("mapel_model")
    private List<MapelModel> mapelModel;

    @SerializedName("materi_model")
    private List<MateriModel> materiModel;

    @SerializedName("bank_tugas_model")
    private List<BankTugasModel> bankTugasModel;  // Perbaikan: Tambahkan variabel untuk BankTugasModel

    // Getter dan Setter untuk status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter dan Setter untuk success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter dan Setter untuk message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter dan Setter untuk kelasModel
    public List<KelasModel> getKelasModel() {
        return kelasModel;
    }

    public void setKelasModel(List<KelasModel> kelasModel) {
        this.kelasModel = kelasModel;
    }

    // Getter dan Setter untuk mapelModel
    public List<MapelModel> getMapelModel() {
        return mapelModel;
    }

    public void setMapelModel(List<MapelModel> mapelModel) {
        this.mapelModel = mapelModel;
    }

    // Getter dan Setter untuk materiModel
    public List<MateriModel> getMateriModel() {
        return materiModel;
    }

    public void setMateriModel(List<MateriModel> materiModel) {
        this.materiModel = materiModel;
    }

    // Getter dan Setter untuk tugasModel
    public List<TugasModel> getTugasModel() {
        return tugasModel;
    }

    public void setTugasModel(List<TugasModel> tugasModel) {
        this.tugasModel = tugasModel;
    }

    // Getter dan Setter untuk bankTugasModel
    public List<BankTugasModel> getBankTugasModel() {
        return bankTugasModel;
    }

    public void setBankTugasModel(List<BankTugasModel> bankTugasModel) {
        this.bankTugasModel = bankTugasModel;
    }

    // Method untuk memeriksa apakah data kosong
    public boolean isKelasModelEmpty() {
        return kelasModel == null || kelasModel.isEmpty();
    }

    public boolean isMapelModelEmpty() {
        return mapelModel == null || mapelModel.isEmpty();
    }

    public boolean isMateriModelEmpty() {
        return materiModel == null || materiModel.isEmpty();
    }

    public boolean isTugasModelEmpty() {
        return tugasModel == null || tugasModel.isEmpty();
    }

    public boolean isBankTugasModelEmpty() {
        return bankTugasModel == null || bankTugasModel.isEmpty();
    }
}
