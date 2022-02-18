package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.SocketClient;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Adapter.AdapterMessage;
import com.example.chattingapp.databinding.ActivityRoomBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRoomBinding binding;
    private Room room;
    private Socket socket;

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
        binding.btnBack.setOnClickListener(this);

        if(room == null){
            room = (Room) getIntent().getExtras().getSerializable("data");
            setRoomData();
        }

        if (socket == null || !socket.connected()) {
            socket = SocketClient.getInstance();
            socket.connect();
            socket.emit("joinRoom", room.getId());
            socket.on("sendMessage", onSendMessage);
        }
    }

    private void scrollDown() {
        binding.recyclerMessage.post(new Runnable() {
            @Override
            public void run() {
                binding.recyclerMessage.scrollToPosition(adapterMessage.getItemCount() - 1);
            }
        });
    }

    private void setRecyclerMessage() {
        binding.recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        adapterMessage = new AdapterMessage(this, message);
        binding.recyclerMessage.setAdapter(adapterMessage);
        scrollDown();
    }

    private void setRoomData() {
        Call<Room> call = apiInterface.doGetRoom(room.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if(response.isSuccessful() && response.body() != null){
                    room = response.body();
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
        binding.edtMessage.setText("");
        adapterMessage.addData(message);
        scrollDown();

        sendSocketMessage(message);

        Call<Message> call = apiInterface.doSendMessage(message);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(),"메세지 보내기 성공", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"메세지 보내기 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                Toast.makeText(getApplicationContext(),"메세지 보내기 실패", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void setMessageData() {
        Call<ArrayList<Message>> call = apiInterface.doGetMessage(room.getId());

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful() && response.body() != null){
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

    private void sendSocketMessage(Message message) {
            JSONObject data = new JSONObject();
            try {
                data.put("roomName", room.getId());
                data.put("message", message.getMessage());
                data.put("fromID", message.getFrom_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("chatting", data);
    }

    Emitter.Listener onSendMessage = args -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                JSONObject data = (JSONObject) args[0];
                Message message = new Message();
                try {
                    message.setMessage(data.getString("message"));
                    message.setFrom_id(data.getString("fromID"));
                    adapterMessage.addData(message);
                    scrollDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0);
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                Message message = new Message();
                message.setMessage(binding.edtMessage.getText().toString());
                message.setRoom_id(room.getId());
                message.setFrom_id(activityUtils.getUserID(this));
                message.setType("Text");
                sendMessage(message);
                break;
            case R.id.btnMenu:
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
