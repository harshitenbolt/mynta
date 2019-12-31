package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.adapters.DeliveryBoysAssessmentListAdapter;
import com.canvascoders.opaper.adapters.SupportListAdapter;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryBoyAssessmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {


    View v;
    private String support_id;
    View view;
    SessionManager sessionManager;
    RecyclerView rvOnboardingSupport;
    Context mContext;

    String next_page_url = "support-listing", nextPageUrl1 = "support-listing";
    DeliveryBoysAssessmentListAdapter supportListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    List<Datum> supportList = new ArrayList<>();
    List<Datum> supportList1 = new ArrayList<>();
    ProgressDialog mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout llNoData;
   // Spinner snDocType;
    String status = "";
    int search = 0;
    boolean onRefresh = false;
    private List<String> docTypeList = new ArrayList<>();


    public DeliveryBoyAssessmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_delivery_boy_assessment, container, false);
        sessionManager = new SessionManager(getActivity());
        init();
        return v;
    }


    private void init() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        llNoData = view.findViewById(R.id.llNoData);
        rvOnboardingSupport = view.findViewById(R.id.rvOnboarding);
        supportListAdapter = new DeliveryBoysAssessmentListAdapter(supportList, getActivity(), DeliveryBoyAssessmentFragment.this);
        rvOnboardingSupport.setAdapter(supportListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvOnboardingSupport.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == 0) {
                    if (!next_page_url.equalsIgnoreCase("")){

                    }
                      //  ApiCallgetReports(status);
                } else {
                    if (!nextPageUrl1.equalsIgnoreCase("")) {

                    }
                    // ApiCallgetReportswithSearch(status);
                }


            }
        };

        rvOnboardingSupport.addOnScrollListener(scrollListener);


      //  snDocType = (Spinner) view.findViewById(R.id.snFilterType);
        docTypeList = Arrays.asList(getResources().getStringArray(R.array.FilterType));
        CustomAdapter<String> doctypeadapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, docTypeList);
        doctypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  snDocType.setAdapter(doctypeadapter);

      /*  snDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();
                status = selectedItem;
                if (selectedItem.equalsIgnoreCase("Select Filter")) {

                    supportList.clear();
                    next_page_url = "support-listing";
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        status = "";
                     //   ApiCallgetReports(status);
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
                  //  ApiCallgetReports(status);
                }
                if (selectedItem.equalsIgnoreCase("in-progress")) {
                    search = 0;
                    next_page_url = "support-listing";
                    status = "in-progress";
                    supportList.clear();
                //    ApiCallgetReports(status);
                    //  ApiCallgetReportswithSearch(status);
                }
                if (selectedItem.equalsIgnoreCase("closed")) {
                    search = 0;
                    status = "closed";
                    next_page_url = "support-listing";
                    supportList.clear();
                  //  ApiCallgetReports(status);
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
        });*/



/*
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports(status);
        } else {
            Constants.ShowNoInternet(getActivity());
        }*/
        // ApiCallgetReports();

    }


    @Override
    public void onRefresh() {
        search = 0;
        next_page_url = "support-listing";
       // snDocType.setSelection(0);
        supportList.clear();
        status = "";
        onRefresh = true;
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
          //  ApiCallgetReports(status);
        } else {
            Constants.ShowNoInternet(getActivity());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvOnboardingSupport.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == 0) {
                    if (!next_page_url.equalsIgnoreCase("")){

                    }
                    //    ApiCallgetReports(status);
                } else {
                    if (!nextPageUrl1.equalsIgnoreCase("")) {

                    }
                    // ApiCallgetReportswithSearch(status);
                }


            }
        };
        rvOnboardingSupport.addOnScrollListener(scrollListener);

    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {

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
