package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.R;

import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;

public class EditCOIActivity extends AppCompatActivity {
    Button btnResend;
    RequestPermissionHandler requestPermissionHandler;
    ImageView ivBack;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String str_process_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coiactivity);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermissionHandler = new RequestPermissionHandler();
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        sessionManager = new SessionManager(this);
        init();
    }

    private void init() {
        btnResend = findViewById(R.id.btnResend);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCallResendAgreement();
            }
        });

        progressDialog = new ProgressDialog(EditCOIActivity.this);
        progressDialog.setTitle("Please wait system is generating Agreement...");
        progressDialog.setCancelable(false);
    }

    private void ApiCallResendAgreement() {

        progressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).sendLinkCOI("Bearer " + sessionManager.getToken(), str_process_id).enqueue(new Callback<ResignAgreementResponse>() {
            @Override
            public void onResponse(Call<ResignAgreementResponse> call, retrofit2.Response<ResignAgreementResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    ResignAgreementResponse resignAgreementResponse = response.body();
                    if (resignAgreementResponse.getResponseCode() == 200) {

                        Toast.makeText(EditCOIActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditCOIActivity.this);
                    } else {
                        Toast.makeText(EditCOIActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditCOIActivity.this, "#errorcode :- 2134 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResignAgreementResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditCOIActivity.this, "#errorcode :- 2134 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }
}