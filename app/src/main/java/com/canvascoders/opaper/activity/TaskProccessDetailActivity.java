package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.GetTrackingDetailResponse.GetTrackDetailsResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EdirRateOnBoardingActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskProccessDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvAddNewGST;
    ImageView ivEditLocation, ivEditKyc, ivEditPan, ivEditBank, ivEditStoreDetails, ivOwnerDetails, ivGstInformation, ivShopActEdit, ivDeliveryDetails, ivRateDetails;
    String proccess_id = "";
    NetworkConnectivity networkConnectivity;
    LinearLayout llShopActDisable, llShopActEnable, llLocationEnable, llLocatonDisable, llKyCEnable, llKYCDisable, llPanEnable, llPanDisable, llChequeEnable, llChequeDisable, llInformationEnable, llVendorDetailsEnable, llVendorsDetailsDisable, llOwnerDisable, llOwnerEnable, llDocumentsDisable, llDocumentsEnable, llDeliveryBoysEnable, llDeliveryBoysDisable, llRateDetailsEnable, llRateDetailsDisable, llRateApprovalEnable, llRateApprovalDisable, llAgreementEnable, llAgreementDisable, llGstEnable, llGstDisable;
    SessionManager sessionManager;
    TextView tvSHopActText, tvLocationText, tvKYCText, tvPanText, tvBankText, tvStoreText, tvOwnerText, tvGstText, tvDeliveryText, tvRateText, tvRateApprovalText;
    TextView tvLattitude, tvLongitude, tvDocNumber, tvDocAddress, tvDocName, tvPanName, tvPanNumber, tvPanDOB, tvBankAccName, tvBankAcccountNumber, tvBankBranchName, tvStoreName, tvStoreAddress, tvStoreDCName, tvOwnerNAme, tvOwnerAddress, tvOwnerEmailID, tvGSTNumber, tvGStPanNumber, tvNoDeliveryBoysAdded, tvNoofRates;
    ProgressDialog progressDialog;
    ImageView ivAdharFront, ivAdharBack, ivPanFront, ivGstFront;
    String mobile = "";

    TextView tvRemarkLocation, tvRemarkKYC, tvRemarkPan, tvRemarkBank, tvRemarkStore, tvRemarkOwner, tvRemarkGST, tvRemarkShopAct, tvRemarkDelivery, tvRemarkRateDetails, tvRemarkRateApproval;
    ImageView ivChequeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_proccess_detail);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        proccess_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        Log.e("process_id", proccess_id);
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.iv_back_process);
        tvAddNewGST = findViewById(R.id.tvAddGST);
        tvAddNewGST.setOnClickListener(this);
        networkConnectivity = new NetworkConnectivity(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivEditLocation = findViewById(R.id.ivEditLocation);
        ivEditKyc = findViewById(R.id.ivEditKYC);
        ivEditPan = findViewById(R.id.ivEditPan);
        ivEditBank = findViewById(R.id.ivEditCheque);
        ivEditStoreDetails = findViewById(R.id.ivEditStoreDetails);
        ivOwnerDetails = findViewById(R.id.ivEditOwnerDetails);
        ivGstInformation = findViewById(R.id.ivEditGSTDetails);
        ivShopActEdit = findViewById(R.id.ivEditShopAct);
        ivShopActEdit.setOnClickListener(this);
        ivGstFront = findViewById(R.id.ivGstFront);
        ivDeliveryDetails = findViewById(R.id.ivDeliveryDetails);
        ivRateDetails = findViewById(R.id.ivRateDetails);
        ivChequeImage = findViewById(R.id.ivChequeImage);
        ivRateDetails.setOnClickListener(this);
        ivEditLocation.setOnClickListener(this);
        ivEditKyc.setOnClickListener(this);
        ivEditPan.setOnClickListener(this);
        ivEditBank.setOnClickListener(this);
        ivEditStoreDetails.setOnClickListener(this);
        ivOwnerDetails.setOnClickListener(this);
        ivGstInformation.setOnClickListener(this);
        ivDeliveryDetails.setOnClickListener(this);
        llShopActDisable = findViewById(R.id.llShopActDisable);
        llShopActEnable = findViewById(R.id.llShopActEnable);

        llShopActEnable.setOnClickListener(this);
        llShopActDisable.setOnClickListener(this);
        llLocationEnable = findViewById(R.id.llLocationEnable);
        llLocatonDisable = findViewById(R.id.llLocationDisable);
        llKyCEnable = findViewById(R.id.llKYCENable);
        llKYCDisable = findViewById(R.id.llKycDisable);
        llPanEnable = findViewById(R.id.llPanEnable);
        llPanDisable = findViewById(R.id.llPanDisable);
        llChequeEnable = findViewById(R.id.llBankEnable);
        llChequeDisable = findViewById(R.id.llChequeDisable);
        llOwnerEnable = findViewById(R.id.llOwnerEnable);
        llOwnerDisable = findViewById(R.id.llOwnerDisable);
        llVendorDetailsEnable = findViewById(R.id.llDetailsEnable);
        llVendorsDetailsDisable = findViewById(R.id.llDetailsDisable);
        llDeliveryBoysEnable = findViewById(R.id.llDelBoysEnable);
        llDeliveryBoysDisable = findViewById(R.id.llDeliveryBoysDisable);
        llRateDetailsEnable = findViewById(R.id.llRateEnable);
        llRateDetailsDisable = findViewById(R.id.llRateDisable);
        llRateApprovalEnable = findViewById(R.id.llRateApprovalEnable);
        llRateApprovalDisable = findViewById(R.id.llRateApprovalDisable);
        llGstEnable = findViewById(R.id.llGStEnable);
        llGstDisable = findViewById(R.id.llGstDisable);


        tvGSTNumber = findViewById(R.id.tvGstNo);
        tvGStPanNumber = findViewById(R.id.tvGstPanNumber);
        tvLocationText = findViewById(R.id.tvTextStepLocation);
        tvSHopActText = findViewById(R.id.tvTextStepShopAct);
        tvKYCText = findViewById(R.id.tvTextStepKYC);
        tvPanText = findViewById(R.id.tvTextStepPan);
        tvBankText = findViewById(R.id.tvTextStepCheque);
        tvStoreText = findViewById(R.id.tvTextStepStoreAddress);
        tvOwnerText = findViewById(R.id.tvTextStepOwnerInfo);
        tvGstText = findViewById(R.id.tvTextStepGST);
        tvDeliveryText = findViewById(R.id.tvTextStepDeliveryBoys);
        tvRateText = findViewById(R.id.tvRateDetailsText);
        tvRateApprovalText = findViewById(R.id.tvTextStepRateApproval);
        tvLattitude = findViewById(R.id.tvLattitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvDocNumber = findViewById(R.id.tvDocKYCNumber);
        tvDocName = findViewById(R.id.tvDocKYCName);
        tvDocAddress = findViewById(R.id.tvDocKYCAddress);
        tvPanNumber = findViewById(R.id.tvPanNumber);
        tvPanName = findViewById(R.id.tvPanName);
        tvPanDOB = findViewById(R.id.tvPanDOB);
        tvGstText = findViewById(R.id.tvTextStepGST);
        tvBankAccName = findViewById(R.id.tvAccNameBank);
        tvBankAcccountNumber = findViewById(R.id.tvBankAccNumber);
        tvBankBranchName = findViewById(R.id.tvBranchName);
        tvOwnerNAme = findViewById(R.id.tvOwnerName);
        tvOwnerAddress = findViewById(R.id.tvOwnerAddress);
        tvOwnerEmailID = findViewById(R.id.tvownerEmail);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreAddress = findViewById(R.id.tvStoreAddress);
        tvStoreDCName = findViewById(R.id.tvDCStore);
        ivAdharFront = findViewById(R.id.ivKyCfront);
        ivAdharBack = findViewById(R.id.ivKYCback);
        ivPanFront = findViewById(R.id.ivPanFront);


        tvRemarkLocation = findViewById(R.id.tvTextNotLocation);
        tvRemarkKYC = findViewById(R.id.tvTextNotKYC);
        tvRemarkPan = findViewById(R.id.tvTextNotPan);
        tvRemarkBank = findViewById(R.id.tvTextNotBank);
        tvRemarkStore = findViewById(R.id.tvTextNotVendor);
        tvRemarkOwner = findViewById(R.id.tvTextOwner);
        tvRemarkGST = findViewById(R.id.tvTextNotGST);
        tvRemarkShopAct = findViewById(R.id.tvTextNotShopAct);
        tvRemarkDelivery = findViewById(R.id.tvTextNotDelivery);
        tvRemarkRateDetails = findViewById(R.id.tvTextNotRate);
        tvRemarkRateApproval = findViewById(R.id.tvTextNotRateApproval);


        tvNoDeliveryBoysAdded = findViewById(R.id.tvNoOfDeliveryBoys);
        tvNoofRates = findViewById(R.id.tvnoOfRates);
      /*  if (networkConnectivity.isNetworkAvailable()) {
            APiCallGetTrackDetails();
        } else {
            Constants.ShowNoInternet(this);
        }*/
    }

    private void APiCallGetTrackDetails() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, proccess_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).geTrackingDetails("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetTrackDetailsResponse>() {
            @Override
            public void onResponse(Call<GetTrackDetailsResponse> call, Response<GetTrackDetailsResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetTrackDetailsResponse getTrackDetailsResponse = response.body();
                    if (getTrackDetailsResponse.getResponseCode() == 200) {
                        //  Toast.makeText(TaskProccessDetailActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        //locatioon data
                        mobile = getTrackDetailsResponse.getData().get(0).getProccessDetail().getMobileNo();

                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getLocationVerify() == 1) {
                            llLocationEnable.setVisibility(View.VISIBLE);
                            llLocatonDisable.setVisibility(View.GONE);
                            tvLattitude.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getLatitude());
                            tvLongitude.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getLongitude());
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getLocationVerifyRemark().equalsIgnoreCase("")) {
                                    tvRemarkLocation.setVisibility(View.VISIBLE);
                                    tvRemarkLocation.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getLocationVerifyRemark());
                                } else {
                                    tvRemarkLocation.setVisibility(View.GONE);
                                }
                            }

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getLocationVerify() == 0) {
                            llLocatonDisable.setVisibility(View.VISIBLE);
                            llLocationEnable.setVisibility(View.GONE);
                            tvLocationText.setVisibility(View.GONE);

                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getLocationVerifyRemark().equalsIgnoreCase("")) {
                                    llLocationEnable.setVisibility(View.VISIBLE);
                                    llLocatonDisable.setVisibility(View.GONE);
                                    tvLocationText.setVisibility(View.GONE);
                                    tvRemarkLocation.setVisibility(View.VISIBLE);
                                    tvRemarkLocation.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getLocationVerifyRemark());
                                } else {
                                    tvRemarkLocation.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            llLocatonDisable.setVisibility(View.VISIBLE);
                            llLocationEnable.setVisibility(View.GONE);
                            tvLocationText.setVisibility(View.VISIBLE);
                        }


                        // KYC Verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAadhaarVerify() == 1) {
                            llKyCEnable.setVisibility(View.VISIBLE);
                            llKYCDisable.setVisibility(View.GONE);
                            if (!getTrackDetailsResponse.getData().get(0).getProccessDetail().getVoterName().equalsIgnoreCase("")) {
                                tvDocNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getVoterIdNum());
                                tvDocAddress.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getVoterDob());
                                tvDocName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getVoterName());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getVoterCardFront()).into(ivAdharFront);
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getVoterCardFront()).into(ivAdharBack);

                            } else if (!getTrackDetailsResponse.getData().get(0).getProccessDetail().getDlName().equalsIgnoreCase("")) {
                                tvDocNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getDlNumber());
                                tvDocAddress.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getDlDob());
                                tvDocName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getDlName());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getDlCardFront()).into(ivAdharFront);
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getDlCardBack()).into(ivAdharBack);

                            } else {
                                tvDocNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarNo());
                                tvDocAddress.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarDob());
                                tvDocName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarName());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getAdharCardFront()).into(ivAdharFront);
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getAdharCardBack()).into(ivAdharBack);

                            }

                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkKYC.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getAadhaarVerifyRemark());
                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getAadhaarVerifyRemark().equalsIgnoreCase("")) {
                                    tvRemarkKYC.setVisibility(View.VISIBLE);
                                    tvRemarkKYC.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getAadhaarVerifyRemark());
                                } else {
                                    tvRemarkKYC.setVisibility(View.GONE);
                                }


                            }
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAadhaarVerify() == 0) {
                            llKyCEnable.setVisibility(View.GONE);
                            llKYCDisable.setVisibility(View.VISIBLE);
                            tvKYCText.setVisibility(View.GONE);

                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getAadhaarVerifyRemark().equalsIgnoreCase("")) {
                                    llKyCEnable.setVisibility(View.VISIBLE);
                                    llKYCDisable.setVisibility(View.GONE);
                                    tvKYCText.setVisibility(View.GONE);
                                    tvRemarkKYC.setVisibility(View.VISIBLE);
                                    tvRemarkKYC.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getAadhaarVerifyRemark());
                                } else {
                                    tvRemarkKYC.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            llKyCEnable.setVisibility(View.GONE);
                            llKYCDisable.setVisibility(View.VISIBLE);
                            tvKYCText.setVisibility(View.VISIBLE);
                        }


                        // Pan Card Verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getPanVerify() == 1) {
                            llPanDisable.setVisibility(View.GONE);
                            llPanEnable.setVisibility(View.VISIBLE);
                            tvPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                            tvPanName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanName());
                            tvPanDOB.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getNsdlPanName());
                            Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getPan()).into(ivPanFront);
                            // Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().get()).into(ivAdharBack);
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                //  tvRemarkKYC.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getPanVerifyRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getPanVerifyRemark().equalsIgnoreCase("")) {
                                    //tvRemarkPan.setVisibility(View.VISIBLE);
                                    tvPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                    tvPanName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanName());
                                    tvPanDOB.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getNsdlPanName());
                                    Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getPan()).into(ivPanFront);
                                    tvRemarkPan.setVisibility(View.VISIBLE);
                                    tvRemarkPan.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getPanVerifyRemark());
                                } else {
                                    tvRemarkPan.setVisibility(View.GONE);
                                }


                            }


                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getPanVerify() == 0) {
                            llPanDisable.setVisibility(View.VISIBLE);
                            llPanEnable.setVisibility(View.GONE);
                            tvPanText.setVisibility(View.GONE);
/*
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getPanVerifyRemark().equalsIgnoreCase("")) {
                                    llPanEnable.setVisibility(View.VISIBLE);
                                    tvPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                    tvPanName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanName());
                                    tvPanDOB.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getNsdlPanName());
                                    Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getPan()).into(ivPanFront);
                                    llPanDisable.setVisibility(View.GONE);
                                    tvPanText.setVisibility(View.GONE);
                                    tvRemarkPan.setVisibility(View.VISIBLE);
                                    tvRemarkPan.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getPanVerifyRemark());
                                }
                            }*/
                        } else {
                            llPanDisable.setVisibility(View.VISIBLE);
                            llPanEnable.setVisibility(View.GONE);
                            tvPanText.setVisibility(View.VISIBLE);
                        }


                        //cheque verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getChequeVerify() == 1) {
                            llChequeEnable.setVisibility(View.VISIBLE);
                            llChequeDisable.setVisibility(View.GONE);

                            tvBankAcccountNumber.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankAc());
                            tvBankAccName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankName());
                            tvBankBranchName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankBranchName());
                            Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getCancelledCheques().get(0).getCancelledCheque()).into(ivChequeImage);
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkBank.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getChequeVerifyRemark());
                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getChequeVerifyRemark().equalsIgnoreCase("")) {
                                    llChequeEnable.setVisibility(View.VISIBLE);
                                    tvBankAcccountNumber.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankAc());
                                    tvBankAccName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankName());
                                    tvBankBranchName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankBranchName());
                                    Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getCancelledCheques().get(0).getCancelledCheque()).into(ivChequeImage);
                                    llChequeDisable.setVisibility(View.GONE);
                                    tvBankText.setVisibility(View.GONE);
                                    tvRemarkBank.setVisibility(View.VISIBLE);
                                    tvRemarkBank.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getChequeVerifyRemark());
                                } else {
                                    tvRemarkBank.setVisibility(View.GONE);
                                }

                            }

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getChequeVerify() == 0) {
                            llChequeEnable.setVisibility(View.GONE);
                            llChequeDisable.setVisibility(View.VISIBLE);
                            tvBankText.setVisibility(View.GONE);

                           /* if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getChequeVerifyRemark().equalsIgnoreCase("")) {
                                    llChequeEnable.setVisibility(View.VISIBLE);
                                    tvBankAcccountNumber.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankAc());
                                    tvBankAccName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankName());
                                    tvBankBranchName.setText(getTrackDetailsResponse.getData().get(0).getBankDetails().get(0).getBankBranchName());
                                    Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getCancelledCheques().get(0).getCancelledCheque()).into(ivChequeImage);
                                    llChequeDisable.setVisibility(View.GONE);
                                    tvBankText.setVisibility(View.GONE);
                                    tvRemarkBank.setVisibility(View.VISIBLE);
                                    tvRemarkBank.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getChequeVerifyRemark());
                                }
                            }*/
                        } else {
                            llChequeEnable.setVisibility(View.GONE);
                            llChequeDisable.setVisibility(View.VISIBLE);
                            tvBankText.setVisibility(View.VISIBLE);
                        }


                        //gstn

                        if (getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn().equalsIgnoreCase("")) {

                            if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillGstDetails() == 2) {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.VISIBLE);

                            } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillGstDetails() == 1) {
                                llGstDisable.setVisibility(View.GONE);
                                llGstEnable.setVisibility(View.VISIBLE);
                                ivGstInformation.setVisibility(View.GONE);
                                tvAddNewGST.setVisibility(View.VISIBLE);
                                tvGSTNumber.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getGstCertificateImage()).into(ivGstFront);
                                tvGStPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                tvGstText.setVisibility(View.GONE);
                                if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark().equalsIgnoreCase("")) {
                                        llGstEnable.setVisibility(View.VISIBLE);
                                        tvGSTNumber.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn());
                                        Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getGstCertificateImage()).into(ivGstFront);
                                        tvGStPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                        llGstDisable.setVisibility(View.GONE);
                                        tvGstText.setVisibility(View.GONE);
                                        tvRemarkGST.setVisibility(View.VISIBLE);
                                        tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());
                                    } else {
                                        tvRemarkGST.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.GONE);

                            }
                        } else {
                            if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillGstDetails() == 2) {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.VISIBLE);
                            } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillGstDetails() == 1) {
                                llGstDisable.setVisibility(View.GONE);
                                llGstEnable.setVisibility(View.VISIBLE);
                                tvGstText.setVisibility(View.GONE);
                                tvGSTNumber.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getGstCertificateImage()).into(ivGstFront);
                                tvGStPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark().equalsIgnoreCase("")) {
                                        llGstEnable.setVisibility(View.VISIBLE);
                                        tvGSTNumber.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn());
                                        Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getGstCertificateImage()).into(ivGstFront);
                                        tvGStPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                                        llGstDisable.setVisibility(View.GONE);
                                        tvGstText.setVisibility(View.GONE);
                                        tvRemarkGST.setVisibility(View.VISIBLE);
                                        tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());
                                    } else {
                                        tvRemarkGST.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.GONE);
                                /* */

                            }


                        }


                        // Store
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillStoreDetails() == 1) {
/*
                            llOwnerEnable.setVisibility(View.VISIBLE);
                            llOwnerDisable.setVisibility(View.GONE);
                            tvOwnerNAme.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getOwnerName());
                            tvOwnerEmailID.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getEmail());
                            tvOwnerAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress1() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressLandmark() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressCity() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressState() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressPicode());
*/


                            llVendorDetailsEnable.setVisibility(View.VISIBLE);
                            llVendorsDetailsDisable.setVisibility(View.GONE);
                            tvStoreName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreName());
                            tvStoreAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress1() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddressLandmark() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getCity() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getState() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getPincode());
                            tvStoreDCName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getDc());


                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                //  tvRemarkOwner.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark());
                                // tvRemarkStore.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark());
                                //  tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());


                                if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark().equalsIgnoreCase("")) {
                                      /*  llOwnerEnable.setVisibility(View.VISIBLE);
                                        tvOwnerNAme.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getOwnerName());
                                        tvOwnerEmailID.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getEmail());
                                        tvOwnerAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress1() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressLandmark() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressCity() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressState() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressPicode());
*/
                                 /*       llOwnerDisable.setVisibility(View.GONE);
                                        tvOwnerText.setVisibility(View.GONE);
                                        tvRemarkOwner.setVisibility(View.VISIBLE);
                                        tvRemarkOwner.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark());
                                 */
                                    }
                                }


                                if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark().equalsIgnoreCase("")) {
                                        llVendorDetailsEnable.setVisibility(View.VISIBLE);
                                        tvStoreName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreName());
                                        tvStoreAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress1() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddressLandmark() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getCity() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getState() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getPincode());
                                        tvStoreDCName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getDc());
                                        llVendorsDetailsDisable.setVisibility(View.GONE);
                                        tvStoreText.setVisibility(View.GONE);
                                        tvRemarkStore.setVisibility(View.VISIBLE);
                                        tvRemarkStore.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark());
                                    } else {
                                        tvRemarkStore.setVisibility(View.GONE);
                                    }
                                }

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark().equalsIgnoreCase("")) {
                                    tvRemarkGST.setVisibility(View.VISIBLE);
                                    tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());
                                }


                            }


                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillStoreDetails() == 0) {
                            /*llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.GONE);
*/
                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.GONE);


                        } else {
                           /* llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.VISIBLE);
*/
                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.VISIBLE);
                        }

                        // Owner
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillOwnerDetails() == 1) {
                            llOwnerEnable.setVisibility(View.VISIBLE);
                            llOwnerDisable.setVisibility(View.GONE);
                            tvOwnerNAme.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getOwnerName());
                            tvOwnerEmailID.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getEmail());
                            tvOwnerAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress1() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressLandmark() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressCity() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressState() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressPicode());

