package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.JsonUser;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private ActivityUtils activityUtils;
    private FragmentLoginBinding binding;
    private APIInterface apiInterface;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        user = new User();
        activityUtils = new ActivityUtils();
        apiInterface = APIClient.getClient(null).create(APIInterface.class);

        binding.edtID.addTextChangedListener(this);
        binding.edtPW.addTextChangedListener(this);

        binding.btnLogin.setOnClickListener(this);
        binding.btnJoin.setOnClickListener(this);

        binding.txtFindIDPW.setOnClickListener(this);

        return binding.getRoot();
    }

    private void doLogin() {
        Call<JsonUser> call = apiInterface.doGetUser(user);
        call.enqueue(new Callback<JsonUser>() {
            @Override
            public void onResponse(Call<JsonUser> call, Response<JsonUser> response) {
                if(response.isSuccessful()){
                    Log.d("TAG", response.code() + "");
                    if(response.body().getToken() == null){
                        Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                    }else {
                        SharedPreferenceUtil.setData(getContext(), "token", response.body().getToken());
                        SharedPreferenceUtil.setData(getContext(), "nikname", response.body().getUser().getNikname());
                        activityUtils.newActivity(getActivity(), SplashActivity.class);
                        getActivity().finish();
                    }
                }else {
                    Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonUser> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                call.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                //DB와 비교하여 로그인 하는 기능 구현
                user.setPhone(binding.edtID.getText().toString());
                user.setEmail(binding.edtID.getText().toString());
                user.setPassword(binding.edtPW.getText().toString());
                doLogin();
                break;
            case R.id.btnJoin:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new TermsFragment()).commit();
                break;
            case R.id.txtFind_ID_PW:
                //아이디 비번 찾는 화면으로 이동
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
        if(binding.edtID.length() > 0 && binding.edtPW.length() > 0){
            binding.btnLogin.setEnabled(true);
        }else{
            binding.btnLogin.setEnabled(false);
        }
    }
}