package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

    public void newActivity(Context mContext, Class mClass){
        Intent intent = new Intent(mContext, mClass);
        mContext.startActivity(intent);
    }

    public boolean checkToken(Context context){
        if (SharedPreferenceUtil.getToken(context) != null) {
            return true;
        }else {
            return false;
        }
    }
}
