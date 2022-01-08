package com.example.chattingapp.Model;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String authToken) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://15.164.191.17:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(authToken))
                .build();

        return retrofit;
    }

    private static OkHttpClient getOkHttpClient(String authToken) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        if (!TextUtils.isEmpty(authToken)) {
            AuthIntercept authIntercept = new AuthIntercept("Bearer " + authToken);
            if (!client.interceptors().contains(authIntercept)) {
                client.addInterceptor(authIntercept);
            }
        }

        return client.build();
    }
}