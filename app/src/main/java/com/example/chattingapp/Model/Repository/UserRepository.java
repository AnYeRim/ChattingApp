package com.example.chattingapp.Model.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.ResponseUser;
import com.example.chattingapp.Tool.BaseRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository extends BaseRepository {

    private final String TAG = getClass().getSimpleName();

    public LiveData<ResponseUser> doLogin(User user) {
        final MutableLiveData<ResponseUser> data = new MutableLiveData<>();
        Call<ResponseUser> call = getApiInterface().doGetUser(user);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUser> call, @NonNull Response<ResponseUser> response) {
                if (response.code() == 400) {
                    data.setValue(null);
                    return;
                }
                if (isSuccessResponse(response)) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseUser> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
        return data;
    }

}
