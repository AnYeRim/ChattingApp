package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.databinding.FragmentJoinInfoBinding;

import java.util.ArrayList;

public class JoinInfoFragment extends BaseFragment implements View.OnClickListener {

    private FragmentJoinInfoBinding binding;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJoinInfoBinding.inflate(inflater, container, false);

        binding.imgProfile.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);

        user = (User) getArguments().getSerializable("User");

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgProfile:
                //TODO 갤러리 이미지 선택하도록
                break;
            case R.id.btnOK:
                if(checkInputData()){
                    setUserData();
                    changeFragment(R.id.frg_container, new AuthEmailFragment(), getBundle());
                }
                break;
        }
    }

    private void setUserData() {
        //TODO 이미지도 있으면 서버에 저장 후 url 데이터 넣도록
        user.setNikname(getNikName());
        user.setBirthday(getBirthday());
        user.setAuto_add_friend(isCheckedAutoAddFriend());
        if(getGender().equals("여성")){
            user.setGender("F");
        }else {
            user.setGender("M");
        }
    }

    private boolean checkInputData() {
        if(getNikName().length() == 0){
            Toast.makeText(getActivity(),"닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(getBirthday().length() != 8){
            Toast.makeText(getActivity(),"생년월일이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(getGender().equals("성별")){
            Toast.makeText(getActivity(),"성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isCheckedAutoAddFriend() {
        return binding.chkAutoAddFriend.isChecked();
    }

    @NonNull
    private String getGender() {
        return binding.spnGender.getSelectedItem().toString();
    }

    @NonNull
    private String getBirthday() {
        return binding.edtBirthDay.getText().toString();
    }

    @NonNull
    private String getNikName() {
        return binding.edtNikname.getText().toString();
    }

    @NonNull
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Terms", getTerms());
        bundle.putSerializable("User", user);
        return bundle;
    }

    private ArrayList<Terms> getTerms() {
        return (ArrayList<Terms>) getArguments().getSerializable("Terms");
    }
}