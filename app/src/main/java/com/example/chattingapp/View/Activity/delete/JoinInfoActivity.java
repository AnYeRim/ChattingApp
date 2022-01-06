package com.example.chattingapp.View.Activity.delete;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityJoinInfoBinding;

public class JoinInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityJoinInfoBinding binding;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJoinInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();

        binding.btnOK.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOK:
                // 지금까지 모은 데이터 보관하며 이메일 인증 화면 띄우기.
                // 자동 친구 추가 체크되었는지 확인해서 그것도 데이터 가지고 있어야 함
                if(binding.edtNikname.length() > 0
                        && binding.edtBirthDay.length() > 0
                        && !binding.spnGender.getSelectedItem().toString().equals("성별")){

                    finish();
                    activityUtils.newActivity(this, AuthEmailActivity.class);

                }else {
                    Toast.makeText(this,"정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}