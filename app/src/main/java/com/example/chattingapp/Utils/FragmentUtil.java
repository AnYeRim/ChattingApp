package com.example.chattingapp.Utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentUtil {

    public void changeFragment(FragmentManager manager, int container, Fragment fragment){
        manager.beginTransaction().replace(container, fragment).commit();
    }

    public void changeFragment(FragmentManager manager, int container, Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        manager.beginTransaction().replace(container, fragment).commit();
    }
}