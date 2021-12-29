package com.example.chattingapp.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityAuthEmailBinding;

public class AuthEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAuthEmailBinding binding;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();

        binding.txtLater.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLater:
                // 서버로 회원가입 정보 넘겨서 추가하고 메인화면 띄우기
                finish();
                activityUtils.newActivity(this, MainActivity.class);
                break;
            case R.id.btnSend:
                if(binding.chkTerm.isChecked() == true){
                    // 입력한 이메일로 인증번호 보내기
                    binding.linearSendMail.setVisibility(View.GONE);
                    binding.linearAuthNum.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(this,"개인정보 이용 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnOK:
                // 인증번호 입력된 값과 보낸 값이 같은지 비교하고 맞으면 회원가입 완료하기
                if(true) {
                    finish();
                    activityUtils.newActivity(this, MainActivity.class);
                }
                break;
        }

    }
}