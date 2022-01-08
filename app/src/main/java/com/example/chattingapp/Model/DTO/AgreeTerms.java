package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class AgreeTerms implements Serializable {

    @SerializedName("terms")
    private ArrayList<Terms> terms;

    @SerializedName("phone")
    private String user_phone;

    public AgreeTerms(ArrayList<Terms> terms, String user_phone) {
        this.terms = terms;
        this.user_phone = user_phone;
    }
}
