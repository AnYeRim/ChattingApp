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
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);

        fragmentUtil = new FragmentUtil();

        user = (User) getArguments().getSerializable("User");
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
                //비밀번호 규정에 맞게 입력했는지 체크 후 화면전환
                if(checkPassword()){
                    user.setPassword(binding.edtPW.getText().toString());
                    fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container, new JoinInfoFragment(),
                            "User", user);
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container, new LoginFragment());
                break;
        }
    }

    private boolean checkPassword() {
        String pw = binding.edtPW.getText().toString();
        String chkPW = binding.edtCheckPW.getText().toString();

        if(pw.length() < 8 || pw.length() > 16
                || chkPW.length() < 8 || chkPW.length() > 16){
            Toast.makeText(getActivity(), "비밀번호는 8~16자로 적어주세요.", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(pw.equals(chkPW)){
            return  true;
        }else {
            Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return  false;
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
        if(binding.edtPW.length() >= 8 && binding.edtCheckPW.length() >= 8){
            binding.btnOK.setEnabled(true);
        }else {
            binding.btnOK.setEnabled(false);
        }
    }

}