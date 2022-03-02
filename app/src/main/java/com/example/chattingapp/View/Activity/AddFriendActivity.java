package com.example.chattingapp.View.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityAddFriendBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends BaseActivity implements TextWatcher {

    private ActivityAddFriendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        disabledTextOK();

        binding.edtFriendPhone.addTextChangedListener(this);
        binding.edtFriendName.addTextChangedListener(this);

        binding.btnBack.setOnClickListener(view -> finish());
        binding.txtOK.setOnClickListener(view -> doAddFriend(getFriendData()));
    }

    private void doAddFriend(Friends friends) {
        Call<Friends> call = getApiInterface().doAddFriends(friends);
        call.enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(@NonNull Call<Friends> call, @NonNull Response<Friends> response) {
                if (isSuccessResponse(response)) {
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Friends> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "친구추가 실패", Toast.LENGTH_SHORT).show();
                call.cancel();

            }
        });
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
    }

    private Friends getFriendData() {
        Friends friends = new Friends();
        friends.setNikName(binding.edtFriendName.getText().toString());
        friends.setPhone(binding.edtFriendPhone.getText().toString());
        return friends;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isEmptyRequiredValues()) {
            disabledTextOK();
            return;
        }

        enabledTextOK();
    }

    void disabledTextOK() {
        binding.txtOK.setEnabled(false);
        binding.txtOK.setTextColor(Color.LTGRAY);
    }

    void enabledTextOK() {
        binding.txtOK.setEnabled(true);
        binding.txtOK.setTextColor(Color.BLACK);
    }

    private boolean isEmptyRequiredValues() {
        return isEmptyEdit(binding.edtFriendName) || isEmptyEdit(binding.edtFriendPhone);
    }

    private boolean isEmptyEdit(EditText editText) {
        return editText.length() == 0;
    }
}