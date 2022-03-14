package com.example.chattingapp.View.Activity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseActivity;
import com.example.chattingapp.View.Adapter.AdapterMessage;
import com.example.chattingapp.databinding.ActivityRoomBinding;
import com.example.chattingapp.viewModel.RoomViewModel;

import org.json.JSONException;

import java.util.ArrayList;

public class RoomActivity extends BaseActivity {

    private ActivityRoomBinding binding;

    private RoomViewModel viewModel;
    private AdapterMessage adapterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ChattingApp.setCurrentActivity(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room);

        viewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.btnSend.setOnClickListener(view -> {
            viewModel.sendSocketMessage(getMessage());
            binding.edtMessage.setText("");
        });

        binding.btnBack.setOnClickListener(view -> finish());

        viewModel.init(getRoom().getId());
        getRoomResult();
        getMessageResult();
        deleteNotification(getRoom().getId());
    }

    private Room getRoom() {
        return (Room) getIntent().getExtras().getSerializable("data");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsetSocket();
    }

    private void getRoomResult() {
        viewModel.getRoom().observe(this, room -> {
            binding.txtTitle.setText(room.getTitle());
            setTotal(room.getTotal());
        });
    }

    private void getMessageResult() {
        viewModel.getMessage().observe(this, message -> {
            try {
                if (adapterMessage == null) {
                    setRecyclerMessage(message);
                } else {
                    adapterMessage.notifyDataSetChanged();
                }
                viewModel.readSocketMessage();
                new Handler().postDelayed(() -> deleteNotification(getRoom().getId()), 1000);
                scrollDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
        // TODO 푸시 알림이 없으면, 그룹으로 묶느라고 사용한 Summary 알림도 지워져야한다.
    }

    private void setTotal(int total) {
        binding.txtTotal.setText(total + "");
        if (total <= 2) {
            binding.txtTotal.setVisibility(View.INVISIBLE);
        }
    }

    private void setRecyclerMessage(ArrayList<Message> message) {
        binding.recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        adapterMessage = new AdapterMessage(this, message);
        binding.recyclerMessage.setAdapter(adapterMessage);
    }

    private void scrollDown() {
        binding.recyclerMessage.post(() -> binding.recyclerMessage.scrollToPosition(adapterMessage.getItemCount() - 1));
    }

    @NonNull
    private Message getMessage() {
        Message message = new Message();
        message.setRoom_id(getRoom().getId());
        message.setFrom_id(getUserID());
        message.setRead_members_id(getUserID());
        message.setMessage(binding.edtMessage.getText().toString());
        message.setType("Text");
        message.setUnread_total(getRoom().getTotal() - 1);
        return message;
    }

}
