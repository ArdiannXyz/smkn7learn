package com.example.smk7;

import java.util.List;

public class ApiResponse {

    private String status;
    private String message;
    private List<ClassModel> data;

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
}
