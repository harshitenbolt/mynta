package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.activity.EndTaskActivity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.activity.TaskDetailActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;


public class MSMEFragment extends Fragment implements View.OnClickListener {

    View v;
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

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_m_s_m_e, container, false);
        context = this.getContext();
        OTPActivity.settitle(Constants.TITLE_MSME_VERIFICATION);
        progressDialog = new ProgressDialog(context);

        sessionManager = new SessionManager(getActivity());
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        init();
        return v;
    }

    private void init() {
        tvMSME = v.findViewById(R.id.tvMSME);
        tvMSME.setOnClickListener(this);
        ivMSME = v.findViewById(R.id.ivMSME);
        etMSMERegistration = v.findViewById(R.id.etMSME);
        requestPermissionHandler = new RequestPermissionHandler();
        ivCheckMSMEFront = v.findViewById(R.id.ivCheckMSMEFront);
        ivCheckMSMEFront.setOnClickListener(this);
        btSkip = v.findViewById(R.id.btSkip);
        btSumit = v.findViewById(R.id.btSubmit);
        btSumit.setOnClickListener(this);
        btSkip.setOnClickListener(this);
    }

    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_MSME_FRONT;
                    Intent intent1 = ImagePicker.getCameraIntent(context);
                    startActivityForResult(intent1, IMAGE_MSME_FRONT);
                    //DrivingFragment.this.startActivityForResult(intent1, IMAGE_DRIVING_FRONT);
                }

            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), "request permission failed", Toast.LENGTH_SHORT).show();

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
                        Constants.ShowNoInternet(getActivity());
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
                        commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    } else {
                        Toast.makeText(getActivity(), commonResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        if (commonResponse.getResponseCode() == 400) {
                            Validation validation = commonResponse.getValidation();
                            if (validation.getMsme_registration_no() != null && validation.getMsme_registration_no().length() > 0) {
                                etMSMERegistration.setError(validation.getMsme_registration_no());
                                etMSMERegistration.requestFocus();
                                // return false;
                            }
                            if (validation.getMsme_registration_cert() != null && validation.getMsme_registration_cert().length() > 0) {
                                Toast.makeText(getActivity(), validation.getMsme_registration_cert(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
        ApiClient.getClient().create(ApiInterface.class).submitMSME("Bearer " + sessionManager.getToken(), params, typedFile).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse.getResponseCode() == 200) {
                     //   commanFragmentCallWithoutBackStack(new DocUploadFragment());
                        showAlert(commonResponse.getResponse());
                    } else {
                        Toast.makeText(getActivity(), commonResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        if (commonResponse.getResponseCode() == 400) {
                            Validation validation = commonResponse.getValidation();
                            if (validation.getMsme_registration_no() != null && validation.getMsme_registration_no().length() > 0) {
                                etMSMERegistration.setError(validation.getMsme_registration_no());
                                etMSMERegistration.requestFocus();
                                // return false;
                            }
                            if (validation.getMsme_registration_cert() != null && validation.getMsme_registration_cert().length() > 0) {
                                Toast.makeText(getActivity(), validation.getMsme_registration_cert(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode 2036 Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showAlert(String msg) {
        Button btSubmit;
        TextView tvMessage, tvTitle;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(context);
        dialog = new Dialog(getActivity(), R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("MSME Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                commanFragmentCallWithoutBackStack(new DocUploadFragment());


            }
        });

        dialog.setCancelable(false);

        dialog.show();

    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }


    private boolean validation() {

        if (!isPanSelected) {
            Toast.makeText(getActivity(), "Please select Certificate Image", Toast.LENGTH_SHORT).show();
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
                Uri uri = ImagePicker.getPickImageResultUri(getActivity(), data);
                Intent intent = new Intent(context, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);

            }

            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                dlImageOathFront = RealPathUtil.getPath(getContext(), imgURI);
                isPanSelected = true;
                try {
                    Glide.with(context).load(dlImageOathFront).into(ivMSME);
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