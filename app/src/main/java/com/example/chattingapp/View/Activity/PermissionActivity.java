package com.example.chattingapp.View.Activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.PermissionUtils;
import com.example.chattingapp.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {

    private PermissionUtils permissionUtils;
    private ActivityUtils activityUtils;
    private ActivityPermissionBinding binding;

    final String TAG = "PermissionActivity";
    final int REQUEST_PERMISSIONS_CODE = 0;

    private String[] initPms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS
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
                if (permissionUtils.checkShouldShowRequestPermissionRationale(PermissionActivity.this, initPms)) {
                    // 거부 2번 이상 시 더이상 권한 창이 안뜨게 되어있어서 거부한 적 있으면 권한 설정 창으로 이동하게 구현.
                    permissionUtils.showDialogRequestPermission(PermissionActivity.this);
                } else {
                    // 첫 권한 요청 (요청 거부한 적 없을 때)
                    requestPermissions(initPms, REQUEST_PERMISSIONS_CODE);
                }
            }
        });

    }

    // 다른 액티비티가 전면에 나올 때
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    // 액티비티가 운영되기 직전
    @Override
    protected void onResume() {
        super.onResume();

        // 시작하거나 앱 설정 창으로 이동했다가 되돌아왔을 때, 권한 필요 여부 확인
        if (permissionUtils.checkNeedPermission(this, initPms) != true) {
            activityUtils.newActivity(this, SplashActivity.class);
            finish();
        }
    }

    // 권한 요청에 대한 결과 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 요청 코드가 REQUEST_PERMISSIONS_CODE 이고, 요청한 퍼미션 개수만큼 수신되었는지 확인
        if (requestCode == REQUEST_PERMISSIONS_CODE && grantResults.length == initPms.length) {
            // 모든 권한을 허용하였는지 확인
            for (int i = 0; i < grantResults.length; i++) {
                Log.d(TAG,"grantResults["+i+"]"+grantResults[i]);
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // 거부된 권한이 있다면, 권한이 필요한 이유와 함께 권한 재요청하는 다이어로그 창 띄우기
                    permissionUtils.showDialogRequestPermission(this);
                    return;
                }
            }
            // 모든 권한이 허용되었다면 스플래시 화면을 띄우며 권한 화면 종료.
            activityUtils.newActivity(this, SplashActivity.class);
            finish();
        } else {
            Log.d(TAG,"Permission denied");
            // 권한이 필요한 이유와 함께 권한 재요청하는 다이어로그 창 띄우기
            permissionUtils.showDialogRequestPermission(this);
        }

    }

}
