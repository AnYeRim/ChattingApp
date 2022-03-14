package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityInfoBinding;
import com.example.chattingapp.viewModel.InfoViewModel;

public class InfoActivity extends BaseActivity {

    private ActivityInfoBinding binding;
    private InfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_info);

        viewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        init();

        binding.imgChat.setOnClickListener(view -> {
            viewModel.findRoom(getFriends().getId());
            getFindRoomResult();
        });
        binding.btnCancle.setOnClickListener(view -> finish());
    }

    private void init() {
        setFullScreen();

        binding.txtNicName.setText(getFriends().getNikName());
        viewModel.setBackgroundTopPadding(this);
    }

    private void getFindRoomResult() {
        viewModel.getFindRoom().observe(this, room -> {
                    if (room == null) {
                        viewModel.createRoom(getFriends());
                        getCreateRoomResult();
                        return;
                    }
                    startActivity(RoomActivity.class, room);
                }
        );
    }

    private void getCreateRoomResult() {
        viewModel.getCreateRoom().observe(this, room -> startActivity(RoomActivity.class, room)
        );
    }

    private Friends getFriends() {
        return (Friends) getIntent().getExtras().getSerializable("data");
    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}

