package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GenerateResetPWResponse.Datum;
import com.canvascoders.opaper.Beans.GenerateResetPWResponse.GenerateResetPWResponse;
import com.canvascoders.opaper.Beans.ResetPassResponse.ResetPassResponse;
import com.canvascoders.opaper.OtpView.PinView;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.showAlert;
import static com.canvascoders.opaper.utils.Validator.isValidPassword;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener {


    private PinView pvOTP;
    private String otp, mobile;
    private ProgressDialog mProgressDialog;
    private Button btVerify;
    TextView tvMobile, tvResend, tvSendOTP;
    String email, remember_token;
    Datum datum;
    boolean showPassword = false, showCNfPw = false;
    ImageView ivShowNewPw, ivSHowCnfPw;
    EditText etNewPassword, etCnfPassword;
    private int countDown = 30;
    private LinearLayout llback;
    private Boolean resend = false;
    TextView tvLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        datum = (Datum) getIntent().getSerializableExtra(Constants.DATA);
        mProgressDialog = new ProgressDialog(this);
        otp = String.valueOf(datum.getOtp());
        mobile = datum.getAgentDetail().getMobile();
        email = datum.getAgentDetail().getEmail();
        remember_token = datum.getAgentDetail().getRememberToken();
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(VerifyOTPActivity.this);
        mProgressDialog.setMessage("Verifying Mobile Number...");
        tvSendOTP = findViewById(R.id.tvSendOtp);
        ivShowNewPw = findViewById(R.id.ivShowNewPw);
        tvLoginPage = findViewById(R.id.tvLoginPage);
        tvLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(VerifyOTPActivity.this, LoginActivity.class));
            }
        });
        llback = findViewById(R.id.llBack);
        llback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivSHowCnfPw = findViewById(R.id.ivShowPw);
        etNewPassword = findViewById(R.id.etNewPassword);
        etCnfPassword = findViewById(R.id.etPassword);

        btVerify = findViewById(R.id.btUpdate);
        pvOTP = findViewById(R.id.pvOtp);
        pvOTP.setTextColor(
                ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
        pvOTP.setTextColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorBlack, getTheme()));
        pvOTP.setLineColor(
                ResourcesCompat.getColor(getResources(), R.color.colorGrey, getTheme()));
        pvOTP.setLineColor(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorPrimary, getTheme()));
        pvOTP.setItemCount(4);
        pvOTP.setAnimationEnable(true);// start animation when adding text
        pvOTP.setCursorVisible(true);
        pvOTP.setCursorColor(
                ResourcesCompat.getColor(getResources(), R.color.line_selected, getTheme()));

        pvOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("tag", "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivShowNewPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPassword == false) {
                    showPassword = true;
                    ivShowNewPw.setImageResource(R.drawable.ic_eye);
                    etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                } else {
                    showPassword = false;
                    ivShowNewPw.setImageResource(R.drawable.ic_eye_close);
                    etNewPassword.setInputType(129);

                }
            }
        });
        ivSHowCnfPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showCNfPw == false) {
                    showCNfPw = true;
                    ivSHowCnfPw.setImageResource(R.drawable.ic_eye);
                    etCnfPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                } else {
                    showCNfPw = false;
                    ivSHowCnfPw.setImageResource(R.drawable.ic_eye_close);
                    etCnfPassword.setInputType(129);

                }
            }
        });
        pvOTP.setHideLineWhenFilled(false);
        tvResend = findViewById(R.id.tvResend);
        tvResend.setOnClickListener(this);
        btVerify.setOnClickListener(this);

        if (resend == false) {
            new CountDownTimer(30000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countDown--;
                    tvSendOTP.setText("Please wait... in 00:" + countDown);
                }

                public void onFinish() {
                    resend = false;
                    countDown = 30;
                    tvSendOTP.setVisibility(View.GONE);
                }
            }.start();
            resend = true;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btUpdate:
                if (isValid(view)) {
                    String userOTp = pvOTP.getText().toString();
                    // String userOTp = edt_otp_1.getText().toString() + edt_otp_2.getText().toString() + edt_otp_3.getText().toString() + edt_otp_4.getText().toString();
                    if (userOTp.equals(otp)) {
                       /* Toast.makeText(this, "OTP Matched !!!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(VerifyOTPActivity.this,ResetPasswordActivity.class);
                        i.putExtra(Constants.DATA,datum);
                        startActivity(i);*/
                        ApiCallReset();
                    } else {

                        showMSG(false, "OTP not matched");
                    }
                }
                break;
            case R.id.tvResend:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSendOtp();
                } else {
                    Constants.ShowNoInternet(VerifyOTPActivity.this);

                }
        }
    }


    private void ApiCallSendOtp() {
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).sendCodeReset(mobile).enqueue(new Callback<GenerateResetPWResponse>() {
            @Override
            public void onResponse(Call<GenerateResetPWResponse> call, Response<GenerateResetPWResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GenerateResetPWResponse generateResetPWResponse = response.body();
                    if (generateResetPWResponse.getResponseCode() == 200) {
                        Toast.makeText(VerifyOTPActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        datum = generateResetPWResponse.getData().get(0);
                        otp = String.valueOf(datum.getOtp());
                        email = datum.getAgentDetail().getEmail();
                        remember_token = datum.getAgentDetail().getRememberToken();
                        if (resend == false) {
                            new CountDownTimer(30000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    countDown--;
                                    tvSendOTP.setVisibility(View.VISIBLE);
                                    tvSendOTP.setText("Uploading cheque document. Please wait... in 00:" + countDown);
                                }

                                public void onFinish() {
                                    resend = false;
                                    countDown = 30;
                                    tvSendOTP.setVisibility(View.GONE);
                                }
                            }.start();
                            resend = true;
                        }

                    } else {
                        Toast.makeText(VerifyOTPActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "#errorcode :- 2013 " +getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GenerateResetPWResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(VerifyOTPActivity.this, "#errorcode :- 2013 " +getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

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


    private void ApiCallReset() {
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_EMAIL_ID, email);
        params.put(Constants.PARAM_REMEMBER_TOKEN, remember_token);
        params.put(Constants.PARAM_MOBILE_NUMBER, mobile);
        params.put(Constants.PARAM_PASSWORD, etCnfPassword.getText().toString());
        ApiClient.getClient().create(ApiInterface.class).ResetPassword(params).enqueue(new Callback<ResetPassResponse>() {
            @Override
            public void onResponse(Call<ResetPassResponse> call, Response<ResetPassResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResetPassResponse resetPassResponse = response.body();
                    if (resetPassResponse.getResponseCode() == 200) {
                        Toast.makeText(VerifyOTPActivity.this, resetPassResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, resetPassResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(VerifyOTPActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResetPassResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(VerifyOTPActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isValid(View v) {
        if (!pvOTP.getText().toString().equalsIgnoreCase("") && pvOTP.getText().toString() == null) {
            showAlert(v, "Provide OTP", false);
            return false;
        }

        if (etNewPassword.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide New Password", false);

            return false;
        }
        if (etCnfPassword.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide confirm Password", false);

            return false;
        }
        if (!etNewPassword.getText().toString().equalsIgnoreCase(etCnfPassword.getText().toString())) {
            showAlert(v, "Both password are not same", false);

            return false;
        }

      /*  if (!isValidPassword(etNewPassword.getText().toString())) {
            showAlert(v, "Provide Password in proper format ", false);
            etNewPassword.requestFocus();
            return false;
        }*/
        if (etNewPassword.getText().toString().length() < 8) {
            showAlert(v, "Password length should be eight or more", false);
            etNewPassword.requestFocus();
            return false;
        }

        return true;
    }

}
