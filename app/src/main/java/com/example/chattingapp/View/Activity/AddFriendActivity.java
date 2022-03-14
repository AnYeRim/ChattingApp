package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityAddFriendBinding;
import com.example.chattingapp.viewModel.AddFriendViewModel;

public class AddFriendActivity extends BaseActivity implements TextWatcher {

    private ActivityAddFriendBinding binding;
    private AddFriendViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);

        viewModel = new ViewModelProvider(this).get(AddFriendViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.edtFriendPhone.addTextChangedListener(this);
        binding.edtFriendName.addTextChangedListener(this);

        binding.btnBack.setOnClickListener(view -> finish());
        binding.txtOK.setOnClickListener(view -> {
            viewModel.addFriend(getFriendName(), getFriendPhone());
            getAddFriendResult();
        });

    }

    private void getAddFriendResult() {
        viewModel.getResult().observe(this, result -> {
            if (result.getAffectedRows() == 0) {
                showMessage("해당 번호인 유저가 없습니다.");
            } else {
                finish();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        viewModel.checkText(getFriendName(), getFriendPhone());
    }

    public String getFriendName() {
        return binding.edtFriendName.getText().toString();
    }

    public String getFriendPhone() {
        return binding.edtFriendPhone.getText().toString();
    }

}