package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.databinding.ActivityJoinInfoBinding;

public class JoinInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityJoinInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJoinInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnOK.setOnClickListener(this);
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
                    Intent authEmail = new Intent(this, AuthEmailActivity.class);
                    startActivity(authEmail);
                }else {
                    Toast.makeText(this,"정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}