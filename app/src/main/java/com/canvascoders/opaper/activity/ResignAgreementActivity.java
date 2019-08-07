package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ResignAgreementActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private ImageView ivBack;
    WebView mWebView;
    VendorList vendor;
    JSONObject jsonObject = new JSONObject();
    Button btChangeRate,btResign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_agreement);


        init();
    }

    private void init() {
        vendor = (VendorList) getIntent().getSerializableExtra("data");

        btChangeRate = findViewById(R.id.btChangeRate);
        btResign = findViewById(R.id.btResign);
        progressDialog = new ProgressDialog(ResignAgreementActivity.this);
        progressDialog.setTitle("Please Wait System is Generating Agreeement...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(ResignAgreementActivity.this);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView = (WebView) findViewById(R.id.web_view_id);

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
            jsonObject.put(Constants.PARAM_IS_DOC, "1");

////            Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
          //  Mylogger.getInstance().Logit(TAG, jsonObject.toString());

            new GetPDFfromServer(jsonObject.toString()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        btChangeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent i = new Intent(ResignAgreementActivity.this,EditRateWhileResigAgreeActivity.class);
                i.putExtra("data", vendor);
                startActivity(i);

               /* if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(EditRateWhileResigAgreeActivity.this);
                }*/
            }
        });

        btResign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(ResignAgreementActivity.this);
                }

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
                        .url(Constants.BaseURL + "store-type-resign")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
              //  Mylogger.getInstance().Logit(TAG + "1", myRes);
                Log.e("Do", "In Background Over");

                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
             //   Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            Log.e("Do", "In Podt Execute");
          //  Mylogger.getInstance().Logit(TAG, message);
            progressDialog.dismiss();
            try {

                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {

                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                       // pdfUrlToSign = result.getString("pdf_url");
                        mWebView.loadData(result.getString("noc_html"), "text/html; charset=utf-8", "UTF-8");
                       // btn_agree.setVisibility(View.GONE);
                    } else {

                        Toast.makeText(ResignAgreementActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("Mesage", "Is null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void ApiCallResendAgreement() {

        progressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).ResignAgreement("Bearer " + sessionManager.getToken(), String.valueOf(vendor.getProccessId())).enqueue(new Callback<ResignAgreementResponse>() {
            @Override
            public void onResponse(Call<ResignAgreementResponse> call, retrofit2.Response<ResignAgreementResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    ResignAgreementResponse resignAgreementResponse = response.body();
                    if (resignAgreementResponse.getResponseCode() == 200) {
                        Toast.makeText(ResignAgreementActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(ResignAgreementActivity.this);
                    } else {
                        Toast.makeText(ResignAgreementActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResignAgreementResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

}
