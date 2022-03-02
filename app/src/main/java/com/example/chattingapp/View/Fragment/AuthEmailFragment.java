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
import com.example.chattingapp.Model.DTO.AgreeTerms;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.VO.ResponseUser;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.ActivityUtils;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.SplashActivity;
import com.example.chattingapp.databinding.FragmentAuthEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthEmailFragment extends Fragment implements View.OnClickListener, TextWatcher {

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
        binding.edtEmail.addTextChangedListener(this);
        binding.edtAuthNum.addTextChangedListener(this);

        return binding.getRoot();
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        user.setFb_token(token);
                        Log.d("TAG", token);
                        doCreateUser();
                    }
                });
    }

    private void doCreateUser() {
        Call<User> call = apiInterface.doCreateUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(isSuccessResponse(response)){
                    doCreateAgreeTerms();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(),"유저 정보 insert 실패", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void doCreateAgreeTerms() {
        AgreeTerms agreeTerms = new AgreeTerms(terms,user.getPhone());

        Call<AgreeTerms> call = apiInterface.doCreateAgreeTerms(agreeTerms);
        call.enqueue(new Callback<AgreeTerms>() {
            @Override
            public void onResponse(Call<AgreeTerms> call, Response<AgreeTerms> response) {
                if(response.isSuccessful()){
                    doLogin();
                }
            }

            @Override
            public void onFailure(Call<AgreeTerms> call, Throwable t) {
                Toast.makeText(getContext(),"동의 내역 insert 실패", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void doLogin() {
        Call<ResponseUser> call = apiInterface.doGetUser(user);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                Log.d("TAG", response.toString());
                if(isSuccessResponse(response)){
                    if(response.body().getToken() == null){
                        Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                    }else {
                        SharedPreferenceUtil.setData(getContext(), "token", response.body().getToken());
                        SharedPreferenceUtil.setData(getContext(), "nikname", response.body().getUser().getNikname());
                        SharedPreferenceUtil.setData(getContext(), "userID", response.body().getUser().getId());
                        activityUtils.newActivity(getActivity(), SplashActivity.class);
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLater:
                // 서버로 회원가입 정보 넘겨서 추가하고 메인화면 띄우기
                getFirebaseToken();
                break;
            case R.id.btnSend:
                if(binding.chkTerm.isChecked() == true){
                    // 입력한 이메일로 인증번호 보내기
                    binding.linearSendMail.setVisibility(View.GONE);
                    binding.linearAuthNum.setVisibility(View.VISIBLE);
                    binding.edtEmail.setEnabled(false);
                }else {
                    Toast.makeText(getActivity(),"개인정보 이용 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnOK:
                // 인증번호 입력된 값과 보낸 값이 같은지 비교하고 맞으면 회원가입 완료하기
                user.setEmail(binding.edtEmail.getText().toString());
                if(true) {
                    getFirebaseToken();
                }
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
        if (binding.btnSend.isEnabled() == false && binding.edtEmail.length() > 0) {
            binding.btnSend.setEnabled(true);
        }else if (binding.edtEmail.length() == 0){
            binding.btnSend.setEnabled(false);
        }else if (binding.btnOK.isEnabled() == false && binding.edtAuthNum.length() > 0){
            binding.btnOK.setEnabled(true);
        }else if (binding.edtAuthNum.length() == 0){
            binding.btnOK.setEnabled(false);
        }
    }
}