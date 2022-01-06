package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.FragmentUtil;
import com.example.chattingapp.databinding.FragmentPasswordBinding;

public class PasswordFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private FragmentPasswordBinding binding;
    private FragmentUtil fragmentUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);

        fragmentUtil = new FragmentUtil();
        User user = (User) getArguments().getSerializable("User");
        binding.txtPhone.setText(user.getPhone());

        binding.btnOK.setOnClickListener(this);
        binding.txtBackBegin.setOnClickListener(this);

        binding.edtPW.addTextChangedListener(this);
        binding.edtCheckPW.addTextChangedListener(this);

        return binding.getRoot();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                //입력한 번호로 인증번호 보내는 기능 구현
                String pw = String.valueOf(binding.edtPW.getText());
                String chkPW = String.valueOf(binding.edtCheckPW.getText());

                if(pw.equals(chkPW)){
                    //폰번호 비밀번호 기억하며 다음 회원가입 경로로 이동 구현해야함
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new JoinInfoFragment()).commit();
                    break;
                }else {
                    Toast.makeText(getActivity(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                // 지금까지 데이터 지우기
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new LoginFragment()).commit();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(binding.edtPW.length() > 0 && binding.edtCheckPW.length() > 0){
            binding.btnOK.setEnabled(true);
        }
    }

}