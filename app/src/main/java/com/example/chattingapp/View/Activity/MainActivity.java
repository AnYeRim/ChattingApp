package com.example.chattingapp.View.Activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.example.chattingapp.viewModel.MainViewModel;

public class MainActivity extends BaseActivity{

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        viewModel.init();
        replaceFragment();

        binding.titleBar.btnAddChat.setOnClickListener(view -> {/*TODO 채팅방 만들기*/});
        binding.titleBar.btnAddFriends.setOnClickListener(view -> viewModel.onClickAddFriends());

        binding.bottomTab.setOnItemSelectedListener(item -> viewModel.onSelectedBottomTab(item.getTitle().toString()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomTab.setSelectedItemId(getSelectedItemId());
    }

    private int getSelectedItemId() {
        if(getIntent().getExtras() != null){
            return getIntent().getExtras().getInt("selectedItem");
        }else {
            return binding.bottomTab.getSelectedItemId();
        }
    }

    private void replaceFragment() {
        viewModel.getFragment().observe(this, fragment ->
                getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, fragment).commit());
    }
}