package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Adapter.AdapterTerms;
import com.example.chattingapp.databinding.ActivityTermsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityTermsBinding binding;
    private ActivityUtils activityUtils;

    private APIInterface apiInterface;
    private Terms resTerms;
    private ArrayList<Terms> data;
    private AdapterTerms adapterTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Terms> call = apiInterface.doGetTermsList();
        System.out.println("------------ call ----------------");
        call.enqueue(new Callback<Terms>() {
            @Override
            public void onResponse(Call<Terms> call, Response<Terms> response) {
                System.out.println("------------ response ----------------");
                Log.d("TAG", response.code() + "");
                resTerms = response.body();

                data = new ArrayList<Terms>();
                data.add(resTerms);

                binding.recyclerTerm.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapterTerms = new AdapterTerms(getApplicationContext(), data);
                binding.recyclerTerm.setAdapter(adapterTerms);
            }

            @Override
            public void onFailure(Call<Terms> call, Throwable t) {
                System.out.println("------------ fail ----------------");
                Log.d("TAG", t.getMessage());
                call.cancel();
            }
        });

        activityUtils = new ActivityUtils();

        binding.txtBackBegin.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(this);
        binding.chkAll.setOnClickListener(this);

        setRecyclerTerm();

    }

    private void setRecyclerTerm() {
/*
        binding.recyclerTerm.setLayoutManager(new LinearLayoutManager(this));

        adapterTerms = new AdapterTerms(this, data);
        binding.recyclerTerm.setAdapter(adapterTerms);*/
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
                if (adapterTerms.checkRequired()) {
                    finish();
                    activityUtils.newActivity(this, AuthPhoneActivity.class);
                } else {
                    Toast.makeText(this, "필수 약관을 모두 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                finish();
                activityUtils.newActivity(this, LoginActivity.class);
                break;
        }
    }
}