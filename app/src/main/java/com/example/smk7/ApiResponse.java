package com.example.smk7;

import com.example.smk7.Model.KelasModel;
import com.example.smk7.Model.MateriModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    // Menyimpan status API
    @SerializedName("status")
    private String status;

    // Pesan API
    @SerializedName("message")
    private String message;

    // Data Kelas
    @SerializedName("data")
    private List<KelasModel> data;

    @SerializedName("materi_model")
    private List<MateriModel> materiModel;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<KelasModel> getData() {
        return data;
    }

    public void setData(List<KelasModel> data) {
        this.data = data;
    }

    public List<MateriModel> getMateriModel() {
        return materiModel;
    }

    public void setMateriModel(List<MateriModel> materiModel) {
        this.materiModel = materiModel;
    }
}
