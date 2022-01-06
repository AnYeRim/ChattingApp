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
import com.example.chattingapp.databinding.FragmentAuthPhoneBinding;

public class AuthPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private FragmentAuthPhoneBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthPhoneBinding.inflate(inflater, container, false);

        binding.edtPhone.addTextChangedListener(this);
        binding.edtAuthNum.addTextChangedListener(this);

        binding.btnAuth.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);

        return binding.getRoot();
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new PasswordFragment()).commit();
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                //지금까지 데이터 지우기
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new LoginFragment()).commit();
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