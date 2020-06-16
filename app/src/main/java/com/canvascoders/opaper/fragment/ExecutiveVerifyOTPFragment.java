package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GetOTPfrStoreExeResponse.GetOTPfrStoreExeResponse;
import com.canvascoders.opaper.Beans.GetVerifyStoreExecResponse.GetVerifyExecutiveResponse;
import com.canvascoders.opaper.OtpView.PinView;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.ExecutiveMobileVerifyActivity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.SessionManager;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExecutiveVerifyOTPFragment extends Fragment implements View.OnClickListener {

    View v;
    TextView tvMobile, tvTime;
    String otp, mobileNumnber;
    TextView tvResendOTP;
    private int countDown = 30;
    private Boolean resend = false;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Button btVerify;
    private PinView pvOTP;
    TextView tvGreeting, tvNameofUser;
    ImageView ivTimeZone;

    public ExecutiveVerifyOTPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_executive_verify_ot, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        sessionManager = new SessionManager(getActivity());
        progressDialog.setCancelable(false);
        otp = getArguments().getString(Constants.OTP);
        mobileNumnber = getArguments().getString(Constants.PARAM_MOBILE_NO);
        init();
        return v;
    }

    private void init() {
        pvOTP = v.findViewById(R.id.secondPinView);
        tvMobile = v.findViewById(R.id.tvMobile);
        tvMobile.setText("+91 " + mobileNumnber);
        tvResendOTP = v.findViewById(R.id.tvResend);
        btVerify = v.findViewById(R.id.btVerify);
        btVerify.setOnClickListener(this);
        tvTime = v.findViewById(R.id.tvTimeofOTP);
        ivTimeZone = v.findViewById(R.id.ivTimeZone);


        tvGreeting = v.findViewById(R.id.tvGreeting);
        tvNameofUser = v.findViewById(R.id.tvNameofUser);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        //Set greeting
        String greeting = "";
        if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon, ";
            ivTimeZone.setImageResource(R.drawable.afternoon);

        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening, ";
            ivTimeZone.setImageResource(R.drawable.evening);
        } else if (hour >= 21 && hour < 24) {
            greeting = "Good Evening, ";
            ivTimeZone.setImageResource(R.drawable.evening);
        } else {
            greeting = "Good Morning, ";
            ivTimeZone.setImageResource(R.drawable.morning);
        }
        tvGreeting.setText(greeting);
        tvNameofUser.setText(sessionManager.getName());


        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumnber.length() < 10) {
                    showMSG(false, "Provide valid number");
                } else {
                    if (resend == false) {
                        new CountDownTimer(30000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                countDown--;
                                tvTime.setVisibility(View.VISIBLE);
                                tvTime.setText("Sending an OTP. Please wait... in 00:" + countDown);
                            }

                            public void onFinish() {
                                resend = false;
                                countDown = 30;
                                tvTime.setVisibility(View.GONE);
                            }
                        }.start();
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            getOtp();
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }

                        resend = true;
                    } else {
                        showMSG(true, "Try after " + countDown + " Seconds");
                    }
                }
            }
        });
    }

    private void getOtp() {
        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_MOBILE, mobileNumnber);
        ApiClient.getClient().create(ApiInterface.class).resendOTPAgentMobile("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetOTPfrStoreExeResponse>() {
            @Override
            public void onResponse(Call<GetOTPfrStoreExeResponse> call, Response<GetOTPfrStoreExeResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTPfrStoreExeResponse getOTPfrStoreExeResponse = response.body();
                    if (getOTPfrStoreExeResponse.getResponseCode() == 200) {
                        Toast.makeText(getActivity(), getOTPfrStoreExeResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        otp = getOTPfrStoreExeResponse.getData().get(0).getOtp();
                        //   commanFragmentCallWithBackStack(new ExecutiveVerifyOTPFragment(), getOTPfrStoreExeResponse.getData().get(0).getOtp(),etMobileNumber.getText().toString());
                    } else {
                        showMSG(false, getOTPfrStoreExeResponse.getResponse());
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2103 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOTPfrStoreExeResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode 2103 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btVerify:
                if (validate(v)) {
                    String userOTp = pvOTP.getText().toString();
                    if (userOTp.equals(otp)) {
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            GPSTracker gps = new GPSTracker(getActivity());
                            if (gps.canGetLocation()) {
                                Double lat = gps.getLatitude();
                                Double lng = gps.getLongitude();


                            } else {

                                gps.showSettingsAlert();
                            }
                            getVerifyMobile();
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }
                    } else {

                        showMSG(false, "OTP not matched");
                    }
                }

                break;
        }
    }

    private void getVerifyMobile() {
        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).verifyStoreExecutive("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetVerifyExecutiveResponse>() {
            @Override
            public void onResponse(Call<GetVerifyExecutiveResponse> call, Response<GetVerifyExecutiveResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetVerifyExecutiveResponse getVerifyExecutiveResponse = response.body();
                    if (getVerifyExecutiveResponse.getResponseCode() == 200) {
                        Toast.makeText(getActivity(), getVerifyExecutiveResponse.getResponse(), Toast.LENGTH_LONG).show();
                        sessionManager.createLogin(sessionManager.getAgentID(), sessionManager.getToken(), sessionManager.getName(), sessionManager.getEmail(), "", sessionManager.getAgentID(), mobileNumnber, ""/*, "1"*/);
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), getVerifyExecutiveResponse.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity(), " #errorcode 2104" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetVerifyExecutiveResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), " #errorcode 2104" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private boolean validate(View v) {
       /* if (TextUtils.isEmpty(edt_otp_1.getText().toString())) {
            edt_otp_1.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_2.getText().toString())) {
            edt_otp_2.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_3.getText().toString())) {
            edt_otp_3.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_4.getText().toString())) {
            edt_otp_4.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        }*/
        return true;
    }


    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) v.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) v.findViewById(R.id.txt_show_msg_fail);
        txt_show_msg_sucess.setVisibility(View.GONE);
        txt_show_msg_fail.setVisibility(View.GONE);
        if (b) {
            txt_show_msg_sucess.setText(msg);
            txt_show_msg_sucess.setVisibility(View.VISIBLE);
        } else {
            txt_show_msg_fail.setText(msg);
            txt_show_msg_fail.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                txt_show_msg_sucess.setVisibility(View.GONE);
                txt_show_msg_fail.setVisibility(View.GONE);
            }
        }, 2000);
    }


}
