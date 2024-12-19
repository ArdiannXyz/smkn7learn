package com.example.smk7.ApiDatabase;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHelper {

    public static String post(String urlString, HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            StringBuilder postData = new StringBuilder();
            for (HashMap.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(param.getKey()).append('=').append(param.getValue());
            }

            OutputStream os = conn.getOutputStream();
            os.write(postData.toString().getBytes());
            os.flush();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            Log.e("APIHelper", "Error: " + e.getMessage());
        }
        return result.toString();
    }

    public void getMateriData(Callback<ApiResponse> callback) {
        try {

            URL url = new URL(Db_Contract.urlApiTambahMateri); // Ganti dengan endpoint API Anda
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "close");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Mengurai respons JSON menjadi objek ApiResponse menggunakan Gson
                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                callback.onResponse(null, Response.success(apiResponse));
            } else {
                callback.onFailure(null, new IOException("Error: " + conn.getResponseCode() + " " + conn.getResponseMessage()));
            }
        } catch (Exception e) {
            callback.onFailure(null, e);
        }
    }

    public void hapusMateri(int idTugas, Callback<ResponseBody> callback) {
        try {
            Log.d("ApiHelper", "Deleting materi with ID: " + idTugas);
            URL url = new URL(Db_Contract.urlApiHapusMateri + idTugas); // Ganti dengan endpoint API Anda
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Connection", "close");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                callback.onResponse(null, Response.success(null));
            } else {
                callback.onFailure(null, new IOException("Error: " + conn.getResponseCode() + " " + conn.getResponseMessage()));
            }
        } catch (Exception e) {
            callback.onFailure(null, e);
        }
    }
}