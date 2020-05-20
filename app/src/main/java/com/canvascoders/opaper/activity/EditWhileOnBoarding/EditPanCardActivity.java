package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetGSTVerify.GetGSTVerify;
import com.canvascoders.opaper.Beans.GetGSTVerify.StoreAddress;
import com.canvascoders.opaper.Beans.GetGstPanEditResponse.GetGstPanEditResponse;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.Beans.UpdatePanResponse.UpdatePancardResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.PanApprovalPending;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.NetworkConnectivity;
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
import static com.canvascoders.opaper.activity.EditGSTActivity.CROPPED_IMAGE_3;

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
    String gstImg = "", imagecamera = "",old_process_id="";
    boolean individual = true;
    private ImageView ivSearch, ivEdit;
    String panno = "";
    private EditText etGST, etStoreNAme, etStoreAddress;
    ImageView ivSGstImage;
    private static int IMAGE_SELCTION_CODE = 0;
    private static final int IMAGE_GST = 102;
    // CardView
    CardView cvGSTDetails;
    ScrollView scMain;
    String final_dc = "";
    TextView tvMessage;
    String store_address, store_address1, store_address_landmark, store_city, store_state, store_pincode, store_full_address;

    GPSTracker gps;
    NetworkConnectivity networkConnectivity;
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
        setContentView(R.layout.activity_edit_pan_card2);

        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        networkConnectivity = new NetworkConnectivity(this);
        init();
        scMain = findViewById(R.id.scMain);
        tvMessage = findViewById(R.id.tvTextMessage);
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
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation(0)) {
//                    Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                        ExtractPanDetail();
                    } else {
                        Constants.ShowNoInternet(EditPanCardActivity.this);
                    }
                }
            }
        });
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        ivPanImage = findViewById(R.id.ivPanImage);
        tvPanClick = findViewById(R.id.ivPanCard);
        ivPanImageSelected = findViewById(R.id.tvClickPanSelected);
        ivPanImageSelected.setOnClickListener(this);
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


        // gst details
        cvGSTDetails = findViewById(R.id.cvGstDetails);
        etGST = findViewById(R.id.etGstNumber);

        ivSGstImage = findViewById(R.id.ivGSTImage);
        ivSGstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_gst_image();
            }
        });

        ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(this);
        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);

        etStoreNAme = findViewById(R.id.etStoreName);

        etStoreAddress = findViewById(R.id.etAddress);

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
                } else {
                    Toast.makeText(EditPanCardActivity.this, "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UpdatePanDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditPanCardActivity.this, "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.ivPanCard:
                if (panImagepath.equalsIgnoreCase("")) {
                    capture_pan_card_image();
                } else {
                    showAlert();

                }
                break;

            case R.id.tvClickPanSelected:
                if (panImagepath.equalsIgnoreCase("")) {
                    capture_pan_card_image();
                } else {
                    showAlert();

                }
                break;

            case R.id.ivBack:
                finish();
                break;


            case R.id.ivSearch:
                if (isValid()) {
                    APiCallVerifyGST();
                }
                break;
            case R.id.ivEdit:
                showAlert1();
                break;


        }
    }


    private boolean isValid() {
        if (TextUtils.isEmpty(etGST.getText().toString())) {
            etGST.setError("Provide GST");
            etGST.requestFocus();
            return false;
        }
        /*
         */
        return true;


    }


    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPanCardActivity.this);
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("Are you sure you want to Edit PAN Card ?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                capture_pan_card_image();

                etStoreAddress.getText().clear();
                etStoreNAme.getText().clear();
                tvPanClick.setVisibility(View.VISIBLE);
                ivPanImageSelected.setVisibility(View.GONE);
                panImagepath = "";
                Glide.with(EditPanCardActivity.this).load(panImagepath).into(ivPanImage);
                // pan part will hold up ...

                cvGSTDetails.setVisibility(View.GONE);

                btSubmit.setText("UPDATE PAN ");
                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation(0)) {
                            if (networkConnectivity.isNetworkAvailable()) {
                                ExtractPanDetail();
                            } else {
                                Constants.ShowNoInternet(EditPanCardActivity.this);
                            }
                        }
                    }
                });
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    private void showAlert1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPanCardActivity.this);
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("Are you sure you want to Edit GST Number?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                etGST.setEnabled(true);
                etGST.getText().clear();
                etStoreAddress.getText().clear();
                etStoreNAme.getText().clear();
                ivSearch.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.GONE);
                gstImg = "";
                Glide.with(EditPanCardActivity.this).load(gstImg).into(ivSGstImage);
                // pan part will hold up ...

                //   rvBottomPan.setVisibility(View.GONE);

               /* btSubmit.setText("SUBMIT");
                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation(v, 0)) {
                            if (networkConnectivity.isNetworkAvailable()) {
                                APiCallSubmitGST();
                            } else {
                                Constants.ShowNoInternet(EditPanCardActivity.this);
                            }
                        }
                    }
                });*/
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
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
        mProgressDialog.setMessage("Please wait...");
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

        Call<UpdatePancardResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).editPanCard("Bearer " + sessionManager.getToken(), params, pan_card_part);

        callUpload.enqueue(new Callback<UpdatePancardResponse>() {
            @Override
            public void onResponse(Call<UpdatePancardResponse> call, Response<UpdatePancardResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {


                    UpdatePancardResponse updatePancardResponse = response.body();
                    if (updatePancardResponse.getResponseCode() == 200) {
                        //deleteImages();


                        Toast.makeText(EditPanCardActivity.this, updatePancardResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (updatePancardResponse.getResponseCode() == 203) {
                        DialogUtil.dismiss();
                        cvGSTDetails.setVisibility(View.VISIBLE);

                        tvMessage.setText(updatePancardResponse.getResponse());
                        scMain.smoothScrollTo(0, cvGSTDetails.getBottom());
                        //rvBottomPan.setVisibility(View.VISIBLE);
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            ApiCallGetDetails();
                        } else {
                            Constants.ShowNoInternet(EditPanCardActivity.this);
                        }
                        btSubmit.setText("SUBMIT");


                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validation(1)) {
                                    if (networkConnectivity.isNetworkAvailable()) {
                                        ApiCallSubmitDataTogather(name, fathername, panId, storename);
                                    } else {
                                        Constants.ShowNoInternet(EditPanCardActivity.this);
                                    }
                                }
                            }
                        });


                        Toast.makeText(EditPanCardActivity.this, updatePancardResponse.getResponse(), Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(EditPanCardActivity.this, validation.getStoreName(), Toast.LENGTH_LONG).show();
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
                } else {
                    Toast.makeText(EditPanCardActivity.this, "#errorcode 2039 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UpdatePancardResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditPanCardActivity.this, "#errorcode 2039 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //  Toast.makeText(EditPanCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean validation(int i) {

        if (!isPanSelected) {
            Toast.makeText(EditPanCardActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (panImagepath.equalsIgnoreCase("")) {
            Toast.makeText(EditPanCardActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (i == 1) {
            if (etGST.getText().toString().equalsIgnoreCase("")) {
                etGST.setError("Provide GST");
                etGST.requestFocus();
                return false;
            }
            if (etStoreAddress.getText().toString().equalsIgnoreCase("")) {
                etStoreAddress.setError("Provide Store Address Search GST Number ");
                etStoreAddress.requestFocus();
                return false;
            }
            Matcher gstMatcher = Constants.GST_PATTERN.matcher(etGST.getText().toString());
            if (!gstMatcher.matches()) {
                etGST.setError("Provide valid GST no.");
                //showMSG(false, "Provide Valid GST No.");
                etGST.requestFocus();
                return false;
            }
            if (gstImg.equalsIgnoreCase("")) {
                Toast.makeText(EditPanCardActivity.this, "Please select GST Image first", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PAN) {
                // deleteImages();
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

            if (requestCode == IMAGE_GST) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditPanCardActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditPanCardActivity.this);
                Intent intent = new Intent(EditPanCardActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_3);
            }


            if (requestCode == CROPPED_IMAGE_3) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                gstImg = RealPathUtil.getPath(EditPanCardActivity.this, imgURI);
                try {
                    Glide.with(EditPanCardActivity.this).load(gstImg).into(ivSGstImage);
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
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        if (!TextUtils.isEmpty(panImagepath)) {

            mProgressDialog.setMessage("Extracting image...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            File imagefile = new File(panImagepath);
            typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));


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


                                DialogUtil.PanDetail2(EditPanCardActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), individual, str_process_id,new DialogListner() {
                                    @Override
                                    public void onClickPositive() {

                                    }

                                    @Override
                                    public void onClickNegative() {

                                    }

                                    @Override
                                    public void onClickDetails(String name, String fathername, String storename, String id) {

                                        if (storename.equalsIgnoreCase("")) {
//                                            Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                UpadatePan(name, fathername, id, "");
                                               // DialogUtil.dismiss();
                                            } else {
                                                Constants.ShowNoInternet(EditPanCardActivity.this);
                                            }

                                        } else {

                                            //  Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                UpadatePan(name, fathername, id, storename);
                                             //   DialogUtil.dismiss();
                                            } else {
                                                Constants.ShowNoInternet(EditPanCardActivity.this);
                                            }
                                        }


                                    }

                                    @Override
                                    public void onClickChequeDetails(String accName, String payeename, String proccessId, String storeanem, String BranchName, String bankAdress) {
                                        old_process_id=proccessId;
                                        UpadatePan1(accName,payeename,str_process_id,storeanem);
                                    }

                                    @Override
                                    public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

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

                                    DialogUtil.PanDetail2(EditPanCardActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), individual,str_process_id, new DialogListner() {
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
                                                  //  DialogUtil.dismiss();
                                                } else {
                                                    Constants.ShowNoInternet(EditPanCardActivity.this);
                                                }

                                            } else {

                                                Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                    UpadatePan(name, fathername, id, storename);
                                                  //  DialogUtil.dismiss();
                                                } else {
                                                    Constants.ShowNoInternet(EditPanCardActivity.this);
                                                }
                                            }


                                        }

                                        @Override
                                        public void onClickChequeDetails(String accName, String payeename, String proccessId, String storeanem, String BranchName, String bankAdress) {
                                            old_process_id=proccessId;
                                            UpadatePan1(accName,payeename,str_process_id,storeanem);
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
                        Toast.makeText(EditPanCardActivity.this, "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetPanDetailsResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditPanCardActivity.this, "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditPanCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


  /*  private void deleteImages() {

        File casted_image = new File(cameraimage);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(panImagepath);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }
    }*/

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


    // capture GST Image
    private void capture_gst_image() {
        requestPermissionHandler.requestPermission(EditPanCardActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                IMAGE_SELCTION_CODE = IMAGE_GST;
                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditPanCardActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_GST);

            }

            @Override
            public void onFailed() {
                Toast.makeText(EditPanCardActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void APiCallVerifyGST() {
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());

        Call<GetGSTVerify> call = ApiClient.getClient().create(ApiInterface.class).getGSTVerify("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<GetGSTVerify>() {
            @Override
            public void onResponse(Call<GetGSTVerify> call, Response<GetGSTVerify> response) {
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    GetGSTVerify getGSTVerify = response.body();
                    if (getGSTVerify.getResponseCode() == 200) {
                        //   Toast.makeText(EditPanCardActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
                     /*   ivSearch.setBackgroundResource(0);
                        ivSearch.setImageResource(R.drawable.checked);*/
                        etStoreNAme.setText(getGSTVerify.getData().get(0).getStoreName());
                        StoreAddress storeAddress = getGSTVerify.getData().get(0).getStoreAddress();
                        etStoreAddress.setText(storeAddress.getBnm() + " " + storeAddress.getBno() + " " + storeAddress.getSt() + " " + storeAddress.getLoc() + " " + storeAddress.getCity() + " " + storeAddress.getDst() + " " + storeAddress.getStcd() + " " + storeAddress.getPncd());

                        store_address = storeAddress.getBnm() + " " + storeAddress.getBno();
                        store_address1 = storeAddress.getSt();
                        store_address_landmark = storeAddress.getLoc();
                        if (storeAddress.getCity().equalsIgnoreCase("")) {
                            store_city = storeAddress.getDst();
                        } else {
                            store_city = storeAddress.getCity();
                        }
                        store_state = storeAddress.getStcd();
                        store_pincode = storeAddress.getPncd();
                        store_full_address = etStoreAddress.getText().toString();
                        ivEdit.setVisibility(View.VISIBLE);
                        ivSearch.setVisibility(View.GONE);
                        etGST.setEnabled(false);
                        //addDC(storeAddress.getPncd());


                        DialogUtil.addressDetails(EditPanCardActivity.this, store_address, store_address1, store_address_landmark, store_pincode, store_city, store_state, new DialogListner() {
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

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String city, String state, String dc) {
                                // etCity.getText().toString(), etState.getText().toString(),""+dc.getSelectedItem());
                                store_address = accName;
                                store_address1 = payeename;
                                store_address_landmark = ifsc;
                                store_pincode = bankname;
                                store_city = city;
                                store_state = state;
                                //addDC(store_pincode);
                                store_full_address = store_address + " " + store_address1 + " " + store_address_landmark + " " + store_pincode + " " + store_city + " " + store_state;
                                etStoreAddress.setText(store_full_address);
                                final_dc = dc;

                            }
                        });


                    } else if (getGSTVerify.getResponseCode() == 400) {
                        if (getGSTVerify.getValidation() != null) {
                            Validation validation = getGSTVerify.getValidation();
                            if (validation.getGstn() != null && validation.getGstn().length() > 0) {
                                etGST.setError(validation.getGstn());
                                etGST.requestFocus();
                                // return false;
                            } else {
                                Toast.makeText(EditPanCardActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(EditPanCardActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    } else if (getGSTVerify.getResponseCode() == 202) {
                        sessionManager.logoutUser(EditPanCardActivity.this);
                    } else {
                        Toast.makeText(EditPanCardActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditPanCardActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetGSTVerify> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditPanCardActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void ApiCallSubmitDataTogather(String name, String fathername, String panId, String storename) {
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        ;
        MultipartBody.Part typedFile = null;
        MultipartBody.Part typedFilepan = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);


        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());
        params.put(Constants.PARAM_STORE_NAME, etStoreNAme.getText().toString());
        params.put(Constants.PARAM_STORE_ADDRESS, store_address);
        params.put(Constants.PARAM_STORE_ADDRESS1, store_address1);
        params.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, store_address_landmark);
        params.put(Constants.PARAM_CITY, store_city);
        params.put(Constants.PARAM_STATE, store_state);
        params.put(Constants.PARAM_PINCODE, store_pincode);
        params.put(Constants.PARAM_STORE_FULL_ADDRESS, store_full_address);
        params.put(Constants.PARAM_IF_GST, "yes");
        params.put(Constants.PARAM_DC, final_dc);
        File imagefile = new File(gstImg);
        typedFile = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstImg)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));

        //pan card department

        params.put(Constants.PARAM_PAN_NO, "" + panId);
        params.put(Constants.PARAM_PAN_NAME, "" + name);
        params.put(Constants.PARAM_FATHER_NAME, "" + fathername);

       /* params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);*/
        File imagefile1 = new File(panImagepath);
        typedFilepan = MultipartBody.Part.createFormData(Constants.PARAM_PAN_CARD_FRONT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile1));


        Call<GetGstPanEditResponse> call = ApiClient.getClient().create(ApiInterface.class).editGSTPAN("Bearer " + sessionManager.getToken(), params, typedFile, typedFilepan);
        call.enqueue(new Callback<GetGstPanEditResponse>() {
            @Override
            public void onResponse(Call<GetGstPanEditResponse> call, Response<GetGstPanEditResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetGstPanEditResponse getGstPanEditResponse = response.body();
                    if (getGstPanEditResponse.getResponseCode() == 200) {
                        Toast.makeText(EditPanCardActivity.this, getGstPanEditResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        mProgressDialog.dismiss();
                        try {
                            if (getGstPanEditResponse.getValidation() != null) {

                                Validation validation = getGstPanEditResponse.getValidation();
                                if (validation.getPanName() != null && validation.getPanName().length() > 0) {

                                   // DialogUtil.etPanName.setError(validation.getPanName());
                                    Toast.makeText(EditPanCardActivity.this, validation.getPanName(), Toast.LENGTH_LONG).show();

                                }
                                if (validation.getFatherName() != null && validation.getFatherName().length() > 0) {

                                   // DialogUtil.etPanFatherName.setError(validation.getFatherName());
                                    Toast.makeText(EditPanCardActivity.this, validation.getFatherName(), Toast.LENGTH_LONG).show();

                                }

                                if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getStoreName(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getPanNo() != null && validation.getPanNo().length() > 0) {
                                  //  DialogUtil.etPanNumber.setError(validation.getPanNo());
                                    Toast.makeText(EditPanCardActivity.this, validation.getPanNo(), Toast.LENGTH_LONG).show();

                                }
                                if (validation.getPanCardFront() != null && validation.getPanCardFront().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getPanCardFront(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getGstCertificateImage(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getGstCertificateImage() != null && validation.getGstn().length() > 0) {
                                    Toast.makeText(EditPanCardActivity.this, validation.getGstn(), Toast.LENGTH_LONG).show();
                                }

                                //    Toast.makeText(EditPanCardActivity.this, getGstPanEditResponse.getResponse(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(EditPanCardActivity.this, getGstPanEditResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditPanCardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // ErrorResponsePanCard errorResponsePanCard = response.body();
                    }
                }
            }


            @Override
            public void onFailure(Call<GetGstPanEditResponse> call, Throwable t) {

            }
        });
    }



    private void UpadatePan1(String name, String fathername, String panId, String storename) {

        mProgressDialog.show();

        MultipartBody.Part pan_card_part = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_PAN_NO, "" + panId);
        params.put(Constants.PARAM_PAN_NAME, "" + name);


        params.put(Constants.PARAM_pan_matched_kiran_proccess_id, old_process_id);
        params.put(Constants.PARAM_IS_PAN_EXIST, "1");


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
                        //deleteImages();


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
                                    Toast.makeText(EditPanCardActivity.this, validation.getStoreName(), Toast.LENGTH_LONG).show();
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
                } else {
                    Toast.makeText(EditPanCardActivity.this, "#errorcode 2039 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UpdatePancardResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditPanCardActivity.this, "#errorcode 2039 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //  Toast.makeText(EditPanCardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}
