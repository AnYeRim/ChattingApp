package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.ResponseUser;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentLoginBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private final String TAG = getClass().getSimpleName();

    private FragmentLoginBinding binding;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.edtID.addTextChangedListener(this);
        binding.edtPW.addTextChangedListener(this);

        binding.btnLogin.setOnClickListener(this);
        binding.btnJoin.setOnClickListener(this);
        binding.txtFindIDPW.setOnClickListener(this);

        user = new User();

        return binding.getRoot();
    }

    private void setFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, task.getException().toString());
                return;
            }

            user.setFb_token(task.getResult());

            doLogin();
        });
    }

    private void doLogin() {
        Call<ResponseUser> call = getApiInterface().doGetUser(user);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUser> call, @NonNull Response<ResponseUser> response) {
                if(response.code() == 400){
                    showMessage("유저 정보가 올바르지 않습니다.");
                }
                if (isSuccessResponse(response)) {
                    setSharedPreference("token", response.body().getToken());
                    setSharedPreference("nikName", response.body().getUser().getNikname());
                    setSharedPreference("userID", response.body().getUser().getId());
                    startActivity(SplashActivity.class);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseUser> call, @NonNull Throwable t) {
                showMessage("로그인 실패");
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void setSharedPreference(String token, String token2) {
        SharedPreferenceUtil.setData(getContext(), token, token2);
    }

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(null).create(APIInterface.class);
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                user.setPhone(binding.edtID.getText().toString());
                user.setEmail(binding.edtID.getText().toString());
                user.setPassword(binding.edtPW.getText().toString());
                setFirebaseToken();
                break;
            case R.id.btnJoin:
                changeFragment(R.id.frg_container, new TermsFragment());
                break;
            case R.id.txtFind_ID_PW:
                //TODO 아이디 비번 찾는 화면으로 이동
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
        setEnabledBtnLogin();
    }

    private void setEnabledBtnLogin() {
        if (binding.edtID.length() > 0 && binding.edtPW.length() > 0) {
            binding.btnLogin.setEnabled(true);
            return;
        }

        binding.btnLogin.setEnabled(false);
    }
}