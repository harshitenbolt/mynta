package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.BillList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.InvoiceEsignActivity;
import com.canvascoders.opaper.adapters.BillsAdapter;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.helper.RecyclerViewTouchListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitInvoiceMainFragment extends Fragment {

    SessionManager sessionManager;
    String TAG = "InvoiceMain";
    private EndlessRecyclerViewScrollListener scrollListener;
    JSONObject object = new JSONObject();
    private BillsAdapter linearAdapter;
    private NetworkConnectivity networkConnectivity;
    private ArrayList<BillList> billLists = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerview;
    private RadioGroup rg_vendor_list;
    private String apiName = "get-invoice1";
    private String type_name="debit";
    Context mcontext;
    View view;
    String invoice_pdf = "";
    JSONObject jsonObject = new JSONObject();
    String status;
    int signed,page = 0;
    WebView webView;
    public DebitInvoiceMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_debit_invoice_main, container, false);

        mcontext = this.getActivity();

        DashboardActivity.settitle(Constants.TITLE_DEBIT_INVOICE_LIST);

        intView();
        webView = view.findViewById(R.id.wvDebitItem);
        rg_vendor_list = (RadioGroup) view.findViewById(R.id.activity_course_rg);
        sessionManager = new SessionManager(mcontext);
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("please wait loading tax invoice...");


        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            object.put(Constants.PARAMS_INVOICE_TYPE, "debit");
        } catch (JSONException e) {
        }


        rg_vendor_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb_vendor_list = (RadioButton) view.findViewById(checkedId);

                if (rb_vendor_list.getId() == R.id.rg_pending_vendorlist) {
                    signed = 0;
                    page = 1;
                    apiName = "get-invoice";
                    progressDialog.setMessage("please wait loading Pending vendor signature...");
                    new GetInvoice1(object.toString(), apiName).execute();

                } else if (rb_vendor_list.getId() == R.id.rg_signatured_vendorlist) {
                    signed = 1;
                    page = 1;
                    apiName = "get-esign-invoice";
                    progressDialog.setTitle("please wait loading Invoice signed by vendor...");

                    new GetInvoice1(object.toString(), apiName).execute();
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        if (!networkConnectivity.isNetworkAvailable()) {

            // Display message in dialog box if you have not internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder.setMessage("You are offline,Please check your internet connection,then try again");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    getActivity().finish();
                    arg0.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {
            webView.setVisibility(View.GONE);
            page=1;
            apiName = "get-invoice";
            progressDialog.setMessage("please wait loading Pending vendor signature...");
            new GetInvoice1(object.toString(), apiName).execute();
        }

    }



    private void intView() {
        networkConnectivity = new NetworkConnectivity(mcontext);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearAdapter = new BillsAdapter(billLists);
        recyclerview.setAdapter(linearAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(!apiName.equalsIgnoreCase(""))
                new GetInvoice1(object.toString(), apiName).execute();
            }
        };
        recyclerview.addOnScrollListener(scrollListener);


        recyclerview.addOnItemTouchListener(new RecyclerViewTouchListener(mcontext, recyclerview, new RecyclerViewClickListener() {


            @Override
            public void onClick(View view, final int position) {

//                commanFragmentCallWithBackStack(new InvoiceEsignActivity(), String.valueOf(billLists.get(position).getId()));
                if(signed == 0) {
                    Intent intent = new Intent(getActivity(), InvoiceEsignActivity.class);
                    intent.putExtra(Constants.KEY_INVOICE_ID, String.valueOf(billLists.get(position).getId()));
                    intent.putExtra(Constants.INVOICE_TYPE, type_name);
                    intent.putExtra(Constants.INVOICE_NUMBER, status);
                    intent.putExtra(Constants.SIGNED, signed);
                    startActivity(intent);
                }
                else{
                    try {
                        jsonObject.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                        jsonObject.put(Constants.PARAMS_INVOICE_TYPE, type_name);
                        jsonObject.put(Constants.PARAM_INVOICE_ID, String.valueOf(billLists.get(position).getId()));
                        // jsonObject.put(Constants.PARAMS_INVOICE_TYPE, getIntent().getStringExtra(Constants.PARAMS_INVOICE_TYPE));
                        Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
                        Mylogger.getInstance().Logit(TAG, jsonObject.toString());

                        new GetPDFfromServer(jsonObject.toString()).execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onLongClick(View view, final int position) {

            }
        }

        ));

    }


    public class GetPDFfromServer extends AsyncTask<String, Void, String> {
        String myRes;
        String s = "";

        public GetPDFfromServer(String s) {
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, s);
                Request requestLogin = null;
                if(signed == 0){
                    requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-invoice")
                            .post(body)
                            .addHeader("Authorization","Bearer "+sessionManager.getToken())
                            .build();
                }
                else{
                    requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-signed-invoice")
                            .post(body)
                            .addHeader("Authorization","Bearer "+sessionManager.getToken())
                            .build();
                }


                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit(TAG + "1", myRes);


                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {

            Mylogger.getInstance().Logit(TAG, message);
            progressDialog.dismiss();
            try {

                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {


                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                       /* if (result.has("uid")) {
                            uid = result.getString("uid").toString();
                        }*/

                       /* enbolt_id = result.getString("enbolt_id").toString();
                        mobile_no = result.getString("mobile_no").toString();*/
                        invoice_pdf = result.getString("invoice_pdf").toString();
                        //  mWebView.getSettings().setJavaScriptEnabled(true);
                        String pdf = invoice_pdf;
                        //mWebView.loadUrl( pdf);
                        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                        startActivity(browserIntent);*/
                        webView.setVisibility(View.VISIBLE);
                        webView.getSettings().setJavaScriptEnabled(true);
                     //   String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
                        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
                       /* mWebView.loadData(result.getString("invoice_html"), "text/html; charset=utf-8", "UTF-8");
                        if (status.equals("1")) {

                            btn_agree.setVisibility(View.GONE);
                        }*/
                    } else {

                        Toast.makeText(getActivity(), jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }



    public class GetInvoice1 extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apinumber;


        public GetInvoice1(String jsonReq, String apinumber) {
            this.jsonReq = jsonReq;
            this.apinumber = apinumber;
            //  this.invoice_type = inv
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            if(page == 1){
                billLists.clear();
            }
        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes;
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonReq);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + apinumber)
                        .post(body)
                        .addHeader("Authorization","Bearer "+sessionManager.getToken())
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
            try {
                JSONObject jsonObject = new JSONObject(message);
                Mylogger.getInstance().Logit(TAG, message);
                if(jsonObject.has("responseCode")) {
                    if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                        sessionManager.logoutUser(getActivity());
                    }
                }
                if(jsonObject.has("response")){
                    Toast.makeText(mcontext, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                }
                else {
                    String next_Url = jsonObject.getString("next_page_url");
                    if(!next_Url.equalsIgnoreCase("")&& !next_Url.equals("null")) {
                        String[] separated = next_Url.split("api3/");
                        apiName = separated[1];
                    }else{
                        apiName = "";
                    }
                    JSONArray result = jsonObject.getJSONArray("data");
                    if (result.length() > 0) {
                        ImageView m = (ImageView) view.findViewById(R.id.imgnodata);
                        m.setVisibility(View.GONE);
                    } else {
                        ImageView m = (ImageView) view.findViewById(R.id.imgnodata);
                        m.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject o = result.getJSONObject(i);

                        Integer id;
                        Integer invoice_status;
                        String proccess_id;
                        String store_name;
                        String bill_period;
                        String dc;
                        String store_address;
                        String amt;
                        String mobile_no;
                        String updated_at;
                        String vendor_id;


                        id = o.getInt("id");
                        proccess_id = o.getString("proccess_id");
                        store_name = o.getString("store_name");
                        if (o.isNull("bill_period")) {
                            bill_period = "";
                        } else {
                            bill_period = o.getString("bill_period");
                        }
                        dc = o.getString("dc");
                        store_address = o.getString("store_address");
                        amt = "0";
                        mobile_no = o.getString("mobile_no");
                        updated_at = o.getString("updated_at");
                        vendor_id = o.getString("vendor_id");
                        status = o.getString("debit_esign_status");
                        invoice_status = 0;
                        page = 0;
                        billLists.add(new BillList(id, proccess_id, store_name, bill_period, dc, store_address, amt, mobile_no, updated_at, invoice_status, vendor_id));
                    }
                    linearAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    public void commanFragmentCallWithBackStack(Fragment fragment, String invoice_id) {

        Fragment cFragment = fragment;

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.KEY_INVOICE_ID, invoice_id);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


}
