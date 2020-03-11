package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.DeliveryBoyList;
import com.canvascoders.opaper.Beans.SearchAssessmentDeliveryBoy.SearchResponseAssessment;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.AssessmentDetaildeliveryBoyActivity;
import com.canvascoders.opaper.adapters.DeliveryBoysAssessmentListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class KiranaAsssessmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener {
    View v;
    private String support_id;
    View view;
    SessionManager sessionManager;
    RecyclerView rvOnboardingSupport;
    Context mContext;

    //String next_page_url = "support-listing", nextPageUrl1 = "support-listing";
    DeliveryBoysAssessmentListAdapter supportListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    List<DeliveryBoyList> supportList = new ArrayList<>();
    List<DeliveryBoyList> supportList1 = new ArrayList<>();
    ProgressDialog mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout llNoData;
    List<String> nameList = new ArrayList<>();
    List<String> mobileList = new ArrayList<>();
    // Spinner snDocType;
    String status = "";
    private String apiName = "vendor-assessment-list";
    private boolean onboard = true;
    private String apiNameSearch = "vendor-assessment-list";
    JSONObject objectSearch = new JSONObject();
    private ImageView ivSearch;
    AutoCompleteTextView actv;
    JSONObject object = new JSONObject();
    int page, page1, pageSearch = 1;
    boolean onRefresh = false;
    private boolean search = false;
    private List<String> docTypeList = new ArrayList<>();
    List<com.canvascoders.opaper.Beans.SearchAssessmentDeliveryBoy.Datum> searchList = new ArrayList<>();
    CustomAdapter<String> spinnerArrayAdapter;
    private ProgressDialog progressDialog;
    private List<DeliveryBoyList> DeliveryBoyLists = new ArrayList<>();

    private List<DeliveryBoyList> DeliveryBoyLists1 = new ArrayList<>();
    String TAG = "VendorLis";


    public KiranaAsssessmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kirana_asssessment, container, false);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }

    private void init() {
        mProgress = new ProgressDialog(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ivSearch = view.findViewById(R.id.ivSearch);
        llNoData = view.findViewById(R.id.llNoData);
        rvOnboardingSupport = view.findViewById(R.id.rvOnboarding);
        supportListAdapter = new DeliveryBoysAssessmentListAdapter(DeliveryBoyLists, getActivity(), KiranaAsssessmentFragment.this);
        rvOnboardingSupport.setAdapter(supportListAdapter);


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports();
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

                if (search == true) {
                    // this condition is for not getting Next URL from API
                    if (!apiNameSearch.equalsIgnoreCase("")) ;
                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    if (!apiName.equalsIgnoreCase("")) {
                        new GetDeliveryBoyList(object.toString(), apiName).execute();
                    }
                }
            }
        };


        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            object.put(Constants.PARAM_STATUS, "1");
        } catch (
                JSONException e) {

        }
        rvOnboardingSupport.addOnScrollListener(scrollListener);


        //  snDocType = (Spinner) view.findViewById(R.id.snFilterType);
        docTypeList = Arrays.asList(getResources().getStringArray(R.array.FilterType));
        CustomAdapter<String> doctypeadapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, docTypeList);
        doctypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        apiName = "vendor-assessment-list";
        onboard = true;
        progressDialog.setMessage("please wait ...");

        new GetDeliveryBoyList(object.toString(), apiName).execute();


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
                            spinnerArrayAdapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mobileList);
                        } else {
                            spinnerArrayAdapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameList);

                        }
                        actv.setAdapter(spinnerArrayAdapter);//setting the adapter data into the AutoCompleteTextView
                        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                try {
                                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                                    objectSearch.put(Constants.PARAM_STATUS, "1");

                                    if (spinnerArrayAdapter.getItem(position).toString() != null) {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    } else {
                                        objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //   pageSearch = 1;
                                search = true;
                                // this condition is for radio button that which is selected so we can call that api
                                if (onboard == true) {           // with Search keywords we are calling this apis
                                    apiNameSearch = "vendor-assessment-list";
                                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "vendor-assessment-list";
                                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
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
                        apiName = "vendor-assessment-list";
                        new GetDeliveryBoyList(object.toString(), apiName).execute();
                    } else {
                        page = 1;
                        page1 = 1;
                        apiName = "vendor-assessment-list";
                        //  new GetDeliveryBoyList(object.toString(), apiName).execute();
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


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                    objectSearch.put(Constants.PARAM_STATUS, "1");
                    objectSearch.put(Constants.PARAM_SEARCH, actv.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pageSearch = 1;
                search = true;
                // this condition is for radio button that which is selected so we can call that api
                if (onboard == true) {           // with Search keywords we are calling this apis
                    apiNameSearch = "vendor-assessment-list";
                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    apiNameSearch = "vendor-assessment-list";
                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
                }
            }
        });

    }


    @Override
    public void onRefresh() {
        page1 = 1;
        actv.getText().clear();
        apiName = "vendor-assessment-list";
        new GetDeliveryBoyList(object.toString(), apiName).execute();

    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position,String data) {

    }

    @Override
    public void SingleClick(String popup, int position) {
        for (int i = 0; i < DeliveryBoyLists.size(); i++) {
            if (DeliveryBoyLists.get(i).getId() == position) {
                //edit_search_vendor.setText("");
                Intent i1 = new Intent(getActivity(), AssessmentDetaildeliveryBoyActivity.class);
                //  commanFragmentCallWithBackStack(new VendorDetailsFragment(), DeliveryBoyLists.get(i));
                i1.putExtra("data", DeliveryBoyLists.get(i).getId());
                i1.putExtra("flag", "2");
                startActivity(i1);
                break;
            }
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


    private void ApiCallgetReports() {
        // mProgress.show();
        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_VENDOR_TYPE, "1");
        ApiClient.getClient().create(ApiInterface.class).getSearchListDeliveryBoy("Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SearchResponseAssessment>() {
            @Override
            public void onResponse(Call<SearchResponseAssessment> call, retrofit2.Response<SearchResponseAssessment> response) {
                if (response.isSuccessful()) {

                    //  mProgress.dismiss();
                    SearchResponseAssessment supportListResponse = response.body();
                    if (supportListResponse.getResponseCode() == 200) {
                        Log.e("harshit", supportListResponse.getResponse());

                        searchList.addAll(supportListResponse.getData());

                        for (int i = 0; i < searchList.size(); i++) {

                            if (searchList.get(i).getStoreName() != null) {
                                nameList.add(searchList.get(i).getStoreName());
                            }
                            if (searchList.get(i).getPhoneNumber() != null) {
                                mobileList.add(searchList.get(i).getPhoneNumber());
                            }


                            //nameList.add(searchList.get(i).getMobileNo());
                            // nameList.add(searchList.get(i).getName());

                        }


                        spinnerArrayAdapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameList);
                        actv.setAdapter(spinnerArrayAdapter);//setting the adapter data into the AutoCompleteTextView

                        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                try {
                                    objectSearch.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                                    objectSearch.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                                    // objectSearch.put(Constants.PARAM_STATUS, "1");

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
                                    apiNameSearch = "vendor-assessment-list";
                                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "vendor-assessment-list";
                                    new GetDeliveryBoyListSearch(objectSearch.toString(), apiNameSearch).execute();
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

                        //  Log.e("harshit", supportListResponse.getResponse());
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2074 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SearchResponseAssessment> call, Throwable t) {

                Toast.makeText(getActivity(), "#errorcode 2074 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public class GetDeliveryBoyListSearch extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetDeliveryBoyListSearch(String jsonReq, String apiURL) {
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
                DeliveryBoyLists.clear();
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
                        Toast.makeText(getActivity(), jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
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

                            DeliveryBoyList DeliveryBoyList = gson.fromJson(o.toString(), DeliveryBoyList.class);

                            Log.e("VENDOR", "" + DeliveryBoyList.getId());

                            DeliveryBoyList vList = new DeliveryBoyList();
                            vList.setId(DeliveryBoyList.getId());
                            vList.setProccessId(DeliveryBoyList.getProccessId());
                            vList.setAgentId(DeliveryBoyList.getAgentId());
                            vList.setPhoneNumber(DeliveryBoyList.getPhoneNumber());
                            vList.setImage(DeliveryBoyList.getImage());
                            vList.setName(DeliveryBoyList.getName());
                            vList.setFatherName(DeliveryBoyList.getFatherName());
                            vList.setAssessmentVerify(DeliveryBoyList.getAssessmentVerify());
                            vList.setStoreName(DeliveryBoyList.getStoreName());
                            vList.setAssessmentTried(DeliveryBoyList.getAssessmentTried());
                            DeliveryBoyLists.add(vList);
                            DeliveryBoyLists1.add(vList);
                            pageSearch = 2;
                        }
                        if (DeliveryBoyLists.size() > 0) {
                            rvOnboardingSupport.getRecycledViewPool().clear();
                            supportListAdapter.notifyDataSetChanged();
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
            supportListAdapter.notifyDataSetChanged();
        }

    }


    public class GetDeliveryBoyList extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetDeliveryBoyList(String jsonReq, String apiURL) {
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
                DeliveryBoyLists.clear();
                DeliveryBoyLists1.clear();
            }
            if (onboard == true && page1 == 1) {
                DeliveryBoyLists.clear();
                DeliveryBoyLists1.clear();
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
                        Toast.makeText(getActivity(), jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
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

                            DeliveryBoyList DeliveryBoyList = gson.fromJson(o.toString(), DeliveryBoyList.class);

                            Log.e("VENDOR", "" + DeliveryBoyList.getId());

                            DeliveryBoyList vList = new DeliveryBoyList();
                            vList.setId(DeliveryBoyList.getId());
                            vList.setAgentId(DeliveryBoyList.getAgentId());
                            vList.setPhoneNumber(DeliveryBoyList.getPhoneNumber());
                            vList.setImage(DeliveryBoyList.getImage());
                            vList.setProccessId(DeliveryBoyList.getProccessId());
                            vList.setName(DeliveryBoyList.getName());
                            vList.setFatherName(DeliveryBoyList.getFatherName());
                            vList.setAssessmentVerify(DeliveryBoyList.getAssessmentVerify());
                            vList.setStoreName(DeliveryBoyList.getStoreName());
                            vList.setAssessmentTried(DeliveryBoyList.getAssessmentTried());

                            page = 0;
                            page1 = 0;
                            DeliveryBoyLists.add(vList);
                            // we have to add data in  new DeliveryBoyList too to manage data well
                            DeliveryBoyLists1.add(vList);
                        }
                        if (DeliveryBoyLists.size() > 0) {
                            rvOnboardingSupport.getRecycledViewPool().clear();
                            supportListAdapter.notifyDataSetChanged();
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
            supportListAdapter.notifyDataSetChanged();
        }
    }

}
