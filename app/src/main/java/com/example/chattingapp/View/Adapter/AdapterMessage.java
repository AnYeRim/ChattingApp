package com.example.chattingapp.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Model.DTO.Message;
import com.example.chattingapp.R;
import com.example.chattingapp.View.Activity.RoomActivity;
import com.example.chattingapp.databinding.ItemMessageBinding;

import java.util.ArrayList;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {

    private Context mContext;
    private ArrayList<Message> data;

    public AdapterMessage(Context mContext, ArrayList<Message> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new AdapterMessage.ViewHolder(view);
    }

    public void addData(Message message) {
        data.add(message);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessage.ViewHolder holder, int position) {
        holder.setItemMessageBinding(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageBinding itemMessageBinding;
        private String userID;
        private ConstraintSet constraintSetRight, constraintSetLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMessageBinding = ItemMessageBinding.bind(itemView);

            userID = ((RoomActivity) mContext).getUserID();
            initConstraintSet();
        }

        private void initConstraintSet() {
            constraintSetLeft = new ConstraintSet();
            constraintSetLeft.clone(itemMessageBinding.constraint);

            constraintSetRight = new ConstraintSet();
            constraintSetRight.clone(itemMessageBinding.constraint);

            constraintSetRight.clear(R.id.txtMessage, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtMessage, ConstraintSet.RIGHT, R.id.constraint, ConstraintSet.RIGHT, 0);

            constraintSetRight.clear(R.id.txtUnreadTotal, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtUnreadTotal, ConstraintSet.RIGHT, R.id.txtMessage, ConstraintSet.LEFT, 0);

            constraintSetRight.clear(R.id.txtCreatedDate, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtCreatedDate, ConstraintSet.RIGHT, R.id.txtMessage, ConstraintSet.LEFT, 0);
        }

        void setItemMessageBinding(Message data) {
            itemMessageBinding.txtMessage.setText(data.getMessage());
            itemMessageBinding.txtCreatedDate.setText(data.getCreated_at());

            setUnReadTotal(data.getUnread_total());
            setMessageStyleByFromId(data.getFrom_id());
        }

        private void setMessageStyleByFromId(String fromId) {
            if (fromId.equals(userID)) {
                setMessageStyle(R.color.white, R.color.main, constraintSetRight);
                return;
            }

            setMessageStyle(R.color.black, R.color.white, constraintSetLeft);
        }

        private void setUnReadTotal(int total) {
            if (total == 0) {
                itemMessageBinding.txtUnreadTotal.setText("");
                return;
            }

            itemMessageBinding.txtUnreadTotal.setText(String.valueOf(total));
        }

        private void setMessageStyle(int textColor, int backColor, ConstraintSet align) {
            itemMessageBinding.txtMessage.setTextColor(mContext.getColor(textColor));
            itemMessageBinding.txtMessage.setBackgroundColor(mContext.getColor(backColor));
            align.applyTo(itemMessageBinding.constraint);
        }

    }
}
