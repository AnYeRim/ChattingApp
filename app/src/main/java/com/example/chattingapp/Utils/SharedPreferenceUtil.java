package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chattingapp.ChattingApp;

public class SharedPreferenceUtil {

    private static String fileName = "userToken";

    public static String getData(String data_name) {
        // getString(찾는 값, 디폴드 값);
        SharedPreferences sharedPreferences = ChattingApp.getCurrentActivity().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(data_name, null);
    }

    public static void setData(String data_name, String data) {
        //commit은 동기로 저장되며 결과값 반환. apply는 비동기로 저장.
        SharedPreferences sharedPreferences = ChattingApp.getCurrentActivity().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(data_name, data).apply();
    }
}
