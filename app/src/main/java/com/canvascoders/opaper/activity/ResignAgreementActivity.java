package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.AddendumDetail;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ApprovalRateDetail;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.BasicDetailRateDetail;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ResignAgreeDetailResponse;
import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.Beans.SubmitPopValue;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.adapters.AdddendumListAdapter;
import com.canvascoders.opaper.adapters.CurrentRateListAdapter;
import com.canvascoders.opaper.adapters.NewRateListAdapter;
import com.canvascoders.opaper.adapters.PopupRateListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Intent.ACTION_VIEW;

public class ResignAgreementActivity extends AppCompatActivity implements RecyclerViewClickListener {


    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private ImageView ivBack;
    WebView mWebView;
    // VendorList vendor;
    private ImageView ivSupport;
    Button ivSelect;
    RelativeLayout rvMainWithRect, rvMain;
    FrameLayout flImage;
    JSONObject jsonObject = new JSONObject();
    Button btChangeRate, btResign;
    RecyclerView rvAddendumList, rvRateList, rvPreviousRate;
    AdddendumListAdapter adddendumListAdapter;
    List<AddendumDetail> addendumDetailsList = new ArrayList<>();
    List<BasicDetailRateDetail> basicDetailRateDetails = new ArrayList<>();
    List<SubmitPopValue> popupValueList = new ArrayList<>();
    List<ApprovalRateDetail> approvalRateDetails = new ArrayList<>();
    View viewSeperate;
    TextView tvPending, tvApprove;
    String attachment = "";
    List<ApprovalRateDetail> approvedList = new ArrayList<>();
    List<ApprovalRateDetail> approvalRateUnderPending = new ArrayList<>();
    CurrentRateListAdapter currentRateListAdapter;
    NewRateListAdapter newRateListAdapter;
    PopupRateListAdapter popupRateListAdapter;
    private TextView tvAgreementName, tvTitleCurrentrate, tvNoAddedndum;
    private LinearLayout llAgreement;
    private static Dialog dialog;
    NestedScrollView nestedScrollView;
    ImageView imageView, ivHome;
    Bitmap b, converted;
    RecyclerView rvRateAgain;
    String proccess_id = "";
    LinearLayout llUpdated, llNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_agreement);
        init();
    }

    private void init() {
        // vendor = (VendorList) getIntent().getSerializableExtra("data");
        proccess_id = getIntent().getStringExtra("data");
        btChangeRate = findViewById(R.id.btChangeRate);
        nestedScrollView = findViewById(R.id.nestedMain);
        btResign = findViewById(R.id.btResign);
        progressDialog = new ProgressDialog(ResignAgreementActivity.this);
        progressDialog.setTitle("Please wait system is generating Agreement...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(ResignAgreementActivity.this);
        rvAddendumList = findViewById(R.id.rvAddedndum);
        rvRateList = findViewById(R.id.rvRateList);
        tvAgreementName = findViewById(R.id.tvAgreementName);
        llAgreement = findViewById(R.id.llAgreement);
        llUpdated = findViewById(R.id.llUpdated);
        llNotice = findViewById(R.id.llNotice);
        tvNoAddedndum = findViewById(R.id.tvNoAddedndum);
        rvRateAgain = findViewById(R.id.rvRateAgainProcess);
        tvPending = findViewById(R.id.tvPending);
        tvApprove = findViewById(R.id.tvApproved);
        viewSeperate = findViewById(R.id.viewSeperate);

        rvPreviousRate = findViewById(R.id.rvPreviousRate);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView = (WebView) findViewById(R.id.web_view_id);

        ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
        p.setVisibility(View.GONE);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    //do your task
                    ProgressBar p = (ProgressBar) findViewById(R.id.activity_thread_progress);
                    p.setVisibility(View.GONE);
                }
            }
        });


        try {
            jsonObject.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            jsonObject.put(Constants.KEY_PROCESS_ID, proccess_id);
            jsonObject.put(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
            jsonObject.put(Constants.PARAM_IS_DOC, "1");

////            Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
            //  Mylogger.getInstance().Logit(TAG, jsonObject.toString());

            new GetPDFfromServer(jsonObject.toString()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCallGetLists();


        btChangeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(ResignAgreementActivity.this, EditRateWhileResigAgreeActivity.class);
                i.putExtra("data", proccess_id);
                startActivityForResult(i, 1);

               /* if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(EditRateWhileResigAgreeActivity.this);
                }*/
            }
        });


        tvTitleCurrentrate = findViewById(R.id.tvRateTextCurrent);


        //support System...
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


    private void ApiCallGetLists() {

        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, proccess_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        Call<ResignAgreeDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).getDetailsResignAgreement("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<ResignAgreeDetailResponse>() {
            @Override
            public void onResponse(Call<ResignAgreeDetailResponse> call, retrofit2.Response<ResignAgreeDetailResponse> response) {
                ResignAgreeDetailResponse resignAgreeDetailResponse = response.body();
                try {
                    if (resignAgreeDetailResponse.getResponseCode() == 200) {
                        progressDialog.dismiss();


                        popupValueList = resignAgreeDetailResponse.getData().get(0).getSubmitPopValue();

                        //condition for agreement Screen after Approved New rates...
                        if (resignAgreeDetailResponse.getData().get(0).getShowAgreement().equalsIgnoreCase("1")) {

                            mWebView.setVisibility(View.VISIBLE);
                            nestedScrollView.setVisibility(View.GONE);
                            btChangeRate.setVisibility(View.VISIBLE);
                            btResign.setText("SIGN AGREEMENT");
                            btResign.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            if (resignAgreeDetailResponse.getData().get(0).getApprovalRateDetail().size() > 0) {
                                approvalRateDetails = resignAgreeDetailResponse.getData().get(0).getApprovalRateDetail();
                            }
                            btResign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showAlert("1");
                                }
                            });

                        } else {
                            mWebView.setVisibility(View.GONE);
                            nestedScrollView.setVisibility(View.VISIBLE);
                            btResign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showAlert("0");
                                }
                            });

                            tvAgreementName.setText(resignAgreeDetailResponse.getData().get(0).getResignAgreementTitle());
                            String pdf_url = Constants.BaseImageURL + resignAgreeDetailResponse.getData().get(0).getESignDocumentDetail().getEsignDoc();
                            llAgreement.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(ACTION_VIEW);
                                    browserIntent.setDataAndType(Uri.parse(pdf_url), "application/pdf");
                                    startActivity(browserIntent);
                                }
                            });


                            // condition for list showing mesage addedndum availale or not...
                            if (resignAgreeDetailResponse.getData().get(0).getAddendumDetail().size() > 0) {
                                rvAddendumList.setVisibility(View.VISIBLE);
                                tvNoAddedndum.setVisibility(View.GONE);
                                addendumDetailsList = resignAgreeDetailResponse.getData().get(0).getAddendumDetail();
                                adddendumListAdapter = new AdddendumListAdapter(ResignAgreementActivity.this, addendumDetailsList, ResignAgreementActivity.this);
                                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                rvAddendumList.setLayoutManager(horizontalLayoutManager1);
                                rvAddendumList.setAdapter(adddendumListAdapter);
                            } else {
                                rvAddendumList.setVisibility(View.GONE);
                                tvNoAddedndum.setVisibility(View.VISIBLE);
                            }


                            //condition for approval rates available or not ////

                            if (resignAgreeDetailResponse.getData().get(0).getApprovalRateDetail().size() > 0) {
                                btChangeRate.setVisibility(View.GONE);
                                btResign.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                                btResign.setText("SKIP TO RESIGN AGREEMENT");
                                llNotice.setVisibility(View.GONE);
                                llUpdated.setVisibility(View.VISIBLE);
                                tvTitleCurrentrate.setText("New Working Rate");


                                approvalRateDetails = resignAgreeDetailResponse.getData().get(0).getApprovalRateDetail();


                                for (int i = 0; i < approvalRateDetails.size(); i++) {
                                    if (approvalRateDetails.get(i).getStatus().equalsIgnoreCase("1")) {
                                        approvedList.add(approvalRateDetails.get(i));
                                    } else {
                                        approvalRateUnderPending.add(approvalRateDetails.get(i));
                                    }
                                }

                                newRateListAdapter = new NewRateListAdapter(ResignAgreementActivity.this, approvedList);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                rvRateList.setLayoutManager(horizontalLayoutManager);
                                rvRateList.setAdapter(newRateListAdapter);


                                if (approvalRateUnderPending.size() > 0 && approvedList.size() > 0) {
                                    tvPending.setVisibility(View.VISIBLE);
                                    tvApprove.setVisibility(View.VISIBLE);
                                    viewSeperate.setVisibility(View.VISIBLE);
                                    btChangeRate.setVisibility(View.VISIBLE);
                                    btResign.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    btResign.setText("RESIGN AGREEMENT");
                                    // llNotice.setVisibility(View.VISIBLE);
                                    tvTitleCurrentrate.setText("Current Working Rate");
                                    // llUpdated.setVisibility(View.GONE);
                                } else {
                                    rvRateList.setVisibility(View.GONE);
                                }

                                if (approvalRateUnderPending.size() > 0) {
                                    newRateListAdapter = new NewRateListAdapter(ResignAgreementActivity.this, approvalRateUnderPending);
                                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                    rvRateAgain.setLayoutManager(horizontalLayoutManager1);
                                    rvRateAgain.setAdapter(newRateListAdapter);

                                }

                                if (resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail().size() > 0) {

                                    basicDetailRateDetails = resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail();
                                    currentRateListAdapter = new CurrentRateListAdapter(ResignAgreementActivity.this, basicDetailRateDetails, "0");
                                    LinearLayoutManager horizontalLayoutManager3 = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                    rvPreviousRate.setLayoutManager(horizontalLayoutManager3);
                                    rvPreviousRate.setAdapter(currentRateListAdapter);

                                }

                               /* if (resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail().size() > 0) {
                                    basicDetailRateDetails = resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail();
                                    currentRateListAdapter = new CurrentRateListAdapter(ResignAgreementActivity.this, basicDetailRateDetails, "1");
                                    LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                    rvRateList.setLayoutManager(horizontalLayoutManager2);
                                    rvRateList.setAdapter(currentRateListAdapter);

                                }*/

                            } else {
                                btChangeRate.setVisibility(View.VISIBLE);
                                btResign.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                btResign.setText("RESIGN AGREEMENT");
                                llNotice.setVisibility(View.VISIBLE);
                                tvTitleCurrentrate.setText("Current Working Rate");
                                llUpdated.setVisibility(View.GONE);
                                if (resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail().size() > 0) {
                                    basicDetailRateDetails = resignAgreeDetailResponse.getData().get(0).getBasicDetailRateDetail();
                                    currentRateListAdapter = new CurrentRateListAdapter(ResignAgreementActivity.this, basicDetailRateDetails, "1");
                                    LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
                                    rvRateList.setLayoutManager(horizontalLayoutManager2);
                                    rvRateList.setAdapter(currentRateListAdapter);

                                }
                            }


                        }


                    } else if (resignAgreeDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(ResignAgreementActivity.this);
                    } else {
                        progressDialog.dismiss();
                        //  Toast.makeText(getActivity(), deliveryBoysList.getResponse(), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ResignAgreementActivity.this, "#errorcode 2075 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResignAgreeDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ResignAgreementActivity.this, "#errorcode 2075 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                   /* if (delivery_boys_list.isEmpty()) {
                    tv_note.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    tv_note.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                }*/
                Log.e("something", t.getMessage());
                t.getMessage();
            }
        });

    }

    @Override
    public void onClick(View view, int position) {


        Intent browserIntent = new Intent(ACTION_VIEW);
        browserIntent.setDataAndType(Uri.parse(Constants.BaseImageURL + addendumDetailsList.get(position).getAddendum()), "application/pdf");
        startActivity(browserIntent);


    }

    @Override
    public void onLongClick(View view, int position,String data) {

    }

    @Override
    public void SingleClick(String popup, int position) {

    }

    public class GetPDFfromServer extends AsyncTask<String, Void, String> {
        String myRes;
        String s = "";

        public GetPDFfromServer(String s) {
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                Log.e("Do", "In Background");
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, s);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + "resign-agreement-view")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
                //  Mylogger.getInstance().Logit(TAG + "1", myRes);
                Log.e("Do", "In Background Over");

                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                //   Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            Log.e("Do", "In Podt Execute");
            //  Mylogger.getInstance().Logit(TAG, message);
            progressDialog.dismiss();
            try {

                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {

                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                        // pdfUrlToSign = result.getString("pdf_url");
                        mWebView.loadData(result.getString("agreement_html"), "text/html; charset=utf-8", "UTF-8");
                        // btn_agree.setVisibility(View.GONE);
                    } else {

                        Toast.makeText(ResignAgreementActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("Mesage", "Is null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void ApiCallResendAgreement() {

        progressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).ResignAgreement("Bearer " + sessionManager.getToken(), String.valueOf(proccess_id)).enqueue(new Callback<ResignAgreementResponse>() {
            @Override
            public void onResponse(Call<ResignAgreementResponse> call, retrofit2.Response<ResignAgreementResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    ResignAgreementResponse resignAgreementResponse = response.body();
                    if (resignAgreementResponse.getResponseCode() == 200) {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "1");
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();

                        dialog.dismiss();
                        Toast.makeText(ResignAgreementActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(ResignAgreementActivity.this);
                    } else {
                        Toast.makeText(ResignAgreementActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResignAgreementActivity.this, "#errorcode :- 2015 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResignAgreementResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ResignAgreementActivity.this, "#errorcode :- 2015 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showAlert(String s) {
        Button btSubmit;
        TextView tvMessage, tvTitle;
        ImageView ivClose;
        RecyclerView rvRateList;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(ResignAgreementActivity.this);
        dialog = new Dialog(ResignAgreementActivity.this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_confirmation_rate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmitConfirm);
        rvRateList = dialog.findViewById(R.id.rvRateList);
        ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ResignAgreementActivity.this, LinearLayoutManager.VERTICAL, false);
        rvRateList.setLayoutManager(horizontalLayoutManager);


       /* if (s.equalsIgnoreCase("1")) {
            newRateListAdapter = new NewRateListAdapter(ResignAgreementActivity.this, approvalRateDetails);
            rvRateList.setAdapter(newRateListAdapter);
        } else {
            currentRateListAdapter = new CurrentRateListAdapter(ResignAgreementActivity.this, basicDetailRateDetails, "1");

        }*/


        popupRateListAdapter = new PopupRateListAdapter(ResignAgreementActivity.this, popupValueList, "1");
        rvRateList.setAdapter(popupRateListAdapter);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(ResignAgreementActivity.this);
                }

            }
        });

        dialog.setCancelable(false);

        dialog.show();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
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
                        Toast.makeText(ResignAgreementActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(ResignAgreementActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());

                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);*/


                        Intent i = new Intent(ResignAgreementActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "ResignAgreement");
                        startActivity(i);

                        // finish();
                    } else {
                        Toast.makeText(ResignAgreementActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ResignAgreementActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ResignAgreementActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
