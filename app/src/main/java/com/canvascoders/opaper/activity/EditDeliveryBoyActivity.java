package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.AdharocrResponse.AdharOCRResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.GetVehicleTypes;
import com.canvascoders.opaper.Beans.SendOTPDelBoyResponse.SendOtpDelBoyresponse;
import com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse.ApiSubmitOCRPanVoterDlResponse;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DataAttributes;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditDeliveryBoyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etFatherName, etPhoneNumber, etRoute, etDrivingNumber;
    private ImageView ivProfile, ivDriving_Licence, ivDlImageBack, ivBack;
    private String profileImagepath = "", licenceImagePath = "", licenceImagePathBack = "";
    private int PROFILEIMAGE = 200, LICENCEIMAGE = 300, LICENCEIMAGEBACK = 400;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addDelBoy;
    private RelativeLayout rvLanguage;
    private static int IMAGE_SELCTION_CODE = 0;
    LinearLayout llDrivingLicenceDetails, llAadharDetails, llVoterDetails;
    private ProgressDialog mProgressDialog;
    private final static int REQUEST_SCANNER = 1000;
    Spinner dc;
    ScrollView svMain;
    private ImageView ivAdharFrontSelected, ivAdharABackSelected, ivVoterFrontSelected, ivDlFrontSelected, ivVoterBackSelected, ivAdharIamgeFront, ivAdharImageBack, ivVoterImageFront, ivVoterImageBack, ivDlImageFront;

    private String aadharImagepathBack = "";
    private RelativeLayout rvLanguageAdhar, rvLanguage1;
    String validationapiUrl;
    private Uri imgURI;
    private static final int IMAGE_AADHAR_FRONT = 1021;
    private static final int IMAGE_AADHAR_BACK = 1022, IMAGE_VOTER_FRONT = 1023, IMAGE_VOTER_BACK = 1024, IMAGE_DL_FRONT = 1025, IMAGE_DL_BACK = 1026;
    private String imagecamera = "", aadharImagepathFront = "", voterImagePathFront = "", voterImagePathBack = "", dlImagePathBack = "", dlImageOathFront = "";
    private static int CROPPED_IMAGE_VOTER_FRONT = 7000, CROPPED_IMAGE_VOTER_BACK = 8000, CROPPED_IMAGE_DL_BACK = 8001, CROPPED_IMAGE_DL_FRONT = 8002, CROPPED_IMAGE_ADHAR_FRONT = 8003, CROPPED_IMAGE_ADHAR_BACK = 8004;
    int optnal = 0;
    View view2, view3;
    int i = 0;
    Bitmap bitmapFromUrl, resizedBitmap;
    private String lattitude = "", longitude = "", currentAdress = "", permAddress = "";
    GPSTracker gps;
    String deliveryBoy = "";
    String[] selectBloodGroupType = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    String[] selectVehicleType = {"Bike", "Cycle", "Truck"};
    LinearLayout llOwnerInfo, llAddressInfo, llDrivingDetails;
    Button btAddNext;
    String str_process_id;
    private ArrayList<String> dcLists = new ArrayList<>();
    private SessionManager sessionManager;
    EditText etCurrentHouseNo, etOtp, etCurrentStreet, etCurrentLandmark, etCurrentPincode, etCurrentCity, etCurrentState, etPerHouseNo, etPermStreet, etPerLandmark, etPerPincode, etPerCity, etPerState;

    private String kyc_type = "3";
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ArrayList<String> listLanaguage = new ArrayList<>();
    boolean[] checkedItems;
    private TextView tvLanguage, tvLanguageAdhar, tvLanguage1, dob;
    private CheckBox cbSame;
    String stringdob = "";
    private String isUpdate = "";
    String otp = "", mobile_number = "";
    Datum datum;
    Button btGetOtp;
    String kyc_old = "3";
    private TextView tvLicenceIssueDate, tvLicenceValidDate;

    private String TAG = "akfdsbksbhdf";
    Spinner spVehicleForDelivery, spBloodGroupType;
    private TextView tvAdharFront, tvAdharBack, tvVoterFront, tvVoterBack, tvDlFront, tvDlBack, tvScan;
    private EditText etAdharName, etDObAdhar, etAdharNumber, etVoterName, etVoterFatherName, etVoterDOB, etVoterIdNumber;
    TextView tvAdharDOB, tvVoterDOB;
    String VoteridDetailId, filename, fileUrl, backsideFileUrl, backsidefilename, dlIdDetailId;
    private boolean isAadhaarFrontSelected = false;
    private boolean isAadhaarBackSelected = false;
    private RequestPermissionHandler requestPermissionHandler;

    RadioGroup rg;
    RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    boolean optional = false;
    String ocrid="";
    EditText etDexter;
    RelativeLayout rvSelctVoterDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy);

        requestPermissionHandler = new RequestPermissionHandler();
        datum = (Datum) getIntent().getSerializableExtra("data");
        str_process_id = String.valueOf(datum.getProccessId());
        deliveryBoy = String.valueOf(datum.getId());
        if (!datum.getKyc_type().equalsIgnoreCase("")) {
            kyc_old = datum.getKyc_type();
        }


        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        if (!datum.getGender().equalsIgnoreCase("")) {
            if (datum.getGender().equalsIgnoreCase("Female")) {
                radioSexGroup.check(R.id.radioFemale);
            } else {
                radioSexGroup.check(R.id.radioMale);
            }
        }
        init();
    }

    private void init() {

        rg = findViewById(R.id.rgMain);
        llDrivingLicenceDetails = findViewById(R.id.llDrivingDetails);
        llAadharDetails = findViewById(R.id.llAadharDetails);
        llVoterDetails = findViewById(R.id.llVoterInfo);

        rvSelctVoterDOB = findViewById(R.id.rvSelctVoterDOB);
        rvSelctVoterDOB.setOnClickListener(this);

        dc = (Spinner) findViewById(R.id.dc);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        sessionManager = new SessionManager(this);


        etName = findViewById(R.id.etName);
        etName.setText(datum.getName());
        view2 = findViewById(R.id.view2);
        spBloodGroupType = findViewById(R.id.spBloodGroup);
        view3 = findViewById(R.id.view3);
        svMain = findViewById(R.id.svMain);
        etFatherName = findViewById(R.id.etFatherName);
        etFatherName.setText(datum.getFatherName());
        etPhoneNumber = findViewById(R.id.etPhone);
        etDexter = findViewById(R.id.etDexter);
        etPhoneNumber.setText(datum.getPhoneNumber());
        // etPermanentAddress = findViewById(R.id.etPermAdd);
        etRoute = findViewById(R.id.etRouteNo);
        etRoute.setText(datum.getRouteNumber());
        etDrivingNumber = findViewById(R.id.etLicenceNumber);
        etDrivingNumber.setText(datum.getDrivingLicenceNum());
        etOtp = findViewById(R.id.etOtp);
        dob = findViewById(R.id.tvDateofBirth);
        stringdob = datum.getDrivingLicenceDob();

        tvLicenceIssueDate = findViewById(R.id.tvIssueDate);
        tvLicenceIssueDate.setOnClickListener(this);
        tvLicenceValidDate = findViewById(R.id.tvValidTilldate);
        tvLicenceValidDate.setOnClickListener(this);

        tvLicenceIssueDate.setText(datum.getDriving_licence_issue_date());
        tvLicenceValidDate.setText(datum.getDriving_licence_till_date());

        tvAdharDOB = findViewById(R.id.tvDOBAdhar);
        tvAdharDOB.setOnClickListener(this);
        tvVoterDOB = findViewById(R.id.tvDObVoter);
        tvVoterDOB.setOnClickListener(this);


        etAdharName = findViewById(R.id.etAdharName);
        //etDObAdhar = findViewById(R.id.etDOBAdhar);
        etAdharNumber = findViewById(R.id.etAdharNumber);
        etVoterName = findViewById(R.id.etVotername);
        //etVoterFatherName = findViewById(R.id.etVoterFathername);
        //etVoterDOB = findViewById(R.id.etDateofBirthvoter);
        etVoterIdNumber = findViewById(R.id.etVoterNumber);


        String outputDateStr = "";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (!datum.getDrivingLicenceDob().equalsIgnoreCase("")) {
            Date date = null;
            try {
                date = inputFormat.parse(datum.getDrivingLicenceDob());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputDateStr = outputFormat.format(date);
        }
        dob.setText(outputDateStr);
        dob.setOnClickListener(this);
        spVehicleForDelivery = findViewById(R.id.spVehicleForDelivery);

        List<String> stringList = new ArrayList<String>(Arrays.asList(selectBloodGroupType));

        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoyActivity.this, android.R.layout.simple_spinner_item, stringList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGroupType.setAdapter(spinnerArrayAdapter);
        spBloodGroupType.setSelection(0);

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
        ivDlImageBack = findViewById(R.id.ivDrivingLicenceBack);
        if (datum.getDrivingLicenceImageback() != null && !datum.getDrivingLicenceImageback().equalsIgnoreCase("")) {

            new getBitmapFromURL2().execute(Constants.BaseImageURL + datum.getDrivingLicenceImageback());

            Glide.with(this).load(Constants.BaseImageURL + datum.getDrivingLicenceImageback()).into(ivDlImageBack);

        }
        ivDlImageBack.setOnClickListener(this);
        ivDriving_Licence.setOnClickListener(this);
        rvLanguage = findViewById(R.id.rvSelectLanguage);
        rvLanguage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        checkedItems = new boolean[select_language.length];
        tvLanguage = findViewById(R.id.tvLanguage);

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

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            APiCallgetvehicleNames();
        } else {
            Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
        }


        tvAdharFront = findViewById(R.id.tvAdharFront);
        tvAdharBack = findViewById(R.id.tvAdharBack);
        ivAdharFrontSelected = findViewById(R.id.ivCheckAdharSelected);
        ivAdharFrontSelected.setOnClickListener(this);
        ivAdharABackSelected = findViewById(R.id.ivCheckAdharBackSelected);
        ivAdharABackSelected.setOnClickListener(this);
        ivAdharImageBack = findViewById(R.id.ivImageAdharBack);
        ivAdharIamgeFront = findViewById(R.id.ivImageAdharFront);
        tvScan = findViewById(R.id.tvScan);
        tvScan.setOnClickListener(this);

        tvAdharBack.setOnClickListener(this);
        tvAdharFront.setOnClickListener(this);

        tvVoterFront = findViewById(R.id.tvVoterFront);
        tvVoterBack = findViewById(R.id.tvVoterBack);
        ivVoterFrontSelected = findViewById(R.id.ivcheckedVoterFront);
        ivVoterFrontSelected.setOnClickListener(this);
        ivVoterBackSelected = findViewById(R.id.ivcheckedVoterBack);
        ivVoterBackSelected.setOnClickListener(this);
        ivVoterImageBack = findViewById(R.id.ivVoterBack);
        ivVoterImageFront = findViewById(R.id.ivVoterFront);
        tvVoterBack.setOnClickListener(this);
        tvVoterFront.setOnClickListener(this);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getId() == R.id.rbDriving) {
                    kyc_type = "3";
                    llDrivingLicenceDetails.setVisibility(View.VISIBLE);
                    llAadharDetails.setVisibility(View.GONE);
                    llVoterDetails.setVisibility(View.GONE);


                    voterImagePathBack = "";
                    ivVoterBackSelected.setVisibility(View.GONE);
                    tvVoterBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathBack).placeholder(R.drawable.voterback).into(ivVoterImageBack);

                    voterImagePathFront = "";
                    ivVoterFrontSelected.setVisibility(View.GONE);
                    tvVoterFront.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathFront).placeholder(R.drawable.voterfront).into(ivVoterImageFront);

                    etVoterName.setText("");
                    tvVoterDOB.setText("Date of Birth");
                    etVoterIdNumber.setText("");
                    //tvLanguage1.setText("Select Language");


                    aadharImagepathFront = "";
                    ivAdharFrontSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);

                    aadharImagepathBack = "";
                    ivAdharABackSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
                    etAdharName.setText("");
                    etAdharNumber.setText("");
                    //tvLanguageAdhar.setText("Select Language");
                    tvAdharDOB.setText("Date of Birth");

                }
                if (rb.getId() == R.id.rbAdharCard) {
                    llDrivingLicenceDetails.setVisibility(View.GONE);
                    llAadharDetails.setVisibility(View.VISIBLE);
                    llVoterDetails.setVisibility(View.GONE);
                    kyc_type = "1";

                    etDrivingNumber.setText("");
                    tvLicenceIssueDate.setText("Licence issue date");
                    tvLicenceValidDate.setText("Licence valid till date");
                    // tvLanguage.setText("Select Language");
                    dob.setText("Date of Birth");

                    licenceImagePath = "";
                    Glide.with(EditDeliveryBoyActivity.this).load(licenceImagePath).placeholder(R.drawable.blfront).into(ivDriving_Licence);

                    licenceImagePathBack = "";
                    Glide.with(EditDeliveryBoyActivity.this).load(licenceImagePathBack).placeholder(R.drawable.dlback).into(ivDlImageBack);

                    voterImagePathBack = "";
                    ivVoterBackSelected.setVisibility(View.GONE);
                    tvVoterBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathBack).placeholder(R.drawable.voterback).into(ivVoterImageBack);


                    voterImagePathFront = "";
                    ivVoterFrontSelected.setVisibility(View.GONE);
                    tvVoterFront.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathFront).placeholder(R.drawable.voterfront).into(ivVoterImageFront);

                    etVoterName.setText("");
                    tvVoterDOB.setText("Date of Birth");
                    etVoterIdNumber.setText("");
                    //tvLanguage1.setText("Select Language");

                }
                if (rb.getId() == R.id.rbVoter) {
                    llDrivingLicenceDetails.setVisibility(View.GONE);
                    llAadharDetails.setVisibility(View.GONE);
                    llVoterDetails.setVisibility(View.VISIBLE);
                    kyc_type = "2";

                    etDrivingNumber.setText("");
                    tvLicenceIssueDate.setText("Licence issue date");
                    tvLicenceValidDate.setText("Licence valid till date");
                    //tvLanguage.setText("Select Language");
                    dob.setText("Date of Birth");


                    aadharImagepathFront = "";
                    ivAdharFrontSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);

                    aadharImagepathBack = "";
                    ivAdharABackSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
                    etAdharName.setText("");
                    etAdharNumber.setText("");
                    // tvLanguageAdhar.setText("Select Language");
                    tvAdharDOB.setText("Date of Birth");

                    licenceImagePath = "";
                    Glide.with(EditDeliveryBoyActivity.this).load(licenceImagePath).placeholder(R.drawable.blfront).into(ivDriving_Licence);

                    licenceImagePathBack = "";
                    Glide.with(EditDeliveryBoyActivity.this).load(licenceImagePathBack).placeholder(R.drawable.dlback).into(ivDlImageBack);

                }
            }
        });

        tvLanguage.setText(datum.getLanguages());
        etDexter.setText(datum.getDexter());
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

    private class getBitmapFromURL2 extends AsyncTask<String, String, Bitmap> {
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
                licenceImagePathBack = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    private class getBitmapFromURLAdharFront extends AsyncTask<String, String, Bitmap> {
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
                aadharImagepathFront = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    private class getBitmapFromURLAdharBack extends AsyncTask<String, String, Bitmap> {
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
                aadharImagepathBack = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    private class getBitmapFromURLVoterFront extends AsyncTask<String, String, Bitmap> {
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
                voterImagePathFront = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    private class getBitmapFromURLVoterBack extends AsyncTask<String, String, Bitmap> {
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
                voterImagePathBack = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyActivity.this);
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

            case R.id.ivDrivingLicenceBack:
                captureImage(3);
                break;

            case R.id.tvAdharFront:
                capture_aadhar_front_and_back_image(1);
                break;
            case R.id.ivCheckAdharSelected:
                capture_aadhar_front_and_back_image(1);
                break;

            case R.id.tvAdharBack: {
                capture_aadhar_front_and_back_image(2);
            }
            break;


            case R.id.tvVoterFront:
                capture_aadhar_front_and_back_image(6);
                break;
            case R.id.ivcheckedVoterFront:
                capture_aadhar_front_and_back_image(6);
                break;

            case R.id.ivcheckedVoterBack:
                capture_aadhar_front_and_back_image(7);
                break;

            case R.id.tvVoterBack:
                capture_aadhar_front_and_back_image(7);
                break;

            case R.id.ivCheckAdharBackSelected:
                capture_aadhar_front_and_back_image(2);
                break;

            case R.id.tvScan:
                if (isAadhaarBackSelected == true & isAadhaarFrontSelected == true) {

                    capture_aadhar_front_and_back_image(3);
                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "Please Upload Both Images First", Toast.LENGTH_LONG).show();
                }

                break;


            case R.id.ivProfileImage:
                captureImage(1);
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
                                stringdob = year + "-" + monthString + "-" + daysString;
                                dob.setText(daysString + "-" + monthString + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();

                break;


            case R.id.tvDOBAdhar:
                final Calendar c4 = Calendar.getInstance();
                mYear = c4.get(Calendar.YEAR);
                mMonth = c4.get(Calendar.MONTH);
                mDay = c4.get(Calendar.DAY_OF_MONTH);

                Date todayAdhar = new Date();
                Calendar c5 = Calendar.getInstance();
                c4.setTime(todayAdhar);
                c4.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDateAdhar = c4.getTime().getTime(); //
                DatePickerDialog datePickerDialogAdhar = new DatePickerDialog(this,
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
                                stringdob = year + "-" + monthString + "-" + daysString;
                                tvAdharDOB.setText(daysString + "-" + monthString + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogAdhar.getDatePicker().setMaxDate(minDateAdhar);
                datePickerDialogAdhar.show();

                break;


            case R.id.tvDObVoter:
                final Calendar c6 = Calendar.getInstance();
                mYear = c6.get(Calendar.YEAR);
                mMonth = c6.get(Calendar.MONTH);
                mDay = c6.get(Calendar.DAY_OF_MONTH);

                Date todayVoter = new Date();
                Calendar c7 = Calendar.getInstance();
                c6.setTime(todayVoter);
                c6.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDateVoter = c6.getTime().getTime(); //
                DatePickerDialog datePickerDialogVoter = new DatePickerDialog(this,
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
                                stringdob = year + "-" + monthString + "-" + daysString;
                                tvVoterDOB.setText(daysString + "-" + monthString + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogVoter.getDatePicker().setMaxDate(minDateVoter);
                datePickerDialogVoter.show();

                break;


            case R.id.rvSelctVoterDOB:
                final Calendar c9 = Calendar.getInstance();
                mYear = c9.get(Calendar.YEAR);
                mMonth = c9.get(Calendar.MONTH);
                mDay = c9.get(Calendar.DAY_OF_MONTH);

                Date today9 = new Date();
                Calendar c10 = Calendar.getInstance();
                c9.setTime(today9);
                c9.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDateVoter10 = c9.getTime().getTime(); //
                DatePickerDialog datePickerDialogVoter10 = new DatePickerDialog(this,
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
                                stringdob = year + "-" + monthString + "-" + daysString;
                                tvVoterDOB.setText(daysString + "-" + monthString + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogVoter10.getDatePicker().setMaxDate(minDateVoter10);
                datePickerDialogVoter10.show();

                break;


            case R.id.tvIssueDate:
                final Calendar cIssue = Calendar.getInstance();
                mYear = cIssue.get(Calendar.YEAR);
                mMonth = cIssue.get(Calendar.MONTH);
                mDay = cIssue.get(Calendar.DAY_OF_MONTH);

                Date todayIssue = new Date();
                Calendar c1Issue = Calendar.getInstance();
                cIssue.setTime(todayIssue);
                // cIssue.add(Calendar.YEAR, -); // Subtract 18 year
                long minDateIssue = cIssue.getTime().getTime(); //
                DatePickerDialog datePickerDialogIssue = new DatePickerDialog(this,
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
                                tvLicenceIssueDate.setText(year + "-" + monthString + "-" + daysString);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogIssue.getDatePicker().setMaxDate(minDateIssue);
                datePickerDialogIssue.show();

                break;

            case R.id.tvValidTilldate:
                final Calendar cValid = Calendar.getInstance();
                mYear = cValid.get(Calendar.YEAR);
                mMonth = cValid.get(Calendar.MONTH);
                mDay = cValid.get(Calendar.DAY_OF_MONTH);

                Date todayValid = new Date();
                Calendar c1Valid = Calendar.getInstance();
                //cValid.setTime(todayValid);
                // cIssue.add(Calendar.YEAR, -); // Subtract 18 year
                //long minDateValid = cValid.getTime().getTime(); //
                DatePickerDialog datePickerDialogValid = new DatePickerDialog(this,
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
                                tvLicenceValidDate.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogValid.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialogValid.show();

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

                        if (kyc_type.equalsIgnoreCase("3")) {
                            ApiCallSubmitOcr("", "", stringdob, etDrivingNumber.getText().toString(), ocrid, filename, fileUrl);
                        }
                        if (kyc_type.equalsIgnoreCase("2")) {
                            ApiCallSubmitOcr("", "", stringdob, etVoterIdNumber.getText().toString(), ocrid, filename, fileUrl);

                        }
                        ApiCallSubmit();
                    }
                }
                break;

        }

    }


    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_FRONT;
                    Intent intent1 = ImagePicker.getCameraIntent(EditDeliveryBoyActivity.this);
                    startActivityForResult(intent1, IMAGE_AADHAR_FRONT);

                }
                if (imageSide == 2) {

                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_BACK;

                    Intent intent2 = ImagePicker.getCameraIntent(EditDeliveryBoyActivity.this);
                    startActivityForResult(intent2, IMAGE_AADHAR_BACK);
                }

                if (imageSide == 3) {

                    Intent intent3 = new Intent(EditDeliveryBoyActivity.this, ScannerActivity.class);
                    startActivityForResult(intent3, REQUEST_SCANNER);
                }


                if (imageSide == 6) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_FRONT;

                    Intent intent6 = ImagePicker.getCameraIntent(EditDeliveryBoyActivity.this);
                    startActivityForResult(intent6, IMAGE_VOTER_FRONT);
                }

                if (imageSide == 7) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_BACK;
                    Intent intent7 = ImagePicker.getCameraIntent(EditDeliveryBoyActivity.this);
                    startActivityForResult(intent7, IMAGE_VOTER_BACK);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(EditDeliveryBoyActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

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
            if (tvLanguage.getText().toString().equalsIgnoreCase("Select Language")) {
                Toast.makeText(this, "Please Select Language", Toast.LENGTH_SHORT).show();
                // showMSG(false, "Provide Pincode");
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


            if (TextUtils.isEmpty(etRoute.getText().toString())) {
                etRoute.requestFocus();
                etRoute.setError("Provide Route");
                // showMSG(false, "Provide Pincode");
                return false;

            }


        }
        if (i == 3) {


            if (kyc_type.equalsIgnoreCase("3")) {


                if (licenceImagePath.equals("")) {
                    Toast.makeText(this, "Please Select Licence Image", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (licenceImagePathBack.equals("")) {
                    Toast.makeText(this, "Please Select Licence back side Image", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (TextUtils.isEmpty(etDrivingNumber.getText().toString())) {
                    etDrivingNumber.requestFocus();
                    etDrivingNumber.setError("Provide Driving Licence Number");
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (tvLicenceIssueDate.getText().equals("Licence issue date") || tvLicenceIssueDate.getText().equals("")) {
                    Toast.makeText(this, "Please Select Licence issue date", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (tvLicenceValidDate.getText().equals("Licence valid till date") || tvLicenceValidDate.getText().equals("")) {
                    Toast.makeText(this, "Please Select valid till date", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }


                if (dob.getText().equals("Date of Birth") || dob.getText().equals("")) {
                    Toast.makeText(this, "Provide Date of Birth", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
            } else if (kyc_type.equalsIgnoreCase("1")) {

                if (aadharImagepathFront.equals("")) {
                    Toast.makeText(this, "Please upload Aadhar card front image", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (aadharImagepathBack.equals("")) {
                    Toast.makeText(this, "Please upload Aadhar card back image", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }

                if (TextUtils.isEmpty(etAdharName.getText().toString())) {
                    etAdharName.requestFocus();
                    etAdharName.setError("Please enter Aadhar name");

                    return false;
                }
                if (TextUtils.isEmpty(etAdharNumber.getText().toString())) {
                    etAdharNumber.requestFocus();
                    etAdharNumber.setError("Please enter Aadhar number");

                    return false;
                }
                if (tvAdharDOB.getText().equals("Date of Birth") || tvAdharDOB.getText().equals("")) {
                    tvAdharDOB.requestFocus();
                    tvAdharDOB.setError("Please enter date of birth");

                    return false;
                }

                if (etAdharNumber.getText().length() < 12) {
                    etAdharNumber.requestFocus();
                    etAdharNumber.setError("Please enter valid Aadhar number");

                    return false;
                }


            } else {
                if (voterImagePathFront.equals("")) {
                    Toast.makeText(this, "Please upload Voter Id front image", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }
                if (voterImagePathBack.equals("")) {
                    Toast.makeText(this, "Please upload Voter Id back image", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return false;
                }

                if (TextUtils.isEmpty(etVoterName.getText().toString())) {
                    etVoterName.requestFocus();
                    etVoterName.setError("Please enter Voter name");

                    return false;
                }

                if (tvVoterDOB.getText().equals("Date of Birth") || tvVoterDOB.getText().equals("")) {
                    tvVoterDOB.requestFocus();
                    tvVoterDOB.setError("Please enter date of birth");

                    return false;

                }
                if (TextUtils.isEmpty(etVoterIdNumber.getText().toString())) {
                    etVoterIdNumber.requestFocus();
                    etVoterIdNumber.setError("Please enter voter id number");

                    return false;
                }

            }
        }


        return true;
    }


    private void APiCallgetvehicleNames() {
        mProgressDialog.show();
        Call<GetVehicleTypes> call = ApiClient.getClient().create(ApiInterface.class).getVehicleListing("Bearer " + sessionManager.getToken());
        call.enqueue(new Callback<GetVehicleTypes>() {
            @Override
            public void onResponse(Call<GetVehicleTypes> call, Response<GetVehicleTypes> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetVehicleTypes getVehicleTypes = response.body();
                    if (getVehicleTypes.getResponseCode() == 200) {
                        List<String> items = Arrays.asList(getVehicleTypes.getData().split("\\s*,\\s*"));
                        EditDeliveryBoyActivity.CustomAdapter<String> spinnerArrayAdapter = new EditDeliveryBoyActivity.CustomAdapter<String>(EditDeliveryBoyActivity.this, android.R.layout.simple_spinner_item, items);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spVehicleForDelivery.setAdapter(spinnerArrayAdapter);
                        for (int i = 0; i < items.size(); i++) {
                            if (datum.getVehicleForDelivery().equalsIgnoreCase(items.get(i))) {
                                spVehicleForDelivery.setSelection(i);
                            }
                        }

                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, getVehicleTypes.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2116" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetVehicleTypes> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2116" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });


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
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
// find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);
            File imagefile1 = new File(profileImagepath);
            prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
            params.put(Constants.NAME, etName.getText().toString().trim());
            params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
            params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString().trim());
            params.put(Constants.PARAM_GENDER, radioSexButton.getText().toString());
            params.put(Constants.PARAM_DELIVERY_BOY_ID, deliveryBoy);
            params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());
            params.put(Constants.PARAM_BLOOD_GROUP, spBloodGroupType.getSelectedItem().toString());
            call = ApiClient.getClient().create(ApiInterface.class).DeliveryBoysDetailsValid1("Bearer " + sessionManager.getToken(), validationapiUrl, params, prof_image);


        } else if (i1 == 2) {
            params.put(Constants.PARAM_CURRENT_RESIDENTIAL, currentAdress);
            params.put("is_dc_dexter_present", String.valueOf(optnal));
            params.put("dexter", etDexter.getText().toString());
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
            params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString());
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
                            if (addDelBoyResponse.getData().get(0).getIsDcDexterPresent() == 1) {
                                optional = true;
                                optnal = 1;
                            } else {
                                optional = false;
                                optnal = 0;
                            }

                        } else if (i1 == 2) {
                            llAddressInfo.setVisibility(View.GONE);
                            llDrivingDetails.setVisibility(View.VISIBLE);
                            llOwnerInfo.setVisibility(View.GONE);
                            view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            svMain.scrollTo(0, svMain.getBottom());

                            if (kyc_old.equalsIgnoreCase("1")) {
                                kyc_type = kyc_old;
                                rg.check(R.id.rbAdharCard);
                                llDrivingLicenceDetails.setVisibility(View.GONE);
                                llAadharDetails.setVisibility(View.VISIBLE);
                                llVoterDetails.setVisibility(View.GONE);
                                if (datum.getAadhaar_card_front() != null && !datum.getAadhaar_card_front().equalsIgnoreCase("")) {
                                    new getBitmapFromURLAdharFront().execute(Constants.BaseImageURL + datum.getAadhaar_card_front());
                                    Glide.with(EditDeliveryBoyActivity.this).load(Constants.BaseImageURL + datum.getAadhaar_card_front()).into(ivAdharIamgeFront);
                                }
                                if (datum.getAadhaar_card_back() != null && !datum.getAadhaar_card_back().equalsIgnoreCase("")) {
                                    new getBitmapFromURLAdharBack().execute(Constants.BaseImageURL + datum.getAadhaar_card_back());
                                    Glide.with(EditDeliveryBoyActivity.this).load(Constants.BaseImageURL + datum.getAadhaar_card_back()).into(ivAdharImageBack);
                                }

                                etAdharName.setText(datum.getAadhaar_name());
                                etAdharNumber.setText(datum.getAadhaar_no());
                                tvAdharDOB.setText(datum.getAadhaar_dob());


                            } else if (kyc_old.equalsIgnoreCase("2")) {
                                kyc_type = kyc_old;
                                rg.check(R.id.rbVoter);
                                llDrivingLicenceDetails.setVisibility(View.GONE);
                                llAadharDetails.setVisibility(View.GONE);
                                llVoterDetails.setVisibility(View.VISIBLE);
                                if (datum.getVoter_front_image() != null && !datum.getVoter_front_image().equalsIgnoreCase("")) {

                                    new getBitmapFromURLVoterFront().execute(Constants.BaseImageURL + datum.getVoter_front_image());

                                    Glide.with(EditDeliveryBoyActivity.this).load(Constants.BaseImageURL + datum.getVoter_front_image()).into(ivVoterImageFront);

                                }
                                if (datum.getVoter_back_image() != null && !datum.getVoter_back_image().equalsIgnoreCase("")) {
                                    new getBitmapFromURLVoterBack().execute(Constants.BaseImageURL + datum.getVoter_back_image());
                                    Glide.with(EditDeliveryBoyActivity.this).load(Constants.BaseImageURL + datum.getVoter_back_image()).into(ivVoterImageBack);
                                }
                                etVoterName.setText(datum.getVoter_name());
                                etVoterIdNumber.setText(datum.getVoterIdNum());
                                tvVoterDOB.setText(datum.getVoter_dob());


                            } else {
                                kyc_type = kyc_old;
                                rg.check(R.id.rbDriving);
                                llDrivingLicenceDetails.setVisibility(View.VISIBLE);
                                llAadharDetails.setVisibility(View.GONE);
                                llVoterDetails.setVisibility(View.GONE);
                            }


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

                                if (validation.getBlood_group() != null && validation.getBlood_group().length() > 0) {
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getBlood_group(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(EditDeliveryBoyActivity.this, validation.getVehicle_for_delivery(), Toast.LENGTH_SHORT).show();

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
        Call<AddDelBoyResponse> callUpload = null;
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
        MultipartBody.Part adhar_front_image = null, adhar_back_image = null, voter_front_image = null, voter_back_image = null;

        MultipartBody.Part license_image = null, licence_back_image = null;
        String image = "image";
        String driving_licence = "driving_licence_image";

        File imagefile1 = new File(profileImagepath);
        prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
        //   list.add(shop_act_part);


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
        params.put("is_dc_dexter_present", String.valueOf(optnal));
        params.put(Constants.PARAM_KYC_TYPE, kyc_type);
        params.put("dexter", etDexter.getText().toString());
        params.put(Constants.PARAM_BLOOD_GROUP, spBloodGroupType.getSelectedItem().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_STATE, etCurrentState.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_PINCODE, etCurrentPincode.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS, etPerHouseNo.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS1, etPermStreet.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_LANDMARK, etPerLandmark.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_CITY, etPerCity.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_STATE, etPerState.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_PINCODE, etPerPincode.getText().toString());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, deliveryBoy);
        params.put(Constants.PARAM_GENDER, radioSexButton.getText().toString());

        params.put(Constants.PARAM_PERMANENT_ADDRESS, permAddress);
        params.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
        params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString().trim());
        /*if(isUpdate.equalsIgnoreCase("1")){
            params.put(Constants.PARAM_IS_EDIT,"1");
        }*/
        params.put(Constants.PARAM_DRIVING_LICENCE_VEHICLE, spVehicleForDelivery.getSelectedItem().toString());
        params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);


        if (kyc_type.equalsIgnoreCase("3")) {
            params.put(Constants.PARAM_DRIVING_LICENCE_NUM, etDrivingNumber.getText().toString().trim());
            params.put(Constants.PARAM_DRIVING_LICENCE_DOB, stringdob);
            params.put(Constants.PARAM_DL_VALID_TILL_DATE, tvLicenceValidDate.getText().toString().trim());
            params.put(Constants.PARAM_DL_ISSUE_DATE, tvLicenceIssueDate.getText().toString().trim());
            File imagefile2 = new File(licenceImagePath);
            license_image = MultipartBody.Part.createFormData(driving_licence, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePath)), imagefile2));

            File imagefile3 = new File(licenceImagePathBack);
            licence_back_image = MultipartBody.Part.createFormData(Constants.PARAM_DL_BACK_IMAGE, imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePathBack)), imagefile3));


            callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, license_image, licence_back_image);
        } else if (kyc_type.equalsIgnoreCase("1")) {

            params.put(Constants.PARAM_AADHAR_NO, etAdharNumber.getText().toString().trim());
            params.put(Constants.PARAM_AADHAR_DOB, stringdob);
            params.put(Constants.PARAM_AADHAR_NAME, etAdharName.getText().toString().trim());


            File imagefile2 = new File(aadharImagepathFront);
            adhar_front_image = MultipartBody.Part.createFormData("aadhaar_card_front", imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathFront)), imagefile2));

            File imagefile3 = new File(aadharImagepathBack);
            adhar_back_image = MultipartBody.Part.createFormData("aadhaar_card_back", imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathBack)), imagefile3));

            callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, adhar_front_image, adhar_back_image);

        } else {

            params.put(Constants.PARAM_VOTER_NO, etVoterIdNumber.getText().toString().trim());
            params.put(Constants.PARAM_VOTER_NAME, etVoterName.getText().toString());
            params.put(Constants.PARAM_VOTER_DOB, stringdob);

            File imagefile2 = new File(voterImagePathFront);
            voter_front_image = MultipartBody.Part.createFormData("voter_front_image", imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile2));

            File imagefile3 = new File(voterImagePathBack);
            voter_back_image = MultipartBody.Part.createFormData("voter_back_image", imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile3));

            callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, voter_front_image, voter_back_image);

        }


        // Log.e("image",list.toString());

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
                    } else if (addDelBoyResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditDeliveryBoyActivity.this);
                    } else if (addDelBoyResponse.getResponseCode() == 400) {

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
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVehicle_for_delivery(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getDriving_licence_issue_date() != null && validation.getDriving_licence_issue_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getDriving_licence_issue_date(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getDriving_licence_till_date() != null && validation.getDriving_licence_till_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getDriving_licence_till_date(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getAadhaar_card_front() != null && validation.getAadhaar_card_front().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAadhaar_card_front(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_card_back() != null && validation.getAadhaar_card_back().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAadhaar_card_back(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_name() != null && validation.getAadhaar_name().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAadhaar_name(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_dob() != null && validation.getAadhaar_dob().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAadhaar_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_no() != null && validation.getAadhaar_no().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getAadhaar_no(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_front_image() != null && validation.getVoter_front_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVoter_front_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_back_image() != null && validation.getVoter_back_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVoter_back_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_name() != null && validation.getVoter_name().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVoter_name(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getVoter_dob() != null && validation.getVoter_dob().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVoter_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoterIdNum() != null && validation.getVoterIdNum().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyActivity.this, validation.getVoterIdNum(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
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
        if (i == 3) {
            Intent chooseImageIntent1 = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent1, LICENCEIMAGEBACK);
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

                        for (int i = 0; i < dcLists.size(); i++) {
                            if (datum.getDc().equalsIgnoreCase(dcLists.get(i))) {
                                dc.setSelection(i);
                            }
                        }
                       // dc.setSelection(0);

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

        File casted_image5 = new File(licenceImagePathBack);
        if (casted_image5.exists()) {
            casted_image5.delete();
        }

        File casted_image6 = new File(profileImagepath);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }


    private void ApiCallGetDetailLicence(String drivingLicencePath, String drivinglicenceback) {
        // MultipartBody.Part voter_front_part = null;
        MultipartBody.Part driving_licence_part = null;
        MultipartBody.Part driving_licence_part_back = null;
        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

       /* File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));
*/
        File imagefile1 = new File(drivingLicencePath);
        driving_licence_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicencePath)), imagefile1));


        File imagefile2 = new File(drivinglicenceback);
        driving_licence_part_back = MultipartBody.Part.createFormData(Constants.PARAM_DL_BACK_IMAGE, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivinglicenceback)), imagefile2));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("we are retrieving information, please wait!");
        mProgressDialog.show();
        // hideKeyboardwithoutPopulateFragment();
        Call<DrivingLicenceDetailResponse> call = ApiClient.getClient2().create(ApiInterface.class).getDrivingLicenceDetail(params, driving_licence_part, driving_licence_part_back);
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

                            ocrid=String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            etDrivingNumber.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber());


                            if (!voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofExpiry().equals("")) {
                                tvLicenceValidDate.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofExpiry());
                            }
                            if (!voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofissue().equals("")) {
                                tvLicenceIssueDate.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofissue());
                            }

                            //fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            stringdob = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate();

                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = inputFormat.parse(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate());
                            String outputDateStr = outputFormat.format(date);

                            dob.setText(outputDateStr);


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

            if (requestCode == IMAGE_AADHAR_FRONT) {

                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);

                imagecamera = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                Intent intent = new Intent(EditDeliveryBoyActivity.this, CropImage2Activity.class);
                Log.e("datadata", imagecamera);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_ADHAR_FRONT);


            }
            if (requestCode == CROPPED_IMAGE_ADHAR_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                aadharImagepathFront = RealPathUtil.getPath(EditDeliveryBoyActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathFront).into(ivAdharIamgeFront);
                    isAadhaarFrontSelected = true;
                    ivAdharFrontSelected.setVisibility(View.VISIBLE);
                    tvAdharFront.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!aadharImagepathFront.equals("") && !aadharImagepathBack.equalsIgnoreCase("")) {
                    //api OCR
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallOCRAdhar();
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                    }
                    //showEditDialog();
                }


            }
            if (requestCode == IMAGE_AADHAR_BACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                Intent intent = new Intent(EditDeliveryBoyActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_ADHAR_BACK);
               /* File casted_image = new File(aadharImagepathFront);
                if (casted_image.exists()) {
                    casted_image.delete();
                }
*/
            }

            if (requestCode == CROPPED_IMAGE_ADHAR_BACK) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                aadharImagepathBack = RealPathUtil.getPath(EditDeliveryBoyActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathBack).into(ivAdharImageBack);
                    isAadhaarBackSelected = true;
                    tvAdharBack.setVisibility(View.GONE);
                    ivAdharABackSelected.setVisibility(View.VISIBLE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!aadharImagepathFront.equals("") && !aadharImagepathBack.equalsIgnoreCase("")) {
                    //api OCR
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallOCRAdhar();
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                    }
                    //showEditDialog();
                }

            }


            if (requestCode == REQUEST_SCANNER && (data != null)) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    processScannedData(data.getStringExtra(Constants.CONTENT));
                } else {
                    Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
                }

            }
            if (requestCode == IMAGE_VOTER_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                Intent intent = new Intent(EditDeliveryBoyActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_FRONT);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathFront = RealPathUtil.getPath(EditDeliveryBoyActivity.this, imgURI);

                try {
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathFront).into(ivVoterImageFront);
                    ivVoterFrontSelected.setVisibility(View.VISIBLE);
                    tvVoterFront.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {

                    try {
                        Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
                        ivVoterBackSelected.setVisibility(View.VISIBLE);
                        tvVoterBack.setVisibility(View.GONE);
                        if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {
                            ApiCallOCRVoter(voterImagePathFront, voterImagePathBack);
                            // ApiCallSubmitOcr();
                            // ApiCallSubmitKYC();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            if (requestCode == IMAGE_VOTER_BACK) {

                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);

                Intent intent = new Intent(EditDeliveryBoyActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_BACK);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_BACK) {

                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathBack = RealPathUtil.getPath(EditDeliveryBoyActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
                    ivVoterBackSelected.setVisibility(View.VISIBLE);
                    tvVoterBack.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {

                    try {
                        Glide.with(EditDeliveryBoyActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
                        ivVoterBackSelected.setVisibility(View.VISIBLE);
                        tvVoterBack.setVisibility(View.GONE);
                        if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {
                            ApiCallOCRVoter(voterImagePathFront, voterImagePathBack);
                            // ApiCallSubmitOcr();
                            // ApiCallSubmitKYC();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            if (requestCode == LICENCEIMAGE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                licenceImagePath = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                ivDriving_Licence.setPadding(0, 0, 0, 0);
                // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(this).load(licenceImagePath).into(ivDriving_Licence);
                if (!licenceImagePathBack.equalsIgnoreCase("") && !licenceImagePath.equalsIgnoreCase("")) {
                    ApiCallGetDetailLicence(licenceImagePath, licenceImagePathBack);
                }

            }

            if (requestCode == LICENCEIMAGEBACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditDeliveryBoyActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                licenceImagePathBack = ImagePicker.getBitmapPath(bitmap, EditDeliveryBoyActivity.this);
                ivDlImageBack.setPadding(0, 0, 0, 0);
                // ImageUtils.getInstant().getImageUri(EditPanCardActivity, photo);
                Glide.with(this).load(licenceImagePathBack).into(ivDlImageBack);
                ApiCallGetDetailLicence(licenceImagePath, licenceImagePathBack);

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


    private void ApiCallOCRAdhar() {
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        MultipartBody.Part attachment_adharFront = null;
        MultipartBody.Part attachment_adharback = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

        File imagefile1 = new File(aadharImagepathFront);
        attachment_adharFront = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathFront)), imagefile1));

        File imagefile = new File(aadharImagepathBack);
        attachment_adharback = MultipartBody.Part.createFormData(Constants.PARAM_BACKSIDE_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathBack)), imagefile));


        List<MultipartBody.Part> imagepart = new ArrayList<>();
        imagepart.add(attachment_adharFront);
        imagepart.add(attachment_adharback);

        Call<AdharOCRResponse> call = ApiClient.getClient2().create(ApiInterface.class).adharOcr("Bearer " + sessionManager.getToken(), params, imagepart);
        call.enqueue(new Callback<AdharOCRResponse>() {
            @Override
            public void onResponse(Call<AdharOCRResponse> call, Response<AdharOCRResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    AdharOCRResponse adharOCRResponse = response.body();
                    if (!adharOCRResponse.getIsFrontOk() && adharOCRResponse.getIsBackOk()) {
                        aadharImagepathFront = "";
                        ivAdharFrontSelected.setVisibility(View.GONE);
                        tvAdharFront.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);
                        Toast.makeText(EditDeliveryBoyActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    } else if (!adharOCRResponse.getIsBackOk() && adharOCRResponse.getIsFrontOk()) {
                        aadharImagepathBack = "";
                        ivAdharABackSelected.setVisibility(View.GONE);
                        tvAdharBack.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
                        Toast.makeText(EditDeliveryBoyActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    } else if (adharOCRResponse.getIsFrontOk() && adharOCRResponse.getIsBackOk()) {
                        //  showEditDialog(adharOCRResponse.getAadharCardDetail());
                        etAdharName.setText(adharOCRResponse.getAadharCardDetail().getName());
                        tvAdharDOB.setText(adharOCRResponse.getAadharCardDetail().getBirthDate());
                        etAdharNumber.setText(adharOCRResponse.getAadharCardDetail().getAadharCardNumber());
                    } else if (!adharOCRResponse.getIsFrontOk() && !adharOCRResponse.getIsBackOk()) {
                        aadharImagepathFront = "";
                        ivAdharFrontSelected.setVisibility(View.GONE);
                        tvAdharFront.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);
                        Toast.makeText(EditDeliveryBoyActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                        aadharImagepathBack = "";
                        ivAdharABackSelected.setVisibility(View.GONE);
                        tvAdharBack.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);

                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2089 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AdharOCRResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode 2089 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }


    protected void processScannedData(String scanData) {

        if (scanData != null) {
            scanData = scanData.replaceAll(Pattern.quote("</?xml version=\"1.0\" encoding=\"UTF-8\"?>"), "").trim();
        }

        Mylogger.getInstance().Logit(TAG, scanData);
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    String udi = "", name = "", year = "", pincode = "";
                    udi = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);

                    year = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    pincode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);
                    etAdharName.setText(name);
                    etAdharNumber.setText(udi);

                   /* sessionManager.saveData(Constants.KEY_UID, udi);
                    sessionManager.saveData(Constants.KEY_AADHAR_NAME, name);
                    sessionManager.saveData(Constants.KEY_YEAR, year);
                    sessionManager.saveData(Constants.KEY_PINCODE, pincode);


                    Mylogger.getInstance().Logit(TAG, "udi: " + udi);
                    Mylogger.getInstance().Logit(TAG, "name: " + name);
                    Mylogger.getInstance().Logit(TAG, "year: " + year);
                    Mylogger.getInstance().Logit(TAG, "pincode: " + pincode);*/
                } else if (eventType == XmlPullParser.END_TAG) {
                } else if (eventType == XmlPullParser.TEXT) {
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Mylogger.getInstance().Logit("XmlPullParserException", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Mylogger.getInstance().Logit("IOException", e.toString());
        } catch (SQLiteConstraintException e) {
            Mylogger.getInstance().Logit("SQLiteConstraintException", e.toString());
        }
        /* if (kyc_type.equalsIgnoreCase("1")) {*/
        if (!aadharImagepathFront.equals("") && !aadharImagepathBack.equalsIgnoreCase("")) {
            //api OCR
            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                ApiCallOCRAdhar();
            } else {
                Constants.ShowNoInternet(EditDeliveryBoyActivity.this);
            }
            //showEditDialog();
        } else {
            Toast.makeText(EditDeliveryBoyActivity.this, "Please upload Both Images.", Toast.LENGTH_SHORT).show();
        }


    }


    private void ApiCallOCRVoter(String voterImagePathFront, String voterImagePathBack) {


        MultipartBody.Part voter_front_part = null;
        MultipartBody.Part voter_back_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

        File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));

        File imagefile1 = new File(voterImagePathBack);
        voter_back_part = MultipartBody.Part.createFormData(Constants.PARAM_BACKSIDE_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("we are retrieving information, please wait!");
        mProgressDialog.show();
        //hideKeyboardwithoutPopulateFragment();

        Call<VoterOCRGetDetaisResponse> call = ApiClient.getClient2().create(ApiInterface.class).getVoterIdOCR(params, voter_front_part, voter_back_part);
        call.enqueue(new Callback<VoterOCRGetDetaisResponse>() {
            @Override
            public void onResponse(Call<VoterOCRGetDetaisResponse> call, Response<VoterOCRGetDetaisResponse> response) {
                String voterDetailsId, fileUrl, filename;
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        VoterOCRGetDetaisResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            voterDetailsId = String.valueOf(voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdDetailId());
                            ocrid=voterDetailsId;
                            filename = voterOCRGetDetaisResponse.getVoterIdDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getVoterIdDetail().getFileUrl();
                            String outputDateStr = "";

                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            if (!voterOCRGetDetaisResponse.getVoterIdDetail().getBirthDate().equalsIgnoreCase("")) {
                                Date date = inputFormat.parse(voterOCRGetDetaisResponse.getVoterIdDetail().getBirthDate());
                                outputDateStr = outputFormat.format(date);

                            }

                            tvVoterDOB.setText(outputDateStr);
                            etVoterName.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getName());
                            //etVoterFatherName.setText(voterOCRGetDetaisResponse.getVoterIdDetail().);
                            etVoterIdNumber.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdNumber());

                            //ApiCallSubmitOcr(voterOCRGetDetaisResponse.getVoterIdDetail().getName(), "", tvVoterDOB.getText().toString(), etVoterIdNumber.getText().toString(), voterDetailsId, filename, fileUrl);

                           /* DialogUtil.VoterDetail(EditDeliveryBoyActivity.this, voterOCRGetDetaisResponse.getVoterIdDetail().getName(), voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdNumber(), voterOCRGetDetaisResponse.getVoterIdDetail().getFatherName(), outputDateStr, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {
                                    ApiCallSubmitOcr(name, fathername, dob, id, voterDetailsId, filename, fileUrl);
                                    //  ApiCallSubmitKYC(name, fathername, dob, id);
                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                }

                                @Override
                                public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                }

                            });*/


                        } else {


                           /* DialogUtil.VoterDetail(EditDeliveryBoyActivity.this, "", "", "", "", new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {
                                    ApiCallSubmitKYC(name, fathername, dob, id);
                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                }

                                @Override
                                public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                }

                            });*/

                            Toast.makeText(EditDeliveryBoyActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VoterOCRGetDetaisResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(EditDeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void ApiCallSubmitOcr(String name, String fathername, String dob, String id, String detailsId, String filename, String fileUrl) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        JSONObject jsonObject = new JSONObject();
        if (kyc_type.equalsIgnoreCase("2")) {

            try {
                jsonObject.put(Constants.PARAM_VOTER_ID_DETAIL_ID, VoteridDetailId);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_VOTER_ID_NUMBER, etVoterIdNumber.getText().toString());
                jsonObject.put(Constants.PARAM_NAME, etVoterName.getText().toString());
                jsonObject.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString());
                jsonObject.put(Constants.PARAM_BIRTH_DATE, tvVoterDOB.getText().toString());
                jsonObject.put(Constants.PARAM_FILE_NAME, filename);
                jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_NAME, backsidefilename);
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_URL, backsideFileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", e.getLocalizedMessage());
            }

            params.put(Constants.PARAM_VOTERID_DETAIL, jsonObject.toString());

        }
        if (kyc_type.equalsIgnoreCase("3")) {

            try {
                jsonObject.put(Constants.PARAM_DL_DETAIL_ID, detailsId);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_DRIVING_LICENCE_NUMBER, id);
                jsonObject.put(Constants.PARAM_NAME, name);
                jsonObject.put(Constants.PARAM_FATHER_NAME, fathername);
                jsonObject.put(Constants.PARAM_BIRTH_DATE, dob);
                jsonObject.put(Constants.PARAM_FILE_NAME, filename);
                jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", e.getLocalizedMessage());
            }
            params.put(Constants.PARAM_DRIVING_LICENCE_DETAIL, jsonObject.toString());

        }


        Call<ApiSubmitOCRPanVoterDlResponse> call = ApiClient.getClient2().create(ApiInterface.class).submitDlorVoter(params);
        call.enqueue(new Callback<ApiSubmitOCRPanVoterDlResponse>() {
            @Override
            public void onResponse(Call<ApiSubmitOCRPanVoterDlResponse> call, Response<ApiSubmitOCRPanVoterDlResponse> response) {
                try {
                    if (response.isSuccessful()) {

                    } else {
                        Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyActivity.this, " Contact administartor immediately", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiSubmitOCRPanVoterDlResponse> call, Throwable t) {
                Toast.makeText(EditDeliveryBoyActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }


}
