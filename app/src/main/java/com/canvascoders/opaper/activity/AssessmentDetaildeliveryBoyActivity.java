package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.DetailsAssessDelBoyResponse.AssessmentTriedResult;
import com.canvascoders.opaper.Beans.DetailsAssessDelBoyResponse.DetailAssessmentDelBoyResponse;
import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.adapters.AssessmentListAdapter;
import com.canvascoders.opaper.adapters.DocumentListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessmentDetaildeliveryBoyActivity extends AppCompatActivity {


    Bitmap b, converted;
    RelativeLayout rvMainWithRect, rvMain;
    ImageView imageView, ivHome;
    Button ivSelect;
    String attachment = "", str_del_boy = "";
    ProgressDialog mProgress;
    private ImageView ivBack, ivSupport;
    FrameLayout flImage;
    AssessmentListAdapter documentListAdapter;
    SessionManager sessionManager;
    TextView tvName, tvMobile, tvStoreName, tvExampAttemtedTimes, tvStatus, tvNodata;
    RecyclerView rvTestList;
    List<AssessmentTriedResult> listExam = new ArrayList<>();
    Button btSendLink;
    String proccess_type = "", flag = "", url = "", resendLink = "";

    //   ImageView iv

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detaildelivery_boy);
        str_del_boy = String.valueOf(getIntent().getIntExtra("data", 0));
        flag = getIntent().getStringExtra("flag");
        if (flag.equalsIgnoreCase("1")) {
            proccess_type = "delivery_boy_id";
            url = "delivery-boy-assessment-detail";
            resendLink = "delivery-boy-assessment-resendlink";
        } else {
            proccess_type = "proccess_id";
            url = "vendor-assessment-detail";
            resendLink = "vendor-assessment-resendlink";
        }
        sessionManager = new SessionManager(this);
        mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");
        init();
        ApiCallGetData();
    }

    private void ApiCallGetData() {
        mProgress.show();
        Map<String, String> params = new HashMap<>();
        params.put(proccess_type, str_del_boy);

        Call<DetailAssessmentDelBoyResponse> call = ApiClient.getClient().create(ApiInterface.class).detailAssesmentDelBoyResponse(url, "Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DetailAssessmentDelBoyResponse>() {
            @Override
            public void onResponse(Call<DetailAssessmentDelBoyResponse> call, Response<DetailAssessmentDelBoyResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    DetailAssessmentDelBoyResponse detailAssessmentDelBoyResponse = response.body();
                    if (detailAssessmentDelBoyResponse.getResponseCode() == 200) {

                        tvName.setText(detailAssessmentDelBoyResponse.getData().getName());
                        tvMobile.setText(detailAssessmentDelBoyResponse.getData().getPhoneNumber());
                        tvStoreName.setText(detailAssessmentDelBoyResponse.getData().getStoreName());
                        tvExampAttemtedTimes.setText(detailAssessmentDelBoyResponse.getData().getAssessmentTried() + "");
                        ivHome.setPadding(0, 0, 0, 0);
                        //Glide.with(AssessmentDetaildeliveryBoyActivity.this).load(Constants.BaseImageURL + detailAssessmentDelBoyResponse.getData().getImage()).placeholder(R.drawable.image_placeholder).into(ivHome);

                        Glide.with(AssessmentDetaildeliveryBoyActivity.this).load(Constants.BaseImageURL + detailAssessmentDelBoyResponse.getData().getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHome) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(AssessmentDetaildeliveryBoyActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                // ivHome.setPadding(0, 0, 0, 0);
                                ivHome.setImageDrawable(circularBitmapDrawable);
                            }
                        });


                        if (detailAssessmentDelBoyResponse.getData().getAssessmentTried() >= 3) {
                            btSendLink.setVisibility(View.GONE);
                        }

                        if (detailAssessmentDelBoyResponse.getData().getAssessmentVerify() == 0 && detailAssessmentDelBoyResponse.getData().getAssessmentTried() == 0) {
                            tvStatus.setText("PENDING");
                            tvStatus.setTextColor(AssessmentDetaildeliveryBoyActivity.this.getResources().getColor(R.color.colorYellow));

                        } else {
                            if (detailAssessmentDelBoyResponse.getData().getAssessmentVerify() == 1) {
                                tvStatus.setText("PASS");
                                btSendLink.setVisibility(View.GONE);
                                tvStatus.setTextColor(AssessmentDetaildeliveryBoyActivity.this.getResources().getColor(R.color.colorPrimary));
                            } else {
                                tvStatus.setText("FAIL");
                                tvStatus.setTextColor(AssessmentDetaildeliveryBoyActivity.this.getResources().getColor(R.color.colorRed));
                            }
                        }


                        listExam.clear();

                        if (detailAssessmentDelBoyResponse.getData().getAssessmentTriedResult() != null && detailAssessmentDelBoyResponse.getData().getAssessmentTriedResult().size() > 0) {

                            listExam = detailAssessmentDelBoyResponse.getData().getAssessmentTriedResult();
                            documentListAdapter = new AssessmentListAdapter(listExam, AssessmentDetaildeliveryBoyActivity.this);
                            rvTestList.setAdapter(documentListAdapter);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AssessmentDetaildeliveryBoyActivity.this);
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            rvTestList.setLayoutManager(linearLayoutManager);
                            rvTestList.setVisibility(View.VISIBLE);
                            tvNodata.setVisibility(View.GONE);
                        } else {
                            rvTestList.setVisibility(View.GONE);
                            tvNodata.setVisibility(View.VISIBLE);

                        }

                    } else if (detailAssessmentDelBoyResponse.getResponseCode() == 401) {
                        sessionManager.logoutUser(AssessmentDetaildeliveryBoyActivity.this);
                    } else {
                        Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, detailAssessmentDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode :- 2016 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DetailAssessmentDelBoyResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode :- 2016 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void init() {


        btSendLink = findViewById(R.id.btSendLink);
        tvName = findViewById(R.id.tv_name);
        tvMobile = findViewById(R.id.tv_mobile);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvNodata = findViewById(R.id.tvNodata);
        tvExampAttemtedTimes = findViewById(R.id.tvNoofTimeAttempt);
        tvStatus = findViewById(R.id.tvStatus);
        rvTestList = findViewById(R.id.rvExamDetails);
        ivHome = findViewById(R.id.ivProfileImage);

        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    APiCallSendLink();
                } else {
                    Constants.ShowNoInternet(AssessmentDetaildeliveryBoyActivity.this);
                }
            }
        });


        // support System...

        imageView = findViewById(R.id.ivImage);
        ivSupport = findViewById(R.id.ivSupport);
        ivSelect = findViewById(R.id.ivSelect);
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
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
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
        ivSelect.setOnClickListener(new View.OnClickListener() {
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

    private void APiCallSendLink() {
        mProgress.show();
        Map<String, String> params = new HashMap<>();
        params.put(proccess_type, str_del_boy);

        Call<ResendOTPResponse> call = ApiClient.getClient().create(ApiInterface.class).sendDeliveryLink(resendLink, "Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, Response<ResendOTPResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    ResendOTPResponse resendOTPResponse = response.body();
                    if (resendOTPResponse.getResponseCode() == 200) {
                        Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resendOTPResponse.getResponseCode() == 401) {
                        sessionManager.logoutUser(AssessmentDetaildeliveryBoyActivity.this);
                    } else {
                        Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode 2065 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode 2065 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


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
        mProgress.setMessage("Please wait...");
        mProgress.show();

        MultipartBody.Part attachment_part = null;


        //Log.e("Id_done", "" + priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitSupportImage("Bearer " + sessionManager.getToken(), attachment_part).enqueue(new Callback<SubmitImageResponse>() {
            @Override
            public void onResponse(Call<SubmitImageResponse> call, Response<SubmitImageResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(VendorDetailActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());

                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);*/
                        Intent i = new Intent(AssessmentDetaildeliveryBoyActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "VendorProfile");
                        startActivity(i);


                        // finish();
                    } else {
                        Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(AssessmentDetaildeliveryBoyActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

}
