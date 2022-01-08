package com.example.chattingapp.Model.VO;

import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Tool.BaseJson;
import com.google.gson.annotations.SerializedName;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class JsonUser extends BaseJson {

    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
