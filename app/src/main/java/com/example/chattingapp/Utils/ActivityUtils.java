package com.example.chattingapp.Utils;

import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

    public void newActivity(Context mContext, Class mClass){
        Intent intent = new Intent(mContext, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent);
    }
}
