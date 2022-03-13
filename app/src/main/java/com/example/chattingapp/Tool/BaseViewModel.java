package com.example.chattingapp.Tool;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.chattingapp.ChattingApp;

import java.io.Serializable;

public abstract class BaseViewModel extends ViewModel {

    public Activity getCurrentActivity(){
        return ChattingApp.getCurrentActivity();
    }

    public void startActivity(Class mClass) {
        Intent intent = new Intent(getCurrentActivity(), mClass);
        getCurrentActivity().startActivity(intent);
    }

    public void startActivity(Class mClass, Serializable data){
        Intent intent = new Intent(getCurrentActivity(), mClass);
        intent.putExtra("data", data);
        getCurrentActivity().startActivity(intent);
    }

    public void showMessage(String message){
        Toast.makeText(getCurrentActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
