package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class Message implements Serializable {

    @SerializedName("id")
    private String message_id;

    @SerializedName("room_id")
    private String room_id;

    @SerializedName("message")
    private String message;

    @SerializedName("from_id")
    private String from_id;

    @SerializedName("type")
    private String type;

    @SerializedName("read_members_id")
    private int read_members_id;

    @SerializedName("unread_total")
    private int unread_total;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public Message() {
    }

    public int getRead_members_id() {
        return read_members_id;
    }

    public void setRead_members_id(int read_members_id) {
        this.read_members_id = read_members_id;
    }

    public int getUnread_total() {
        return unread_total;
    }

    public void setUnread_total(int unread_total) {
        this.unread_total = unread_total;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        DateFormat dateFormat = null;
        DateFormat formatter = new SimpleDateFormat("a hh:mm", Locale.KOREA);
        Date date = null;
        if(created_at != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            try {
                date = dateFormat.parse(created_at);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            long now = System.currentTimeMillis()+1000*60*60*9;
            date = new Date(now);
        }
        return formatter.format(date);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
