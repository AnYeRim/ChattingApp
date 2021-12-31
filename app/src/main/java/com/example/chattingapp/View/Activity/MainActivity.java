package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.R;
import com.example.chattingapp.View.Fragment.ChatFragment;
import com.example.chattingapp.View.Fragment.EtcFragment;
import com.example.chattingapp.View.Fragment.FriendsFragment;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Fragment frg_friends, frg_chat, frg_etc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frg_friends = new FriendsFragment();
        frg_chat = new ChatFragment();
        frg_etc = new EtcFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, frg_friends).commit();

        binding.bottomTap.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_friends:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container,frg_friends).commit();
                        return true;

                    case R.id.menu_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container,frg_chat).commit();
                        return true;

                    case R.id.menu_etc:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container,frg_etc).commit();
                        return true;
                }
                return false;
            }
        });

    }

}