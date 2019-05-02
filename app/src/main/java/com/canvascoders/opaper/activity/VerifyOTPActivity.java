package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GenerateResetPWResponse.Datum;
import com.canvascoders.opaper.Beans.GenerateResetPWResponse.GenerateResetPWResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private EditText edt_otp_1;
    private EditText edt_otp_2;
    private EditText edt_otp_3;
    private EditText edt_otp_4;
    private String otp, mobile;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton btVerify;
    TextView tvMobile,tvResend;
    Datum datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        datum = (Datum) getIntent().getSerializableExtra(Constants.DATA);
        otp = String.valueOf(datum.getOtp());
        mobile = datum.getAgentDetail().getMobile();
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(VerifyOTPActivity.this);
        mProgressDialog.setMessage("Verifying Mobile Number...");
        tvMobile = findViewById(R.id.tvMobile);
        edt_otp_1 = (EditText) findViewById(R.id.edt_otp_1);
        edt_otp_2 = (EditText) findViewById(R.id.edt_otp_2);
        edt_otp_3 = (EditText) findViewById(R.id.edt_otp_3);
        edt_otp_4 = (EditText) findViewById(R.id.edt_otp_4);
        tvMobile.setText("+91 " + mobile);
        btVerify = findViewById(R.id.fbVerify);
        tvResend = findViewById(R.id.tv_resend);
        tvResend.setOnClickListener(this);
        btVerify.setOnClickListener(this);
        edt_otp_1.addTextChangedListener(this);
        edt_otp_2.addTextChangedListener(this);
        edt_otp_3.addTextChangedListener(this);
        edt_otp_4.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence == edt_otp_1.getEditableText() && i2 != 0) {
            edt_otp_2.requestFocus();
        } else if (charSequence == edt_otp_2.getEditableText() && i2 != 0) {
            edt_otp_3.requestFocus();
        } else if (charSequence == edt_otp_3.getEditableText() && i2 != 0) {
            edt_otp_4.requestFocus();
        } else if (charSequence == edt_otp_4.getEditableText() && i2 != 0) {
            hideKeyboardwithoutPopulate(VerifyOTPActivity.this);
        }
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fbVerify:
                if (validate(view)) {
                    String userOTp = edt_otp_1.getText().toString() + edt_otp_2.getText().toString() + edt_otp_3.getText().toString() + edt_otp_4.getText().toString();
                    if (userOTp.equals(otp)) {
                        Toast.makeText(this, "OTP Matched !!!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(VerifyOTPActivity.this,ResetPasswordActivity.class);
                        i.putExtra(Constants.DATA,datum);
                        startActivity(i);
                    } else {
                        edt_otp_1.setText("");
                        edt_otp_2.setText("");
                        edt_otp_3.setText("");
                        edt_otp_4.setText("");
                        edt_otp_1.requestFocus();
                        showMSG(false, "OTP not matched");
                    }
                }
                break;
            case R.id.tv_resend:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSendOtp();
                }
                else{
                    Constants.ShowNoInternet(VerifyOTPActivity.this);

                }
        }
    }


    private boolean validate(View v) {
        if (TextUtils.isEmpty(edt_otp_1.getText().toString())) {
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
        }
        return true;
    }


    private void ApiCallSendOtp() {
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).sendCodeReset(mobile).enqueue(new Callback<GenerateResetPWResponse>() {
            @Override
            public void onResponse(Call<GenerateResetPWResponse> call, Response<GenerateResetPWResponse> response) {
                mProgressDialog.dismiss();
                if(response.isSuccessful()){
                    GenerateResetPWResponse generateResetPWResponse = response.body();
                    if(generateResetPWResponse.getResponseCode()==200){
                        Toast.makeText(VerifyOTPActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        datum = generateResetPWResponse.getData().get(0);
                        otp = String.valueOf(datum.getOtp());

                    }
                    else{
                        Toast.makeText(VerifyOTPActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenerateResetPWResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
