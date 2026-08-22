package com.example.chatapplication.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getApiService() {
        if(apiService == null) {
            // 1. setup http logging to view live network traffic in logcat
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.Body);

            // 2. build OkHttpClient with 15 second timeouts
            OkHttpClient okHttpClient = new OkHttpClient.builder()
                    .addInterceptor(loggingInterceptor)
                    .connectionTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            // 3. build retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
