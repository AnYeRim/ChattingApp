package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.R;
import com.example.chattingapp.Tool.BaseFragment;
import com.example.chattingapp.View.Adapter.AdapterTerms;
import com.example.chattingapp.databinding.FragmentTermsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<Terms> resTerms;
    private AdapterTerms adapterTerms;

    private FragmentTermsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTermsBinding.inflate(inflater, container, false);

        binding.txtBackBegin.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(this);
        binding.chkAll.setOnClickListener(this);

        setTermsData();

        return binding.getRoot();
    }

    private void setTermsData() {
        Call<ArrayList<Terms>> call = getApiInterface().doGetTermsList();
        call.enqueue(new Callback<ArrayList<Terms>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Terms>> call, @NonNull Response<ArrayList<Terms>> response) {
                if(isSuccessResponse(response)){
                    resTerms = response.body();
                    setRecyclerTerm();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Terms>> call, @NonNull Throwable t) {
                Log.d(TAG,t.getMessage());
                call.cancel();
            }
        });
    }

    @NonNull
    private APIInterface getApiInterface() {
        return  APIClient.getClient(null).create(APIInterface.class);
    }

    boolean isSuccessResponse(Response response) {
        return response.code() == 200 && response.isSuccessful() && response.body() != null;
    }

    private void setRecyclerTerm() {
        binding.recyclerTerm.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterTerms = new AdapterTerms(getActivity(), resTerms);
        binding.recyclerTerm.setAdapter(adapterTerms);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkAll:
                boolean checked = ((CheckBox) view).isChecked();
                adapterTerms.setCheckedAll(checked);
                break;
            case R.id.btnAgree:
                if (!adapterTerms.isCheckedRequired()) {
                    showMessage("필수 약관을 모두 동의해주세요");
                    return;
                }
                changeFragment(R.id.frg_container, new AuthPhoneFragment(), getBundle());
                break;
            case R.id.txtBackBegin:
                changeFragment(R.id.frg_container, new LoginFragment());
                break;
        }
    }

    @NonNull
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Terms", resTerms);
        return bundle;
    }

}