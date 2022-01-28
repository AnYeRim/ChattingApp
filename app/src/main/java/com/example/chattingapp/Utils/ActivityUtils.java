package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

    public void newActivity(Context mContext, Class mClass){
        Intent intent = new Intent(mContext, mClass);
        mContext.startActivity(intent);
    }

    public String getToken(Context context){
        return SharedPreferenceUtil.getData(context, "token");
    }

    public String getNikName(Context context){
        return SharedPreferenceUtil.getData(context, "nikname");
    }
}
