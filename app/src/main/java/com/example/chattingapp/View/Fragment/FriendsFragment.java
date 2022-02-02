package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.VO.JSONFriends;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Activity.InfoActivity;
import com.example.chattingapp.View.Adapter.AdapterFriends;
import com.example.chattingapp.databinding.FragmentFriendsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private FragmentFriendsBinding binding;
    private ActivityUtils activityUtils;

    private ArrayList<Friends> favorites, friends;
    private AdapterFriends adapterFavorites, adapterFriends;
    private APIInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        activityUtils = new ActivityUtils();

        String token = activityUtils.getToken(getContext());
        String nikName = activityUtils.getNikName(getContext());
        binding.myInfo.txtNicName.setText(nikName);

        apiInterface = APIClient.getClient(token).create(APIInterface.class);
        getFriendsList();

        favorites = new ArrayList<Friends>();
        friends = new ArrayList<Friends>();
        setRecyclerFriends();
        setRecyclerFavorites();

        binding.myInfo.linearFriends.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setRecyclerFriends();
    }

    private void getFriendsList() {
        Call<JSONFriends> call = apiInterface.doGetFriendsList();

        //NetworkResponse<JSONFriends> networkResponse = new NetworkResponse<JSONFriends>();
        call.enqueue(new Callback<JSONFriends>() {
            @Override
            public void onResponse(Call<JSONFriends> call, Response<JSONFriends> response) {
                if(response.isSuccessful()){
                    friends = response.body().getFriends();
                    setRecyclerFriends();
                }
            }

            @Override
            public void onFailure(Call<JSONFriends> call, Throwable t) {
                call.cancel();
            }
        });

        /*if(networkResponse.getRes() != null){
            friends = networkResponse.getRes().getFriends();
            setRecyclerFriends();
        }else {
            call.cancel();
        }*/
    }

    private void setRecyclerFavorites() {
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFavorites = new AdapterFriends(getContext(), favorites);
        binding.recyclerFavorites.setAdapter(adapterFavorites);
        adapterFavorites.notifyDataSetChanged();
    }

    private void setRecyclerFriends() {
        binding.recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFriends = new AdapterFriends(getContext(), friends);
        binding.recyclerFriends.setAdapter(adapterFriends);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myInfo:
                ActivityUtils activityUtils = new ActivityUtils();
                activityUtils.newActivity(getContext(), InfoActivity.class);
                break;
        }
    }
}