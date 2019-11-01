package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.utils.Validator;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMobileActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cvFirst,cvSecond;
    private EditText edit_mobile_no;
    private Button btn_next;
    private SessionManager sessionManager;
    private String TAG = "MobileFragment";
    private ProgressDialog mProgressDialog;
    VendorList vendor;
    String str_process_id;
    ImageView ivBackProcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile);
        sessionManager = new SessionManager(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendor = (VendorList) bundle.getSerializable(Constants.KEY_VENDOR_MOBILE);
            str_process_id = String.valueOf(vendor.getProccessId());
        }
        initView();
    }

    private void initView() {
      //  cvFirst = findViewById(R.id.cvfirst);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("sending OTP to mobile...");
        edit_mobile_no = findViewById(R.id.edit_mobile);
        ivBackProcess = findViewById(R.id.iv_back_process);
        ivBackProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        edit_mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Validator.isNumberLengthValid(s.toString())) {
                    btn_next.setVisibility(View.VISIBLE);
                } else {
                    btn_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if (TextUtils.isEmpty(edit_mobile_no.getText())) {
                    showMSG(false, "Mobile Not Valid");
                } else {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        getOtp(view);
                    } else {
                        Constants.ShowNoInternet(this);
                    }

                }
                break;
        }

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
        user.addProperty(Constants.PARAM_MOBILE_NO, edit_mobile_no.getText().toString().trim());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();

                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    Mylogger.getInstance().Logit(TAG, getOTP.getResponse());
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
                        Toast.makeText(ChangeMobileActivity.this, getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ChangeMobileActivity.this, ChangeMobileOTPActivity.class);
                        i.putExtra("otp", getOTP.getData().get(0).getOtp().toString());
                        i.putExtra(Constants.KEY_EMP_MOBILE, edit_mobile_no.getText().toString());
                        i.putExtra(Constants.KEY_PROCESS_ID,str_process_id);
                        startActivity(i);
                        finish();

                    } else if (getOTP.getResponseCode() == 405) {
                        sessionManager.logoutUser(ChangeMobileActivity.this);
                    }
                    else if (getOTP.getResponseCode()==411){
                        sessionManager.logoutUser(ChangeMobileActivity.this);
                    }else {
                        showMSG(false, getOTP.getResponse());
                    }
                } else {
                    showMSG(false, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(ChangeMobileActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
