package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetGSTVerify.GetGSTVerify;
import com.canvascoders.opaper.Beans.GetGSTVerify.StoreAddress;
import com.canvascoders.opaper.Beans.VerifyGstResponse.VerifyGst;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.CropImage2Activity;

import com.canvascoders.opaper.activity.EditGSTActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
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


public class EditGstDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_gstn;
    ImageView ivBack;
    RequestPermissionHandler requestPermissionHandler;
    SessionManager sessionManager;
    String str_process_id;
    GPSTracker gps;
    private static final int IMAGE_GST = 101;
    ProgressDialog mprogressDialog;
    NetworkConnectivity networkConnectivity;
    private static int IMAGE_SELCTION_CODE = 0;
    private Uri imgURI;
    private ImageView ivSearch, ivEdit, ivPanImageSelected;
    ImageView ivSGstImage;
    Button btSubmit;
    String final_dc = "";
    String store_address, store_address1, store_address_landmark, store_city, store_state, store_pincode, store_full_address;

    private EditText etGST, etStoreNAme, etStoreAddress;
    String gstImg = "", imagecamera = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gst_details);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermissionHandler = new RequestPermissionHandler();
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("Submitting GST details . Please wait ...");
        mprogressDialog.setCancelable(false);
        networkConnectivity = new NetworkConnectivity(this);
        init();
    }

    private void init() {
        etGST = findViewById(R.id.etGstNumber);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btSubmit:
                if (validation(v)) {
                    if (networkConnectivity.isNetworkAvailable()) {
                        APiCallSubmitGST();
                    }
                }
                break;

            case R.id.ivSearch:
                if (isValid()) {
                    APiCallVerifyGST();
                }
                break;
            case R.id.ivEdit:
                showAlert();

                break;
        }

    }

    private boolean validation(View v) {
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
            Toast.makeText(EditGstDetailsActivity.this, "Please select GST Image first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void APiCallSubmitGST() {
        mprogressDialog.show();
        ;
        MultipartBody.Part typedFile = null;
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
        typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstImg)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));
        Call<VerifyGst> call = ApiClient.getClient().create(ApiInterface.class).editGST("Bearer " + sessionManager.getToken(), params, typedFile);
        call.enqueue(new Callback<VerifyGst>() {
            @Override
            public void onResponse(Call<VerifyGst> call, Response<VerifyGst> response) {
                mprogressDialog.dismiss();
                if (response.isSuccessful()) {
                    VerifyGst verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {

                        finish();
                    } else {
                        if (verifyGst.getResponseCode() == 400) {
                            if (verifyGst.getValidation() != null) {
                                Validation validation = verifyGst.getValidation();
                                if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                    //showAlertValidation(validation.getVoterCardFront());
                                    Toast.makeText(EditGstDetailsActivity.this, validation.getGstCertificateImage(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //  showAlert2(getaadhardetail.getResponse());
                                Toast.makeText(EditGstDetailsActivity.this, verifyGst.getResponse(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        Toast.makeText(EditGstDetailsActivity.this, verifyGst.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyGst> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(EditGstDetailsActivity.this, "#errorcode ", Toast.LENGTH_SHORT).show();

            }
        });


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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditGstDetailsActivity.this);
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
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    // capture GST Image
    private void capture_gst_image() {
        requestPermissionHandler.requestPermission(EditGstDetailsActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                IMAGE_SELCTION_CODE = IMAGE_GST;
                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditGstDetailsActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_GST);

            }

            @Override
            public void onFailed() {
                Toast.makeText(EditGstDetailsActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_GST) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditGstDetailsActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditGstDetailsActivity.this);
                Intent intent = new Intent(EditGstDetailsActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_3);
            }


            if (requestCode == CROPPED_IMAGE_3) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                gstImg = RealPathUtil.getPath(EditGstDetailsActivity.this, imgURI);
                try {
                    Glide.with(EditGstDetailsActivity.this).load(gstImg).into(ivSGstImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private void APiCallVerifyGST() {
        mprogressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());

        Call<GetGSTVerify> call = ApiClient.getClient().create(ApiInterface.class).getGSTVerify("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<GetGSTVerify>() {
            @Override
            public void onResponse(Call<GetGSTVerify> call, Response<GetGSTVerify> response) {
                if (response.isSuccessful()) {
                    mprogressDialog.dismiss();
                    GetGSTVerify getGSTVerify = response.body();
                    if (getGSTVerify.getResponseCode() == 200) {
                        //   Toast.makeText(EditGstDetailsActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
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


                        DialogUtil.addressDetails(EditGstDetailsActivity.this, store_address, store_address1, store_address_landmark, store_pincode, store_city, store_state, new DialogListner() {
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
                                Toast.makeText(EditGstDetailsActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(EditGstDetailsActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    } else if (getGSTVerify.getResponseCode() == 202) {
                        sessionManager.logoutUser(EditGstDetailsActivity.this);
                    } else {
                        Toast.makeText(EditGstDetailsActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    mprogressDialog.dismiss();
                    Toast.makeText(EditGstDetailsActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetGSTVerify> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(EditGstDetailsActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }


}
