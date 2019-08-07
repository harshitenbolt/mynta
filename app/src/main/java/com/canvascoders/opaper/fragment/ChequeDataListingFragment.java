package com.canvascoders.opaper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.BankDetailResp;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddendumEsignActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.StoreTypeListingActivity;
import com.canvascoders.opaper.adapters.ChequeListAdapter;
import com.canvascoders.opaper.adapters.StoreReListingAdapter;
import com.canvascoders.opaper.adapters.VendorAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ChequeDataListingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    Context mcontext;
    RecyclerView rvCheque;
    Button btn_next;
    VendorList vendor;
    String str_process_id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chequelist, container, false);
        mcontext = this.getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            vendor = (VendorList) bundle.getSerializable("data");
            str_process_id = String.valueOf(vendor.getProccessId());
        }
        sessionManager = new SessionManager(mcontext);
        init(view);
        return view;
    }

    private void init(View view) {
//        DashboardActivity.settitle(Constants.TITLE_BANK_DETAILS);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        rvCheque = (RecyclerView) view.findViewById(R.id.rvCheqList);
        rvCheque.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCheque.setLayoutManager(linearLayoutManager);


        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commanFragmentCallWithBackStack(new ChequedataUpdateFragment(), vendor);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    getBankDetails();
                } else {
                    Constants.ShowNoInternet(getActivity());
                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getBankDetails();
        } else {
            Constants.ShowNoInternet(getActivity());
        }

//        DashboardActivity.settitle(Constants.TITLE_BANK_DETAILS);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            DashboardActivity.settitle(Constants.TITLE_BANK_DETAILS);
        }
    }

    private void getBankDetails() {
        JsonObject dataObj = new JsonObject();
        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, vendor.getProccessId());
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());

        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).getBankDetails("Bearer " + sessionManager.getToken(), dataObj).enqueue(new Callback<BankDetailResp>() {
            @Override
            public void onResponse(Call<BankDetailResp> call, retrofit2.Response<BankDetailResp> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    try {
                        String res = response.body().toString();
                        Mylogger.getInstance().Logit("BANK DETAILS", res);
                        if (response.code() == 200) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                ChequeListAdapter chequeListAdapter = new ChequeListAdapter(mcontext, response.body().getData());

                                rvCheque.setAdapter(chequeListAdapter);
                                chequeListAdapter.notifyDataSetChanged();

                            }
                            if (response.body().getAdd_btn()) {
                                btn_next.setVisibility(View.VISIBLE);
                            } else {
                                btn_next.setVisibility(View.GONE);
                            }
                        } else if (response.code() == 405) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            sessionManager.logoutUser(getContext());
                        } else if (response.code() == 411) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            sessionManager.logoutUser(getContext());
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }
                    /////
                }
            }

            @Override
            public void onFailure(Call<BankDetailResp> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }
    //for test

    public void commanFragmentCallWithBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendor);

        if (cFragment != null) {


            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.rvMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


    @Override
    public void onRefresh() {
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getBankDetails();
        } else {
            Constants.ShowNoInternet(getActivity());
        }
       // getBankDetails();
    }


}
