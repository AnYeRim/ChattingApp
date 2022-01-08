package com.example.chattingapp.Model;

import com.example.chattingapp.Model.DTO.AgreeTerms;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.JsonUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("terms")
    Call<ArrayList<Terms>> doGetTermsList();

    @POST("agree-terms")
    Call<AgreeTerms> doCreateAgreeTerms(@Body AgreeTerms agreeTerms);

    // https + get으로 해야함. http일때는 get은 위험하니 post로
    @POST("user/login")
    Call<JsonUser> doGetUser(@Body User user);

    @POST("user")
    Call<User> doCreateUser(@Body User user);

    @GET("friends")
    Call<ArrayList<Friends>> doGetFriendsList();

}
