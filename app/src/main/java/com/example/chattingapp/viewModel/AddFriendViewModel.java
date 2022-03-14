package com.example.chattingapp.viewModel;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.Repository.FriendRepository;
import com.example.chattingapp.Model.VO.ResponseData;

public class AddFriendViewModel extends ViewModel {

    private FriendRepository repository;

    private MutableLiveData<Boolean> enabledOK;
    private MutableLiveData<Integer> color;

    private MutableLiveData<ResponseData> result;

    public AddFriendViewModel() {
        repository = new FriendRepository();
        result = new MutableLiveData<>();
    }

    public LiveData<Boolean> getEnabledOK() {
        if (enabledOK == null) {
            enabledOK = new MutableLiveData<>(false);
        }
        return enabledOK;
    }

    public LiveData<Integer> getColor() {
        if (color == null) {
            color = new MutableLiveData<>(Color.LTGRAY);
        }
        return color;
    }

    public LiveData<ResponseData> getResult() {
        return result;
    }

    public void addFriend(String friendName, String friendPhone) {
        result = repository.doAddFriend(toFriends(friendName, friendPhone));
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