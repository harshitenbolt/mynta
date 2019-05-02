package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.camera.MyCameraActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogueUtils;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.ImageUploadTask;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.OnTaskCompleted;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

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

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;
import static com.canvascoders.opaper.fragment.PanVerificationFragment.CROPPED_IMAGE;


public class ChequeUploadFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_CHEQUE = 101;
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    ProgressDialog progressDialog;
    private String TAG = "ChequeUpload";
    private Button btn_next;
    private String cancelChequeImagepath = "";
    private Uri imgURI;
    private ImageView btn_cheque_card;
    private ImageView btn_cheque_card_select;
    private SessionManager sessionManager;
    private boolean isPanSelected = false;
    private EditText edit_ac_no, edit_ac_name, edit_ifsc, edit_bank_name, edit_bank_branch_name, edit_bank_address;
    private TextRecognizer detector;
    Context mcontext;
    View view;
    private RequestPermissionHandler requestPermissionHandler;
    private CheckBox cd_aggre_terms_condition;
    private String str_process_id;
    int request_id = 0;

//    public static final String OCRMEREK = "https://api.merak.ai/v1/text-recognition/cheque/recognize/?key=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImJpa2FzaC5taXNocmFAbXludHJhLmNvbSIsIm9yZ2FuaXphdGlvbl9pZCI6OSwiaWF0IjoxNTMxOTkwOTUzfQ.5EpThmiqO_iN9Bg6RBR-kKQch0QjvEmI-lNg2SQxi8k";

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Mylogger.getInstance().Logit(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "O_" + System.currentTimeMillis() + ".jpeg");
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cheque, container, false);

        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        OTPActivity.settitle(Constants.TITLE_CHEQUE);

        requestPermissionHandler = new RequestPermissionHandler();

        initView();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!AppApplication.networkConnectivity.isNetworkAvailable()) {

            Constants.ShowNoInternet(mcontext);

        }
        edit_ifsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 5) {
                    getBankDetails(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) view.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) view.findViewById(R.id.txt_show_msg_fail);
        txt_show_msg_sucess.setVisibility(View.GONE);
        txt_show_msg_fail.setVisibility(View.GONE);
        if (b) {
            txt_show_msg_sucess.setText(msg);
            txt_show_msg_sucess.setVisibility(View.VISIBLE);
        } else {
            txt_show_msg_fail.setText(msg);
            txt_show_msg_fail.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                txt_show_msg_sucess.setVisibility(View.GONE);
                txt_show_msg_fail.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void initView() {

        btn_next = view.findViewById(R.id.btn_next);
        btn_cheque_card = (ImageView) view.findViewById(R.id.btn_cheque_cards);
        btn_cheque_card_select = (ImageView) view.findViewById(R.id.btn_cheque_cards_done);
        cd_aggre_terms_condition = view.findViewById(R.id.cbAgreeTC);
        edit_ac_no = (EditText) view.findViewById(R.id.edit_ac_no);
        edit_ac_name = (EditText) view.findViewById(R.id.edit_ac_name);
        edit_ifsc = (EditText) view.findViewById(R.id.edit_ifsc);
        edit_bank_name = (EditText) view.findViewById(R.id.edit_bank_name);
        edit_bank_branch_name = (EditText) view.findViewById(R.id.edit_bank_branch_name);
        edit_bank_address = (EditText) view.findViewById(R.id.edit_bank_address);

        edit_bank_name.setEnabled(false);
        edit_bank_branch_name.setEnabled(false);
        edit_bank_address.setEnabled(false);
        edit_ac_no.setEnabled(false);
        edit_ac_name.setEnabled(false);
        edit_ifsc.setEnabled(false);


        btn_next.setOnClickListener(this);
        btn_cheque_card.setOnClickListener(this);
        detector = new TextRecognizer.Builder(mcontext).build();
        progressDialog = new ProgressDialog(mcontext);


        setButtonImage();


    }

    private void setButtonImage() {
        if (isPanSelected == true) {
            btn_next.setBackground(getResources().getDrawable(R.drawable.btn_active));
            btn_next.setEnabled(true);
            btn_next.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_cheque_card_select.setVisibility(View.VISIBLE);
        } else {
            btn_next.setBackground(getResources().getDrawable(R.drawable.btn_normal));
            btn_next.setEnabled(false);
            btn_next.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btn_cheque_card_select.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Constants.hideKeyboardwithoutPopulate(getActivity());
            if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                if (validation()) storeCheque();/*{
                    DialogueUtils.failedPayment(getActivity(), new DialogListner() {
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
                    DialogueUtils.successPayment(getActivity(), new DialogListner() {
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
                Constants.ShowNoInternet(getActivity());
            }

        } else if (v.getId() == R.id.btn_cheque_cards) {

            capture_cheque_image();

        }

    }

    private void capture_cheque_image() {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                startActivityForResult(chooseImageIntent, IMAGE_CHEQUE);

                /*Intent intent = new Intent(getContext(), MyCameraActivity.class);
                startActivityForResult(intent, IMAGE_CHEQUE);*/


            }

            @Override
            public void onFailed() {
                Toast.makeText(mcontext, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validation() {
        if (TextUtils.isEmpty(edit_ac_no.getText().toString())) {
            edit_ac_no.requestFocus();
            edit_ac_no.setError("Provide Accout Number");
            return false;
        }
        if (TextUtils.isEmpty(edit_ac_name.getText().toString())) {
            edit_ac_name.requestFocus();
            edit_ac_name.setError("Provide Accout Name");
            showMSG(false, "Provide Accout Name");
            return false;
        }
        if (TextUtils.isEmpty(edit_ifsc.getText().toString())) {
            edit_ifsc.requestFocus();
            showMSG(false, "Provide IFSCode");
            return false;
        }
        if (!cd_aggre_terms_condition.isChecked()) {
            showMSG(false, "Please verify all details with physical evidence.");
            return false;
        }
        if (!isPanSelected) {
            return false;
        }
        return true;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == IMAGE_CHEQUE && resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CHEQUE) {


                    Constants.hideKeyboardwithoutPopulate(getActivity());


                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    cancelChequeImagepath = ImagePicker.getBitmapPath(bitmap, getActivity());

                    HashMap<String, String> fileMap = new HashMap<>();
                    fileMap.put("image", cancelChequeImagepath);
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setTitle("Loading . . .");
                    new ImageUploadTask(OCRMEREK, null, fileMap, new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(String result) {
                            pd.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.optInt("success") == 1) {
                                    JSONObject dataObj = jsonObject.getJSONObject("data");
                                    if (dataObj != null) {
                                        String ifscCode = dataObj.optString("ifsc_code");
                                        String accountNumber = dataObj.optString("account_number");
                                        String bank_name = dataObj.optString("bank_name");
                                        String bank_branch = dataObj.optString("bank_branch");
                                        String branch_address = dataObj.optString("branch_address");

                                        edit_ac_no.setText(accountNumber);
                                        edit_ifsc.setText(ifscCode);
                                        edit_bank_name.setText(bank_name);
                                        edit_bank_branch_name.setText(bank_branch);
                                        edit_bank_address.setText(branch_address);

                                        JSONArray payeeNames = dataObj.optJSONArray("payee_name");
                                        if (payeeNames != null && payeeNames.length() > 0) {
                                            String payeeName = payeeNames.getString(0);
                                            edit_ac_name.setText(payeeName);
                                        }
                                        request_id = jsonObject.optInt("request_id");
                                    }
                                } else {
                                    Toast.makeText(mcontext, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mcontext, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();




                    Glide.with(ChequeUploadFragment.this).load(Uri.fromFile(new File(cancelChequeImagepath))).placeholder(R.drawable.placeholder)
                            .into(btn_cheque_card);

                    isPanSelected = true;


                }
                setButtonImage();

            }

        }*/
        /*if (requestCode == IMAGE_CHEQUE && resultCode == Activity.RESULT_OK) {
            CropImage.activity(Uri.fromFile(new File(data.getStringExtra(MyCameraActivity.FILEURI)))).start(getContext(), this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                CallMerekApi(data);
            }
        }*/

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CHEQUE) {

                Constants.hideKeyboardwithoutPopulate(getActivity());
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                cancelChequeImagepath = ImagePicker.getBitmapPath(bitmap, getActivity());
               /* Glide.with(getActivity()).load(panImagepath).into(btn_pan_card);
                isPanSelected = true;
                btn_pan_card_select.setVisibility(View.VISIBLE);
                Log.e("Pan image path", panImagepath);*/
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(cancelChequeImagepath)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);

                //new ResizeAsync().execute();
            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(getActivity(), imgURI);
                CallMerekApi();
            }
        }
    }

    int tryCount = 0;

    public void CallMerekApi() {
        /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
        Constants.hideKeyboardwithoutPopulate(getActivity());
        imgURI = result.getUri();
        cancelChequeImagepath = imgURI.getPath();*/


        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put("image", cancelChequeImagepath);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading . . .");
        Log.e("Merak", "API Called");
        new ImageUploadTask(Constants.OCRMEREK, null, fileMap, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                Log.e("MERAK RESULT", tryCount + "::" + result);
                pd.dismiss();
                try {
                    edit_ac_no.setEnabled(true);
                    edit_ac_name.setEnabled(true);
                    edit_ifsc.setEnabled(true);
                    edit_bank_name.setEnabled(true);
                    edit_bank_branch_name.setEnabled(true);
                    edit_bank_address.setEnabled(true);

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("success") == 1) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        if (dataObj != null) {
                            String ifscCode = dataObj.optString("ifsc_code");
                            String accountNumber = dataObj.optString("account_number");
                            String bank_name = dataObj.optString("bank_name");
                            String bank_branch = dataObj.optString("bank_branch");
                            String branch_address = dataObj.optString("branch_address");
                            try {
                                request_id = jsonObject.optInt("request_id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            edit_ac_no.setText(accountNumber);
                            edit_ifsc.setText(ifscCode);
                            edit_bank_name.setText(bank_name);
                            edit_bank_branch_name.setText(bank_branch);
                            edit_bank_address.setText(branch_address);

                            JSONArray payeeNames = dataObj.optJSONArray("payee_name");
                            if (payeeNames != null && payeeNames.length() > 0) {
                                String payeeName = payeeNames.getString(0);
                                edit_ac_name.setText(payeeName);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                }
//                tryCount++;
//                if (tryCount <= 100) {
//                    CallMerekApi(data);
//                }
            }
        }).execute();

//                OCRGetDetails(imgURI);
        Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                .into(btn_cheque_card);
        isPanSelected = true;

        setButtonImage();
    }

    public void storeCheque() {

        MultipartBody.Part cheque_image_part = null;


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_AC_NAME, "" + edit_ac_name.getText());
            params.put(Constants.PARAM_BANK_AC, "" + edit_ac_no.getText());
            params.put(Constants.PARAM_IFSC, "" + edit_ifsc.getText());
            params.put(Constants.PARAM_REQUEST_ID, "" + request_id);

            File imagefile = new File(cancelChequeImagepath);
            cheque_image_part = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagefile));

            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            progressDialog.setMessage("Please Wait Uploading Cheque Document...");
            progressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstoreCheque("Bearer " + sessionManager.getToken(), params, cheque_image_part);
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
                                showAlert(response.body().getResponse());
                            }
                            if (chequeDetail.getResponseCode() == 400) {
                                progressDialog.dismiss();
                                if (chequeDetail.getValidation() != null) {
                                    Validation validation = chequeDetail.getValidation();
                                    if (validation.getIfsc() != null && validation.getIfsc().length() > 0) {
                                        edit_ifsc.setError(validation.getIfsc());
                                        edit_ifsc.requestFocus();
                                        // return false;
                                    }
                                    if (validation.getBankAc() != null && validation.getBankAc().length() > 0) {
                                        edit_ac_no.setError(validation.getBankAc());
                                        edit_ac_no.requestFocus();

                                    }
                                    if (validation.getAcName() != null && validation.getAcName().length() > 0) {
                                        edit_ac_name.setError(validation.getAcName());
                                        edit_ac_name.requestFocus();

                                        // Toast.makeText(getActivity(),validation.getAgentId(),Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getRequestId() != null && validation.getRequestId().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getRequestId(), Toast.LENGTH_LONG).show();

                                    }
                                    if (validation.getCancelledCheque() != null && validation.getCancelledCheque().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getCancelledCheque(), Toast.LENGTH_LONG).show();
                                    }

                                    if (validation.getCancelledCheque() != null && validation.getCancelledCheque().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getCancelledCheque(), Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    Toast.makeText(getActivity(), chequeDetail.getStatus(), Toast.LENGTH_LONG).show();
                                }

                                // ErrorResponsePanCard errorResponsePanCard = response.body();
                            } else {

                                Constants.showAlert(edit_ac_name.getRootView(), chequeDetail.getResponse(), true);
                            }

                        } else {
                            Toast.makeText(mcontext, response.message(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(mcontext, response.message(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }


    private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Cheque Details");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                commanFragmentCallWithoutBackStack(new InfoFragment());
            }
        });
        alertDialog.show();
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
                                    edit_bank_name.setText(result.getString("bank_name").toString());
                                    edit_bank_branch_name.setText(result.getString("bank_branch_name").toString());
                                    edit_bank_address.setText(result.getString("bank_address").toString());
                                } else if (jsonObject.getInt("responseCode") == 405) {
                                    sessionManager.logoutUser(mcontext);
                                } else {
                                }
                            }
                        } else {
                            Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, cFragment);
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
}
