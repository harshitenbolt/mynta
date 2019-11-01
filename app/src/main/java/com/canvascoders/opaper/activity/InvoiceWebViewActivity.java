package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SendInvoiceEsignResponse.SendInvoiceLinkresponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class InvoiceWebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private AppCompatTextView btn_agree, btn_send_link;
    private int invoice_id = 0;
    private String invoice_type = "";
    ProgressDialog progressDialog;
    private String TAG = "InvoiceEsign";
    SessionManager sessionManager;
    int signed;
    private String mobile_no = "", storename = "";
    private String enbolt_id = "", invoice_num = "";
    private String uid = "";
    private String invoice_pdf = "";
    Button ivSelect;
    private String status = "", store_type = "";
    Bitmap b, converted;
    RelativeLayout rvMain;
    private RelativeLayout relative_buttom;
    FrameLayout flImage;
    ImageView ivSupport, imageView;
    RelativeLayout rvMainWithRect;
    String pending = "";
    JSONObject jsonObject = new JSONObject();
    private ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_web_view);
        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
*/

        initView();
    }

    private void initView() {
        btn_send_link = (AppCompatTextView) findViewById(R.id.btn_send_link);
        relative_buttom = findViewById(R.id.relative_buttom);
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        invoice_id = getIntent().getIntExtra(Constants.KEY_INVOICE_ID, 0);
        invoice_type = getIntent().getStringExtra(Constants.INVOICE_TYPE);
        status = getIntent().getStringExtra(Constants.INVOICE_NUMBER);
        store_type = getIntent().getStringExtra(Constants.PARAM_STORE_TYPE_CONFIG);
        pending = getIntent().getStringExtra("Pending");
        Log.e("invoice_num", status);
        signed = getIntent().getIntExtra(Constants.SIGNED, 0);
        storename = getIntent().getStringExtra(Constants.KEY_NAME);


        if (pending.equalsIgnoreCase("Pending")) {
            relative_buttom.setVisibility(View.VISIBLE);

        } else {
            relative_buttom.setVisibility(View.GONE);
        }

        mWebView = (WebView) findViewById(R.id.web_view_id);
        btn_agree = (AppCompatTextView) findViewById(R.id.btn_agree);

        btn_send_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    apicallSendLink();
                } else {
                    Constants.ShowNoInternet(InvoiceWebViewActivity.this);
                }


            }
        });

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
                converted = getResizedBitmap(bitmap, 400);
                Intent i = new Intent(InvoiceWebViewActivity.this, ReportIssueActivity.class);
                i.putExtra("BitmapImage", converted);
                i.putExtra(Constants.PARAM_SCREEN_NAME, "Invoice");
                i.putExtra(Constants.KEY_PROCESS_ID, enbolt_id);
                i.putExtra(Constants.KEY_INVOICE_NUM, invoice_num);
                i.putExtra(Constants.KEY_NAME, storename);
                i.putExtra(Constants.KEY_INVOICE_ID, invoice_id);

                startActivity(i);
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
            jsonObject.put(Constants.PARAMS_INVOICE_TYPE, getIntent().getStringExtra(Constants.PARAMS_INVOICE_TYPE));
            jsonObject.put(Constants.PARAM_INVOICE_ID, invoice_id);
            jsonObject.put(Constants.PARAM_STORE_TYPE, store_type);
            // jsonObject.put(Constants.PARAMS_INVOICE_TYPE, getIntent().getStringExtra(Constants.PARAMS_INVOICE_TYPE));
            Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
            Mylogger.getInstance().Logit(TAG, jsonObject.toString());

            new GetPDFfromServer(jsonObject.toString()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, s);
                Request requestLogin = null;
                if (signed == 0) {
                    requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-invoice")
                            .post(body)
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .build();
                } else {
                    requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-signed-invoice")
                            .post(body)
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .build();
                }


                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();
                Mylogger.getInstance().Logit(TAG + "1", myRes);


                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {

            Mylogger.getInstance().Logit(TAG, message);
            progressDialog.dismiss();
            try {

                if (message != null) {
                    JSONObject jsonObject = new JSONObject(message);
                    Integer responseCode = jsonObject.getInt("responseCode");
                    if (responseCode == 200) {


                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                        if (result.has("uid")) {
                            uid = result.getString("uid").toString();
                        }

                        enbolt_id = result.getString("enbolt_id").toString();
                        invoice_num = result.getString("invoice_num").toString();

                        mobile_no = result.getString("mobile_no").toString();
                        invoice_pdf = result.getString("invoice_pdf").toString();

                        mWebView.getSettings().setJavaScriptEnabled(true);
                        String pdf = invoice_pdf;
                        mWebView.loadUrl(pdf);
                        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                        startActivity(browserIntent);*/
                        mWebView.loadData(result.getString("invoice_html"), "text/html; charset=utf-8", "UTF-8");
                        if (status.equals("1")) {

                            btn_agree.setVisibility(View.GONE);
                        }
                    } else {

                        Toast.makeText(InvoiceWebViewActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        public void commanFragmentCallWithoutBackStack(Fragment fragment) {

            Fragment cFragment = fragment;


            if (cFragment != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, cFragment);
                fragmentTransaction.commit();

            }
        }

        private String showPdf(String url) {
            String googleDocsUrl = "http://docs.google.com/viewer?embedded=true&url=";
            return googleDocsUrl + Uri.encode(url);
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


    private void apicallSendLink() {
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            // params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_INVOICE_ID, String.valueOf(invoice_id));
            params.put(Constants.PARAMS_INVOICE_TYPE, invoice_type);
            params.put(Constants.PARAM_STORE_TYPE, store_type);

            progressDialog.setMessage("Please Wait ...");
            progressDialog.show();
            Call<SendInvoiceLinkresponse> call = ApiClient.getClient().create(ApiInterface.class).sendInvoice("Bearer " + sessionManager.getToken(), params);
            call.enqueue(new Callback<SendInvoiceLinkresponse>() {
                @Override
                public void onResponse(Call<SendInvoiceLinkresponse> call, retrofit2.Response<SendInvoiceLinkresponse> response) {

                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                    progressDialog.dismiss();
                    if (response != null) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            SendInvoiceLinkresponse sendInvoiceLinkresponse = response.body();
                            if (sendInvoiceLinkresponse.getResponseCode() == 200) {
                                Toast.makeText(InvoiceWebViewActivity.this, sendInvoiceLinkresponse.getResponse(), Toast.LENGTH_LONG).show();
                            } else if (sendInvoiceLinkresponse.getResponseCode() == 411) {
                                sessionManager.logoutUser(InvoiceWebViewActivity.this);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(InvoiceWebViewActivity.this, sendInvoiceLinkresponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SendInvoiceLinkresponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        } else {
            Constants.ShowNoInternet(InvoiceWebViewActivity.this);
        }
    }
}
