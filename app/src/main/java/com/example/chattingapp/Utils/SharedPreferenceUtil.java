package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private static String fileName = "userToken";

    public static String getToken(Context context) {
        // getString(찾는 값, 디폴드 값);
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    public static void setToken(Context context, String token) {
        //commit은 동기로 저장되며 결과값 반환. apply는 비동기로 저장.
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("token", token).apply();
    }
}
