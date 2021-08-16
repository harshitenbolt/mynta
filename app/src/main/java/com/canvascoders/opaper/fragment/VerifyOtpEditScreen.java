package com.canvascoders.opaper.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.OtpView.PinView;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddDeliveryBoysActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.EditRateWhileResigAgreeActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EdirRateOnBoardingActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditBankDetailsActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditGstDetailsActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditITRActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditKycActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditLocationActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditMSMEActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditOwnerInfoActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditPanCardActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditShopActImagesActivity;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditStoreInformationActivity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.fragment.AadharVerificationFragment.TAG;
import static com.canvascoders.opaper.utils.Constants.OTP_TEMPLATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyOtpEditScreen extends Fragment implements View.OnClickListener {

    View v;
    String mobilenumber = "", otp = "";
    TextView tvMobileNumber, tvTime, tv_resent_otp, tvChangeMobile;
    Button btSubmit;
    private PinView pvOTP;
    String lattitude, longitude;
    private int countDown = 30;
    ProgressDialog mProgressDialog;
    private Boolean resend = false;
    SessionManager sessionManager;
    String screenname = "";
    String proccess_id = "";

    public VerifyOtpEditScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_verify_otp_edit_screen, container, false);
        mobilenumber = getArguments().getString(Constants.KEY_EMP_MOBILE);
        otp = getArguments().getString(OTP_TEMPLATE);
        screenname = getArguments().getString(Constants.PARAM_SCREEN_NAME);
        proccess_id = getArguments().getString(Constants.KEY_PROCESS_ID);
        init();
        sessionManager = new SessionManager(getActivity());
        return v;
    }

    private void init() {
        pvOTP = v.findViewById(R.id.secondPinView);
        tvMobileNumber = v.findViewById(R.id.tvMobile);
        tvMobileNumber.setText(mobilenumber);

        btSubmit = v.findViewById(R.id.btVerify);
        btSubmit.setOnClickListener(this);
        pvOTP = v.findViewById(R.id.secondPinView);
        //txt_mobile = findViewById(R.id.tvMobile);
        tvTime = v.findViewById(R.id.tvTimeofOTP);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Verifying Mobile Number...");
        mProgressDialog.setCancelable(false);
        tv_resent_otp = (TextView) v.findViewById(R.id.tvResend);
        tvChangeMobile = v.findViewById(R.id.tvChangeMobile);
        tvChangeMobile.setOnClickListener(this);

        // btn_next = findViewById(R.id.btVerify);

        tv_resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobilenumber.length() < 10) {
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
                            getOtp(v);
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }

                        resend = true;
                    } else {
                        Toast.makeText(getActivity(), "Try after " + countDown + " Seconds", Toast.LENGTH_SHORT).show();
                        //showMSG(true, "Try after " + countDown + " Seconds");
                    }
                }
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
                                lattitude = String.valueOf(gps.getLatitude());
                                longitude = String.valueOf(gps.getLongitude());
                                Log.e("Lattitude", lattitude);
                                Log.e("Longitude", longitude);


                            } else {

                                gps.showSettingsAlert();
                            }
                            //  getMobileDetails(v);
                            gotoscreen();


                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }
                    } else {
                        Toast.makeText(getActivity(), "OTP not matched", Toast.LENGTH_SHORT).show();
                        //showMSG(false, "OTP not matched");
                    }
                }

                break;

        }

    }

    private void gotoscreen() {
        Intent i;
        if (screenname.equalsIgnoreCase("Location")) {
            i = new Intent(getActivity(), EditLocationActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("KYC")) {
            i = new Intent(getActivity(), EditKycActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("PAN")) {
            i = new Intent(getActivity(), EditPanCardActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("CHEQUE")) {
            i = new Intent(getActivity(), EditBankDetailsActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("ITR")) {
            i = new Intent(getActivity(), EditITRActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("STORE")) {
            i = new Intent(getActivity(), EditStoreInformationActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("OWNER")) {
            i = new Intent(getActivity(), EditOwnerInfoActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }

        if (screenname.equalsIgnoreCase("MSME")) {
            i = new Intent(getActivity(), EditMSMEActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("GSTN")) {
            i = new Intent(getActivity(), EditGstDetailsActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }
        if (screenname.equalsIgnoreCase("DELIVERY")) {
            i = new Intent(getActivity(), AddDeliveryBoysActivity.class);
            i.putExtra(Constants.DATA, proccess_id);
            i.putExtra(Constants.KEY_EDIT_DETAIL, "1");
            startActivity(i);
            getActivity().finish();

        }
        if (screenname.equalsIgnoreCase("RATEUPDATE")) {
            i = new Intent(getActivity(), EdirRateOnBoardingActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();

        }

        if (screenname.equalsIgnoreCase("DOCUPLOAD")) {
            i = new Intent(getActivity(), EditShopActImagesActivity.class);
            i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
            startActivity(i);
            getActivity().finish();
        }

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

    private boolean validate(View v) {
        return true;
    }

    private void getOtp(final View v) {

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_MOBILE_NO, mobilenumber.trim());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
                        // Toast.makeText(getApplicationContext(), getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        otp = getOTP.getData().get(0).getOtp();
                    } else {

                        if (getOTP.getResponseCode() == 405) {
                            sessionManager.logoutUser(getActivity());
                        } else if (getOTP.getResponseCode() == 411) {
                            sessionManager.logoutUser(getActivity());
                        } else {
                            Toast.makeText(getActivity(), getOTP.getResponse(), Toast.LENGTH_SHORT).show();
                            //   showMSG(false, getOTP.getResponse());
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2012 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode :- 2012 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }


}
