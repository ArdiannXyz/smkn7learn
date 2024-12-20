package com.example.smk7.ApiDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

            // Create a lenient Gson instance
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Add logging interceptor for debugging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configure OkHttpClient with timeouts and logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    // Method to reset retrofit instance if needed
    public static void resetRetrofit() {
        retrofit = null;
    }
}