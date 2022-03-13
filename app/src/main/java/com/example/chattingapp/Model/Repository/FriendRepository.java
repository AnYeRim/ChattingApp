package com.example.chattingapp.Model.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.VO.ResponseData;
import com.example.chattingapp.Tool.BaseRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRepository extends BaseRepository {

    private final String TAG ="FriendRepository";

    public LiveData<ResponseData> doAddFriend(Friends friends) {
        final MutableLiveData<ResponseData> data = new MutableLiveData<>();
        Call<ResponseData> call = getApiInterface().doAddFriends(friends);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                if (isSuccessResponse(response)) {
                        data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                Log.e(TAG,t.getMessage());
                call.cancel();
            }
        });
        return data;
    }

}
