package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GenerateResetPWResponse.Datum;
import com.canvascoders.opaper.Beans.GenerateResetPWResponse.GenerateResetPWResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.showAlert;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail,etMobile;
    private Button btSubmit;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        mProgressDialog.setMessage("Please wait...");
        etMobile = findViewById(R.id.etMobile);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSubmit:
                if(isValid(view)){
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallSendOtp();
                    }
                    else{
                        Constants.ShowNoInternet(ForgotPasswordActivity.this);

                    }
                }

        }
    }

    private void ApiCallSendOtp() {
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).sendCodeReset(etMobile.getText().toString()).enqueue(new Callback<GenerateResetPWResponse>() {
            @Override
            public void onResponse(Call<GenerateResetPWResponse> call, Response<GenerateResetPWResponse> response) {
                mProgressDialog.dismiss();
                if(response.isSuccessful()){
                    GenerateResetPWResponse generateResetPWResponse = response.body();
                    if(generateResetPWResponse.getResponseCode()==200){
                        Toast.makeText(ForgotPasswordActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Datum datum  = generateResetPWResponse.getData().get(0);
                        Intent i = new Intent(ForgotPasswordActivity.this,VerifyOTPActivity.class);
                        i.putExtra(Constants.DATA,datum);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this, generateResetPWResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
               }
            }

            @Override
            public void onFailure(Call<GenerateResetPWResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid(View v) {
       /* if (etEmail.getText().toString().length() == 0) {
            //_editTextMobile.setError("Provide Username");
            showAlert(v, "Provide Email", false);
            etEmail.requestFocus();

            return false;
        }*/
        if (etMobile.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide Mobile", false);
            etMobile.requestFocus();

            return false;
        }

        return true;
    }

}
