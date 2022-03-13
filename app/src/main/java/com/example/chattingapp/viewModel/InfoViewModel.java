package com.example.chattingapp.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.Repository.RoomRepository;
import com.example.chattingapp.Tool.BaseViewModel;
import com.example.chattingapp.View.Activity.RoomActivity;

import java.util.ArrayList;

public class InfoViewModel extends BaseViewModel {

    private RoomRepository repository;

    private Friends friends;
    private MutableLiveData<String> friend_name;

    public void init() {
        friends = (Friends) getCurrentActivity().getIntent().getExtras().getSerializable("data");
        getFriend_name().setValue(friends.getNikName());
    }

    public MutableLiveData<String> getFriend_name() {
        if(friend_name == null){
            friend_name = new MutableLiveData<>();
        }
        return friend_name;
    }

    public RoomRepository getRepository() {
        if (repository == null) {
            repository = new RoomRepository();
        }

        return repository;
    }

    public void findRoom() {
        getRepository().findRoom(friends.getId())
                .observe((LifecycleOwner) getCurrentActivity(), responseData -> {
            if (responseData == null) {
                createRoom();
                return;
            }
            Room room = responseData.get(0);
            startActivity(RoomActivity.class, room);
        });
    }

    private void createRoom() {
        getRepository().createRoom(getMember())
                .observe((LifecycleOwner) getCurrentActivity(), responseData -> {
            if(responseData != null){
                Room room = responseData;
                startActivity(RoomActivity.class, room);
            }
        });
    }

    @NonNull
    private ArrayList<Friends> getMember() {
        ArrayList<Friends> member = new ArrayList<>();
        member.add(friends);
        return member;
    }
}
