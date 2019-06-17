package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.canvascoders.opaper.Beans.AadhaarCard;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse.ApiSubmitOCRPanVoterDlResponse;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.activity.ScannerActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.AadhaarXMLParser;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DataAttributes;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;
import static com.canvascoders.opaper.fragment.PanVerificationFragment.CROPPED_IMAGE;
import static com.canvascoders.opaper.utils.Constants.showAlert;


public class AadharVerificationFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_AADHAR_FRONT = 1021;
    private static final int IMAGE_AADHAR_BACK = 1022, IMAGE_VOTER_FRONT = 1023, IMAGE_VOTER_BACK = 1024, IMAGE_DL_FRONT = 1025, IMAGE_DL_BACK = 1026;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = ".oppr";
    private final static int REQUEST_SCANNER = 1000;
    public String udi = "";
    public String name = "";
    private int mYear, mMonth, mDay, mHour, mMinute;
    Bitmap bitmap1;
    public String year = "";
    private EditText etvoterName, etVoterFatherName, etVoterId, etNameDL, etDlFatherName, etDLNumber;
    private TextView tvVoterDOB, tvDlDOB;
    private LinearLayout lladharfront, lladharback, llvoterfront, llVoterBack, llDlFront, llDlBack;
    private CardView cvDLFront, cvDlBack;
    private ImageView ivAdharFront, ivAdharback, ivvoterFront, ivvoterFrontSelected, ivVoterBack, ivVoterBackSelected, ivDlBack, ivDlBackSelected, ivDlfront, ivDlfrontSelected;
    private int layout = 0;
    public String pincode = "";
    private String kyc_type = "1";
    static String TAG = "AadharVerification";
    private static int IMAGE_SELCTION_CODE = 0;
    private Button btn_next, btnSubmit;
    private List<String> docTypeList = new ArrayList<>();
    private Uri imgURI;
    private String aadharImagepathFront = "", voterImagePathFront = "", voterImagePathBack = "", dlImagePathBack = "", dlImageOathFront = "";
    private String aadharImagepathBack = "";
    private SessionManager sessionManager;
    //private PermissionUtil.PermissionRequestObject mALLPermissionRequest;
    private ImageView btn_aadhar_front, btn_aadhar_back;
    private ImageView btn_aadhar_front_select, btn_aadhar_back_select;
    private boolean isAadhaarFrontSelected = false;
    private boolean isAadhaarBackSelected = false;
    Context mcontext;
    private ScrollView svAadhar, svVoter, svDL;
    private Spinner snDocType;
    View view;
    String VoteridDetailId,filename,fileUrl,backsideFileUrl,backsidefilename,dlIdDetailId;
    private RelativeLayout rvAdhar, rvother, rvFrontDl, rvBackDl, rvMain;
    ProgressDialog mProgressDialog;
    private TextView tv_review;
    GPSTracker gps;
    private String lattitude="",longitude="";
    private RequestPermissionHandler requestPermissionHandler;
    private String str_process_id;
    private static int CROPPED_IMAGE_VOTER_FRONT = 7000, CROPPED_IMAGE_VOTER_BACK = 8000, CROPPED_IMAGE_DL_BACK = 8001, CROPPED_IMAGE_DL_FRONT = 8002;

