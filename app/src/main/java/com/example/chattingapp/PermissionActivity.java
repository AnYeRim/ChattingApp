package com.example.chattingapp;


import android.Manifest;
import android.app.Activity;
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

import com.example.chattingapp.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {

    private PermissionUtil permissionUtil;
    private ActivityUtil activityUtil;
    private ActivityPermissionBinding binding;

    private String[] initPms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        permissionUtil = new PermissionUtil();
        activityUtil = new ActivityUtil();

        binding.btnPermission.setOnClickListener(new View.OnClickListener() {
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
        if (permissionUtil.checkNeedPermission(this, initPms) != true) {
            finish();
            activityUtil.newActivity(this, LoginActivity.class);
        }
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[1])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[2])) {
            // 사용자가 권한 거부한 적 있을 때, 권한 필요한 이유 설명.
            // 거부 2번이상시 더이상 권한 창이 안뜨게 되어있어서 거부한 적 있으면 권한 설정창으로 이동하게 구현. (카톡도 이렇게 했길래)
            permissionUtil.requestPermissionMessage(this);
        } else {
            // 첫 권한 요청 (요청 거부한 적 없을 때)
            requestPermissions(initPms, 0);
        }
    }

    // 권한 확인 여부 완료후 호출
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0) {

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionUtil.requestPermissionMessage(this);
                        break;
                    }
                }

                finish();
                activityUtil.newActivity(this, LoginActivity.class);

            } else {
                permissionUtil.requestPermissionMessage(this);
            }
        }

    }

}
