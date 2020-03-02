package com.canvascoders.opaper.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.SendOTPDelBoyResponse.SendOtpDelBoyresponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDeliveryBoyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etFatherName, etPhoneNumber, etRoute, etDrivingNumber, etVehicle;
    private ImageView ivProfile, ivDriving_Licence, ivBack;
    private String profileImagepath = "", licenceImagePath = "";
    private int PROFILEIMAGE = 200, LICENCEIMAGE = 300;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addDelBoy;
    private RelativeLayout rvLanguage;
    private ProgressDialog mProgressDialog;
    Spinner dc;
    ScrollView svMain;
    String validationapiUrl;
    View view2, view3;
    int i = 0;
    Bitmap bitmapFromUrl, resizedBitmap;
    private String lattitude = "", longitude = "", currentAdress = "", permAddress = "";
    GPSTracker gps;
    String deliveryBoy = "";
    LinearLayout llOwnerInfo, llAddressInfo, llDrivingDetails;
    Button btAddNext;
    String str_process_id;
    private ArrayList<String> dcLists = new ArrayList<>();
    private SessionManager sessionManager;
    EditText etCurrentHouseNo, etOtp, etCurrentStreet, etCurrentLandmark, etCurrentPincode, etCurrentCity, etCurrentState, etPerHouseNo, etPermStreet, etPerLandmark, etPerPincode, etPerCity, etPerState;

    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ArrayList<String> listLanaguage = new ArrayList<>();
    boolean[] checkedItems;
    private TextView tvLanguage, dob;
    private CheckBox cbSame;
    private String isUpdate = "";
    String otp = "", mobile_number = "";
    Datum datum;
    Button btGetOtp;
    private String TAG = "akfdsbksbhdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy);


        datum = (Datum) getIntent().getSerializableExtra("data");
        str_process_id = String.valueOf(datum.getProccessId());
        deliveryBoy = String.valueOf(datum.getId());

        init();
    }

    private void init() {
        dc = (Spinner) findViewById(R.id.dc);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        sessionManager = new SessionManager(this);
        //str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

      /*  if(isUpdate.equalsIgnoreCase("1")){
            str_process_id = getIntent().getStringExtra("process");
        }*/
       /* addDelBoy = findViewById(R.id.btSubmitDelBoy);
        addDelBoy.setOnClickListener(this);
       */

        etName = findViewById(R.id.etName);
        etName.setText(datum.getName());
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        svMain = findViewById(R.id.svMain);
        etFatherName = findViewById(R.id.etFatherName);
        etFatherName.setText(datum.getFatherName());
        etPhoneNumber = findViewById(R.id.etPhone);
        etPhoneNumber.setText(datum.getPhoneNumber());
        // etPermanentAddress = findViewById(R.id.etPermAdd);
        etRoute = findViewById(R.id.etRouteNo);
        etRoute.setText(datum.getRouteNumber());
        etDrivingNumber = findViewById(R.id.etLicenceNumber);
        etDrivingNumber.setText(datum.getDrivingLicenceNum());
        etVehicle = findViewById(R.id.etVehicleForDelivery);
        etVehicle.setText(datum.getVehicleForDelivery());
        etOtp = findViewById(R.id.etOtp);
        dob = findViewById(R.id.tvDateofBirth);
        dob.setText(datum.getDrivingLicenceDob());
        dob.setOnClickListener(this);


        ivProfile = findViewById(R.id.ivProfileImage);


        if (datum.getImage() != null && !datum.getImage().equalsIgnoreCase("")) {
            ivProfile.setPadding(0, 0, 0, 0);


            // Bitmap bitmapFromUrl = getBitmapFromURL(Constants.BaseImageURL + datum.getImage());

            new getBitmapFromURL().execute(Constants.BaseImageURL + datum.getImage());
            // resizedBitmap = ImagePicker.getResizedBitmap(bitmapFromUrl, 400, 400);


            Glide.with(this).load(Constants.BaseImageURL + datum.getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(EditDeliveryBoyActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }


        ivDriving_Licence = findViewById(R.id.ivDrivingLicence);
        if (datum.getDrivingLicenceImage() != null && !datum.getDrivingLicenceImage().equalsIgnoreCase("")) {

            new getBitmapFromURL1().execute(Constants.BaseImageURL + datum.getDrivingLicenceImage());

            Glide.with(this).load(Constants.BaseImageURL + datum.getDrivingLicenceImage()).into(ivDriving_Licence);

        }

        ivDriving_Licence.setOnClickListener(this);
        rvLanguage = findViewById(R.id.rvSelectLanguage);
        rvLanguage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        checkedItems = new boolean[select_language.length];
        tvLanguage = findViewById(R.id.tvLanguage);
        tvLanguage.setText(datum.getLanguages());
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);
        btAddNext = findViewById(R.id.btNext);
        btAddNext.setOnClickListener(this);
        llOwnerInfo = findViewById(R.id.llOwnerInfo);
        llAddressInfo = findViewById(R.id.llAddressInfo);
        llDrivingDetails = findViewById(R.id.llDrivingInformation);

        etCurrentHouseNo = findViewById(R.id.etCurrentShopNo);
        etCurrentHouseNo.setText(datum.getCurrentAddress());
        etCurrentStreet = findViewById(R.id.etCurrentStreet);
        etCurrentStreet.setText(datum.getCurrentAddress1());

        etCurrentLandmark = findViewById(R.id.etCurrentLandmark);
        etCurrentLandmark.setText(datum.getCurrentAddressLandmark());
        etCurrentPincode = findViewById(R.id.etCurrentPincode);
        etCurrentPincode.setText(datum.getCurrentAddressPicode());
        addDC(etCurrentPincode.getText().toString());
        etCurrentCity = findViewById(R.id.etCurrentCity);
        etCurrentCity.setText(datum.getCurrentAddressCity());
        etCurrentState = findViewById(R.id.etCurrentState);
        etCurrentState.setText(datum.getCurrentAddressState());
        cbSame = findViewById(R.id.cbSameAsAbove);
        btGetOtp = findViewById(R.id.btGetOtp);

        btGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        APiCallCheckMobileNumber();
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                    }

                }

            }
        });

        etPerHouseNo = findViewById(R.id.etPermShopNo);
        etPerHouseNo.setText(datum.getPermanentResidentialAddress());
        etPermStreet = findViewById(R.id.etPerStreet);
        etPermStreet.setText(datum.getPermanentResidentialAddress1());
        etPerLandmark = findViewById(R.id.etPerLandmark);
        etPerLandmark.setText(datum.getPermanentResidentialAddressLandmark());
        etPerPincode = findViewById(R.id.etPerPincode);
        etPerPincode.setText(datum.getPermanentResidentialAddressPicode());
        etPerCity = findViewById(R.id.etPerCity);
        etPerCity.setText(datum.getPermanentResidentialAddressCity());
        etPerState = findViewById(R.id.etPerState);
        etPerState.setText(datum.getPermanentResidentialAddressState());


        etCurrentPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC(s.toString());
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                    }
                    //addDC(s.toString());
                }
            }
        });


        cbSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    etPerHouseNo.setText(etCurrentHouseNo.getText().toString().trim());
                    etPermStreet.setText(etCurrentStreet.getText().toString().trim());
                    etPerLandmark.setText(etCurrentLandmark.getText().toString().trim());
                    etPerPincode.setText(etCurrentPincode.getText().toString().trim());
                    etPerCity.setText(etCurrentCity.getText().toString().trim());
                    etPerState.setText(etCurrentState.getText().toString().trim());

                } else {

                }
            }
        });

        //ApiCallGetDc();
    }


    private boolean isValid() {
        if (etPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            etPhoneNumber.setError("Please provide mobile number");
            etPhoneNumber.requestFocus();
            return false;
        }

        if (etPhoneNumber.getText().toString().equalsIgnoreCase(datum.getPhoneNumber())) {
            Toast.makeText(EditDeliveryBoyActivity.this, "Please enter another mobile number", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etPhoneNumber.length() < 10) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError("Provide Valid number");
            //showMSG(false, "Provide Store address");
            return false;
        }
        return true;
    }


    private void APiCallCheckMobileNumber() {
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString());
        // params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


        Call<SendOtpDelBoyresponse> call = ApiClient.getClient().create(ApiInterface.class).deliveryBoysSendOTP("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<SendOtpDelBoyresponse>() {
            @Override
            public void onResponse(Call<SendOtpDelBoyresponse> call, Response<SendOtpDelBoyresponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    SendOtpDelBoyresponse sendOtpDelBoyresponse = response.body();
                    if (sendOtpDelBoyresponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoyActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_SHORT).show();
                        otp = sendOtpDelBoyresponse.getData().get(0).getOtp();
                        mobile_number = etPhoneNumber.getText().toString();
                    } else {
                        if (sendOtpDelBoyresponse.getResponseCode() == 400) {
                            if (!sendOtpDelBoyresponse.getValidation().getPhoneNumber().equalsIgnoreCase("") && sendOtpDelBoyresponse.getValidation().getPhoneNumber() != null) {
                                btGetOtp.setError(sendOtpDelBoyresponse.getValidation().getPhoneNumber());
                                Toast.makeText(EditDeliveryBoyActivity.this, sendOtpDelBoyresponse.getValidation().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                //btGetOtp.requestFocus();
                            } else {
                                Toast.makeText(EditDeliveryBoyActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(EditDeliveryBoyActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2076 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendOtpDelBoyresponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2076 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }
   /* public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/


    private class getBitmapFromURL extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... urls) {

            try {
                String s = urls[0];
                java.net.URL url = new java.net.URL(s);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                profileImagepath = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    private class getBitmapFromURL1 extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... urls) {

            try {
                String s = urls[0];
                java.net.URL url = new java.net.URL(s);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                licenceImagePath = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfileImage:
                captureImage(1);
                break;
            case R.id.ivDrivingLicence:
                captureImage(2);
                break;

            case R.id.rvSelectLanguage:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Select Languagaes");
                mBuilder.setMultiChoiceItems(select_language, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            listLanaguage.add(select_language[i]);
                        } else {
                            listLanaguage.remove(select_language[i]);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                break;

            case R.id.tvDateofBirth:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c.setTime(today);
                c.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate = c.getTime().getTime(); //
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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

                                dob.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();

                break;


            case R.id.ivBack:
                finish();
                break;
            case R.id.btNext:
                if (i == 0) {
                    if (validation(1)) {
                        i = 1;
                        validationapiUrl = "1";
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            ApiCallValidationCheck(validationapiUrl, 1);
                        } else {
                            Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                        }


                    }
                } else if (i == 1) {
                    if (validation(2)) {
                        i = 2;
                        validationapiUrl = "2";
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            ApiCallValidationCheck(validationapiUrl, 2);
                        } else {
                            Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                        }

                    }
                } else {
                    if (validation(3)) {
                        ApiCallSubmit();
                    }
                }
                break;

        }

    }

    private boolean validation(int i) {
        if (i == 1) {
            if (profileImagepath.equals("")) {
                Toast.makeText(this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etName.getText().toString())) {
                etName.requestFocus();
                etName.setError("Provide Name");

                // showMSG(false, "dProvide Email");
                return false;
            }


            if (TextUtils.isEmpty(etFatherName.getText().toString())) {
                etFatherName.requestFocus();
                //showMSG(false, "Provide Store name");
                etFatherName.setError("Provide Father name");
                return false;
            }
            if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
                etPhoneNumber.requestFocus();
                etPhoneNumber.setError("Provide Phone number");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(etRoute.getText().toString())) {
                etRoute.requestFocus();
                etRoute.setError("Provide Route");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (!etPhoneNumber.getText().toString().equalsIgnoreCase(datum.getPhoneNumber())) {
                if (TextUtils.isEmpty(etOtp.getText().toString())) {
                    etOtp.requestFocus();
                    etOtp.setError("Provide OTP");
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (!etOtp.getText().toString().equalsIgnoreCase(otp)) {
                    etOtp.requestFocus();
                    etOtp.setError("Provide Valid OTP");
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (!etPhoneNumber.getText().toString().equalsIgnoreCase(mobile_number)) {
                    etPhoneNumber.requestFocus();
                    etPhoneNumber.setError("Provide Same mobile number");
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
            }

            if (etPhoneNumber.length() < 10) {
                etPhoneNumber.requestFocus();
                etPhoneNumber.setError("Provide Valid number");
                //showMSG(false, "Provide Store address");
                return false;
            }
        } else if (i == 2) {
            if (TextUtils.isEmpty(etCurrentHouseNo.getText().toString())) {
                etCurrentHouseNo.requestFocus();
                etCurrentHouseNo.setError("Provide Number");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etCurrentStreet.getText().toString())) {
                etCurrentStreet.requestFocus();
                etCurrentStreet.setError("Provide Street");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentLandmark.getText().toString())) {
                etCurrentLandmark.requestFocus();
                etCurrentLandmark.setError("Provide Landmark");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etCurrentPincode.getText().toString())) {
                etCurrentPincode.requestFocus();
                etCurrentPincode.setError("Provide Pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (etCurrentPincode.getText().length() != 6) {
                etCurrentPincode.requestFocus();
                etCurrentPincode.setError("Provide Valid Pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentCity.getText().toString())) {
                etCurrentCity.requestFocus();
                etCurrentCity.setError("Provide City");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etCurrentState.getText().toString())) {
                etCurrentState.requestFocus();
                etCurrentState.setError("Provide State");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerHouseNo.getText().toString())) {
                etPerHouseNo.requestFocus();
                etPerHouseNo.setError("Provide Number");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etPermStreet.getText().toString())) {
                etPermStreet.requestFocus();
                etPermStreet.setError("Provide Street");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerLandmark.getText().toString())) {
                etPerLandmark.requestFocus();
                etPerLandmark.setError("Provide Landmark");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etPerPincode.getText().toString())) {
                etPerPincode.requestFocus();
                etPerPincode.setError("Provide Pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etPerCity.getText().toString())) {
                etPerCity.requestFocus();
                etPerCity.setError("Provide City");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etPerState.getText().toString())) {
                etPerState.requestFocus();
                etPerState.setError("Provide State");
                // showMSG(false, "Provide Pincode");
                return false;
            }

        }
        if (i == 3) {


            if (TextUtils.isEmpty(etDrivingNumber.getText().toString())) {
                etDrivingNumber.requestFocus();
                etDrivingNumber.setError("Provide Driving Licence Number");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etVehicle.getText().toString())) {
                etVehicle.requestFocus();
                etVehicle.setError("Provide Vehicle Name");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (tvLanguage.getText().equals("Select Language")) {
                Toast.makeText(this, "Please Select Language", Toast.LENGTH_SHORT).show();
                // showMSG(false, "Provide Pincode");
                return false;
            }


            if (licenceImagePath.equals("")) {
                Toast.makeText(this, "Please Select Licence Image", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (dob.getText().equals("Date of Birth")) {
                Toast.makeText(this, "Provide Date of Birth", Toast.LENGTH_SHORT).show();
                // showMSG(false, "Provide Pincode");
                return false;
            }
        }


        return true;
    }


    private void ApiCallValidationCheck(String validationapiUrl, int i1) {

        currentAdress = etCurrentHouseNo.getText().toString() + " " + etCurrentStreet.getText().toString() + " " + etCurrentLandmark.getText().toString() + " " + etCurrentCity.getText().toString() + " " + etCurrentState.getText().toString() + " " + etCurrentPincode.getText().toString();
        permAddress = etPerHouseNo.getText().toString() + " " + etPermStreet.getText().toString() + " " + etPerLandmark.getText().toString() + " " + etPerCity.getText().toString() + " " + etPerState.getText().toString() + " " + etPerPincode.getText().toString();

        mProgressDialog.show();
        MultipartBody.Part prof_image = null;

        Call<AddDelBoyResponse> call = null;
        String image = "image";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


        if (i1 == 1) {
            File imagefile1 = new File(profileImagepath);
            prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
            params.put(Constants.NAME, etName.getText().toString().trim());
            params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
            params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString().trim());
            params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString());
            params.put(Constants.PARAM_DELIVERY_BOY_ID, deliveryBoy);
            call = ApiClient.getClient().create(ApiInterface.class).DeliveryBoysDetailsValid1("Bearer " + sessionManager.getToken(), validationapiUrl, params, prof_image);


        } else if (i1 == 2) {
            params.put(Constants.PARAM_CURRENT_RESIDENTIAL, currentAdress);
            params.put(Constants.PARAM_PERMANENT_ADDRESS, permAddress);
            params.put(Constants.PARAM_CURRENT_ADDRESS1, etCurrentStreet.getText().toString().trim());
            params.put(Constants.PARAM_CURRENT_ADDRESS_LANDMARK, etCurrentLandmark.getText().toString());
            params.put(Constants.PARAM_CURRENT_ADDRESS_PINCODE, etCurrentPincode.getText().toString());
            params.put(Constants.PARAM_CURRENT_ADDRESS_CITY, etCurrentCity.getText().toString().trim());
            params.put(Constants.PARAM_CURRENT_ADDRESS_STATE, etCurrentState.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS, etPerHouseNo.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS1, etPermStreet.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_LANDMARK, etPerLandmark.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_PINCODE, etPerPincode.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_CITY, etPerCity.getText().toString());
            params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_STATE, etPerState.getText().toString());
            params.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
            call = ApiClient.getClient().create(ApiInterface.class).DeliveryBoysDetailsValid2("Bearer " + sessionManager.getToken(), validationapiUrl, params);

        }

        call.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {

                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {

                        if (i1 == 1) {
                            llOwnerInfo.setVisibility(View.GONE);
                            llAddressInfo.setVisibility(View.VISIBLE);
                            llDrivingDetails.setVisibility(View.GONE);
                            view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            svMain.scrollTo(0, svMain.getBottom());

                        } else if (i1 == 2) {
                            llAddressInfo.setVisibility(View.GONE);
                            llDrivingDetails.setVisibility(View.VISIBLE);
                            llOwnerInfo.setVisibility(View.GONE);
                            view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            svMain.scrollTo(0, svMain.getBottom());
                        }


                    } else {
                        if (addDelBoyResponse.getResponseCode() == 400) {
                            if (i1 == 1) {
                                i = 0;
                            } else if (i1 == 2) {
                                i = 1;
                            }

                            mProgressDialog.dismiss();
                            if (addDelBoyResponse.getValidation() != null) {
                                AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                                if (validation.getImage() != null && validation.getImage().length() > 0) {
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                if (validation.getName() != null && validation.getName().length() > 0) {
                                    etName.setError(validation.getName());
                                    etName.requestFocus();
                                }
                                if (validation.getFather_name() != null && validation.getFather_name().length() > 0) {
                                    etFatherName.setError(validation.getName());
                                    etFatherName.requestFocus();
                                }
                                if (validation.getPhoneNumber() != null && validation.getPhoneNumber().length() > 0) {

                                    etPhoneNumber.setError(validation.getPhoneNumber());
                                    etPhoneNumber.requestFocus();
                                }
                                if (validation.getCurrent_residential_address() != null && validation.getCurrent_residential_address().length() > 0) {
                                    etCurrentHouseNo.setError(validation.getCurrent_residential_address());
                                    etCurrentHouseNo.requestFocus();
                                }
                                if (validation.getPermanent_address() != null && validation.getPermanent_address().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerHouseNo.setError(validation.getPermanent_address());
                                    etPerHouseNo.requestFocus();

                                }

                                if (validation.getCurrentAddress() != null && validation.getCurrentAddress().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentHouseNo.setError(validation.getCurrentAddress());
                                    etCurrentHouseNo.requestFocus();

                                }

                                if (validation.getCurrentaddress1() != null && validation.getCurrentaddress1().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentStreet.setError(validation.getCurrentaddress1());
                                    etCurrentStreet.requestFocus();

                                }
                                if (validation.getCurrentaddressLandmark() != null && validation.getCurrentaddressLandmark().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentLandmark.setError(validation.getCurrentaddressLandmark());
                                    etCurrentLandmark.requestFocus();

                                }

                                if (validation.getCurrentAddressPincode() != null && validation.getCurrentAddressPincode().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentPincode.setError(validation.getCurrentAddressPincode());
                                    etCurrentPincode.requestFocus();

                                }
                                if (validation.getCurrentAddressCity() != null && validation.getCurrentAddressCity().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentCity.setError(validation.getCurrentAddressCity());
                                    etCurrentCity.requestFocus();

                                }

                                if (validation.getCurrentAddressState() != null && validation.getCurrentAddressState().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentState.setError(validation.getCurrentAddressState());
                                    etCurrentState.requestFocus();

                                }

                                if (validation.getPermanentResidentialAddress() != null && validation.getPermanentResidentialAddress().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerHouseNo.setError(validation.getPermanentResidentialAddress());
                                    etPerHouseNo.requestFocus();

                                }

                                if (validation.getPermanentResidentialAddress1() != null && validation.getPermanentResidentialAddress1().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPermStreet.setError(validation.getPermanentResidentialAddress1());
                                    etPermStreet.requestFocus();

                                }

                                if (validation.getPermanentResidentialAddressLandmark() != null && validation.getPermanentResidentialAddressLandmark().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerLandmark.setError(validation.getPermanentResidentialAddressLandmark());
                                    etPerLandmark.requestFocus();

                                }

                                if (validation.getPermanentResidentialAddressPincode() != null && validation.getPermanentResidentialAddressPincode().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerPincode.setError(validation.getPermanentResidentialAddressPincode());
                                    etPerPincode.requestFocus();

                                }

                                if (validation.getPermanentResidentialAddressCity() != null && validation.getPermanentResidentialAddressCity().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerCity.setError(validation.getPermanentResidentialAddressCity());
                                    etPerCity.requestFocus();

                                }
                                if (validation.getPermanentResidentialAddressState() != null && validation.getPermanentResidentialAddressState().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerState.setError(validation.getPermanentResidentialAddressState());
                                    etPerState.requestFocus();

                                }


                                if (validation.getDc() != null && validation.getDc().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();


                                }
                                if (validation.getRoute_number() != null && validation.getRoute_number().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etRoute.setError(validation.getRoute_number());
                                    etRoute.requestFocus();

                                }
                                if (validation.getDriving_licence_num() != null && validation.getDriving_licence_num().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etDrivingNumber.setError(validation.getDriving_licence_num());
                                    etDrivingNumber.requestFocus();
                                }
                                if (validation.getDriving_licence_dob() != null && validation.getDriving_licence_dob().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    dob.setError(validation.getDriving_licence_dob());
                                    dob.requestFocus();
                                }
                                if (validation.getDriving_licence_image() != null && validation.getDriving_licence_image().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etVehicle.setError(validation.getDriving_licence_dob());
                                    etVehicle.requestFocus();
                                }
                                if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                                }

                                if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                                } /*else {
                                    Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                                }
*/
                            } else {
                                Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                            }
                        }
                    }
                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2044 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {


                mProgressDialog.dismiss();

                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2044 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ApiCallSubmit() {
        gps = new GPSTracker(EditDeliveryBoyActivity.this);
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
        mProgressDialog.show();
        MultipartBody.Part prof_image = null;
        MultipartBody.Part license_image = null;
        String image = "image";
        String driving_licence = "driving_licence_image";

        File imagefile1 = new File(profileImagepath);
        prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
        //   list.add(shop_act_part);


        File imagefile2 = new File(licenceImagePath);
        license_image = MultipartBody.Part.createFormData(driving_licence, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePath)), imagefile2));

        // Log.e("image",list.toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.NAME, etName.getText().toString().trim());
        params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
        params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString().trim());
        params.put(Constants.PARAM_CURRENT_RESIDENTIAL, currentAdress);
        params.put(Constants.PARAM_CURRENT_ADDRESS, etCurrentHouseNo.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS1, etCurrentStreet.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_LANDMARK, etCurrentLandmark.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_CITY, etCurrentCity.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_STATE, etCurrentState.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_PINCODE, etCurrentPincode.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS, etPerHouseNo.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS1, etPermStreet.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_LANDMARK, etPerLandmark.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_CITY, etPerCity.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_STATE, etPerState.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_PINCODE, etPerPincode.getText().toString());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, deliveryBoy);


        params.put(Constants.PARAM_PERMANENT_ADDRESS, permAddress);
        params.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
        params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString().trim());
        /*if(isUpdate.equalsIgnoreCase("1")){
            params.put(Constants.PARAM_IS_EDIT,"1");
        }*/
        params.put(Constants.PARAM_DRIVING_LICENCE_NUM, etDrivingNumber.getText().toString().trim());
        params.put(Constants.PARAM_DRIVING_LICENCE_DOB, dob.getText().toString().trim());
        params.put(Constants.PARAM_DRIVING_LICENCE_VEHICLE, etVehicle.getText().toString().trim());
        params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);


        Call<AddDelBoyResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, license_image);
        callUpload.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                try {
                    mProgressDialog.dismiss();
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        deleteImages();
                        finish();
                    }
                    else if (addDelBoyResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditDeliveryBoyActivity.this);
                    }
                    else if (addDelBoyResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (addDelBoyResponse.getValidation() != null) {
                            AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                            if (validation.getImage() != null && validation.getImage().length() > 0) {
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
                                finish();
                            }
                            if (validation.getName() != null && validation.getName().length() > 0) {
                                etName.setError(validation.getName());
                                etName.requestFocus();
                            }
                            if (validation.getFather_name() != null && validation.getFather_name().length() > 0) {
                                etFatherName.setError(validation.getName());
                                etFatherName.requestFocus();
                            }
                            if (validation.getPhoneNumber() != null && validation.getPhoneNumber().length() > 0) {

                                etPhoneNumber.setError(validation.getPhoneNumber());
                                etPhoneNumber.requestFocus();
                            }
                            if (validation.getCurrent_residential_address() != null && validation.getCurrent_residential_address().length() > 0) {
                                etCurrentHouseNo.setError(validation.getCurrent_residential_address());
                                etCurrentHouseNo.requestFocus();
                            }
                            if (validation.getPermanent_address() != null && validation.getPermanent_address().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPerHouseNo.setError(validation.getPermanent_address());
                                etPerHouseNo.requestFocus();

                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getRoute_number() != null && validation.getRoute_number().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etRoute.setError(validation.getRoute_number());
                                etRoute.requestFocus();

                            }
                            if (validation.getDriving_licence_num() != null && validation.getDriving_licence_num().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etDrivingNumber.setError(validation.getDriving_licence_num());
                                etDrivingNumber.requestFocus();
                            }
                            if (validation.getDriving_licence_dob() != null && validation.getDriving_licence_dob().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                dob.setError(validation.getDriving_licence_dob());
                                dob.requestFocus();
                            }
                            if (validation.getDriving_licence_image() != null && validation.getDriving_licence_image().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etVehicle.setError(validation.getDriving_licence_dob());
                                etVehicle.requestFocus();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                        if (addDelBoyResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditDeliveryBoyActivity.this);
                        } else {

                            Toast.makeText(EditDeliveryBoyActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoyActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void captureImage(int i) {
        if (i == 1) {
            Intent chooseImageIntent = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent, PROFILEIMAGE);
        }
        if (i == 2) {
            Intent chooseImageIntent1 = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent1, LICENCEIMAGE);
        }
    }

    private void addDC(String pcode) {
        // state is DC and DC is state

        dcLists.clear();
        mProgressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }
                            etCurrentState.setText(getUserDetails.getData().get(i).getState());
                            etCurrentCity.setText(getUserDetails.getData().get(i).getCity());

                        }

                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoyActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditDeliveryBoyActivity.this);
                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    class CustomAdapter<T> extends ArrayAdapter<T> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<T> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(12);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.rb_regular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

    private void deleteImages() {

        File casted_image = new File(licenceImagePath);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(profileImagepath);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }


    private void ApiCallGetDetailLicence(String drivingLicencePath) {
        // MultipartBody.Part voter_front_part = null;
        MultipartBody.Part driving_licence_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

       /* File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));
*/
        File imagefile1 = new File(drivingLicencePath);
        driving_licence_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicencePath)), imagefile1));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("Fetching details. Please wait......");
        mProgressDialog.show();
        // hideKeyboardwithoutPopulateFragment();
        Call<DrivingLicenceDetailResponse> call = ApiClient.getClient2().create(ApiInterface.class).getDrivingLicenceDetail(params, driving_licence_part);
        call.enqueue(new Callback<DrivingLicenceDetailResponse>() {
            @Override
            public void onResponse(Call<DrivingLicenceDetailResponse> call, Response<DrivingLicenceDetailResponse> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        DrivingLicenceDetailResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(EditDeliveryBoyActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            etDrivingNumber.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber());

                            //fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            dob.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate());
                           /* dlnumber = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber();
                            dlIdDetailId = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            filename = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileUrl();*/


                        } else {
                            licenceImagePath = "";
                            ivDriving_Licence.setPadding(0, 0, 0, 0);
                            // ImageUtils.getInstant().getImageUri(EditPanCardActivity, photo);
                            Glide.with(EditDeliveryBoyActivity.this).load(licenceImagePath).into(ivDriving_Licence);
                            Toast.makeText(EditDeliveryBoyActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditPanCardActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DrivingLicenceDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //      Toast.makeText(EditPanCardActivity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LICENCEIMAGE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                licenceImagePath = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                ivDriving_Licence.setPadding(0, 0, 0, 0);
                // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(this).load(licenceImagePath).into(ivDriving_Licence);
                ApiCallGetDetailLicence(licenceImagePath);
            }
            if (requestCode == PROFILEIMAGE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                profileImagepath = ImagePicker.getBitmapPath(bitmap, this);
                ivProfile.setPadding(0, 0, 0, 0);// ImageUtils.getInstant().getImageUri(getActivity(), photo);

                Glide.with(this).load(profileImagepath).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(EditDeliveryBoyActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }
        }
    }


}
