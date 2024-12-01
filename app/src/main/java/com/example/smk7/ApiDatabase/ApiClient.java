package com.example.smk7.ApiDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    // Mendapatkan instance Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Db_Contract.BASE_URL)  // Ganti dengan URL base API Anda
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Mendapatkan ApiServiceInterface
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
