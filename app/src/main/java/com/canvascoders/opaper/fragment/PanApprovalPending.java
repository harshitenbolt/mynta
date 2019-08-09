package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.CommonResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.activity.EditPanCardActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class PanApprovalPending extends Fragment {

    Context mcontext;
    View view;
    TextView tvMessage;
    String message = "";
    String processID = "";
    private Button btResend, btCheckSign;
    ProgressDialog mProgressDialog;
    SessionManager sessionManager;
    Button btn_onboard;
    private ImageView ivAgreeStatus, ivAssessStatus;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    int pL, pT, pR, pB;
    LinearLayout llAgreement, llAssesment;
    private int countDown = 30;
    private Boolean resend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pan_approval, container, false);
        mcontext = this.getActivity();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }


    private void init() {

        tvMessage = view.findViewById(R.id.lbl_your_name);
        btCheckSign = view.findViewById(R.id.btnCheckStep);
        btResend = view.findViewById(R.id.btResend);
        ivAgreeStatus = view.findViewById(R.id.ivAgreeStatus);
        ivAssessStatus = view.findViewById(R.id.ivAsessStatus);

        llAssesment = view.findViewById(R.id.llAssessment);
        llAgreement = view.findViewById(R.id.llAgreement);

        pL = llAgreement.getPaddingLeft();
        pT = llAgreement.getPaddingTop();
        pR = llAgreement.getPaddingRight();
        pB = llAgreement.getPaddingBottom();


        if (message != null && message.length() > 0)
            tvMessage.setText(message);

        btn_onboard = (Button) view.findViewById(R.id.btnDashboard);

        btn_onboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DashboardActivity.class);
                startActivity(intent);
            }
        });


        if (processID != null && processID.length() > 0) {
            ApiCallGetDetails();
        }


        btCheckSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCallGetDetails();
            }
        });


        btResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resend == false) {
                    new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            countDown--;
                            btResend.setText("00:" + countDown);
                        }

                        public void onFinish() {
                            resend = false;
                            countDown = 30;
                            btResend.setText("Resend");
                        }
                    }.start();
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiSendMessage();
                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }

                    resend = true;
                }


            }
        });

    }

    private void ApiCallGetDetails() {
        mProgressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PROCESS_ID, processID);
        Call<UpdatePanDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).pandetailResponse("Bearer " + sessionManager.getToken(), param);
        call.enqueue(new Callback<UpdatePanDetailResponse>() {
            @Override
            public void onResponse(Call<UpdatePanDetailResponse> call, retrofit2.Response<UpdatePanDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    UpdatePanDetailResponse updatePanDetailResponse = response.body();
                    if (updatePanDetailResponse.getResponseCode() == 200) {

                    } else if (updatePanDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(getActivity());
                    } else if (updatePanDetailResponse.getResponseCode() == 202) {
                        tvMessage.setText(updatePanDetailResponse.getResponse());
                        if (updatePanDetailResponse.getStatus().equalsIgnoreCase("0")) {
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));

                            } else {
                                ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                llAgreement.setBackgroundResource(R.drawable.roundedcornergrey);
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                llAssesment.setPadding(pL, pT, pR, pB);
                            }


                         //   btResend.setVisibility(View.GONE);

                        } else if (updatePanDetailResponse.getStatus().equalsIgnoreCase("1")) {

                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                                ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending));
                                llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedcornergrey));
                                llAssesment.setPadding(pL, pT, pR, pB);

                            } else {
                                ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                llAgreement.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.pending));
                                llAssesment.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                                llAssesment.setPadding(pL, pT, pR, pB);
                            }
                            btResend.setVisibility(View.VISIBLE);
                        } else if (updatePanDetailResponse.getStatus().equalsIgnoreCase("2")) {

                           /* if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                                ivAgreeStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                llAgreement.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencheck));
                                llAssesment.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                llAssesment.setPadding(pL, pT, pR, pB);
                            } else {
                                ivAgreeStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                llAgreement.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                                llAgreement.setPadding(pL, pT, pR, pB);

                                ivAssessStatus.setBackground(getResources().getDrawable(R.drawable.greencheck));
                                llAssesment.setBackground(getResources().getDrawable(R.drawable.rounded_corner_bordercolor_green));
                                llAssesment.setPadding(pL, pT, pR, pB);
                            }*/

                           getActivity().finish();

                        }
                        else {

                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePanDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }


    private void ApiSendMessage() {
        mProgressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PROCESS_ID, processID);
        Call<CommonResponse> call = ApiClient.getClient().create(ApiInterface.class).sendmsgForAgreementLink("Bearer " + sessionManager.getToken(), param);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    CommonResponse updatePanDetailResponse = response.body();
                    if (updatePanDetailResponse.getResponseCode() == 200) {
                        /* *//* tvPanName.setText(updatePanDetailResponse.getData().get(0).getPanName());
                        tvPanFatherName.setText(updatePanDetailResponse.getData().get(0).getFatherName());
                        tvPanNo.setText(updatePanDetailResponse.getData().get(0).getPanNo());*//*
                        Glide.with(getActivity()).load(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan()).into(ivStoreImage);
                        *///isPanSelected = true;
                        // ivPanImageSelected.setVisibility(View.VISIBLE);
                       /* etpanNumber.setEnabled(true);
                        etFatherName.setEnabled(true);
                        etPanName.setEnabled(true);*/
                        //setButtonImage();

                       /* Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    Bitmap bitmap = ImagePicker.getBitmapFromURL(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan());
                                    panImagepath = ImagePicker.getBitmapPath(bitmap, getApplicationContext());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();*/

                       /* try {

                            Bitmap bitmap = ImagePicker.getBitmapFromURL(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan());
                            panImagepath = ImagePicker.getBitmapPath(bitmap, EditPanCardActivity.this);

                        }
                        catch (Exception e){
                       //     Toast.makeText(EditPanCardActivity.this, "Server has no image ", Toast.LENGTH_SHORT).show();
                        }                     */   // ExtractPanDetail();
                    } else if (updatePanDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(getActivity());
                    } else if (updatePanDetailResponse.getResponseCode() == 202) {


                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    public void setMesssge(String string, String processId) {
        message = string;
        processID = processId;
    }


}