package com.example.chattingapp.Tool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Utils.SharedPreferenceUtil;

import java.io.Serializable;

public abstract class BaseFragment extends Fragment {

    public void changeFragment(int container, Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
    }

    public void changeFragment(int container, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        changeFragment(container, fragment);
    }

    public void startActivity(Class mClass) {
        Intent intent = new Intent(getActivity(), mClass);
        startActivity(intent);
    }

    public void startActivity(Class mClass, Serializable data){
        Intent intent = new Intent(getActivity(), mClass);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
