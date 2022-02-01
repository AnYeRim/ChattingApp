package com.example.chattingapp.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.databinding.ActivityRoomBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRoomBinding binding;
    private Room room;

    private ActivityUtils activityUtils;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityUtils = new ActivityUtils();
        String token = activityUtils.getToken(this);
        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        init();

    }

    private void init(){
        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("data");
        System.out.println("---------1---------------");
        setRoomData();
        binding.btnSend.setOnClickListener(this);
    }

    private void setRoomData() {
        System.out.println("---------2---------------");
        System.out.println(room.getId());
        Call<Room> call = apiInterface.doGetRoom(room.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if(response.isSuccessful()){
                    System.out.println("---------3---------------");
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    Room room = response.body();
                    binding.txtTitle.setText(room.getTitle());
                }
                System.out.println("---------4---------------");
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                call.cancel();
                System.out.println("---------5---------------");
                System.out.println(t.getMessage());
            }
        });
    }

    private void sendMessage(Message message) {
        Call<Message> call = apiInterface.doSendMessage(message);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                Message message = new Message();
                message.setMessage(binding.edtMessage.getText().toString());
                message.setRoom_id(room.getId());
                sendMessage(message);
                break;
        }
    }
}
