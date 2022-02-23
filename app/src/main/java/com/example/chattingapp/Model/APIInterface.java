package com.example.chattingapp.Model;

import com.example.chattingapp.Model.DTO.AgreeTerms;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.JSONFriends;
import com.example.chattingapp.Model.VO.JsonUser;
import com.example.chattingapp.Model.VO.ResponseData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<JSONFriends> doGetFriendsList();

    @POST("friends")
    Call<Friends> doAddFriends(@Body Friends friends);


    @GET("room")
    Call<ArrayList<Room>> doFindRoom(@Query("friend_id") String friend_id);

    @POST("room")
    Call<Room> doCreateRoom(@Body ArrayList<Friends> friends);

    @GET("room/{room_id}")
    Call<Room> doGetRoom(@Path("room_id") String room_id);

    @GET("room/{room_id}/message")
    Call<ArrayList<Message>> doGetMessage(@Path("room_id") String room_id);

    @POST("room/message")
    Call<Message> doSendMessage(@Body Message message);

    @PUT("room/message")
    Call<ResponseData> doUpdateReadMessage();

}
