package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.VenderListFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.digio.in.esign2sdk.Digio;
import com.digio.in.esign2sdk.DigioConfig;
import com.digio.in.esign2sdk.DigioEnvironment;
import com.digio.in.esign2sdk.DigioServiceMode;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class AddendumEsignActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //Digio API call
    static String documentId = null;
    private static boolean isBiometrix = false;
    private static boolean isOtp = false;
    Digio digio;
    Toolbar toolbar;
    private WebView mWebView;
    private SessionManager sessionManager;
    private AppCompatTextView btn_agree,btn_resend,btn_update;
    private ProgressDialog progressDialog;
    JSONObject jsonObject = new JSONObject();
    private String TAG = "AddendumEsign";
    VendorList vendor;
    String pdfUrlToSign = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addendum);

        vendor = (VendorList) getIntent().getSerializableExtra("data");

        digio = new Digio();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("");

        progressDialog = new ProgressDialog(AddendumEsignActivity.this);
        progressDialog.setTitle("Please Wait System is Generating Addendum...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(AddendumEsignActivity.this);

        mWebView = (WebView) findViewById(R.id.web_view_id);
        btn_agree = (AppCompatTextView) findViewById(R.id.btn_agree);
        btn_resend = findViewById(R.id.btn_resend);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOTPorBiometricDialog();
            }
        });
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCallResend();
            }
        });

        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateStoreListing();
            }
        });
        ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
        p.setVisibility(View.GONE);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    //do your task
                    ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                    p.setVisibility(View.GONE);
                }
            }
        });


        try {
            jsonObject.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            jsonObject.put(Constants.KEY_PROCESS_ID, vendor.getProccessId());
            jsonObject.put(Constants.KEY_AGENT_ID, sessionManager.getAgentID());

//            Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
            Mylogger.getInstance().Logit(TAG, jsonObject.toString());

            new GetPDFfromServer(jsonObject.toString()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void apiCallResend() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.KEY_PROCESS_ID, "" + vendor.getProccessId());
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_ADUMDUM,"1");
        Call<ResendOTPResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).resendOTP("Bearer "+sessionManager.getToken(),params);
        callUpload.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, retrofit2.Response<ResendOTPResponse> response) {
                ResendOTPResponse resendOTPResponse = response.body();
                Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                if(resendOTPResponse.getResponseCode() == 200){
                    Toast.makeText(AddendumEsignActivity.this,resendOTPResponse.getResponse(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(AddendumEsignActivity.this,resendOTPResponse.getResponse(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {

            }
        });
    }

    private void goToUpdateStoreListing() {

        Intent myIntent = new Intent(AddendumEsignActivity.this, StoreTypeListingActivity.class);
        myIntent.putExtra("data", vendor);
        myIntent.putExtra("editFlag",true);
        startActivity(myIntent);
        finish();
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
                Log.e("Do", "In Background");
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, s);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + "get-addendum-pdf")
                        .post(body)
                        .addHeader("Authorization","Bearer "+sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit(TAG + "1", myRes);
                Log.e("Do", "In Background Over");

                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            Log.e("Do", "In Podt Execute");
            Mylogger.getInstance().Logit(TAG, message);
            progressDialog.dismiss();
            try {

                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {

                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                        pdfUrlToSign = result.getString("pdf_url");
                        mWebView.loadData(result.getString("noc_html"), "text/html; charset=utf-8", "UTF-8");
                        btn_agree.setVisibility(View.GONE);
                    } else {

                        Toast.makeText(AddendumEsignActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("Mesage", "Is null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void selectOTPorBiometricDialog() {
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.choose_option_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout btnbio, btnotp;

        btnbio = (LinearLayout) dialogView.findViewById(R.id.btn_bio);
        btnotp = (LinearLayout) dialogView.findViewById(R.id.btn_otp);

        AppCompatImageView btn_close = (AppCompatImageView) dialogView.findViewById(R.id.btn_close);
        AppCompatTextView btn_ok = (AppCompatTextView) dialogView.findViewById(R.id.btn_ok);

        final AppCompatImageView img_phone_otp = (AppCompatImageView) dialogView.findViewById(R.id.img_phone_otp);
        AppCompatTextView txt_phone_otp = (AppCompatTextView) dialogView.findViewById(R.id.txt_phone_otp);

        final AppCompatImageView img_bio = (AppCompatImageView) dialogView.findViewById(R.id.img_bio);
        AppCompatTextView txt_biomatric = (AppCompatTextView) dialogView.findViewById(R.id.txt_biomatric);

        final android.app.AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        btnbio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBiometrix = true;
                isOtp = false;
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_active));
                img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_normal));
            }
        });

        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtp = true;
                isBiometrix = false;
                img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_active));
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_normal));
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                b.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (isBiometrix) {

                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                            digioConfig.setServiceMode(DigioServiceMode.FP);  //FP is fingerprint

                            try {
                                digio.init(AddendumEsignActivity.this, digioConfig);
                                new getDocumentID("").execute(); // this will mnage document id from Digio server
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (isOtp) {
                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                            digioConfig.setServiceMode(DigioServiceMode.OTP);  //FP is fingerprint

                            try {
                                digio.init(AddendumEsignActivity.this, digioConfig);

                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    try {
                                        digio.init(AddendumEsignActivity.this, digioConfig);
                                        //showVidDialogue();
                                        new getDocumentID("").execute(); // this will mnage document id from Digio server
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Constants.ShowNoInternet(AddendumEsignActivity.this);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            }
        });
    }

    ////// Esign Process
    // Calling of DIGIO for DOCS Signing
    public class getDocumentID extends AsyncTask<String, Void, String> {
        String myRes = null;

        String vId = "";

        public getDocumentID(String id) {
            this.vId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Uploading Document...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Log.e("PDF URL TO SIgn", "" + pdfUrlToSign);
            Log.e("AADHAR NO :", "" + vendor.getAadhaarNo());
            Log.e("MOBILE NO : ", "" + vendor.getMobileNo());
        }

        @Override
        protected String doInBackground(String[] params) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000, TimeUnit.MINUTES)
                    .readTimeout(1000, TimeUnit.MINUTES)
                    .writeTimeout(1000, TimeUnit.MINUTES)
                    .build();

            Request request = new Request.Builder()
                    .url(pdfUrlToSign)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();


                byte[] data = response.body().bytes();
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);

                Mylogger.getInstance().Logit(TAG, base64);

                MediaType mediaType = MediaType.parse("application/json");

                JSONObject param = new JSONObject();
                JSONObject identifier = new JSONObject();
                JSONArray signers = new JSONArray();

                identifier.put(Constants.PARAM_IDENTIFIRE, vendor.getMobileNo());
                identifier.put(Constants.PARAM_AADHAR_ID, vendor.getAadhaarNo());
                if (vId.length() > 0)
                    identifier.put(Constants.PARAM_VID, vId);
                signers.put(identifier);

                param.put(Constants.PARAM_FILE_NAME, "addendum.pdf");
                param.put(Constants.PARAM_COMMENT, "Please sign");
                param.put(Constants.PARAM_SIGNERS, signers);
                param.put(Constants.PARAM_FILE_DATA, base64);
                param.put(Constants.PARAM_EXPIRE_IN_DAY, 7);
                param.put(Constants.PARAM_NOTIFY_SIGNERS, true);


                Mylogger.getInstance().Logit(TAG, param.toString());
                RequestBody body = RequestBody.create(mediaType, param.toString());

                String ccid = Constants.DIGIO_CLIENT_ID + ":" + Constants.DIGIO_CLIENT_SECRET;

                byte[] cid = ccid.getBytes("UTF-8");
                String base642 = Base64.encodeToString(cid, Base64.NO_WRAP);

                Mylogger.getInstance().Logit(TAG, base642);

                Request requestLogin = new Request.Builder()
                        .url(Constants.DIGIO_BASE_URL + "v2/client/document/uploadpdf")
                        .post(body)
                        .addHeader("accept-language", "en-US,en;q=0.8")
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", "Basic " + base642)
                        .addHeader("accept", "*/*")
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();


                String tmp = responseLogin.body().string();
                myRes = tmp;
                Mylogger.getInstance().Logit(TAG, tmp);

                return myRes;

            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage());
            }


            return myRes;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            if (message != null) {
                Mylogger.getInstance().Logit(TAG, message);
                try {
//                    Toast.makeText(this, mobile_no + "", Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(message);
                    documentId = jsonObject.getString("id");
                    //  sessionFP.startDigioEsign(documentId, mobile_no);
                    try {
                        digio.esign(documentId, vendor.getMobileNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddendumEsignActivity.this, "Timeout", Toast.LENGTH_LONG).show();
            }

        }
    }

