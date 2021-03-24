package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class AddGstImageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivSupport, imageView;
    Button ivSelect;
    FrameLayout flImage;
    Bitmap b, converted;
    RelativeLayout rvMain;
    RelativeLayout rvMainWithRect;
    ImageView ivGSTImage, ivCLickGSTSelected;
    TextView tvCLickGST;
    private RequestPermissionHandler requestPermissionHandler;
    private static final int IMAGE_PAN = 101;
    public static final int CROPPED_IMAGE = 5333;
    private Uri imgURI;
    Button btSubmitImg;
    String str_process_id = "", store_name = "";
    ProgressDialog mProgressDialog;
    SessionManager sessionManager;
    ImageView ivBack;
    String attachment = "";
    private String panImagepath = "", imagecamera = "", identityType = "", identityEmail = "", identityCallbackUrl = "", identityAccessToken = "", identityID = "", identityPatronId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gst_image);
        requestPermissionHandler = new RequestPermissionHandler();
        mProgressDialog = new ProgressDialog(this);
        str_process_id = getIntent().getStringExtra("data");
        // store_name = getIntent().getStringExtra("store_name");
        sessionManager = new SessionManager(this);

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

        ivGSTImage = findViewById(R.id.ivGStImage);
        ivCLickGSTSelected = findViewById(R.id.ivCLickGSTSelected);
        ivCLickGSTSelected.setOnClickListener(this);
        tvCLickGST = findViewById(R.id.tvClickGST);
        tvCLickGST.setOnClickListener(this);

        btSubmitImg = findViewById(R.id.btSubmitGST);
        btSubmitImg.setOnClickListener(this);
        ivSupport = findViewById(R.id.ivSupport);
        imageView = findViewById(R.id.ivImage);
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
                //converted = getResizedBitmap(bitmap, 400);

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


    private void capture_GST_image() {
        requestPermissionHandler.requestPermission(AddGstImageActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent chooseImageIntent = ImagePicker.getCameraIntent(AddGstImageActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_PAN);
               /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_PAN);*/
            }

            @Override
            public void onFailed() {
                Toast.makeText(AddGstImageActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClickGST:
                capture_GST_image();
                break;
            case R.id.ivCLickGSTSelected:
                capture_GST_image();
                break;
            case R.id.btSubmitGST:
                if (validation()) {
                    ApiCallAddGST();
                }
                break;
        }

    }

    private void ApiCallAddGST() {
        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        File imagefile = new File(panImagepath);
        typedFile = MultipartBody.Part.createFormData("gst_certificate_image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));

        Call<AddDelBoyResponse> call = ApiClient.getClient().create(ApiInterface.class).addGSTCertiImage("Bearer " + sessionManager.getToken(), params, typedFile);
        call.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(AddGstImageActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddGstImageActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddGstImageActivity.this, "#errorcode :- 2021 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddGstImageActivity.this, "#errorcode :- 2021 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }


    private boolean validation() {

        if (panImagepath.equalsIgnoreCase("")) {
            Toast.makeText(AddGstImageActivity.this, "Please select GST Image", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PAN) {

                //  Constants.hideKeyboardwithoutPopulate(getActivity());
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                Intent intent = new Intent(AddGstImageActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE);

            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                panImagepath = RealPathUtil.getPath(AddGstImageActivity.this, imgURI);
                try {
                    Glide.with(AddGstImageActivity.this).load(imgURI).placeholder(R.drawable.ic_add_img).into(ivGSTImage);
                    // isPanSelected = true;
                    ivCLickGSTSelected.setVisibility(View.VISIBLE);
                    tvCLickGST.setVisibility(View.GONE);

                    File casted_image6 = new File(imagecamera);
                    if (casted_image6.exists()) {
                        casted_image6.delete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private void ApiCallSendImageSupport(Bitmap bitmap) {
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        MultipartBody.Part attachment_part = null;


        //Log.e("Id_done", "" + priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitSupportImage("Bearer " + sessionManager.getToken(), attachment_part).enqueue(new Callback<SubmitImageResponse>() {
            @Override
            public void onResponse(Call<SubmitImageResponse> call, Response<SubmitImageResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(AddGstImageActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddGstImageActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "Add GST Image");
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.KEY_PROCESS_ID, str_process_id);
                        //i.putExtra(Constants.KEY_INVOICE_NUM, invoice_num);
                        i.putExtra(Constants.KEY_NAME, "");
                        // i.putExtra(Constants.KEY_INVOICE_ID, invoice_id);
                        startActivity(i);
                        // finish();
                    } else {
                        Toast.makeText(AddGstImageActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(AddGstImageActivity.this, "#errorcode :- 2038 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddGstImageActivity.this,"#errorcode :- 2038 "+ getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


            }
        });

    }


}
