package com.example.chattingapp.View.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityAddFriendBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity implements TextWatcher {

    private ActivityAddFriendBinding binding;
    private APIInterface apiInterface;
    private ActivityUtils activityUtils;
    private Friends friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();
        friends = new Friends();

        apiInterface = APIClient.getClient(activityUtils.getToken(this)).create(APIInterface.class);

        binding.txtOK.setTextColor(Color.LTGRAY);
        binding.edtFriendPhone.addTextChangedListener(this);
        binding.edtFriendName.addTextChangedListener(this);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friends.setNikName(binding.edtFriendName.getText().toString());
                friends.setPhone(binding.edtFriendPhone.getText().toString());
                doAddFriend();
            }
        });
    }

    private void doAddFriend() {
        Call<Friends> call = apiInterface.doAddFriends(friends);

        //NetworkResponse<User> networkResponse = new NetworkResponse<User>();
        call.enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(Call<Friends> call, Response<Friends> response) {
                if(response.isSuccessful()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Friends> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"친구추가 실패", Toast.LENGTH_SHORT).show();
                call.cancel();

            }
        });
/*
        if(networkResponse.getRes() != null){
            doCreateAgreeTerms();
        }else {
            Toast.makeText(getContext(),"유저 정보 insert 실패", Toast.LENGTH_SHORT).show();
            call.cancel();
        }*/
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (binding.edtFriendName.length() > 0 && binding.edtFriendPhone.length() > 0) {
            binding.txtOK.setEnabled(true);
            binding.txtOK.setTextColor(Color.BLACK);
        }else{
            binding.txtOK.setEnabled(false);
            binding.txtOK.setTextColor(Color.LTGRAY);
        }
    }
}