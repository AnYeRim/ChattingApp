package com.example.chattingapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PermissionActivity extends AppCompatActivity {

    private String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        if(chkPermission()){
            finish();
        }

        Button btnPermit = findViewById(R.id.btnPermit);

        btnPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(permissionList, 0);
            }
        });

    }

    // 권한 확인
    private boolean chkPermission() {

        int[] chk = new int[permissionList.length];

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finish();
        }

        for(int i=0; i<permissionList.length; i++) {
            chk[i] = ContextCompat.checkSelfPermission(this, permissionList[i]);
        }

        if(chk[0] == PackageManager.PERMISSION_GRANTED
                && chk[1] == PackageManager.PERMISSION_GRANTED
                && chk[2] == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }

    }

    // 권한 확인 여부 완료후 호출
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    finish();
                }else {
                    // 권한 허용해달라고 메세지창 띄우기
                }
            }else{
                // 권한 허용해달라고 메세지창 띄우기
            }
        }

    }
}
