package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.PauseTaskResponse.PauseTaskResponse;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.SignedDocDetailResponse;
import com.canvascoders.opaper.Beans.StartTaskResponse.StartTaskResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.GetTaskDetailsResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
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

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.PARAM_TYPE;

public class TaskDetailActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static Double mDefaultLatitude = 12.972442;
    private static Double mDefaultLongitude = 77.580643;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    int taskid;
    CountDownTimer newtimer;
    TextView tvTitleName, tvDescription, tvStoreName;
    SupportMapFragment mapFragment;
    LatLng sydney;
    String taskImage;
    GoogleMap mMap;
    private Marker mPerth;
    ImageView ivBack;
    private static Dialog dialog;
    String timeduration = "";
    private static int IMAGE_SELCTED_IMG = 0;
    TextView tvAssignedBy, tvTimer, tvTitleDueDate, tvAssignedTime, tvAddress, tvMobile, tvTaskId, tvTaskCompletedDuration, tvTaskTime, tvDueDate, tvDuration;
    TextView tvLocate;
    private static final int DEFAULT_ZOOM = 15;
    Button btStartTask, btPauseTask;
    LinearLayout llComplete, llCall, llBottom;
    NestedScrollView nvMain;
    ImageView ivClose, ivIssueImage;

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
        ApiCallgetDetails();
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

                PauseDetails(TaskDetailActivity.this, "", "", "", "", new DialogListner() {
                    @Override
                    public void onClickPositive() {

                    }

                    @Override
                    public void onClickNegative() {

                    }

                    @Override
                    public void onClickDetails(String taskId, String sub_task_reason_id, String sub_task_reason_text, String description) {
                        //  ApiCallSubmitKYC(name, fathername, dob, id);
                        newtimer.cancel();
                        tvTimer.setTextColor(getResources().getColor(R.color.colorYellow));
                        btStartTask.setVisibility(View.GONE);
                        btPauseTask.setText("RESUME TASK");
                        dialog.dismiss();

                        APiCallPauseList(taskId, sub_task_reason_id, sub_task_reason_text, description);
                    }

                    @Override
                    public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                    }

                });


            }
        });


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
                startActivity(intent);
            }
        });


        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvMobile.getText().toString()));
                startActivity(intent);
            }
        });


        nvMain = findViewById(R.id.nvMain);


    }

    private void APiCallPauseList(String taskId, String sub_task_reason_id, String sub_task_reason_text, String description) {

        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(PARAM_TYPE, "start");

        Call<PauseTaskResponse> call = ApiClient.getClient().create(ApiInterface.class).pauseTask("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<PauseTaskResponse>() {
            @Override
            public void onResponse(Call<PauseTaskResponse> call, Response<PauseTaskResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    PauseTaskResponse pauseTaskResponse = response.body();
                    if (pauseTaskResponse.getResponseCode() == 200) {
                        DialogUtil.dismiss();
                    } else {
                        Toast.makeText(TaskDetailActivity.this, pauseTaskResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PauseTaskResponse> call, Throwable t) {
                progressDialog.dismiss();

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
                EndApiCall();
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

        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(PARAM_TYPE, "start");
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

                }

            }

            @Override
            public void onFailure(Call<StartTaskResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    private void EndApiCall() {

        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(PARAM_TYPE, "end");
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


                }

            }

            @Override
            public void onFailure(Call<StartTaskResponse> call, Throwable t) {
                progressDialog.dismiss();
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

                        tvStoreName.setText(getTaskDetailsResponse.getData().get(0).getBasicDetail().getStoreName());
                        tvTitleName.setText(getTaskDetailsResponse.getData().get(0).getType());
                        tvDescription.setText(getTaskDetailsResponse.getData().get(0).getDescription());
                        tvAssignedBy.setText(getTaskDetailsResponse.getData().get(0).getAssign_by_name());
                        tvAssignedTime.setText(getTaskDetailsResponse.getData().get(0).getAssignTime());
                        tvMobile.setText(getTaskDetailsResponse.getData().get(0).getProcessDetail().getMobileNo());
                        tvAddress.setText(getTaskDetailsResponse.getData().get(0).getStoreFullAddress());


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


                        //  tvTimer.setText(String.valueOf(System.currentTimeMillis()));

                        /*CountDownTimer timer=new CountDownTimer(300000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                                tvTimer.setText(String.format("%d:%d:%d",hours,minutes,seconds));
                            }
                            public void onFinish() {
                                tvTimer.setText("Time Up");
                            }
                        };
                        timer.start();*/


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
                        } else {
                            btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_red);
                            btStartTask.setText("END TASK");
                            btPauseTask.setVisibility(View.VISIBLE);
                        }


                        if (getTaskDetailsResponse.getData().get(0).getStatus().equalsIgnoreCase("1")) {


                            btStartTask.setVisibility(View.GONE);
                            btPauseTask.setVisibility(View.GONE);
                            llBottom.setVisibility(View.GONE);
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
                }

            }

            @Override
            public void onFailure(Call<GetTaskDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


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
        ivIssueImage = dialog.findViewById(R.id.ivIssueImage);
        ivIssueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;


                Intent chooseImageIntent = ImagePicker.getCameraIntent(TaskDetailActivity.this);
                startActivityForResult(chooseImageIntent, 200);

            }
        });

        btSubmit = dialog.findViewById(R.id.btSubmitReasonDetails);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogInterface.cancel();
                dialogInterface.onClickDetails("", "", "", "");

               /* if (isValid(v)) {
                    dialogInterface.onClickDetails(etVotername.getText().toString(), etVoterFatherName.getText().toString(), etVoterDateofBirth.getText().toString(), etVoterIdNumber.getText().toString());
                }*/
            }

            private boolean isValid(View v) {
                if (etDescription.getText().toString().equalsIgnoreCase("")) {
                    etDescription.setError("Provide name");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 200 && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {


                Bitmap bitmap = ImagePicker.getImageFromResult(TaskDetailActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                taskImage = ImagePicker.getBitmapPath(bitmap, TaskDetailActivity.this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Log.e("datadone",taskImage);
                Glide.with(TaskDetailActivity.this).load(taskImage).into(ivIssueImage);


            }


            //setButtonImage();
        }

    }


}
