package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.AdharocrResponse.AdharOCRResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse.ApiSubmitOCRPanVoterDlResponse;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
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

public class EditDeliveryBoyTSActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog mProgressDialog;
    RequestPermissionHandler requestPermissionHandler;
    SessionManager sessionManager;
    String str_process_id = "", delivery_boy_id = "";
    String kyc_type_old = "", kyc_type = "3";
    Datum datum;
    private static int CROPPED_IMAGE_VOTER_FRONT = 7000, CROPPED_IMAGE_VOTER_BACK = 8000, CROPPED_IMAGE_DL_BACK = 8001, CROPPED_IMAGE_DL_FRONT = 8002, CROPPED_IMAGE_ADHAR_FRONT = 8003, CROPPED_IMAGE_ADHAR_BACK = 8004;
    String VoteridDetailId, filename, fileUrl, backsideFileUrl, backsidefilename, dlIdDetailId;
    private Uri imgURI;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String lattitude = "", longitude = "", currentAdress = "", permAddress = "";
    private EditText etName, etFatherName, etPhoneNumber, etRoute, etDrivingNumber;
    private static final int IMAGE_AADHAR_FRONT = 1021;
    private static final int IMAGE_AADHAR_BACK = 1022, IMAGE_VOTER_FRONT = 1023, IMAGE_VOTER_BACK = 1024, IMAGE_DL_FRONT = 1025, IMAGE_DL_BACK = 1026;
    private final static int REQUEST_SCANNER = 1000;
    private TextView tvLanguage, tvLanguageAdhar, tvLanguage1, dob;
    private String profileImagepath = "", licenceImagePath = "", licenceBackImagePath = "";
    private int PROFILEIMAGE = 200, LICENCEIMAGE = 300, LICENCEIMAGEBACK = 400;
    private boolean isAadhaarFrontSelected = false;
    private boolean isAadhaarBackSelected = false;
    RadioGroup rg;
    String stringDob = "";
    GPSTracker gps;
    TextView tvAdharDOB, tvVoterDOB;
    private TextView tvAdharFront, tvAdharBack, tvVoterFront, tvVoterBack, tvDlFront, tvDlBack, tvScan;
    RelativeLayout rvSelctVoterDOB;

    private String imagecamera = "", aadharImagepathFront = "", voterImagePathFront = "", voterImagePathBack = "", dlImagePathBack = "", dlImageOathFront = "";
    private ImageView ivAdharFrontSelected, ivAdharABackSelected, ivVoterFrontSelected, ivDlFrontSelected, ivVoterBackSelected, ivAdharIamgeFront, ivAdharImageBack, ivVoterImageFront, ivVoterImageBack, ivDlImageFront, ivDlImageBack;
    private TextView tvLicenceIssueDate, tvLicenceValidDate;
    LinearLayout llDrivingLicenceDetails, llAadharDetails, llVoterDetails;
    private ImageView ivProfile, ivDriving_Licence, ivDrivingLicenceBack, ivBack;
    private String aadharImagepathBack = "";
    private static int IMAGE_SELCTION_CODE = 0;
    private EditText etAdharName, etDObAdhar, etAdharNumber, etVoterName, etVoterFatherName, etVoterDOB, etVoterIdNumber;
    private Button btSubmit;
    String ocrid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy_ts);
        requestPermissionHandler = new RequestPermissionHandler();
        mProgressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        delivery_boy_id = getIntent().getStringExtra("delivery_boy_id");
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            APiCallGetDeliveryBoyDeetails();
        } else {
            Constants.ShowNoInternet(EditDeliveryBoyTSActivity.this);
        }

        init();
    }

    private void init() {
        dob = findViewById(R.id.tvDateofBirth);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvLicenceIssueDate = findViewById(R.id.tvIssueDate);
        tvLicenceIssueDate.setOnClickListener(this);
        tvLicenceValidDate = findViewById(R.id.tvValidTilldate);
        tvLicenceValidDate.setOnClickListener(this);


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


        dob.setOnClickListener(this);
        etDrivingNumber = findViewById(R.id.etLicenceNumber);
        rg = findViewById(R.id.rgMain);
        llDrivingLicenceDetails = findViewById(R.id.llDrivingDetails);
        llAadharDetails = findViewById(R.id.llAadharDetails);
        llVoterDetails = findViewById(R.id.llVoterInfo);

        rvSelctVoterDOB = findViewById(R.id.rvSelctVoterDOB);
        rvSelctVoterDOB.setOnClickListener(this);


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
        ivDriving_Licence = findViewById(R.id.ivDrivingLicence);
        ivDriving_Licence.setOnClickListener(this);
        ivDrivingLicenceBack = findViewById(R.id.ivDrivingLicenceBack);
        ivDrivingLicenceBack.setOnClickListener(this);


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
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathBack).placeholder(R.drawable.voterback).into(ivVoterImageBack);

                    voterImagePathFront = "";
                    ivVoterFrontSelected.setVisibility(View.GONE);
                    tvVoterFront.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathFront).placeholder(R.drawable.voterfront).into(ivVoterImageFront);

                    etVoterName.setText("");
                    tvVoterDOB.setText("Date of Birth");
                    etVoterIdNumber.setText("");
                    //tvLanguage1.setText("Select Language");


                    aadharImagepathFront = "";
                    ivAdharFrontSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);

                    aadharImagepathBack = "";
                    ivAdharABackSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
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
                    Glide.with(EditDeliveryBoyTSActivity.this).load(licenceImagePath).placeholder(R.drawable.blfront).into(ivDriving_Licence);

                    licenceBackImagePath = "";
                    Glide.with(EditDeliveryBoyTSActivity.this).load(licenceBackImagePath).placeholder(R.drawable.dlback).into(ivDrivingLicenceBack);


                    voterImagePathBack = "";
                    ivVoterBackSelected.setVisibility(View.GONE);
                    tvVoterBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathBack).placeholder(R.drawable.voterback).into(ivVoterImageBack);


                    voterImagePathFront = "";
                    ivVoterFrontSelected.setVisibility(View.GONE);
                    tvVoterFront.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathFront).placeholder(R.drawable.voterfront).into(ivVoterImageFront);

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
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);

                    aadharImagepathBack = "";
                    ivAdharABackSelected.setVisibility(View.GONE);
                    tvAdharBack.setVisibility(View.VISIBLE);
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
                    etAdharName.setText("");
                    etAdharNumber.setText("");
                    // tvLanguageAdhar.setText("Select Language");
                    tvAdharDOB.setText("Date of Birth");

                    licenceImagePath = "";
                    Glide.with(EditDeliveryBoyTSActivity.this).load(licenceImagePath).placeholder(R.drawable.blfront).into(ivDriving_Licence);
                    licenceBackImagePath = "";
                    Glide.with(EditDeliveryBoyTSActivity.this).load(licenceBackImagePath).placeholder(R.drawable.dlback).into(ivDrivingLicenceBack);


                }
            }
        });


    }


    private void APiCallGetDeliveryBoyDeetails() {

        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, delivery_boy_id);

        Call<DeliveryboyListResponse> call = ApiClient.getClient().create(ApiInterface.class).getDelivery_boys("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DeliveryboyListResponse>() {
            @Override
            public void onResponse(Call<DeliveryboyListResponse> call, Response<DeliveryboyListResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    DeliveryboyListResponse deliveryboyListResponse = response.body();
                    if (deliveryboyListResponse.getResponseCode() == 200) {
                        datum = deliveryboyListResponse.getData().get(0);
                        kyc_type_old = datum.getKyc_type();

                        if (kyc_type_old.equalsIgnoreCase("1")) {
                            kyc_type = kyc_type_old;
                            rg.check(R.id.rbAdharCard);
                            llDrivingLicenceDetails.setVisibility(View.GONE);
                            llAadharDetails.setVisibility(View.VISIBLE);
                            llVoterDetails.setVisibility(View.GONE);
                            if (datum.getAadhaar_card_front() != null && !datum.getAadhaar_card_front().equalsIgnoreCase("")) {
                                new getBitmapFromURLAdharFront().execute(Constants.BaseImageURL + datum.getAadhaar_card_front());
                                Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getAadhaar_card_front()).into(ivAdharIamgeFront);
                            }
                            if (datum.getAadhaar_card_back() != null && !datum.getAadhaar_card_back().equalsIgnoreCase("")) {
                                new getBitmapFromURLAdharBack().execute(Constants.BaseImageURL + datum.getAadhaar_card_back());
                                Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getAadhaar_card_back()).into(ivAdharImageBack);
                            }
                            stringDob = datum.getAadhaar_dob();
                            etAdharName.setText(datum.getAadhaar_name());
                            etAdharNumber.setText(datum.getAadhaar_no());
                            tvAdharDOB.setText(datum.getAadhaar_dob());


                        } else if (kyc_type_old.equalsIgnoreCase("2")) {
                            kyc_type = kyc_type_old;
                            rg.check(R.id.rbVoter);
                            llDrivingLicenceDetails.setVisibility(View.GONE);
                            llAadharDetails.setVisibility(View.GONE);
                            llVoterDetails.setVisibility(View.VISIBLE);
                            stringDob = datum.getVoter_dob();
                            if (datum.getVoter_front_image() != null && !datum.getVoter_front_image().equalsIgnoreCase("")) {

                                new getBitmapFromURLVoterFront().execute(Constants.BaseImageURL + datum.getVoter_front_image());

                                Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getVoter_front_image()).into(ivVoterImageFront);

                            }
                            if (datum.getVoter_back_image() != null && !datum.getVoter_back_image().equalsIgnoreCase("")) {
                                new getBitmapFromURLVoterBack().execute(Constants.BaseImageURL + datum.getVoter_back_image());
                                Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getVoter_back_image()).into(ivVoterImageBack);
                            }
                            etVoterName.setText(datum.getVoter_name());
                            etVoterIdNumber.setText(datum.getVoterIdNum());
                            tvVoterDOB.setText(datum.getVoter_dob());


                        } else {
                            kyc_type = kyc_type_old;
                            rg.check(R.id.rbDriving);
                            llDrivingLicenceDetails.setVisibility(View.VISIBLE);
                            llAadharDetails.setVisibility(View.GONE);
                            llVoterDetails.setVisibility(View.GONE);
                            stringDob = datum.getDrivingLicenceDob();
                            tvLicenceIssueDate.setText(datum.getDriving_licence_issue_date());
                            tvLicenceValidDate.setText(datum.getDriving_licence_till_date());
                         /*   String outputDateStr = "";
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
                            }*/
                            dob.setText(datum.getDrivingLicenceDob());
                            etDrivingNumber.setText(datum.getDrivingLicenceNum());
                            new getBitmapFromURL1().execute(Constants.BaseImageURL + datum.getDrivingLicenceImage());
                            new getBitmapFromURL2().execute(Constants.BaseImageURL + datum.getDrivingLicenceImageback());
                            Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getDrivingLicenceImage()).into(ivDriving_Licence);
                            Glide.with(EditDeliveryBoyTSActivity.this).load(Constants.BaseImageURL + datum.getDrivingLicenceImageback()).into(ivDrivingLicenceBack);


                        }


                    } else {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryboyListResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


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
                aadharImagepathFront = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
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
                voterImagePathFront = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
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
                voterImagePathBack = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
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
                aadharImagepathBack = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


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
                                stringDob = year + "-" + monthString + "-" + daysString;
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
                                stringDob = year + "-" + monthString + "-" + daysString;
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
                                stringDob = year + "-" + monthString + "-" + daysString;
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
                                stringDob = year + "-" + monthString + "-" + daysString;
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


            case R.id.btSubmit:
                if (validation()) {

                    if (kyc_type.equalsIgnoreCase("3")) {
                        ApiCallSubmitOcr("", "", stringDob, etDrivingNumber.getText().toString(), ocrid, filename, fileUrl);
                    }
                    if (kyc_type.equalsIgnoreCase("2")) {
                        ApiCallSubmitOcr("", "", stringDob, etVoterIdNumber.getText().toString(), ocrid, filename, fileUrl);

                    }
                    if (kyc_type.equalsIgnoreCase("1")) {
                        ApiCallSubmitOcr(etAdharName.getText().toString(), "", "", etAdharNumber.getText().toString(), etAdharNumber.getText().toString(), "", "");
                    }
                    ApiCallSubmit();
                }
                break;
            case R.id.ivDrivingLicence:
                captureImage(2);
                break;
            case R.id.ivDrivingLicenceBack:
                captureImage(3);
                break;

            case R.id.ivBack:
                finish();
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
                    Toast.makeText(EditDeliveryBoyTSActivity.this, "Please Upload Both Images First", Toast.LENGTH_LONG).show();
                }

                break;


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
                licenceImagePath = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
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
                licenceBackImagePath = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyTSActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

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

    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_FRONT;
                    Intent intent1 = ImagePicker.getCameraIntent(EditDeliveryBoyTSActivity.this);
                    startActivityForResult(intent1, IMAGE_AADHAR_FRONT);

                }
                if (imageSide == 2) {

                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_BACK;

                    Intent intent2 = ImagePicker.getCameraIntent(EditDeliveryBoyTSActivity.this);
                    startActivityForResult(intent2, IMAGE_AADHAR_BACK);
                }

                if (imageSide == 3) {

                    Intent intent3 = new Intent(EditDeliveryBoyTSActivity.this, ScannerActivity.class);
                    startActivityForResult(intent3, REQUEST_SCANNER);
                }


                if (imageSide == 6) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_FRONT;

                    Intent intent6 = ImagePicker.getCameraIntent(EditDeliveryBoyTSActivity.this);
                    startActivityForResult(intent6, IMAGE_VOTER_FRONT);
                }

                if (imageSide == 7) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_BACK;
                    Intent intent7 = ImagePicker.getCameraIntent(EditDeliveryBoyTSActivity.this);
                    startActivityForResult(intent7, IMAGE_VOTER_BACK);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(EditDeliveryBoyTSActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    protected void processScannedData(String scanData) {

        if (scanData != null) {
            scanData = scanData.replaceAll(Pattern.quote("</?xml version=\"1.0\" encoding=\"UTF-8\"?>"), "").trim();
        }

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

                   /* udi = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);

                    year = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    pincode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);*/


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
                Constants.ShowNoInternet(EditDeliveryBoyTSActivity.this);
            }
            //showEditDialog();
        } else {
            Toast.makeText(EditDeliveryBoyTSActivity.this, "Please upload Both Images.", Toast.LENGTH_SHORT).show();
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
                        Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);
                        Toast.makeText(EditDeliveryBoyTSActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    } else if (!adharOCRResponse.getIsBackOk() && adharOCRResponse.getIsFrontOk()) {
                        aadharImagepathBack = "";
                        ivAdharABackSelected.setVisibility(View.GONE);
                        tvAdharBack.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);
                        Toast.makeText(EditDeliveryBoyTSActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    } else if (adharOCRResponse.getIsFrontOk() && adharOCRResponse.getIsBackOk()) {
                        //  showEditDialog(adharOCRResponse.getAadharCardDetail());
                        etAdharName.setText(adharOCRResponse.getAadharCardDetail().getName());
                        tvAdharDOB.setText(adharOCRResponse.getAadharCardDetail().getBirthDate());
                        etAdharNumber.setText(adharOCRResponse.getAadharCardDetail().getAadharCardNumber());
                    } else if (!adharOCRResponse.getIsFrontOk() && !adharOCRResponse.getIsBackOk()) {
                        aadharImagepathFront = "";
                        ivAdharFrontSelected.setVisibility(View.GONE);
                        tvAdharFront.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathFront).placeholder(R.drawable.aadharcardfront).into(ivAdharIamgeFront);
                        Toast.makeText(EditDeliveryBoyTSActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                        aadharImagepathBack = "";
                        ivAdharABackSelected.setVisibility(View.GONE);
                        tvAdharBack.setVisibility(View.VISIBLE);
                        Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathBack).placeholder(R.drawable.aadhardcardback).into(ivAdharImageBack);

                    } else {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, adharOCRResponse.getFrontBackImageMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode 2089 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AdharOCRResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode 2089 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_AADHAR_FRONT) {

                Uri uri = ImagePicker.getPickImageResultUri(this, data);

                Intent intent = new Intent(EditDeliveryBoyTSActivity.this, CropImage2Activity.class);
                Log.e("datadata", imagecamera);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_ADHAR_FRONT);


            }
            if (requestCode == CROPPED_IMAGE_ADHAR_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                aadharImagepathFront = RealPathUtil.getPath(EditDeliveryBoyTSActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathFront).into(ivAdharIamgeFront);
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
                        Constants.ShowNoInternet(EditDeliveryBoyTSActivity.this);
                    }
                    //showEditDialog();
                }


            }
            if (requestCode == IMAGE_AADHAR_BACK) {
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                Intent intent = new Intent(EditDeliveryBoyTSActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_ADHAR_BACK);
               /* File casted_image = new File(aadharImagepathFront);
                if (casted_image.exists()) {
                    casted_image.delete();
                }
*/
            }

            if (requestCode == CROPPED_IMAGE_ADHAR_BACK) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                aadharImagepathBack = RealPathUtil.getPath(EditDeliveryBoyTSActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyTSActivity.this).load(aadharImagepathBack).into(ivAdharImageBack);
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
                        Constants.ShowNoInternet(EditDeliveryBoyTSActivity.this);
                    }
                    //showEditDialog();
                }

            }


            if (requestCode == REQUEST_SCANNER && (data != null)) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    processScannedData(data.getStringExtra(Constants.CONTENT));
                } else {
                    Constants.ShowNoInternet(EditDeliveryBoyTSActivity.this);
                }

            }
            if (requestCode == IMAGE_VOTER_FRONT) {
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                Intent intent = new Intent(EditDeliveryBoyTSActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_FRONT);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathFront = RealPathUtil.getPath(EditDeliveryBoyTSActivity.this, imgURI);

                try {
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathFront).into(ivVoterImageFront);
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
                        Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
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

                Uri uri = ImagePicker.getPickImageResultUri(this, data);

                Intent intent = new Intent(EditDeliveryBoyTSActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_BACK);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_BACK) {

                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathBack = RealPathUtil.getPath(EditDeliveryBoyTSActivity.this, imgURI);
                try {
                    Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
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
                        Glide.with(EditDeliveryBoyTSActivity.this).load(voterImagePathBack).into(ivVoterImageBack);
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
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                Intent intent = new Intent(this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);
            }

            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                licenceImagePath = RealPathUtil.getPath(this, imgURI);
                try {
                    Glide.with(this).load(licenceImagePath).into(ivDriving_Licence);
                    ivDriving_Licence.setPadding(0, 0, 0, 0);
                    if (!licenceBackImagePath.equalsIgnoreCase("") && !licenceImagePath.equalsIgnoreCase(""))
                        ApiCallGetDetailLicence(licenceImagePath, licenceBackImagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == LICENCEIMAGEBACK) {
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                Intent intent = new Intent(this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_BACK);
            }

            if (requestCode == CROPPED_IMAGE_DL_BACK) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                licenceBackImagePath = RealPathUtil.getPath(this, imgURI);
                try {
                    Glide.with(this).load(licenceBackImagePath).into(ivDrivingLicenceBack);
                    ivDrivingLicenceBack.setPadding(0, 0, 0, 0);
                    tvDlBack.setVisibility(View.GONE);
                    ApiCallGetDetailLicence(licenceImagePath, licenceBackImagePath);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (requestCode == PROFILEIMAGE) {
               // Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
          //       = ImagePicker.getBitmapPath(bitmap, this);
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                //  Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                profileImagepath = ImagePicker.getPathFromUri( this,uri); // ImageUtils.getInstant().getImageUri(getActivity(), photo);

                ivProfile.setPadding(0, 0, 0, 0);// ImageUtils.getInstant().getImageUri(getActivity(), photo);

                Glide.with(this).load(profileImagepath).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(EditDeliveryBoyTSActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }
        }
    }


    private void ApiCallOCRVoter(String voterImagePathFront, String voterImagePathBack) {


        MultipartBody.Part voter_front_part = null;
        MultipartBody.Part voter_back_part = null;


        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

        File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));

        File imagefile1 = new File(voterImagePathBack);
        voter_back_part = MultipartBody.Part.createFormData(Constants.PARAM_BACKSIDE_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile));

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
                            ocrid = voterDetailsId;
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

                           /* DialogUtil.VoterDetail(EditDeliveryBoyTSActivity.this, voterOCRGetDetaisResponse.getVoterIdDetail().getName(), voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdNumber(), voterOCRGetDetaisResponse.getVoterIdDetail().getFatherName(), outputDateStr, new DialogListner() {
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


                           /* DialogUtil.VoterDetail(EditDeliveryBoyTSActivity.this, "", "", "", "", new DialogListner() {
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

                            Toast.makeText(EditDeliveryBoyTSActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyTSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VoterOCRGetDetaisResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2035 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(EditDeliveryBoyTSActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
        if (kyc_type.equalsIgnoreCase("1")) {
            try {
                jsonObject.put(Constants.PARAM_AADHAR_ID, id);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_AADHAR_NO, id);
                jsonObject.put(Constants.PARAM_NAME, name);
                jsonObject.put(Constants.PARAM_FATHER_NAME, "");
                jsonObject.put(Constants.PARAM_BIRTH_DATE, dob);
                jsonObject.put(Constants.PARAM_FILE_NAME, filename);
                jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_NAME, "");
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_URL, "");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", e.getLocalizedMessage());
            }
            params.put(Constants.PARAM_AADHAR_LICENCE_DETAIL, jsonObject.toString());
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
                        Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyTSActivity.this, " Contact administartor immediately", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiSubmitOCRPanVoterDlResponse> call, Throwable t) {
                Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2028 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void ApiCallGetDetailLicence(String drivingLicencePath, String drivingLicenceBack) {
        // MultipartBody.Part voter_front_part = null;
        MultipartBody.Part driving_licence_part = null, drivign_licence_back_part = null;


        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

       /* File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));
*/
        File imagefile1 = new File(drivingLicencePath);
        driving_licence_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicencePath)), imagefile1));

        File imagefile2 = new File(drivingLicenceBack);
        drivign_licence_back_part = MultipartBody.Part.createFormData(Constants.PARAM_DL_BACK_IMAGE, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicenceBack)), imagefile2));


        mProgressDialog.setMessage("we are retrieving information, please wait!");
        mProgressDialog.show();
        // hideKeyboardwithoutPopulateFragment();
        Call<DrivingLicenceDetailResponse> call = ApiClient.getClient2().create(ApiInterface.class).getDrivingLicenceDetail(params, driving_licence_part, drivign_licence_back_part);
        call.enqueue(new Callback<DrivingLicenceDetailResponse>() {
            @Override
            public void onResponse(Call<DrivingLicenceDetailResponse> call, Response<DrivingLicenceDetailResponse> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        DrivingLicenceDetailResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(EditDeliveryBoyTSActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            etDrivingNumber.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber());

                            //fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            dob.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate());

                            ocrid = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            /* dlnumber = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber();
                            dlIdDetailId = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            filename = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileUrl();*/
                            etDrivingNumber.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber());

                            if (!voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofExpiry().equals("")) {
                                tvLicenceValidDate.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofExpiry());
                            }
                            if (!voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofissue().equals("")) {
                                tvLicenceIssueDate.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDateofissue());
                            }

                            //fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            if (!voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate().equalsIgnoreCase("")) {
                                stringDob = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate();

                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = inputFormat.parse(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate());
                                String outputDateStr = outputFormat.format(date);

                                dob.setText(outputDateStr);
                            }


                        } else {
                            licenceImagePath = "";
                            ivDriving_Licence.setPadding(0, 0, 0, 0);
                            // ImageUtils.getInstant().getImageUri(EditPanCardActivity, photo);
                            Glide.with(EditDeliveryBoyTSActivity.this).load(licenceImagePath).into(ivDriving_Licence);
                            Toast.makeText(EditDeliveryBoyTSActivity.this, voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            licenceBackImagePath = "";
                            ivDrivingLicenceBack.setPadding(0, 0, 0, 0);
                            // ImageUtils.getInstant().getImageUri(EditPanCardActivity, photo);
                            Glide.with(EditDeliveryBoyTSActivity.this).load(licenceBackImagePath).into(ivDrivingLicenceBack);

                        }
                    } else {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditPanCardActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DrivingLicenceDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyTSActivity.this, "#errorcode :- 2036 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //      Toast.makeText(EditPanCardActivity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean validation() {

        if (kyc_type.equalsIgnoreCase("3")) {


            if (licenceImagePath.equals("")) {
                Toast.makeText(this, "Please Select Licence Image", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (licenceBackImagePath.equals("")) {
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


        return true;
    }


    private void ApiCallSubmit() {
        Call<AddDelBoyResponse> callUpload = null;
        gps = new GPSTracker(EditDeliveryBoyTSActivity.this);
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
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_KYC_TYPE, kyc_type);
        params.put(Constants.PARAM_DELIVERY_BOY_ID, delivery_boy_id);
        /*if(isUpdate.equalsIgnoreCase("1")){
            params.put(Constants.PARAM_IS_EDIT,"1");
        }*/

        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);


        if (kyc_type.equalsIgnoreCase("3")) {
            params.put(Constants.PARAM_DRIVING_LICENCE_NUM, etDrivingNumber.getText().toString().trim());
            params.put(Constants.PARAM_DRIVING_LICENCE_DOB, stringDob);
            params.put(Constants.PARAM_DL_VALID_TILL_DATE, tvLicenceValidDate.getText().toString().trim());
            params.put(Constants.PARAM_DL_ISSUE_DATE, tvLicenceIssueDate.getText().toString().trim());
            File imagefile2 = new File(licenceImagePath);
            license_image = MultipartBody.Part.createFormData(driving_licence, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePath)), imagefile2));

            File imagefile3 = new File(licenceBackImagePath);
            licence_back_image = MultipartBody.Part.createFormData(Constants.PARAM_DL_BACK_IMAGE, imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceBackImagePath)), imagefile3));

            callUpload = ApiClient.getClient().create(ApiInterface.class).edidelivery3("Bearer " + sessionManager.getToken(), params, license_image, licence_back_image);
        } else if (kyc_type.equalsIgnoreCase("1")) {

            params.put(Constants.PARAM_AADHAR_NO, etAdharNumber.getText().toString().trim());
            params.put(Constants.PARAM_AADHAR_DOB, stringDob);
            // params.put(Constants.PARAM_LANGUAGES, tvLanguageAdhar.getText().toString().trim());
            params.put(Constants.PARAM_AADHAR_NAME, etAdharName.getText().toString().trim());


            File imagefile2 = new File(aadharImagepathFront);
            adhar_front_image = MultipartBody.Part.createFormData("aadhaar_card_front", imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathFront)), imagefile2));

            File imagefile3 = new File(aadharImagepathBack);
            adhar_back_image = MultipartBody.Part.createFormData("aadhaar_card_back", imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathBack)), imagefile3));

            callUpload = ApiClient.getClient().create(ApiInterface.class).edidelivery3("Bearer " + sessionManager.getToken(), params, adhar_front_image, adhar_back_image);

        } else {

            params.put(Constants.PARAM_VOTER_NO, etVoterIdNumber.getText().toString().trim());
            params.put(Constants.PARAM_VOTER_NAME, etVoterName.getText().toString());
            params.put(Constants.PARAM_VOTER_DOB, stringDob);


            File imagefile2 = new File(voterImagePathFront);
            voter_front_image = MultipartBody.Part.createFormData("voter_front_image", imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile2));

            File imagefile3 = new File(voterImagePathBack);
            voter_back_image = MultipartBody.Part.createFormData("voter_back_image", imagefile3.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile3));

            callUpload = ApiClient.getClient().create(ApiInterface.class).edidelivery3("Bearer " + sessionManager.getToken(), params, voter_front_image, voter_back_image);

        }


        // Log.e("image",list.toString());

        callUpload.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                try {
                    mProgressDialog.dismiss();
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        deleteImages();
                        finish();
                    } else if (addDelBoyResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditDeliveryBoyTSActivity.this);
                    } else if (addDelBoyResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (addDelBoyResponse.getValidation() != null) {
                            AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                            if (validation.getImage() != null && validation.getImage().length() > 0) {
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
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

                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();


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
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVehicle_for_delivery(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getDriving_licence_issue_date() != null && validation.getDriving_licence_issue_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                tvLicenceIssueDate.setError(validation.getDriving_licence_issue_date());
                                tvLicenceIssueDate.requestFocus();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getDriving_licence_issue_date(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getDriving_licence_till_date() != null && validation.getDriving_licence_till_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                tvLicenceValidDate.setError(validation.getDriving_licence_till_date());
                                tvLicenceValidDate.requestFocus();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getDriving_licence_till_date(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getAadhaar_card_front() != null && validation.getAadhaar_card_front().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAadhaar_card_front(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_card_back() != null && validation.getAadhaar_card_back().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAadhaar_card_back(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_name() != null && validation.getAadhaar_name().length() > 0) {
                                etAdharName.setError(validation.getAadhaar_name());
                                etAdharName.requestFocus();

                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAadhaar_name(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_dob() != null && validation.getAadhaar_dob().length() > 0) {
                                tvAdharDOB.setError(validation.getAadhaar_dob());
                                tvAdharDOB.requestFocus();
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAadhaar_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_no() != null && validation.getAadhaar_no().length() > 0) {
                                etAdharNumber.setError(validation.getAadhaar_no());
                                etAdharNumber.requestFocus();
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAadhaar_no(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_front_image() != null && validation.getVoter_front_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVoter_front_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_back_image() != null && validation.getVoter_back_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVoter_back_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_name() != null && validation.getVoter_name().length() > 0) {
                                etVoterName.setError(validation.getVoter_name());
                                etVoterName.requestFocus();
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVoter_name(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getVoter_dob() != null && validation.getVoter_dob().length() > 0) {
                                tvVoterDOB.setError(validation.getVoter_dob());
                                tvVoterDOB.requestFocus();
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVoter_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoterIdNum() != null && validation.getVoterIdNum().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etVoterIdNumber.setError(validation.getVoterIdNum());
                                etVoterIdNumber.requestFocus();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getVoterIdNum(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyTSActivity.this, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditDeliveryBoyTSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(EditDeliveryBoyTSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(EditDeliveryBoyTSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                        if (addDelBoyResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditDeliveryBoyTSActivity.this);
                        } else {

                            Toast.makeText(EditDeliveryBoyTSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoyTSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyTSActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyTSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void deleteImages() {
        File casted_image = new File(licenceImagePath);
        if (casted_image.exists()) {
            casted_image.delete();
        }
        File casted_image1 = new File(licenceBackImagePath);
        if (casted_image1.exists()) {
            casted_image1.delete();
        }

    }
}
