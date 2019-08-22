package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private TextView tvTitleActionBar, tv_message;
    private ImageView ivActionBarBack;
    Button btnDashboard, btnCheckSign, btnResend;
    private SessionManager sessionManager;
    String str_process_id;
    TextView tvDashBoard;
    private int countDown = 30;
    String page_num;
    private String lattitude = "", longitude = "";
    GPSTracker gps;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    RelativeLayout rvGST;
    private ImageView ivAgreeStatus, ivGstStatus, ivAssessStatus, ivNocStatus;
    private Boolean resend = false;
    private String TAG = "Process_info";
    ProgressDialog progressDialog;
    int pL, pT, pR, pB;
    LinearLayout llNoc, llAgreement, llGSt, llAssesment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_info);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        init();
    }

    private void init() {
        tv_message = findViewById(R.id.lbl_your_name);
        tv_message.setText(getText(R.string.start_esign) + "" + getText(R.string.complete_esign));
        sessionManager = new SessionManager(ProcessInfoActivity.this);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        tvTitleActionBar = findViewById(R.id.tv_title_Process);
        ivActionBarBack = findViewById(R.id.iv_back_process);
        ivActionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
                startActivity(new Intent(ProcessInfoActivity.this, DashboardActivity.class));
            }
        });
        ivAgreeStatus = findViewById(R.id.ivAgreeStatus);
        ivAssessStatus = findViewById(R.id.ivAsessStatus);
        ivNocStatus = findViewById(R.id.ivNOCStatus);
        ivGstStatus = findViewById(R.id.ivGSTStatus);

        btnDashboard = findViewById(R.id.btnDashboard);
        btnDashboard.setOnClickListener(this);
        btnCheckSign = findViewById(R.id.btnCheckSign);
        btnCheckSign.setOnClickListener(this);
        btnResend = findViewById(R.id.btnResend);
        btnResend.setOnClickListener(this);
        rvGST = findViewById(R.id.rvGst);
        llAgreement = findViewById(R.id.llAgreement);
        llNoc = findViewById(R.id.llNoc);
        llGSt = findViewById(R.id.llGST);
        llAssesment = findViewById(R.id.llAssessment);
        pL = llAgreement.getPaddingLeft();
        pT = llAgreement.getPaddingTop();
        pR = llAgreement.getPaddingRight();
        pB = llAgreement.getPaddingBottom();


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallCheckEsign();
        } else {
            Constants.ShowNoInternet(ProcessInfoActivity.this);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                    } else {
                        Constants.ShowNoInternet(this);
                    }

                    resend = true;
                }

                break;

            case R.id.btnCheckSign:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallCheckEsign();
                } else {
                    Constants.ShowNoInternet(this);
                }

                break;
            case R.id.btnDashboard:
                startActivity(new Intent(ProcessInfoActivity.this, DashboardActivity.class));
                break;
        }
    }

    private void ApiCallCheckEsign() {
        progressDialog.setMessage("Please Wait Uploading Cheque Document...");
        progressDialog.show();

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.KEY_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

            Call<CheckEsignResponse> call = ApiClient.getClient().create(ApiInterface.class).checkEsign("Bearer " + sessionManager.getToken(), params);
            call.enqueue(new Callback<CheckEsignResponse>() {
                @Override
                public void onResponse(Call<CheckEsignResponse> call, Response<CheckEsignResponse> response) {
                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                    CheckEsignResponse checkEsignResponse = response.body();
                    if (checkEsignResponse.getResponseCode() == 200) {
                        progressDialog.dismiss();

                        if (checkEsignResponse.getData().get(0).getScreen() == 12) {
                            finish();
                            startActivity(new Intent(ProcessInfoActivity.this, TaskCompletedActivity.class));
                        } else {
                            //  Constants.showAlert(tv_message.getRootView(), checkEsignResponse.getData().get(0).getScreenMsg(), true);
                            if (checkEsignResponse.getData().get(0).getIfGst() != null) {
                                if (checkEsignResponse.getData().get(0).getIfGst().equalsIgnoreCase("no")) {
                                    rvGST.setVisibility(View.VISIBLE);
                                    if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Agreement Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                                            ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llAgreement.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }
                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("NOC Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llNoc.setPadding(pL, pT, pR, pB);
                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }

                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("GST Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(null);
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(null);
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }

                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Assessment Screen.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(null);
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackgroundDrawable(null);
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAssesment.setPadding(pL, pT, pR, pB);

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(null);
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(null);
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAssesment.setPadding(pL, pT, pR, pB);
                                        }

                                    }

                                } else {
                                    rvGST.setVisibility(View.GONE);
                                    if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Agreement Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                                            ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llAgreement.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }
                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("NOC Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llNoc.setPadding(pL, pT, pR, pB);
                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }

                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("GST Sign.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(null);
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(null);
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        }

                                    } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Assessment Screen.")) {
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            ivAgreeStatus.setBackgroundDrawable(null);
                                            llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackgroundDrawable(null);
                                            llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackgroundDrawable(null);
                                            llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                            llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAssesment.setPadding(pL, pT, pR, pB);

                                        } else {
                                            ivAgreeStatus.setBackground(null);
                                            llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llAgreement.setPadding(pL, pT, pR, pB);

                                            ivNocStatus.setBackground(null);
                                            llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llNoc.setPadding(pL, pT, pR, pB);

                                            ivGstStatus.setBackground(null);
                                            llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                            llGSt.setPadding(pL, pT, pR, pB);

                                            ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                            llAssesment.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                            llAssesment.setPadding(pL, pT, pR, pB);
                                        }


                                    }


                                }
                            } else {
                                rvGST.setVisibility(View.GONE);
                                if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Agreement Sign.")) {
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                                        ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                        llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                    } else {
                                        ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                        llAgreement.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                    }
                                } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("NOC Sign.")) {
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        ivAgreeStatus.setBackgroundDrawable(null);
                                        llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                        llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llNoc.setPadding(pL, pT, pR, pB);
                                        ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                    } else {
                                        ivAgreeStatus.setBackground(null);
                                        llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                        llNoc.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llNoc.setPadding(pL, pT, pR, pB);

                                        ivGstStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                                        ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                    }

                                } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("GST Sign.")) {
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        ivAgreeStatus.setBackgroundDrawable(null);
                                        llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackgroundDrawable(null);
                                        llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llNoc.setPadding(pL, pT, pR, pB);

                                        ivGstStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                        llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llGSt.setPadding(pL, pT, pR, pB);

                                        ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                                    } else {
                                        ivAgreeStatus.setBackground(null);
                                        llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackground(null);
                                        llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llNoc.setPadding(pL, pT, pR, pB);

                                        ivGstStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                        llGSt.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llGSt.setPadding(pL, pT, pR, pB);

                                        ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                        llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                    }

                                } else if (checkEsignResponse.getData().get(0).getScreenMsg().equalsIgnoreCase("Assessment Screen.")) {
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        ivAgreeStatus.setBackgroundDrawable(null);
                                        llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackgroundDrawable(null);
                                        llNoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llNoc.setPadding(pL, pT, pR, pB);

                                        ivGstStatus.setBackgroundDrawable(null);
                                        llGSt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llGSt.setPadding(pL, pT, pR, pB);

                                        ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                        llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llAssesment.setPadding(pL, pT, pR, pB);

                                    } else {
                                        ivAgreeStatus.setBackground(null);
                                        llAgreement.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llAgreement.setPadding(pL, pT, pR, pB);

                                        ivNocStatus.setBackground(null);
                                        llNoc.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llNoc.setPadding(pL, pT, pR, pB);

                                        ivGstStatus.setBackground(null);
                                        llGSt.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                        llGSt.setPadding(pL, pT, pR, pB);

                                        ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                        llAssesment.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                        llAssesment.setPadding(pL, pT, pR, pB);
                                    }


                                }


                            }


                        }

                    } else if (checkEsignResponse.getResponseCode() == 405) {
                        progressDialog.dismiss();

                        sessionManager.logoutUser(ProcessInfoActivity.this);
                    } else {
                        progressDialog.dismiss();
                        Constants.showAlert(tv_message.getRootView(), checkEsignResponse.getStatus(), false);
                        // Toast.makeText(ProcessInfoActivity.this, object.getStatus(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CheckEsignResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ProcessInfoActivity.this, "Something went wrong", Toast.LENGTH_LONG);
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
            params.put(Constants.PARAM_LONGITUDE, longitude);
            params.put(Constants.PARAM_LATITUDE, lattitude);

            Call<ResendOTPResponse> call = ApiClient.getClient().create(ApiInterface.class).resendOTP("Bearer " + sessionManager.getToken(), params);
            call.enqueue(new Callback<ResendOTPResponse>() {
                @Override
                public void onResponse(Call<ResendOTPResponse> call, Response<ResendOTPResponse> response) {
                    ResendOTPResponse resendOTPResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                    if (resendOTPResponse.getResponseCode() == 200) {
                        Toast.makeText(ProcessInfoActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
                    } else if (resendOTPResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(ProcessInfoActivity.this);
                    } else {
                        Toast.makeText(ProcessInfoActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(ProcessInfoActivity.this, DashboardActivity.class));
    }

    public static void setViewBackgroundWithoutResettingPadding(final View v, final int backgroundResId) {
        final int paddingBottom = v.getPaddingBottom(), paddingLeft = v.getPaddingLeft();
        final int paddingRight = v.getPaddingRight(), paddingTop = v.getPaddingTop();
        v.setBackgroundResource(backgroundResId);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }
}
