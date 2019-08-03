package com.canvascoders.opaper.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.UserDetailTResponse.GetUserDetails;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.database.RealmController;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.fragment.InfoEditFragment;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.VenderListFragment;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.canvascoders.opaper.utils.Constants.showAlert;

public class AgreementDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static String documentId = null;
    private static boolean isBiometrix = false;
    private static boolean isOtp = false;
    JSONObject jsonObject = new JSONObject();
    Digio digio;
    private WebView mWebView;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private AppCompatTextView btn_agree;
    private AppCompatTextView btn_edit;
    private String aggrementPDFUrl = "";
    private String TAG = "AgreementDetail";
    private String uid = "";
    private String uname = "";
    private ProgressDialog progressDialog;
    String str_process_id;
    String str_mobile_no;
    public static CardView card_webview;
    static TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_detail);


        digio = new Digio();
        //create instance of database Realm to access functions
        RealmController.with(this).getRealm();
        sessionManager = new SessionManager(AgreementDetailActivity.this);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        str_mobile_no = sessionManager.getData(Constants.KEY_EMP_MOBILE);
        uid = sessionManager.getData(Constants.KEY_UID);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        card_webview = findViewById(R.id.card_webview);
        card_webview.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle(Constants.TITLE_AGREEMENT);

        mWebView = (WebView) findViewById(R.id.web_view_id);
        btn_agree = (AppCompatTextView) findViewById(R.id.btn_agree);
        btn_edit = (AppCompatTextView) findViewById(R.id.btn_edit);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(Constants.TITLE_AGREEMENT);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commanFragmentCallWithBackStack(new InfoEditFragment());
            }
        });
        progressDialog = new ProgressDialog(AgreementDetailActivity.this);
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
            jsonObject.put(Constants.PARAM_PROCESS_ID, str_process_id);
            jsonObject.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            Mylogger.getInstance().Logit("tkn", jsonObject.toString());

            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                new GetPDFfromServer(jsonObject.toString()).execute();
            } else {
                Constants.ShowNoInternet(AgreementDetailActivity.this);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangDialog();
            }
        });


    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgreementDetailActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.choose_option_dialog, null);
        dialogBuilder.setView(dialogView);
        final LinearLayout btnbio, btnotp;

        btnbio = (LinearLayout) dialogView.findViewById(R.id.btn_bio);
        btnotp = (LinearLayout) dialogView.findViewById(R.id.btn_otp);

        AppCompatImageView btn_close = (AppCompatImageView) dialogView.findViewById(R.id.btn_close);
        AppCompatTextView btn_ok = (AppCompatTextView) dialogView.findViewById(R.id.btn_ok);

        final AppCompatImageView img_phone_otp = (AppCompatImageView) dialogView.findViewById(R.id.img_phone_otp);
        AppCompatTextView txt_phone_otp = (AppCompatTextView) dialogView.findViewById(R.id.txt_phone_otp);

        final AppCompatImageView img_bio = (AppCompatImageView) dialogView.findViewById(R.id.img_bio);
        AppCompatTextView txt_biomatric = (AppCompatTextView) dialogView.findViewById(R.id.txt_biomatric);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnbio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBiometrix = true;
                isOtp = false;
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_active));
               // img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_normal));
            }
        });
        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtp = true;
                isBiometrix = false;
               // img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_active));
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_normal));
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (isBiometrix) {
                            //   RealmController.getInstance().save(new TrackerModel(1, sessionManager.getAgentID(), Constants.processID, "AgreementDetails", "isBiometrix selected", "", "", ""));
                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
//                            digioConfig.setEnvironment(DigioEnvironment.STAGE);
                            digioConfig.setServiceMode(DigioServiceMode.FP);  //FP is fingerprint

                            try {
                                digio.init(AgreementDetailActivity.this, digioConfig);
                                new getDocumentID("").execute(); // this will mnage document id from Digio server
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("error",e.getMessage());
                            }
                        }
                        if (isOtp) {
                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
//                            digioConfig.setEnvironment(DigioEnvironment.STAGE);
                            digioConfig.setServiceMode(DigioServiceMode.OTP);  //FP is fingerprint

                            try {
                                digio.init(AgreementDetailActivity.this, digioConfig);
//                                showVidDialogue();
                                new getDocumentID("").execute();
                                // this will mnage document id from Digio server
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Exception", String.valueOf(e));
                            }
                        }
                    }
                });
            }
        });
    }


    private void showVidDialogue() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgreementDetailActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_enter_vid, null);
        dialogBuilder.setView(dialogView);
        final LinearLayout btnbio, btnotp;

        AppCompatImageView btn_close = dialogView.findViewById(R.id.btn_close);
        AppCompatTextView btn_ok = dialogView.findViewById(R.id.btn_ok);

        TextView tvGetVId = dialogView.findViewById(R.id.tvGetVId);
        final EditText etVid = dialogView.findViewById(R.id.etVid);

        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVid.getText().toString().length() >= 15) {
                    String vId = etVid.getText().toString();
                    b.dismiss();
                    new getDocumentID(vId).execute();
                }

            }
        });
        tvGetVId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.AADHAR_VID_URL));
                startActivity(i);
            }
        });

    }


