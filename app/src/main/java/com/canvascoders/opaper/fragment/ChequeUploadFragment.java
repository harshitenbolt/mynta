package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
import android.view.KeyEvent;
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
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.DialogueUtils;
import com.canvascoders.opaper.utils.GPSTracker;
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
    Context mcontext;
    View view;
    String bank_name = "";
    String bank_branch = "";
    String branch_address = "";
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

        btExtract = view.findViewById(R.id.btExtract);
        btn_cheque_card = (TextView) view.findViewById(R.id.tvClickCheque);
        btn_cheque_card_select = (ImageView) view.findViewById(R.id.tvClickChequeSelected);
        btn_cheque_card_select.setOnClickListener(this);
        ivChequeImage = view.findViewById(R.id.ivChequeImage);
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
        detector = new TextRecognizer.Builder(mcontext).build();
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setCancelable(false);


//        setButtonImage();


    }

   /* private void setButtonImage() {
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
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btExtract) {
            Constants.hideKeyboardwithoutPopulate(getActivity());
            if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                if (validation())
                    CallMerekApi();

                /*storeCheque()*/
                ;/*{
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

        } else if (v.getId() == R.id.tvClickCheque) {

            capture_cheque_image();

        } else if (v.getId() == R.id.tvClickChequeSelected) {
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

        if (!isPanSelected) {
            Toast.makeText(mcontext, "Please select Cheque Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CHEQUE) {
                deleteImages();
//                Constants.hideKeyboardwithoutPopulate(getActivity());
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());

                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);


            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(getActivity(), imgURI);
                Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
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
        final ProgressDialog pd = new ProgressDialog(getActivity());
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

                            DialogUtil.chequeDetail(getActivity(), accountNumber, payeeName, ifsccode, str_process_id, bank_name, bank_branch, branch_address, new DialogListner() {
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
                            });
                           /* edit_ac_no.setText(accountNumber);
                            edit_ifsc.setText(ifscCode);
                            edit_bank_name.setText(bank_name);
                            edit_bank_branch_name.setText(bank_branch);
                            edit_bank_address.setText(branch_address);*/


                        }
                    } else {
                        DialogUtil.chequeDetail(getActivity(), "", "", "", str_process_id, "", "", "", new DialogListner() {
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
                        });
                        Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    cancelChequeImagepath = "";
                    Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.checkimage)
                            .into(ivChequeImage);
                    isPanSelected = false;
                    btn_cheque_card.setVisibility(View.VISIBLE);
                    btn_cheque_card_select.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();

                }

            }
        }).execute();

        Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                .into(ivChequeImage);
        isPanSelected = true;

    }

    public void storeCheque(String acname, String payeename, String ifsc, String bankname, String branchname, String branchAddress) {
        gps = new GPSTracker(getActivity());
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
                                deleteImages();
                                DialogUtil.dismiss();
                                showAlert(response.body().getResponse());
                            }
                            if (chequeDetail.getResponseCode() == 411) {
                                sessionManager.logoutUser(mcontext);
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


                                } else {
                                    Toast.makeText(getActivity(), chequeDetail.getStatus(), Toast.LENGTH_LONG).show();
                                }

                                // ErrorResponsePanCard errorResponsePanCard = response.body();
                            } else {
                                Toast.makeText(getActivity(), chequeDetail.getResponse(), Toast.LENGTH_LONG).show();
                                // Constants.showAlert(edit_ac_name.getRootView(), chequeDetail.getResponse(), true);
                            }

                        } else {
                            Toast.makeText(mcontext, response.message(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
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

        dialog = new Dialog(mcontext);
        dialog = new Dialog(getActivity(), R.style.DialogLSideBelow);
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
                commanFragmentCallWithoutBackStack(new InfoFragment());

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
                                    sessionManager.logoutUser(mcontext);
                                } else if (jsonObject.getInt("responseCode") == 411) {
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
            Constants.ShowNoInternet(mcontext);
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    deleteImages();

                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });


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
