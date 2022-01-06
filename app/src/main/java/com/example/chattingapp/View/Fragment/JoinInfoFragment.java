package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.FragmentJoinInfoBinding;

public class JoinInfoFragment extends Fragment implements View.OnClickListener {

    private FragmentJoinInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJoinInfoBinding.inflate(inflater, container, false);

        binding.btnOK.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOK:
                // 지금까지 모은 데이터 보관하며 이메일 인증 화면 띄우기.
                // 자동 친구 추가 체크되었는지 확인해서 그것도 데이터 가지고 있어야 함
                if(binding.edtNikname.length() > 0
                        && binding.edtBirthDay.length() > 0
                        && !binding.spnGender.getSelectedItem().toString().equals("성별")){

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new AuthEmailFragment()).commit();

                }else {
                    Toast.makeText(getActivity(),"정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}