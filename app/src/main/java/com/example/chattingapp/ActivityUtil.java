package com.example.chattingapp;

import android.content.Context;
import android.content.Intent;

public class ActivityUtil {

    public void newActivity(Context mContext, Class mClass){
        Intent intent = new Intent(mContext, mClass);
        mContext.startActivity(intent);
    }
}
