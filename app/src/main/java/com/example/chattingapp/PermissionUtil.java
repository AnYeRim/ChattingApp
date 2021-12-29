package com.example.chattingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public boolean checkNeedPermission(Context mContext, String[] pmsList) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 마시멜로 이전 버전은 안해도 됨.
            return false;
        }

        // 사용자가 이미 앱에 특정 권한을 부여했는지 확인
        for (int i=0; i<pmsList.length; i++){
            if(ContextCompat.checkSelfPermission(mContext,pmsList[i]) == PackageManager.PERMISSION_DENIED){
                return true;
            }
        }

        return false;
    }

    public void requestPermissionMessage(Context mContext){
        // 권한 거부한 경우 띄우는 권한 허용 요청 메세지 창
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("권한 요청");
        builder.setMessage("앱을 실행하기 위해 다음 권한이 필요합니다.\n전화,주소록,저장공간 권한 허용해주세요.");

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
