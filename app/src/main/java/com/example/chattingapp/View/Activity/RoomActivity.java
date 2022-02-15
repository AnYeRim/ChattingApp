package com.example.chattingapp.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Adapter.AdapterMessage;
import com.example.chattingapp.databinding.ActivityRoomBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRoomBinding binding;
    private Room room;

    final String TAG = "RoomActivity";

    private ActivityUtils activityUtils;
    private APIInterface apiInterface;
    private AdapterMessage adapterMessage;
    private ArrayList<Message> message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init(){
        activityUtils = new ActivityUtils();
        String token = activityUtils.getToken(this);
        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        binding.btnSend.setOnClickListener(this);

        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("data");
        setRoomData();
    }

    private void setRecyclerMessage() {
        binding.recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        adapterMessage = new AdapterMessage(this, message);
        binding.recyclerMessage.setAdapter(adapterMessage);
        adapterMessage.notifyDataSetChanged();
    }

    private void setRoomData() {
        Call<Room> call = apiInterface.doGetRoom(room.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if(response.isSuccessful()){
                    Room room = response.body();
                    binding.txtTitle.setText(room.getTitle());
                    setMessageData();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                call.cancel();
            }
        });
    }

    private void sendMessage(Message message) {
        Call<Message> call = apiInterface.doSendMessage(message);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    binding.edtMessage.setText("");
                    setMessageData();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                call.cancel();
            }
        });
    }

    private void setMessageData() {
        Call<ArrayList<Message>> call = apiInterface.doGetMessage(room.getId());

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful()){
                    message = response.body();
                    setRecyclerMessage();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Log.d(TAG,t.getMessage());
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
                message.setType("Text");
                sendMessage(message);
                break;
        }
    }
}
