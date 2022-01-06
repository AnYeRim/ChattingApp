package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.FragmentUtil;
import com.example.chattingapp.databinding.FragmentJoinInfoBinding;

import java.util.ArrayList;

public class JoinInfoFragment extends Fragment implements View.OnClickListener {

    private FragmentJoinInfoBinding binding;
    private FragmentUtil fragmentUtil;
    private User user;
    private ArrayList<Terms> terms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJoinInfoBinding.inflate(inflater, container, false);

        binding.imgProfile.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);

        fragmentUtil = new FragmentUtil();
        user = (User) getArguments().getSerializable("User");
        terms = (ArrayList<Terms>) getArguments().getSerializable("Terms");

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgProfile:
                // 갤러리 이미지 선택하도록
                break;
            case R.id.btnOK:
                // 지금까지 모은 데이터 보관하며 이메일 인증 화면 띄우기.
                // 자동 친구 추가 체크되었는지 확인해서 그것도 데이터 가지고 있어야 함
                if(checkInputData()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Terms", terms);
                    bundle.putSerializable("User", user);
                    fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container,
                            new AuthEmailFragment(), bundle);
                }
                break;
        }
    }

    private boolean checkInputData() {
        String nikname = binding.edtNikname.getText().toString();
        String birthday = binding.edtBirthDay.getText().toString();
        String gender = binding.spnGender.getSelectedItem().toString();
        //boolean autoAddFriend = binding.chkAutoAddFriend.isChecked();

        if(nikname.length() == 0){
            Toast.makeText(getActivity(),"닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(birthday.length() != 8){
            Toast.makeText(getActivity(),"생년월일이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(gender.equals("성별")){
            Toast.makeText(getActivity(),"성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            // 이미지도 있으면 서버에 저장 후 url 데이터 넣도록
            user.setNikname(nikname);
            user.setBirthday(birthday);
            if(gender.equals("여성")){
                user.setGender("F");
            }else {
                user.setGender("M");
            }
            //user.setAuto_add_friend(autoAddFriend);
            return true;
        }
    }

}