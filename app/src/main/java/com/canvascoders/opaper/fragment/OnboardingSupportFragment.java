package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingSupportFragment extends Fragment implements RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String support_id;
    View view;
    SessionManager sessionManager;
    RecyclerView rvOnboardingSupport;
    Context mContext;

    String next_page_url = "support-listing", nextPageUrl1 = "support-listing";
    SupportListAdapter supportListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    List<Datum> supportList = new ArrayList<>();
    List<Datum> supportList1 = new ArrayList<>();
    ProgressDialog mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout llNoData;
    Spinner snDocType;
    String status = "";
    int search = 0;
    boolean onRefresh = false;
    private List<String> docTypeList = new ArrayList<>();

    public OnboardingSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_onboarding_support, container, false);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }

    private void init() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        llNoData = view.findViewById(R.id.llNoData);
        rvOnboardingSupport = view.findViewById(R.id.rvOnboarding);
        supportListAdapter = new SupportListAdapter(supportList, getActivity(), OnboardingSupportFragment.this);
        rvOnboardingSupport.setAdapter(supportListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvOnboardingSupport.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == 0) {
                    if (!next_page_url.equalsIgnoreCase(""))
                        ApiCallgetReports(status);
                } else {
                    if (!nextPageUrl1.equalsIgnoreCase("")) {

                    }
                    // ApiCallgetReportswithSearch(status);
                }


            }
        };

        rvOnboardingSupport.addOnScrollListener(scrollListener);


        snDocType = (Spinner) view.findViewById(R.id.snFilterType);
        docTypeList = Arrays.asList(getResources().getStringArray(R.array.FilterType));
        CustomAdapter<String> doctypeadapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, docTypeList);
        doctypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snDocType.setAdapter(doctypeadapter);

        snDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();
                status = selectedItem;
                if (selectedItem.equalsIgnoreCase("Select Filter")) {

                        supportList.clear();
                        next_page_url = "support-listing";
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            status = "";
                            ApiCallgetReports(status);
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }


                }


                if (selectedItem.equalsIgnoreCase("pending")) {
                    search = 0;
                    supportList.clear();
                    supportList1.clear();
                    next_page_url = "support-listing";
                    status = "pending";
                    ApiCallgetReports(status);
                }
                if (selectedItem.equalsIgnoreCase("in-progress")) {
                    search = 0;
                    next_page_url = "support-listing";
                    status = "in-progress";
                    supportList.clear();
                    ApiCallgetReports(status);
                    //  ApiCallgetReportswithSearch(status);
                }
                if (selectedItem.equalsIgnoreCase("closed")) {
                    search = 0;
                    status = "closed";
                    next_page_url = "support-listing";
                    supportList.clear();
                    ApiCallgetReports(status);
                    // ApiCallgetReportswithSearch(status);
                }
                if (selectedItem.equalsIgnoreCase("re-open")) {
                    search = 0;
                    supportList.clear();
                    next_page_url = "support-listing";
                    supportList1.clear();
                    status = "re-open";
                    ApiCallgetReports(status);
                    // ApiCallgetReportswithSearch(status);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



/*
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports(status);
        } else {
            Constants.ShowNoInternet(getActivity());
        }*/
        // ApiCallgetReports();

    }

    private void ApiCallgetReports(String s) {
        mProgress.show();
        //supportList.clear();
        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_IS_INVOICE, "0");
        param.put(Constants.PARAM_STATUS, s);

        ApiClient.getClient().create(ApiInterface.class).getSupportList(next_page_url, "Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SupportListResponse>() {
            @Override
            public void onResponse(Call<SupportListResponse> call, Response<SupportListResponse> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgress.dismiss();
                    SupportListResponse supportListResponse = response.body();
                    if (supportListResponse.getData() != null) {
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);

                        supportList.addAll(supportListResponse.getData());

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

                        if (supportList.size() > 0) {
                            llNoData.setVisibility(View.GONE);
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                        }

                        supportListAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("DoneDOnaNon", "DoneDOnaNon1");

                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SupportListResponse> call, Throwable t) {
                mProgress.dismiss();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

   /* private void ApiCallgetReportswithSearch(String status) {

        mProgress.show();

        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_IS_INVOICE, "0");
        param.put(Constants.PARAM_STATUS, status);
        ApiClient.getClient().create(ApiInterface.class).getSupportList(nextPageUrl1, "Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SupportListResponse>() {
            @Override
            public void onResponse(Call<SupportListResponse> call, Response<SupportListResponse> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgress.dismiss();
                    SupportListResponse supportListResponse = response.body();
                    if (supportListResponse.getData() != null) {
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);

                        supportList1.addAll(supportListResponse.getData());

                        supportListAdapter = new SupportListAdapter(supportList, getActivity(), OnboardingSupportFragment.this);
                        rvOnboardingSupport.setAdapter(supportListAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvOnboardingSupport.setLayoutManager(linearLayoutManager);


                        if (supportListResponse.getNextPageUrl() != null) {
                            if (!supportListResponse.getNextPageUrl().equalsIgnoreCase("")) {
                                nextPageUrl1 = supportListResponse.getNextPageUrl();
                                String[] separated = nextPageUrl1.split("api3/");
                                nextPageUrl1 = Constants.BaseURL + separated[1];
                                Log.e("next_page_url", nextPageUrl1);
                            }
                        } else {
                            nextPageUrl1 = "";
                        }


                        if(supportList1.size()>0){
                            llNoData.setVisibility(View.GONE);
                        }
                        else{
                            llNoData.setVisibility(View.VISIBLE);
                        }


                        supportListAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("DoneDOnaNon", "DoneDOnaNon1");

                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SupportListResponse> call, Throwable t) {
                mProgress.dismiss();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }*/


    @Override
    public void onClick(View view, int position) {
        support_id = String.valueOf(supportList.get(position).getId());
        Log.e("Support_id", support_id);
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
        search = 0;
        next_page_url = "support-listing";
        snDocType.setSelection(0);
        supportList.clear();
        status = "";
        onRefresh = true;
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports(status);
        } else {
            Constants.ShowNoInternet(getActivity());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOnboardingSupport.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == 0) {
                    if (!next_page_url.equalsIgnoreCase(""))
                        ApiCallgetReports(status);
                } else {
                    if (!nextPageUrl1.equalsIgnoreCase("")) {

                    }
                    // ApiCallgetReportswithSearch(status);
                }


            }
        };
        rvOnboardingSupport.addOnScrollListener(scrollListener);

    }

    class CustomAdapter<T> extends ArrayAdapter<T> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<T> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(12);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.rb_regular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

}
