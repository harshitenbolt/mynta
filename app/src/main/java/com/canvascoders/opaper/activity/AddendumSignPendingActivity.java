package com.canvascoders.opaper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AddendumSignPendingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTitle, tvMessage, tvSendLink, tvStatus, tvCounts, tvLastSigned;
    LinearLayout llBottom;
    ImageView ivBack;
    VendorList vendor;
    String count = "", message = "", lastsigned = "", status = "";
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_pending_screen);
        init();
    }

    private void init() {
        vendor = (VendorList) getIntent().getSerializableExtra("data");
        count = getIntent().getStringExtra("count");
        message = getIntent().getStringExtra("message");
        lastsigned = getIntent().getStringExtra("last_signed");
        status = getIntent().getStringExtra("is_esign");
        sessionManager = new SessionManager(AddendumSignPendingActivity.this);
        tvTitle = findViewById(R.id.tv_title_Process);
        tvMessage = findViewById(R.id.tvMessage);
        llBottom = findViewById(R.id.llBottom);
        tvLastSigned = findViewById(R.id.tvLastSigned);
        tvCounts = findViewById(R.id.tvCounts);
        tvSendLink = findViewById(R.id.tvResendSign);
        tvSendLink.setOnClickListener(this);
        tvStatus = findViewById(R.id.tvStatus);
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(this);

        if (status.equalsIgnoreCase("")) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);

        }
        tvCounts.setText(count);
        tvStatus.setText(status);
        tvMessage.setText(message);
        tvLastSigned.setText(lastsigned);


    }

    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvResendSign:
                apiCallResend();
                break;
            case R.id.iv_back_process:
                finish();
                break;


        }

    }

    private void apiCallResend() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.KEY_PROCESS_ID, "" + vendor.getProccessId());
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_ADUMDUM, "1");
        Call<ResendOTPResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).resendOTP("Bearer " + sessionManager.getToken(), params);
        callUpload.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, retrofit2.Response<ResendOTPResponse> response) {
                ResendOTPResponse resendOTPResponse = response.body();

                if (resendOTPResponse.getResponseCode() == 200) {
                    Toast.makeText(AddendumSignPendingActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
                } else if (resendOTPResponse.getResponseCode() == 411) {
                    sessionManager.logoutUser(AddendumSignPendingActivity.this);
                } else {
                    Toast.makeText(AddendumSignPendingActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {

            }
        });
    }

}
