package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GenerateResetPWResponse.Datum;
import com.canvascoders.opaper.Beans.ResetPassResponse.ResetPassResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.showAlert;
import static com.canvascoders.opaper.utils.Validator.isValidPassword;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Datum datum;
    private EditText etNewpw, etCnfPw;
    private Button btSubmit;
    String mobile, email, remember_token, password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        progressDialog = new ProgressDialog(ResetPasswordActivity.this);
        progressDialog.setMessage("Please wait...");
        datum = (Datum) getIntent().getSerializableExtra(Constants.DATA);
        remember_token = datum.getAgentDetail().getRememberToken();
        mobile = datum.getAgentDetail().getMobile();
        email = datum.getAgentDetail().getEmail();
        init();

    }

    private void init() {
        etNewpw = findViewById(R.id.etNewPassword);
        etCnfPw = findViewById(R.id.etConfirmPw);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSubmit:
                if (isValid(view)) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        password = etNewpw.getText().toString();
                        ApiCallReset();
                    } else {
                        Constants.ShowNoInternet(ResetPasswordActivity.this);

                    }

                }

        }
    }

    private void ApiCallReset() {
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_EMAIL_ID, email);
        params.put(Constants.PARAM_REMEMBER_TOKEN, remember_token);
        params.put(Constants.PARAM_MOBILE_NUMBER, mobile);
        params.put(Constants.PARAM_PASSWORD, password);
        ApiClient.getClient().create(ApiInterface.class).ResetPassword(params).enqueue(new Callback<ResetPassResponse>() {
            @Override
            public void onResponse(Call<ResetPassResponse> call, Response<ResetPassResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    ResetPassResponse resetPassResponse = response.body();
                    if(resetPassResponse.getResponseCode()==200){
                        Toast.makeText(ResetPasswordActivity.this, resetPassResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                     }
                     else{
                        Toast.makeText(ResetPasswordActivity.this, resetPassResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResetPassResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ResetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValid(View v) {

        if (etNewpw.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide New Password", false);
            etNewpw.requestFocus();
            return false;
        }
        if (etNewpw.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide Mobile", false);
            etNewpw.requestFocus();
            return false;
        }


        if(!isValidPassword(etNewpw.getText().toString())){
            showAlert(v, "Provide Password in proper format ", false);
            etNewpw.requestFocus();
            return false;
        }
        if(etNewpw.getText().toString().length()<8) {
            showAlert(v, "Password length should be eight or more", false);
            etNewpw.requestFocus();
            return false;
        }
        if (etCnfPw.getText().toString().isEmpty()) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide Confirm Password", false);
            etCnfPw.requestFocus();

            return false;
        }
        if (!etNewpw.getText().toString().equalsIgnoreCase(etCnfPw.getText().toString())) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Password Mismatch", false);
            etNewpw.requestFocus();
            return false;
        }
        return true;
    }

}
