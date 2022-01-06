package com.example.chattingapp.Model;

import com.example.chattingapp.Model.DTO.Terms;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("Terms")
    Call<ArrayList<Terms>> doGetTermsList();
}
