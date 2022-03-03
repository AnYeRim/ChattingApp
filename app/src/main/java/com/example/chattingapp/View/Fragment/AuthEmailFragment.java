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
import com.example.chattingapp.Model.DTO.AgreeTerms;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.ResponseUser;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentAuthEmailBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthEmailFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private final String TAG = getClass().getSimpleName();
    private FragmentAuthEmailBinding binding;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthEmailBinding.inflate(inflater, container, false);

        binding.txtLater.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.btnOK.setOnClickListener(this);

        binding.edtEmail.addTextChangedListener(this);
        binding.edtAuthNum.addTextChangedListener(this);

        user = (User) getArguments().getSerializable("User");

        return binding.getRoot();
    }

    private void setFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, task.getException().toString());
                return;
            }

            Log.d(TAG, task.getResult());
            user.setFb_token(task.getResult());

            doCreateUser();
        });
    }

    private void doCreateUser() {
        Call<User> call = getApiInterface().doCreateUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (isSuccessResponse(response)) {
                    doCreateAgreeTerms();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                showMessage("유저 정보 insert 실패");
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void doCreateAgreeTerms() {
        Call<AgreeTerms> call = getApiInterface().doCreateAgreeTerms(getAgreeTerms());
        call.enqueue(new Callback<AgreeTerms>() {
            @Override
            public void onResponse(@NonNull Call<AgreeTerms> call, @NonNull Response<AgreeTerms> response) {
                if (response.isSuccessful()) {
                    doLogin();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AgreeTerms> call, @NonNull Throwable t) {
                showMessage("동의 내역 insert 실패");
                Log.e(TAG, t.getMessage());
                call.cancel();
            }
        });
    }

    private void doLogin() {
        Call<ResponseUser> call = getApiInterface().doGetUser(user);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUser> call, @NonNull Response<ResponseUser> response) {
                if(response.code() == 400){
                    showMessage("유저 정보가 옳바르지 않습니다.");
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

    @NonNull
    private APIInterface getApiInterface() {
        return APIClient.getClient(null).create(APIInterface.class);
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    void setSharedPreference(String data_name, String data) {
        SharedPreferenceUtil.setData(getContext(), data_name, data);
    }

    @NonNull
    private AgreeTerms getAgreeTerms() {
        AgreeTerms agreeTerms = new AgreeTerms();
        agreeTerms.setTerms(getTerms());
        agreeTerms.setUser_phone(user.getPhone());
        return agreeTerms;
    }

    private ArrayList<Terms> getTerms() {
        return (ArrayList<Terms>) getArguments().getSerializable("Terms");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLater:
                setFirebaseToken();
                break;
            case R.id.btnSend:
                if (!binding.chkTerm.isChecked()) {
                    showMessage("개인정보 이용 동의해주세요");
                    return;
                }
                //TODO 입력한 이메일로 인증번호 보내기
                binding.linearSendMail.setVisibility(View.GONE);
                binding.linearAuthNum.setVisibility(View.VISIBLE);
                binding.edtEmail.setEnabled(false);
                break;
            case R.id.btnOK:
                user.setEmail(binding.edtEmail.getText().toString());
                //TODO 인증번호 입력된 값과 보낸 값이 같은지 비교하고 맞으면 아래 실행하도록 수정해야함.
                setFirebaseToken();
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
        setEnabledBtnSend();
        setEnabledBtnOK();

    }

    private void setEnabledBtnSend() {
        if (binding.edtEmail.length() > 0) {
            binding.btnSend.setEnabled(true);
            return;
        }

        binding.btnSend.setEnabled(false);
    }

    private void setEnabledBtnOK() {
        if (binding.edtAuthNum.length() > 0) {
            binding.btnOK.setEnabled(true);
            return;
        }
        binding.btnOK.setEnabled(false);
    }
}