///////   Post sign Process


    public void onSigningSuccess(String documentIds) {
        Toast.makeText(this, documentIds + " signed successfully", Toast.LENGTH_SHORT).show();
        Boolean isSigned = true;
        documentId = documentIds;
        storePDFOnServer(documentId);

    }

    public void onSigningFailure(String documentId, int code, String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        }
        return super.onOptionsItemSelected(item);
    }


    public void storePDFOnServer(String sss) {

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.KEY_PROCESS_ID, "" + vendor.getProccessId());
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_ESIGN_URL, sss);


            Call<JsonObject> callUpload = ApiClient.getClient().create(ApiInterface.class).StoreAddendum("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));

                    if (response != null) {

                        if (response.isSuccessful()) {
                            JsonObject object = response.body();
                            if (response.code() == 200) {
//                                String msg = "Store name: " + Constants.billList.getStore_name() +
//                                        " , Invoice No: " + getInvoiceWithFormate(Constants.billList.getVendor_id(), invoice_id) +
//                                        " , Invoice signed on " + Constants.billList.getUpdated_at() + " successfully.";
//                                showAlert();
                                //SendMSG(mobile_no, msg);
                                Toast.makeText(AddendumEsignActivity.this, "Addendum E-sign done successfully", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AddendumEsignActivity.this, "Addendum E-sign could not complete,retry again", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });
        } else {
            Constants.ShowNoInternet(this);
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_profile) {

            commanFragmentCallWithoutBackStack(new ProfileFragment());

        }
        if (id == R.id.nav_1) {
            Intent i = new Intent(AddendumEsignActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_2) {

            commanFragmentCallWithoutBackStack(new MobileFragment());

        }
        if (id == R.id.nav_3) {

            commanFragmentCallWithoutBackStack(new InvoiceMainFragment());

        }
        if (id == R.id.nav_v) {

            commanFragmentCallWithoutBackStack(new VenderListFragment());

        }
        if (id == R.id.nav_gst) {
            commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());

        }
        if (id == R.id.nav_debit) {
            commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());

        }
        if (id == R.id.nav_4) {
            commanFragmentCallWithoutBackStack(new NotificationFragment());

        }
        if (id == R.id.nav_5) {

            commanFragmentCallWithoutBackStack(new ReportFragment());


        }
        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(AddendumEsignActivity.this);
            sessionManager.logoutUser(AddendumEsignActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;


        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.commit();

        }
    }
}
