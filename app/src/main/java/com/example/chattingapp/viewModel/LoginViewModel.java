package com.example.chattingapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chattingapp.Model.DTO.User;
import com.example.chattingapp.Model.Repository.FirebaseRepository;
import com.example.chattingapp.Model.Repository.UserRepository;
import com.example.chattingapp.Model.VO.ResponseUser;
import com.example.chattingapp.Utils.SharedPreferenceUtil;

public class LoginViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();

    private UserRepository userRepository;
    private FirebaseRepository firebaseRepository;

    private LiveData<ResponseUser> loginResult;
    private LiveData<String> tokenResult;
    private MutableLiveData<Boolean> enabledBtnLogin;

    public LoginViewModel() {
        userRepository = new UserRepository();
        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<ResponseUser> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getTokenResult() {
        return tokenResult;
    }

    public LiveData<Boolean> getBtnLogin() {
        if (enabledBtnLogin == null) {
            enabledBtnLogin = new MutableLiveData<>();
        }
        return enabledBtnLogin;
    }

    public void getFirebaseToken(){
        tokenResult = firebaseRepository.getFirebaseToken();
    }

    public void doLogin(User user){
        loginResult = userRepository.doLogin(user);
    }

    public void setSharedPreference(ResponseUser responseUser) {
        SharedPreferenceUtil.setData("token", responseUser.getToken());
        SharedPreferenceUtil.setData("nikName", responseUser.getUser().getNikname());
        SharedPreferenceUtil.setData("userID", responseUser.getUser().getId());
    }

    public void checkText(String id, String pw) {
        if (id.length() > 0 && pw.length() > 0) {
            enabledBtnLogin.setValue(true);
            return;
        }
        enabledBtnLogin.setValue(false);
    }
}