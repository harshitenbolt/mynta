package com.canvascoders.opaper.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.CommentListResponse.Datum;
import com.canvascoders.opaper.Beans.CommentResponse.CommentResponse;
import com.canvascoders.opaper.Beans.SupportDetailResponse.SupportDetailResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.CommentListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;

import com.canvascoders.opaper.utils.ImageUtils;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportDetailActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener {

    private String supportId, apiname;
    private EditText etComment;
    private static int IMAGE_SELCTION_CODE = 0;
    private SessionManager sessionManager;
    private ImageView ivBack, ivComment, ivUpload;
    JSONObject objectSearch = new JSONObject();
    private static final int GALINTENT = 1021;
    List<Datum> finalList = new ArrayList<>();
    RecyclerView rvCommentList;
    CommentListAdapter commentListAdapter;
    TextView tvTicketNo, tvPriority, tvDesc, tvStatus;
    private String image = "";
    Bitmap bitmap;
    private ImageView ivVendorImage;
    private RequestPermissionHandler requestPermissionHandler;
    private String apigetComments = Constants.GET_COMMENT_LIST;
    ProgressBar pbBar;
    private TextView tvNoComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            supportId = bundle.getString(Constants.KEY_SUPPORT_ID);
        }
        init();
        requestPermissionHandler = new RequestPermissionHandler();
    }

    private void init() {
        sessionManager = new SessionManager(this);
        tvDesc = findViewById(R.id.tvDescription);
        rvCommentList = findViewById(R.id.rvComments);
        rvCommentList.setNestedScrollingEnabled(false);
        tvTicketNo = findViewById(R.id.tvTicketNo);
        //tvPriority = findViewById(R.id.tvPriority);
        tvStatus = findViewById(R.id.tvStatus);
        ivBack = findViewById(R.id.iv_back);
        pbBar = findViewById(R.id.mProgress);
        ivVendorImage = findViewById(R.id.iv_vendor_image);
        etComment = findViewById(R.id.etComment);
        ivComment = findViewById(R.id.ivComment);
        ivComment.setOnClickListener(this);
        tvNoComment = findViewById(R.id.tvNocomment);
        ivUpload = findViewById(R.id.ivImageAdd);
        ivUpload.setOnClickListener(this);
        ivBack.setOnClickListener(this);


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetSupportDetails();
        } else {
            Constants.ShowNoInternet(this);
        }




        // Api Call for getting Comment List...
        try {
            objectSearch.put(Constants.PARAM_SUPPORT_ID, supportId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            new GetCommentList(objectSearch.toString(), apigetComments).execute();
        } else {
            Constants.ShowNoInternet(this);
        }

    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ivComment:

                if (isValid()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        ApiCallComment();
                    } else {
                        Constants.ShowNoInternet(this);
                    }
                }

                break;

            case R.id.ivImageAdd:
                UploadImage(view);
                break;
        }
    }


    private boolean isValid() {

        /*if (image.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
            return false;

        }*/

        if (TextUtils.isEmpty(etComment.getText().toString())) {
            Toast.makeText(this, "Please enter Comment", Toast.LENGTH_SHORT).show();

            return false;
        }


        return true;
    }


    // capture profile image
    private void UploadImage(final View v) {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                Intent gallaryIntent = ImageUtils.getGalleryIntenr(SupportDetailActivity.this);
                startActivityForResult(gallaryIntent, GALINTENT);
            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ApiCallComment() {
        Call call;
        showWait();
        ivComment.setVisibility(View.GONE);

        MultipartBody.Part attachment_part = null;

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_SUPPORT_ID, supportId);
        params.put(Constants.PARAM_COMMENT, etComment.getText().toString());


        if (image.equalsIgnoreCase("")) {
            call = ApiClient.getClient().create(ApiInterface.class).getCommentResponse("Bearer " + sessionManager.getToken(), params);

        } else {
            File imagefile1 = new File(image);
            attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(image)), imagefile1));
            call = ApiClient.getClient().create(ApiInterface.class).getCommentResponsewithImage("Bearer " + sessionManager.getToken(), params, attachment_part);

        }


        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    removeWait();
                    CommentResponse commentResponse = response.body();
                    if (commentResponse.getResponseCode() == 200) {
                        ivComment.setVisibility(View.VISIBLE);
                        ivUpload.setImageResource(R.drawable.ic_add);
                        image = "";
                        etComment.getText().clear();
                        new GetCommentList(objectSearch.toString(), apigetComments).execute();
                        //  Toast.makeText(SupportDetailActivity.this, commentResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SupportDetailActivity.this, commentResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }


                }
                else{
                    Toast.makeText(SupportDetailActivity.this, "#errorcode 2072 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                removeWait();
                Toast.makeText(SupportDetailActivity.this, "#errorcode 2072 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void ApiCallGetSupportDetails() {
        ApiClient.getClient().create(ApiInterface.class).getSupportDetails("Bearer " + sessionManager.getToken(), supportId).enqueue(new Callback<SupportDetailResponse>() {
            @Override
            public void onResponse(Call<SupportDetailResponse> call, Response<SupportDetailResponse> response) {
                if (response.isSuccessful()) {
                    SupportDetailResponse supportDetailResponse = response.body();
                    if (supportDetailResponse.getResponseCode() == 200) {
                        //  Toast.makeText(SupportDetailActivity.this, supportDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        tvDesc.setText(supportDetailResponse.getData().get(0).getDescreption());
                        tvStatus.setText(supportDetailResponse.getData().get(0).getStatus());
                        tvTicketNo.setText(supportDetailResponse.getData().get(0).getTicketNumber());
                        Glide.with(SupportDetailActivity.this).load(supportDetailResponse.getData().get(0).getAttachmentUrl()).placeholder(R.drawable.image_placeholder).into(ivVendorImage);

                        //    tvPriority.setText(supportDetailResponse.getData().get(0).getPriority());
                        //  Glide.with(SupportDetailActivity.this).load(supportDetailResponse.getData().get(0).getAttachment()).into(holder.ivAttachment);

                    } else {
                        Toast.makeText(SupportDetailActivity.this, supportDetailResponse.getResponse(), Toast.LENGTH_LONG).show();
                        //    Toast.makeText(SupportDetailActivity.this, supportDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SupportDetailActivity.this, "#errorcode 2070 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SupportDetailResponse> call, Throwable t) {
                Toast.makeText(SupportDetailActivity.this, "#errorcode 2070 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALINTENT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                image = ImagePicker.getBitmapPath(bitmap, this);

                Glide.with(this).load(image).into(ivUpload);
            }
        }
    }


    public void showWait() {
        runOnUiThread(() -> {
            if (pbBar != null)
                pbBar.setVisibility(View.VISIBLE);
        });
    }


    public void removeWait() {
        runOnUiThread(() -> {
            if (pbBar != null)
                pbBar.setVisibility(View.GONE);
        });
    }


    public class GetCommentList extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetCommentList(String jsonReq, String apiURL) {
            this.jsonReq = jsonReq;
            this.apiURL = apiURL;

            Mylogger.getInstance().Logit("apiURL: ", apiURL);
            Mylogger.getInstance().Logit("jsonReq: ", jsonReq);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            finalList.clear();
            /*progressDialog.show();
            progressDialog.setCancelable(false  );
            // if we are on 1st page of every new keyword it is mandatory to clear our list
            if (pageSearch == 1) {
                vendorLists.clear();
            }*/
        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes;
            try {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.MINUTES)
                        .readTimeout(1000, TimeUnit.MINUTES)
                        .writeTimeout(1000, TimeUnit.MINUTES)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonReq);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + apiURL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                okhttp3.Response responseLogin = client.newCall(requestLogin).execute();
                myRes = responseLogin.body().string();
                //  Mylogger.getInstance().Logit(TAG + "1", myRes);
                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
          /*  progressDialog.dismiss();
            Mylogger.getInstance().Logit(TAG, message);
          */
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        //     Toast.makeText(SupportDetailActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(SupportDetailActivity.this);
                        }
                    }
                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        if (!next_Url.equalsIgnoreCase("") && !next_Url.equalsIgnoreCase("null")) {
                            String[] separated = next_Url.split("api3/");
                            apiname = separated[1];

                        } else {
                            apiname = "";
                        }
                        //Log.e("URL", next_Url);
                        JSONArray result = jsonObject.getJSONArray("data");
                        ;
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject o = result.getJSONObject(i);

                            Datum vendorList = gson.fromJson(o.toString(), Datum.class);

                            Log.e("VENDOR", "" + vendorList.getAttachmentUrl());
                            Datum datum = new Datum();
                            datum = vendorList;

                            finalList.add(datum);
                        }
                        if (finalList.size() > 0) {
                            rvCommentList.setVisibility(View.VISIBLE);
                            tvNoComment.setVisibility(View.GONE);
                        } else {
                            tvNoComment.setVisibility(View.VISIBLE);
                            rvCommentList.setVisibility(View.GONE);
                        }


//                        rvCommentList.getRecycledViewPool().clear();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SupportDetailActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvCommentList.setLayoutManager(linearLayoutManager);
                        commentListAdapter = new CommentListAdapter(finalList, SupportDetailActivity.this, SupportDetailActivity.this);
                        rvCommentList.setAdapter(commentListAdapter);
                        // commentListAdapter.notifyDataSetChanged();

                    }
                    commentListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // vendorAdapter.notifyDataSetChanged();
        }

    }
}
