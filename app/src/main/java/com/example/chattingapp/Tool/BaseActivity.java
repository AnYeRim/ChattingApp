package com.example.chattingapp.Tool;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Utils.SharedPreferenceUtil;

import java.io.Serializable;

public abstract class BaseActivity extends AppCompatActivity {

    public void startActivity(Class mClass) {
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    public void startActivity(Class mClass, Serializable data){
        Intent intent = new Intent(this, mClass);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmptyToken() {
        return getToken() == null;
    }

    public String getToken(){
        return SharedPreferenceUtil.getData("token");
    }

    public String getUserID(){
        return SharedPreferenceUtil.getData( "userID");
    }

}
