package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.SocketClient;
import com.example.chattingapp.Model.VO.ResponseData;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Adapter.AdapterMessage;
import com.example.chattingapp.databinding.ActivityRoomBinding;
import com.google.gson.Gson;

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
    private String userID, token;

    private final String TAG = "RoomActivity";

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

    private void init() {
        activityUtils = new ActivityUtils();
        token = activityUtils.getToken(this);
        userID = activityUtils.getUserID(this);
        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        binding.btnSend.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);

        if (room == null) {
            room = (Room) getIntent().getExtras().getSerializable("data");
            setRoomData();
        }

    }

    private void setSocket() {
        if (socket == null || !socket.connected()) {
            socket = SocketClient.getInstance();
            socket.connect();
            socket.on("getSendMessage", onSendMessage);
            socket.on("getReadMessage", onReadMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    private void setRoomData() {
        Call<Room> call = apiInterface.doGetRoom(room.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful() && response.body() != null) {
                    room = response.body();
                    String title = room.getTitle();
                    if (room.getTotal() != 1 && room.getTotal() != 2) {
                        title += " " + room.getTotal();
                    }
                    binding.txtTitle.setText(title);
                    setMessageData();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void setMessageData() {
        Call<ArrayList<Message>> call = apiInterface.doGetMessage(room.getId());

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message = response.body();
                    setRecyclerMessage();
                    setSocket();
                    socket.emit("joinRoom", room.getId());
                    readSocketMessage();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void setRecyclerMessage() {
        binding.recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        adapterMessage = new AdapterMessage(this, message);
        binding.recyclerMessage.setAdapter(adapterMessage);
        scrollDown();
    }

    private void scrollDown() {
        binding.recyclerMessage.post(new Runnable() {
            @Override
            public void run() {
                binding.recyclerMessage.scrollToPosition(adapterMessage.getItemCount() - 1);
            }
        });
    }

    private void sendSocketMessage(Message message) {
        try {
            JSONObject data = new JSONObject();
            data.put("room_id", message.getRoom_id());
            data.put("from_id", message.getFrom_id());
            data.put("read_members_id", message.getRead_members_id());
            data.put("message", message.getMessage());
            data.put("type", message.getType());
            data.put("unread_total", message.getUnread_total());
            socket.emit("sendMessage", data);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    Emitter.Listener onSendMessage = args -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = (JSONObject) args[0];
                    if (data.getString("from_id").equals(userID)) {
                        for (int i = message.size() - 1; i > 0; i--) {
                            if (message.get(i).getMessage().equals(data.getString("message"))) {
                                message.get(i).setMessage_id(data.getString("message_id"));
                                break;
                            }
                        }
                    } else {
                        Log.d(TAG,"onSendMessage message_id : "+data.getString("message_id"));
                        Message message = new Message();
                        message.setRoom_id(data.getString("room_id"));
                        message.setFrom_id(data.getString("from_id"));
                        message.setMessage_id(data.getString("message_id"));
                        message.setMessage(data.getString("message"));
                        message.setUnread_total(data.getInt("unread_total"));
                        message.setMy_read_status(data.getString("my_read_status"));
                        adapterMessage.addData(message);
                        scrollDown();
                        readSocketMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }, 0);
    };

    private void readSocketMessage() {
        try {
            for (int i = message.size() - 1; i > 0; i--) {
                Log.d(TAG,"onSendMessage getMy_read_status : "+message.get(i).getMy_read_status());
                if (!message.get(i).getFrom_id().equals(userID)
                        && message.get(i).getMy_read_status().equals("안읽음")) {
                    JSONObject data = new JSONObject();
                    data.put("room_id", message.get(i).getRoom_id());
                    data.put("message_id", message.get(i).getMessage_id());
                    data.put("message", message.get(i).getMessage());
                    data.put("from_id", message.get(i).getFrom_id());
                    data.put("read_member_id", userID);
                    socket.emit("setReadMessage", data);
                    Log.d(TAG,"readSocketMessage 읽음처리 보냈어!");
                } else {
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Emitter.Listener onReadMessage = args -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"onReadMessage");
                    JSONObject data = (JSONObject) args[0];
                    for (int i = message.size() - 1; i > 0; i--) {
                        Log.d(TAG,"onReadMessage for : "+data);
                        Log.d(TAG,"onReadMessage for : "+data);
                        if (message.get(i).getMessage_id().equals(data.getString("message_id"))) {
                            Log.d(TAG,"set 안읽은 수 수정");
                            Log.d(TAG,"get("+i+") message "+message.get(i).getMessage());
                            Log.d(TAG,"get("+i+") unread "+message.get(i).getUnread_total());
                            message.get(i).setUnread_total(message.get(i).getUnread_total() - 1);
                            break;
                        }
                    }
                    adapterMessage.notifyDataSetChanged();
                    scrollDown();
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }, 0);
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                Message message = new Message();
                message.setRoom_id(room.getId());
                message.setFrom_id(userID);
                message.setMessage(binding.edtMessage.getText().toString());
                message.setRead_members_id(userID);
                message.setType("Text");
                message.setUnread_total(room.getTotal() - 1);
                adapterMessage.addData(message);
                binding.edtMessage.setText("");
                scrollDown();
                sendSocketMessage(message);
                break;
            case R.id.btnMenu:
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
