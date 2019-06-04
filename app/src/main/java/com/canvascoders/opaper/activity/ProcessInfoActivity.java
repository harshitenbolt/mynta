package com.canvascoders.opaper.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.CheckEsignResponse.CheckEsignResponse;
import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitleActionBar,tv_message;
    private ImageView ivActionBarBack;
    Button btnDashboard, btnCheckSign, btnResend;
    private SessionManager sessionManager;
    String str_process_id;
    private int countDown = 30;
    String page_num;
    private String lattitude = "",longitude="";
    GPSTracker gps;
    private Boolean resend = false;
    private String TAG = "Process_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_info);
        init();
    }

    private void init() {
        tv_message = findViewById(R.id.lbl_your_name);
        tv_message.setText(getText(R.string.start_esign)+" "+page_num+" "+getText(R.string.complete_esign));
        sessionManager = new SessionManager(ProcessInfoActivity.this);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        tvTitleActionBar = findViewById(R.id.tv_title_Process);
        ivActionBarBack = findViewById(R.id.iv_back_process);
        ivActionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // finish();
                startActivity(new Intent(ProcessInfoActivity.this,DashboardActivity.class));
            }
        });

        btnDashboard = findViewById(R.id.btnDashboard);
        btnDashboard.setOnClickListener(this);
        btnCheckSign = findViewById(R.id.btnCheckSign);
        btnCheckSign.setOnClickListener(this);
        btnResend = findViewById(R.id.btnResend);
        btnResend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnResend:
                /*if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                }
                else {
                    Constants.ShowNoInternet(this);
                }*/

                if (resend == false) {
                    new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            countDown--;
                            btnResend.setText("00:" + countDown);
                        }

                        public void onFinish() {
                            resend = false;
                            countDown = 30;
                            btnResend.setText("Resend");
                        }
                    }.start();
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallResend();
                    }
                    else {
                        Constants.ShowNoInternet(this);
                    }

                    resend = true;
                }

                break;

            case R.id.btnCheckSign:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallCheckEsign();
                }
                else {
                    Constants.ShowNoInternet(this);
                }

                break;
            case R.id.btnDashboard:
                startActivity(new Intent(ProcessInfoActivity.this,DashboardActivity.class));
                break;
        }
    }

    private void ApiCallCheckEsign() {

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.KEY_PROCESS_ID,  str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

            Call<CheckEsignResponse> call = ApiClient.getClient().create(ApiInterface.class).checkEsign("Bearer "+sessionManager.getToken(),params);
            call.enqueue(new Callback<CheckEsignResponse>() {
                @Override
                public void onResponse(Call<CheckEsignResponse> call, Response<CheckEsignResponse> response) {
                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                    CheckEsignResponse checkEsignResponse = response.body();
                    if(checkEsignResponse.getResponseCode() == 200){

                            if(checkEsignResponse.getData().get(0).getScreen() ==11){
                                finish();
                                startActivity(new Intent(ProcessInfoActivity.this,TaskCompletedActivity.class));
                            }
                            else{
                                Constants.showAlert(tv_message.getRootView(), checkEsignResponse.getData().get(0).getScreenMsg(), true);
                            }

                        }
                    else if (checkEsignResponse.getResponseCode() == 405) {

                        sessionManager.logoutUser(ProcessInfoActivity.this);
                    }else {
                            Constants.showAlert(tv_message.getRootView(), checkEsignResponse.getStatus(), false);
                            // Toast.makeText(ProcessInfoActivity.this, object.getStatus(), Toast.LENGTH_LONG).show();
                        }
                }

                @Override
                public void onFailure(Call<CheckEsignResponse> call, Throwable t) {
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });

        }

    }

    private void ApiCallResend() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.KEY_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_LONGITUDE,longitude);
            params.put(Constants.PARAM_LATITUDE,lattitude);

            Call<ResendOTPResponse> call = ApiClient.getClient().create(ApiInterface.class).resendOTP("Bearer "+sessionManager.getToken(),params);
            call.enqueue(new Callback<ResendOTPResponse>() {
                @Override
                public void onResponse(Call<ResendOTPResponse> call, Response<ResendOTPResponse> response) {
                    ResendOTPResponse resendOTPResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                    if(resendOTPResponse.getResponseCode() == 200){
                        Toast.makeText(ProcessInfoActivity.this,resendOTPResponse.getResponse(),Toast.LENGTH_LONG).show();
                    }
                    else if(resendOTPResponse.getResponseCode() ==411){
                        sessionManager.logoutUser(ProcessInfoActivity.this);
                    }
                    else{
                        Toast.makeText(ProcessInfoActivity.this,resendOTPResponse.getResponse(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResendOTPResponse> call, Throwable t) {
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ProcessInfoActivity.this,DashboardActivity.class));
    }
}
