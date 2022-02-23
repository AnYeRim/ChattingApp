package com.example.chattingapp.Model.VO;

import com.google.gson.annotations.SerializedName;

//DTO란 전달하는 데이터가 저장되어있는 객체. (서버로 보내는 값)
//VO는 Read를 목적으로 값이 저장되어있는 객체로 값의 변경이 없다. (서버에서 받은 값)
public class ResponseData<T> {

    @SerializedName("insertId")
    private int insertId;

    @SerializedName("affectedRows")
    private int affectedRows;

    @SerializedName("data")
    private T data;

    public int getInsertId() {
        return insertId;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public T getData() {
        return data;
    }
}
