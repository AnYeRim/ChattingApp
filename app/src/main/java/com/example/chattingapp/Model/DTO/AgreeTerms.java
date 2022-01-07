package com.example.chattingapp.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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
