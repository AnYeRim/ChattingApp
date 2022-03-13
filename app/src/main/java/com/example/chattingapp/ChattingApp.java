package com.example.chattingapp;

import android.app.Activity;
import android.app.Application;

public class ChattingApp extends Application {
    private static volatile Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        ChattingApp.currentActivity = currentActivity;
    }

}
