package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.Beans.SupportListResponse.SupportListResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.SupportDetailActivity;
import com.canvascoders.opaper.adapters.SupportListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

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
public class PaymentSupportFragment extends Fragment implements RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    View view;
    RecyclerView rvPayment;
    Context mContext;
    int support_id;
    List<Datum> list = new ArrayList<>();
    SupportListAdapter supportListAdapter;
    String next_page_url = "support-listing";
    SessionManager sessionManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout llNoData;
    ProgressDialog mProgress;
    private EndlessRecyclerViewScrollListener scrollListener;
    FloatingActionButton floatingActionButton;

    public PaymentSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_support, container, false);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }

    private void init() {
        rvPayment = view.findViewById(R.id.rvSupports);
        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        llNoData = view.findViewById(R.id.llNoData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPayment.setLayoutManager(linearLayoutManager);
        supportListAdapter = new SupportListAdapter(list, getActivity(), PaymentSupportFragment.this);
        rvPayment.setAdapter(supportListAdapter);
        floatingActionButton = view.findViewById(R.id.fbAdd);



        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports();
        } else {
            Constants.ShowNoInternet(getActivity());
        }




        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search
                if (!next_page_url.equalsIgnoreCase("")){
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallgetReports();
                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }
                }




            }
        };
        rvPayment.addOnScrollListener(scrollListener);

    }

    private void ApiCallgetReports() {
        mProgress.show();
        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_IS_INVOICE, "1");
        ApiClient.getClient().create(ApiInterface.class).getSupportList(next_page_url, "Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SupportListResponse>() {
            @Override
            public void onResponse(Call<SupportListResponse> call, Response<SupportListResponse> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgress.dismiss();

                    try {
                        SupportListResponse supportListResponse = response.body();
                        if (supportListResponse.getData() != null) {

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);

                            list.addAll(supportListResponse.getData());

                            if (supportListResponse.getNextPageUrl() != null) {
                                if (!supportListResponse.getNextPageUrl().equalsIgnoreCase("")) {
                                    next_page_url = supportListResponse.getNextPageUrl();
                                    String[] separated = next_page_url.split("api3/");
                                    next_page_url = Constants.BaseURL + separated[1];
                                    Log.e("next_page_url", next_page_url);
                                }
                            } else {
                                next_page_url = "";
                            }

                            supportListAdapter.notifyDataSetChanged();

                        } else {
                            Log.e("DoneDOnaNon", "DoneDOnaNon1");
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e("DoneDOnaNon", "DoneDOnaNon2");
                        e.printStackTrace();
                    }


                } else {
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SupportListResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgress.dismiss();

            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        support_id = list.get(position).getId();
        Log.e("Support_id", String.valueOf(support_id));
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_SUPPORT_ID, String.valueOf(support_id));
        Intent i = new Intent(getActivity(), SupportDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {

    }


    @Override
    public void onRefresh() {
        list.clear();
        next_page_url = "support-listing";
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports();
        } else {
            Constants.ShowNoInternet(getActivity());
        }


    }
}
