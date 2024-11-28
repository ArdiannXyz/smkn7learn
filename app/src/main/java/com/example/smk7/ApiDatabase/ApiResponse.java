package com.example.smk7.ApiDatabase;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.Model.MapelModel;
import com.example.smk7.Model.MateriModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    // Status API
    @SerializedName("status")
    private String status;

    // Pesan API
    @SerializedName("message")
    private String message;

    // Data Kelas - Ubah serialized name sesuai dengan respons terbaru
    @SerializedName("kelasModel")
    private List<KelasModel> kelasModel;

    // Data Mapel - Ubah serialized name sesuai dengan respons terbaru
    @SerializedName("mapel_model")
    private List<MapelModel> mapelModel;

    // Data Materi - Ubah serialized name sesuai dengan respons terbaru
    @SerializedName("materi_model")
    private List<MateriModel> materiModel;

    // Getter dan Setter untuk status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // Menambahkan method untuk memeriksa jika response kosong
    public boolean isKelasModelEmpty() {
        return kelasModel == null || kelasModel.isEmpty();
    }

    public boolean isMapelModelEmpty() {
        return mapelModel == null || mapelModel.isEmpty();
    }

    public boolean isMateriModelEmpty() {
        return materiModel == null || materiModel.isEmpty();
    }
}
