package com.example.chattingapp.Model.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseRepository {

    private final String TAG = getClass().getSimpleName();

    public LiveData<String> getFirebaseToken() {
        final MutableLiveData<String> data = new MutableLiveData<>();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, task.getException().toString());
                return;
            }
            data.setValue(task.getResult());
        });
        return data;
    }
}
