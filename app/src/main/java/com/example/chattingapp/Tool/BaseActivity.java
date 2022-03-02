package com.example.chattingapp.Tool;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Utils.SharedPreferenceUtil;

public abstract class BaseActivity extends AppCompatActivity {

    public void startActivity(Class mClass) {
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmptyToken() {
        return getToken() == null;
    }

    public String getToken(){
        return SharedPreferenceUtil.getData(this, "token");
    }

    public String getUserID(){
        return SharedPreferenceUtil.getData(this, "userID");
    }

}
