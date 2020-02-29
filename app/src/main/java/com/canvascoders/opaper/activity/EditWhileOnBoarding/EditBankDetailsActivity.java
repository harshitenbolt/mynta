package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.getMerakApiResponse.GetMerakResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.ImageUploadTask;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.OnTaskCompleted;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;
import static com.canvascoders.opaper.fragment.PanVerificationFragment.CROPPED_IMAGE;

public class EditBankDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int IMAGE_CHEQUE = 101;
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    ProgressDialog progressDialog;
    private String TAG = "ChequeUpload";
    private Button btExtract;
    private String cancelChequeImagepath = "", imagecamera = "";
    private Uri imgURI;
    private static Dialog dialog;
    GPSTracker gps;
    private String lattitude = "", longitude = "";
    private TextView btn_cheque_card;
    private ImageView ivChequeImage;
    private ImageView btn_cheque_card_select;
    private SessionManager sessionManager;
    private boolean isPanSelected = false;
    private EditText edit_ac_no, edit_ac_name, edit_ifsc, edit_bank_name, edit_bank_branch_name, edit_bank_address;
    private TextRecognizer detector;

    View view;
    String bank_name = "";
    String bank_branch = "";
    String branch_address = "";
    private RequestPermissionHandler requestPermissionHandler;
    private CheckBox cd_aggre_terms_condition;
    private String str_process_id;
    int request_id = 0;
    ImageView ivBack;
    NetworkConnectivity networkConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank_details);
        sessionManager = new SessionManager(this);
        networkConnectivity = new NetworkConnectivity(this);
        requestPermissionHandler = new RequestPermissionHandler();
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        initView();
    }


    private void initView() {

        btExtract = findViewById(R.id.btExtract);
        btn_cheque_card = (TextView) findViewById(R.id.tvClickCheque);
        btn_cheque_card_select = (ImageView) findViewById(R.id.tvClickChequeSelected);
        btn_cheque_card_select.setOnClickListener(this);
        ivChequeImage = findViewById(R.id.ivChequeImage);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*cd_aggre_terms_condition = view.findViewById(R.id.cbAgreeTC);
        edit_ac_no = (EditText) view.findViewById(R.id.edit_ac_no);
        edit_ac_name = (EditText) view.findViewById(R.id.edit_ac_name);
        edit_ifsc = (EditText) view.findViewById(R.id.edit_ifsc);
        edit_bank_name = (EditText) view.findViewById(R.id.edit_bank_name);
        edit_bank_branch_name = (EditText) view.findViewById(R.id.edit_bank_branch_name);
        edit_bank_address = (EditText) view.findViewById(R.id.edit_bank_address);*/

       /* edit_bank_name.setEnabled(false);
        edit_bank_branch_name.setEnabled(false);
        edit_bank_address.setEnabled(false);
        edit_ac_no.setEnabled(false);
        edit_ac_name.setEnabled(false);
        edit_ifsc.setEnabled(false);
*/

        btExtract.setOnClickListener(this);
        btn_cheque_card.setOnClickListener(this);
        detector = new TextRecognizer.Builder(EditBankDetailsActivity.this).build();
        progressDialog = new ProgressDialog(EditBankDetailsActivity.this);
        progressDialog.setCancelable(false);


//        setButtonImage();


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btExtract) {
//            Constants.hideKeyboardwithoutPopulate(EditBankDetailsActivity.this);
            if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                if (validation()) {


                    CallMerekApi();
                }
                /*storeCheque()*/
                ;/*{
                    DialogueUtils.failedPayment(EditBankDetailsActivity.this, new DialogListner() {
                        @Override
                        public void onClickPositive() {
                            DialogueUtils.dismiss();
                        }

                        @Override
                        public void onClickNegative() {
                            DialogueUtils.dismiss();
                        }
                    });

                }*///
               /* else{
                    DialogueUtils.successPayment(EditBankDetailsActivity.this, new DialogListner() {
                        @Override
                        public void onClickPositive() {
                            DialogueUtils.dismiss();
                        }

                        @Override
                        public void onClickNegative() {
                            DialogueUtils.dismiss();
                        }
                    });
                }*/


            } else {
                Constants.ShowNoInternet(EditBankDetailsActivity.this);
            }

        } else if (v.getId() == R.id.tvClickCheque) {

            capture_cheque_image();

        } else if (v.getId() == R.id.tvClickChequeSelected) {
            capture_cheque_image();
        }

    }

    private void capture_cheque_image() {
        requestPermissionHandler.requestPermission(EditBankDetailsActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditBankDetailsActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_CHEQUE);

                /*Intent intent = new Intent(getContext(), MyCameraActivity.class);
                startActivityForResult(intent, IMAGE_CHEQUE);*/


            }

            @Override
            public void onFailed() {
                Toast.makeText(EditBankDetailsActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validation() {

        if (!isPanSelected) {
            Toast.makeText(EditBankDetailsActivity.this, "Please select Cheque Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CHEQUE) {
                deleteImages();
//                Constants.hideKeyboardwithoutPopulate(EditBankDetailsActivity.this);
                Bitmap bitmap = ImagePicker.getImageFromResult(EditBankDetailsActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditBankDetailsActivity.this);

                Intent intent = new Intent(EditBankDetailsActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);


            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(EditBankDetailsActivity.this, imgURI);
                Glide.with(EditBankDetailsActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                        .into(ivChequeImage);
                isPanSelected = true;
                btn_cheque_card.setVisibility(View.GONE);
                btn_cheque_card_select.setVisibility(View.VISIBLE);
                File casted_image6 = new File(imagecamera);
                if (casted_image6.exists()) {
                    casted_image6.delete();
                }


            }
        }
    }

    int tryCount = 0;

    public void CallMerekApi() {

        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put("image", cancelChequeImagepath);
        final ProgressDialog pd = new ProgressDialog(EditBankDetailsActivity.this);
        pd.setTitle("Loading . . .");
        pd.show();
        Log.e("Merak", "API Called");
        new ImageUploadTask(Constants.OCRMEREK, null, fileMap, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                Log.e("MERAK RESULT", tryCount + "::" + result);
                pd.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("success") == 1) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        if (dataObj != null) {
                            String ifsccode = "";
                            ifsccode = dataObj.optString("ifsc_code");
                            getBankDetails(ifsccode);
                            String accountNumber = dataObj.optString("account_number");
                            bank_name = dataObj.optString("bank_name");
                            bank_branch = dataObj.optString("bank_branch");
                            branch_address = dataObj.optString("branch_address");
                            String payeeName = "";
                            try {
                                request_id = jsonObject.optInt("request_id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            JSONArray payeeNames = dataObj.optJSONArray("payee_name");
                            if (payeeNames != null && payeeNames.length() > 0) {
                                payeeName = payeeNames.getString(0);
                                // edit_ac_name.setText(payeeName);
                            }

                            if (networkConnectivity.isNetworkAvailable()) {
                                ApiCallGetMerakCount();
                            } else {
                                Constants.ShowNoInternet(EditBankDetailsActivity.this);
                            }

                            DialogUtil.chequeDetail(EditBankDetailsActivity.this, accountNumber, payeeName, ifsccode, str_process_id, bank_name, bank_branch, branch_address, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {
                                    storeCheque(accName, payeename, ifsc, bankname, BranchName, bankAdress);
                                }

                                @Override
                                public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                }
                            });
                           /* edit_ac_no.setText(accountNumber);
                            edit_ifsc.setText(ifscCode);
                            edit_bank_name.setText(bank_name);
                            edit_bank_branch_name.setText(bank_branch);
                            edit_bank_address.setText(branch_address);*/


                        }
                    } else {
                        DialogUtil.chequeDetail(EditBankDetailsActivity.this, "", "", "", str_process_id, "", "", "", new DialogListner() {
                            @Override
                            public void onClickPositive() {

                            }

                            @Override
                            public void onClickNegative() {

                            }

                            @Override
                            public void onClickDetails(String name, String fathername, String dob, String id) {

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {
                                storeCheque(accName, payeename, ifsc, bankname, BranchName, bankAdress);
                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }
                        });
                        Toast.makeText(EditBankDetailsActivity.this, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    cancelChequeImagepath = "";
                    Glide.with(EditBankDetailsActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.checkimage)
                            .into(ivChequeImage);
                    isPanSelected = false;
                    btn_cheque_card.setVisibility(View.VISIBLE);
                    btn_cheque_card_select.setVisibility(View.GONE);
                    Toast.makeText(EditBankDetailsActivity.this, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();

                }

            }
        }).execute();

        Glide.with(EditBankDetailsActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                .into(ivChequeImage);
        isPanSelected = true;

    }

    private void ApiCallGetMerakCount() {
        Map<String, String> params = new HashMap<String, String>();

        // params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.DATA, "");
        Call<GetMerakResponse> callUpload = ApiClient.getClient2().create(ApiInterface.class).getMerakList(params);
        callUpload.enqueue(new Callback<GetMerakResponse>() {
            @Override
            public void onResponse(Call<GetMerakResponse> call, Response<GetMerakResponse> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<GetMerakResponse> call, Throwable t) {

            }
        });


    }

    public void storeCheque(String acname, String payeename, String ifsc, String bankname, String branchname, String branchAddress) {
        gps = new GPSTracker(EditBankDetailsActivity.this);
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);
        } else {

            gps.showSettingsAlert();
        }

        MultipartBody.Part cheque_image_part = null;


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_AC_NAME, "" + payeename);
            params.put(Constants.PARAM_BANK_AC, "" + acname);
            params.put(Constants.PARAM_IFSC, "" + ifsc);
            params.put(Constants.PARAM_REQUEST_ID, "" + request_id);
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);

            File imagefile = new File(cancelChequeImagepath);
            cheque_image_part = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagefile));

            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            progressDialog.setMessage("Uploading cheque document. Please wait......");
            progressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).editgetstoreCheque("Bearer " + sessionManager.getToken(), params, cheque_image_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    try {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.isSuccessful()) {
                            CommonResponse chequeDetail = response.body();

                            if (chequeDetail.getResponseCode() == 200) {
                                deleteImages();
                                DialogUtil.dismiss();
                                showAlert(response.body().getResponse());
                            }
                            if (chequeDetail.getResponseCode() == 411) {
                                sessionManager.logoutUser(EditBankDetailsActivity.this);
                            }
                            if (chequeDetail.getResponseCode() == 400) {
                                progressDialog.dismiss();
                                if (chequeDetail.getValidation() != null) {
                                    Validation validation = chequeDetail.getValidation();
                                    if (validation.getIfsc() != null && validation.getIfsc().length() > 0) {
                                        DialogUtil.etIfscCode.setError(validation.getIfsc());
                                        DialogUtil.etIfscCode.requestFocus();
                                        // return false;
                                    }
                                    if (validation.getBankAc() != null && validation.getBankAc().length() > 0) {
                                        DialogUtil.etChequeNumber.setError(validation.getBankAc());
                                        DialogUtil.etChequeNumber.requestFocus();

                                    }
                                    if (validation.getAcName() != null && validation.getAcName().length() > 0) {
                                        DialogUtil.etPayeeName.setError(validation.getAcName());
                                        DialogUtil.etPayeeName.requestFocus();

                                        // Toast.makeText(EditBankDetailsActivity.this,validation.getAgentId(),Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                        Toast.makeText(EditBankDetailsActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                        Toast.makeText(EditBankDetailsActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getRequestId() != null && validation.getRequestId().length() > 0) {
                                        Toast.makeText(EditBankDetailsActivity.this, validation.getRequestId(), Toast.LENGTH_LONG).show();

                                    }
                                    if (validation.getCancelledCheque() != null && validation.getCancelledCheque().length() > 0) {
                                        Toast.makeText(EditBankDetailsActivity.this, validation.getCancelledCheque(), Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    Toast.makeText(EditBankDetailsActivity.this, chequeDetail.getResponse(), Toast.LENGTH_LONG).show();
                                }

                                // ErrorResponsePanCard errorResponsePanCard = response.body();
                            } else {
                                Toast.makeText(EditBankDetailsActivity.this, chequeDetail.getResponse(), Toast.LENGTH_LONG).show();
                                // Constants.showAlert(edit_ac_name.getRootView(), chequeDetail.getResponse(), true);
                            }

                        } else {
                            Toast.makeText(EditBankDetailsActivity.this, "#errorcode :- 2093 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                            // Toast.makeText(EditBankDetailsActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(EditBankDetailsActivity.this, "#errorcode :- 2093 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditBankDetailsActivity.this, "#errorcode :- 2093 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Constants.ShowNoInternet(EditBankDetailsActivity.this);
        }

    }


    private void showAlert(String msg) {

        Button btSubmit;
        TextView tvMessage, tvTitle;


        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(EditBankDetailsActivity.this);
        dialog = new Dialog(EditBankDetailsActivity.this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Cheque Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                //  commanFragmentCallWithoutBackStack(new InfoFragment());

            }
        });

        dialog.setCancelable(false);

        dialog.show();
    }

    public void getBankDetails(String ifsc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_IFSC, ifsc);

        Log.e("DATA", "PID:" + str_process_id + " aID : " + sessionManager.getAgentID() + " IFSC:" + ifsc);
        Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getBankDetailsFromIfsc("Bearer " + sessionManager.getToken(), params);
        callUpload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.has("responseCode")) {
                                if (jsonObject.getInt("responseCode") == 200) {
                                    JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                    bank_name = result.getString("bank_name").toString();
                                    bank_branch = result.getString("bank_branch_name").toString();
                                    branch_address = result.getString("bank_address").toString();
                                } else if (jsonObject.getInt("responseCode") == 405) {
                                    sessionManager.logoutUser(EditBankDetailsActivity.this);
                                } else if (jsonObject.getInt("responseCode") == 411) {
                                    sessionManager.logoutUser(EditBankDetailsActivity.this);
                                } else {
                                }
                            }
                        } else {
                            Toast.makeText(EditBankDetailsActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(EditBankDetailsActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditBankDetailsActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = EditBankDetailsActivity.this.getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }
   /* public void deleteFileFromMediaManager(Context context, String path) {
        File file = new File(path);
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
                if (file.exists()) {
                    context.deleteFile(file.getName());
                    Log.e("Deleted File", String.valueOf(file));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }*/


    @Override
    public void onResume() {
        if (!AppApplication.networkConnectivity.isNetworkAvailable()) {
            Constants.ShowNoInternet(EditBankDetailsActivity.this);
        }
        super.onResume();
    }

    private void deleteImages() {

        File casted_image = new File(cancelChequeImagepath);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(imagecamera);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }
}
