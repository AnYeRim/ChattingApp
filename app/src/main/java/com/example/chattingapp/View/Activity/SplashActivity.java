package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.os.Handler;

import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isEmptyToken()) {
                startActivity(LoginActivity.class);
            }else {
                startActivity(MainActivity.class);
            }
            finish();
        }, 1000);
    }

}