// RealmController.getInstance().save(new TrackerModel(1, "test " + m, "test 3", "test 4", "test 5", "test 6", "test 7", "test 8"));
    //RealmController.getInstance().delete();
    // Callback listener functions

    public void onSigningSuccess(String documentIds) {
        // Toast.makeText(this, documentIds + " signed successfully", Toast.LENGTH_SHORT).show();
        Toast.makeText(AgreementDetailActivity.this, "Document signed successfully", Toast.LENGTH_LONG).show();
        // RealmController.getInstance().save(new TrackerModel(1, sessionManager.getAgentID(), Constants.processID, "AgreementDetails", "getDocumentID", "onSigningSuccess", "Success", documentIds));
        documentId = documentIds;
        storePDFOnServer(documentId);

    }

    public void onSigningFailure(String documentId, int code, String response) {
        Toast.makeText(AgreementDetailActivity.this, response, Toast.LENGTH_SHORT).show();
        //  RealmController.getInstance().save(new TrackerModel(1, sessionManager.getAgentID(), Constants.processID, "AgreementDetails", "getDocumentID", "onSigningSuccess", response, documentId));
        callOnDigioFailure(documentId, "" + code, response);
    }


    public void storePDFOnServer(String sss) {

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Toast.makeText(AgreementDetailActivity.this, "Please wait we are storing document", Toast.LENGTH_LONG).show();

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_ESIGN_URL, sss);

            Mylogger.getInstance().Logit(TAG, params.toString());

            Call<JsonObject> callUpload = ApiClient.getClient().create(ApiInterface.class).StoreAgreement("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                    if (response != null) {

                        if (response.isSuccessful()) {
                            JsonObject object = response.body();

                            if (response.code() == 200) {
                                showAlert();
                            } else {
                                Toast.makeText(AgreementDetailActivity.this, "Could not store your agreement to our server", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        Toast.makeText(AgreementDetailActivity.this, "Could not store your agreement to our server", Toast.LENGTH_LONG).show();
                        Mylogger.getInstance().Logit(TAG, "response is null here ");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });
        } else {
            Constants.ShowNoInternet(AgreementDetailActivity.this);
        }

    }

    private void showAlert() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(AgreementDetailActivity.this);
        alertDialog.setTitle("Vendor E-signing using Aadhaar.");
        alertDialog.setMessage("Document has been signed sucessfully.");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                commanFragmentCallWithoutBackStack(new NocActivity());

               /* Intent intent = new Intent(AgreementDetailActivity.this, NocActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(AgreementDetailActivity.this, ProcessInfoActivity.class);
                startActivity(intent);
                finish();


            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {

            commanFragmentCallWithoutBackStack(new ProfileFragment());

        }
        if (id == R.id.nav_1) {
            Intent i = new Intent(AgreementDetailActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_2) {

            commanFragmentCallWithoutBackStack(new MobileFragment());

        }
        if (id == R.id.nav_3) {

            commanFragmentCallWithoutBackStack(new InvoiceMainFragment());

        }
        if (id == R.id.nav_gst) {
            commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());

        }
        if (id == R.id.nav_debit) {
            commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());

        }
        if (id == R.id.nav_v) {

            commanFragmentCallWithoutBackStack(new VenderListFragment());

        }
        if (id == R.id.nav_4) {
            commanFragmentCallWithoutBackStack(new NotificationFragment());

        }
        if (id == R.id.nav_5) {

            commanFragmentCallWithoutBackStack(new ReportFragment());


        }
        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(AgreementDetailActivity.this);
            sessionManager.logoutUser(AgreementDetailActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

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
        }

        @Override
        protected String doInBackground(String[] params) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000, TimeUnit.MINUTES)
                    .readTimeout(1000, TimeUnit.MINUTES)
                    .writeTimeout(1000, TimeUnit.MINUTES)
                    .build();

            if (aggrementPDFUrl != null && !aggrementPDFUrl.equalsIgnoreCase("")) {
                Request request = new Request.Builder()
                        .url(aggrementPDFUrl)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    byte[] data = response.body().bytes();
                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);

                    Mylogger.getInstance().Logit("Main", base64);
                    MediaType mediaType = MediaType.parse("application/json");

                    JSONObject param = new JSONObject();
                    JSONObject identifier = new JSONObject();
                    JSONArray signers = new JSONArray();

                    identifier.put(Constants.PARAM_IDENTIFIRE, str_mobile_no);
                    identifier.put(Constants.PARAM_AADHAR_ID, uid);
                    if (vId.length() > 0)
                        identifier.put(Constants.PARAM_VID, vId);
                    signers.put(identifier);

                    param.put(Constants.PARAM_FILE_NAME, "agreement.pdf");
                    param.put(Constants.PARAM_COMMENT, "Please sign Agreement");
                    param.put(Constants.PARAM_SIGNERS, signers);
                    param.put(Constants.PARAM_FILE_DATA, base64);
                    param.put(Constants.PARAM_EXPIRE_IN_DAY, 7);
                    param.put(Constants.PARAM_DISPLAY_ON_PAGE, "all");
                    param.put(Constants.PARAM_NOTIFY_SIGNERS, true);


                    Mylogger.getInstance().Logit("Mainparam", param.toString());
                    RequestBody body = RequestBody.create(mediaType, param.toString());

                    String ccid = Constants.DIGIO_CLIENT_ID + ":" + Constants.DIGIO_CLIENT_SECRET;

                    byte[] cid = ccid.getBytes("UTF-8");
                    String base642 = Base64.encodeToString(cid, Base64.NO_WRAP);

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
                    Mylogger.getInstance().Logit("Main", tmp);
                    final String Message = responseLogin.message();
                    Mylogger.getInstance().Logit("Main", Message);
                    if (Message.equals("OK")) {
                    }
                    return myRes;

                } catch (Exception e) {
                    e.printStackTrace();
                    Mylogger.getInstance().Logit("Main", e.getMessage());
                }
            }


            return myRes;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            if (message != null) {
                Mylogger.getInstance().Logit("Main", message);
                try {

                    JSONObject jsonObject = new JSONObject(message);
                    documentId = jsonObject.getString("id");
                    callLogBeforeDigioSign(documentId);
                    try {
                        digio.esign(documentId, str_mobile_no);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AgreementDetailActivity.this, str_mobile_no + "", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {

                Toast.makeText(AgreementDetailActivity.this, "Timeout", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callLogBeforeDigioSign(String documentId) {
        JsonObject dataObj = new JsonObject();
        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        dataObj.addProperty(Constants.KEY_PROCESS_ID, str_process_id);
        dataObj.addProperty(Constants.KEY_DOCUMENT_ID, documentId);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.addProperty("method", "Agreement Detail");

        Log.e("FCMID", "FCMID" + sessionManager.getData(Constants.KEY_FCM_ID));

        ApiClient.getClient().create(ApiInterface.class).submitSigningLog("Bearer "+sessionManager.getToken(),dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callOnDigioFailure(String documentId, String Code, String msg) {
        JsonObject dataObj = new JsonObject();

        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, str_process_id);
        dataObj.addProperty(Constants.KEY_DOCUMENT_ID, documentId);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.addProperty("method", "Agreement Detail");
        dataObj.addProperty("reason", "Code:" + Code + " ::" + msg);

        Log.e("FCMID", "FCMID" + sessionManager.getData(Constants.KEY_FCM_ID));

        ApiClient.getClient().create(ApiInterface.class).submitSigningLog("Bearer "+sessionManager.getToken(),dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
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
            progressDialog.setMessage("wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.MINUTES)
                        .readTimeout(1000, TimeUnit.MINUTES)
                        .writeTimeout(1000, TimeUnit.MINUTES)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, s);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + "agreement")
                        .post(body)
                        .addHeader("Authorization","Bearer "+sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit("TAG" + "1", myRes);


                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit("TAG", e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            try {
                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {
                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                        aggrementPDFUrl = result.getString("agreement_doc").toString();
                        uid = result.getString("uid").toString();
                        uname = result.getString("name").toString();
                        mWebView.loadData(result.getString("agreement_html"), "text/html; charset=utf-8", "UTF-8");
                    } else if (responseCode == 405) {
                        sessionManager.logoutUser(AgreementDetailActivity.this);
                    } else {
                        ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                        p.setVisibility(View.GONE);
                        Toast.makeText(AgreementDetailActivity.this, jsonObject.getString("message").toLowerCase(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                    p.setVisibility(View.GONE);
                    Toast.makeText(AgreementDetailActivity.this, "Server could not response,Please try again", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                p.setVisibility(View.GONE);
                e.printStackTrace();
            } catch (Exception f) {
                ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                p.setVisibility(View.GONE);
                Toast.makeText(AgreementDetailActivity.this, "Unable to store with erver", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void settitle(String title) {
        if (title != null) {
            tv_title.setText(title);
        }

    }

    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            card_webview.setVisibility(View.INVISIBLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
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


    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            tv_title.setText(Constants.TITLE_AGREEMENT);
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}