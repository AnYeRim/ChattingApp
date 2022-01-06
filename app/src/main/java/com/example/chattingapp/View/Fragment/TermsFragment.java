package com.example.chattingapp.View.Fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.chattingapp.View.Adapter.AdapterTerms;
import com.example.chattingapp.databinding.FragmentTermsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends Fragment implements View.OnClickListener {

    private APIInterface apiInterface;
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
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<Terms>> call = apiInterface.doGetTermsList();
        call.enqueue(new Callback<ArrayList<Terms>>() {
            @Override
            public void onResponse(Call<ArrayList<Terms>> call, Response<ArrayList<Terms>> response) {
                if(response.isSuccessful()){
                    Log.d("TAG", response.code() + "");
                    resTerms = response.body();
                    setRecyclerTerm();
                }else {
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Terms>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                call.cancel();
            }
        });
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
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new AuthPhoneFragment()).commit();
                } else {
                    Toast.makeText(getActivity(), "필수 약관을 모두 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackBegin:
                //로그인 화면으로 되돌아가기
                // 지금까지 데이터 지우기
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frg_container, new LoginFragment()).commit();
                break;
        }
    }
}