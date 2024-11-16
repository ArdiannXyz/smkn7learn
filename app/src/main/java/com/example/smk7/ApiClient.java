package com.example.smk7;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    // Get Retrofit instance
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Db_Contract.BASE_URL)  // Use the base URL from Db_Contract
                    .addConverterFactory(GsonConverterFactory.create())  // Gson converter for JSON
                    .build();
        }
        return retrofit;
    }

    // Get ApiService instance
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
