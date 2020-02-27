package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.VerifyOtpEditScreen;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.utils.Validator;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFunctionalityKiranaActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTitle;
    String titlename = "";
    private EditText edit_mobile_no;
    private Button btn_next;
    private SessionManager sessionManager;
    private String TAG = "MobileFragment";
    private ProgressDialog mProgressDialog;
    ImageView iv_back_process;
    Context mcontext;
    View view;
    String processid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_functionality_kirana);
        titlename = getIntent().getStringExtra(Constants.DATA);
        sessionManager = new SessionManager(this);
        iv_back_process = findViewById(R.id.iv_back_process);
        iv_back_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        processid = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_title_Process);
        tvTitle.setText("Edit " + titlename);

        mProgressDialog = new ProgressDialog(EditFunctionalityKiranaActivity.this);
        mProgressDialog.setMessage("Sending OTP to mobile.");
        mProgressDialog.setCancelable(false);
        edit_mobile_no = findViewById(R.id.etMobileNumber);
        btn_next = findViewById(R.id.btSendOTP);
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

        String vendor_mobileno = getIntent().getStringExtra(Constants.KEY_EMP_MOBILE);

        if (vendor_mobileno != null) {
            edit_mobile_no.setText(vendor_mobileno);
            btn_next.setVisibility(View.VISIBLE);
        }

       /* Bundle bundle = this.getArguments();
        if (bundle != null) {
            String vendor_mobileno = bundle.getString(Constants.KEY_EMP_MOBILE);

            if (vendor_mobileno != null) {
                edit_mobile_no.setText(vendor_mobileno);
                btn_next.setVisibility(View.VISIBLE);
            }
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSendOTP) {
            if (TextUtils.isEmpty(edit_mobile_no.getText())) {
                showMSG(false, "Mobile Not Valid");
            } else {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    getOtp(v);
                } else {
                    Constants.ShowNoInternet(mcontext);
                }

            }
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
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();

                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    Mylogger.getInstance().Logit(TAG, getOTP.getResponse());
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());

                        // Toast.makeText(mcontext, getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(mcontext, OTPActivity.class);
                        i.putExtra("otp", getOTP.getData().get(0).getOtp().toString());
                        i.putExtra(Constants.KEY_EMP_MOBILE, edit_mobile_no.getText().toString());
                        startActivity(i);*/


                        commanFragmentCallWithBackStackwithData(new VerifyOtpEditScreen(), getOTP.getData().get(0).getOtp().toString(), edit_mobile_no.getText().toString());
                    } else if (getOTP.getResponseCode() == 405) {
                        sessionManager.logoutUser(mcontext);
                    } else if (getOTP.getResponseCode() == 411) {
                        sessionManager.logoutUser(mcontext);
                    } else {
                        showMSG(false, getOTP.getResponse());
                    }
                } else {
                    showMSG(false, "#errorcode :- 2012 " + getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditFunctionalityKiranaActivity.this, "#errorcode :- 2012 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                //  Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void commanFragmentCallWithBackStackwithData(Fragment fragment, String otp, String mobile) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putString(Constants.KEY_EMP_MOBILE, mobile);
        bundle.putString(Constants.OTP_TEMPLATE, otp);

        bundle.putString(Constants.PARAM_SCREEN_NAME, titlename);
        bundle.putString(Constants.KEY_PROCESS_ID, processid);



        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            fragmentTransaction.replace(R.id.rvView, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


}
