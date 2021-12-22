package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        PermissionActivity p = new PermissionActivity();
        if(p.chkPermission()){

        }
        */
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);

    }



}