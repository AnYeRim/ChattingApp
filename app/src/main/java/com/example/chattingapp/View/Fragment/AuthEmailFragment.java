package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.AgreeTerms;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.NetworkResponse;
import com.example.chattingapp.Model.VO.JsonUser;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentAuthEmailBinding;

import java.util.ArrayList;

import retrofit2.Call;

public class AuthEmailFragment extends Fragment implements View.OnClickListener {

    private ActivityUtils activityUtils;
    private FragmentAuthEmailBinding binding;
    private User user;
    private ArrayList<Terms> terms;
    private APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthEmailBinding.inflate(inflater, container, false);

        activityUtils = new ActivityUtils();

        user = (User) getArguments().getSerializable("User");
        terms = (ArrayList<Terms>) getArguments().getSerializable("Terms");

        apiInterface = APIClient.getClient(null).create(APIInterface.class);

        binding.txtLater.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);

        return binding.getRoot();
    }

    private void doCreateUser() {
        Call<User> call = apiInterface.doCreateUser(user);

        NetworkResponse<User> networkResponse = new NetworkResponse<User>();
        call.enqueue(networkResponse);

        if(networkResponse.getRes() != null){
            doCreateAgreeTerms();
        }else {
            Toast.makeText(getContext(),"유저 정보 insert 실패", Toast.LENGTH_SHORT).show();
            call.cancel();
        }
    }

    private void doCreateAgreeTerms() {
        AgreeTerms agreeTerms = new AgreeTerms(terms,user.getPhone());

        Call<AgreeTerms> call = apiInterface.doCreateAgreeTerms(agreeTerms);

        NetworkResponse<AgreeTerms> networkResponse = new NetworkResponse<AgreeTerms>();
        call.enqueue(networkResponse);

        if(networkResponse.getRes() != null){
            doLogin();
        }else {
            Toast.makeText(getContext(),"동의 내역 insert 실패", Toast.LENGTH_SHORT).show();
            call.cancel();
        }
    }

    private void doLogin() {
        Call<JsonUser> call = apiInterface.doGetUser(user);

        NetworkResponse<JsonUser> networkResponse = new NetworkResponse<JsonUser>();
        call.enqueue(networkResponse);

        if(networkResponse.getRes() != null){
            if(networkResponse.getRes().getToken() == null){
                Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferenceUtil.setData(getContext(), "token", networkResponse.getRes().getToken());
                SharedPreferenceUtil.setData(getContext(), "nikname", networkResponse.getRes().getUser().getNikname());
                activityUtils.newActivity(getActivity(), SplashActivity.class);
                getActivity().finish();
            }
        }else {
            Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
            call.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLater:
                // 서버로 회원가입 정보 넘겨서 추가하고 메인화면 띄우기
                doCreateUser();
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
                user.setEmail(binding.edtEmail.getText().toString());
                if(true) {
                    doCreateUser();
                }
                break;
        }

    }
}