package com.example.chattingapp.Tool;

import androidx.annotation.NonNull;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Utils.SharedPreferenceUtil;

import retrofit2.Response;

public class BaseRepository {

    public boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    @NonNull
    public APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
    }

    public String getToken(){
        return SharedPreferenceUtil.getData("token");
    }
}
