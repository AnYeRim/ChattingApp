package com.example.chattingapp.View.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity {

    private ActivityPermissionBinding binding;

    private final String TAG = "PermissionActivity";
    private final int REQUEST_PERMISSIONS_CODE = 0;

    private final String[] initPms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPermission.setOnClickListener(view -> showRequestPermissions());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (noNeedAgreePermissions()) {
            showSplash();
        }
    }

    private boolean noNeedAgreePermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return !checkDeniedPermissions(getApplicationContext(), initPms);
    }

    private void showSplash() {
        startActivity(SplashActivity.class);
        finish();
    }

    private void showRequestPermissions() {
        if (checkDeniedPermissions(PermissionActivity.this, initPms)) {
            showDialogRequestPermissions();
            return;
        }

        requestPermissions(initPms, REQUEST_PERMISSIONS_CODE);
    }

    // ?????? ????????? ?????? ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (isDifferentResult(requestCode, grantResults)) {
            Log.d(TAG, "Fail onRequestPermissionsResult");
            return;
        }

        if (checkDeniedPermissions(grantResults)) {
            showDialogRequestPermissions();
            return;
        }

        showSplash();
    }

    // ?????? ????????? ??????, ???????????? ?????? ?????? ??????????????? ???????????? ?????? ?????????.
    public void showDialogRequestPermissions() {
        new AlertDialog.Builder(this)
                .setTitle("?????? ??????")
                .setMessage("?????? ???????????? ?????? ?????? ????????? ???????????????.\n??????,?????????,???????????? ?????? ??????????????????.")
                .setPositiveButton("??????", (dialogInterface, i) -> showSettingView(this))
                .setNegativeButton("??????", null)
                .create()
                .show();
    }

    // ??? ?????? ?????? ?????????
    private void showSettingView(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    private boolean isDifferentResult(int requestCode, @NonNull int[] grantResults) {
        return !(requestCode == REQUEST_PERMISSIONS_CODE) || grantResults.length != initPms.length;
    }

    private boolean checkDeniedPermissions(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (isDenied(grantResult)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDeniedPermissions(Context mContext, String[] pmsList) {
        for (String permission : pmsList) {
            if (isDenied(ContextCompat.checkSelfPermission(mContext, permission))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDeniedPermissions(Activity mActivity, String[] pmsList) {
        for (String permission : pmsList) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDenied(int grantResult) {
        return grantResult == PackageManager.PERMISSION_DENIED;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
