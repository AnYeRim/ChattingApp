package com.example.chattingapp.View.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.R;
import com.example.chattingapp.View.Fragment.LoginFragment;
import com.example.chattingapp.databinding.ActivityTestBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new LoginFragment()).commit();

    }

}