//    private static File getOutputMediaFile(int type) {
//        File mediaStorageDir = new File(Environment.getDownloadCacheDirectory(), IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Mylogger.getInstance().Logit(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
//                        + IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "O_" + System.currentTimeMillis() + ".jpeg");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aadhar, container, false);

        mcontext = this.getActivity();

        requestPermissionHandler = new RequestPermissionHandler();

        initView();


        sessionManager = new SessionManager(getActivity());

        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        Log.e("process_id", "Process_id" + str_process_id);
        OTPActivity.settitle("Aadhaar Verification");

        return view;
    }


    private void initView() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setTitle("Please wait updating vendor details");
        mProgressDialog.setCancelable(false);
        //svAadhar = view.findViewById(R.id.svAadhar);
        svDL = view.findViewById(R.id.svDL);
        svVoter = view.findViewById(R.id.svVoter);
        rvAdhar = view.findViewById(R.id.rvAdhar);
        rvother = view.findViewById(R.id.rvOther);
       /* rvFrontDl =view.findViewById(R.id.rvfrontDl);
        rvFrontDl.setOnClickListener(this);
        rvBackDl = view.findViewById(R.id.rvBackDl);
        rvBackDl.setOnClickListener(this);*/
        llDlFront = view.findViewById(R.id.llDLFront);
        llDlFront.setOnClickListener(this);

        llDlBack = view.findViewById(R.id.llDlBack);
        llDlBack.setOnClickListener(this);


      /*  cvDLFront = view.findViewById(R.id.cvDlFront);
        cvDLFront.setOnClickListener(this);
        cvDlBack = view.findViewById(R.id.cdDLBack);
        cvDlBack.setOnClickListener(this);*/

        llvoterfront = view.findViewById(R.id.llvoterFront);
        llvoterfront.setOnClickListener(this);
        llVoterBack = view.findViewById(R.id.llvoterBack);
        llVoterBack.setOnClickListener(this);

        ivvoterFront = view.findViewById(R.id.ivVoterFront);
        ivvoterFront.setOnClickListener(this);
        ivvoterFrontSelected = view.findViewById(R.id.ivVoterFrontSelected);

        ivVoterBack = view.findViewById(R.id.ivBackVoter);
        ivVoterBack.setOnClickListener(this);
        ivVoterBackSelected = view.findViewById(R.id.ivBackVoter_selected);
        ivDlBack = view.findViewById(R.id.ivDlBack);

        ivDlBackSelected = view.findViewById(R.id.ivDlBackSelected);
        ivDlfront = view.findViewById(R.id.ivDLFront);
        ivDlfrontSelected = view.findViewById(R.id.ivDLFrontSelected);

        etvoterName = view.findViewById(R.id.etVoterName);
        etVoterFatherName = view.findViewById(R.id.etVoterFatherName);
        etVoterId = view.findViewById(R.id.etVoterId);
        tvVoterDOB = view.findViewById(R.id.tvVoterDob);

        etNameDL = view.findViewById(R.id.etNameDl);
        etDlFatherName = view.findViewById(R.id.etDlFatherName);
        etDLNumber = view.findViewById(R.id.etDLNumber);
        tvDlDOB = view.findViewById(R.id.tvDlDob);
        tvDlDOB.setOnClickListener(this);

        tvVoterDOB.setOnClickListener(this);
        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);


        btnSubmit = view.findViewById(R.id.btSubmit);
        btnSubmit.setOnClickListener(this);
        btn_aadhar_front = (ImageView) view.findViewById(R.id.btn_aadhar_front);
        btn_aadhar_front_select = (ImageView) view.findViewById(R.id.btn_aadhar_front_select);
        btn_aadhar_back = (ImageView) view.findViewById(R.id.btn_aadhar_back);
        btn_aadhar_back_select = (ImageView) view.findViewById(R.id.btn_aadhar_back_select);
        tv_review = (TextView) view.findViewById(R.id.tv_review_detail);

        btn_aadhar_front.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                capture_aadhar_front_and_back_image(1);

            }
        });

        snDocType = (Spinner) view.findViewById(R.id.snDocType);
        docTypeList = Arrays.asList(getResources().getStringArray(R.array.DocType));
        CustomAdapter<String> doctypeadapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, docTypeList);
        doctypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snDocType.setAdapter(doctypeadapter);

        snDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Aadhar Card")) {
                    kyc_type = "1";
                    rvAdhar.setVisibility(View.VISIBLE);
                    rvother.setVisibility(View.GONE);
                    svDL.setVisibility(View.GONE);
                    svVoter.setVisibility(View.GONE);

                    dlImagePathBack = "";
                    ivDlBackSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(dlImagePathBack).into(ivDlBack);

                    dlImageOathFront = "";
                    ivDlfrontSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(dlImageOathFront).into(ivDlfront);

                    voterImagePathBack = "";
                    ivVoterBackSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterBack);

                    voterImagePathFront = "";
                    ivvoterFrontSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(voterImagePathFront).into(ivvoterFront);

                }
                if (selectedItem.equals("Voter ID")) {
                    kyc_type = "2";
                    rvAdhar.setVisibility(View.GONE);
                    rvother.setVisibility(View.VISIBLE);
                    svDL.setVisibility(View.GONE);
                    svVoter.setVisibility(View.VISIBLE);


                    dlImagePathBack = "";
                    ivDlBackSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(dlImagePathBack).into(ivDlBack);

                    dlImageOathFront = "";
                    ivDlfrontSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(dlImageOathFront).into(ivDlfront);

                    aadharImagepathFront = "";
                    btn_aadhar_front_select.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(aadharImagepathFront).into(btn_aadhar_front);

                    aadharImagepathBack = "";
                    btn_aadhar_back_select.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(aadharImagepathBack).into(btn_aadhar_back);

                }
                if (selectedItem.equals("Driving Licence")) {
                    kyc_type = "3";
                    rvother.setVisibility(View.VISIBLE);
                    rvAdhar.setVisibility(View.GONE);
                    svDL.setVisibility(View.VISIBLE);
                    svVoter.setVisibility(View.GONE);

                    voterImagePathBack = "";
                    ivVoterBackSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterBack);

                    voterImagePathFront = "";
                    ivvoterFrontSelected.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(voterImagePathFront).into(ivvoterFront);

                    aadharImagepathFront = "";
                    btn_aadhar_front_select.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(aadharImagepathFront).into(btn_aadhar_front);

                    aadharImagepathBack = "";
                    btn_aadhar_back_select.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(aadharImagepathBack).into(btn_aadhar_back);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_aadhar_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                capture_aadhar_front_and_back_image(2);
            }
        });

        tv_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAadhaarBackSelected == true & isAadhaarFrontSelected == true) {
                    showEditDialog();
                }
            }
        });

    }

    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_FRONT;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_AADHAR_FRONT);

                }
                if (imageSide == 2) {

                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_BACK;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_AADHAR_BACK);
                }

                if (imageSide == 3) {

                    Intent intent = new Intent(getActivity(), ScannerActivity.class);
                    startActivityForResult(intent, REQUEST_SCANNER);
                }
                if (imageSide == 4) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_FRONT;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_VOTER_FRONT);
                }
                if (imageSide == 5) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_BACK;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_VOTER_BACK);
                }

                if (imageSide == 6) {

                    IMAGE_SELCTION_CODE = IMAGE_DL_FRONT;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_DL_FRONT);
                }

                if (imageSide == 7) {

                    IMAGE_SELCTION_CODE = IMAGE_DL_BACK;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_DL_BACK);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(mcontext, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

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
        switch (v.getId()) {
            case R.id.btn_next:
                if (isAadhaarBackSelected == true & isAadhaarFrontSelected == true) {

                    capture_aadhar_front_and_back_image(3);
                }
                break;


            case R.id.btSubmit:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation(kyc_type)){
                        ApiCallSubmitOcr();
                       ApiCallSubmitKYC();
                    }

                } else {
                    Constants.ShowNoInternet(getActivity());
                }
                break;


            case R.id.ivVoterFront:
                capture_aadhar_front_and_back_image(4);
                break;


            case R.id.ivBackVoter:
                capture_aadhar_front_and_back_image(5);
                break;

            case R.id.llDLFront:
                capture_aadhar_front_and_back_image(6);
                break;

            case R.id.llDlBack:
                capture_aadhar_front_and_back_image(7);
                break;


            case R.id.tvVoterDob:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c.setTime(today);
                c.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate = c.getTime().getTime(); //
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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

                                tvVoterDOB.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();

                break;
            case R.id.tvDlDob:
                final Calendar c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                Date today1 = new Date();
                Calendar c3 = Calendar.getInstance();
                c3.setTime(today1);
                c3.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate1 = c2.getTime().getTime(); //
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(),
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

                                tvDlDOB.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.getDatePicker().setMaxDate(minDate1);
                datePickerDialog1.show();

                break;
           /* case R.id.lladharfront:
                capture_aadhar_front_and_back_image(1);
                break;

            case R.id.lladharback:
                capture_aadhar_front_and_back_image(2);
                break;*/


        }


    }

    private void ApiCallSubmitOcr() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        JSONObject jsonObject = new JSONObject();
        if (kyc_type.equalsIgnoreCase("2")) {

            try {
                jsonObject.put(Constants.PARAM_VOTER_ID_DETAIL_ID,VoteridDetailId);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_VOTER_ID_NUMBER, etVoterId.getText().toString());
                jsonObject.put(Constants.PARAM_NAME, etvoterName.getText().toString());
                jsonObject.put(Constants.PARAM_FATHER_NAME, etVoterFatherName.getText().toString());
                jsonObject.put(Constants.PARAM_BIRTH_DATE, tvVoterDOB.getText().toString());
                jsonObject.put(Constants.PARAM_FILE_NAME,filename);
                jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_NAME,backsidefilename);
                jsonObject.put(Constants.PARAM_BACKSIDE_FILE_URL,backsideFileUrl);
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("error",e.getLocalizedMessage());
            }

            params.put(Constants.PARAM_VOTERID_DETAIL,jsonObject.toString());

        }
        if (kyc_type.equalsIgnoreCase("3")) {

            try {
                jsonObject.put(Constants.PARAM_DL_DETAIL_ID,dlIdDetailId);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_DRIVING_LICENCE_NUMBER, etDLNumber.getText().toString());
                jsonObject.put(Constants.PARAM_NAME, etNameDL.getText().toString());
                jsonObject.put(Constants.PARAM_FATHER_NAME, etDlFatherName.getText().toString());
                jsonObject.put(Constants.PARAM_BIRTH_DATE, tvDlDOB.getText().toString());
                jsonObject.put(Constants.PARAM_FILE_NAME,filename);
                jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);

            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("error",e.getLocalizedMessage());
            }
            params.put(Constants.PARAM_DRIVING_LICENCE_DETAIL,jsonObject.toString());

        }


       Call<ApiSubmitOCRPanVoterDlResponse> call = ApiClient.getClient2().create(ApiInterface.class).submitDlorVoter(params);
        call.enqueue(new Callback<ApiSubmitOCRPanVoterDlResponse>() {
            @Override
            public void onResponse(Call<ApiSubmitOCRPanVoterDlResponse> call, Response<ApiSubmitOCRPanVoterDlResponse> response) {
                try{
                    if(response.isSuccessful()){
                    /*    ApiSubmitOCRPanVoterDlResponse apiSubmitOCRPanVoterDlResponse = response.body();
                        if(apiSubmitOCRPanVoterDlResponse.getStatus().equalsIgnoreCase("status")){
                        Toast.makeText(getActivity(),apiSubmitOCRPanVoterDlResponse.getStatus(),Toast.LENGTH_SHORT).show();
                        }
                        else{

                        }
                    */}
                }
                catch (Exception e){
                    Toast.makeText(getActivity()," Contact administartor immediately",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiSubmitOCRPanVoterDlResponse> call, Throwable t) {

            }
        });
    }


    private void showEditDialog() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(AadharVerificationFragment.this);
                editNameDialogFragment.setCancelable(false);
                editNameDialogFragment.show(getChildFragmentManager(), "fragment_edit_name");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if ((requestCode == IMAGE_AADHAR_FRONT && resultCode == RESULT_OK) || (requestCode == IMAGE_AADHAR_BACK && resultCode == RESULT_OK) || requestCode == REQUEST_SCANNER && resultCode == RESULT_OK) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_AADHAR_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                aadharImagepathFront = ImagePicker.getBitmapPath(bitmap, getActivity());
                Glide.with(getActivity()).load(aadharImagepathFront).into(btn_aadhar_front);
                isAadhaarFrontSelected = true;
                btn_aadhar_front_select.setVisibility(View.VISIBLE);
                Log.e("aadharcard", "front image" + aadharImagepathFront);
                /*btn_aadhar_front_select.setVisibility(View.VISIBLE);*/
            }
            if (requestCode == IMAGE_AADHAR_BACK) {

                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                aadharImagepathBack = ImagePicker.getBitmapPath(bitmap, getActivity());
                Glide.with(getActivity()).load(aadharImagepathBack).into(btn_aadhar_back);
                isAadhaarBackSelected = true;
                btn_aadhar_back_select.setVisibility(View.VISIBLE);

                    /*Bitmap photo = (Bitmap) data.getExtras().get("data");
                    btn_aadhar_back.setImageBitmap(photo);
                    imgURI = ImageUtils.getInstant().getImageUri(getActivity(), photo);
                    aadharImagepathBack = ImageUtils.getInstant().getRealPathFromURI(mcontext, imgURI);*/
                Log.e("aadharcard", "back image" + aadharImagepathBack);
                    /*isAadhaarBackSelected = true;
                    btn_aadhar_back_select.setVisibility(View.VISIBLE);*/

            }
            if (requestCode == REQUEST_SCANNER && (data != null)) {
                processScannedData(data.getStringExtra(Constants.CONTENT));
            }
            if (requestCode == IMAGE_VOTER_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                voterImagePathFront = ImagePicker.getBitmapPath(bitmap, getActivity());
                // Glide.with(getActivity()).load(voterImagePathFront).into(ivvoterFront);
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(voterImagePathFront)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_FRONT);

                //new ResizeAsync().execute();
            }
            if (requestCode == CROPPED_IMAGE_VOTER_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathFront = RealPathUtil.getPath(getActivity(), imgURI);

                try {
                    Glide.with(getActivity()).load(voterImagePathFront).into(ivvoterFront);
                    ivvoterFrontSelected.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == IMAGE_VOTER_BACK) {

                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                voterImagePathBack = ImagePicker.getBitmapPath(bitmap, getActivity());
                /*Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterBack);
                ivVoterBackSelected.setVisibility(View.VISIBLE);
*/
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(voterImagePathBack)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_BACK);

                //new ResizeAsync().execute();
            }
            if (requestCode == CROPPED_IMAGE_VOTER_BACK) {

                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathBack = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterBack);
                    ivVoterBackSelected.setVisibility(View.VISIBLE);
                    if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {
                        ApiCallOCRVoter(voterImagePathFront, voterImagePathBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == IMAGE_DL_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                dlImageOathFront = ImagePicker.getBitmapPath(bitmap, getActivity());
                /*Glide.with(getActivity()).load(aadharImagepathFront).into(btn_aadhar_front);
                isAadhaarFrontSelected = true;
                btn_aadhar_front_select.setVisibility(View.VISIBLE);
                Log.e("aadharcard", "front image" + aadharImagepathFront);*/
                /*btn_aadhar_front_select.setVisibility(View.VISIBLE);*/
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(dlImageOathFront)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);
            }


            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                dlImageOathFront = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(dlImageOathFront).into(ivDlfront);
                    ivDlfrontSelected.setVisibility(View.VISIBLE);
                    if (!dlImageOathFront.equals("")) {
                        ApiCallGetDetailLicence(dlImageOathFront);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (requestCode == IMAGE_DL_BACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                dlImagePathBack = ImagePicker.getBitmapPath(bitmap, getActivity());
                Glide.with(getActivity()).load(dlImagePathBack).into(ivDlBack);
                ivDlBackSelected.setVisibility(View.VISIBLE);
            }

            setButtonImage();
        }


    }

    private void ApiCallOCRVoter(String voterImagePathFront, String voterImagePathBack) {
        MultipartBody.Part voter_front_part = null;
        MultipartBody.Part voter_back_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);

        File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));

        File imagefile1 = new File(voterImagePathBack);
        voter_back_part = MultipartBody.Part.createFormData(Constants.PARAM_BACKSIDE_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("Please wait getting details...");
        mProgressDialog.show();
        Call<VoterOCRGetDetaisResponse> call = ApiClient.getClient2().create(ApiInterface.class).getVoterIdOCR(params, voter_front_part, voter_back_part);
        call.enqueue(new Callback<VoterOCRGetDetaisResponse>() {
            @Override
            public void onResponse(Call<VoterOCRGetDetaisResponse> call, Response<VoterOCRGetDetaisResponse> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        VoterOCRGetDetaisResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            etvoterName.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getName());
                            etVoterId.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdNumber());
                            etVoterFatherName.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getFatherName());
                            tvVoterDOB.setText(voterOCRGetDetaisResponse.getVoterIdDetail().getBirthDate());
                            VoteridDetailId = String.valueOf(voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdDetailId());
                            filename = voterOCRGetDetaisResponse.getVoterIdDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getVoterIdDetail().getFileUrl();
                            backsidefilename=voterOCRGetDetaisResponse.getVoterIdDetail().getBackSideFileName();
                            backsideFileUrl = voterOCRGetDetaisResponse.getVoterIdDetail().getBackSideFileUrl();
                        } else {
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VoterOCRGetDetaisResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void ApiCallGetDetailLicence(String drivingLicencePath) {
        // MultipartBody.Part voter_front_part = null;
        MultipartBody.Part driving_licence_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);

       /* File imagefile = new File(voterImagePathFront);
        voter_front_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));
*/
        File imagefile1 = new File(drivingLicencePath);
        driving_licence_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicencePath)), imagefile1));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("Please wait getting details...");
        mProgressDialog.show();
        Call<DrivingLicenceDetailResponse> call = ApiClient.getClient2().create(ApiInterface.class).getDrivingLicenceDetail(params, driving_licence_part);
        call.enqueue(new Callback<DrivingLicenceDetailResponse>() {
            @Override
            public void onResponse(Call<DrivingLicenceDetailResponse> call, Response<DrivingLicenceDetailResponse> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        DrivingLicenceDetailResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            etNameDL.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getName());
                            etDlFatherName.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName());
                            tvDlDOB.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate());
                            etDLNumber.setText(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber());
                            dlIdDetailId = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            filename = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileUrl();

                        } else {
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DrivingLicenceDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setButtonImage() {
        if (isAadhaarBackSelected == true & isAadhaarFrontSelected == true) {
            btn_next.setBackground(getResources().getDrawable(R.drawable.btn_active));
            btn_next.setEnabled(true);
            btn_next.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }


    public void ProcessData(String scanData) {
        try {

            if (scanData.contains("<?xml") || scanData.startsWith("<?xml")) {
                scanData = scanData.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", "");
            }

            AadhaarCard newCard = new AadhaarXMLParser().parse(scanData.trim());
            Mylogger.getInstance().Logit(TAG, newCard.toString());
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

                    udi = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);
                    year = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    pincode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);

                    sessionManager.saveData(Constants.KEY_UID, udi);
                    sessionManager.saveData(Constants.KEY_AADHAR_NAME, name);
                    sessionManager.saveData(Constants.KEY_YEAR, year);
                    sessionManager.saveData(Constants.KEY_PINCODE, pincode);


                    Mylogger.getInstance().Logit(TAG, "udi: " + udi);
                    Mylogger.getInstance().Logit(TAG, "name: " + name);
                    Mylogger.getInstance().Logit(TAG, "year: " + year);
                    Mylogger.getInstance().Logit(TAG, "pincode: " + pincode);
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


        showEditDialog();

    }


    /*public void storeAadhar() {

        MultipartBody.Part aadharcard_front_part = null;
        MultipartBody.Part aadharcard_back_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        udi = sessionManager.getData(Constants.KEY_UID);
        name = sessionManager.getData(Constants.KEY_AADHAR_NAME);
        year = sessionManager.getData(Constants.KEY_YEAR);
        pincode = sessionManager.getData(Constants.KEY_PINCODE);


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_UID, udi.trim());
            params.put(Constants.PARAM_NAME, name.trim());
            params.put(Constants.PARAM_YEAR_OF_BIRTH, year.trim());
            params.put(Constants.PARAM_PINCODE, pincode.trim());

            File imagefile = new File(aadharImagepathFront);
            aadharcard_front_part = MultipartBody.Part.createFormData(Constants.PARAM_AADHAR_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathFront)), imagefile));

            File imagefile1 = new File(aadharImagepathBack);
            aadharcard_back_part = MultipartBody.Part.createFormData(Constants.PARAM_AADHAR_BACK, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathBack)), imagefile));

            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Please wait getting details...");
            mProgressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstoreAadhar("Bearer "+sessionManager.getToken(),params, aadharcard_front_part, aadharcard_back_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        CommonResponse getaadhardetail = response.body();


                        if (getaadhardetail.getResponseCode() == 200) {


                            if (aadharImagepathFront != null) {
                                //deleteFileFromMediaManager(getActivity(), aadharImagepathFront);
                            }
                            if (aadharImagepathBack != null) {
                                //deleteFileFromMediaManager(getActivity(), aadharImagepathBack);
                            }

                            showAlert(getaadhardetail.getResponse());

                            sessionManager.saveData(Constants.KEY_AADHAR_NAME, "");
                            sessionManager.saveData(Constants.KEY_UID, "");
                            sessionManager.saveData(Constants.KEY_PINCODE, "");
                            sessionManager.saveData(Constants.KEY_YEAR, "");


                        } else {
                            if (getaadhardetail.getResponseCode() == 405) {
                                sessionManager.logoutUser(mcontext);
                            }
                            showAlert2(getaadhardetail.getResponse());
                        }

                    } else {
                        Toast.makeText(mcontext, "No response", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    mProgressDialog.dismiss();

                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }*/

    public void storeAadhar() {
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

        MultipartBody.Part aadharcard_front_part = null;
        MultipartBody.Part aadharcard_back_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        udi = sessionManager.getData(Constants.KEY_UID);
        name = sessionManager.getData(Constants.KEY_AADHAR_NAME);
        year = sessionManager.getData(Constants.KEY_YEAR);
        pincode = sessionManager.getData(Constants.KEY_PINCODE);


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_UID, udi.trim());
            params.put(Constants.PARAM_KYC_TYPE, kyc_type);
            params.put(Constants.PARAM_NAME, name.trim());
            params.put(Constants.PARAM_YEAR_OF_BIRTH, year.trim());
            params.put(Constants.PARAM_PINCODE, pincode.trim());
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);

            File imagefile = new File(aadharImagepathFront);
            aadharcard_front_part = MultipartBody.Part.createFormData(Constants.PARAM_AADHAR_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathFront)), imagefile));

            File imagefile1 = new File(aadharImagepathBack);
            aadharcard_back_part = MultipartBody.Part.createFormData(Constants.PARAM_AADHAR_BACK, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(aadharImagepathBack)), imagefile1));

            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Please wait getting details...");
            mProgressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class). getstoreAadhar("Bearer " + sessionManager.getToken(), params, aadharcard_front_part, aadharcard_back_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        CommonResponse getaadhardetail = response.body();


                        if (getaadhardetail.getResponseCode() == 200) {


                            if (aadharImagepathFront != null) {
                                //deleteFileFromMediaManager(getActivity(), aadharImagepathFront);
                            }
                            if (aadharImagepathBack != null) {
                                //deleteFileFromMediaManager(getActivity(), aadharImagepathBack);
                            }

                            showAlert(getaadhardetail.getResponse());

                            sessionManager.saveData(Constants.KEY_AADHAR_NAME, "");
                            sessionManager.saveData(Constants.KEY_UID, "");
                            sessionManager.saveData(Constants.KEY_PINCODE, "");
                            sessionManager.saveData(Constants.KEY_YEAR, "");


                        } else {
                            if (getaadhardetail.getResponseCode() == 405) {
                                sessionManager.logoutUser(mcontext);
                            }
                            if (getaadhardetail.getResponseCode()==411){
                                sessionManager.logoutUser(getActivity());
                            }

                            if (getaadhardetail.getResponseCode() == 400) {
                                if (getaadhardetail.getValidation() != null) {
                                    Validation validation = getaadhardetail.getValidation();
                                    if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                        Toast.makeText(mcontext, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                                        // return false;
                                    } else if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                        Toast.makeText(mcontext, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                                    } else if (validation.getAdharCardFront() != null && validation.getAdharCardFront().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getAdharCardFront(), Toast.LENGTH_LONG).show();
                                    } else if (validation.getAdharCardBack() != null && validation.getAdharCardBack().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getAdharCardBack(), Toast.LENGTH_LONG).show();
                                    } else if (validation.getUid() != null && validation.getUid().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getUid(), Toast.LENGTH_LONG).show();
                                    } else if (validation.getName() != null && validation.getName().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getName(), Toast.LENGTH_LONG).show();
                                    } else if (validation.getYob() != null && validation.getYob().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getYob(), Toast.LENGTH_LONG).show();
                                    } else if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                        Toast.makeText(getActivity(), validation.getPincode(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mcontext, getaadhardetail.getResponse(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mcontext, getaadhardetail.getResponse(), Toast.LENGTH_SHORT).show();

                                }
                            }
                            showAlert2(getaadhardetail.getResponse());
                        }

                    } else {
                        Toast.makeText(mcontext, "No response", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    mProgressDialog.dismiss();

                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }

    private void showAlert(String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("KYC Verification");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                commanFragmentCallWithoutBackStack(new PanVerificationFragment());

            }
        });
        alertDialog.show();

    }

    private void showAlert2(String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Aadhaarcard Verification");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

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

    /*public void deleteFileFromMediaManager(Context context, String path) {
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


    private boolean validation(String type) {
        if (type.equalsIgnoreCase("2")) {
            if (TextUtils.isEmpty(etvoterName.getText().toString())) {
                etvoterName.requestFocus();
                etvoterName.setError("Provide Voter Name");

                // showMSG(false, "dProvide Email");
                return false;
            }

            if (TextUtils.isEmpty(etVoterFatherName.getText().toString())) {
                etVoterFatherName.requestFocus();
                //showMSG(false, "Provide Store name");
                etVoterFatherName.setError("Provide Father name");
                return false;
            }
            if (TextUtils.isEmpty(etVoterId.getText().toString())) {
                etVoterId.requestFocus();
                etVoterId.setError("Provide Voter ID");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(tvVoterDOB.getText().toString())) {
                Toast.makeText(getActivity(), "Provide Date of Birth", Toast.LENGTH_SHORT).show();

                return false;
            }


            if (TextUtils.isEmpty(tvVoterDOB.getText().toString())) {
                Toast.makeText(getActivity(), "Provide Date of Birth", Toast.LENGTH_SHORT).show();

                return false;
            }

            if (voterImagePathFront.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Select Voter ID", Toast.LENGTH_SHORT).show();

                return false;
            }

            if (voterImagePathBack.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Select Back Side of Voter ID", Toast.LENGTH_SHORT).show();

                return false;
            }
            return true;
        }
        if (type.equalsIgnoreCase("3")) {
            if (TextUtils.isEmpty(etNameDL.getText().toString())) {
                etNameDL.requestFocus();
                etNameDL.setError("Provide Name");
                return false;
            }

            if (TextUtils.isEmpty(etDlFatherName.getText().toString())) {
                etDlFatherName.requestFocus();
                //showMSG(false, "Provide Store name");
                etDlFatherName.setError("Provide Father name");
                return false;
            }
            if (tvDlDOB.getText().equals("Date of Birth")) {
                tvDlDOB.requestFocus();
                tvDlDOB.setError("Provide Date of Birth");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(etDLNumber.getText().toString())) {
                etDLNumber.requestFocus();
                etDLNumber.setError("Provide Driving Licence Number");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (dlImageOathFront.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please Select Driving Licence Image", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (dlImagePathBack.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please Select Driving Licence Back Image", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
        return true;
    }


    private void ApiCallSubmitKYC() {
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
        MultipartBody.Part aadharcard_front_part = null;
        MultipartBody.Part aadharcard_back_part = null;
        Map<String, String> params = new HashMap<String, String>();

        if (kyc_type.equalsIgnoreCase("2")) {

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_KYC_TYPE, kyc_type);
            params.put(Constants.PARAM_VOTER_NAME, etvoterName.getText().toString());
            params.put(Constants.PARAM_VOTER_FATHER_NAME, etVoterFatherName.getText().toString());
            params.put(Constants.PARAM_VOTER_DOB, tvVoterDOB.getText().toString());
            params.put(Constants.PARAM_VOTER_ID_NUM, etVoterId.getText().toString());
            /*params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);*/

            File imagefile = new File(voterImagePathFront);
            aadharcard_front_part = MultipartBody.Part.createFormData(Constants.PARAM_VOTER_CARD_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathFront)), imagefile));

            File imagefile1 = new File(voterImagePathBack);
            aadharcard_back_part = MultipartBody.Part.createFormData(Constants.PARAM_VOTER_CARD_BACK, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(voterImagePathBack)), imagefile1));


        }
        if (kyc_type.equalsIgnoreCase("3")) {

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_KYC_TYPE, kyc_type);
            params.put(Constants.PARAM_DL_NAME, etNameDL.getText().toString());
            params.put(Constants.PARAM_DL_FATHER_NAME, etDlFatherName.getText().toString());
            params.put(Constants.PARAM_DL_DOB, tvDlDOB.getText().toString());
            params.put(Constants.PARAM_DL_ID_NUM, etDLNumber.getText().toString());
          /*  params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);*/

            File imagefile = new File(dlImageOathFront);
            aadharcard_front_part = MultipartBody.Part.createFormData(Constants.PARAM_DL_CARD_FRONT, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(dlImageOathFront)), imagefile));

            File imagefile1 = new File(dlImagePathBack);
            aadharcard_back_part = MultipartBody.Part.createFormData(Constants.PARAM_DL_CARD_BACK, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(dlImagePathBack)), imagefile1));

        }

        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {


            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Please wait getting details...");
            mProgressDialog.show();
            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstoreAadhar("Bearer " + sessionManager.getToken(), params, aadharcard_front_part, aadharcard_back_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        CommonResponse getaadhardetail = response.body();


                        if (getaadhardetail.getResponseCode() == 200) {

                            showAlert(getaadhardetail.getResponse());

                        } else {
                            if (getaadhardetail.getResponseCode() == 405) {
                                sessionManager.logoutUser(mcontext);
                            }
                            if (getaadhardetail.getResponseCode() == 411) {
                                sessionManager.logoutUser(mcontext);
                            }

                            if (getaadhardetail.getResponseCode() == 400) {
                                if (getaadhardetail.getValidation() != null) {
                                    Validation validation = getaadhardetail.getValidation();
                                    if (validation.getVoterCardFront() != null && validation.getVoterCardFront().length() > 0) {
                                        Toast.makeText(mcontext, validation.getVoterCardFront(), Toast.LENGTH_SHORT).show();
                                        // return false;
                                    }  if (validation.getVoterCardBack() != null && validation.getVoterCardBack().length() > 0) {
                                        Toast.makeText(mcontext, validation.getVoterCardBack(), Toast.LENGTH_SHORT).show();
                                        // return false;
                                    }  if (validation.getVoterDob() != null && validation.getVoterDob().length() > 0) {
                                        // Toast.makeText(mcontext, validation.getVoterCardBack(), Toast.LENGTH_SHORT).show();
                                        tvVoterDOB.setError(validation.getVoterDob());
                                        tvVoterDOB.requestFocus();
                                    }  if (validation.getVoterName() != null && validation.getVoterName().length() > 0) {
                                        // Toast.makeText(mcontext, validation.getVoterCardBack(), Toast.LENGTH_SHORT).show();
                                        etvoterName.setError(validation.getVoterName());
                                        etvoterName.requestFocus();
                                    } if (validation.getVoterFatherName() != null && validation.getVoterFatherName().length() > 0) {
                                        // Toast.makeText(mcontext, validation.getVoterCardBack(), Toast.LENGTH_SHORT).show();
                                        etVoterFatherName.setError(validation.getVoterFatherName());
                                        etVoterFatherName.requestFocus();
                                    }  if (validation.getVoterIdNum() != null && validation.getVoterIdNum().length() > 0) {
                                        // Toast.makeText(mcontext, validation.getVoterCardBack(), Toast.LENGTH_SHORT).show();
                                        etVoterId.setError(validation.getVoterIdNum());
                                        etVoterId.requestFocus();
                                    }  if (validation.getDlCardFront() != null && validation.getDlCardFront().length() > 0) {
                                        Toast.makeText(mcontext, validation.getDlCardFront(), Toast.LENGTH_SHORT).show();
                                        /*etVoterId.setError(validation.getVoterIdNum());
                                        etVoterId.requestFocus();*/
                                    } if (validation.getDlCardBack() != null && validation.getDlCardBack().length() > 0) {
                                        Toast.makeText(mcontext, validation.getDlCardBack(), Toast.LENGTH_SHORT).show();
                                        /*etVoterId.setError(validation.getVoterIdNum());
                                        etVoterId.requestFocus();*/
                                    } if (validation.getDlDob() != null && validation.getDlDob().length() > 0) {
                                        tvDlDOB.setError(validation.getDlDob());
                                        tvDlDOB.requestFocus();
                                    }  if (validation.getDlFatherName() != null && validation.getDlFatherName().length() > 0) {
                                        etDlFatherName.setError(validation.getDlFatherName());
                                        etDlFatherName.requestFocus();
                                    }  if (validation.getDlNumber() != null && validation.getDlNumber().length() > 0) {
                                        etDLNumber.setError(validation.getDlNumber());
                                        etDLNumber.requestFocus();
                                    }  if (validation.getDlName() != null && validation.getDlName().length() > 0) {
                                        etNameDL.setError(validation.getDlName());
                                        etNameDL.requestFocus();
                                    }
                                } else {
                                    showAlert2(getaadhardetail.getResponse());
                                }
                            }
                            showAlert2(getaadhardetail.getResponse());
                        }

                    } else {
                        Toast.makeText(mcontext, "Contact administrator immediately", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    mProgressDialog.dismiss();

                }
            });

        }
    }
}
