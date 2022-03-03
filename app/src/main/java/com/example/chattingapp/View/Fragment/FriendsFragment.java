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
import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.InfoActivity;
import com.example.chattingapp.View.Adapter.AdapterFriends;
import com.example.chattingapp.databinding.FragmentFriendsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private FragmentFriendsBinding binding;

    private ArrayList<Friends> favorites, friends;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        binding.myInfo.linearFriends.setOnClickListener(this);

        binding.myInfo.txtNicName.setText(getNikName());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(friends == null || favorites == null){
            favorites = new ArrayList<>();
            friends = new ArrayList<>();
            getFriendsList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(friends != null || favorites != null){
            friends = null;
            favorites = null;
        }
    }

    private void getFriendsList() {
        Call<ArrayList<Friends>> call = getApiInterface().doGetFriendsList();
        call.enqueue(new Callback<ArrayList<Friends>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Friends>> call, @NonNull Response<ArrayList<Friends>> response) {
                if(isSuccessResponse(response)){
                    friends = response.body();
                    setRecyclerFriends();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Friends>> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(getToken()).create(APIInterface.class);
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    private void setRecyclerFriends() {
        AdapterFriends adapterFriends = new AdapterFriends(getContext(), friends);

        binding.recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFriends.setAdapter(adapterFriends);
    }

    //TODO 즐겨찾기하기
    //TODO 즐겨찾기한 목록 가져오기
/*    private void setRecyclerFavorites() {
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterFriends adapterFavorites = new AdapterFriends(getContext(), favorites);
        binding.recyclerFavorites.setAdapter(adapterFavorites);
        adapterFavorites.notifyDataSetChanged();
    }*/

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.myInfo) {
            startActivity(InfoActivity.class, getFriends());
        }
    }

    @NonNull
    private Friends getFriends() {
        Friends friends = new Friends();
        friends.setId(getUserID());
        friends.setNikName(getNikName());
        return friends;
    }

    private String getToken(){
        return SharedPreferenceUtil.getData(getContext(), "token");
    }

    private String getUserID(){
        return SharedPreferenceUtil.getData(getContext(), "userID");
    }

    private String getNikName(){
        return SharedPreferenceUtil.getData(getContext(), "nikName");
    }
}