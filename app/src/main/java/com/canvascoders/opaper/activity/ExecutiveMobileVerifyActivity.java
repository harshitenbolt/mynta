package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GetOTPfrStoreExeResponse.GetOTPfrStoreExeResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.ExecutiveVerifyOTPFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecutiveMobileVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etMobileNumber;
    Button btSendOTP;
    TextView tvGreeting, tvChangeMobile, tvNoticeMessage, tvNameofUser;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    LinearLayout llChangeMobile;
    String mobilefinal;
    ImageView ivTimeZone, iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executive_mobile_verify);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        sessionManager = new SessionManager(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }

    private void init() {
        etMobileNumber = findViewById(R.id.etMobileNumber);
        tvNoticeMessage = findViewById(R.id.tvNoticeMessage);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvNameofUser = findViewById(R.id.tvNameofUser);
        ivTimeZone = findViewById(R.id.ivTimeZone);
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
        if (!sessionManager.getMobile().equalsIgnoreCase("")) {
            etMobileNumber.setText(sessionManager.getMobile());
            etMobileNumber.setVisibility(View.GONE);
            etMobileNumber.setEnabled(false);
            String s = getResources().getString(R.string.noticed_error_messge) + " " + "<b>" + "+91 " + sessionManager.getMobile() + "</b> ";
            tvNoticeMessage.setText(Html.fromHtml(s));
            mobilefinal = sessionManager.getMobile();

        } else {
            tvNoticeMessage.setText(getResources().getString(R.string.noticed_error_messge));
            etMobileNumber.setVisibility(View.VISIBLE);
            etMobileNumber.setEnabled(true);

        }
        btSendOTP = findViewById(R.id.btSendOTP);
        btSendOTP.setOnClickListener(this);
        tvChangeMobile = findViewById(R.id.tvChangeMobile);
        tvChangeMobile.setOnClickListener(this);

    }


    public void commanFragmentCallWithBackStack(Fragment fragment, String otp, String mobilenumber) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putString(Constants.OTP, otp);
        bundle.putString(Constants.PARAM_MOBILE_NO, mobilenumber);

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            fragmentTransaction.replace(R.id.llMainLayout, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSendOTP:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (isValid()) {
                        if (!etMobileNumber.getText().equals("")) {
                            mobilefinal = etMobileNumber.getText().toString();
                        }
                        APiCallSendOTPStoreExecute();
                    }
                } else {
                    Constants.ShowNoInternet(this);
                }
                break;
            case R.id.tvChangeMobile:
                etMobileNumber.setEnabled(true);
                etMobileNumber.setVisibility(View.VISIBLE);
                tvNoticeMessage.setText(getResources().getString(R.string.noticed_error_messge));
                etMobileNumber.setText("");
                break;
        }
    }

    private void APiCallSendOTPStoreExecute() {
        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_MOBILE, mobilefinal);
        ApiClient.getClient().create(ApiInterface.class).updateAgentMobile("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetOTPfrStoreExeResponse>() {
            @Override
            public void onResponse(Call<GetOTPfrStoreExeResponse> call, Response<GetOTPfrStoreExeResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTPfrStoreExeResponse getOTPfrStoreExeResponse = response.body();
                    if (getOTPfrStoreExeResponse.getResponseCode() == 200) {
                        Toast.makeText(ExecutiveMobileVerifyActivity.this, getOTPfrStoreExeResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        commanFragmentCallWithBackStack(new ExecutiveVerifyOTPFragment(), getOTPfrStoreExeResponse.getData().get(0).getOtp(), etMobileNumber.getText().toString());
                    } else {
                        showMSG(false, getOTPfrStoreExeResponse.getResponse());
                    }
                } else {
                    Toast.makeText(ExecutiveMobileVerifyActivity.this, "#errorcode 2102 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOTPfrStoreExeResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ExecutiveMobileVerifyActivity.this, "#errorcode 2102 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etMobileNumber.getText())) {
            showMSG(false, "Mobile Not Valid");
            return false;
        }
        return true;
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) findViewById(R.id.txt_show_msg_fail);
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
