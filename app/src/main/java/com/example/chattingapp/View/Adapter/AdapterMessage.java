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
import com.example.chattingapp.Utils.ActivityUtils;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message,parent,false);
        return new AdapterMessage.ViewHolder(view);
    }

    public void addData(Message message){
        data.add(message);
        notifyDataSetChanged();
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
        private ActivityUtils activityUtils;
        private String userID;
        private ConstraintSet constraintSetRight, constraintSetLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMessageBinding = ItemMessageBinding.bind(itemView);

            activityUtils = new ActivityUtils();
            userID = activityUtils.getUserID(mContext);
            setConstraintSetRight();
        }

        private void setConstraintSetRight() {
            constraintSetRight = new ConstraintSet();
            constraintSetLeft = new ConstraintSet();

            constraintSetRight.clone(itemMessageBinding.constraint);
            constraintSetLeft.clone(itemMessageBinding.constraint);

            constraintSetRight.clear(R.id.txtMessage, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtMessage,ConstraintSet.RIGHT,
                    R.id.constraint, ConstraintSet.RIGHT, 0);

            constraintSetRight.clear(R.id.txtReadTotal, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtReadTotal,ConstraintSet.RIGHT,
                    R.id.txtMessage, ConstraintSet.LEFT, 0);

            constraintSetRight.clear(R.id.txtCreatedDate, ConstraintSet.LEFT);
            constraintSetRight.connect(R.id.txtCreatedDate,ConstraintSet.RIGHT,
                    R.id.txtMessage, ConstraintSet.LEFT, 0);
        }

        void setItemMessageBinding(Message data) {
            itemMessageBinding.txtMessage.setText(data.getMessage());
            itemMessageBinding.txtReadTotal.setText(data.getUnread_total()+"");
            itemMessageBinding.txtCreatedDate.setText(data.getCreated_at());
            if(data.getFrom_id().equals(userID)){
                itemMessageBinding.txtMessage.setTextColor(mContext.getColor(R.color.white));
                itemMessageBinding.txtMessage.setBackgroundColor(mContext.getColor(R.color.main));
                constraintSetRight.applyTo(itemMessageBinding.constraint);
            }else {
                itemMessageBinding.txtMessage.setTextColor(mContext.getColor(R.color.black));
                itemMessageBinding.txtMessage.setBackgroundColor(mContext.getColor(R.color.white));
                constraintSetLeft.applyTo(itemMessageBinding.constraint);
            }
        }

    }
}
