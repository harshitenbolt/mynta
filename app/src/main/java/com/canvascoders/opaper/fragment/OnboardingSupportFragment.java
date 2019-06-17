package com.canvascoders.opaper.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.Beans.SupportListResponse.SupportListResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.SupportDetailActivity;
import com.canvascoders.opaper.adapters.SupportListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingSupportFragment extends Fragment implements RecyclerViewClickListener {
    private String support_id;
    View view;
    SessionManager sessionManager;
    RecyclerView rvOnboardingSupport;
    Context mContext;
    SupportListAdapter supportListAdapter;
    List<Datum> supportList  = new ArrayList<>();
    public OnboardingSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_onboarding_support, container, false);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }

    private void init() {
        rvOnboardingSupport = view.findViewById(R.id.rvOnboarding);
        ApiCallgetReports();

    }

    private void ApiCallgetReports() {
        Map<String,String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID,sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).getSupportList("Bearer "+sessionManager.getToken(),param).enqueue(new Callback<SupportListResponse>() {
            @Override
            public void onResponse(Call<SupportListResponse> call, Response<SupportListResponse> response) {
                if(response.isSuccessful()){
                    SupportListResponse supportListResponse = response.body();

                    if(supportListResponse.getData().size()>0){
                        supportList.addAll(supportListResponse.getData());

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvOnboardingSupport.setLayoutManager(linearLayoutManager);
                        supportListAdapter = new SupportListAdapter(supportList,getActivity(),OnboardingSupportFragment.this);
                        rvOnboardingSupport.setAdapter(supportListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<SupportListResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        support_id = String.valueOf(supportList.get(position).getId());
        Log.e("Support_id",support_id);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_SUPPORT_ID, String.valueOf(support_id));
        Intent i = new Intent(getActivity(), SupportDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
