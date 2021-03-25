package com.canvascoders.opaper.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.AddNewTaskResponse.AddSelfTaskResponse;
import com.canvascoders.opaper.Beans.GetTasksTypeListing;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.Beans.VendorDetailResponse.VendorDetailResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.dc.DC;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditStoreInformationActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    Spinner spReasons;
    EditText etTaskDetails;
    TextView tvSubmit, tvTaskStartDate, tvTaskStartTime, tvTaskEndDate, tvTaskEndtime;
    ImageView ivAddTaskImage;
    EditText etStoreName, etEnboltId;
    ImageView ivSupport;
    Bitmap b, converted;
    Button btSelect, btCancel;
    ImageView imageView, ivHome;
    RelativeLayout rvMain, rvMainWithRect;
    FrameLayout flImage;
    ProgressDialog progressDialog;
    private ArrayList<String> taskTypeList = new ArrayList<>();
    String attachment = "";
    SessionManager sessionManager;
    ProgressDialog mProgressDialog;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    VendorList vendor;
    private Runnable input_finish_checker;
    private String TAG = "sdfjksdhbf";
    String shopImg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        init();
        addTaskType();
    }

    private void init() {
        sessionManager = new SessionManager(this);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spReasons = findViewById(R.id.spReasons);
        etTaskDetails = findViewById(R.id.etDescription);
        tvTaskStartDate = findViewById(R.id.tvPeakDate);
        tvTaskStartTime = findViewById(R.id.tvPeakHoursStart);
        tvTaskEndDate = findViewById(R.id.tvEndTaskDate);
        tvTaskEndtime = findViewById(R.id.tvEndTaskHour);
        ivAddTaskImage = findViewById(R.id.ivTaskImage);
        ivAddTaskImage.setOnClickListener(this);
        tvSubmit = findViewById(R.id.tvSubmnit);
        tvSubmit.setOnClickListener(this);
        etStoreName = findViewById(R.id.etStoreName);
        etEnboltId = findViewById(R.id.etEnboltId);

        input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    // TODO: do what you need here
                    // ............
                    // ............
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallGetDetailofVendor();
                    } else {
                        Constants.ShowNoInternet(AddNewTaskActivity.this);
                    }
                }
            }
        };

        etEnboltId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        ivSupport = findViewById(R.id.ivSupport);
        ivSupport.setOnClickListener(this);
        imageView = findViewById(R.id.ivImage);
        btSelect = findViewById(R.id.btSelect);
        rvMain = findViewById(R.id.rvMain);
        flImage = findViewById(R.id.flImage);
        rvMainWithRect = findViewById(R.id.rlWithMain);

        ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = 0x90000000;
                final Drawable drawable = new ColorDrawable(color);
                b = Screenshot.takescreenshotOfRootView(imageView);
                imageView.setImageBitmap(b);
                findViewById(R.id.rvCaptured).setVisibility(View.VISIBLE);
                rvMain.setVisibility(View.GONE);
                //ivSupport.setVisibility(View.GONE);
                flImage.setForeground(drawable);
                //  btn_next.setForeground(drawable);
               /* findViewById(R.id.btn_next).setVisibility(View.GONE);
                findViewById(R.id.scView).setVisibility(View.GONE);*/
            }
        });


        findViewById(R.id.btClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  final int color = 0xFFFFFF;
                final Drawable drawable = new ColorDrawable(color);
                imageView.setImageResource(android.R.color.transparent);*/
                //  ivSupport.setVisibility(View.GONE);
                findViewById(R.id.rvCaptured).setVisibility(View.GONE);
                findViewById(R.id.rvMain).setVisibility(View.VISIBLE);

                // rvMain.setForeground(drawable);
             /* //  findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
              //  rvMain.setForeground(drawable);
              //  btn_next.setForeground(drawable);*/
            }
        });
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*View v1 = rvMainWithRect.getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = v1.getDrawingCache();
                BitmapDrawable drawable=new BitmapDrawable(bitmap);*/

                final int color = 0xFFFFFF;
                final Drawable drawable = new ColorDrawable(color);
                flImage.setForeground(drawable);
                findViewById(R.id.llButton).setVisibility(View.GONE);
                findViewById(R.id.tverror).setVisibility(View.GONE);
                Bitmap bitmap = viewToBitmap(rvMainWithRect);
                ApiCallSendImageSupport(bitmap);
                converted = getResizedBitmap(bitmap, 400);

                findViewById(R.id.rvCaptured).setVisibility(View.GONE);
                findViewById(R.id.rvMain).setVisibility(View.VISIBLE);
                findViewById(R.id.llButton).setVisibility(View.VISIBLE);
                findViewById(R.id.tverror).setVisibility(View.VISIBLE);
            }
        });
        final DragRectView view = (DragRectView) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new DragRectView.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    // view.setForeground(drawable);

                }
            });
        }

    }

    private void ApiCallGetDetailofVendor() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, etEnboltId.getText().toString());
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).vendorDetailResponse("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<VendorDetailResponse>() {
            @Override
            public void onResponse(Call<VendorDetailResponse> call, Response<VendorDetailResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VendorDetailResponse vendorDetailResponse = response.body();
                    if (vendorDetailResponse.getData().size() > 0) {

                        vendor = vendorDetailResponse.getData().get(0);
                        etStoreName.setText(vendor.getStoreName());
                    } else {
                        Toast.makeText(AddNewTaskActivity.this, "Please select correct EID", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(AddNewTaskActivity.this, "#errorcode :- 2017 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<VendorDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddNewTaskActivity.this, "#errorcode :- 2017 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmnit:
                if (isValid()) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        AppSubmitData();
                    } else {
                        Constants.ShowNoInternet(AddNewTaskActivity.this);
                    }
                }
                break;
            case R.id.ivTaskImage:
                Intent chooseImageIntent = ImagePicker.getCameraIntent(AddNewTaskActivity.this);
                startActivityForResult(chooseImageIntent, 105);
                break;
        }

    }

    private void AppSubmitData() {
        progressDialog.show();
        MultipartBody.Part typedFile = null;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.KEY_PROCESS_ID, etEnboltId.getText().toString());
        map.put(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        map.put("task_type", spReasons.getSelectedItem().toString() + "");
        map.put(Constants.PARAM_DESCRIPTION, etTaskDetails.getText().toString());

        Call<AddSelfTaskResponse> call;
        if (shopImg.equalsIgnoreCase("")) {
            call = ApiClient.getClient().create(ApiInterface.class).addTaskWithoutImage("Bearer " + sessionManager.getToken(), map);
        } else {
            File imagefile = new File(shopImg);
            typedFile = MultipartBody.Part.createFormData("attachment_file", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));


            call = ApiClient.getClient().create(ApiInterface.class).addTaskWithImage("Bearer " + sessionManager.getToken(), map, typedFile);

        }
        call.enqueue(new Callback<AddSelfTaskResponse>() {
            @Override
            public void onResponse(Call<AddSelfTaskResponse> call, Response<AddSelfTaskResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    AddSelfTaskResponse addSelfTaskResponse = response.body();
                    if (addSelfTaskResponse.getResponseCode() == 200) {
                        Toast.makeText(AddNewTaskActivity.this, addSelfTaskResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddNewTaskActivity.this, addSelfTaskResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddNewTaskActivity.this, "#errorcode 2151 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddSelfTaskResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddNewTaskActivity.this, "#errorcode 2151 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etTaskDetails.getText().toString())) {
            etTaskDetails.setError("Please provide task description");
            etTaskDetails.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etEnboltId.getText().toString())) {
            etEnboltId.setError("Please provide Enbolt ID");
            etEnboltId.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etStoreName.getText().toString())) {
            etStoreName.setError("Please provide correct Store Name");
            etStoreName.requestFocus();
            return false;
        }

        return true;
    }


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private void ApiCallSendImageSupport(Bitmap bitmap) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        MultipartBody.Part attachment_part = null;


        //Log.e("Id_done", "" + priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitSupportImage("Bearer " + sessionManager.getToken(), attachment_part).enqueue(new Callback<SubmitImageResponse>() {
            @Override
            public void onResponse(Call<SubmitImageResponse> call, retrofit2.Response<SubmitImageResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(AddNewTaskActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    /*    Intent i = new Intent(TaskListActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());

                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);*/


                        Intent i = new Intent(AddNewTaskActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "tasklist");
                        startActivity(i);

                        // finish();
                    } else {
                        Toast.makeText(AddNewTaskActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddNewTaskActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddNewTaskActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void addTaskType() {
        // state is DC and DC is state
        taskTypeList.clear();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Map<String, String> user = new HashMap<>();
     /*   user.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.put(Constants.PARAM_PINCODE, pcode);*/
        ApiClient.getClient().create(ApiInterface.class).getTaskTypeListing("Bearer " + sessionManager.getToken()).enqueue(new Callback<GetTasksTypeListing>() {
            @Override
            public void onResponse(Call<GetTasksTypeListing> call, Response<GetTasksTypeListing> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetTasksTypeListing getUserDetails = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetails.getResponse());
                    if (getUserDetails.getResponseCode() == 200) {

                        /*for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).GetTasksTypeListing()) {
                                taskTypeList.add(dc.GetTasksTypeListing());
                            }
                            etStoreState.setText(getUserDetails.getData().get(i).getState());
                            etStoreCity.setText(getUserDetails.getData().get(i).getCity());
                        }*/
                        taskTypeList.addAll(getUserDetails.getData());

                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(AddNewTaskActivity.this, android.R.layout.simple_spinner_item, taskTypeList);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spReasons.setAdapter(spinnerArrayAdapter);
                        spReasons.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(AddNewTaskActivity.this);
                    } else {
                        Toast.makeText(AddNewTaskActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddNewTaskActivity.this, "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetTasksTypeListing> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddNewTaskActivity.this, "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                // Toast.makeText(AddNewTaskActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
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
                ((TextView) view).setTextSize(10);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.monteregular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 105) {

               /* Bitmap bitmap = ImagePicker.getImageFromResult(AddNewTaskActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                shopImg = ImagePicker.getBitmapPath(bitmap, AddNewTaskActivity.this); // ImageUtils.getInstant().getImageUri(EditStoreInformationActivity.this, photo);
*/
                Uri uri = ImagePicker.getPickImageResultUri(AddNewTaskActivity.this, data);
                //  Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                shopImg = ImagePicker.getPathFromUri( AddNewTaskActivity.this,uri); // ImageUtils.getInstant().getImageUri(getActivity(), photo);


                Glide.with(AddNewTaskActivity.this).load(shopImg).into(ivAddTaskImage);
                Log.e("imageowner", "back image" + shopImg);
                // ivStoreImageSelected.setVisibility(View.VISIBLE);
            }
        }
    }


}
