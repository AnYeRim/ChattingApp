package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.viewModel.AddFriendViewModel;
import com.example.chattingapp.databinding.ActivityAddFriendBinding;

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
        binding.txtOK.setOnClickListener(view -> viewModel.addData(getFriendName(),getFriendPhone()));
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