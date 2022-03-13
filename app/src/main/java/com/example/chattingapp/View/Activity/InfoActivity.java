package com.example.chattingapp.View.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chattingapp.ChattingApp;
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

        binding.imgChat.setOnClickListener(view -> viewModel.findRoom());
        binding.btnCancle.setOnClickListener(view -> finish());

        init();
    }

    private void init(){
        setFullScreen();
        setBackgroundTopPadding(getStatusBarHeight(this));
        viewModel.init();
    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setBackgroundTopPadding(int topPadding) {
        binding.imgBackground.setPadding(0, topPadding, 0, 0);
    }

    public int getStatusBarHeight(Context context) {
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        int statusBar = 0;

        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusBar = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBar;
    }

}

