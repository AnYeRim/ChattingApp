package com.example.chattingapp.View.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.R;
import com.example.chattingapp.View.Fragment.LoginFragment;

public class LoginJoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_join);

        ChattingApp.setCurrentActivity(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new LoginFragment()).commit();
    }

}