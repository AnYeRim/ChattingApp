package com.example.chattingapp.viewModel;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.View.Fragment.EtcFragment;
import com.example.chattingapp.View.Fragment.FriendsFragment;
import com.example.chattingapp.View.Fragment.RoomListFragment;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Fragment> fragment;
    private MutableLiveData<String> title;
    private MutableLiveData<Integer> visibilityAddChat, visibilityAddFriends;

    public MainViewModel() {
        fragment = new MutableLiveData<>();
        title = new MutableLiveData<>();
        visibilityAddChat = new MutableLiveData<>();
        visibilityAddFriends = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<Integer> getVisibilityAddChat() {
        return visibilityAddChat;
    }

    public LiveData<Integer> getVisibilityAddFriends() {
        return visibilityAddFriends;
    }

    public LiveData<Fragment> getFragment() {
        return fragment;
    }

    public boolean onSelectedBottomTab(String title) {
        switch (title) {
            case "친구":
                setSelectedData(title, visibilityAddFriends, new FriendsFragment());
                break;
            case "채팅":
                setSelectedData(title, visibilityAddChat, new RoomListFragment());
                break;
            case "더보기":
                setSelectedData(title, null, new EtcFragment());
                break;
        }
        return true;
    }

    private void setSelectedData(String title, MutableLiveData<Integer> icon, Fragment fragment) {
        this.title.setValue(title);
        setIcon(icon);
        this.fragment.setValue(fragment);
    }

    private void setIcon(MutableLiveData<Integer> icon) {
        visibilityAddChat.setValue(View.GONE);
        visibilityAddFriends.setValue(View.GONE);
        if(icon != null){
            icon.setValue(View.VISIBLE);
        }
    }
}