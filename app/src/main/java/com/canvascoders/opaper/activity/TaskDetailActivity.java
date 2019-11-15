package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SignedDocDetailResponse.SignedDocDetailResponse;
import com.canvascoders.opaper.Beans.StartTaskResponse.StartTaskResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.GetTaskDetailsResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

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
    TextView tvTitleName, tvDescription, tvStoreName;
    SupportMapFragment mapFragment;
    LatLng sydney;
    GoogleMap mMap;
    private Marker mPerth;
    ImageView ivBack;
    String timeduration = "";


    Button btStartTask;

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
        btStartTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeduration.equalsIgnoreCase("")) {
                    startApiCall();
                } else {
                    EndApiCall();
                }

            }
        });


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
                        mDefaultLatitude = Double.valueOf(getTaskDetailsResponse.getData().get(0).getProcessDetail().getLatitude());
                        mDefaultLongitude = Double.valueOf(getTaskDetailsResponse.getData().get(0).getProcessDetail().getLongitude());

                        mapFragment.getMapAsync(TaskDetailActivity.this);
                        mPerth.setPosition(new LatLng(mDefaultLatitude, mDefaultLongitude));

                        timeduration = getTaskDetailsResponse.getData().get(0).getTaskStart();
                        if (getTaskDetailsResponse.getData().get(0).getTaskStart().equalsIgnoreCase("")) {
                            btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_green);
                            btStartTask.setText("START TASK");
                        } else {
                            btStartTask.setBackgroundResource(R.drawable.rounded_bottom_corner_view_red);
                            btStartTask.setText("END TASK");
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
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mPerth = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mDefaultLatitude, mDefaultLongitude)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
