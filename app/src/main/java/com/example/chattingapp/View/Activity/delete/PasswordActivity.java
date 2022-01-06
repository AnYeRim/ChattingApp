package com.example.chattingapp.View.Activity.delete;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ActivityPasswordBinding binding;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();

        binding.txtPhone.setText("010-1111-2222");

        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);

        binding.edtPW.addTextChangedListener(this);
        binding.edtCheckPW.addTextChangedListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                //입력한 번호로 인증번호 보내는 기능 구현
                String pw = String.valueOf(binding.edtPW.getText());
                String chkPW = String.valueOf(binding.edtCheckPW.getText());

                if(pw.equals(chkPW)){
                    //폰번호 비밀번호 기억하며 다음 회원가입 경로로 이동 구현해야함
                    finish();
                    activityUtils.newActivity(this, JoinInfoActivity.class);
                    break;
                }else {
                    Toast.makeText(this,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                finish();
                activityUtils.newActivity(this, LoginActivity.class);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(binding.edtPW.length() > 0 && binding.edtCheckPW.length() > 0){
            binding.btnOK.setEnabled(true);
        }
    }

}