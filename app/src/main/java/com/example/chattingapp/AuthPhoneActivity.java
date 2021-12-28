package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.chattingapp.databinding.ActivityAuthPhoneBinding;

public class AuthPhoneActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    ActivityAuthPhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtPhone.addTextChangedListener(this);
        binding.edtAuthNum.addTextChangedListener(this);

        binding.btnAuth.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAuth:
                //입력한 번호로 인증번호 보내는 기능 구현
                binding.edtPhone.setEnabled(false);
                binding.btnAuth.setVisibility(View.GONE);
                binding.btnReAuth.setVisibility(View.VISIBLE);
                break;
            case R.id.btnReAuth:
                //입력한 번호로 인증번호 보내는 기능 구현
                break;
            case R.id.btnOK:
                //인증번호 확인하는 기능 구현
                finish();

                Intent pw = new Intent(this, PasswordActivity.class);
                startActivity(pw);
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                finish();

                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
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
        if(binding.edtPhone.isEnabled() == false && binding.edtAuthNum.length() > 0){
            binding.btnOK.setEnabled(true);
        }else if(binding.edtPhone.isEnabled() == true && binding.edtPhone.length() > 0){
            binding.btnAuth.setEnabled(true);
        }else if(binding.edtPhone.length() == 0){
            binding.btnAuth.setEnabled(false);
        }else if(binding.edtAuthNum.length() == 0){
            binding.btnOK.setEnabled(false);
        }
    }
}