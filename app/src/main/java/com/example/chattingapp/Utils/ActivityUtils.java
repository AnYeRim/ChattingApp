package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class ActivityUtils {

    public void newActivity(Context mContext, Class mClass){
        Intent intent = new Intent(mContext, mClass);
        mContext.startActivity(intent);
    }

    public void newActivity(Context mContext, Class mClass, Serializable data){
        Intent intent = new Intent(mContext, mClass);
        intent.putExtra("data", data);
        mContext.startActivity(intent);
    }

    public String getToken(Context context){
        return SharedPreferenceUtil.getData(context, "token");
    }

    public String getNikName(Context context){
        return SharedPreferenceUtil.getData(context, "nikname");
    }

    public String getUserID(Context context){
        return SharedPreferenceUtil.getData(context, "userID");
    }
}
