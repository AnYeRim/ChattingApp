package com.example.chattingapp.Tool;

import com.google.gson.annotations.SerializedName;

public class BaseJson {
    @SerializedName("ERROR_CODE")
    private String error_code;

    @SerializedName("ERROR_MESSAGE")
    private String error_message;

    public String getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }
}
