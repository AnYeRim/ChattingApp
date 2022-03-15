package com.example.chattingapp.viewModel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.Repository.RoomRepository;
import com.example.chattingapp.Model.SocketClient;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RoomViewModel extends ViewModel {
// TODO 보내는 쪽은 정상적으로 1번씩 소켓 응답 받는데, 받는 쪽에서 2번씩 소켓 응답을 받아 오류 발생
    private final String TAG ="RoomViewModel";

    private RoomRepository repository;

    private Socket socket;

    private MutableLiveData<Room> room;
    private MutableLiveData<ArrayList<Message>> message;
    private ArrayList<Message> messageList;

    private Gson gson = new Gson();

    public RoomViewModel() {
    }

    public void init(int id) {
        repository = new RoomRepository();
        room = repository.getRoom(id);
        message = repository.getMessage(id);

        setSocket();
        socket.emit("joinRoom", id);
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public LiveData<ArrayList<Message>> getMessage() {
        return message;
    }

    public void setSocket() {
        if (socket == null || !socket.connected()) {
            socket = SocketClient.getInstance();
            socket.connect();
            socket.on("getSendMessage", onSendMessage);
            socket.on("getReadMessage", onReadMessage);
        }
    }

    public void unsetSocket() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.off("getSendMessage", onSendMessage);
            socket.off("getReadMessage", onReadMessage);
        }
    }

    @NonNull
    public Message JsonToMessage(String data) {
        return gson.fromJson(data, Message.class);
    }

    @NonNull
    public JSONObject MessageToJson(Message message) throws JSONException {
        return new JSONObject(gson.toJson(message));
    }

    public void sendSocketMessage(Message message) {
        Log.d(TAG, message + " 메세지 보내기 요청");
        try {
            messageList = getMessageList();
            messageList.add(message);
            this.message.setValue(messageList);
            socket.emit("sendMessage", MessageToJson(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Emitter.Listener onSendMessage = data -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d(TAG, "상대방이 보낸 메세지 받음" + data[0]);
            messageList = getMessageList();
            messageList.add(JsonToMessage(data[0].toString()));
            this.message.setValue(messageList);

        }, 0);
    };

    public void readSocketMessage() throws JSONException {
        for (int i = getLastMessagePosition(); i > 0; i--) {
            if (isUnreadMessage(getMessage(i))) {
                socket.emit("setReadMessage", i, MessageToJson(getMessage(i)), getUserID());
            } else {
                return;
            }
        }
    }

    Emitter.Listener onReadMessage = position -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Log.d(TAG, "읽음 처리 요청에 대한 응답 받음" + position[0]);

            int i = (int) position[0];
            messageList = getMessageList();
            messageList.get(i).setUnread_total(messageList.get(i).getUnread_total() - 1);
            messageList.get(i).setMy_read_status("읽음");
            this.message.setValue(messageList);
        }, 0);
    };

    public boolean isUnreadMessage(Message message) {
        boolean a = !message.getFrom_id().equals(getUserID());
        boolean b = message.getMy_read_status()==null?true:message.getMy_read_status().equals("안읽음");
        return a && b;
    }

    private String getUserID() {
        return SharedPreferenceUtil.getData("userID");
    }

    private ArrayList<Message> getMessageList() {
        return message.getValue();
    }

    public Message getMessage(int position) {
        return message.getValue().get(position);
    }

    private int getLastMessagePosition() {
        return message.getValue().size() - 1;
    }
}
