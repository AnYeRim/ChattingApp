package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.chattingapp.databinding.ActivityTermsBinding;

import java.util.ArrayList;

public class TermsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityTermsBinding binding;
    private ActivityUtil activityUtil;

    private ArrayList<DataTerms> data = new ArrayList<DataTerms>();
    private AdapterTerms adapterTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtil = new ActivityUtil();

        binding.txtBackBegin.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(this);
        binding.chkAll.setOnClickListener(this);

        setRecyclerTerm();

    }

    private void setRecyclerTerm() {
        binding.recyclerTerm.setLayoutManager(new LinearLayoutManager(this));

        data.add(new DataTerms("필수 약관1", "약관내용1", true, false));
        data.add(new DataTerms("선택 약관2", "약관내용2",false, false));
        data.add(new DataTerms("필수 약관3", "약관내용3",true, false));

        adapterTerms = new AdapterTerms(this, data);
        binding.recyclerTerm.setAdapter(adapterTerms);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkAll:
                //전체 선택 체크 상태에 따라 전체선택/해제됨
                boolean checked = ((CheckBox) view).isChecked();
                adapterTerms.setCheckedAll(checked);
                break;
            case R.id.btnAgree:
                // 필수 약관 동의 체크 후 휴대폰 인증화면으로 이동 구현해야함.
                if(adapterTerms.checkRequired()){
                    finish();
                    activityUtil.newActivity(this,AuthPhoneActivity.class);
                }else {
                    Toast.makeText(this,"필수 약관을 모두 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                finish();
                activityUtil.newActivity(this,LoginActivity.class);
                break;
        }
    }
}