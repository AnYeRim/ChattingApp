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
public class Room implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("profile_url")
    private String imageURL;

    @SerializedName("title")
    private String title;

    @SerializedName("members_total")
    private String total;

    @SerializedName("alarm")
    private String alarm;

    @SerializedName("update_at")
    private String updateDate;

    @SerializedName("message")
    private String message;

    @SerializedName("cnt_new_message")
    private int cntNewMessage;

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

    public String isAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getUpdateDate() {
        DateFormat dateFormat = null;
        DateFormat formatter = null;
        Date date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        }
        try {
            date = dateFormat.parse(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);

        if(date.getDate() == nowDate.getDate()+1){
            formatter = new SimpleDateFormat("a H:mm", Locale.KOREA);
        }else if(date.getYear() == nowDate.getYear()){
            formatter = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
        }else {
            formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        }

        return formatter.format(date);
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCntNewMessage() {
        return cntNewMessage;
    }

    public void setCntNewMessage(int cntNewMessage) {
        this.cntNewMessage = cntNewMessage;
    }
}
