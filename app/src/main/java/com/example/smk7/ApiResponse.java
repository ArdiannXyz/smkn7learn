package com.example.smk7;
import com.example.smk7.kelasmodel;

import java.util.List;

public class ApiResponse {

    private String status;
    private String message;
    private List<kelasmodel> data;

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
    public List<kelasmodel> getData() {
        return data;
    }
    public void setData(List<kelasmodel> data) {
        this.data = data;
    }
}
