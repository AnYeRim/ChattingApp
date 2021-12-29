package com.example.chattingapp.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Model.DataTerms;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ItemTermsBinding;

import java.util.ArrayList;

public class AdapterTerms extends RecyclerView.Adapter<AdapterTerms.ViewHolder> {

    private Context mContext;
    private ArrayList<DataTerms> data;

    public AdapterTerms(Context mContext, ArrayList<DataTerms> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterTerms.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_terms,parent,false);
        return new AdapterTerms.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTerms.ViewHolder holder, int position) {
        holder.setItemTermsBinding(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setCheckedAll(boolean checked) {
        for(int i = 0; i<getItemCount(); i++){
            data.get(i).setChecked(checked);
        }

        notifyDataSetChanged();
    }

    public boolean checkRequired() {
        for(int i = 0; i<getItemCount(); i++){
            if(data.get(i).isRequired() == true && data.get(i).isChecked() == false){
                return false;
            }
        }
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemTermsBinding itemTermsBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTermsBinding = ItemTermsBinding.bind(itemView);

            itemTermsBinding.chkTerm.setOnClickListener(this);
            itemTermsBinding.btnDetail.setOnClickListener(this);

        }

        void setItemTermsBinding(DataTerms data) {
            itemTermsBinding.chkTerm.setText(data.getTitle());
            itemTermsBinding.chkTerm.setChecked(data.isChecked());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (view.getId()){
                case R.id.chkTerm:
                    boolean checked = ((CheckBox)view).isChecked();

                    data.get(position).setChecked(checked);

                    Toast.makeText(mContext,data.get(position).getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btnDetail:
                    Toast.makeText(mContext,data.get(position).getContents().toString(), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}
