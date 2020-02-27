package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.R;

import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOwnerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBack;
    RequestPermissionHandler requestPermissionHandler;
    SessionManager sessionManager;
    String str_process_id;
    GPSTracker gps;
    ProgressDialog mProgressDialog;
    NetworkConnectivity networkConnectivity;


    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};

    private String same_address = "0", pincode;
    ImageView ivOwnerImage, ivOwnerImageSelected;
    String ownerImage = "";
    boolean[] checkedItems;
    TextView tvDOB;


    Button btSubmit;

    private EditText etOwnerName, etEmail, etDOB, etCurrentShopNo, etCurrentStreet, etCurrentLandmark, etCurrentPincode, etCurrentCity, etCurrentState, etPerShopNo, etPerStreet, etPerLandmark, etPerPincode, etPerCity, etPerState, edit_gstn;
    private AppCompatCheckBox cbSame;
    TextView tvLanguage;
    ArrayList<String> listLanaguage = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String lattitude = "", longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owner_info);

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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Submitting Owner details . Please wait ...");
        mProgressDialog.setCancelable(false);
        networkConnectivity = new NetworkConnectivity(this);

        init();

    }

    private void init() {
        ivOwnerImage = findViewById(R.id.ivOwnerImageSelected);
        ivOwnerImage = findViewById(R.id.ivOwnerImage);
        ivOwnerImage.setOnClickListener(this);
        ivOwnerImageSelected.setOnClickListener(this);
        etOwnerName = findViewById(R.id.etOwnerName);
        etEmail = findViewById(R.id.etEmailInfo);

        etCurrentShopNo = findViewById(R.id.etCurrentShopNo);
        etCurrentStreet = findViewById(R.id.etCurrentStreet);
        etCurrentLandmark = findViewById(R.id.etCurrentLandmark);
        etCurrentPincode = findViewById(R.id.etCurrentPincode);
        etCurrentCity = findViewById(R.id.etCurrentCity);
        etCurrentState = findViewById(R.id.etCurrentState);
        tvLanguage = findViewById(R.id.tvLanguage);
        cbSame = findViewById(R.id.cbSameAsAbove);
        etPerShopNo = findViewById(R.id.etPermShopNo);
        etPerStreet = findViewById(R.id.etPerStreet);
        etPerLandmark = findViewById(R.id.etPerLandmark);
        etPerPincode = findViewById(R.id.etPerPincode);
        etPerCity = findViewById(R.id.etPerCity);
        etPerState = findViewById(R.id.etPerState);


        tvDOB = findViewById(R.id.etDateOfBirth);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);

        cbSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    same_address = "1";
                    etPerShopNo.setText(etCurrentShopNo.getText().toString().trim());
                    etPerStreet.setText(etCurrentStreet.getText().toString().trim());
                    etPerLandmark.setText(etCurrentLandmark.getText().toString().trim());
                    etPerPincode.setText(etCurrentPincode.getText().toString().trim());
                    etPerCity.setText(etCurrentCity.getText().toString().trim());
                    etPerState.setText(etCurrentState.getText().toString().trim());

                } else {
                    same_address = "0";
                }
            }
        });
        checkedItems = new boolean[select_language.length];

        final Calendar c = Calendar.getInstance();
        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c.setTime(today);
                c.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate = c.getTime().getTime(); //

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditOwnerInfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                String monthString = String.valueOf(monthOfYear + 1);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }


                                //logic for add 0 in string if date digit is on 1 only
                                String daysString = String.valueOf(dayOfMonth);
                                if (daysString.length() == 1) {
                                    daysString = "0" + daysString;
                                }


                                tvDOB.setText(year + "-" + monthString + "-" + daysString);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOwnerImage:
                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditOwnerInfoActivity.this);
                startActivityForResult(chooseImageIntent, 105);
                break;
            case R.id.ivOwnerImageSelected:
                Intent chooseImageIntent1 = ImagePicker.getCameraIntent(EditOwnerInfoActivity.this);
                startActivityForResult(chooseImageIntent1, 105);
                break;

            case R.id.rvSelectLanguage:

                AlertDialog.Builder mBuilderLanguage = new AlertDialog.Builder(EditOwnerInfoActivity.this);
                mBuilderLanguage.setTitle("Select Languagaes");
                mBuilderLanguage.setMultiChoiceItems(select_language, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            listLanaguage.add(select_language[i]);
                        } else {
                            listLanaguage.remove(select_language[i]);
                        }
                    }
                });
                mBuilderLanguage.setCancelable(false);
                mBuilderLanguage.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < listLanaguage.size(); i++) {
                            item = item + listLanaguage.get(i);
                            if (i != listLanaguage.size() - 1) {
                                item = item + ",";
                            }
                        }
                        if (listLanaguage.size() == 0) {
                            tvLanguage.setText("Select Language");
                        } else {
                            tvLanguage.setText(item);
                        }
                    }
                });
                AlertDialog mDialogLang = mBuilderLanguage.create();
                mDialogLang.show();

                break;


            case R.id.btSubmit:
                if (validation()) {
                    if (networkConnectivity.isNetworkAvailable()) {
                        bizDetailsSubmit(v);
                    } else {
                        Constants.ShowNoInternet(this);
                    }

                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 105) {

                Bitmap bitmap = ImagePicker.getImageFromResult(EditOwnerInfoActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                ownerImage = ImagePicker.getBitmapPath(bitmap, EditOwnerInfoActivity.this); // ImageUtils.getInstant().getImageUri(EditStoreInformationActivity.this, photo);
                Glide.with(EditOwnerInfoActivity.this).load(ownerImage).into(ivOwnerImage);
                //  Log.e("imageowner", "back image" + shopImg);
                ivOwnerImageSelected.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean validation() {

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.requestFocus();
            etEmail.setError("Provide Email");
            // showMSG(false, "dProvide Email");
            return false;
        }


        if (!Constants.isEmailValid(etEmail.getText().toString())) {
            //_editTextMobile.setError("Provide Valid email");
            // showMSG(false, "Provide Valid Email");
            etEmail.setError("Provide valid email");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etOwnerName.getText().toString())) {
            etOwnerName.requestFocus();
            etOwnerName.setError("Provide Owner Name");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (tvDOB.getText().equals("Date of Birth")) {
            Toast.makeText(EditOwnerInfoActivity.this, "Provide Date of Birth", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (TextUtils.isEmpty(etCurrentShopNo.getText().toString())) {
            etCurrentShopNo.requestFocus();
            etCurrentShopNo.setError("Provide Residential Address");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentStreet.getText().toString())) {
            etCurrentStreet.requestFocus();
            etCurrentStreet.setError("Provide Residential Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etCurrentLandmark.getText().toString())) {
            etCurrentLandmark.requestFocus();
            etCurrentLandmark.setError("Provide Residential Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentPincode.getText().toString())) {
            etCurrentPincode.requestFocus();
            etCurrentPincode.setError("Provide Residential pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentCity.getText().toString())) {
            etCurrentCity.requestFocus();
            etCurrentCity.setError("Provide Residential City");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentState.getText().toString())) {
            etCurrentState.requestFocus();
            etCurrentState.setError("Provide Residential State");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPerShopNo.getText().toString())) {
            etPerShopNo.requestFocus();
            etPerShopNo.setError("Provide Permanent Address");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPerStreet.getText().toString())) {
            etPerStreet.requestFocus();
            etPerStreet.setError("Provide Permanent Address Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPerLandmark.getText().toString())) {
            etPerLandmark.requestFocus();
            etPerLandmark.setError("Provide Permanent Address Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (TextUtils.isEmpty(etPerPincode.getText().toString())) {
            etPerPincode.requestFocus();
            etPerPincode.setError("Provide Permanent Address Pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPerCity.getText().toString())) {
            etPerCity.requestFocus();
            etPerCity.setError("Provide Permanent Address City");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPerState.getText().toString())) {
            etPerState.requestFocus();
            etPerState.setError("Provide Permanent Address State");
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (listLanaguage.size() == 0) {
            Toast.makeText(EditOwnerInfoActivity.this, "Please Select Language", Toast.LENGTH_LONG).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (ownerImage.equalsIgnoreCase("")) {
            Toast.makeText(EditOwnerInfoActivity.this, "Please select owner image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }


        return true;
    }


    public void bizDetailsSubmit(final View v) {

        Call<GetUserDetailResponse> call;
        MultipartBody.Part typedFile = null;
        gps = new GPSTracker(EditOwnerInfoActivity.this);
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
        mProgressDialog.setMessage("Submitting kirana details . Please wait ...");
        mProgressDialog.show();
        HashMap<String, String> user = new HashMap<>();

        user.put(Constants.PARAM_PROCESS_ID, str_process_id);
        user.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.put(Constants.PARAM_EMAIL, "" + etEmail.getText());

        user.put(Constants.PARAM_LATITUDE, "" + lattitude);
        user.put(Constants.PARAM_LONGITUDE, "" + longitude);


        //new Update
        user.put(Constants.PARAM_OWNER_NAME, "" + etOwnerName.getText());
        user.put(Constants.PARAM_DOB, "" + tvDOB.getText());
        // user.put(Constants.PARAM_ROUTE, "R-" + etRoute.getText());
        user.put(Constants.PARAM_RESIDENTIAL_ADDRESS, "" + etCurrentShopNo.getText());
        user.put(Constants.PARAM_RESIDENTIAL_ADDRESS1, "" + etCurrentStreet.getText());
        user.put(Constants.PARAM_RESIDENTIAL_LANDMARK, "" + etCurrentLandmark.getText());
        user.put(Constants.PARAM_RESIDENTIAL_CITY, "" + etCurrentCity.getText());
        user.put(Constants.PARAM_RESIDENTIAL_PINCODE, "" + etCurrentPincode.getText());
        user.put(Constants.PARAM_RESIDENTIAL_STATE, "" + etCurrentState.getText());


        user.put(Constants.PARAM_PERMANENT_ADDRESS, "" + etPerShopNo.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS1, "" + etPerStreet.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_LANDMARK, "" + etPerLandmark.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_PINCODE, "" + etPerPincode.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_CITY, "" + etPerCity.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_STATE, "" + etPerState.getText());


        user.put(Constants.PARAM_LANGUAGES, "" + tvLanguage.getText());


        Log.e("User Date", "Edit info" + user);
        Log.e("User Date", "Edit info" + str_process_id + "   " + sessionManager.getAgentID());


        File imagefile = new File(ownerImage);
        typedFile = MultipartBody.Part.createFormData(Constants.PARAM_OWNER_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(ownerImage)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));
        call = ApiClient.getClient().create(ApiInterface.class).submitDetailsOnwer("Bearer " + sessionManager.getToken(), user, typedFile);

        call.enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    String TAG = "data";
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());
                        Toast.makeText(EditOwnerInfoActivity.this, getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                        // showAlert(getUserDetailResponse.getResponse());
                        // commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    }
                    if (getUserDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditOwnerInfoActivity.this);
                    }

                    if (getUserDetailResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (getUserDetailResponse.getValidation() != null) {
                            Validation validation = getUserDetailResponse.getValidation();
                            if (validation.getOwnerName() != null && validation.getOwnerName().length() > 0) {
                                etOwnerName.setError(validation.getOwnerName());
                                etOwnerName.requestFocus();
                                // return false;
                            }
                            if (validation.getDob() != null && validation.getDob().length() > 0) {
                              /*  tvDob.setError(validation.getOwnerName());
                                tvDob.requestFocus();*/
                                // return false;
                            }
                            if (validation.getVendorTypeDetail() != null && validation.getVendorTypeDetail().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getVendorTypeDetail(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }


                            if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getGstCertificateImage(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                            /*    edit_storeaddress.setError(validation.getStoreAddress());
                                edit_storeaddress.requestFocus();*/

                            }
                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                              /*  //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStreet.setError(validation.getStoreAddress1());
                                etStreet.requestFocus();

*/
                            }
                            if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etLandmark.setError(validation.getStoreAddressLandmark());
                                etLandmark.requestFocus();*/
                            }
                            if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* edit_pincode.setError(validation.getPincode());
                                edit_pincode.requestFocus();*//**/

                            }
                            if (validation.getCity() != null && validation.getCity().length() > 0) {

                               /* edit_city.setError(validation.getCity());
                                edit_city.requestFocus();*/
                            }
                            if (validation.getState() != null && validation.getState().length() > 0) {
                               /* edit_state.setError(validation.getState());
                                edit_state.requestFocus();*/
                            }
                            if (validation.getResidentialAddress() != null && validation.getResidentialAddress().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialAddress.setError(validation.getResidentialAddress());
                                etResidentialAddress.requestFocus();*/
                            }
                            if (validation.getResidentialAddress1() != null && validation.getResidentialAddress1().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etResidentialStreet.setError(validation.getResidentialAddress1());
                                etResidentialStreet.requestFocus();*/
                            }
                            if (validation.getResidentialAddressLandmark() != null && validation.getResidentialAddressLandmark().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialLandmark.setError(validation.getResidentialAddressLandmark());
                                etResidentialLandmark.requestFocus();*/
                            }
                            if (validation.getResidentialAddresspicode() != null && validation.getResidentialAddresspicode().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialPincode.setError(validation.getResidentialAddresspicode());
                                etResidentialPincode.requestFocus();*/
                            }
                            if (validation.getResidentialAddressCity() != null && validation.getResidentialAddressCity().length() > 0) {
                               /* //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialCity.setError(validation.getResidentialAddressCity());
                                etResidentialCity.requestFocus();*/
                            }
                            if (validation.getResidentialAddressState() != null && validation.getResidentialAddressState().length() > 0) {
                            /*    //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialState.setError(validation.getResidentialAddressState());
                                etResidentialState.requestFocus();*/
                            }

                            if (validation.getPermanentAddress() != null && validation.getPermanentAddress().length() > 0) {
                             /*   //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermententAdd.setError(validation.getPermanentAddress());
                                etPermententAdd.requestFocus();*/
                            }

                            if (validation.getPermanentAddress1() != null && validation.getPermanentAddress1().length() > 0) {
                              /*  //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentStreet.setError(validation.getPermanentAddress1());
                                etPermanentStreet.requestFocus();*/
                            }
                            if (validation.getPermanentAddressLandmark() != null && validation.getPermanentAddressLandmark().length() > 0) {
                             /*   //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentLandmark.setError(validation.getPermanentAddressLandmark());
                                etPermanentLandmark.requestFocus();*/
                            }
                            if (validation.getPermanentAddressPicode() != null && validation.getPermanentAddressPicode().length() > 0) {
                             /*   //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentPincode.setError(validation.getPermanentAddressPicode());
                                etPermanentPincode.requestFocus();*/
                            }
                            if (validation.getPermanentAddressCity() != null && validation.getPermanentAddressCity().length() > 0) {
                               /* //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentCity.setError(validation.getPermanentAddressCity());
                                etPermanentCity.requestFocus();*/
                            }
                            if (validation.getPermanentAddressState() != null && validation.getPermanentAddressState().length() > 0) {
                              /*  //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentState.setError(validation.getPermanentAddressState());
                                etPermanentState.requestFocus();*/
                            }
                            if (validation.getVendorType() != null && validation.getVendorType().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getVendorType(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLocality() != null && validation.getLocality().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getLocality(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getApproach() != null && validation.getApproach().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getApproach(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getShipmentTransfer() != null && validation.getShipmentTransfer().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getShipmentTransfer(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getPartnerWithOtherEcommerce() != null && validation.getPartnerWithOtherEcommerce().length() > 0) {
                                //Toast.makeText(EditOwnerInfoActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getPartnerWithOtherEcommerce(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                               /* edit_storename.setError(validation.getStoreName());
                                edit_storename.requestFocus();*/
                                // return false;
                            }
                            if (validation.getIfGst() != null && validation.getIfGst().length() > 0) {
                                edit_gstn.setError(validation.getIfGst());
                                edit_gstn.requestFocus();
                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getEmail() != null && validation.getEmail().length() > 0) {
                              /*  edit_email.setError(validation.getEmail());
                                edit_email.requestFocus();
                                // return false;*/
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getStoreTypeConfig() != null && validation.getStoreTypeConfig().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getStoreTypeConfig(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                Toast.makeText(EditOwnerInfoActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();

                                // return false;
                            } else {
                                Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                            }

                        } else {
                            Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                        }

                    } else {
                        Constants.showAlert(v, getUserDetailResponse.getResponse(), true);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditOwnerInfoActivity.this);
                        }
                    }
                } else {
                    Constants.showAlert(v, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditOwnerInfoActivity.this, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(EditOwnerInfoActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }


}