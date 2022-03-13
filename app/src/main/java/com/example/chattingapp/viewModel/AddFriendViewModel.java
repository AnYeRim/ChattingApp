package com.example.chattingapp.viewModel;

import android.graphics.Color;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.ChattingApp;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.Repository.FriendRepository;

public class AddFriendViewModel extends ViewModel {

    private FriendRepository repository;

    private MutableLiveData<Boolean> enabledOK;
    private MutableLiveData<Integer> color;

    public LiveData<Boolean> getEnabledOK() {
        if (enabledOK == null) {
            enabledOK = new MutableLiveData<>();
        }
        return enabledOK;
    }

    public LiveData<Integer> getColor() {
        if (color == null) {
            color = new MutableLiveData<>();
        }
        return color;
    }

    public FriendRepository getRepository() {
        if (repository == null) {
            repository = new FriendRepository();
        }

        return repository;
    }

    public void addData(String friendName, String friendPhone) {
        getRepository().doAddFriend(toFriends(friendName, friendPhone)).observe(
                (LifecycleOwner) ChattingApp.getCurrentActivity(), result -> {
            if (result.getAffectedRows() != 0) {
                ChattingApp.getCurrentActivity().finish();
            } else {
                Toast.makeText(ChattingApp.getCurrentActivity(), "해당 번호인 유저가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Friends toFriends(String friendName, String friendPhone) {
        Friends friends = new Friends();
        friends.setNikName(friendName);
        friends.setPhone(friendPhone);
        return friends;
    }

    public void checkText(String friendName, String friendPhone) {
        if (isEmptyRequiredValues(friendName, friendPhone)) {
            enabledOK.setValue(false);
            color.setValue(Color.LTGRAY);
            return;
        }

        enabledOK.setValue(true);
        color.setValue(Color.BLACK);
    }

    public boolean isEmptyRequiredValues(String friendName, String friendPhone) {
        return isEmptyEdit(friendName) || isEmptyEdit(friendPhone);
    }

    public boolean isEmptyEdit(String text) {
        return text.length() == 0;
    }

}