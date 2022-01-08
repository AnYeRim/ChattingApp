package com.example.chattingapp.Model.VO;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Tool.BaseJson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class JSONFriends extends BaseJson {

    @SerializedName("friends")
    private ArrayList<Friends> friends;

    public ArrayList<Friends> getFriends() {
        return friends;
    }
}
