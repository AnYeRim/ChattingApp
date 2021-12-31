package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.DataFriends;
import com.example.chattingapp.View.Adapter.AdapterFriends;
import com.example.chattingapp.databinding.FragmentFriendsBinding;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private ArrayList<DataFriends> favorites, friends;
    private AdapterFriends adapterFavorites, adapterFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        favorites = new ArrayList<DataFriends>();
        friends = new ArrayList<DataFriends>();
        setRecyclerFriends();
        setRecyclerFavorites();

        return binding.getRoot();
    }

    private void setRecyclerFavorites() {
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        favorites.add(new DataFriends("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEyMTZfMTM2%2FMDAxNjM5NjI5Nzc1MDc1.qBZ7MOHHxd2eLrucCWh-QR8ojGguwXNOJEaVGuyGpkYg.wscCJgOptEKoO5vqsjJAmS7g4mBd631nC08UbrmdlkMg.JPEG.happyhome369%2F20211208_092400.jpg&type=sc960_832"
                , "즐찾이름1", "상태메세지1"));
        favorites.add(new DataFriends("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEyMjVfMjM3%2FMDAxNjQwNDE1MTg2MTE3.fyNd8vS4-icOTDx_rBqQUMwLYWil_-9-B6Rw1a0Xvbcg.dNEbvfmdp7du1rnEAQIqUDn3lhR2plxTsX6b03qzh0Ig.JPEG.cheonkuk2%2F20211225%25A3%25DF090551.jpg&type=sc960_832"
                , "즐찾이름2", "상태메세지2"));

        adapterFavorites = new AdapterFriends(getContext(), favorites);
        binding.recyclerFavorites.setAdapter(adapterFavorites);
    }

    private void setRecyclerFriends() {
        binding.recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        friends.add(new DataFriends("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEyMTZfMTM2%2FMDAxNjM5NjI5Nzc1MDc1.qBZ7MOHHxd2eLrucCWh-QR8ojGguwXNOJEaVGuyGpkYg.wscCJgOptEKoO5vqsjJAmS7g4mBd631nC08UbrmdlkMg.JPEG.happyhome369%2F20211208_092400.jpg&type=sc960_832"
                , "이름1", "상태메세지1"));
        friends.add(new DataFriends("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEyMjVfMjM3%2FMDAxNjQwNDE1MTg2MTE3.fyNd8vS4-icOTDx_rBqQUMwLYWil_-9-B6Rw1a0Xvbcg.dNEbvfmdp7du1rnEAQIqUDn3lhR2plxTsX6b03qzh0Ig.JPEG.cheonkuk2%2F20211225%25A3%25DF090551.jpg&type=sc960_832"
                , "이름2", "상태메세지2"));
        friends.add(new DataFriends("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTExMTRfNzkg%2FMDAxNjM2ODQ3NzUzODYy.KPnHuRofTMzb4xfhH0VJQNcKFClYiY0KHT3zUPCi3hcg.zLYTGc1cDuPsBFvXNirDw6aYQoimNOTGbuYhw-fU9fsg.JPEG.impear%2F20211114%25A3%25DF074222%25A3%25DFHDR.jpg&type=sc960_832"
                , "이름3", "상태메세지3"));

        adapterFriends = new AdapterFriends(getContext(), friends);
        binding.recyclerFriends.setAdapter(adapterFriends);
    }

}