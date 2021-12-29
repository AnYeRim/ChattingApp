package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.chattingapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ActivityLoginBinding binding;
    private ActivityUtil activityUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtil = new ActivityUtil();

        binding.edtID.addTextChangedListener(this);
        binding.edtPW.addTextChangedListener(this);

        binding.btnLogin.setOnClickListener(this);
        binding.btnJoin.setOnClickListener(this);

        binding.txtFindIDPW.setOnClickListener(this);
        
        // 로그인 되어있으면 main화면으로 넘어가기
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                //DB와 비교하여 로그인 하는 기능 구현
                break;
            case R.id.btnJoin:
                finish();
                activityUtil.newActivity(this, TermsActivity.class);
                break;
            case R.id.txtFind_ID_PW:
                //아이디 비번 찾는 화면으로 이동
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
        if(binding.edtID.length() > 0 && binding.edtPW.length() > 0){
            binding.btnLogin.setEnabled(true);
        }else{
            binding.btnLogin.setEnabled(false);
        }
    }
}