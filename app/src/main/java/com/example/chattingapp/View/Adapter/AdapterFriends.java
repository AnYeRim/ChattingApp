package com.example.chattingapp.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Model.DTO.Friends;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ItemFriendsBinding;

import java.util.ArrayList;

public class AdapterFriends extends RecyclerView.Adapter<AdapterFriends.ViewHolder> {

    private Context mContext;
    private ArrayList<Friends> data;

    public AdapterFriends(Context mContext, ArrayList<Friends> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends,parent,false);
        return new AdapterFriends.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriends.ViewHolder holder, int position) {
        holder.setItemTermsBinding(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemFriendsBinding itemFriendsBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemFriendsBinding = ItemFriendsBinding.bind(itemView);
            itemFriendsBinding.linearFriends.setOnClickListener(this);
        }

        void setItemTermsBinding(Friends data) {
            //Glide.with(mContext).load(data.getImageURL()).into(itemFriendsBinding.imgProfile);
            itemFriendsBinding.txtNicName.setText(data.getNikName());
            //itemFriendsBinding.txtMessage.setText(data.getMessage());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Toast.makeText(mContext,data.get(position).getNikName().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
