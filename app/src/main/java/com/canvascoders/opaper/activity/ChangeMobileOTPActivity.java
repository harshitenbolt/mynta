package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ChangeMobileResponse.ChangeMobileResponse;
import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMobileOTPActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private EditText edt_otp_1;
    private EditText edt_otp_2;
    private EditText edt_otp_3;
    private EditText edt_otp_4;
    private FloatingActionButton btn_next;
    private SessionManager sessionManager;
    private String TAG = "OTPActivity";
    private String otp, mobile,str_process_id;
    private TextView txt_mobile;
    private ProgressDialog mProgressDialog;
    private TextView tv_resent_otp;
    private Boolean resend = false;
    private int countDown = 30;
    static TextView tv_title;
    private Toolbar toolbar;
    private String processID;
    private String lattitude="",longitude="";
    private int chqCount = 0;
    String status_page;
    ImageView ivBack;

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile_otp);
        sessionManager = new SessionManager(ChangeMobileOTPActivity.this);
        otp = getIntent().getStringExtra("otp");
        mobile = getIntent().getStringExtra(Constants.KEY_EMP_MOBILE);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        initView();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(ChangeMobileOTPActivity.this);
        mProgressDialog.setMessage("Verifying Mobile Number...");
        tv_resent_otp = (TextView) findViewById(R.id.tv_resend);
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        edt_otp_1 = (EditText) findViewById(R.id.edt_otp_1);
        edt_otp_2 = (EditText) findViewById(R.id.edt_otp_2);
        edt_otp_3 = (EditText) findViewById(R.id.edt_otp_3);
        edt_otp_4 = (EditText) findViewById(R.id.edt_otp_4);
        edt_otp_1.addTextChangedListener(this);
        edt_otp_2.addTextChangedListener(this);
        edt_otp_3.addTextChangedListener(this);
        edt_otp_4.addTextChangedListener(this);
        txt_mobile = (TextView) findViewById(R.id.tv_mobile);
        btn_next = (FloatingActionButton) findViewById(R.id.btn_next);
        txt_mobile.setText("+91 " + mobile);
        ImageView img_edit_mobile = (ImageView) findViewById(R.id.img_edit_mobile);
        img_edit_mobile.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        edt_otp_1.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String first = edt_otp_1.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && first.trim().length() == 0) {
                    edt_otp_1.setText("");
                    edt_otp_1.requestFocus();
                }
                return false;
            }
        });
        edt_otp_2.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String second = edt_otp_2.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && second.trim().length() == 0) {
                    edt_otp_2.setText("");
                    edt_otp_1.requestFocus();

                }
                return false;
            }
        });
        edt_otp_3.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String three = edt_otp_3.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && three.trim().length() == 0) {
                    edt_otp_3.setText("");
                    edt_otp_2.requestFocus();
                }
                return false;
            }
        });
        edt_otp_4.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String four = edt_otp_4.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && four.trim().length() == 0) {
                    edt_otp_4.setText("");
                    edt_otp_3.requestFocus();
                }
                return false;
            }
        });
        tv_resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.length() < 10) {
                    showMSG(false, "Provide valid number");
                } else {
                    if (resend == false) {
                        new CountDownTimer(30000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                countDown--;
                                tv_resent_otp.setText("00:" + countDown);
                            }

                            public void onFinish() {
                                resend = false;
                                countDown = 30;
                                tv_resent_otp.setText("Resend code");
                            }
                        }.start();
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            getOtp(v);
                        } else {
                            Constants.ShowNoInternet(ChangeMobileOTPActivity.this);
                        }

                        resend = true;
                    } else {
                        showMSG(true, "Try after " + countDown + " Seconds");
                    }
                }
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


    private void getOtp(final View v) {

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_MOBILE_NO, mobile.trim());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
                        Toast.makeText(getApplicationContext(), getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        otp = getOTP.getData().get(0).getOtp();
                    } else {
                        showMSG(false, getOTP.getResponse());
                        if (getOTP.getResponseCode() == 405) {
                            sessionManager.logoutUser(ChangeMobileOTPActivity.this);
                        }
                        else if (getOTP.getResponseCode()==411){
                            sessionManager.logoutUser(ChangeMobileOTPActivity.this);
                        }
                    }
                } else {
                    showMSG(false, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (charSequence == edt_otp_1.getEditableText() && after != 0) {
            edt_otp_2.requestFocus();
        } else if (charSequence == edt_otp_2.getEditableText() && after != 0) {
            edt_otp_3.requestFocus();
        } else if (charSequence == edt_otp_3.getEditableText() && after != 0) {
            edt_otp_4.requestFocus();
        } else if (charSequence == edt_otp_4.getEditableText() && after != 0) {
            hideKeyboardwithoutPopulate(ChangeMobileOTPActivity.this);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next) {
            if (validate(view)) {
                String userOTp = edt_otp_1.getText().toString() + edt_otp_2.getText().toString() + edt_otp_3.getText().toString() + edt_otp_4.getText().toString();
                if (userOTp.equals(otp)) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                       /* GPSTracker gps = new GPSTracker(ChangeMobileOTPActivity.this);
                        if (gps.canGetLocation()) {
                            Double lat = gps.getLatitude();
                            Double lng = gps.getLongitude();
                            lattitude = String.valueOf(gps.getLatitude());
                            longitude = String.valueOf(gps.getLongitude());
                            Log.e("Lattitude", lattitude);
                            Log.e("Longitude", longitude);



                        } else {

                            gps.showSettingsAlert();
                        }*/
                        //getMobileDetails(v);
                        ApiCallChangeMobile();
                    } else {
                        Constants.ShowNoInternet(this);
                    }
                } else {
                    edt_otp_1.setText("");
                    edt_otp_2.setText("");
                    edt_otp_3.setText("");
                    edt_otp_4.setText("");
                    edt_otp_1.requestFocus();
                    showMSG(false, "OTP not matched");
                }
            }
        }
    }

    private void ApiCallChangeMobile() {

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            mProgressDialog.show();

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_AGENT_ID,sessionManager.getAgentID());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_MOBILE_NO,mobile);
            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Please wait getting details...");
            mProgressDialog.show();

            Call<ChangeMobileResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).changeMobile("Bearer " + sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<ChangeMobileResponse>() {
                @Override
                public void onResponse(Call<ChangeMobileResponse> call, Response<ChangeMobileResponse> response) {
                    mProgressDialog.dismiss();
                    if(response.isSuccessful()){

                        ChangeMobileResponse changeMobileResponse = response.body();
                        if(changeMobileResponse.getResponseCode() == 200){
                            Toast.makeText(ChangeMobileOTPActivity.this, changeMobileResponse.getResponse(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ChangeMobileOTPActivity.this,MobileChangedSuccessActivity.class);
                            i.putExtra("data",changeMobileResponse.getResponse());
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(ChangeMobileOTPActivity.this, changeMobileResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChangeMobileResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                }
            });
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
}
