package com.example.smk7.ApiDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            String baseUrl = Db_Contract.BASE_URL;
            if (baseUrl == null || baseUrl.isEmpty()) {
                throw new IllegalArgumentException("Base URL is not set in Db_Contract.");
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)  // Memastikan baseUrl valid
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
