package com.example.chattingapp.View.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.FragmentUtil;
import com.example.chattingapp.databinding.FragmentAuthPhoneBinding;

import java.util.ArrayList;

public class AuthPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private FragmentAuthPhoneBinding binding;
    private FragmentUtil fragmentUtil;
    private User user;
    private ArrayList<Terms> terms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthPhoneBinding.inflate(inflater, container, false);

        binding.edtPhone.addTextChangedListener(this);
        binding.edtAuthNum.addTextChangedListener(this);

        binding.btnAuth.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);

        fragmentUtil = new FragmentUtil();
        user = new User();
        terms = (ArrayList<Terms>) getArguments().getSerializable("Terms");

        setPhoneNumber();

        return binding.getRoot();
    }

    private void setPhoneNumber() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String phoneNum = "";
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        phoneNum = telephonyManager.getLine1Number().toString();
        if (phoneNum.startsWith("+82")) {
            phoneNum = phoneNum.replace("+82", "0");
        }
        binding.edtPhone.setText(phoneNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAuth:
                //입력한 번호로 인증번호 보내는 기능 구현
                binding.edtPhone.setEnabled(false);
                binding.btnAuth.setText("재인증");
                break;
            case R.id.btnOK:
                //인증번호 확인하는 기능 구현
                //TODO 이미 회원이면 가입 불가하도록
                user.setPhone(binding.edtPhone.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("Terms", terms);
                bundle.putSerializable("User", user);
                fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container,
                        new PasswordFragment(), bundle);
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container, new LoginFragment());
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
        if (binding.edtPhone.isEnabled() == false && binding.edtAuthNum.length() > 0) {
            binding.btnOK.setEnabled(true);
        } else if (binding.edtPhone.isEnabled() == true && binding.edtPhone.length() >= 10) {
            binding.btnAuth.setEnabled(true);
        } else if (binding.edtPhone.isEnabled() == true && binding.edtPhone.length() < 10) {
            binding.btnAuth.setEnabled(false);
        } else if (binding.edtPhone.length() == 0) {
            binding.btnAuth.setEnabled(false);
        } else if (binding.edtAuthNum.length() == 0) {
            binding.btnOK.setEnabled(false);
        }
    }
}