package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
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
import com.example.chattingapp.databinding.FragmentPasswordBinding;

import java.util.ArrayList;

public class PasswordFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private FragmentPasswordBinding binding;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);

        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);

        binding.edtPassword.addTextChangedListener(this);
        binding.edtCheckPassword.addTextChangedListener(this);

        user = (User) getArguments().getSerializable("User");
        binding.txtPhone.setText(user.getPhone());

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                if (checkPassword()) {
                    user.setPassword(getPassword());
                    changeFragment(R.id.frg_container, new JoinInfoFragment(), getBundle());
                }
                break;
            case R.id.txtBackBegin:
                changeFragment(R.id.frg_container, new LoginFragment());
                break;
        }
    }

    private boolean checkPassword() {
        if (outOfPasswordFormat(getPassword()) || outOfPasswordFormat(getCheckPassword())) {
            showMessage("비밀번호는 8~16자로 적어주세요.");
            return false;
        }

        if (!getPassword().equals(getCheckPassword())) {
            showMessage("비밀번호가 일치하지 않습니다.");
            return false;
        }

        return true;
    }

    private boolean outOfPasswordFormat(String password) {
        return password.length() < 8 || password.length() > 16;
    }

    @NonNull
    private String getCheckPassword() {
        return binding.edtCheckPassword.getText().toString();
    }

    @NonNull
    private String getPassword() {
        return binding.edtPassword.getText().toString();
    }

    @NonNull
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Terms", getTerms());
        bundle.putSerializable("User", user);
        return bundle;
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
        setEnabledBtnOK();
    }

    private void setEnabledBtnOK() {
        if (binding.edtPassword.length() >= 8 && binding.edtCheckPassword.length() >= 8) {
            binding.btnOK.setEnabled(true);
            return;
        }

        binding.btnOK.setEnabled(false);
    }

}