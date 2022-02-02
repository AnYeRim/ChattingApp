package com.example.chattingapp.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityInfoBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityInfoBinding binding;
    private Friends friends;

    private ActivityUtils activityUtils;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();
        String token = activityUtils.getToken(this);
        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        init();

    }

    private void init() {
        // 풀스크린 설정
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // 풀스크린으로 하면서 버튼이 status바 자리로 이동함.
        // status바 높이만큼 백그라운드 패딩 세팅
        binding.imgBackground.setPadding(0,
                getStatusBarHeight(this),
                0,
                0);

        Intent intent = getIntent();
        friends = (Friends) intent.getExtras().getSerializable("data");
        binding.txtNicName.setText(friends.getNikName());
        binding.imgChat.setOnClickListener(this);
    }

    public static int getStatusBarHeight(Context context) {
        // status바의 높이 구하기
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        int statusbar = 0;

        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusbar = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusbar;
    }

    private void findRoom() {
        Call<Room> call = apiInterface.doFindRoom(friends.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                    if(response.body() == null){
                        createRoom();
                    } else {
                        Room room = response.body();
                        Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                        intent.putExtra("data", room);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        getApplicationContext().startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void createRoom() {
        ArrayList<Friends> member = new ArrayList<>();
        member.add(friends);

        Call<Room> call = apiInterface.doCreateRoom(member);

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                    Room room = response.body();

                    Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                    intent.putExtra("data", room);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    getApplicationContext().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgChat:
                findRoom();
                break;
        }
    }
}
