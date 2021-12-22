package com.example.chattingapp;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

        Button btnPermit = findViewById(R.id.btnPermit);

        btnPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 시작하거나 권한설정창으로 나갔다 들어와서 다시 실행될 때, 권한 필요 여부 확인
        if(checkNeedPermission() != true){
            finish();
        }
    }

    // 권한이 필요한지 확인
    public boolean checkNeedPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 마시멜로 이전 버전은 안해도 됨.
            return false;
        }

        // 사용자가 이미 앱에 특정 권한을 부여했는지 확인
        if (ContextCompat.checkSelfPermission(this, permissionList[0])
                == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, permissionList[1])
                == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, permissionList[2])
                == PackageManager.PERMISSION_GRANTED) {
                return false;
        }

        return true;
    }

    private void requestPermission(){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionList[0])
            || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionList[1])
            || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionList[2])) {
                // 사용자가 권한 거부한 적 있을 때, 권한 필요한 이유 설명.
                // 거부 2번이상시 더이상 권한 창이 안뜨게 되어있어서 거부한 적 있으면 권한 설정창으로 이동하게 구현. (카톡도 이렇게 했길래)
                requestPermissionMessage();
            } else {
                // 첫 권한 요청 (요청 거부한 적 없을 때)
                requestPermissions(permissionList, 0);
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
                    requestPermissionMessage();
                }
            }else{
                requestPermissionMessage();
            }
        }

    }

    private void requestPermissionMessage(){
        // 권한 거부한 경우 띄우는 권한 허용 요청 메세지 창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 요청");
        builder.setMessage("앱을 실행하기 위해 다음 권한이 필요합니다.\n전화,주소록,저장공간 권한 허용해주세요.");

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
