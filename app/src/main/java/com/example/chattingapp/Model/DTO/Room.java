package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class Room implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("profile_url")
    private String imageURL;

    @SerializedName("title")
    private String title;

    @SerializedName("member_total")
    private String total;

    @SerializedName("room_alarm")
    private boolean alarm;

    @SerializedName("update_date")
    private String updateDate;

    @SerializedName("message")
    private String message;

    @SerializedName("new_message")
    private int newMessage;

    public Room(String id, String imageURL, String title, String message, String total,
                boolean alarm, String updateDate, int newMessage) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.message = message;
        this.total = total;
        this.alarm = alarm;
        this.updateDate = updateDate;
        this.newMessage = newMessage;
    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }
}
