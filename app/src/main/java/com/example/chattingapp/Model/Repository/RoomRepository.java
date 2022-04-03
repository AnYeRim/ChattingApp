package com.example.chattingapp.Model.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Tool.BaseRepository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomRepository extends BaseRepository {

    private final String TAG ="RoomRepository";

    public LiveData<ArrayList<Room>> findRoom() {
        final MutableLiveData<ArrayList<Room>> data = new MutableLiveData<>();
        Call<ArrayList<Room>> call = getApiInterface().doFindRoom(null);
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Room>> call, @NonNull Response<ArrayList<Room>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Room>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
        return data;
    }

    public MutableLiveData<Room> findRoom(String friend_id) {
        final MutableLiveData<Room> data = new MutableLiveData<>();
        Call<ArrayList<Room>> call = getApiInterface().doFindRoom(friend_id);
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Room>> call, @NonNull Response<ArrayList<Room>> response) {
                if (response.isSuccessful()) {
                    if(response.body() == null){
                        data.setValue(null);
                        return;
                    }
                    data.setValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Room>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
        return data;
    }

    public MutableLiveData<Room> createRoom(ArrayList<Friends> member) {
        final MutableLiveData<Room> data = new MutableLiveData<>();
        Call<Room> call = getApiInterface().doCreateRoom(member);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(@NonNull Call<Room> call, @NonNull Response<Room> response) {
                if (isSuccessResponse(response)) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Room> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
        return data;
    }

}
