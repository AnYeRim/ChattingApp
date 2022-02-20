package com.example.chattingapp.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.View.Activity.RoomActivity;
import com.example.chattingapp.databinding.ItemRoomBinding;

import java.util.ArrayList;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.ViewHolder> {

    private Context mContext;
    private ArrayList<Room> data;

    public AdapterRoom(Context mContext, ArrayList<Room> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_room,parent,false);
        return new AdapterRoom.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRoom.ViewHolder holder, int position) {
        holder.setItemRoomBinding(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ItemRoomBinding itemRoomBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemRoomBinding = ItemRoomBinding.bind(itemView);
            itemRoomBinding.linearRoom.setOnClickListener(this);
            itemRoomBinding.linearRoom.setOnLongClickListener(this);
        }

        void setItemRoomBinding(Room data) {
            //Glide.with(mContext).load(data.getImageURL()).into(itemFriendsBinding.imgProfile);
            itemRoomBinding.txtRoomTitle.setText(data.getTitle());
            itemRoomBinding.txtTotal.setText(data.getTotal()+"");
            itemRoomBinding.txtUpdateDate.setText(data.getUpdateDate());
            itemRoomBinding.txtMessage.setText(data.getMessage());
            itemRoomBinding.txtNewMessage.setText(Integer.toString(data.getCntNewMessage()));

            if(data.getTotal() == 1 || data.getTotal() == 2){
                itemRoomBinding.txtTotal.setVisibility(View.GONE);
            }

            if(data.isAlarm().equals("Y")){
                itemRoomBinding.imgAlarm.setVisibility(View.GONE);
            }else {
                itemRoomBinding.imgAlarm.setVisibility(View.VISIBLE);
            }

            if(data.getCntNewMessage() == 0){
                itemRoomBinding.txtNewMessage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Room room = data.get(position);

            Intent intent = new Intent(mContext, RoomActivity.class);
            intent.putExtra("data", room);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();

            System.out.println("롱클릭");
            final String[] items;
            if(data.get(position).isAlarm().equals("Y")){
                items = new String[]{"채팅방 이름 설정","즐겨찾기에 추가","채팅방 알림 끄기","나가기"};
            }else {
                items = new String[]{"채팅방 이름 설정","즐겨찾기에 추가","채팅방 알림 켜기","나가기"};
            }

            AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
            dlg.setTitle(data.get(position).getTitle());
            dlg.setItems(items,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    switch (which){
                        case 0:
                            Toast.makeText(mContext, "채팅방 이름 설정", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(mContext, "즐겨찾기에 추가", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            if(data.get(position).isAlarm().equals("Y")){
                                data.get(position).setAlarm("N");
                            }else {
                                data.get(position).setAlarm("Y");
                            }
                            Toast.makeText(mContext, "채팅방 알림", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(mContext, "나가기", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    notifyDataSetChanged();
                    Toast.makeText(mContext,items[which],Toast.LENGTH_SHORT).show();
                }
            });
            dlg.show();

            return true;
        }
    }
}
