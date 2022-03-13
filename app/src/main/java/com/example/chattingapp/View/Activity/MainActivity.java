package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.View.Fragment.EtcFragment;
import com.example.chattingapp.View.Fragment.FriendsFragment;
import com.example.chattingapp.View.Fragment.RoomListFragment;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private Fragment frg_friends, frg_chat, frg_etc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frg_friends = new FriendsFragment();
        frg_chat = new RoomListFragment();
        frg_etc = new EtcFragment();

        binding.titleBar.btnAddChat.setOnClickListener(this);
        binding.titleBar.btnAddFriends.setOnClickListener(this);
        binding.bottomTap.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            int selectedItem = getIntent().getExtras().getInt("selectedItem");
            showSelectedItem(selectedItem);
        }else {
            showSelectedItem(binding.bottomTap.getSelectedItemId());
        }
    }

    private void showSelectedItem(int selectedItemId) {
        switch (selectedItemId) {
            case R.id.menu_friends:
                setSelectedData("친구", binding.titleBar.btnAddFriends, frg_friends);
                break;
            case R.id.menu_chat:
                setSelectedData("채팅", binding.titleBar.btnAddChat, frg_chat);
                break;
            case R.id.menu_etc:
                setSelectedData("더보기", null, frg_etc);
                break;
        }
    }

    private void setSelectedData(String title, ImageView icon, Fragment fragment) {
        setTitle(title);
        showIcon(icon);
        showFragment(fragment);
    }

    private void setTitle(String title) {
        binding.titleBar.txtTitle.setText(title);
    }

    private void showIcon(ImageView icon) {
        binding.titleBar.btnAddChat.setVisibility(View.GONE);
        binding.titleBar.btnAddFriends.setVisibility(View.GONE);
        if(icon != null){
            icon.setVisibility(View.VISIBLE);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        showSelectedItem(item.getItemId());
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddChat:
                //TODO 채팅방 만들기
                break;
            case R.id.btnAddFriends:
                startActivity(AddFriendActivity.class);
                break;
        }
    }
}