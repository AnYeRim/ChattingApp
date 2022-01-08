package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class Friends {

    @SerializedName("friend_id")
    private String id;

    @SerializedName("friend_name")
    private String nikName;

//    @SerializedName("")
//    private String message;

    @SerializedName("profile_url")
    private String imageURL;

    public Friends(String id, String nikName, String imageURL) {
        this.id = id;
        this.nikName = nikName;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNikName() {
        return nikName;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
