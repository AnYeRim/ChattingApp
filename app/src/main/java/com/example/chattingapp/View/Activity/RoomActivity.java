package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.SocketClient;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
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

public class RoomActivity extends BaseActivity implements View.OnClickListener {

    private ActivityRoomBinding binding;

    private final String TAG = getClass().getSimpleName();

    private Socket socket;
    private Room room;
    private ArrayList<Message> message;
    private AdapterMessage adapterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSend.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);

        if (room == null) {
            room = (Room) getIntent().getExtras().getSerializable("data");
            setRoomData();
            setMessageData();
            setSocket();
            socket.emit("joinRoom", room.getId());
        }
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
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
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.off("getSendMessage", onSendMessage);
            socket.off("getReadMessage", onReadMessage);
        }
    }

    private void setRoomData() {
        Call<Room> call = getApiInterface().doGetRoom(room.getId());

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(@NonNull Call<Room> call, @NonNull Response<Room> response) {
                if (isSuccessResponse(response)) {
                    room = response.body();
                    binding.txtTitle.setText(room.getTitle());
                    setTotal(room.getTotal());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Room> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    private void setTotal(int total) {
        binding.txtTotal.setText(total + "");
        if (total <= 2) {
            binding.txtTotal.setVisibility(View.INVISIBLE);
        }
    }

    private void setMessageData() {
        Call<ArrayList<Message>> call = getApiInterface().doGetMessage(room.getId());

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Message>> call, @NonNull Response<ArrayList<Message>> response) {
                if (isSuccessResponse(response)) {
                    try {
                        message = response.body();
                        setRecyclerMessage();
                        readSocketMessage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Message>> call, @NonNull Throwable t) {
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
        binding.recyclerMessage.post(() -> binding.recyclerMessage.scrollToPosition(getLastMessagePosition()));
    }

    private void sendSocketMessage(int position) {
        Log.d(TAG, message + " 메세지 보내기 요청");
        try {
            socket.emit("sendMessage", MessageToJson(message.get(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Emitter.Listener onSendMessage = data -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d(TAG, "상대방이 보낸 메세지 받음" + data[0]);
            try {
                addMessage(JsonToMessage((JSONObject) data[0]));
                readSocketMessage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 0);
    };

    @NonNull
    private Message JsonToMessage(JSONObject data) throws JSONException {
        Message message = new Message();
        message.setRoom_id(data.getString("room_id"));
        message.setFrom_id(data.getString("from_id"));
        message.setMessage_id(data.getString("message_id"));
        message.setMessage(data.getString("message"));
        message.setUnread_total(data.getInt("unread_total"));
        message.setMy_read_status(data.getString("my_read_status"));
        return message;
    }

    @NonNull
    private JSONObject MessageToJson(Message message) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("room_id", message.getRoom_id());
        data.put("from_id", message.getFrom_id());
        data.put("read_members_id", message.getRead_members_id());
        data.put("message", message.getMessage());
        data.put("message_id", message.getMessage_id());
        data.put("type", message.getType());
        data.put("unread_total", message.getUnread_total());
        return data;
    }

    private void readSocketMessage() throws JSONException {
        for (int i = getLastMessagePosition(); i > 0; i--) {
            if (isUnreadMessage(message.get(i))) {
                socket.emit("setReadMessage", i, MessageToJson(message.get(i)), getUserID());
            } else {
                return;
            }
        }
    }

    private boolean isUnreadMessage(Message message) {
        return !message.getFrom_id().equals(getUserID()) && message.getMy_read_status().equals("안읽음");
    }

    Emitter.Listener onReadMessage = position -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d(TAG, "읽음 처리 요청에 대한 응답 받음" + position[0]);
            changeReadMessage((int) position[0]);
        }, 0);
    };

    private void changeReadMessage(int position) {
        message.get(position).setUnread_total(message.get(position).getUnread_total() - 1);
        message.get(position).setMy_read_status("읽음");
        adapterMessage.notifyDataSetChanged();
        scrollDown();
    }

    @NonNull
    private Message addMessage(Message message) {
        adapterMessage.addData(message);
        adapterMessage.notifyDataSetChanged();
        scrollDown();
        binding.edtMessage.setText("");
        return message;
    }

    @NonNull
    private Message getMessage() {
        Message message = new Message();
        message.setRoom_id(room.getId());
        message.setFrom_id(getUserID());
        message.setRead_members_id(getUserID());
        message.setMessage(binding.edtMessage.getText().toString());
        message.setType("Text");
        message.setUnread_total(room.getTotal() - 1);
        return message;
    }

    private int getLastMessagePosition() {
        return adapterMessage.getItemCount() - 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                addMessage(getMessage());
                sendSocketMessage(getLastMessagePosition());
                break;
            case R.id.btnMenu:
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
