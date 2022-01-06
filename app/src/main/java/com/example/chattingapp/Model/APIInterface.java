package com.example.chattingapp.Model;

import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("Terms")
    Call<ArrayList<Terms>> doGetTermsList();

    @POST("Terms")
    Call<ArrayList<Terms>> doCreateAgreeTerms(@Body ArrayList<Terms> terms);

    @POST("User")
    Call<User> doCreateUser(@Body User user);



}
