package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private ActivityUtils activityUtils;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        activityUtils = new ActivityUtils();

        binding.edtID.addTextChangedListener(this);
        binding.edtPW.addTextChangedListener(this);

        binding.btnLogin.setOnClickListener(this);
        binding.btnJoin.setOnClickListener(this);

        binding.txtFindIDPW.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                //DB와 비교하여 로그인 하는 기능 구현

                activityUtils.newActivity(getActivity(), SplashActivity.class);
                getActivity().finish();
                break;
            case R.id.btnJoin:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new TermsFragment()).commit();
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