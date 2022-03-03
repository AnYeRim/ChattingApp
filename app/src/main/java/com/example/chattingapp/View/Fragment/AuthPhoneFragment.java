package com.example.chattingapp.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.databinding.FragmentAuthPhoneBinding;

import java.util.ArrayList;

public class AuthPhoneFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

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

        setPhoneNumber();

        return binding.getRoot();
    }

    private void setPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission")
        String phoneNum = telephonyManager.getLine1Number();
        if (phoneNum.startsWith("+82")) {
            phoneNum = phoneNum.replace("+82", "0");
        }
        binding.edtPhone.setText(phoneNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAuth:
                //TODO 입력한 번호로 인증번호 보내는 기능 구현
                binding.edtPhone.setEnabled(false);
                binding.btnAuth.setText("재인증");
                break;
            case R.id.btnOK:
                //TODO 인증번호 확인하는 기능 구현
                changeFragment(R.id.frg_container, new PasswordFragment(), getBundle());
                break;
            case R.id.txtBackBegin:
                changeFragment(R.id.frg_container, new LoginFragment());
                break;
        }
    }

    @NonNull
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Terms", getTerms());
        bundle.putSerializable("User", getUser());
        return bundle;
    }

    @NonNull
    private User getUser() {
        User user = new User();
        user.setPhone(binding.edtPhone.getText().toString());
        return user;
    }

    private ArrayList<Terms> getTerms() {
        return (ArrayList<Terms>) getArguments().getSerializable("Terms");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setEnabledBtnAuth();
        setEnabledBtnOK();
    }

    private void setEnabledBtnOK() {
        if (!binding.edtPhone.isEnabled() && binding.edtAuthNum.length() > 0) {
            binding.btnOK.setEnabled(true);
            return;
        }

        binding.btnOK.setEnabled(false);
    }

    private void setEnabledBtnAuth() {
        if (binding.edtPhone.length() >= 10) {
            binding.btnAuth.setEnabled(true);
            return;
        }

        binding.btnAuth.setEnabled(false);
    }
}