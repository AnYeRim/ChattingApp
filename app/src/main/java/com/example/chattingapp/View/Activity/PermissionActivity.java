package com.example.chattingapp.View.Activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.PermissionUtils;
import com.example.chattingapp.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {

    private PermissionUtils permissionUtils;
    private ActivityUtils activityUtils;
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

        permissionUtils = new PermissionUtils();
        activityUtils = new ActivityUtils();

        binding.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 시작하거나 권한설정창으로 나갔다 들어와서 다시 실행될 때, 권한 필요 여부 확인
        if (permissionUtils.checkNeedPermission(this, initPms) != true) {
            activityUtils.newActivity(this, SplashActivity.class);
            finish();
        }
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[1])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, initPms[2])) {
            // 사용자가 권한 거부한 적 있을 때, 권한 필요한 이유 설명.
            // 거부 2번이상시 더이상 권한 창이 안뜨게 되어있어서 거부한 적 있으면 권한 설정창으로 이동하게 구현. (카톡도 이렇게 했길래)
            permissionUtils.requestPermissionMessage(this);
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
                        permissionUtils.requestPermissionMessage(this);
                        return;
                    }
                }

                activityUtils.newActivity(this, SplashActivity.class);
                finish();

            } else {
                permissionUtils.requestPermissionMessage(this);
            }
        }

    }

}
