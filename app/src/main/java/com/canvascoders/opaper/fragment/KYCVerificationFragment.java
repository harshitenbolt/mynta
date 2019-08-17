package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.AadhaarCard;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse.ApiSubmitOCRPanVoterDlResponse;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.activity.ScannerActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.AadhaarXMLParser;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DataAttributes;
import com.canvascoders.opaper.utils.DialogUtil;
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
import java.io.StringReader;
import java.util.ArrayList;
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

import static android.app.Activity.RESULT_OK;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;


public class KYCVerificationFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_AADHAR_FRONT = 1021;
    private static final int IMAGE_AADHAR_BACK = 1022, IMAGE_VOTER_FRONT = 1023, IMAGE_VOTER_BACK = 1024, IMAGE_DL_FRONT = 1025, IMAGE_DL_BACK = 1026;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = ".oppr";
    private final static int REQUEST_SCANNER = 1000;
    public String udi = "";
    private static Dialog dialog;
    public String name = "", fathername = "";

    String dob = "", dlnumber = "";
    Button btSubmit;
    private TextView tvMessage, tvTitle, tvScan;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Bitmap bitmap1;
    public String year = "";
    private EditText etvoterName, etVoterFatherName, etVoterId, etNameDL, etDlFatherName, etDLNumber;
    private TextView tvVoterDOB, tvDlDOB;
    private LinearLayout lladharfront, lladharback, llvoterfront, llVoterBack, llDlFront, llDlBack;

    private ImageView ivAdharFront, ivAdharback, ivvoterFront, ivvoterFrontSelected, ivVoterBack, ivVoterBackSelected, ivDlBack, ivDlBackSelected, ivDlfront, ivDlfrontSelected;
    private int layout = 0;
    public String pincode = "";
    private String kyc_type = "1";
    static String TAG = "AadharVerification";
    private static int IMAGE_SELCTION_CODE = 0;
    private Button btn_next, btnSubmit;
    private List<String> docTypeList = new ArrayList<>();
    private Uri imgURI;
    private String imagecamera = "", aadharImagepathFront = "", voterImagePathFront = "", voterImagePathBack = "", dlImagePathBack = "", dlImageOathFront = "";
    private String aadharImagepathBack = "";
    private SessionManager sessionManager;
    //private PermissionUtil.PermissionRequestObject mALLPermissionRequest;
    private boolean isAadhaarFrontSelected = false;
    private boolean isAadhaarBackSelected = false;
    Context mcontext;

    private Spinner snDocType;


    View view;
    String VoteridDetailId, filename, fileUrl, backsideFileUrl, backsidefilename, dlIdDetailId;
    ProgressDialog mProgressDialog;
    private TextView tv_review;


    private TextView tvAdharFront, tvAdharBack, tvVoterFront, tvVoterBack, tvDlFront, tvDlBack;
    private ImageView ivAdharFrontSelected, ivAdharABackSelected, ivVoterFrontSelected, ivDlFrontSelected, ivAdharIamgeFront, ivAdharImageBack, ivVoterImageFront, ivVoterImageBack, ivDlImageFront, ivDlImageBack;
    GPSTracker gps;
    private String lattitude = "", longitude = "";
    private RequestPermissionHandler requestPermissionHandler;
    private String str_process_id;
    private static int CROPPED_IMAGE_VOTER_FRONT = 7000, CROPPED_IMAGE_VOTER_BACK = 8000, CROPPED_IMAGE_DL_BACK = 8001, CROPPED_IMAGE_DL_FRONT = 8002, CROPPED_IMAGE_ADHAR_FRONT = 8003, CROPPED_IMAGE_ADHAR_BACK = 8004;

    CardView cdAdhar, cdVoter, cdDriving;
    LinearLayout llAdhar, llVoter, llDriving;


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
        OTPActivity.settitle("KYC Verification");


        return view;
    }


    private void initView() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setTitle("Please wait updating vendor details");
        mProgressDialog.setCancelable(false);
        llAdhar = view.findViewById(R.id.llAdharcard);
        llDriving = view.findViewById(R.id.llDrivingLicence);
        llVoter = view.findViewById(R.id.llVoterID);
        cdAdhar = view.findViewById(R.id.cdAdharcard);
        cdDriving = view.findViewById(R.id.cdDrivingLicence);
        cdVoter = view.findViewById(R.id.cdVoter);

        tvAdharFront = view.findViewById(R.id.tvAdharFront);
        tvAdharBack = view.findViewById(R.id.tvAdharBack);
        ivAdharFrontSelected = view.findViewById(R.id.ivCheckAdharSelected);
        ivAdharFrontSelected.setOnClickListener(this);
        ivAdharABackSelected = view.findViewById(R.id.ivCheckAdharBackSelected);
        ivAdharABackSelected.setOnClickListener(this);
        ivAdharImageBack = view.findViewById(R.id.ivImageAdharBack);
        ivAdharIamgeFront = view.findViewById(R.id.ivImageAdharFront);
        tvScan = view.findViewById(R.id.tvScan);
        tvScan.setOnClickListener(this);

        tvAdharBack.setOnClickListener(this);
        tvAdharFront.setOnClickListener(this);

        tvVoterFront = view.findViewById(R.id.tvVoterFront);
        tvVoterBack = view.findViewById(R.id.tvVoterBack);
        ivVoterFrontSelected = view.findViewById(R.id.ivcheckedVoterFront);
        ivVoterFrontSelected.setOnClickListener(this);
        ivVoterBackSelected = view.findViewById(R.id.ivcheckedVoterBack);
        ivVoterBackSelected.setOnClickListener(this);
        ivVoterImageBack = view.findViewById(R.id.ivVoterBack);
        ivVoterImageFront = view.findViewById(R.id.ivVoterFront);
        tvVoterBack.setOnClickListener(this);
        tvVoterFront.setOnClickListener(this);

        tvDlFront = view.findViewById(R.id.tvDLFront);
        tvDlBack = view.findViewById(R.id.tvDlBackside);
        ivDlBackSelected = view.findViewById(R.id.ivCheckDlBack);
        ivDlBackSelected.setOnClickListener(this);
        ivDlFrontSelected = view.findViewById(R.id.ivCheckDlFront);
        ivDlFrontSelected.setOnClickListener(this);
        ivDlImageFront = view.findViewById(R.id.ivDlImageFront);
        ivDlImageBack = view.findViewById(R.id.ivDlImageBack);

        tvDlBack.setOnClickListener(this);
        tvDlFront.setOnClickListener(this);


        llAdhar.setOnClickListener(this);
        llVoter.setOnClickListener(this);
        llDriving.setOnClickListener(this);


        btn_next = view.findViewById(R.id.btExtract);
        btn_next.setOnClickListener(this);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.e("back", "back from adhar");
                    return true;
                }
                return false;
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
                    Intent intent1 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent1, IMAGE_AADHAR_FRONT);

                }
                if (imageSide == 2) {

                    IMAGE_SELCTION_CODE = IMAGE_AADHAR_BACK;

                    Intent intent2 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent2, IMAGE_AADHAR_BACK);
                }

                if (imageSide == 3) {

                    Intent intent3 = new Intent(getActivity(), ScannerActivity.class);
                    startActivityForResult(intent3, REQUEST_SCANNER);
                }
                if (imageSide == 4) {

                    IMAGE_SELCTION_CODE = IMAGE_DL_FRONT;

                    Intent intent4 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent4, IMAGE_DL_FRONT);
                }
                if (imageSide == 5) {

                    IMAGE_SELCTION_CODE = IMAGE_DL_BACK;

                    Intent intent5 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent5, IMAGE_DL_BACK);
                }

                if (imageSide == 6) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_FRONT;

                    Intent intent6 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent6, IMAGE_VOTER_FRONT);
                }

                if (imageSide == 7) {

                    IMAGE_SELCTION_CODE = IMAGE_VOTER_BACK;
                    Intent intent7 = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(intent7, IMAGE_VOTER_BACK);
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

            case R.id.ivCheckAdharBackSelected:
                capture_aadhar_front_and_back_image(2);
                break;

            case R.id.tvScan:
                if (isAadhaarBackSelected == true & isAadhaarFrontSelected == true) {

                    capture_aadhar_front_and_back_image(3);
                } else {
                    Toast.makeText(getActivity(), "Please Upload Both Images First", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.tvDLFront:
                capture_aadhar_front_and_back_image(4);
                break;
            case R.id.ivCheckDlFront:
                capture_aadhar_front_and_back_image(4);
                break;


            case R.id.tvDlBackside:
                capture_aadhar_front_and_back_image(5);
                break;

            case R.id.ivCheckDlBack:
                capture_aadhar_front_and_back_image(5);
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


            case R.id.btSubmit:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation(kyc_type)) {
                        //  ApiCallSubmitOcr();
                        // ApiCallSubmitKYC();
                    }

                } else {
                    Constants.ShowNoInternet(getActivity());
                }
                break;


            case R.id.llAdharcard: {
                deleteImages();
                kyc_type = "1";
                llAdhar.setBackground(getResources().getDrawable(R.drawable.roundedcornergreen));
                cdAdhar.setVisibility(View.VISIBLE);
                cdDriving.setVisibility(View.GONE);
                cdVoter.setVisibility(View.GONE);
                llDriving.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                llVoter.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));

                dlImagePathBack = "";
                ivDlBackSelected.setVisibility(View.GONE);
                tvDlFront.setVisibility(View.VISIBLE);
                ivDlImageBack.setImageResource(R.drawable.dlback);


                dlImageOathFront = "";
                ivDlFrontSelected.setVisibility(View.GONE);
                ivDlImageFront.setImageResource(R.drawable.dlback);
                tvDlBack.setVisibility(View.VISIBLE);


                voterImagePathBack = "";
                ivVoterFrontSelected.setVisibility(View.GONE);
                tvVoterFront.setVisibility(View.VISIBLE);
                ivVoterImageFront.setImageResource(R.drawable.voterfront);

                voterImagePathFront = "";
                ivVoterBackSelected.setVisibility(View.GONE);
                tvVoterBack.setVisibility(View.VISIBLE);
                ivVoterImageBack.setImageResource(R.drawable.voterback);


            }

            break;

            case R.id.llVoterID: {
                deleteImages();

                kyc_type = "2";

                llVoter.setBackground(getResources().getDrawable(R.drawable.roundedcornergreen));
                cdAdhar.setVisibility(View.GONE);
                cdDriving.setVisibility(View.GONE);
                cdVoter.setVisibility(View.VISIBLE);

                llAdhar.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                llDriving.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));


                aadharImagepathFront = "";
                ivAdharFrontSelected.setVisibility(View.GONE);
                tvAdharFront.setVisibility(View.VISIBLE);
                ivAdharIamgeFront.setImageResource(R.drawable.aadharcardfront);


                aadharImagepathBack = "";
                ivAdharABackSelected.setVisibility(View.GONE);
                tvAdharBack.setVisibility(View.VISIBLE);
                ivAdharImageBack.setImageResource(R.drawable.aadhardcardback);


                dlImagePathBack = "";
                ivDlBackSelected.setVisibility(View.GONE);
                tvDlFront.setVisibility(View.VISIBLE);
                ivDlImageBack.setImageResource(R.drawable.dlback);


                dlImageOathFront = "";
                ivDlFrontSelected.setVisibility(View.GONE);
                ivDlImageFront.setImageResource(R.drawable.dlback);
                tvDlBack.setVisibility(View.VISIBLE);


            }
            break;

            case R.id.llDrivingLicence: {
                deleteImages();
                kyc_type = "3";
                llDriving.setBackground(getResources().getDrawable(R.drawable.roundedcornergreen));
                cdAdhar.setVisibility(View.GONE);
                cdDriving.setVisibility(View.VISIBLE);
                cdVoter.setVisibility(View.GONE);

                llAdhar.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));
                llVoter.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey));


                aadharImagepathFront = "";
                ivAdharFrontSelected.setVisibility(View.GONE);
                tvAdharFront.setVisibility(View.VISIBLE);
                ivAdharIamgeFront.setImageResource(R.drawable.aadharcardfront);

                aadharImagepathBack = "";
                ivAdharABackSelected.setVisibility(View.GONE);
                tvAdharBack.setVisibility(View.VISIBLE);
                ivAdharImageBack.setImageResource(R.drawable.aadhardcardback);

                voterImagePathBack = "";
                ivVoterFrontSelected.setVisibility(View.GONE);
                tvVoterFront.setVisibility(View.VISIBLE);
                ivVoterImageFront.setImageResource(R.drawable.voterfront);

                voterImagePathFront = "";
                ivVoterBackSelected.setVisibility(View.GONE);
                tvVoterBack.setVisibility(View.VISIBLE);
                ivVoterImageBack.setImageResource(R.drawable.voterback);

            }

            break;


            case R.id.btExtract:

                if (kyc_type.equalsIgnoreCase("1")) {
                    if (!aadharImagepathFront.equals("") && !aadharImagepathBack.equalsIgnoreCase("")) {

                        showEditDialog();
                    } else {
                        Toast.makeText(getActivity(), "Please upload Both Images.", Toast.LENGTH_SHORT).show();
                    }


                } else if (kyc_type.equalsIgnoreCase("2")) {
                    if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {

                        try {
                            Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterImageBack);
                            ivVoterBackSelected.setVisibility(View.VISIBLE);
                            tvVoterBack.setVisibility(View.GONE);
                            if (!voterImagePathFront.equals("") && !voterImagePathBack.equalsIgnoreCase("")) {

                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    // getBankDetails(mContext,s.toString(),processId);
                                    ApiCallOCRVoter(voterImagePathFront, voterImagePathBack);
                                }
                                else {
                                    Constants.ShowNoInternet(getActivity());
                                }

                                // ApiCallSubmitOcr();
                                // ApiCallSubmitKYC();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please upload Both Images.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (!dlImageOathFront.equals("") && !dlImagePathBack.equalsIgnoreCase("")) {

                        try {
                          /*  Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterImageBack);
                            ivVoterBackSelected.setVisibility(View.VISIBLE);
                            tvVoterBack.setVisibility(View.GONE);*/
                            if (!dlImageOathFront.equalsIgnoreCase("") && !dlImagePathBack.equalsIgnoreCase("")) {
                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    // getBankDetails(mContext,s.toString(),processId);
                                    ApiCallGetDetailLicence(dlImageOathFront);
                                }
                                else {
                                    Constants.ShowNoInternet(getActivity());
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please upload Both Images.", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

        }


    }

    private void ApiCallSubmitOcr(String name, String fathername, String dob, String id, String detailsId, String filename, String fileUrl) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        JSONObject jsonObject = new JSONObject();
        if (kyc_type.equalsIgnoreCase("2")) {

            try {
                jsonObject.put(Constants.PARAM_VOTER_ID_DETAIL_ID, VoteridDetailId);
                jsonObject.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
                jsonObject.put(Constants.PARAM_VOTER_ID_NUMBER, etVoterId.getText().toString());
                jsonObject.put(Constants.PARAM_NAME, etvoterName.getText().toString());
                jsonObject.put(Constants.PARAM_FATHER_NAME, etVoterFatherName.getText().toString());
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

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), " Contact administartor immediately", Toast.LENGTH_LONG).show();
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
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(KYCVerificationFragment.this);
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
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_ADHAR_FRONT);


            }
            if (requestCode == CROPPED_IMAGE_ADHAR_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                aadharImagepathFront = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(aadharImagepathFront).into(ivAdharIamgeFront);
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

            }
            if (requestCode == IMAGE_AADHAR_BACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
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
                aadharImagepathBack = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(aadharImagepathBack).into(ivAdharImageBack);
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

            }


            if (requestCode == REQUEST_SCANNER && (data != null)) {
                processScannedData(data.getStringExtra(Constants.CONTENT));
            }
            if (requestCode == IMAGE_VOTER_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_FRONT);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathFront = RealPathUtil.getPath(getActivity(), imgURI);

                try {
                    Glide.with(getActivity()).load(voterImagePathFront).into(ivVoterImageFront);
                    ivVoterFrontSelected.setVisibility(View.VISIBLE);
                    tvVoterFront.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == IMAGE_VOTER_BACK) {

                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());

                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_VOTER_BACK);

            }
            if (requestCode == CROPPED_IMAGE_VOTER_BACK) {

                imgURI = Uri.parse(data.getStringExtra("uri"));
                voterImagePathBack = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(voterImagePathBack).into(ivVoterImageBack);
                    ivVoterBackSelected.setVisibility(View.VISIBLE);
                    tvVoterBack.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == IMAGE_DL_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getActivity());
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);
            }


            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                dlImageOathFront = RealPathUtil.getPath(getActivity(), imgURI);
                try {
                    Glide.with(getActivity()).load(dlImageOathFront).into(ivDlImageFront);
                    ivDlFrontSelected.setVisibility(View.VISIBLE);
                    tvDlFront.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (requestCode == IMAGE_DL_BACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                dlImagePathBack = ImagePicker.getBitmapPath(bitmap, getActivity());
                Glide.with(getActivity()).load(dlImagePathBack).into(ivDlImageBack);
                ivDlBackSelected.setVisibility(View.VISIBLE);
                tvDlBack.setVisibility(View.GONE);

            }

            //setButtonImage();
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
        hideKeyboardwithoutPopulateFragment();

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
                            filename = voterOCRGetDetaisResponse.getVoterIdDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getVoterIdDetail().getFileUrl();


                            DialogUtil.VoterDetail(getActivity(), voterOCRGetDetaisResponse.getVoterIdDetail().getName(), voterOCRGetDetaisResponse.getVoterIdDetail().getVoterIdNumber(), voterOCRGetDetaisResponse.getVoterIdDetail().getFatherName(), voterOCRGetDetaisResponse.getVoterIdDetail().getBirthDate(), new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {
                                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                        // getBankDetails(mContext,s.toString(),processId);
                                        ApiCallSubmitOcr(name, fathername, dob, id, voterDetailsId, filename, fileUrl);
                                        ApiCallSubmitKYC(name, fathername, dob, id);
                                    } else {
                                        Constants.ShowNoInternet(getActivity());
                                    }

                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                }

                            });


                        } else {


                            DialogUtil.VoterDetail(getActivity(), "", "", "", "", new DialogListner() {
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

                            });

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

    private void hideKeyboardwithoutPopulateFragment() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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
        hideKeyboardwithoutPopulateFragment();
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

                            name = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getName();
                            fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            dob = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate();
                            dlnumber = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber();
                            dlIdDetailId = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            filename = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileUrl();

                            DialogUtil.DrivingDetail(getActivity(), name, fathername, dob, dlnumber, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                        // getBankDetails(mContext,s.toString(),processId);
                                        ApiCallSubmitOcr(name, fathername, dob, id, dlIdDetailId, filename, fileUrl);
                                        ApiCallSubmitKYC(name, fathername, dob, id);
                                    }
                                    else {
                                        Constants.ShowNoInternet(getActivity());
                                    }




                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                }
                            });


                        } else {
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            DialogUtil.DrivingDetail(getActivity(), name, fathername, dob, dlnumber, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                    //ApiCallSubmitOcr(name,fathername,dob,id,dlIdDetailId,filename,fileUrl);

                                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                        ApiCallSubmitKYC(name, fathername, dob, id);
                                    }
                                    else {
                                        Constants.ShowNoInternet(getActivity());
                                    }



                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                }
                            });

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


                            File casted_image = new File(aadharImagepathFront);
                            if (casted_image.exists()) {
                                casted_image.delete();
                            }
                            File casted_image1 = new File(aadharImagepathBack);
                            if (casted_image1.exists()) {
                                casted_image1.delete();
                            }
                            deleteImages();

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
                            if (getaadhardetail.getResponseCode() == 411) {
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
        tvTitle.setText("KYC VERIFICATION");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commanFragmentCallWithoutBackStack(new PanVerificationFragment());
            }
        });

        dialog.setCancelable(false);

        dialog.show();

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


    private boolean validation(String type) {
        if (type.equalsIgnoreCase("2")) {
            if (TextUtils.isEmpty(etvoterName.getText().toString())) {
                etvoterName.requestFocus();
                etvoterName.setError("Provide Voter Name");

                //showMSG(false, "dProvide Email");
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
                // showMSG(false, "Provide Store name");
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


    private void ApiCallSubmitKYC(String name, String fathername, String dob, String id) {
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
            params.put(Constants.PARAM_VOTER_NAME, name);
            params.put(Constants.PARAM_VOTER_FATHER_NAME, fathername);
            params.put(Constants.PARAM_VOTER_DOB, dob);
            params.put(Constants.PARAM_VOTER_ID_NUM, id);
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);

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
            params.put(Constants.PARAM_DL_NAME, name);
            params.put(Constants.PARAM_DL_FATHER_NAME, fathername);
            params.put(Constants.PARAM_DL_DOB, dob);
            params.put(Constants.PARAM_DL_ID_NUM, id);
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);

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
            hideKeyboardwithoutPopulateFragment();
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
                            deleteImages();


                            DialogUtil.dismiss();
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
                                        //showAlertValidation(validation.getVoterCardFront());
                                    }
                                    if (validation.getVoterCardBack() != null && validation.getVoterCardBack().length() > 0) {
                                        //   showAlertValidation(validation.getVoterCardBack());
                                    }
                                    if (validation.getVoterDob() != null && validation.getVoterDob().length() > 0) {
                                        //  showAlertValidation(validation.getVoterDob());
                                        DialogUtil.etVoterDateofBirth.setError(validation.getVoterDob());
                                    }
                                    if (validation.getVoterName() != null && validation.getVoterName().length() > 0) {
                                        DialogUtil.etVotername.setError(validation.getVoterName());
                                    }
                                    if (validation.getVoterFatherName() != null && validation.getVoterFatherName().length() > 0) {
                                        // showAlertValidation(validation.getVoterFatherName());
                                        DialogUtil.etVoterFatherName.setError(validation.getFatherName());

                                    }
                                    if (validation.getVoterIdNum() != null && validation.getVoterIdNum().length() > 0) {


                                        // showAlertValidation(validation.getVoterIdNum());
                                        DialogUtil.etVoterIdNumber.setError(validation.getVoterIdNum());
                                    }
                                    if (validation.getDlCardFront() != null && validation.getDlCardFront().length() > 0) {
                                        /*DialogUtil.etDlNumber.setError(validation.getVoterIdNum());
                                        showAlertValidation(validation.getDlCardFront());*/

                                    }
                                    if (validation.getDlCardBack() != null && validation.getDlCardBack().length() > 0) {

                                        //  showAlertValidation(validation.getDlCardBack());


                                    }
                                    if (validation.getDlDob() != null && validation.getDlDob().length() > 0) {
                                        DialogUtil.etDlDob.setError(validation.getDlDob());
                                    }
                                    if (validation.getDlFatherName() != null && validation.getDlFatherName().length() > 0) {
                                        //  Toast.makeText(mcontext, validation.getDlFatherName(), Toast.LENGTH_SHORT).show();
                                        DialogUtil.etFathername.setError(validation.getDlFatherName());
                                    }
                                    if (validation.getDlNumber() != null && validation.getDlNumber().length() > 0) {
                                        DialogUtil.etDlNumber.setError(validation.getDlNumber());

                                    }
                                    if (validation.getDlName() != null && validation.getDlName().length() > 0) {
                                        DialogUtil.etDlName.setError(validation.getDlName());

                                    }
                                } else {
                                    //  showAlert2(getaadhardetail.getResponse());
                                }
                            }
                            Toast.makeText(mcontext, getaadhardetail.getResponse(), Toast.LENGTH_SHORT).show();

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


    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
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

        File casted_image = new File(aadharImagepathFront);
        if (casted_image.exists()) {
            casted_image.delete();
        }
        File casted_image1 = new File(aadharImagepathBack);
        if (casted_image1.exists()) {
            casted_image1.delete();
        }
        File casted_image6 = new File(imagecamera);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }
        File casted_image2 = new File(dlImageOathFront);
        if (casted_image2.exists()) {
            casted_image2.delete();
        }
        File casted_image3 = new File(dlImagePathBack);
        if (casted_image3.exists()) {
            casted_image3.delete();
        }
        File casted_image4 = new File(voterImagePathFront);
        if (casted_image4.exists()) {
            casted_image4.delete();
        }
        File casted_image5 = new File(voterImagePathBack);
        if (casted_image5.exists()) {
            casted_image5.delete();
        }
    }
}
