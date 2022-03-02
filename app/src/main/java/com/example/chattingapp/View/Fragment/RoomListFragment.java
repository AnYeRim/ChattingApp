package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Adapter.AdapterRoom;
import com.example.chattingapp.databinding.FragmentRoomListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomListFragment extends Fragment {

    private FragmentRoomListBinding binding;
    private ArrayList<Room> room;
    private AdapterRoom adapterRoom;
    private APIInterface apiInterface;
    private ActivityUtils activityUtils;
    final private String TAG = "RoomListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRoomListBinding.inflate(inflater, container, false);

        activityUtils = new ActivityUtils();
        String token = activityUtils.getToken(getContext());
        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRoomList();
    }

    private void getRoomList() {
        Call<ArrayList<Room>> call = apiInterface.doFindRoom(null);
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(Call<ArrayList<Room>> call, Response<ArrayList<Room>> response) {
                if (isSuccessResponse(response)) {
                    room = response.body();
                    Log.d(TAG, room.get(0).getTotal()+"");
                    setRecyclerRoom();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Room>> call, Throwable t) {
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
        adapterRoom = new AdapterRoom(getActivity(), room);
        binding.recyclerRoom.setAdapter(adapterRoom);
    }
}