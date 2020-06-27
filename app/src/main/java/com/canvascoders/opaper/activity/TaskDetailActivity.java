package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.PauseTaskResponse.PauseTaskResponse;
import com.canvascoders.opaper.Beans.ResumeTaskListResponse.ResumeTaskListResponse;
import com.canvascoders.opaper.Beans.SendAgreementLinkResponse.GetAgreementLinkSend;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.SignedDocDetailResponse;
import com.canvascoders.opaper.Beans.StartTaskResponse.StartTaskResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.Datum;
import com.canvascoders.opaper.Beans.TaskDetailResponse.GetTaskDetailsResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.SubTaskList;
import com.canvascoders.opaper.Beans.TaskDetailResponse.SubTaskReason;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static com.canvascoders.opaper.utils.Constants.PARAM_TYPE;

public class TaskDetailActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static Double mDefaultLatitude = 12.972442;
    private static Double mDefaultLongitude = 77.580643;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    int taskid;
    GPSTracker gps;
    CountDownTimer newtimer;
    TextView tvTitleName, tvDescription, tvStoreName;
    SupportMapFragment mapFragment;
    LatLng sydney;
    String taskImage = "";
    GoogleMap mMap;
    private Marker mPerth;
    ImageView ivBack;
    boolean isResume = false;
    private static Dialog dialog;
    String timeduration = "";
    private static int IMAGE_SELCTED_IMG = 0;
    TextView tvAssignedBy, tvTimer, tvTitleDueDate, tvAssignedTime, tvAddress, tvMobile, tvTaskId, tvTaskCompletedDuration, tvTaskTime, tvDueDate, tvDuration;
    TextView tvLocate;
    private static final int DEFAULT_ZOOM = 15;
    Button btStartTask, btPauseTask;
    LinearLayout llComplete, llCall, llBottom;
    NestedScrollView nvMain;
    String lattitude = "", longitude = "";
    List<SubTaskList> subTaskLists = new ArrayList<>();
    ImageView ivClose, ivIssueImage;
    List<SubTaskReason> subtaskReasonList = new ArrayList<>();
    List<String> subTaskReasonNameList = new ArrayList<>();
    Datum datum;
    List<String> valuesList = new ArrayList<>();
    List<String> keyList = new ArrayList<>();
    Button btGotoScreen;
    String screenNumber = "";
    String docRequire = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        sessionManager = new SessionManager(this);


        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        taskid = getIntent().getIntExtra(Constants.KEY_ID, 0);
        init();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        //ApiCallgetDetails();
        tvTitleName = findViewById(R.id.tv_title_Process);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvDescription = findViewById(R.id.tvDescription);
        btStartTask = findViewById(R.id.btStartTask);
        btPauseTask = findViewById(R.id.btPauseTask);
        llComplete = findViewById(R.id.llComplete);
        llCall = findViewById(R.id.llCall);
        tvAssignedBy = findViewById(R.id.tvAssignedBy);
        tvAssignedTime = findViewById(R.id.tvAssignedTime);
        tvTitleDueDate = findViewById(R.id.tvTitleDueDate);
        tvTimer = findViewById(R.id.tvTimer);
        tvLocate = findViewById(R.id.tvLocate);
        tvAddress = findViewById(R.id.tvLocation);
        tvMobile = findViewById(R.id.tvMobileNo);

        tvTaskId = findViewById(R.id.tvTaskID);
        tvTaskCompletedDuration = findViewById(R.id.tvTaskCompletedDuration);
        tvTaskTime = findViewById(R.id.tvTaskTime);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvDuration = findViewById(R.id.tvDuration);
        btStartTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeduration.equalsIgnoreCase("")) {
                    startApiCall();
                } else {
                    showAlert();
                }

            }
        });
        llBottom = findViewById(R.id.llBottom);
        btPauseTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResume) {
                    ApiCallResumeTask();
                } else {
                    Intent i = new Intent(TaskDetailActivity.this, EditReasonPauseActivity.class);
                    i.putExtra(Constants.DATA, datum);
                    i.putExtra(Constants.PARAM_TASK_ID, taskid);
                    startActivityForResult(i, 1);
                }


            }
        });
        btGotoScreen = findViewById(R.id.btGoscreen);


        tvLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mDefaultLatitude, mDefaultLongitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvMobile.getText().toString()));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }

            }
        });


        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvMobile.getText().toString()));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });


        nvMain = findViewById(R.id.nvMain);

        btGotoScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screenNumber.equalsIgnoreCase("0")) {
                    Intent i = new Intent(TaskDetailActivity.this, DashboardActivity.class);
                    i.putExtra("data", "1");
                    i.putExtra("mobile_no", tvMobile.getText().toString());
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("1")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                    }
                    Intent i = new Intent(TaskDetailActivity.this, EditPanCardActivity.class);
                    i.putExtra("data", proccess_id);
                    startActivity(i);


                } else if (screenNumber.equalsIgnoreCase("2")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                    }
                    Intent i = new Intent(TaskDetailActivity.this, ChangeMobileActivity.class);
                    i.putExtra(Constants.KEY_VENDOR_MOBILE, proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("3")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                    }
                    Intent i = new Intent(TaskDetailActivity.this, VendorDetailActivity.class);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("4")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, StoreTypeListingActivity.class);
                    i.putExtra("data", proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("5")) {
                    Intent i = new Intent(TaskDetailActivity.this, GstListingActivity.class);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("6")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, AddDeliveryBoysActivity.class);
                    i.putExtra("data", proccess_id);
                    i.putExtra(Constants.KEY_EDIT_DETAIL, "0");
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("7")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, ResignAgreementActivity.class);
                    i.putExtra("data", proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("8")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, InvoiceDetailsActivity.class);
                    i.putExtra("data", proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("9")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, AddGstImageActivity.class);
                    i.putExtra("data", proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("10")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, TaskProccessDetailActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("11")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        APiCallSendGenerateLink(proccess_id);
                    } else {
                        Constants.ShowNoInternet(TaskDetailActivity.this);
                    }
                 /*   Intent i = new Intent(TaskDetailActivity.this, TaskProccessDetailActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    startActivity(i);
*/
                } else if (screenNumber.equalsIgnoreCase("12")) {
                    String proccess_id = "";
                    String delivery_boy_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                        if (keyList.get(i).equalsIgnoreCase("delivery_boy_id")) {
                            delivery_boy_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, EditDeliveryBoyFSActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    i.putExtra("delivery_boy_id", delivery_boy_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("13")) {
                    String proccess_id = "";
                    String delivery_boy_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                        if (keyList.get(i).equalsIgnoreCase("delivery_boy_id")) {
                            delivery_boy_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, EditDeliveryBoySSActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    i.putExtra("delivery_boy_id", delivery_boy_id);
                    startActivity(i);

                } else if (screenNumber.equalsIgnoreCase("14")) {
                    String proccess_id = "";
                    String delivery_boy_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }
                        if (keyList.get(i).equalsIgnoreCase("delivery_boy_id")) {
                            delivery_boy_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, EditDeliveryBoyTSActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    i.putExtra("delivery_boy_id", delivery_boy_id);
                    startActivity(i);
                } else if (screenNumber.equalsIgnoreCase("15")) {
                    String proccess_id = "";
                    for (int i = 0; i < keyList.size(); i++) {
                        if (keyList.get(i).equalsIgnoreCase("proccess_id")) {
                            proccess_id = valuesList.get(i);
                        }

                    }
                    Intent i = new Intent(TaskDetailActivity.this, EditKYCfromProfileActivity.class);
                    i.putExtra(Constants.KEY_PROCESS_ID, proccess_id);
                    startActivity(i);

                }


            }
        });


    }

    private void APiCallSendGenerateLink(String proccessId) {
        progressDialog.show();

        Map<String, String> params = new HashMap<>();

        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(Constants.PARAM_PROCESS_ID, proccessId);

        ApiClient.getClient().create(ApiInterface.class).linkGenerate("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetAgreementLinkSend>() {
            @Override
            public void onResponse(Call<GetAgreementLinkSend> call, Response<GetAgreementLinkSend> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetAgreementLinkSend getAgreementLinkSend = response.body();
                    if (getAgreementLinkSend.getResponseCode() == 200) {
                        Toast.makeText(TaskDetailActivity.this, getAgreementLinkSend.getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskDetailActivity.this, getAgreementLinkSend.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(TaskDetailActivity.this, "#errorcode 2188 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetAgreementLinkSend> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, "#errorcode 2188 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void ApiCallResumeTask() {

        gps = new GPSTracker(TaskDetailActivity.this);
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


        progressDialog.show();
        Map<String, String> params = new HashMap<>();

        params.put(Constants.PARAM_SUB_TASK_ID, String.valueOf(subTaskLists.get(0).getId()));
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);

        ApiClient.getClient().create(ApiInterface.class).resumeTask("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<ResumeTaskListResponse>() {
            @Override
            public void onResponse(Call<ResumeTaskListResponse> call, Response<ResumeTaskListResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResumeTaskListResponse resumeTaskListResponse = response.body();
                    if (resumeTaskListResponse.getResponseCode() == 200) {
                        ApiCallgetDetails();

                    } else {
                        Toast.makeText(TaskDetailActivity.this, resumeTaskListResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TaskDetailActivity.this, "#errorcode 2088 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResumeTaskListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, "#errorcode 2088 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


            }
        });

    }


    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("Are you sure you want complete this task?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //condition would be here...
                if (docRequire.equalsIgnoreCase("1")) {
                    Intent i = new Intent(TaskDetailActivity.this, EndTaskActivity.class);
                    i.putExtra(Constants.PARAM_TASK_ID, taskid);
                    startActivityForResult(i, 0);

                } else {
                    EndApiCall();
                }

                //
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }


    private void startApiCall() {

        gps = new GPSTracker(TaskDetailActivity.this);
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


        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(PARAM_TYPE, "start");
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);
        ApiClient.getClient().create(ApiInterface.class).startTask("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<StartTaskResponse>() {
            @Override
            public void onResponse(Call<StartTaskResponse> call, Response<StartTaskResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    StartTaskResponse startTaskResponse = response.body();
                    if (startTaskResponse.getResponseCode() == 200) {
                        btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_red);
                        btStartTask.setText("END TASK");
                        btPauseTask.setVisibility(View.VISIBLE);
                        ApiCallgetDetails();

                    } else {
                        Toast.makeText(TaskDetailActivity.this, startTaskResponse.getResponse(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(TaskDetailActivity.this, "#errorcode 2086 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StartTaskResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, "#errorcode 2086 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void EndApiCall() {

        gps = new GPSTracker(TaskDetailActivity.this);
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


        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(PARAM_TYPE, "end");
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);
        ApiClient.getClient().create(ApiInterface.class).startTask("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<StartTaskResponse>() {
            @Override
            public void onResponse(Call<StartTaskResponse> call, Response<StartTaskResponse> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    StartTaskResponse startTaskResponse = response.body();
                    if (startTaskResponse.getResponseCode() == 200) {
                        finish();
                    } else {
                        Toast.makeText(TaskDetailActivity.this, startTaskResponse.getResponse(), Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(TaskDetailActivity.this, "#errorcode 2086 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<StartTaskResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, "#errorcode 2086 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void ApiCallgetDetails() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));

        ApiClient.getClient().create(ApiInterface.class).getTaskDetailResponse("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetTaskDetailsResponse>() {
            @Override
            public void onResponse(Call<GetTaskDetailsResponse> call, Response<GetTaskDetailsResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    GetTaskDetailsResponse getTaskDetailsResponse = response.body();
                    if (getTaskDetailsResponse.getResponseCode() == 200) {
                        datum = getTaskDetailsResponse.getData().get(0);
                        List<Map<String, String>> list = new ArrayList<>();
                        list = datum.getRedirectScreenParams();


                        /*for (int i = 0; i < list.size(); i++) {


                        }*/

                        docRequire = datum.getIsRequiredProof();


                        Log.e("ScreeName", datum.getRedirectScreenNumber());
                        screenNumber = datum.getRedirectScreenNumber();
                        for (Map<String, String> map : list)
                            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                                String key = mapEntry.getKey();
                                String value = mapEntry.getValue();
                                valuesList.add(String.valueOf(value));
                                keyList.add(String.valueOf(key));
                                Log.e("valuesFound", valuesList.get(0));
                            }


                        tvStoreName.setText(getTaskDetailsResponse.getData().get(0).getBasicDetail().getStoreName());
                        tvTitleName.setText(getTaskDetailsResponse.getData().get(0).getType());
                        tvDescription.setText(getTaskDetailsResponse.getData().get(0).getDescription());
                        tvAssignedBy.setText(getTaskDetailsResponse.getData().get(0).getAssign_by_name());
                        tvAssignedTime.setText(getTaskDetailsResponse.getData().get(0).getAssignTime());
                        tvMobile.setText(getTaskDetailsResponse.getData().get(0).getProcessDetail().getMobileNo());
                        tvAddress.setText(getTaskDetailsResponse.getData().get(0).getStoreFullAddress());
                        subtaskReasonList = getTaskDetailsResponse.getData().get(0).getSubTaskReason();


                        for (int i = 0; i < subtaskReasonList.size(); i++) {
                            subTaskReasonNameList.add(subtaskReasonList.get(i).getName());
                        }

                        subTaskLists = getTaskDetailsResponse.getData().get(0).getSubTaskList();

                        if (!getTaskDetailsResponse.getData().get(0).getStatus().equalsIgnoreCase("1")) {
                            if (!getTaskDetailsResponse.getData().get(0).getStartTimer().equalsIgnoreCase("")) {
                                String currentString = getTaskDetailsResponse.getData().get(0).getStartTimer();
                                String[] separated = currentString.split(":");
                                long alarmtime = TimeUnit.MINUTES.toMillis(Long.parseLong(separated[1]));
                                Log.e("minutes:", separated[1]);
                                newtimer = new CountDownTimer(alarmtime, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(separated[0]));
                                        c.set(Calendar.MINUTE, Integer.parseInt(separated[1]));
                                        tvTimer.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                                    }

                                    public void onFinish() {

                                    }
                                };
                                newtimer.start();
                            }
                        }


                        mDefaultLatitude = Double.valueOf(getTaskDetailsResponse.getData().get(0).getProcessDetail().getLatitude());
                        mDefaultLongitude = Double.valueOf(getTaskDetailsResponse.getData().get(0).getProcessDetail().getLongitude());
                        tvTaskId.setText("#" + String.valueOf(getTaskDetailsResponse.getData().get(0).getId()));
                        mapFragment.getMapAsync(TaskDetailActivity.this);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mDefaultLatitude, mDefaultLongitude), DEFAULT_ZOOM));
                        mPerth.setPosition(new LatLng(mDefaultLatitude, mDefaultLongitude));

                        timeduration = getTaskDetailsResponse.getData().get(0).getTaskStart();
                        if (getTaskDetailsResponse.getData().get(0).getTaskStart().equalsIgnoreCase("")) {
                            btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_green);
                            btStartTask.setText("START TASK");
                            btGotoScreen.setVisibility(View.GONE);
                        } else {
                            btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_red);
                            btStartTask.setText("END TASK");
                            btPauseTask.setText("PAUSE TASK");
                            btStartTask.setVisibility(View.VISIBLE);
                            btPauseTask.setVisibility(View.VISIBLE);
                            btGotoScreen.setVisibility(View.VISIBLE);
                            isResume = false;
                        }


                        if (getTaskDetailsResponse.getData().get(0).getIsPause() == 1) {
                            newtimer.cancel();
                            tvTimer.setTextColor(getResources().getColor(R.color.colorYellow));
                            btStartTask.setVisibility(View.GONE);
                            btPauseTask.setText("RESUME TASK");
                            btGotoScreen.setVisibility(View.GONE);
                            isResume = true;
                        }


                        if (getTaskDetailsResponse.getData().get(0).getStatus().equalsIgnoreCase("1")) {

                            btStartTask.setVisibility(View.GONE);
                            btPauseTask.setVisibility(View.GONE);
                            llBottom.setVisibility(View.GONE);
                            btGotoScreen.setVisibility(View.GONE);
                            llComplete.setVisibility(View.VISIBLE);
                            tvDueDate.setText(getTaskDetailsResponse.getData().get(0).getDueDate());

                            /*if (!getTaskDetailsResponse.getData().get(0).getDueTime().equalsIgnoreCase("")) {
                                tvDuration.setText(getTaskDetailsResponse.getData().get(0).getDueTime());
                            }*/

                            if (!getTaskDetailsResponse.getData().get(0).getCompleteTime().equalsIgnoreCase("")) {
                                tvTaskCompletedDuration.setText(getTaskDetailsResponse.getData().get(0).getCompleteTime());
                            }
                            tvTaskTime.setText(getTaskDetailsResponse.getData().get(0).getCompleteDateTime());
                        } else {
                            if (!getTaskDetailsResponse.getData().get(0).getDueDate().equalsIgnoreCase("")) {
                                tvTitleDueDate.setVisibility(View.VISIBLE);
                                tvDueDate.setText(getTaskDetailsResponse.getData().get(0).getDueDate());
                            }
                           /* if (!getTaskDetailsResponse.getData().get(0).getDueTime().equalsIgnoreCase("")) {
                                tvDuration.setText(getTaskDetailsResponse.getData().get(0).getDueTime());
                            }*/
                        }


                    } else {
                        Toast.makeText(TaskDetailActivity.this, getTaskDetailsResponse.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(TaskDetailActivity.this, "#errorcode 2085 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetTaskDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, "#errorcode 2085 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(mDefaultLatitude, mDefaultLongitude);


        mPerth = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mDefaultLatitude, mDefaultLongitude)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    public void PauseDetails(Context mContext, String name, String id, String fathername, String birthdate, final DialogListner dialogInterface) {

        Button btSubmit;
        EditText etDescription;
        ImageView ivClose;
        Spinner spReasons;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguetask_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        etDescription = dialog.findViewById(R.id.etDescription);
        ivClose = dialog.findViewById(R.id.ivClose);
        spReasons = dialog.findViewById(R.id.spReasons);
        ivIssueImage = dialog.findViewById(R.id.ivIssueImage);
        ivIssueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;


            }
        });
        if (!taskImage.equalsIgnoreCase("")) {
            Glide.with(TaskDetailActivity.this).load(taskImage).into(ivIssueImage);
        }


        CustomAdapter<SubTaskReason> spinnerArrayAdapter = new CustomAdapter<SubTaskReason>(TaskDetailActivity.this, android.R.layout.simple_spinner_item, subtaskReasonList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReasons.setAdapter(spinnerArrayAdapter);
        spReasons.setSelection(0);


        btSubmit = dialog.findViewById(R.id.btSubmitReasonDetails);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogInterface.cancel();
                String id = "";
                if (isValid(v)) {
                    SubTaskReason subTaskReason = (SubTaskReason) spReasons.getSelectedItem();
                    Log.e("subTaskReson", subTaskReason.getName());
                    //    dialogInterface.onClickDetails(etVotername.getText().toString(), etVoterFatherName.getText().toString(), etVoterDateofBirth.getText().toString(), etVoterIdNumber.getText().toString());
                    dialogInterface.onClickDetails(taskImage, String.valueOf(subTaskReason.getId()), subTaskReason.getName(), etDescription.getText().toString());

                }
            }

            private boolean isValid(View v) {
                if (etDescription.getText().toString().equalsIgnoreCase("")) {
                    etDescription.setError("Provide Description");
                    etDescription.requestFocus();
                    return false;
                }

                return true;
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    class CustomAdapter<T> extends ArrayAdapter<SubTaskReason> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<SubTaskReason> objects) {

            super(context, textViewResourceId, objects);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SubTaskReason subTaskReason = getItem(position);

            TextView v = (TextView) super
                    .getView(position, convertView, parent);
            Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.monteregular);
            ((TextView) v).setTypeface(typeface);
            v.setTextSize(10);

            v.setText(subTaskReason.getName());
            return v;

        }

        public TextView getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
            SubTaskReason subTaskReason = getItem(position);
            TextView v = (TextView) super
                    .getView(position, convertView, parent);
            Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.montesemibold);
            ((TextView) v).setTypeface(typeface);
            v.setTextSize(15);
            v.setLines(2);
            v.setPadding(10, 10, 10, 10);
            v.setText(subTaskReason.getName());
            return v;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        ApiCallgetDetails();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equalsIgnoreCase("stop")) {
                    newtimer.cancel();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        // for end Task
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equalsIgnoreCase("stop")) {
                    finish();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }//onAct


}
