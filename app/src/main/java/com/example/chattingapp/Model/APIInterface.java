package com.example.chattingapp.Model;

import com.example.chattingapp.Model.DTO.Terms;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("Terms")
    Call<Terms> doGetTermsList();
}
