package com.example.chattingapp.View.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.SocketClient;
import com.example.chattingapp.R;
import com.example.chattingapp.View.Fragment.EtcFragment;
import com.example.chattingapp.View.Fragment.FriendsFragment;
import com.example.chattingapp.View.Fragment.RoomListFragment;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment frg_friends, frg_chat, frg_etc;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frg_friends = new FriendsFragment();
        frg_chat = new RoomListFragment();
        frg_etc = new EtcFragment();

        binding.titleBar.txtTitle.setText("친구");
        binding.titleBar.btnAddChat.setVisibility(View.GONE);
        binding.titleBar.btnAddFriends.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, frg_friends).commit();

        binding.bottomTap.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    private void init() {
        if (socket == null || !socket.connected()) {
            socket = SocketClient.getInstance();
            socket.connect();
            socket.emit("joinRoom", "room1");
            JSONObject data = new JSONObject();
            try {
                data.put("roomName", "room1");
                data.put("message", "test 메세지 입니다.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("chatting", data);
            socket.on("sendMessage", onSendMessage);
        }
    }

    Emitter.Listener onSendMessage = args -> {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();
            }
        }, 0);
    };


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_friends:
                    binding.titleBar.txtTitle.setText("친구");
                    binding.titleBar.btnAddChat.setVisibility(View.GONE);
                    binding.titleBar.btnAddFriends.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, frg_friends).commit();
                    return true;

                case R.id.menu_chat:
                    binding.titleBar.txtTitle.setText("채팅");
                    binding.titleBar.btnAddChat.setVisibility(View.VISIBLE);
                    binding.titleBar.btnAddFriends.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, frg_chat).commit();
                    return true;

                case R.id.menu_etc:
                    init();
                    binding.titleBar.txtTitle.setText("더보기");
                    binding.titleBar.btnAddChat.setVisibility(View.GONE);
                    binding.titleBar.btnAddFriends.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, frg_etc).commit();
                    return true;
            }
            return false;
        }
    }

}