package com.example.chattingapp.viewModel;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Model.Repository.RoomRepository;

import java.util.ArrayList;

public class InfoViewModel extends ViewModel {

    private RoomRepository repository;

    private MutableLiveData<Room> findRoom, createRoom;
    private MutableLiveData<Integer> paddingTop;

    public InfoViewModel() {
        repository = new RoomRepository();
        paddingTop = new MutableLiveData<>();
    }

    public MutableLiveData<Room> getFindRoom() {
        return findRoom;
    }

    public MutableLiveData<Room> getCreateRoom() {
        return createRoom;
    }

    public LiveData<Integer> getPaddingTop() {
        return paddingTop;
    }

    public void findRoom(String id) {
        findRoom = repository.findRoom(id);
    }

    public void createRoom(Friends friends) {
        createRoom = repository.createRoom(toArrayList(friends));
    }

    @NonNull
    private ArrayList<Friends> toArrayList(Friends friends) {
        ArrayList<Friends> member = new ArrayList<>();
        member.add(friends);
        return member;
    }

    public void setBackgroundTopPadding(Context context) {
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        int statusBar = 0;

        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusBar = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        paddingTop = new MutableLiveData<>(statusBar);
    }
}
