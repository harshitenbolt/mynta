package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    LinearLayout llLocationEnable, llLocatonDisable, llKyCEnable, llKYCDisable, llPanEnable, llPanDisable, llChequeEnable, llChequeDisable, llInformationEnable, llVendorDetailsEnable, llVendorsDetailsDisable, llOwnerDisable, llOwnerEnable, llDocumentsDisable, llDocumentsEnable, llDeliveryBoysEnable, llDeliveryBoysDisable, llRateDetailsEnable, llRateDetailsDisable, llRateApprovalEnable, llAgreementEnable, llAgreementDisable;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

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
        llRateApprovalEnable = findViewById(R.id.llRateEnable);
        llRateDetailsDisable = findViewById(R.id.llRateDisable);


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
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getLocationVerify() == 0) {
                            llLocatonDisable.setVisibility(View.VISIBLE);
                            llLocationEnable.setVisibility(View.GONE);

                        } else {

                        }

                        // KYC Verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAadhaarVerify() == 1) {
                            llKyCEnable.setVisibility(View.VISIBLE);
                            llKYCDisable.setVisibility(View.GONE);
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getAadhaarVerify() == 0) {
                            llKyCEnable.setVisibility(View.GONE);
                            llKYCDisable.setVisibility(View.VISIBLE);
                        } else {

                        }


                        // Pan Card Verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getPanVerify() == 1) {
                            llPanDisable.setVisibility(View.GONE);
                            llPanEnable.setVisibility(View.VISIBLE);
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getPanVerify() == 0) {
                            llPanDisable.setVisibility(View.VISIBLE);
                            llPanEnable.setVisibility(View.GONE);
                        } else {

                        }


                        //cheque verification
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getChequeVerify() == 1) {
                        llChequeEnable.setVisibility(View.VISIBLE);
                        llChequeDisable.setVisibility(View.GONE);
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getChequeVerify() == 0) {
                            llChequeEnable.setVisibility(View.GONE);
                            llChequeDisable.setVisibility(View.VISIBLE);
                        } else {

                        }


                        // Information
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 1) {
                        llOwnerEnable.setVisibility(View.VISIBLE);
                        llOwnerDisable.setVisibility(View.GONE);

                        llVendorDetailsEnable.setVisibility(View.VISIBLE);
                        llVendorsDetailsDisable.setVisibility(View.GONE);

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getFillDetails() == 0) {
                            llOwnerEnable.setVisibility(View.GONE);
                            llOwnerDisable.setVisibility(View.VISIBLE);

                            llVendorDetailsEnable.setVisibility(View.GONE);
                            llVendorsDetailsDisable.setVisibility(View.VISIBLE);
                        } else {

                        }


                        // Upload Files
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getUploadFiles() == 1) {
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


                        // Delivery Boys Details
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getDeliveryBoy() == 1) {
                            llDeliveryBoysDisable.setVisibility(View.GONE);
                            llDeliveryBoysEnable.setVisibility(View.VISIBLE);
                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getDeliveryBoy() == 0) {
                            llDeliveryBoysDisable.setVisibility(View.VISIBLE);
                            llDeliveryBoysEnable.setVisibility(View.GONE);
                        } else {

                        }

                        //Rate

                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRate() == 1) {

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRate() == 0) {

                        } else {

                        }

                        // rate sent for approval
                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRateSendForApproval() == 1) {

                        } else if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getRateSendForApproval() == 0) {

                        } else {

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
