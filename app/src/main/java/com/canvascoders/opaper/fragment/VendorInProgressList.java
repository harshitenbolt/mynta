package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SearchListResponse.Datum;
import com.canvascoders.opaper.Beans.SearchListResponse.SearchListResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.TaskProccessDetailActivity;
import com.canvascoders.opaper.activity.VendorDetailActivity;
import com.canvascoders.opaper.adapters.VendorListInProgressAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorInProgressList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {


    AppCompatRadioButton radio_onboard;
    AppCompatRadioButton radio_inprogress;
    RecyclerView recyclerview;
    VendorListInProgressAdapter vendorAdapter;
    String TAG = "VendorLis";
    SessionManager sessionManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONObject object = new JSONObject();
    JSONObject objectSearch = new JSONObject();
    ImageView iv_clear_text;
    EditText edit_search_vendor;
    int page, page1, pageSearch = 1;
    TextView tvNoDataMsg;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<VendorList> vendorLists = new ArrayList<>();
    private ArrayList<VendorList> vendorLists1 = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RadioGroup radio_group;
    private String apiName = "inprogress-vendors";
    private boolean onboard = true;
    private String apiNameSearch = "inprogress-vendors";
    private boolean search = false;
    View view;
    CustomAdapter<String> spinnerArrayAdapter;
    List<Datum> searchList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> mobileList = new ArrayList<>();
    LinearLayout llNoData;
    SwipeRefreshLayout swMain;
    AutoCompleteTextView actv;
    private ImageView ivSearch;


    //
    Context mcontext;


    public VendorInProgressList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_onboarded_list, container, false);
        mcontext = this.getActivity();
        DashboardActivity.settitle("In Progress Vendors");

        Initialize();
        return view;
    }

    public void Initialize() {

        edit_search_vendor = view.findViewById(R.id.etSearchPlace);
        sessionManager = new SessionManager(getActivity());
        llNoData = view.findViewById(R.id.llNoData);


        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("please wait loading onboarded vendors...");

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        tvNoDataMsg = view.findViewById(R.id.tvNoDataMsg);
        ivSearch = view.findViewById(R.id.ivSearch);
        tvNoDataMsg.setText("No any in-progress vendors are available");
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        vendorAdapter = new VendorListInProgressAdapter(vendorLists, getActivity(), this);

        recyclerview.setAdapter(vendorAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == true) {
                    // this condition is for not getting Next URL from API
                    if (!apiNameSearch.equalsIgnoreCase("")) ;
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    if (!apiName.equalsIgnoreCase("")) {
                        new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }
        };
        recyclerview.addOnScrollListener(scrollListener);

        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        } catch (
                JSONException e) {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "inprogress-vendors";
        onboard = true;
        progressDialog.setMessage("Loading in progress. Please wait...");

        new GetVendorList(object.toString(), apiName).execute();


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            ApiCallgetReports();
        } else {
            Constants.ShowNoInternet(getActivity());
        }


        actv = (AutoCompleteTextView) view.findViewById(R.id.etSearchPlace);

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this condition is for not contain any keyword in ediitext
                if (s.length() != 0) {

                    if (s.length() > 3) {
                        String regexStr = "^[0-9]*$";

                        if (s.toString().matches(regexStr)) {
                            spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, mobileList);
                        } else {
                            spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, nameList);

                        }
                        actv.setAdapter(spinnerArrayAdapter);//setting the adapter data into the AutoCompleteTextView
                        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                try {
                                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


                                    if (spinnerArrayAdapter.getItem(position).toString() != null) {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    } else {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pageSearch = 1;
                                search = true;
                                // this condition is for radio button that which is selected so we can call that api
                                if (onboard == true) {           // with Search keywords we are calling this apis
                                    apiNameSearch = "inprogress-vendors";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "inprogress-vendors";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                }
                            }
                        });

                        actv.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                actv.showDropDown();
                                return false;
                            }
                        });
                    }


                } else {
                    // without using search keywords we are calling this because it have to be called everytime even there is no text
                    if (onboard == true) {
                        page1 = 1;
                        page = 1;
                        apiName = "inprogress-vendors";
                        ;
                        new GetVendorList(object.toString(), apiName).execute();
                    } else {
                        page = 1;
                        page1 = 1;
                        apiName = "inprogress-vendors";
                        //  new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv.showDropDown();
            }
        });


        actv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                }
            }
        });


      /*  edit_search_vendor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this condition is for not contain any keyword in ediitext
                if (s.length() != 0) {
                    try {
                        objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        objectSearch.put(Constants.PARAM_SEARCH, s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pageSearch = 1;
                    search = true;
                    // this condition is for radio button that which is selected so we can call that api
                    if (onboard == true) {           // with Search keywords we are calling this apis
                        apiNameSearch = "inprogress-vendors";
                        new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                    } else {
                        apiNameSearch = "inprogress-vendors";
                        new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                    }
                } else {
                    // without using search keywords we are calling this because it have to be called everytime even there is no text
                    if (onboard == true) {
                        page1 = 1;
                        page = 1;
                        apiName = "inprogress-vendors";
                        new GetVendorList(object.toString(), apiName).execute();
                    } else {
                        page = 1;
                        page1 = 1;
                        apiName = "inprogress-vendors";
                        //  new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                    objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pageSearch = 1;
                search = true;
                // this condition is for radio button that which is selected so we can call that api
                if (onboard == true) {           // with Search keywords we are calling this apis
                    apiNameSearch = "inprogress-vendors";
                    ;
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    apiNameSearch = "inprogress-vendors";
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                }
            }
        });


    }

    @Override
    public void onClick(View view, int position) {
//        Intent i = new Intent(getActivity(), TaskProccessDetailActivity.class);
//        i.putExtra(Constants.KEY_PROCESS_ID, vendorLists.get(position).getProccessId()+"");
//        startActivity(i);

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {
        for (int i = 0; i < vendorLists.size(); i++) {
            if (vendorLists.get(i).getProccessId() == position) {
                //edit_search_vendor.setText("");
                Intent i1 = new Intent(getActivity(), VendorDetailActivity.class);

                //  commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists.get(i));
                i1.putExtra("data", vendorLists.get(i));
                startActivity(i1);
                break;
            }
        }

    }


    public class GetVendorListSearch extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetVendorListSearch(String jsonReq, String apiURL) {
            this.jsonReq = jsonReq;
            this.apiURL = apiURL;

            Mylogger.getInstance().Logit("apiURL: ", apiURL);
            Mylogger.getInstance().Logit("jsonReq: ", jsonReq);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setCancelable(false);
            // if we are on 1st page of every new keyword it is mandatory to clear our list
            if (pageSearch == 1) {
                vendorLists.clear();
            }
        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes;
            try {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.MINUTES)
                        .readTimeout(1000, TimeUnit.MINUTES)
                        .writeTimeout(1000, TimeUnit.MINUTES)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonReq);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + apiURL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();
                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit(TAG + "1", myRes);
                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            Mylogger.getInstance().Logit(TAG, message);
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        Toast.makeText(mcontext, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(getActivity());
                        }
                    }
                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        if (!next_Url.equalsIgnoreCase("") && !next_Url.equalsIgnoreCase("null")) {
                            String[] separated = next_Url.split("api3/");
                            apiNameSearch = separated[1];

                        } else {
                            apiNameSearch = "";
                        }
                        //Log.e("URL", next_Url);
                        JSONArray result = jsonObject.getJSONArray("data");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject o = result.getJSONObject(i);

                            VendorList vendorList = gson.fromJson(o.toString(), VendorList.class);

                            Log.e("VENDOR", "" + vendorList.getBankDetailUpdationRequired());


                            VendorList vList = new VendorList();
                            vList.setId(vendorList.getId());
                            vList.setProccessId(vendorList.getProccessId());
                            //vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            // vList.setAadhaarName(vendorList.getAadhaarName());
                            // vList.setAadhaarNo(vendorList.getAadhaarNo());
                            //vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            //  vList.setLatitude(vendorList.getLatitude());
                            //vList.setLongitude(vendorList.getLongitude());
                            // vList.setPanName(vendorList.getPanName());
                            // vList.setPanNo(vendorList.getPanNo());
                            vList.setShopImage(vendorList.getShopImage());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileVerify(vendorList.getMobileVerify());
                            vList.setLocationVerify(vendorList.getLocationVerify());
                            vList.setAadhaarVerify(vendorList.getAadhaarVerify());
                            vList.setPanVerify(vendorList.getPanVerify());
                            vList.setChequeVerify(vendorList.getChequeVerify());
                            vList.setFillDetails(vendorList.getFillDetails());
                            vList.setUploadFiles(vendorList.getUploadFiles());
                            vList.setRateSendForApproval(vendorList.getRateSendForApproval());
                            vList.setGstdeclaration(vendorList.getGstdeclaration());
                            vList.setVendorSendForApproval(vendorList.getVendorSendForApproval());
                            vList.setBankDetailUpdationRequired(vendorList.getBankDetailUpdationRequired());

                            vList.setName(vendorList.getName());
                            // vList.setRate(vendorList.getRate());
                            vList.setAssessmentverify(vendorList.getAssessmentverify());
                            vList.setDeliveryBoy(vendorList.getDeliveryBoy());
                            vList.setNoc(vendorList.getNoc());
                            vList.setIsAgreementUpdationRequire(vendorList.getIsAgreementUpdationRequire());
                            // vList.setAllowedit(vendorList.getAllowedit());
                            // vList.setIsAddDeliveryBoy(vendorList.getIsAddDeliveryBoy());
                            vList.setAgreement(vendorList.getAgreement());
                            vList.setEcomAgreeement(vendorList.getEcomAgreeement());

                            vendorLists.add(vList);
                            vendorLists1.add(vList);
                            pageSearch = 2;
                        }

                        if (vendorLists.size() > 0) {
                            recyclerview.getRecycledViewPool().clear();
                            vendorAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            vendorAdapter.notifyDataSetChanged();
        }

    }


    public class GetVendorList extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetVendorList(String jsonReq, String apiURL) {
            this.jsonReq = jsonReq;
            this.apiURL = apiURL;

            Mylogger.getInstance().Logit("apiURL: ", apiURL);
            Mylogger.getInstance().Logit("jsonReq: ", jsonReq);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setCancelable(false);
            search = false;
            // if there will be page 1 so we have to clear our list sp this condition putted
            if (onboard == false && page == 1) {
                vendorLists.clear();
                vendorLists1.clear();
            }
            if (onboard == true && page1 == 1) {
                vendorLists.clear();
                vendorLists1.clear();
            }
        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes;
            try {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.MINUTES)
                        .readTimeout(1000, TimeUnit.MINUTES)
                        .writeTimeout(1000, TimeUnit.MINUTES)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonReq);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + apiURL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();
                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit(TAG + "1", myRes);
                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            Mylogger.getInstance().Logit(TAG, message);
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        Toast.makeText(mcontext, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(getActivity());
                        }
                    }

                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        // if next page URL will get as a "" so this condition will help
                        if (!next_Url.equalsIgnoreCase("") && !next_Url.equalsIgnoreCase("null")) {
                            String[] separated = next_Url.split("api3/");
                            apiName = separated[1];
                        } else {
                            // and if no so we have to stop pagination so we declared apiname as blak string
                            apiName = "";
                        }
                        Log.e("URL", next_Url);
                        JSONArray result = jsonObject.getJSONArray("data");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject o = result.getJSONObject(i);

                            VendorList vendorList = gson.fromJson(o.toString(), VendorList.class);

                            Log.e("VENDOR", "" + vendorList.getBankDetailUpdationRequired());


                            VendorList vList = new VendorList();
                            vList.setId(vendorList.getId());
                            vList.setProccessId(vendorList.getProccessId());
                            //vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            // vList.setAadhaarName(vendorList.getAadhaarName());
                            // vList.setAadhaarNo(vendorList.getAadhaarNo());
                            //vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            //  vList.setLatitude(vendorList.getLatitude());
                            //vList.setLongitude(vendorList.getLongitude());
                            // vList.setPanName(vendorList.getPanName());
                            // vList.setPanNo(vendorList.getPanNo());
                            vList.setShopImage(vendorList.getShopImage());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileVerify(vendorList.getMobileVerify());
                            vList.setLocationVerify(vendorList.getLocationVerify());
                            vList.setAadhaarVerify(vendorList.getAadhaarVerify());
                            vList.setPanVerify(vendorList.getPanVerify());
                            vList.setChequeVerify(vendorList.getChequeVerify());
                            vList.setFillDetails(vendorList.getFillDetails());
                            vList.setUploadFiles(vendorList.getUploadFiles());
                            vList.setRateSendForApproval(vendorList.getRateSendForApproval());
                            vList.setGstdeclaration(vendorList.getGstdeclaration());
                            vList.setVendorSendForApproval(vendorList.getVendorSendForApproval());
                            vList.setBankDetailUpdationRequired(vendorList.getBankDetailUpdationRequired());

                            vList.setName(vendorList.getName());
                            // vList.setRate(vendorList.getRate());
                            vList.setAssessmentverify(vendorList.getAssessmentverify());
                            vList.setDeliveryBoy(vendorList.getDeliveryBoy());
                            vList.setNoc(vendorList.getNoc());
                            vList.setIsAgreementUpdationRequire(vendorList.getIsAgreementUpdationRequire());
                            // vList.setAllowedit(vendorList.getAllowedit());
                            // vList.setIsAddDeliveryBoy(vendorList.getIsAddDeliveryBoy());
                            vList.setAgreement(vendorList.getAgreement());
                            vList.setEcomAgreeement(vendorList.getEcomAgreeement());
                            page = 0;
                            page1 = 0;
                            vendorLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well
                            vendorLists1.add(vList);
                        }
                        if (vendorLists.size() > 0) {
                            recyclerview.getRecycledViewPool().clear();
                            vendorAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }


                    }
                } catch (JSONException e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
            vendorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        page1 = 1;
        actv.getText().clear();
        apiName = "inprogress-vendors";
        new GetVendorList(object.toString(), apiName).execute();
    }


    public void sendOTP(final int vendorProcessId) {
        for (int i = 0; i < vendorLists1.size(); i++) {
            if (vendorLists1.get(i).getProccessId() == vendorProcessId) {
                edit_search_vendor.setText("");
                // commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists1.get(i));
                Intent i1 = new Intent(getActivity(), VendorDetailActivity.class);

                //  commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists.get(i));
                i1.putExtra("data", vendorLists1.get(i));
                startActivity(i1);
                break;

            }
        }
//        final ProgressDialog mProgressDialog = new ProgressDialog(mcontext);
//        mProgressDialog.setMessage("sending OTP to mobile...");
//        mProgressDialog.show();
//        JsonObject user = new JsonObject();
//        user.addProperty(Constants.PARAM_MOBILE_NO, vendorLists.get(position).getMobileNo());
//        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
//
//        Mylogger.getInstance().Logit(TAG, user.toString());
//        ApiClient.getClient().create(ApiInterface.class).sendOTP(user).enqueue(new Callback<GetOTP>() {
//            @Override
//            public void onResponse(Call<GetOTP> call, retrofit2.Response<GetOTP> response) {
//                mProgressDialog.dismiss();
//
//                if (response.isSuccessful()) {
//                    GetOTP getOTP = response.body();
//                    Mylogger.getInstance().Logit(TAG, getOTP.getResponse());
//                    if (getOTP.getResponseCode() == 200) {
//                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
//                        showDialogOTPConfirm(position, getOTP.getData().get(0).getOtp().toString());
//                    } else if (getOTP.getResponseCode() == 405) {
//                        sessionManager.logoutUser(mcontext);
//                    } else {
//                        Toast.makeText(mcontext, getOTP.getResponse(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(mcontext, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetOTP> call, Throwable t) {
//                mProgressDialog.dismiss();
//                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
//            }
//        });

    }


    public void commanFragmentCallWithBackStack(Fragment fragment, VendorList vendorList) {

        Fragment cFragment = fragment;

        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendorList);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.rvContentMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    private void ApiCallgetReports() {
        // mProgress.show();
        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_VENDOR_TYPE, "0");
        ApiClient.getClient().create(ApiInterface.class).getSearchListResponse("Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SearchListResponse>() {
            @Override
            public void onResponse(Call<SearchListResponse> call, retrofit2.Response<SearchListResponse> response) {
                if (response.isSuccessful()) {

                    //  mProgress.dismiss();
                    SearchListResponse supportListResponse = response.body();
                    if (supportListResponse.getResponseCode() == 200) {
                        Log.e("harshit", supportListResponse.getResponse());

                        searchList.addAll(supportListResponse.getData());

                        for (int i = 0; i < searchList.size(); i++) {

                            if (searchList.get(i).getStoreName() != null) {
                                nameList.add(searchList.get(i).getStoreName());
                            }
                            if (searchList.get(i).getMobileNo() != null) {
                                mobileList.add(searchList.get(i).getMobileNo());
                            }


                            //nameList.add(searchList.get(i).getMobileNo());
                            // nameList.add(searchList.get(i).getName());

                        }


                        spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, nameList);
                        actv.setAdapter(spinnerArrayAdapter);//setting the adapter data into the AutoCompleteTextView

                        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                try {
                                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

                                    if (spinnerArrayAdapter.getItem(position).toString() != null) {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    } else {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pageSearch = 1;
                                search = true;
                                // this condition is for radio button that which is selected so we can call that api
                                if (onboard == true) {           // with Search keywords we are calling this apis
                                    apiNameSearch = "inprogress-vendors";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "inprogress-vendors";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                }
                            }
                        });

                        actv.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                actv.showDropDown();
                                return false;
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), supportListResponse.getResponse(), Toast.LENGTH_LONG).show();

                        Log.e("harshit", supportListResponse.getResponse());
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2073 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SearchListResponse> call, Throwable t) {

                Toast.makeText(getActivity(), "#errorcode 2073 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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
                ((TextView) view).setTextSize(15);
                ((TextView) view).setPadding(15, 15, 0, 5);
                ((TextView) view).setTransformationMethod(null);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.montesemibold);

                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

}
