package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Adapter.AdapterRoom;
import com.example.chattingapp.databinding.FragmentRoomListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomListFragment extends BaseFragment {

    final private String TAG = "RoomListFragment";

    private FragmentRoomListBinding binding;
    private ArrayList<Room> room;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRoomListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRoomList();
    }

    private void getRoomList() {
        Call<ArrayList<Room>> call = getApiInterface().doFindRoom(null);
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Room>> call, @NonNull Response<ArrayList<Room>> response) {
                if (isSuccessResponse(response)) {
                    room = response.body();
                    setRecyclerRoom();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Room>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    private void setRecyclerRoom() {
        binding.recyclerRoom.setLayoutManager(new LinearLayoutManager(getActivity()));
        AdapterRoom adapterRoom = new AdapterRoom(getActivity(), room);
        binding.recyclerRoom.setAdapter(adapterRoom);
    }

    private String getToken(){
        return SharedPreferenceUtil.getData(getContext(), "token");
    }
}