package com.canvascoders.opaper.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.adapters.VendorAdapter;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.Beans.VendorList;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VenderListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    AppCompatRadioButton radio_onboard;
    AppCompatRadioButton radio_inprogress;
    RecyclerView recyclerview;
    VendorAdapter vendorAdapter;
    String TAG = "VendorLis";
    SessionManager sessionManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONObject object = new JSONObject();
    JSONObject objectSearch = new JSONObject();
    ImageView iv_clear_text;
    EditText edit_search_vendor;
    int page, page1, pageSearch = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<VendorList> vendorLists = new ArrayList<>();
    private ArrayList<VendorList> vendorLists1 = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RadioGroup radio_group;
    private String apiName = "completed-vendors";
    private boolean onboard = true;
    private String apiNameSearch = "completed-vendors";
    private boolean search = false;
    View view;

    Context mcontext;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_vender_list, container, false);

        mcontext = this.getActivity();

        DashboardActivity.settitle(Constants.TITLE_VENDOR_LIST);

        Initialize();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            DashboardActivity.settitle(Constants.TITLE_VENDOR_LIST);
        }
    }

    public void Initialize() {

        edit_search_vendor = view.findViewById(R.id.edt_search_text);
        iv_clear_text = view.findViewById(R.id.iv_clear_text);

        radio_group = (RadioGroup) view.findViewById(R.id.radio_group);
        sessionManager = new SessionManager(mcontext);

        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("please wait loading onboarded vendors...");
        radio_onboard = (AppCompatRadioButton) view.findViewById(R.id.radio_onboard);
        radio_inprogress = (AppCompatRadioButton) view.findViewById(R.id.radio_onboard);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        vendorAdapter = new VendorAdapter(VenderListFragment.this, vendorLists);
        vendorAdapter.setInWhichScreen(1);
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

        try

        {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        } catch (
                JSONException e)

        {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "completed-vendors";
        onboard = true;
        progressDialog.setMessage("please wait loading onboarded vendors...");
        vendorAdapter.setInWhichScreen(1);
        new GetVendorList(object.toString(), apiName).execute();



        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) view.findViewById(checkedId);

                if (rb.getId() == R.id.radio_onboard) {
                    apiName = "completed-vendors";
                    apiNameSearch = "completed-vendors";
                    onboard = true;
                    page1 = 1;
                    progressDialog.setMessage("please wait loading onboarded vendors...");
                    vendorAdapter.setInWhichScreen(1);
                    edit_search_vendor.setText("");
                    new GetVendorList(object.toString(), apiName).execute();

                } else if (rb.getId() == R.id.radio_inprogress) {
                    apiName = "inprogress-vendors";
                    apiNameSearch = "inprogress-vendors";
                    page = 1;
                    onboard = false;
                    progressDialog.setMessage("please wait loading in-progress vendors...");
                        vendorAdapter.setInWhichScreen(2);
                    edit_search_vendor.setText("");
                    new GetVendorList(object.toString(), apiName).execute();
                }
            }
        });

        edit_search_vendor.addTextChangedListener(new TextWatcher() {
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
                                                                          apiNameSearch = "completed-vendors";
                                                                          new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                                                      } else {
                                                                          apiNameSearch = "inprogress-vendors";
                                                                          new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                                                      }
                                                                  } else {
                                                                      // without using search keywords we are calling this because it have to be called everytime even there is no text
                                                                      if (onboard == true) {
                                                                          page1 = 1;
                                                                          page= 1;
                                                                          apiName = "completed-vendors";
                                                                        // new GetVendorList(object.toString(), apiName).execute();
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

        iv_clear_text.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                edit_search_vendor.setText("");
                if (onboard == true) {
                    page1 = 1;
                    apiName = "completed-vendors";
                    new GetVendorList(object.toString(), apiName).execute();
                } else {
                    page = 1;
                    apiName = "inprogress-vendors";
                    new GetVendorList(object.toString(), apiName).execute();
                }

            }
        });


    }


    public void sendOTP(final int vendorProcessId) {
        for (int i = 0; i < vendorLists1.size(); i++) {
            if (vendorLists1.get(i).getProccessId() == vendorProcessId) {
                edit_search_vendor.setText("");
                commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists1.get(i));
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

    private void showDialogOTPConfirm(final int position, final String otp) {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(R.layout.dialog_confirmotp);
        dialog.show();

        final EditText etOtp = dialog.findViewById(R.id.etOtp);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tvOk = dialog.findViewById(R.id.tvOk);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp.equalsIgnoreCase(etOtp.getText().toString())) {
                    dialog.dismiss();
                    commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists.get(position));
                } else {
                    Toast.makeText(mcontext, "Invalid OTP ", Toast.LENGTH_LONG).show();
                    etOtp.setText("");
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        // if the radio button is on onboard we ll call as page 1 and call api
        if (onboard == true) {
            page1 = 1;
            edit_search_vendor.setText("");
            apiName = "completed-vendors";
            new GetVendorList(object.toString(), apiName).execute();
        } else {
            page = 1;
            edit_search_vendor.setText("");
            apiName = "inprogress-vendors";
            new GetVendorList(object.toString(), apiName).execute();
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
                    if(jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(getActivity());
                        }
                    }

                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        // if next page URL will get as a "" so this condition will help
                        if (!next_Url.equalsIgnoreCase("")&& !next_Url.equalsIgnoreCase("null")) {
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
                            vList.setLatitude(vendorList.getLatitude());
                            vList.setLongitude(vendorList.getLongitude());
                            vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            vList.setPanName(vendorList.getPanName());

                            vList.setPanNo(vendorList.getPanNo());
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
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setName(vendorList.getName());
                            vList.setRate(vendorList.getRate());
                            vList.setNoc(vendorList.getNoc());
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
                        recyclerview.getRecycledViewPool().clear();
                        vendorAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e)

                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
            vendorAdapter.notifyDataSetChanged();
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
            progressDialog.setCancelable(false  );
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
                    if(jsonObject.has("responseCode")) {
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
                            vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setAadhaarName(vendorList.getAadhaarName());
                            vList.setLatitude(vendorList.getLatitude());
                            vList.setLongitude(vendorList.getLongitude());
                            vList.setAadhaarNo(vendorList.getAadhaarNo());
                            vList.setAadhaarPincode(vendorList.getAadhaarPincode());
                            vList.setLatitude(vendorList.getLatitude());
                            vList.setLongitude(vendorList.getLongitude());
                            vList.setPanName(vendorList.getPanName());
                            vList.setPanNo(vendorList.getPanNo());
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
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setName(vendorList.getName());
                            vList.setRate(vendorList.getRate());
                            vList.setNoc(vendorList.getNoc());
                            vList.setIsAgreementUpdationRequire(vendorList.getIsAgreementUpdationRequire());
                            vList.setAllowedit(vendorList.getAllowedit());
                            vList.setIsAddDeliveryBoy(vendorList.getIsAddDeliveryBoy());
                            vList.setAgreement(vendorList.getAgreement());

                            vendorLists.add(vList);
                            vendorLists1.add(vList);
                            pageSearch = 2;
                        }
                        recyclerview.getRecycledViewPool().clear();
                        vendorAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e)

                {
                    e.printStackTrace();
                }
            }
            vendorAdapter.notifyDataSetChanged();
        }

    }

    public void commanFragmentCallWithBackStack(Fragment fragment, VendorList vendorList) {

        Fragment cFragment = fragment;

        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendorList);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
}
