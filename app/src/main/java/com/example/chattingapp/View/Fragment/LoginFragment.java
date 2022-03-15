package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentLoginBinding;
import com.example.chattingapp.viewModel.LoginViewModel;

public class LoginFragment extends BaseFragment implements TextWatcher {

    private FragmentLoginBinding binding;

    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.edtID.addTextChangedListener(this);
        binding.edtPW.addTextChangedListener(this);

        binding.btnLogin.setOnClickListener(view -> {
            viewModel.getFirebaseToken();
            getFirebaseToken();
        });
        binding.btnJoin.setOnClickListener(view -> changeFragment(R.id.frg_container, new TermsFragment()));
        binding.txtFindIDPW.setOnClickListener(view -> {/*TODO 아이디 비번 찾는 화면으로 이동*/});

        return binding.getRoot();
    }

    private void getFirebaseToken() {
        viewModel.getTokenResult().observe(getViewLifecycleOwner(), token -> {
            User user = getUser();
            user.setFb_token(token);

            viewModel.doLogin(user);
            getLoginResult();
        });
    }

    private void getLoginResult() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), responseUser -> {
            if (responseUser == null) {
                showMessage("유저 정보가 올바르지 않습니다.");
                return;
            }
            viewModel.setSharedPreference(responseUser);
            startActivity(SplashActivity.class);
            getActivity().finish();
        });
    }

    private User getUser() {
        User user = new User();
        user.setPhone(binding.edtID.getText().toString());
        user.setEmail(binding.edtID.getText().toString());
        user.setPassword(binding.edtPW.getText().toString());
        return user;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        viewModel.checkText(getID(), getPW());
    }

    private String getID() {
        return binding.edtID.toString();
    }

    private String getPW() {
        return binding.edtPW.toString();
    }
}