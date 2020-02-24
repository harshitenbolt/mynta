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
    ImageView ivEditLocation, ivEditKyc, ivEditPan, ivEditBank, ivEditStoreDetails, ivOwnerDetails, ivGstInformation, ivDeliveryDetails;
    String proccess_id = "";
    NetworkConnectivity networkConnectivity;
    LinearLayout llLocationEnable, llLocatonDisable, llKyCEnable, llKYCDisable, llPanEnable, llPanDisable, llChequeEnable, llChequeDisable, llInformationEnable, llVendorDetailsEnable, llVendorsDetailsDisable, llOwnerDisable, llOwnerEnable, llDocumentsDisable, llDocumentsEnable, llDeliveryBoysEnable, llDeliveryBoysDisable, llRateDetailsEnable, llRateDetailsDisable, llRateApprovalEnable, llRateApprovalDisable, llAgreementEnable, llAgreementDisable, llGstEnable, llGstDisable;
    SessionManager sessionManager;
    TextView tvLocationText, tvKYCText, tvPanText, tvBankText, tvStoreText, tvOwnerText, tvGstText, tvDeliveryText, tvRateText, tvRateApprovalText;
    TextView tvLattitude, tvLongitude, tvDocNumber, tvDocAddress, tvDocName, tvPanName, tvPanNumber, tvPanDOB, tvBankAccName, tvBankAcccountNumber, tvBankBranchName, tvStoreName, tvStoreAddress, tvStoreDCName, tvOwnerNAme, tvOwnerAddress, tvOwnerEmailID, tvGSTNumber, tvGStPanNumber, tvNoDeliveryBoysAdded;
    ProgressDialog progressDialog;
    ImageView ivAdharFront, ivAdharBack, ivPanFront, ivGstFront;

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
        ivGstFront = findViewById(R.id.ivGstFront);
        ivDeliveryDetails = findViewById(R.id.ivDeliveryDetails);
        ivEditLocation.setOnClickListener(this);
        ivEditKyc.setOnClickListener(this);
        ivEditPan.setOnClickListener(this);
        ivEditBank.setOnClickListener(this);
        ivEditStoreDetails.setOnClickListener(this);
        ivOwnerDetails.setOnClickListener(this);
        ivGstInformation.setOnClickListener(this);
        ivDeliveryDetails.setOnClickListener(this);
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


        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreAddress = findViewById(R.id.tvStoreAddress);
        tvStoreDCName = findViewById(R.id.tvDCStore);
        ivAdharFront = findViewById(R.id.ivKyCfront);
        ivAdharBack = findViewById(R.id.ivKYCback);
        ivPanFront = findViewById(R.id.ivPanFront);
        if (networkConnectivity.isNetworkAvailable()) {
            APiCallGetTrackDetails();
        } else {
            Constants.ShowNoInternet(this);
        }
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
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getLocationVerify() == 1) {
                            llLocationEnable.setVisibility(View.VISIBLE);
                            llLocatonDisable.setVisibility(View.GONE);
                            tvLattitude.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getLatitude());
                            tvLongitude.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getLongitude());

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getLocationVerify() == 0) {
                            llLocatonDisable.setVisibility(View.VISIBLE);
                            llLocationEnable.setVisibility(View.GONE);
                            tvLocationText.setVisibility(View.GONE);

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
                                tvDocName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getVoterName());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getDlCardFront()).into(ivAdharFront);
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getDlCardBack()).into(ivAdharBack);

                            } else {
                                tvDocNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarNo());
                                tvDocAddress.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarDob());
                                tvDocName.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getAadhaarName());
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getAdharCardFront()).into(ivAdharFront);
                                Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getAdharCardBack()).into(ivAdharBack);

                            }
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAadhaarVerify() == 0) {
                            llKyCEnable.setVisibility(View.GONE);
                            llKYCDisable.setVisibility(View.VISIBLE);
                            tvKYCText.setVisibility(View.GONE);
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
                            tvPanDOB.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getDob());
                            Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getPan()).into(ivPanFront);
                            // Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().get()).into(ivAdharBack);
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getPanVerify() == 0) {
                            llPanDisable.setVisibility(View.VISIBLE);
                            llPanEnable.setVisibility(View.GONE);
                            tvPanText.setVisibility(View.GONE);
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

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getChequeVerify() == 0) {
                            llChequeEnable.setVisibility(View.GONE);
                            llChequeDisable.setVisibility(View.VISIBLE);
                            tvBankText.setVisibility(View.GONE);
                        } else {
                            llChequeEnable.setVisibility(View.GONE);
                            llChequeDisable.setVisibility(View.VISIBLE);
                            tvBankText.setVisibility(View.VISIBLE);
                        }


                        //gstn

                        if (getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn().equalsIgnoreCase("")) {

                            if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 2) {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.VISIBLE);
                            } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 1) {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.GONE);
                            } else {
                                llGstDisable.setVisibility(View.VISIBLE);
                                llGstEnable.setVisibility(View.GONE);
                                tvGstText.setVisibility(View.GONE);
                            }
                        } else {
                            llGstEnable.setVisibility(View.VISIBLE);
                            llGstDisable.setVisibility(View.GONE);
                            tvGstText.setVisibility(View.GONE);
                            tvGSTNumber.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getGstn());
                            Glide.with(TaskProccessDetailActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getGstCertificateImage());
                            tvGStPanNumber.setText(getTrackDetailsResponse.getData().get(0).getProccessDetail().getPanNo());
                        }


                        // Information
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 1) {
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


                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 0) {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.GONE);

                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.GONE);
                        } else {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);
                            tvOwnerText.setVisibility(View.VISIBLE);

                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                            tvStoreText.setVisibility(View.VISIBLE);
                        }


                        // Upload Files
                  /*      if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getUploadFiles() == 1) {
                            llOwnerEnable.setVisibility(View.VISIBLE);
                            llOwnerDisable.setVisibility(View.GONE);

                            llVendorDetailsEnable.setVisibility(View.VISIBLE);
                            llVendorsDetailsDisable.setVisibility(View.GONE);

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getUploadFiles() == 0) {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);

                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                        } else {

                        }
*/

                        // Delivery Boys Details
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getDeliveryBoy() == 1) {
                            llDeliveryBoysDisable.setVisibility(View.GONE);
                            llDeliveryBoysEnable.setVisibility(View.VISIBLE);

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
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRateSendForApproval() == 0) {
                            llRateApprovalEnable.setVisibility(View.GONE);
                            llRateApprovalDisable.setVisibility(View.VISIBLE);
                            tvRateApprovalText.setVisibility(View.GONE);
                        } else {
                            llRateApprovalDisable.setVisibility(View.VISIBLE);
                            llRateApprovalEnable.setVisibility(View.GONE);
                            tvRateApprovalText.setVisibility(View.VISIBLE);
                        }
                        // rate sent for approval
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
                i.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i);
                break;
            case R.id.ivEditKYC:
                Intent i1 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i1.putExtra(Constants.DATA, "KYC");
                i1.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i1);
                break;
            case R.id.ivEditPan:
                Intent i2 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i2.putExtra(Constants.DATA, "PAN");
                i2.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i2);
                break;
            case R.id.ivEditCheque:
                Intent i3 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i3.putExtra(Constants.DATA, "CHEQUE");
                i3.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i3);
                break;
            case R.id.ivEditStoreDetails:
                Intent i4 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i4.putExtra(Constants.DATA, "STORE");
                i4.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i4);
                break;
            case R.id.ivEditOwnerDetails:
                Intent i5 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i5.putExtra(Constants.DATA, "OWNER");
                i5.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i5);
                break;
            case R.id.ivEditGSTDetails:
                Intent i6 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i6.putExtra(Constants.DATA, "GST");
                i6.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i6);
                break;
            case R.id.ivDeliveryDetails:
                Intent i7 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i7.putExtra(Constants.DATA, "DELIVERY");
                i7.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i7);
                break;


        }

    }
}
