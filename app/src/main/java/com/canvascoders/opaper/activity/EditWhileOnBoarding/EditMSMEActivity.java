package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetTrackingDetailResponse.GetTrackDetailsResponse;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DocUploadFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditMSMEActivity extends AppCompatActivity implements View.OnClickListener {


    Button btSumit, btSkip;
    ImageView ivmenu;
    Button btExtractPAn;
    private static final int IMAGE_PAN = 101;
    ImageView ivPanImage;
    private static Dialog dialog;
    private Uri imgURI;
    public static final int CROPPED_IMAGE = 5333;
    private String panImagepath = "", imagecamera = "", dlImageOathFront = "", identityEmail = "", identityCallbackUrl = "", identityAccessToken = "", identityID = "", identityPatronId = "";
    AppCompatImageView btn_pan_card_select;
    TextView tvMSME;
    private static final int IMAGE_MSME_FRONT = 1021;
    LinearLayout llCapturePan;
    RequestPermissionHandler requestPermissionHandler;

    private static int IMAGE_SELCTION_CODE = 0;
    private boolean isPanSelected = false;
    String str_process_id = "";
    private String file_name, file_url, pan_card_detail_id, birth_date = "";
    SessionManager sessionManager;

    ImageView ivMSME, ivCheckMSMEFront;
    EditText etMSMERegistration;
    ProgressDialog progressDialog;
    private static final int IMAGE_DRIVING_FRONT = 1021, IMAGE_DRIVING_BACK = 1022, CROPPED_IMAGE_DL_FRONT = 1023, CROPPED_IMAGE_DL_BACK = 1024;
    ImageView ivBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_msmeactivity);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        requestPermissionHandler = new RequestPermissionHandler();
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        init();
    }

    private void init() {
        tvMSME = findViewById(R.id.tvMSME);
        tvMSME.setOnClickListener(this);
        ivMSME = findViewById(R.id.ivMSME);
        etMSMERegistration = findViewById(R.id.etMSME);
        ivCheckMSMEFront = findViewById(R.id.ivCheckMSMEFront);
        ivCheckMSMEFront.setOnClickListener(this);
        btSkip = findViewById(R.id.btSkip);
        btSumit = findViewById(R.id.btSubmit);
        btSumit.setOnClickListener(this);
        btSkip.setOnClickListener(this);
        APiCallGetTrackDetails();
    }


    private void APiCallGetTrackDetails() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).geTrackingDetails("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetTrackDetailsResponse>() {
            @Override
            public void onResponse(Call<GetTrackDetailsResponse> call, Response<GetTrackDetailsResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetTrackDetailsResponse getTrackDetailsResponse = response.body();
                    if (getTrackDetailsResponse.getResponseCode() == 200) {
                        //  Toast.makeText(EditMSMEActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        etMSMERegistration.setText(getTrackDetailsResponse.getData().get(0).getBasicDetails().getMsme_registration_no());


                        Glide.with(EditMSMEActivity.this).load(Constants.BaseImageURL + getTrackDetailsResponse.getData().get(0).getDocUpload().getMsme_registration_cert()).into(ivMSME);
                    } else {
                        Toast.makeText(EditMSMEActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditMSMEActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTrackDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditMSMEActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(EditMSMEActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_MSME_FRONT;
                    Intent intent1 = ImagePicker.getCameraIntent(EditMSMEActivity.this);
                    startActivityForResult(intent1, IMAGE_MSME_FRONT);
                    //DrivingFragment.this.startActivityForResult(intent1, IMAGE_DRIVING_FRONT);

                }

            }

            @Override
            public void onFailed() {
                Toast.makeText(EditMSMEActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMSME:
                capture_aadhar_front_and_back_image(1);
                break;
            case R.id.ivCheckMSMEFront:
                capture_aadhar_front_and_back_image(1);
                break;
            case R.id.btSubmit:
                if (validation()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiSubmitMSME();
                    } else {
                        Constants.ShowNoInternet(EditMSMEActivity.this);
                    }
                }

                break;
            case R.id.btSkip:

                showAlert();
                break;

        }

    }

    private void ApiSkip() {
        progressDialog.show();
        MultipartBody.Part typedFile = null;
        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put("app_current_version", String.valueOf(Constants.APP_VERSION));

        // File imagefile = new File(panImagepath);
        // typedFile = MultipartBody.Part.createFormData("msme_registration_cert", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));
        ApiClient.getClient().create(ApiInterface.class).skipMSME("Bearer " + sessionManager.getToken(), str_process_id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse.getResponseCode() == 200) {
                        finish();
                    } else {
                        Toast.makeText(EditMSMEActivity.this, commonResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        if (commonResponse.getResponseCode() == 400) {
                            Validation validation = commonResponse.getValidation();
                            if (validation.getMsme_registration_no() != null && validation.getMsme_registration_no().length() > 0) {
                                etMSMERegistration.setError(validation.getMsme_registration_no());
                                etMSMERegistration.requestFocus();
                                // return false;
                            }
                            if (validation.getMsme_registration_cert() != null && validation.getMsme_registration_cert().length() > 0) {
                                Toast.makeText(EditMSMEActivity.this, validation.getMsme_registration_cert(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(EditMSMEActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(EditMSMEActivity.this, "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditMSMEActivity.this, "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditMSMEActivity.this);
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("Are you sure you want skip this step?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ApiSkip();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    private void ApiSubmitMSME() {
        progressDialog.show();
        MultipartBody.Part typedFile = null;
        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_MSME_REG_NO, "" + etMSMERegistration.getText());
        params.put("app_current_version", String.valueOf(Constants.APP_VERSION));

        File imagefile = new File(dlImageOathFront);
        typedFile = MultipartBody.Part.createFormData("msme_registration_cert", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(dlImageOathFront)), imagefile));
        ApiClient.getClient().create(ApiInterface.class).editMSME("Bearer " + sessionManager.getToken(), params, typedFile).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse.getResponseCode() == 200) {
                      finish();
                    } else {
                        Toast.makeText(EditMSMEActivity.this, commonResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        if (commonResponse.getResponseCode() == 400) {
                            Validation validation = commonResponse.getValidation();
                            if (validation.getMsme_registration_no() != null && validation.getMsme_registration_no().length() > 0) {
                                etMSMERegistration.setError(validation.getMsme_registration_no());
                                etMSMERegistration.requestFocus();
                                // return false;
                            }
                            if (validation.getMsme_registration_cert() != null && validation.getMsme_registration_cert().length() > 0) {
                                Toast.makeText(EditMSMEActivity.this, validation.getMsme_registration_cert(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(EditMSMEActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(EditMSMEActivity.this, "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditMSMEActivity.this, "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = EditMSMEActivity.this.getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }


    private boolean validation() {

        if (!isPanSelected) {
            Toast.makeText(EditMSMEActivity.this, "Please select Certificate Image", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (TextUtils.isEmpty(etMSMERegistration.getText().toString())) {
            etMSMERegistration.setError("provide certificate number");
            etMSMERegistration.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("KYC", "DRIVITNG");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_MSME_FRONT) {
                Uri uri = ImagePicker.getPickImageResultUri(EditMSMEActivity.this, data);
                Intent intent = new Intent(this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);

            }

            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                dlImageOathFront = RealPathUtil.getPath(EditMSMEActivity.this, imgURI);
                isPanSelected = true;
                try {
                    Glide.with(this).load(dlImageOathFront).into(ivMSME);
                    ivCheckMSMEFront.setVisibility(View.VISIBLE);
                    tvMSME.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}