package com.example.chattingapp.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.databinding.ActivityInfoBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private ActivityInfoBinding binding;
    private Friends friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgChat.setOnClickListener(this);
        binding.btnCancle.setOnClickListener(this);

        friends = (Friends) getIntent().getExtras().getSerializable("data");
        binding.txtNicName.setText(friends.getNikName());

        setFullScreen();
        setBackgroundTopPadding(getStatusBarHeight(this));

    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setBackgroundTopPadding(int topPadding) {
        binding.imgBackground.setPadding(0, topPadding, 0, 0);
    }

    public static int getStatusBarHeight(Context context) {
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

    private void findRoom() {
        Call<ArrayList<Room>> call = getApiInterface().doFindRoom(friends.getId());
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Room>> call, @NonNull Response<ArrayList<Room>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        createRoom();
                        return;
                    }

                    Room room = response.body().get(0);
                    startActivity(RoomActivity.class, room);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Room>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void createRoom() {
        ArrayList<Friends> member = new ArrayList<>();
        member.add(friends);

        Call<Room> call = getApiInterface().doCreateRoom(member);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(@NonNull Call<Room> call, @NonNull Response<Room> response) {
                if (isSuccessResponse(response)) {
                    Room room = response.body();
                    startActivity(RoomActivity.class, room);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Room> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
    }

    private boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgChat:
                findRoom();
                break;
            case R.id.btnCancle:
                finish();
                break;
        }
    }
}
