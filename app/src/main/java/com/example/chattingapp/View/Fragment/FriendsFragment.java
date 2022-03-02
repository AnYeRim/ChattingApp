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

    private final String TAG = getClass().getSimpleName();
    private FragmentFriendsBinding binding;
    private ActivityUtils activityUtils;

    private ArrayList<Friends> favorites, friends;
    private AdapterFriends adapterFavorites, adapterFriends;
    private APIInterface apiInterface;
    private String token, nikName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        activityUtils = new ActivityUtils();

        token = activityUtils.getToken(getContext());
        nikName = activityUtils.getNikName(getContext());
        binding.myInfo.txtNicName.setText(nikName);

        apiInterface = APIClient.getClient(token).create(APIInterface.class);

        binding.myInfo.linearFriends.setOnClickListener(this);
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
        Call<ArrayList<Friends>> call = apiInterface.doGetFriendsList();
        call.enqueue(new Callback<ArrayList<Friends>>() {
            @Override
            public void onResponse(Call<ArrayList<Friends>> call, Response<ArrayList<Friends>> response) {
                if(isSuccessResponse(response)){
                    friends = response.body();
                    setRecyclerFriends();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Friends>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    //TODO 즐겨찾기하기
    //TODO 즐겨찾기한 목록 가져오기
/*    private void setRecyclerFavorites() {
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFavorites = new AdapterFriends(getContext(), favorites);
        binding.recyclerFavorites.setAdapter(adapterFavorites);
        adapterFavorites.notifyDataSetChanged();
    }*/

    private void setRecyclerFriends() {
        binding.recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterFriends = new AdapterFriends(getContext(), friends);
        binding.recyclerFriends.setAdapter(adapterFriends);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myInfo:
                Friends friends = new Friends();
                friends.setId(activityUtils.getUserID(getContext()));
                friends.setNikName(activityUtils.getNikName(getContext()));

                activityUtils.newActivity(getContext(), InfoActivity.class, friends);
                break;
        }
    }
}