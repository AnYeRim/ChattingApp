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
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.View.Activity.MainActivity;
import com.example.chattingapp.databinding.FragmentAuthEmailBinding;

public class AuthEmailFragment extends Fragment implements View.OnClickListener {

    private ActivityUtils activityUtils;
    private FragmentAuthEmailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthEmailBinding.inflate(inflater, container, false);

        activityUtils = new ActivityUtils();

        binding.txtLater.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLater:
                // 서버로 회원가입 정보 넘겨서 추가하고 메인화면 띄우기
                getActivity().finish();
                activityUtils.newActivity(getActivity(), MainActivity.class);
                break;
            case R.id.btnSend:
                if(binding.chkTerm.isChecked() == true){
                    // 입력한 이메일로 인증번호 보내기
                    binding.linearSendMail.setVisibility(View.GONE);
                    binding.linearAuthNum.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getActivity(),"개인정보 이용 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnOK:
                // 인증번호 입력된 값과 보낸 값이 같은지 비교하고 맞으면 회원가입 완료하기
                if(true) {
                    getActivity().finish();
                    activityUtils.newActivity(getActivity(), MainActivity.class);
                }
                break;
        }

    }
}