package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.GetTaskEndResponse.GetTaskEndResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.GetTaskDetailsResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.MyAdapter;
import com.canvascoders.opaper.adapters.MyAdapterforRecycler;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndTaskActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    EditText etDescription;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    ImageView ivCaptureImage;
    RecyclerView rvImageBillsMultiple;
    Button btSubmitReasonDetails;
    ViewPager mPager;
    MyAdapter myAdapter;
    private ArrayList<String> shopActImage = new ArrayList<>();
    private MyAdapterforRecycler myAdapterforRecycler;
    RequestPermissionHandler requestPermissionHandler;
    int taskid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_task);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        taskid = getIntent().getIntExtra(Constants.PARAM_TASK_ID, 0);

        requestPermissionHandler = new RequestPermissionHandler();
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etDescription = findViewById(R.id.etDescription);
        ivCaptureImage = findViewById(R.id.ivCaptureImage);
        ivCaptureImage.setOnClickListener(this);
        rvImageBillsMultiple = findViewById(R.id.rvImageBillsMultiple);
        btSubmitReasonDetails = findViewById(R.id.btSubmitReasonDetails);
        btSubmitReasonDetails.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);

        myAdapter = new MyAdapter(EndTaskActivity.this, shopActImage);
        myAdapterforRecycler = new MyAdapterforRecycler(EndTaskActivity.this, shopActImage);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EndTaskActivity.this, RecyclerView.HORIZONTAL, false);
        rvImageBillsMultiple.setLayoutManager(horizontalLayoutManager);

        rvImageBillsMultiple.setAdapter(myAdapterforRecycler);
        mPager.setAdapter(myAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmitReasonDetails:
                if (isValid()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallEndTask();
                    } else {
                        Constants.ShowNoInternet(EndTaskActivity.this);
                    }

                }
                break;


            case R.id.ivCaptureImage:
                capture_document_front_and_back_image(1);

                break;

        }
    }

    private void ApiCallEndTask() {

        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_DESCRIPTION,etDescription.getText().toString());


        MultipartBody.Part shop_act_part[] = new MultipartBody.Part[shopActImage.size()];

        for (int i = 0; i < shopActImage.size(); i++) {
            File imagefile1 = new File(shopActImage.get(i));
            Log.e("bill_proof", shopActImage.get(i));
            shop_act_part[i] = MultipartBody.Part.createFormData("proof[]", imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopActImage.get(i))), imagefile1));

        }

        ApiClient.getClient().create(ApiInterface.class).getSelfTaskEnd("Bearer "+sessionManager.getToken(),params,shop_act_part).enqueue(new Callback<GetTaskEndResponse>() {
            @Override
            public void onResponse(Call<GetTaskEndResponse> call, Response<GetTaskEndResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    GetTaskEndResponse getTaskDetailsResponse = response.body();
                    if(getTaskDetailsResponse.getResponseCode()==200){
                        Toast.makeText(EndTaskActivity.this, getTaskDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "stop");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                    else{
                        Toast.makeText(EndTaskActivity.this, getTaskDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetTaskEndResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EndTaskActivity.this, "#errorcode :- 2151"+getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etDescription.getText().toString())) {
            etDescription.setError("Enter remark");
            etDescription.requestFocus();
            return false;
        }
        if (shopActImage.size() == 0) {
            Toast.makeText(EndTaskActivity.this, "Please upload images of task", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        return true;
    }


    private void capture_document_front_and_back_image(int side_of_document) {
        requestPermissionHandler.requestPermission(EndTaskActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

               /* if (side_of_document == 1) {


                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);


                    if (switch_shopact.isChecked()) {
                        if (shopActImage.size() > 0) {
                            shopActImage.clear();
                            myAdapter = new MyAdapter(mcontext, shopActImage);
                            rvImageListBills.setAdapter(myAdapter);
                            //mPager.setAdapter(myAdapter);
                        }
                        ivShopImageSingle.setVisibility(View.VISIBLE);

                    }

                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;
                }*/


                if (side_of_document == 1) {

                    if (shopActImage.size() > 0) {
                        // shopActImage.clear();
                        myAdapter = new MyAdapter(EndTaskActivity.this, shopActImage);
                        mPager.setAdapter(myAdapter);
                        myAdapterforRecycler = new MyAdapterforRecycler(EndTaskActivity.this, shopActImage);
                        rvImageBillsMultiple.setAdapter(myAdapterforRecycler);
                    }



                    /*Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);*/

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, IMAGE_SHPO_ACT);

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EndTaskActivity.this);
                    startActivityForResult(chooseImageIntent, 143);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(EndTaskActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 143 && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {

                //Bitmap bitmap = ImagePicker.getImageFromResult(EndTaskActivity.this, resultCode, data);
              //  String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, EndTaskActivity.this);
                Uri uri = ImagePicker.getPickImageResultUri(EndTaskActivity.this, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                String  shoap_act_image_path = ImagePicker.getPathFromUri( EndTaskActivity.this,uri); // ImageUtils.getInstant().getImageUri(getActivity(), photo);


                shopActImage.add(shoap_act_image_path);


                Log.e("size", String.valueOf(shopActImage.size()));
                myAdapterforRecycler.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();


                //setButtonImage();
            }

        }
    }

}
