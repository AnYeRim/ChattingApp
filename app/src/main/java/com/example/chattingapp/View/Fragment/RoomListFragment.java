package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.View.Adapter.AdapterRoom;
import com.example.chattingapp.databinding.FragmentRoomListBinding;

import java.util.ArrayList;

public class RoomListFragment extends Fragment {

    private FragmentRoomListBinding binding;
    private ArrayList<Room> room;
    private AdapterRoom adapterRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRoomListBinding.inflate(inflater, container, false);

        getRoomList();
        setRecyclerRoom();

        return binding.getRoot();
    }

    private void getRoomList() {
        /*Call<JSONFriends> call = apiInterface.doGetFriendsList();

        NetworkResponse<JSONFriends> networkResponse = new NetworkResponse<JSONFriends>();
        call.enqueue(networkResponse);

        if(networkResponse.getRes() != null){
            friends = networkResponse.getRes().getFriends();
            setRecyclerFriends();
        }else {
            call.cancel();
        }*/
        room = new ArrayList<Room>();
        room.add(new Room("1",null,"채팅방 이름","채팅 메세지",
                "1",false,"오후 8:24",3));
        room.add(new Room("2",null,"채팅방 이름2","채팅 메세지2",
                "1",true,"오전 7:59",0));
        room.add(new Room("3",null,"채팅방 이름3","채팅 메세지3",
                "1",false,"어제",0));
    }

    private void setRecyclerRoom() {
        binding.recyclerRoom.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterRoom = new AdapterRoom(getActivity(), room);
        binding.recyclerRoom.setAdapter(adapterRoom);
    }
}