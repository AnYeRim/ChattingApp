package com.example.chattingapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    // 권한 필요 여부 확인
    public boolean checkNeedPermission(Context mContext, String[] pmsList) {
        // 마시멜로 이전 버전은 안해도 됨
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        // 사용자가 이미 앱에 특정 권한이 부여되어있는지 확인
        for (int i=0; i<pmsList.length; i++){
            if(ContextCompat.checkSelfPermission(mContext,pmsList[i]) == PackageManager.PERMISSION_DENIED){
                return true;
            }
        }
        return false;
    }

    // 권한 거부된 적 있는지 체크
    public boolean checkShouldShowRequestPermissionRationale(Activity mActivity, String[] pmsList) {
        // 특정 권한 중 하나라도 사용자가 거부한 적이 있는지 (권한 요청 근거를 보여야하는지) 확인
        for (int i=0; i<pmsList.length; i++){
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, pmsList[i])){
                return true;
            }
        }
        return false;
    }

    // 권한 거부한 경우, 사용자가 직접 권한 설정하도록 요청하는 창의 띄운다.
    public void showDialogRequestPermission(Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("권한 요청");
        builder.setMessage("앱을 실행하기 위해 다음 권한이 필요합니다.\n전화,주소록,저장공간 권한 허용해주세요.");

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 앱 설정 창을 띄운다. (사용자가 직접 권한 설정하도록)
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
