package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class User implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("fb_token")
    private String fb_token;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("nikname")
    private String nikname;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("gender")
    private String gender;

    @SerializedName("email")
    private String email;

    @SerializedName("profile_url")
    private String profile_url;

    @SerializedName("auto_add_friend")
    private boolean autoAddFriend;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }

    public boolean isAuto_add_friend() {
        return autoAddFriend;
    }

    public void setAuto_add_friend(boolean autoAddFriend) {
        this.autoAddFriend = autoAddFriend;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }
}
