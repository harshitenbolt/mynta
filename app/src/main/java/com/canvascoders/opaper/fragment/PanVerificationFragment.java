package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.PanCardOcrResponse.PanCardSubmitResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImageUtils;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class PanVerificationFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_PAN = 101;
    private TextView tvClickPan;
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    File sourceFile;
    int totalSize = 0;
    ProgressDialog pd;
    private String TAG = "PanVerification";
    private Button btn_next, btn_extract;
    private String panImagepath = "", imagecamera = "", identityType = "", identityEmail = "", identityCallbackUrl = "", identityAccessToken = "", identityID = "", identityPatronId = "";
    private Uri imgURI;
    private static Dialog dialog;
    public static LinearLayout llGoback;
    private ImageView ivPanImage;
    Context mcontext;
    private boolean isedit = false;
    public static final int CROPPED_IMAGE = 5333;
    View view;
    String email = "", storename = "", ownername = "", dateofbirth = "", storeaddress = "", storeaddress1 = "", storeaddresslandmark = "", storeaddressPincode = "", storeaddressCity = "", storeaddressState = "", dc = "", route = "";
    String str_process_id, authUserID, authCreated, authID;
    private long authTTL;
    private TextView tvGoBack;
    private String file_name, file_url, pan_card_detail_id, birth_date = "";
    private RequestPermissionHandler requestPermissionHandler;
    private ImageView btn_pan_card_select;
    private SessionManager sessionManager;
    private boolean isPanSelected = false;
    private EditText edit_pan_name, edit_pan_name_father, edit_pan_number;
    private String lattitude = "", longitude = "";
    GPSTracker gps;
    /*TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edit_pan_name.getText().toString().length() > 1 && edit_pan_name_father.getText().toString().length() > 0 && edit_pan_number.getText().toString().length() > 3) {
               // setButtonImage();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };*/
    private String from = "individualPan";
    private JSONObject panEssentials = new JSONObject();
    private ProgressDialog mProgressDialog;
    Bundle bundle;

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
        view = inflater.inflate(R.layout.fragment_pan, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        pd = new ProgressDialog(mcontext);

        OTPActivity.settitle(Constants.TITLE_PAN_CARD_VERIFICATION);

        requestPermissionHandler = new RequestPermissionHandler();

        initView();

        return view;

    }

    private void initView() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Please wait, While we are uploading...");
        mProgressDialog.setCancelable(false);

        ivPanImage = view.findViewById(R.id.ivPanImage);
        btn_extract = view.findViewById(R.id.bgtExtractPan);
        tvClickPan = view.findViewById(R.id.tvClickPan);
        tvClickPan.setOnClickListener(this);
        //  tvGoBack= view.findViewById(R.id.tvGoBack);
        llGoback = view.findViewById(R.id.llGonext);
        llGoback.setOnClickListener(this);

        btn_pan_card_select = (ImageView) view.findViewById(R.id.tvClickPanSelected);
        btn_pan_card_select.setOnClickListener(this);
        btn_extract.setOnClickListener(this);


        bundle = this.getArguments();
        if (bundle != null) {
            String is_edit = bundle.getString(Constants.KEY_EMP_MOBILE);
            email = bundle.getString(Constants.KEY_EMAIL);
            storename = bundle.getString(Constants.KEY_STORES);
            ownername = bundle.getString(Constants.KEY_NAME);
            dateofbirth = bundle.getString(Constants.PARAM_BIRTH_DATE);
            storeaddress = bundle.getString(Constants.PARAM_STORE_ADDRESS);
            storeaddress1 = bundle.getString(Constants.PARAM_STORE_ADDRESS1);
            storeaddresslandmark = bundle.getString(Constants.PARAM_STORE_ADDRESS_LANDMARK);
            storeaddressPincode = bundle.getString(Constants.PARAM_PINCODE);
            storeaddressCity = bundle.getString(Constants.PARAM_CITY);
            storeaddressState = bundle.getString(Constants.PARAM_STATE);
            dc = bundle.getString(Constants.PARAM_DC);
            route = bundle.getString(Constants.PARAM_ROUTE);
            if (is_edit != null) {
                Log.e("isedit", is_edit);
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    isedit = true;
                    llGoback.setVisibility(View.VISIBLE);
                    llGoback.setEnabled(false);
                    ApiCallGetDetails();
                } else {
                    Constants.ShowNoInternet(mcontext);
                }
            }
        }
    }

    private void ApiCallGetDetails() {
        mProgressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PROCESS_ID, str_process_id);
        Call<UpdatePanDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).pandetailResponse("Bearer " + sessionManager.getToken(), param);
        call.enqueue(new Callback<UpdatePanDetailResponse>() {
            @Override
            public void onResponse(Call<UpdatePanDetailResponse> call, retrofit2.Response<UpdatePanDetailResponse> response) {

                if (response.isSuccessful()) {
                    UpdatePanDetailResponse updatePanDetailResponse = response.body();
                    if (updatePanDetailResponse.getResponseCode() == 200) {
                        mProgressDialog.dismiss();

                        Glide.with(getActivity()).load(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan()).placeholder(R.drawable.pancard).into(ivPanImage);
                        isPanSelected = true;
                        btn_pan_card_select.setVisibility(View.VISIBLE);
                        Bitmap bitmap = ImagePicker.getBitmapFromURL(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan());
                        panImagepath = ImagePicker.getBitmapPath(bitmap, getActivity());
                        // llGoback.setEnabled(true);
                        DialogUtil.PanDetail(getActivity(), updatePanDetailResponse.getData().get(0).getPanName(), updatePanDetailResponse.getData().get(0).getFatherName(), updatePanDetailResponse.getData().get(0).getPanNo(), new DialogListner() {
                            @Override
                            public void onClickPositive() {

                            }

                            @Override
                            public void onClickNegative() {

                            }

                            @Override
                            public void onClickDetails(String name, String fathername, String dob, String id) {

                                Constants.hideKeyboardwithoutPopulate(getActivity());
                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    // storePanwithOCR(name, fathername, id, pan_card_detail_id, file_name, file_url, birth_date);
                                    storePAN(name, fathername, id);
                                } else {
                                    Constants.ShowNoInternet(getActivity());
                                }

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }
                        });


                        // ExtractPanDetail();
                    } else if (updatePanDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(getActivity());
                    } else {

                        DialogUtil.PanDetail(getActivity(), updatePanDetailResponse.getData().get(0).getPanName(), updatePanDetailResponse.getData().get(0).getFatherName(), updatePanDetailResponse.getData().get(0).getPanNo(), new DialogListner() {
                            @Override
                            public void onClickPositive() {

                            }

                            @Override
                            public void onClickNegative() {

                            }

                            @Override
                            public void onClickDetails(String name, String fathername, String dob, String id) {

                                Constants.hideKeyboardwithoutPopulate(getActivity());
                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    // storePanwithOCR(name, fathername, id, pan_card_detail_id, file_name, file_url, birth_date);
                                    storePAN(name, fathername, id);
                                } else {
                                    Constants.ShowNoInternet(getActivity());
                                }

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<UpdatePanDetailResponse> call, Throwable t) {
                llGoback.setEnabled(true);
                Toast.makeText(getActivity(), "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }

    /* private void setButtonImage() {
         if (isPanSelected == true) {
             btn_next.setBackground(getResources().getDrawable(R.drawable.btn_active));
             btn_next.setEnabled(true);
             btn_next.setTextColor(getResources().getColor(R.color.colorWhite));
         } else {
             btn_next.setBackground(getResources().getDrawable(R.drawable.btn_normal));
             btn_next.setEnabled(false);
             btn_next.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
         }
     }
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //check permition
    private void capture_pan_card_image() {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                startActivityForResult(chooseImageIntent, IMAGE_PAN);
               /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_PAN);*/
            }

            @Override
            public void onFailed() {
                Toast.makeText(mcontext, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    //why this is not used ?
    private boolean validation() {

        if (!isPanSelected) {
            Toast.makeText(mcontext, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (validation()) {
                Constants.hideKeyboardwithoutPopulate(getActivity());
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                   /* storePanwithOCR();
                    storePAN();*/
                } else {
                    Constants.ShowNoInternet(getActivity());
                }
            }
        } else if (v.getId() == R.id.tvClickPan) {

            capture_pan_card_image();


        } else if (v.getId() == R.id.tvClickPanSelected) {
            capture_pan_card_image();
        } else if (v.getId() == R.id.bgtExtractPan) {
            try {


                if (validation()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ExtractPanDetail();
                    } else {
                        Constants.ShowNoInternet(mcontext);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.llGonext) {
            commanFragmentCallWithoutBackStack2(new InfoFragment());
        }

    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PAN) {

                //  Constants.hideKeyboardwithoutPopulate(getActivity());
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);

            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                panImagepath = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(imgURI).placeholder(R.drawable.pancard).into(ivPanImage);
                    isPanSelected = true;
                    btn_pan_card_select.setVisibility(View.VISIBLE);
                    tvClickPan.setVisibility(View.GONE);

                    File casted_image6 = new File(imagecamera);
                    if (casted_image6.exists()) {
                        casted_image6.delete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private void storePanwithOCR(String name, String fathername, String pannumber, String pancardDetailId, String filename, String FileUrl, String Birthdate) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PAN_CARD_DETAIL_ID, pancardDetailId);
        param.put(Constants.PARAM_PAN_CARD_NUMBER, pannumber);
        param.put(Constants.PARAM_NAME, name);
        param.put(Constants.PARAM_FATHER_NAME, fathername);
        param.put(Constants.PARAM_FILE_NAME, filename);
        param.put(Constants.PARAM_FILE_URL, FileUrl);
        param.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        param.put(Constants.PARAM_PROCESS_ID, str_process_id);
        Call<PanCardSubmitResponse> call = ApiClient.getClient2().create(ApiInterface.class).SubmitPancardOCR(param);
        call.enqueue(new Callback<PanCardSubmitResponse>() {
            @Override
            public void onResponse(Call<PanCardSubmitResponse> call, retrofit2.Response<PanCardSubmitResponse> response) {
                if (response.isSuccessful()) {
                    PanCardSubmitResponse panCardDetail = response.body();
                    if (panCardDetail.getStatus().equalsIgnoreCase("success")) {
                        // Toast.makeText(getActivity(), panCardDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), panCardDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2027 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<PanCardSubmitResponse> call, Throwable t) {
                //    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "#errorcode :- 2027 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void storePAN(String name, String fathername, String id) {
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        MultipartBody.Part pan_card_part = null;

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_PAN_NO, id);
            params.put(Constants.PARAM_PAN_NAME, name);
            params.put(Constants.PARAM_FATHER_NAME, fathername);
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);
            if (isedit == true) {
                params.put(Constants.PARAM_IS_EDIT, "1");

            }

            File imagefile = new File(panImagepath);
            pan_card_part = MultipartBody.Part.createFormData(Constants.PARAM_PAN_CARD_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));

            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Please wait ...");
            mProgressDialog.show();
            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstorePancard("Bearer " + sessionManager.getToken(), params, pan_card_part);
            callUpload.enqueue(new Callback<CommonResponse>() {


                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }


                    if (response.isSuccessful()) {
                        //  deleteImages();

                        CommonResponse panVerificationDetail = response.body();

                        if (panVerificationDetail.getResponseCode() == 200) {
                            deleteImages();
                            str_process_id = panVerificationDetail.getData().get(0).getProccess_id();
                            DialogUtil.dismiss();
                            showAlert(response.body().getResponse());
                        }
                        if (panVerificationDetail.getResponseCode() == 411) {
                            deleteImages();
                            sessionManager.logoutUser(mcontext);
                        }

                        if (panVerificationDetail.getResponseCode() == 400) {
                            mProgressDialog.dismiss();
                            try {
                                if (panVerificationDetail.getValidation() != null) {

                                    Validation validation = panVerificationDetail.getValidation();
                                    if (validation.getPanName() != null && validation.getPanName().length() > 0) {
                                        DialogUtil.etPanName.setError(validation.getPanName());
                                        DialogUtil.etPanName.requestFocus();
                                    }
                                    if (validation.getFatherName() != null && validation.getFatherName().length() > 0) {
                                        DialogUtil.etPanFatherName.setError(validation.getFatherName());
                                        DialogUtil.etPanFatherName.requestFocus();
                                    }
                                    if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                                    }
                                    if (validation.getPanNo() != null && validation.getPanNo().length() > 0) {
                                        DialogUtil.etPanNumber.setError(validation.getPanNo());
                                        DialogUtil.etPanNumber.requestFocus();
                                    }
                                    if (validation.getPanCardFront() != null && validation.getPanCardFront().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getPanCardFront(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), panVerificationDetail.getResponse(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(mcontext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                            // ErrorResponsePanCard errorResponsePanCard = response.body();
                        } else {
                            Toast.makeText(mcontext, panVerificationDetail.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                        if (panVerificationDetail.getResponseCode() == 405) {
                            deleteImages();
                            sessionManager.logoutUser(mcontext);
                        }

                    } else {
                        Toast.makeText(mcontext, "#errorcode :- 2037 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mcontext, "#errorcode :- 2037 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }


  /*  private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("PAN Details");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isedit == true) {
                    commanFragmentCallWithoutBackStack(new InfoFragment());
                } else {
                    commanFragmentCallWithoutBackStack(new ChequeUploadFragment());
                }

            }
        });
        alertDialog.show();
    }*/


    public void ExtractPanDetail() {

        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        if (!TextUtils.isEmpty(panImagepath)) {

            mProgressDialog.setMessage("Extracting image..");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            File imagefile = new File(panImagepath);
            typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));

           /* Call<PanImageResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getPancardOcrUrl("Bearer "+sessionManager.getToken(),sessionManager.getToken(), str_process_id, typedFile);

            callUpload.enqueue(new Callback<PanImageResponse>() {
                @Override
                public void onResponse(Call<PanImageResponse> call, retrofit2.Response<PanImageResponse> response) {
                    mProgressDialog.dismiss();

                    if (response.isSuccessful()) {
                        PanImageResponse panImageResponse = response.body();
                        if (panImageResponse.getResponseCode() == 200) {
                            String imagePath = panImageResponse.getData().get(0).getPan_url();
                            if (!TextUtils.isEmpty(imagePath)) {
                                ExtractPanDetail extractPanDetail = new ExtractPanDetail();
                                extractPanDetail.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePath);
                            } else {

                            }
                        } else if (panImageResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        } else {
                            Toast.makeText(mcontext, panImageResponse.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PanImageResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mcontext, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });*/


            Call<GetPanDetailsResponse> call = ApiClient.getClient2().create(ApiInterface.class).getPanDetails(params, typedFile);
            call.enqueue(new Callback<GetPanDetailsResponse>() {
                @Override
                public void onResponse(Call<GetPanDetailsResponse> call, retrofit2.Response<GetPanDetailsResponse> response) {
                    mProgressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            GetPanDetailsResponse getPanDetailsResponse = response.body();
                            if (getPanDetailsResponse.getStatus().equalsIgnoreCase("success")) {
                                Toast.makeText(getActivity(), getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                               /* edit_pan_name.setText(getPanDetailsResponse.getPanCardDetail().getName());
                                edit_pan_name_father.setText(getPanDetailsResponse.getPanCardDetail().getFatherName());
                                edit_pan_number.setText(getPanDetailsResponse.getPanCardDetail().getPanCardNumber());*/
                                pan_card_detail_id = String.valueOf(getPanDetailsResponse.getPanCardDetail().getPanCardDetailId());
                                file_name = getPanDetailsResponse.getPanCardDetail().getFileName();
                                file_url = getPanDetailsResponse.getPanCardDetail().getFileUrl();
                                birth_date = getPanDetailsResponse.getPanCardDetail().getBirthDate();


                                DialogUtil.PanDetail(getActivity(), getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), new DialogListner() {
                                    @Override
                                    public void onClickPositive() {

                                    }

                                    @Override
                                    public void onClickNegative() {

                                    }

                                    @Override
                                    public void onClickDetails(String name, String fathername, String dob, String id) {

                                        Constants.hideKeyboardwithoutPopulate(getActivity());
                                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                            storePanwithOCR(name, fathername, id, pan_card_detail_id, file_name, file_url, birth_date);
                                            storePAN(name, fathername, id);
                                        } else {
                                            Constants.ShowNoInternet(getActivity());
                                        }

                                    }

                                    @Override
                                    public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                    }

                                    @Override
                                    public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                    }
                                });

                            } else {
                                if (getPanDetailsResponse.getReselectImage() != null && getPanDetailsResponse.getReselectImage().equalsIgnoreCase("1")) {
                                    panImagepath = "";
                                    Glide.with(getActivity()).load(panImagepath).placeholder(R.drawable.pancard).into(ivPanImage);
                                    isPanSelected = false;
                                    btn_pan_card_select.setVisibility(View.GONE);
                                    tvClickPan.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {

                                    pan_card_detail_id = String.valueOf(getPanDetailsResponse.getPanCardDetail().getPanCardDetailId());
                                    file_name = getPanDetailsResponse.getPanCardDetail().getFileName();
                                    file_url = getPanDetailsResponse.getPanCardDetail().getFileUrl();
                                    birth_date = getPanDetailsResponse.getPanCardDetail().getBirthDate();
                                    Toast.makeText(getActivity(), getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                    DialogUtil.PanDetail(getActivity(), getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), new DialogListner() {
                                        @Override
                                        public void onClickPositive() {

                                        }

                                        @Override
                                        public void onClickNegative() {

                                        }

                                        @Override
                                        public void onClickDetails(String name, String fathername, String dob, String id) {

                                            Constants.hideKeyboardwithoutPopulate(getActivity());
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                storePanwithOCR(name, fathername, id, pan_card_detail_id, file_name, file_url, birth_date);
                                                storePAN(name, fathername, id);
                                            } else {
                                                Constants.ShowNoInternet(getActivity());
                                            }

                                        }

                                        @Override
                                        public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                        }

                                        @Override
                                        public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                        }
                                    });

                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetPanDetailsResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();

                    //   Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public class ResizeAsync extends AsyncTask<Void, Void, File> {

        @Override
        protected File doInBackground(Void... voids) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            Bitmap out = ImageUtils.getInstant().getCompressedBitmap(panImagepath);

            File file = new File(dir, "pan_" + System.currentTimeMillis() + ".png");
            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(file);
                out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                out.recycle();
            } catch (Exception e) {
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                panImagepath = file.getPath();
                Glide.with(getActivity())
                        .load(Uri.fromFile(file))
                        .placeholder(R.drawable.placeholder)
                        .into(ivPanImage);
                //.transform(new RotateTransformation(this, 90f))
                isPanSelected = true;
                btn_pan_card_select.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ExtractPanDetail extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Pan card (Auto reading and verification)");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes = "";
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");

                JSONObject jsonLoginCheck = new JSONObject();
                jsonLoginCheck.put(Constants.PARAM_USER_NAME, Constants.UserName);
                jsonLoginCheck.put(Constants.PARAM_PASSWORD, Constants.PasswordORApiKey);

                RequestBody body = RequestBody.create(mediaType, jsonLoginCheck.toString());

                Request requestLogin = new Request.Builder()
                        .url(Constants.singzyBaseURL + "patrons/login")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                if (responseLogin.isSuccessful()) {
                    final String ress = responseLogin.body().string();
                    final String Message = responseLogin.message();

                    Mylogger.getInstance().Logit(TAG + "1", ress);

                    try {

                        JSONObject resultLogin = new JSONObject(ress);
                        authID = resultLogin.optString("id").toString();
                        authTTL = resultLogin.optLong("ttl");
                        authCreated = resultLogin.optString("created");
                        authUserID = resultLogin.optString("userId");

                        //  call API for get Identity from signzy to get  identities
                        if (authID != null && authID.length() > 0 && authUserID != null && authUserID.length() > 0) {
                            JSONObject param = new JSONObject();
                            param.put("type", from);
                            param.put("callbackUrl", "http://www.opaper.in/");// need to replace
                            param.put("email", Constants.Email);// need to replace
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(params[0]);
                            param.put("images", jsonArray);

                            RequestBody bodyauthi = RequestBody.create(mediaType, param.toString());

                            Request requestauthi = new Request.Builder()
                                    .url(Constants.singzyBaseURL + "patrons/" + authUserID + "/identities")
                                    .post(bodyauthi)
                                    .addHeader("accept-language", "en-US,en;q=0.8")
                                    .addHeader("content-type", "application/json")
                                    .addHeader("accept", "*/*")
                                    .addHeader("authorization", authID)

                                    .build();

                            Response response2 = client.newCall(requestauthi).execute();

                            if (response2.isSuccessful()) {
                                final String Message1 = response2.message();
                                String tmp = response2.body().string();
                                Mylogger.getInstance().Logit(TAG + "2", tmp);

                                if (Message1.equals("OK")) {

                                    JSONObject res = new JSONObject(tmp);

                                    identityAccessToken = res.getString("accessToken");
                                    identityCallbackUrl = res.getString("callbackUrl");
                                    identityEmail = res.getString("email");
                                    identityID = res.getString("id");
                                    identityPatronId = res.getString("patronId");
                                    identityType = res.getString("type");


                                    JSONObject pan = new JSONObject();
                                    pan.put(Constants.PARAM_SERVICE, "Identity");
                                    pan.put(Constants.PARAM_ITEMID, identityID);
                                    pan.put(Constants.PARAM_ACCESSTOKEN, identityAccessToken);
                                    pan.put(Constants.PARAM_TASK, "autoRecognition");
                                    pan.put(Constants.PARAM_ESSENTIALS, panEssentials);

                                    Mylogger.getInstance().Logit(TAG + "param", pan.toString());

                                    RequestBody bodyAadhar = RequestBody.create(mediaType, pan.toString());
                                    Request requestAadhar = new Request.Builder()
                                            .url(Constants.singzyBaseURL + "snoops")
                                            //
                                            .post(bodyAadhar)
                                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                                            .build();

                                    Response responseAadhar = client.newCall(requestAadhar).execute();

                                    myRes = responseAadhar.body().string();
                                    panEssentials = null;
                                    Mylogger.getInstance().Logit(TAG + "pandata", myRes);
                                }

                            } else {
                                myRes = response2.body().string();
                            }
                        }
                    } catch (Exception e) {
                        Mylogger.getInstance().Logit(TAG, "Pan admin Auth fail");
                    }

                } else {
                    Mylogger.getInstance().Logit(TAG, "Pan admin Auth fail");
                }


                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return myRes;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            edit_pan_name.setEnabled(true);
            edit_pan_name_father.setEnabled(true);
            edit_pan_number.setEnabled(true);
            Mylogger.getInstance().Logit(TAG, message);
            try {
                JSONObject jsonObject = new JSONObject(message);

                JSONObject response = jsonObject.getJSONObject("response");
                JSONObject result = response.getJSONObject("result");
                if (result.has("name")) {
                    edit_pan_name.setText(result.getString("name"));
                }

                if (result.has("fatherName")) {
                    edit_pan_name_father.setText(result.getString("fatherName"));
                }

                if (result.has("number")) {
                    edit_pan_number.setText(result.getString("number"));
                }

                //  setButtonImage();
            } catch (Exception e) {
                //  e.printStackTrace();
                Constants.showAlert(edit_pan_name.getRootView(), "Network error please try again later", false);
            }

        }
    }

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        String line = "{'test':'a'}";

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            pd.setMessage("Uploading");
            pd.setCancelable(false);
            pd.show();
            sourceFile = new File(imgURI.getPath());
            totalSize = (int) sourceFile.length();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pd.setMessage("Uploading (" + progress[0] + "%)");
            Mylogger.getInstance().Logit(TAG, "" + progress[0]);
            //donut_progress.setProgress(Integer.parseInt(progress[0])); //Updating progress
        }

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = sourceFile.getName();

            try {
                connection = (HttpURLConnection) new URL("http://www.canvascoders.com/nikhil/UploadToServer.php").openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = sourceFile.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress("" + (int) ((progress * 100) / totalSize)); // sending progress percent to publishProgress
                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return line;
            } catch (Exception e) {
                Mylogger.getInstance().Logit(TAG, e.getMessage());
            } finally {
                if (connection != null) connection.disconnect();
            }
            return line;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    Mylogger.getInstance().Logit(TAG, jsonObject.toString());
                } else {
                    Mylogger.getInstance().Logit(TAG, "Null response");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

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

    public void commanFragmentCallWithoutBackStack2(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
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


    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    private void showAlert(String msg) {
        Button btSubmit;
        TextView tvMessage, tvTitle;
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
        tvTitle.setText("PAN VERIFICATION");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isedit == true) {
                    commanFragmentCallWithoutBackStack(new InfoFragment());
                } else {
                    commanFragmentCallWithoutBackStack(new ChequeUploadFragment());
                }

            }
        });

        dialog.setCancelable(false);

        dialog.show();

    }


    @Override
    public void onResume() {

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

        File casted_image = new File(panImagepath);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(imagecamera);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }

}

