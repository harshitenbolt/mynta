package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.PanCardOcrResponse.PanCardSubmitResponse;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.Beans.UpdatePanResponse.UpdatePancardResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.PanApprovalPending;
import com.canvascoders.opaper.fragment.TaskCompletedFragment2;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditPanCardActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog mProgressDialog;
    private String str_process_id, panImagepath = "", cameraimage = "";
    private SessionManager sessionManager;
    private ImageView ivStoreImage, ivPanImage, ivBack, ivPanImageSelected;
    VendorList vendor;
    private TextView tvPanClick, tvPanName, tvPanFatherName, tvPanNo;

    private static final int IMAGE_PAN = 101;
    private Uri imgURI;
    private String lattitude = "", longitude = "";
    boolean isPanSelected = false;
    public static final int CROPPED_IMAGE = 5333;
    private EditText etPanName, etFatherName, etpanNumber;
    private Button btSubmit;
    boolean individual = true;
    String panno = "";
    GPSTracker gps;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etFatherName.getText().toString().length() > 1 && etPanName.getText().toString().length() > 0 && etpanNumber.getText().toString().length() > 3) {
                // setButtonImage();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private RequestPermissionHandler requestPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pan_card);
        sessionManager = new SessionManager(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendor = (VendorList) bundle.getSerializable("data");
            str_process_id = String.valueOf(vendor.getProccessId());
            Log.e("process_id", str_process_id);

        }
        init();
        requestPermissionHandler = new RequestPermissionHandler();


    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCancelable(false);


        etFatherName = findViewById(R.id.etFatherName);
        etPanName = findViewById(R.id.etPanName);
        etpanNumber = findViewById(R.id.etPanNumber);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        ivPanImage = findViewById(R.id.ivPanImage);
        tvPanClick = findViewById(R.id.ivPanCard);
        ivPanImageSelected = findViewById(R.id.tvClickPanSelected);
        tvPanClick.setOnClickListener(this);
        tvPanName = findViewById(R.id.tvPanName);
        tvPanFatherName = findViewById(R.id.tvPanFatherName);
        tvPanNo = findViewById(R.id.tvPanNo);
        ivStoreImage = findViewById(R.id.ivStoreImage);

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            // getBankDetails(mContext,s.toString(),processId);
            ApiCallGetDetails();
        } else {
            Constants.ShowNoInternet(EditPanCardActivity.this);
        }
        //  ApiCallGetDetails();
    }


    private void capture_pan_card_image() {
        requestPermissionHandler.requestPermission(EditPanCardActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditPanCardActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_PAN);
               /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_PAN);*/
            }

            @Override
            public void onFailed() {
                Toast.makeText(EditPanCardActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ApiCallGetDetails() {
        mProgressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PROCESS_ID, str_process_id);
        Call<UpdatePanDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).pandetailResponse("Bearer " + sessionManager.getToken(), param);
        call.enqueue(new Callback<UpdatePanDetailResponse>() {
            @Override
            public void onResponse(Call<UpdatePanDetailResponse> call, retrofit2.Response<UpdatePanDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    UpdatePanDetailResponse updatePanDetailResponse = response.body();
                    if (updatePanDetailResponse.getResponseCode() == 200) {
                        tvPanName.setText(updatePanDetailResponse.getData().get(0).getPanName());

                        tvPanFatherName.setText(updatePanDetailResponse.getData().get(0).getFatherName());
                        tvPanNo.setText(updatePanDetailResponse.getData().get(0).getPanNo());
                        panno = updatePanDetailResponse.getData().get(0).getPanNo();


                        char first = panno.charAt(3);
                        String word = String.valueOf(first);
                        if (word.equalsIgnoreCase("P")) {
                            individual = true;
                        } else {
                            individual = false;
                        }

                        Glide.with(EditPanCardActivity.this).load(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan()).into(ivStoreImage);
                        //isPanSelected = true;
                        // ivPanImageSelected.setVisibility(View.VISIBLE);
                       /* etpanNumber.setEnabled(true);
                        etFatherName.setEnabled(true);
                        etPanName.setEnabled(true);*/
                        //setButtonImage();

                       /* Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    Bitmap bitmap = ImagePicker.getBitmapFromURL(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan());
                                    panImagepath = ImagePicker.getBitmapPath(bitmap, getApplicationContext());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();*/

                       /* try {

                            Bitmap bitmap = ImagePicker.getBitmapFromURL(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan());
                            panImagepath = ImagePicker.getBitmapPath(bitmap, EditPanCardActivity.this);

                        }
                        catch (Exception e){
                       //     Toast.makeText(EditPanCardActivity.this, "Server has no image ", Toast.LENGTH_SHORT).show();
                        }                     */   // ExtractPanDetail();
                    } else if (updatePanDetailResponse.getResponseCode() == 202) {
                        PanApprovalPending panApprovalPending = new PanApprovalPending();
                        panApprovalPending.setMesssge("", str_process_id);
                        commanFragmentCallWithoutBackStack(panApprovalPending);
                    } else if (updatePanDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditPanCardActivity.this);
                    } else {
                        etPanName.setEnabled(true);
                        etFatherName.setEnabled(true);
                        etpanNumber.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePanDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSubmit:
                if (validation()) {
                    Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                        ExtractPanDetail();
                    } else {
                        Constants.ShowNoInternet(EditPanCardActivity.this);
                    }
                }
                break;


            case R.id.ivPanCard:
                capture_pan_card_image();
                break;

            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void UpadatePan(String name, String fathername, String panId, String storename) {
       /* gps = new GPSTracker(EditPanCardActivity.this);
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
        }*/

        mProgressDialog.show();

        MultipartBody.Part pan_card_part = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_PAN_NO, "" + panId);
        params.put(Constants.PARAM_PAN_NAME, "" + name);
        params.put(Constants.PARAM_FATHER_NAME, "" + fathername);
        if (storename.equalsIgnoreCase("")) {

        } else {
            params.put(Constants.PARAM_STORE_NAME, storename);
        }
       /* params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);*/
        File imagefile = new File(panImagepath);
        pan_card_part = MultipartBody.Part.createFormData(Constants.PARAM_PAN_CARD_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));

        Call<UpdatePancardResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).updatePanDetails("Bearer " + sessionManager.getToken(), params, pan_card_part);

        callUpload.enqueue(new Callback<UpdatePancardResponse>() {
            @Override
            public void onResponse(Call<UpdatePancardResponse> call, Response<UpdatePancardResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {


                    UpdatePancardResponse updatePancardResponse = response.body();
                    if (updatePancardResponse.getResponseCode() == 200) {
                        deleteImages();
                        File casted_image = new File(cameraimage);
                        if (casted_image.exists()) {
                            casted_image.delete();
                        }
                        File casted_image1 = new File(panImagepath);
                        if (casted_image1.exists()) {
                            casted_image1.delete();
                        }


                        Toast.makeText(EditPanCardActivity.this, updatePancardResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (updatePancardResponse.getResponseCode() == 400) {
                        mProgressDialog.dismiss();
                        try {
                            if (updatePancardResponse.getValidation() != null) {

                                Validation validation = updatePancardResponse.getValidation();
                                if (validation.getPanName() != null && validation.getPanName().length() > 0) {

                                    DialogUtil.etPanName.setError(validation.getPanName());

                                }
                                if (validation.getFatherName() != null && validation.getFatherName().length() > 0) {

                                    DialogUtil.etPanFatherName.setError(validation.getFatherName());
                                }

                                if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {

                                    DialogUtil.etStoreName.setError(validation.getStoreName());
                                }


                                if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getPanNo() != null && validation.getPanNo().length() > 0) {
                                    DialogUtil.etPanNumber.setError(validation.getPanNo());
                                }
                                if (validation.getPanCardFront() != null && validation.getPanCardFront().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getPanCardFront(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(EditPanCardActivity.this, updatePancardResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditPanCardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // ErrorResponsePanCard errorResponsePanCard = response.body();
                    } else {
                        Toast.makeText(EditPanCardActivity.this, updatePancardResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePancardResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditPanCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean validation() {

        if (!isPanSelected) {
            Toast.makeText(EditPanCardActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (panImagepath.equalsIgnoreCase("")) {
            Toast.makeText(EditPanCardActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PAN) {
                deleteImages();
//                Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                Bitmap bitmap = ImagePicker.getImageFromResult(EditPanCardActivity.this, resultCode, data);
                cameraimage = ImagePicker.getBitmapPath(bitmap, EditPanCardActivity.this);
               /* Glide.with(getActivity()).load(panImagepath).into(btn_pan_card);
                isPanSelected = true;
                btn_pan_card_select.setVisibility(View.VISIBLE);
                Log.e("Pan image path", panImagepath);*/
                Intent intent = new Intent(EditPanCardActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(cameraimage)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);


                //new ResizeAsync().execute();
            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                panImagepath = RealPathUtil.getPath(EditPanCardActivity.this, imgURI);

                try {
                    Glide.with(EditPanCardActivity.this).load(imgURI).into(ivPanImage);
                    isPanSelected = true;
                    ivPanImageSelected.setVisibility(View.VISIBLE);

                    tvPanClick.setVisibility(View.GONE);
                    // ExtractPanDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    public void ExtractPanDetail() {

        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        if (!TextUtils.isEmpty(panImagepath)) {

            mProgressDialog.setMessage("Image extracting...");
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
                            // deleteImages();
                            GetPanDetailsResponse getPanDetailsResponse = response.body();
                            if (getPanDetailsResponse.getStatus().equalsIgnoreCase("success")) {
                                Toast.makeText(EditPanCardActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();


                                DialogUtil.PanDetail2(EditPanCardActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), individual, new DialogListner() {
                                    @Override
                                    public void onClickPositive() {

                                    }

                                    @Override
                                    public void onClickNegative() {

                                    }

                                    @Override
                                    public void onClickDetails(String name, String fathername, String storename, String id) {

                                        if (storename.equalsIgnoreCase("")) {
                                            Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                UpadatePan(name, fathername, id, "");
                                            } else {
                                                Constants.ShowNoInternet(EditPanCardActivity.this);
                                            }

                                        } else {

                                            Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                UpadatePan(name, fathername, id, storename);
                                            } else {
                                                Constants.ShowNoInternet(EditPanCardActivity.this);
                                            }
                                        }


                                    }

                                    @Override
                                    public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                    }
                                });

                            } else {
                                if (getPanDetailsResponse.getReselectImage() != null && getPanDetailsResponse.getReselectImage().equalsIgnoreCase("1")) {
                                    panImagepath = "";
                                    Glide.with(EditPanCardActivity.this).load(panImagepath).placeholder(R.drawable.pancard).into(ivPanImage);
                                    isPanSelected = false;
                                    ivPanImageSelected.setVisibility(View.GONE);
                                    tvPanClick.setVisibility(View.VISIBLE);

                                } else {


                                    Toast.makeText(EditPanCardActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                    DialogUtil.PanDetail2(EditPanCardActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), individual, new DialogListner() {
                                        @Override
                                        public void onClickPositive() {

                                        }

                                        @Override
                                        public void onClickNegative() {

                                        }

                                        @Override
                                        public void onClickDetails(String name, String fathername, String storename, String id) {

                                            if (storename.equalsIgnoreCase("")) {
                                                Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                    UpadatePan(name, fathername, id, "");
                                                } else {
                                                    Constants.ShowNoInternet(EditPanCardActivity.this);
                                                }

                                            } else {

                                                Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                    UpadatePan(name, fathername, id, storename);
                                                } else {
                                                    Constants.ShowNoInternet(EditPanCardActivity.this);
                                                }
                                            }


                                        }

                                        @Override
                                        public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                        }
                                    });
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditPanCardActivity.this, "NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetPanDetailsResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditPanCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    private void deleteImages() {

        File casted_image = new File(cameraimage);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(panImagepath);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }
    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvmain, cFragment);
            fragmentTransaction.commit();

        }
    }

}
