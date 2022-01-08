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
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.Model.VO.JSONFriends;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Adapter.AdapterFriends;
import com.example.chattingapp.databinding.FragmentFriendsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private ArrayList<Friends> favorites, friends;
    private AdapterFriends adapterFavorites, adapterFriends;
    private APIInterface apiInterface;
    private ActivityUtils activityUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        String token = SharedPreferenceUtil.getData(getContext(), "token");
        String nikName = SharedPreferenceUtil.getData(getContext(), "nikname");
        binding.myInfo.txtNicName.setText(nikName);

        apiInterface = APIClient.getClient(token).create(APIInterface.class);
        activityUtils = new ActivityUtils();
        getFriendsList();

        favorites = new ArrayList<Friends>();
        friends = new ArrayList<Friends>();
        setRecyclerFriends();
        setRecyclerFavorites();

        return binding.getRoot();
    }

    private void getFriendsList() {
        Call<JSONFriends> call = apiInterface.doGetFriendsList();
        call.enqueue(new Callback<JSONFriends>() {
            @Override
            public void onResponse(Call<JSONFriends> call, Response<JSONFriends> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", response.code() + "");
                    friends = response.body().getFriends();
                    setRecyclerFriends();
                } else {

                }
            }

            @Override
            public void onFailure(Call<JSONFriends> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                call.cancel();
            }
        });
    }

    private void setRecyclerFavorites() {
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFavorites = new AdapterFriends(getContext(), favorites);
        binding.recyclerFavorites.setAdapter(adapterFavorites);
    }

    private void setRecyclerFriends() {
        binding.recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFriends = new AdapterFriends(getContext(), friends);
        binding.recyclerFriends.setAdapter(adapterFriends);
    }

}