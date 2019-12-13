package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddendumSignPendingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTitle, tvMessage, tvSendLink, tvStatus, tvCounts, tvLastSigned;
    LinearLayout llBottom;
    ImageView ivBack;
    VendorList vendor;
    String proccess_id = "";
    ImageView ivSupport, imageView;
    String count = "", message = "", lastsigned = "", status = "";
    private SessionManager sessionManager;
    Bitmap b, converted;
    RelativeLayout rvMainWithRect;
    FrameLayout flImage;
    RelativeLayout rvMain;
    Button ivSelect;
    ProgressDialog progressDialog;
    String attachment="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_pending_screen);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        init();
    }

    private void init() {
        //vendor = (VendorList) getIntent().getSerializableExtra("data");
        proccess_id = getIntent().getStringExtra("data");
        count = getIntent().getStringExtra("count");
        message = getIntent().getStringExtra("message");
        lastsigned = getIntent().getStringExtra("last_signed");
        status = getIntent().getStringExtra("is_esign");
        sessionManager = new SessionManager(AddendumSignPendingActivity.this);
        tvTitle = findViewById(R.id.tv_title_Process);
        tvMessage = findViewById(R.id.tvMessage);
        ivSupport = findViewById(R.id.ivSupport);
        llBottom = findViewById(R.id.llBottomView);
        tvLastSigned = findViewById(R.id.tvLastSigned);
        tvCounts = findViewById(R.id.tvCounts);
        tvSendLink = findViewById(R.id.tvResendSign);
        tvSendLink.setOnClickListener(this);
        tvStatus = findViewById(R.id.tvStatus);
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(this);

        if (status.equalsIgnoreCase("")) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);

        }
        tvCounts.setText(count);

        if (status.equalsIgnoreCase("1")) {
            tvStatus.setText("Signed");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else if (status.equalsIgnoreCase("0")) {
            tvStatus.setText("Pending");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.colorYellow));
        }
        // tvStatus.setText(status);
        tvMessage.setText(message);
        tvLastSigned.setText(lastsigned);


        imageView = findViewById(R.id.ivImage);
        rvMain = findViewById(R.id.rvMain);
        flImage = findViewById(R.id.flImage);
        rvMainWithRect = findViewById(R.id.rlWithMain);

        ivSelect = findViewById(R.id.ivSelect);
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
               // converted = getResizedBitmap(bitmap, 400);

                findViewById(R.id.rvCaptured).setVisibility(View.GONE);
                findViewById(R.id.rvMain).setVisibility(View.VISIBLE);
                findViewById(R.id.llButton).setVisibility(View.VISIBLE);
                findViewById(R.id.tverror).setVisibility(View.VISIBLE);
            }
        });
        final DragRectView view = (DragRectView) findViewById(R.id.dragRect);


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
            public void onResponse(Call<SubmitImageResponse> call, Response<SubmitImageResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(AddendumSignPendingActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(AddendumSignPendingActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT,submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "Addendum");
                        startActivity(i);


                        // finish();
                    } else {
                        Toast.makeText(AddendumSignPendingActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }


    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvResendSign:
                apiCallResend();
                break;
            case R.id.iv_back_process:
                finish();
                break;


        }

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

    private void apiCallResend() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.KEY_PROCESS_ID, "" + proccess_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_ADUMDUM, "1");
        Call<ResendOTPResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).resendOTP("Bearer " + sessionManager.getToken(), params);
        callUpload.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, retrofit2.Response<ResendOTPResponse> response) {
                ResendOTPResponse resendOTPResponse = response.body();

                if (resendOTPResponse.getResponseCode() == 200) {
                    Toast.makeText(AddendumSignPendingActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
                } else if (resendOTPResponse.getResponseCode() == 411) {
                    sessionManager.logoutUser(AddendumSignPendingActivity.this);
                } else {
                    Toast.makeText(AddendumSignPendingActivity.this, resendOTPResponse.getResponse(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {

            }
        });
    }

}
