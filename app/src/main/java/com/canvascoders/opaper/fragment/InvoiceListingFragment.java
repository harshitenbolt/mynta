package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SearchListResponse.Datum;
import com.canvascoders.opaper.Beans.SearchListResponse.SearchListResponse;
import com.canvascoders.opaper.Beans.VendorInvoiceList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.InvoiceDetailsActivity;
import com.canvascoders.opaper.adapters.InvoiceListCommonAdapter;
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
public class InvoiceListingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "InvoiceList";
    View v;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerview;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    LinearLayout llNoData;
    JSONObject objectSearch = new JSONObject();
    ImageView ivSearch;
    JSONObject object = new JSONObject();
    private boolean search = false;
    private EndlessRecyclerViewScrollListener scrollListener;
    InvoiceListCommonAdapter invoiceListCommonAdapter;
    List<VendorInvoiceList> vendorLists = new ArrayList<>();
    private ArrayList<VendorInvoiceList> vendorLists1 = new ArrayList<>();
    private String apiName = "get-all-kirana-invoice";
    private String apiNameSearch = "get-all-kirana-invoice";
    AutoCompleteTextView actv;
    CustomAdapter<String> spinnerArrayAdapter;
    private boolean onboard = true;
    int page, page1, pageSearch = 1;
    List<String> nameList = new ArrayList<>();
    List<String> mobileList = new ArrayList<>();
    String[] options;
    String parameter = "";
    List<Datum> searchList = new ArrayList<>();

    public InvoiceListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_invoice_listing, container, false);
        DashboardActivity.settitle("Invoices");
        init();
        return v;
    }

    private void init() {


        DashboardActivity.settitle("Invoices");
        sessionManager = new SessionManager(getActivity());
        sessionManager = new SessionManager(getActivity());
        llNoData = v.findViewById(R.id.llNoData);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait loading Invoices ...");

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ivSearch = v.findViewById(R.id.ivSearch);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        invoiceListCommonAdapter = new InvoiceListCommonAdapter(vendorLists, getActivity(), InvoiceListingFragment.this);

        recyclerview.setAdapter(invoiceListCommonAdapter);
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallgetReports();
        } else {
            Constants.ShowNoInternet(getActivity());
        }


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

        apiName = "get-all-kirana-invoice";
        onboard = true;
        progressDialog.setMessage("please wait loading Invoices ...");

        new GetVendorList(object.toString(), apiName).execute();

        actv = (AutoCompleteTextView) v.findViewById(R.id.etSearchPlace);

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
                            parameter = "mobile_no";
                            spinnerArrayAdapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mobileList);
                        } else {
                            parameter = "store_name";
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


                                    if (spinnerArrayAdapter.getItem(position).toString() != null) {
                                        parameter = "store_name";
                                        objectSearch.put(parameter, actv.getText().toString());

                                    } else {
                                        parameter = "mobile_no";
                                        objectSearch.put(parameter, actv.getText().toString());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pageSearch = 1;
                                search = true;
                                // this condition is for radio button that which is selected so we can call that api
                                if (onboard == true) {           // with Search keywords we are calling this apis
                                    apiNameSearch = "get-all-kirana-invoice";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "get-all-kirana-invoice";
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
                        apiName = "get-all-kirana-invoice";
                        new GetVendorList(object.toString(), apiName).execute();
                    } else {
                        page = 1;
                        page1 = 1;
                        apiName = "get-all-kirana-invoice";
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
                    apiNameSearch = "get-all-kirana-invoice";
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    apiNameSearch = "get-all-kirana-invoice";
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                }
            }
        });


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
                Intent i1 = new Intent(getActivity(), InvoiceDetailsActivity.class);

                //  commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists.get(i));
                i1.putExtra("data", String.valueOf(vendorLists.get(i).getProccessId()));
                startActivity(i1);
                break;
            }
        }

    }


    private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("PAN Details");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), " You select >> " + options[i], Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

                            VendorInvoiceList vendorList = gson.fromJson(o.toString(), VendorInvoiceList.class);


                            VendorInvoiceList vList = new VendorInvoiceList();
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setBillPeriod(vendorList.getBillPeriod());
                            vList.setNoOfInvoice(vendorList.getNoOfInvoice());
                            vList.setStoreAddress(vendorList.getStoreAddress());
                            vList.setName(vendorList.getName());

                            vendorLists.add(vList);
                            vendorLists1.add(vList);
                            pageSearch = 2;
                        }
                        if (vendorLists.size() > 0) {
                            recyclerview.getRecycledViewPool().clear();
                            invoiceListCommonAdapter.notifyDataSetChanged();
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
            invoiceListCommonAdapter.notifyDataSetChanged();
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

                            VendorInvoiceList vendorList = gson.fromJson(o.toString(), VendorInvoiceList.class);


                            VendorInvoiceList vList = new VendorInvoiceList();
                            vList.setProccessId(vendorList.getProccessId());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setBillPeriod(vendorList.getBillPeriod());
                            vList.setNoOfInvoice(vendorList.getNoOfInvoice());
                            vList.setStoreAddress(vendorList.getStoreAddress());
                            vList.setName(vendorList.getName());

                            page = 0;
                            page1 = 0;
                            vendorLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well
                            vendorLists1.add(vList);
                        }
                        if (vendorLists.size() > 0) {
                            recyclerview.getRecycledViewPool().clear();
                            invoiceListCommonAdapter.notifyDataSetChanged();
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
            invoiceListCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        page1 = 1;
        actv.getText().clear();
        apiName = "get-all-kirana-invoice";
        parameter = "";
        new GetVendorList(object.toString(), apiName).execute();

    }


    public void sendOTP(final int vendorProcessId) {
        for (int i = 0; i < vendorLists1.size(); i++) {
            if (vendorLists1.get(i).getProccessId() == vendorProcessId) {
                //    edit_search_vendor.setText("");
                Intent i1 = new Intent(getActivity(), InvoiceDetailsActivity.class);

                //  commanFragmentCallWithBackStack(new VendorDetailsFragment(), vendorLists.get(i));
                i1.putExtra("data", String.valueOf(vendorLists.get(i).getProccessId()));
                startActivity(i1);
                break;
            }
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
        param.put(Constants.PARAM_VENDOR_TYPE, "1");
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


                        spinnerArrayAdapter = new CustomAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameList);
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
                                    apiNameSearch = "get-all-kirana-invoice";
                                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                                } else {
                                    apiNameSearch = "get-all-kirana-invoice";
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


  /*  class CustomAdapter<T> extends ArrayAdapter<T> {
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
*/

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
           /* View row = null;
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                row = inflater.inflate(R.layout.spinner_item_search, parent,
                        false);
            } else {
                row = convertView;
            }
            Datum detail = searchList.get(position);
            TextView name = (TextView) row.findViewById(R.id.tv1);
            name.setText(detail.getName());
            name.setText(detail.getMobileNo());
           *//* TextView email = (TextView) row.findViewById(R.id.tvClientEmail);
            email.setText(detail.email);
            TextView id = (TextView) row.findViewById(R.id.tvClientID);
            id.setText("ID : " + detail.id);
            TextView company = (TextView) row
                    .findViewById(R.id.tvClientCompanyName);
            company.setText(detail.company);
            TextView status = (TextView) row.findViewById(R.id.tvClientStatus);
            status.setText("Status:" + detail.status);*//*
            return row;*/

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
        }
    }
}