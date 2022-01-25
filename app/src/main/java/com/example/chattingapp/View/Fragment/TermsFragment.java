package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chattingapp.Model.APIClient;
import com.example.chattingapp.Model.APIInterface;
import com.example.chattingapp.Model.DTO.Terms;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.FragmentUtil;
import com.example.chattingapp.View.Adapter.AdapterTerms;
import com.example.chattingapp.databinding.FragmentTermsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Terms> resTerms;
    private AdapterTerms adapterTerms;

    private FragmentTermsBinding binding;
    private FragmentUtil fragmentUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTermsBinding.inflate(inflater, container, false);

        binding.txtBackBegin.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(this);
        binding.chkAll.setOnClickListener(this);

        fragmentUtil = new FragmentUtil();
        setTermsData();

        return binding.getRoot();
    }

    private void setTermsData() {
        APIInterface apiInterface = APIClient.getClient(null).create(APIInterface.class);
        Call<ArrayList<Terms>> call = apiInterface.doGetTermsList();

        //NetworkResponse<ArrayList<Terms>> networkResponse = new NetworkResponse<ArrayList<Terms>>();
        call.enqueue(new Callback<ArrayList<Terms>>() {
            @Override
            public void onResponse(Call<ArrayList<Terms>> call, Response<ArrayList<Terms>> response) {
                if(response.isSuccessful()){
                    resTerms = response.body();
                    setRecyclerTerm();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Terms>> call, Throwable t) {
                call.cancel();
            }
        });
/*
        if(networkResponse.getRes() != null){
            resTerms = networkResponse.getRes();
            setRecyclerTerm();
        }else {
            call.cancel();
        }*/
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
                //전체 선택 체크 상태에 따라 전체선택/해제됨
                boolean checked = ((CheckBox) view).isChecked();
                adapterTerms.setCheckedAll(checked);
                break;
            case R.id.btnAgree:
                // 필수 약관 동의 체크 후 휴대폰 인증화면으로 이동 구현해야함.
                if (adapterTerms.checkRequired()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Terms", adapterTerms.getData());
                    fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container,
                            new AuthPhoneFragment(), bundle);
                } else {
                    Toast.makeText(getActivity(), "필수 약관을 모두 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                fragmentUtil.changeFragment(getActivity().getSupportFragmentManager(), R.id.frg_container, new LoginFragment());
                break;
        }
    }
}