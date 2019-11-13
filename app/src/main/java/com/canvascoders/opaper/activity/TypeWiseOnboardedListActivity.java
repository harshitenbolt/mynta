package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.SearchListResponse.SearchListResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.VendorListOnboardedAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

public class TypeWiseOnboardedListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {

    SessionManager sessionManager;
    LinearLayout llNoData;
    ProgressDialog progressDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    VendorListOnboardedAdapter vendorAdapter;
    private ArrayList<VendorList> vendorLists = new ArrayList<>();
    private ArrayList<VendorList> vendorLists1 = new ArrayList<>();
    private EndlessRecyclerViewScrollListener scrollListener;
    private boolean search = false;
    String TAG = "VendorLis";
    private String apiName = "completed-vendors";
    private boolean onboard = true;
    int page, page1, pageSearch = 1;
    private String apiNameSearch = "completed-vendors";
    JSONObject object = new JSONObject();
    TextView tvTitle;
    private List<String> storeTypeList = new ArrayList<>();
    private Spinner snDocType;
    ImageView ivBack;
    JSONObject objectSearch = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_wise_onboarded_list);
        Initialize();
    }

    private void Initialize() {

        //edit_search_vendor = view.findViewById(R.id.etSearchPlace);
        sessionManager = new SessionManager(this);
        llNoData = findViewById(R.id.llNoData);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait loading onboarded vendors...");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        vendorAdapter = new VendorListOnboardedAdapter(vendorLists, this, this);

        recyclerView.setAdapter(vendorAdapter);


        snDocType = (Spinner) findViewById(R.id.snDocType);
        storeTypeList = Arrays.asList(getResources().getStringArray(R.array.DocType));

        CustomAdapter<String> doctypeadapter = new CustomAdapter<String>(TypeWiseOnboardedListActivity.this, android.R.layout.simple_spinner_item, storeTypeList);
        doctypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snDocType.setAdapter(doctypeadapter);


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
        recyclerView.addOnScrollListener(scrollListener);

        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            object.put(Constants.PARAM_IS_EXPIRED, "1");
        } catch (
                JSONException e) {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "completed-vendors";
        onboard = true;
        progressDialog.setMessage("please wait loading onboarded vendors...");

        // new GetVendorList(object.toString(), apiName).execute();


        snDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Expired Vendors")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_IS_EXPIRED, "1");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("Expired Vendors");
                    new GetVendorList(object.toString(), apiName).execute();
                }
                if (selectedItem.equals("Upcoming Expire")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_EXPIRED_SOON, "1");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("Upcoming Expire");
                    new GetVendorList(object.toString(), apiName).execute();
                }
                if (selectedItem.equals("in-progress")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_STATUS, "0");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("In Progress Vendors");
                    new GetVendorList(object.toString(), apiName).execute();
                }
                if (selectedItem.equals("Active")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_STATUS, "1");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("Active Vendors");
                    new GetVendorList(object.toString(), apiName).execute();
                }
                if (selectedItem.equals("Deactivated/Closed")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_STATUS, "2");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("Deactivated/Closed");
                    new GetVendorList(object.toString(), apiName).execute();
                }
                if (selectedItem.equals("Payment Hold")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_STATUS, "3");
                    } catch (JSONException e) {
                    }
                    new GetVendorList(object.toString(), apiName).execute();
                    tvTitle.setText("Payment Hold");
                }
                if (selectedItem.equals("Black Listed")) {
                    object = new JSONObject();
                    page1 = 1;
                    apiName = "completed-vendors";
                    try {
                        object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                        object.put(Constants.PARAM_STATUS, "4");
                    } catch (JSONException e) {
                    }
                    tvTitle.setText("Black Listed");
                    new GetVendorList(object.toString(), apiName).execute();
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onRefresh() {
        page1 = 1;

        apiName = "completed-vendors";
        new GetVendorList(object.toString(), apiName).execute();

    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {
        for (int i = 0; i < vendorLists.size(); i++) {
            if (vendorLists.get(i).getProccessId() == position) {
                //edit_search_vendor.setText("");
                Intent i1 = new Intent(TypeWiseOnboardedListActivity.this, VendorDetailActivity.class);

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
                        Toast.makeText(TypeWiseOnboardedListActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(TypeWiseOnboardedListActivity.this);
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
                            vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setAadhaarName(vendorList.getAadhaarName());
                            vList.setAadhaarNo(vendorList.getAadhaarNo());
                            vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            vList.setLatitude(vendorList.getLatitude());
                            vList.setLongitude(vendorList.getLongitude());
                            vList.setPanName(vendorList.getPanName());
                            vList.setStoreAddress1(vendorList.getStoreAddress1());
                            vList.setStoreAddressLandmark(vendorList.getStoreAddressLandmark());
                            vList.setCity(vendorList.getCity());
                            vList.setState(vendorList.getState());
                            vList.setPincode(vendorList.getPincode());
                            vList.setPanNo(vendorList.getPanNo());
                            vList.setStatus(vendorList.getStatus());
                            vList.setShopImage(vendorList.getShopImage());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileVerify(vendorList.getMobileVerify());
                            vList.setExpirationDate(vendorList.getExpirationDate());
                            vList.setLocationVerify(vendorList.getLocationVerify());
                            vList.setAadhaarVerify(vendorList.getAadhaarVerify());
                            vList.setPanVerify(vendorList.getPanVerify());
                            vList.setChequeVerify(vendorList.getChequeVerify());
                            vList.setChequeVerify(vendorList.getChequeVerify());
                            vList.setFillDetails(vendorList.getFillDetails());
                            vList.setUploadFiles(vendorList.getUploadFiles());
                            vList.setRateSendForApproval(vendorList.getRateSendForApproval());
                            vList.setGstdeclaration(vendorList.getGstdeclaration());
                            vList.setGstn(vendorList.getGstn());
                            vList.setVendorSendForApproval(vendorList.getVendorSendForApproval());
                            vList.setBankDetailUpdationRequired(vendorList.getBankDetailUpdationRequired());
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setName(vendorList.getName());
                            vList.setRate(vendorList.getRate());
                            vList.setNoc(vendorList.getNoc());
                            vList.setIsAgreementUpdationRequire(vendorList.getIsAgreementUpdationRequire());
                            vList.setAllowedit(vendorList.getAllowedit());
                            vList.setIsAddDeliveryBoy(vendorList.getIsAddDeliveryBoy());
                            vList.setAgreement(vendorList.getAgreement());
                            vList.setStoreAddress(vendorList.getStoreAddress());
                            vList.setAssessmentverify(vendorList.getAssessmentverify());
                            vList.setIsUpdateGst(vendorList.getIsUpdateGst());
                            vList.setStoreType(vendorList.getStoreType());

                            vendorLists.add(vList);
                            vendorLists1.add(vList);
                            pageSearch = 2;
                        }
                        if (vendorLists.size() > 0) {
                            recyclerView.getRecycledViewPool().clear();
                            vendorAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
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
                        Toast.makeText(TypeWiseOnboardedListActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(TypeWiseOnboardedListActivity.this);
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
                            vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setAadhaarName(vendorList.getAadhaarName());
                            vList.setAadhaarNo(vendorList.getAadhaarNo());
                            vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            vList.setLatitude(vendorList.getLatitude());
                            vList.setLongitude(vendorList.getLongitude());
                            vList.setPanName(vendorList.getPanName());
                            vList.setStoreAddress1(vendorList.getStoreAddress1());
                            vList.setStoreAddressLandmark(vendorList.getStoreAddressLandmark());


                            vList.setCity(vendorList.getCity());
                            vList.setState(vendorList.getState());
                            vList.setPincode(vendorList.getPincode());

                            vList.setStatus(vendorList.getStatus());
                            vList.setPanNo(vendorList.getPanNo());
                            vList.setShopImage(vendorList.getShopImage());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileVerify(vendorList.getMobileVerify());
                            vList.setLocationVerify(vendorList.getLocationVerify());
                            vList.setAadhaarVerify(vendorList.getAadhaarVerify());
                            vList.setPanVerify(vendorList.getPanVerify());
                            vList.setChequeVerify(vendorList.getChequeVerify());
                            vList.setExpirationDate(vendorList.getExpirationDate());
                            vList.setGstn(vendorList.getGstn());
                            vList.setFillDetails(vendorList.getFillDetails());
                            vList.setUploadFiles(vendorList.getUploadFiles());
                            vList.setRateSendForApproval(vendorList.getRateSendForApproval());
                            vList.setGstdeclaration(vendorList.getGstdeclaration());
                            vList.setVendorSendForApproval(vendorList.getVendorSendForApproval());
                            vList.setBankDetailUpdationRequired(vendorList.getBankDetailUpdationRequired());
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setName(vendorList.getName());
                            vList.setRate(vendorList.getRate());
                            vList.setAssessmentverify(vendorList.getAssessmentverify());
                            vList.setIsUpdateGst(vendorList.getIsUpdateGst());
                            vList.setStoreType(vendorList.getStoreType());
                            vList.setNoc(vendorList.getNoc());
                            vList.setStoreAddress(vendorList.getStoreAddress());
                            vList.setIsAgreementUpdationRequire(vendorList.getIsAgreementUpdationRequire());
                            vList.setAllowedit(vendorList.getAllowedit());
                            vList.setIsAddDeliveryBoy(vendorList.getIsAddDeliveryBoy());
                            vList.setAgreement(vendorList.getAgreement());
                            page = 0;
                            page1 = 0;
                            vendorLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well
                            vendorLists1.add(vList);
                        }
                        if (vendorLists.size() > 0) {
                            recyclerView.getRecycledViewPool().clear();
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
