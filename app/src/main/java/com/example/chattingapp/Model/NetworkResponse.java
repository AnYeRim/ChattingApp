package com.example.chattingapp.Model;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkResponse<ResponseType> implements Callback<ResponseType> {

    private ResponseType res = null;

    public ResponseType getRes() {
        return res;
    }

    @Override
    public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
        if (response.isSuccessful()) {
            Log.d("TAG", response.code() + "");
            res = response.body();
        }
    }

    @Override
    public void onFailure(Call<ResponseType> call, Throwable t) {

    }
}
