package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.utils.Validator;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileFragment extends Fragment implements View.OnClickListener {

    private EditText edit_mobile_no;
    private Button btn_next;
    private SessionManager sessionManager;
    private String TAG = "MobileFragment";
    private ProgressDialog mProgressDialog;

    Context mcontext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mobile, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mcontext = this.getActivity();
        sessionManager = new SessionManager(mcontext);
        DashboardActivity.settitle(Constants.TITLE_MOBILE_AUTH);


        initView();


        return view;
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Sending OTP to mobile.");
        mProgressDialog.setCancelable(false);
        edit_mobile_no = view.findViewById(R.id.etMobileNumber);
        btn_next = view.findViewById(R.id.btSendOTP);
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String vendor_mobileno = bundle.getString(Constants.KEY_EMP_MOBILE);

            if (vendor_mobileno != null) {
                edit_mobile_no.setText(vendor_mobileno);
                btn_next.setVisibility(View.VISIBLE);
            }
        }

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
                        Intent i = new Intent(mcontext, OTPActivity.class);
                        i.putExtra("otp", getOTP.getData().get(0).getOtp().toString());
                        i.putExtra(Constants.KEY_EMP_MOBILE, edit_mobile_no.getText().toString());
                        startActivity(i);
                    } else if (getOTP.getResponseCode() == 405) {
                        sessionManager.logoutUser(mcontext);
                    } else if (getOTP.getResponseCode() == 411) {
                        sessionManager.logoutUser(mcontext);
                    } else {
                        showMSG(false, getOTP.getResponse());
                    }
                } else {
                    showMSG(false, "#errorcode :- 2012 " +getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode :- 2012 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

              //  Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) view.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) view.findViewById(R.id.txt_show_msg_fail);
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


}