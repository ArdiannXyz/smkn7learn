package com.example.smk7.ApiDatabase;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    // Mendapatkan instance Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Logging Interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttpClient dengan timeout dan logging
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Db_Contract.BASE_URL)  // Ganti dengan URL base API Anda
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Mendapatkan ApiServiceInterface
    public static ApiServiceInterface getApiService() {
        return getRetrofitInstance().create(ApiServiceInterface.class);
    }
}