/*

                            llVendorDetailsEnable.setVisibility(View.VISIBLE);
                            llVendorsDetailsDisable.setVisibility(View.GONE);
                            tvStoreName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreName());
                            tvStoreAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress1() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddressLandmark() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getCity() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getState() + " " +
                                    getTrackDetailsResponse.getData().get(0).getBasicDetails().getPincode());
                            tvStoreDCName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getDc());
*/


                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                //  tvRemarkOwner.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark());
                                // tvRemarkStore.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark());
                                //  tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());


                                if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark().equalsIgnoreCase("")) {
                                        llOwnerEnable.setVisibility(View.VISIBLE);
                                        tvOwnerNAme.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getOwnerName());
                                        tvOwnerEmailID.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getEmail());
                                        tvOwnerAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddress1() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressLandmark() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressCity() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressState() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getResidentialAddressPicode());
                                        llOwnerDisable.setVisibility(View.GONE);
                                        tvOwnerText.setVisibility(View.GONE);
                                        tvRemarkOwner.setVisibility(View.VISIBLE);
                                        tvRemarkOwner.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillOwnerDetailsRemark());
                                    } else {
                                        tvRemarkOwner.setVisibility(View.GONE);
                                    }
                                }


                             /*   if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                    // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                    if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark().equalsIgnoreCase("")) {
                                        llVendorDetailsEnable.setVisibility(View.VISIBLE);
                                        tvStoreName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreName());
                                        tvStoreAddress.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddress1() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getStoreAddressLandmark() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getCity() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getState() + " " +
                                                getTrackDetailsResponse.getData().get(0).getBasicDetails().getPincode());
                                        tvStoreDCName.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getDc());
                                        llVendorsDetailsDisable.setVisibility(View.GONE);
                                        tvStoreText.setVisibility(View.GONE);
                                        tvRemarkStore.setVisibility(View.VISIBLE);
                                        tvRemarkStore.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillStoreDetailsRemark());
                                    }
                                }*/

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark().equalsIgnoreCase("")) {
                                    tvRemarkGST.setVisibility(View.VISIBLE);
                                    tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());
                                } else {
                                    tvRemarkGST.setVisibility(View.GONE);
                                }


                            }


                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillOwnerDetails() == 0) {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.GONE);
                        /*    llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.GONE);*/


                        } else {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.VISIBLE);
                            /*llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.VISIBLE);*/
                        }


                        // Upload Files
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getUploadFiles() == 1) {
                            llShopActEnable.setVisibility(View.VISIBLE);
                            llShopActDisable.setVisibility(View.GONE);

                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getUploadFilesRemark().equalsIgnoreCase("")) {
                                    llShopActEnable.setVisibility(View.VISIBLE);
                                    llShopActDisable.setVisibility(View.GONE);
                                    tvSHopActText.setVisibility(View.GONE);
                                    tvRemarkShopAct.setVisibility(View.VISIBLE);
                                    tvRemarkShopAct.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getUploadFilesRemark());
                                } else {
                                    tvRemarkShopAct.setVisibility(View.GONE);
                                }
                            }


                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getUploadFiles() == 0) {
                            llShopActEnable.setVisibility(View.GONE);
                            llShopActDisable.setVisibility(View.VISIBLE);
                            tvSHopActText.setVisibility(View.GONE);

                        } else {
                            tvSHopActText.setVisibility(View.VISIBLE);
                            llShopActEnable.setVisibility(View.GONE);
                            llShopActDisable.setVisibility(View.VISIBLE);
                        }

                        // Delivery Boys Details
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getDeliveryBoy() == 1) {
                            llDeliveryBoysDisable.setVisibility(View.GONE);
                            llDeliveryBoysEnable.setVisibility(View.VISIBLE);
                            tvNoDeliveryBoysAdded.setText(getTrackDetailsResponse.getData().get(0).getDeliveryBoysDetailCount() + "");

                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {
                                // tvRemarkGST.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getFillGstDetailsRemark());

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getDeliveryBoyRemark().equalsIgnoreCase("")) {
                                    llDeliveryBoysEnable.setVisibility(View.VISIBLE);
                                    tvNoDeliveryBoysAdded.setText(getTrackDetailsResponse.getData().get(0).getDeliveryBoysDetailCount() + "");
                                    llDeliveryBoysDisable.setVisibility(View.GONE);
                                    tvDeliveryText.setVisibility(View.GONE);
                                    tvRemarkDelivery.setVisibility(View.VISIBLE);
                                    tvRemarkDelivery.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getDeliveryBoyRemark());
                                } else {
                                    tvRemarkDelivery.setVisibility(View.GONE);
                                }
                            }

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getDeliveryBoy() == 0) {
                            llDeliveryBoysDisable.setVisibility(View.VISIBLE);
                            llDeliveryBoysEnable.setVisibility(View.GONE);
                            tvDeliveryText.setVisibility(View.GONE);
                        } else {
                            llDeliveryBoysDisable.setVisibility(View.VISIBLE);
                            llDeliveryBoysEnable.setVisibility(View.GONE);
                            tvDeliveryText.setVisibility(View.VISIBLE);
                        }

                        //Rate

                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRate() == 1) {
                            llRateDetailsEnable.setVisibility(View.VISIBLE);
                            llRateDetailsDisable.setVisibility(View.GONE);
                            tvRateText.setText(getTrackDetailsResponse.getData().get(0).getBasicDetailRate().size() + "");
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getRateRemark().equalsIgnoreCase("")) {
                                    llRateDetailsEnable.setVisibility(View.VISIBLE);
                                    tvRateText.setText(getTrackDetailsResponse.getData().get(0).getBasicDetailRate().size() + "");
                                    llRateDetailsDisable.setVisibility(View.GONE);
                                    tvRateText.setVisibility(View.GONE);
                                    tvRemarkRateDetails.setVisibility(View.VISIBLE);
                                    tvRemarkRateDetails.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getRateRemark());
                                } else {
                                    tvRemarkRateDetails.setVisibility(View.GONE);
                                }
                            }

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRate() == 0) {
                            llRateDetailsEnable.setVisibility(View.GONE);
                            llRateDetailsDisable.setVisibility(View.VISIBLE);
                            tvRateText.setVisibility(View.GONE);


                        } else {
                            llRateDetailsEnable.setVisibility(View.GONE);
                            llRateDetailsDisable.setVisibility(View.VISIBLE);
                            tvRateText.setVisibility(View.VISIBLE);
                        }


                        // rate sent for approval
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRateSendForApproval() == 1) {
                            llRateApprovalEnable.setVisibility(View.VISIBLE);
                            llRateApprovalDisable.setVisibility(View.GONE);
                            if (getTrackDetailsResponse.getData().get(0).getVendorRejection().size() > 0) {

                                if (!getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getRateSendForApprovalRemark().equalsIgnoreCase("")) {
                                    llRateApprovalEnable.setVisibility(View.VISIBLE);
                                    llRateApprovalDisable.setVisibility(View.GONE);
                                    tvRateApprovalText.setVisibility(View.GONE);
                                    tvRemarkRateApproval.setVisibility(View.VISIBLE);
                                    tvRemarkRateApproval.setText(getTrackDetailsResponse.getData().get(0).getVendorRejection().get(0).getRateSendForApprovalRemark());
                                } else {
                                    tvRemarkRateApproval.setVisibility(View.GONE);
                                }
                            }
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRateSendForApproval() == 0) {
                            llRateApprovalEnable.setVisibility(View.GONE);
                            llRateApprovalDisable.setVisibility(View.VISIBLE);
                            tvRateApprovalText.setVisibility(View.GONE);

                        } else {
                            llRateApprovalDisable.setVisibility(View.VISIBLE);
                            llRateApprovalEnable.setVisibility(View.GONE);
                            tvRateApprovalText.setVisibility(View.VISIBLE);
                        }


                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAgreement() == 1) {

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAgreement() == 0) {

                        } else {

                        }

                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getEcomAgreement() == 1) {

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getEcomAgreement() == 0) {

                        } else {

                        }


                    } else {
                        Toast.makeText(TaskProccessDetailActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(TaskProccessDetailActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTrackDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskProccessDetailActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivEditLocation:
                Intent i = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i.putExtra(Constants.DATA, "Location");
                i.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i);
                break;
            case R.id.ivEditKYC:
                Intent i1 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i1.putExtra(Constants.DATA, "KYC");
                i1.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i1.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i1);
                break;
            case R.id.ivEditPan:
                Intent i2 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i2.putExtra(Constants.DATA, "PAN");
                i2.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i2.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i2);
                break;
            case R.id.ivEditCheque:
                Intent i3 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i3.putExtra(Constants.DATA, "CHEQUE");
                i3.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i3.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i3);
                break;
            case R.id.ivEditStoreDetails:
                Intent i4 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i4.putExtra(Constants.DATA, "STORE");
                i4.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i4.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i4);
                break;
            case R.id.ivEditOwnerDetails:
                Intent i5 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i5.putExtra(Constants.DATA, "OWNER");
                i5.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i5.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i5);
                break;
            case R.id.ivEditGSTDetails:
                Intent i6 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i6.putExtra(Constants.DATA, "GSTN");
                i6.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i6.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i6);
                break;


            case R.id.ivDeliveryDetails:
                Intent i7 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i7.putExtra(Constants.DATA, "DELIVERY");
                i7.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i7.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i7);
                break;


            case R.id.ivRateDetails:
                Intent i8 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i8.putExtra(Constants.DATA, "RATEUPDATE");
                i8.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i8.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i8);
                break;
            case R.id.ivEditShopAct:
                Intent i9 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i9.putExtra(Constants.DATA, "DOCUPLOAD");
                i9.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i9.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i9);
                break;
            case R.id.tvAddGST:
                Intent i10 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i10.putExtra(Constants.DATA, "GSTN");
                i10.putExtra(Constants.KEY_EMP_MOBILE, mobile);
                i10.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                startActivity(i10);
                break;


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkConnectivity.isNetworkAvailable()) {
            APiCallGetTrackDetails();
        } else {
            Constants.ShowNoInternet(this);
        }
    }
}
