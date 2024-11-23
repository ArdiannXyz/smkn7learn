package com.example.smk7;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ClassModel> data;

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

    public List<ClassModel> getData() {
        return data;
    }

    public void setData(List<ClassModel> data) {
        this.data = data;
    }

    public List<MateriModel> getMateriModel() {
        return materiModel;
    }

    public void setMateriModel(List<MateriModel> materiModel) {
        this.materiModel = materiModel;
    }
}
