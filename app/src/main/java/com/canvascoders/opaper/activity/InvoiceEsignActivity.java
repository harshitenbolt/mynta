package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SendInvoiceEsignResponse.SendInvoiceLinkresponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.utils.ToFormat;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.VenderListFragment;
import com.digio.in.esign2sdk.Digio;
import com.digio.in.esign2sdk.DigioConfig;
import com.digio.in.esign2sdk.DigioEnvironment;
import com.digio.in.esign2sdk.DigioServiceMode;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class InvoiceEsignActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    //Digio API call
    static String documentId = null;
    private static boolean isBiometrix = false;
    private static boolean isOtp = false;
    JSONObject jsonObject = new JSONObject();
    Digio digio;
    RelativeLayout rvMainWithRect;
    private WebView mWebView;
    private RelativeLayout relative_buttom;
    private SessionManager sessionManager;
    //    private Toolbar toolbar;
    private AppCompatTextView btn_agree,btn_send_link;
    private String TAG = "InvoiceEsign";
    private String directURL = "";
    //private String aggrementPDFUrl = "";
    private String uid = "";
    private String uname = "";
    private String invoice_pdf = "";
    private String enbolt_id = "",invoice_num="";
    private String invoice_id = "";
    private String invoice_type = "";
    private String mobile_no = "",storename="";
    private ProgressDialog progressDialog;
    private boolean isSigned = false;
    Toolbar toolbar;
    CardView cvMain;
    private String status = "";
    int signed ;
    Button ivSelect;
    FrameLayout flImage;
    Bitmap b,converted;
    RelativeLayout rvMain;
    private String status_value;
    String str_mobile_no;
    ImageView ivSupport,imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noc);

        digio = new Digio();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

      /*  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        progressDialog = new ProgressDialog(InvoiceEsignActivity.this);
        progressDialog.setTitle("Please Wait System is Generating INVOICE...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(InvoiceEsignActivity.this);
        btn_send_link = (AppCompatTextView)findViewById(R.id.btn_send_link);
        relative_buttom = findViewById(R.id.relative_buttom);
        str_mobile_no = sessionManager.getData(Constants.KEY_EMP_MOBILE);
        cvMain = findViewById(R.id.cvMain);
        invoice_id = getIntent().getStringExtra(Constants.KEY_INVOICE_ID);
        invoice_type = getIntent().getStringExtra(Constants.INVOICE_TYPE);
        status = getIntent().getStringExtra(Constants.INVOICE_NUMBER);
        Log.e("invoice_num",status);
        signed = getIntent().getIntExtra(Constants.SIGNED,0);
        storename = getIntent().getStringExtra(Constants.KEY_NAME);
        if(invoice_type.equalsIgnoreCase("gst")){
            relative_buttom.setVisibility(View.VISIBLE);
            btn_send_link.setVisibility(View.VISIBLE);
        }
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
                Intent i = new Intent(InvoiceEsignActivity.this,ReportIssueActivity.class);
                i.putExtra("BitmapImage", converted);
                i.putExtra(Constants.PARAM_SCREEN_NAME,"Invoice");
                i.putExtra(Constants.KEY_PROCESS_ID,enbolt_id);
                i.putExtra(Constants.KEY_INVOICE_NUM,invoice_num);
                i.putExtra(Constants.KEY_NAME,storename);
                i.putExtra(Constants.KEY_INVOICE_ID,invoice_id);

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

/*
        if(invoice_number != null){
            if(invoice_number.equalsIgnoreCase("1")){
                status= "esign_status";
                status_value="text";
            }
             if (invoice_number.equalsIgnoreCase("2")){
                 status= "gst_esign_status";
                 status_value="gst";
            }
            if (invoice_number.equalsIgnoreCase("3")){
                status= "debit_esign_status";
                status_value="debit";
            }*/


        getSupportActionBar().setTitle(invoice_type + " invoice");

        mWebView = (WebView) findViewById(R.id.web_view_id);
        btn_agree = (AppCompatTextView) findViewById(R.id.btn_agree);

        btn_send_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apicallSendLink();
            }
        });

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangeLangDialog();
            }
        });

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
            jsonObject.put(Constants.PARAM_INVOICE_ID, getIntent().getStringExtra(Constants.PARAM_INVOICE_ID));
            // jsonObject.put(Constants.PARAMS_INVOICE_TYPE, getIntent().getStringExtra(Constants.PARAMS_INVOICE_TYPE));
            Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "get-single-invoice");
            Mylogger.getInstance().Logit(TAG, jsonObject.toString());

            new GetPDFfromServer(jsonObject.toString()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void apicallSendLink() {
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
           // params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_INVOICE_ID, invoice_id);
            params.put(Constants.PARAMS_INVOICE_TYPE, invoice_type);

            progressDialog.setTitle("Please Wait ...");
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
                            if (sendInvoiceLinkresponse.getResponseCode()==200){
                                Toast.makeText(InvoiceEsignActivity.this,sendInvoiceLinkresponse.getResponse(),Toast.LENGTH_LONG).show();
                            }
                            else if(sendInvoiceLinkresponse.getResponseCode() ==411){
                                sessionManager.logoutUser(InvoiceEsignActivity.this);
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(InvoiceEsignActivity.this,sendInvoiceLinkresponse.getResponse(),Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    else{
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SendInvoiceLinkresponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }
        else {
            Constants.ShowNoInternet(InvoiceEsignActivity.this);
        }
    }

    public void showChangeLangDialog() {
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(InvoiceEsignActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.choose_option_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout btnbio, btnotp;

        btnbio = (LinearLayout) dialogView.findViewById(R.id.btn_bio);
        btnotp = (LinearLayout) dialogView.findViewById(R.id.btn_otp);

        AppCompatImageView btn_close = (AppCompatImageView) dialogView.findViewById(R.id.btn_close);
        AppCompatTextView btn_ok = (AppCompatTextView) dialogView.findViewById(R.id.btn_ok);

        final AppCompatImageView img_phone_otp = (AppCompatImageView) dialogView.findViewById(R.id.img_phone_otp);
        AppCompatTextView txt_phone_otp = (AppCompatTextView) dialogView.findViewById(R.id.txt_phone_otp);

        final AppCompatImageView img_bio = (AppCompatImageView) dialogView.findViewById(R.id.img_bio);
        AppCompatTextView txt_biomatric = (AppCompatTextView) dialogView.findViewById(R.id.txt_biomatric);

        final android.app.AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        btnbio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBiometrix = true;
                isOtp = false;
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_active));
                img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_normal));
            }
        });

        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtp = true;
                isBiometrix = false;
                img_phone_otp.setBackground(getResources().getDrawable(R.drawable.phone_active));
                img_bio.setBackground(getResources().getDrawable(R.drawable.bio_normal));
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                b.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (isBiometrix) {

                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                            digioConfig.setServiceMode(DigioServiceMode.FP);  //FP is fingerprint

                            try {
                                digio.init(InvoiceEsignActivity.this, digioConfig);
                                new getDocumentID("").execute(); // this will mnage document id from Digio server
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (isOtp) {
                            DigioConfig digioConfig = new DigioConfig();
                            digioConfig.setLogo(Constants.IMGEFORDIGIO); //Your company logo
                            digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                            digioConfig.setServiceMode(DigioServiceMode.OTP);  //FP is fingerprint

                            try {
                                digio.init(InvoiceEsignActivity.this, digioConfig);

                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    try {
                                        digio.init(InvoiceEsignActivity.this, digioConfig);
                                        //showVidDialogue();
                                        new getDocumentID("").execute(); // this will mnage document id from Digio server
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Constants.ShowNoInternet(InvoiceEsignActivity.this);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            }
        });
    }

    private void showVidDialogue() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(InvoiceEsignActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_enter_vid, null);
        dialogBuilder.setView(dialogView);
        final LinearLayout btnbio, btnotp;

        AppCompatImageView btn_close = dialogView.findViewById(R.id.btn_close);
        AppCompatTextView btn_ok = dialogView.findViewById(R.id.btn_ok);

        TextView tvGetVId = dialogView.findViewById(R.id.tvGetVId);
        final EditText etVid = dialogView.findViewById(R.id.etVid);

        final android.app.AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVid.getText().toString().length() >= 15) {
                    String vId = etVid.getText().toString();
                    b.dismiss();
                    new getDocumentID(vId).execute();
                }

            }
        });
        tvGetVId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.AADHAR_VID_URL));
                startActivity(i);
            }
        });

    }


    public void onSigningSuccess(String documentIds) {
        Toast.makeText(InvoiceEsignActivity.this, documentIds + " signed successfully", Toast.LENGTH_SHORT).show();
        isSigned = true;
        documentId = documentIds;
        storePDFOnServer(documentId);

    }

    public void onSigningFailure(String documentId, int code, String response) {
        Toast.makeText(InvoiceEsignActivity.this, response, Toast.LENGTH_SHORT).show();

    }

    /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void storePDFOnServer(String sss) {

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_INVOICE_ID, invoice_id);
            params.put(Constants.PARAMS_INVOICE_TYPE, invoice_type);
            params.put(Constants.PARAM_ENBOLT_ID, enbolt_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_ESIGN_URL, sss);


            Call<JsonObject> callUpload = ApiClient.getClient().create(ApiInterface.class).Storeinvoice("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                    Mylogger.getInstance().Logit(TAG, String.valueOf(response));

                    if (response != null) {

                        if (response.isSuccessful()) {
                            JsonObject object = response.body();
                            if (response.code() == 200) {
                                storename = Constants.billList.getStore_name();
                                String msg = "Store name: " + Constants.billList.getStore_name() +
                                        " , Invoice No: " + getInvoiceWithFormate(Constants.billList.getVendor_id(), invoice_id) +
                                        " , Invoice signed on " + Constants.billList.getUpdated_at() + " successfully.";
                                showAlert();//SendMSG(mobile_no, msg);

                            } else {
                                Toast.makeText(InvoiceEsignActivity.this, "INVOICE E-sign could not complete,retry again", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });
        } else {
            Constants.ShowNoInternet(InvoiceEsignActivity.this);
        }

    }

    private String getInvoiceWithFormate(String param, String invoice) {
        Calendar calendar = new GregorianCalendar();
        int thisMonth = calendar.get(Calendar.MONTH) + 1;
        int thisYear = calendar.get(Calendar.YEAR) - 2000;
        String invoiceNumber = param + "/" + ToFormat.getInstance().setFormat2(thisMonth + "") + "" + thisYear + "/" + ToFormat.getInstance().setFormat(invoice);
        return invoiceNumber;
    }

    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvoiceEsignActivity.this);
        alertDialog.setTitle("Vendor E-signing using Aadhaar.");
        alertDialog.setMessage("INVOICE Document has been signed sucessfully.");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }

    /* @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
         // Handle navigation view item clicks here.
         int id = item.getItemId();
         if (id == R.id.menu_profile) {

             commanFragmentCallWithoutBackStack(new ProfileFragment());

         }
         if (id == R.id.nav_1) {
             Intent i = new Intent(InvoiceEsignActivity.this, DashboardActivity.class);
             startActivity(i);
             finish();
         }
         if (id == R.id.nav_2) {

             commanFragmentCallWithoutBackStack(new MobileFragment());

         }
         if (id == R.id.nav_3) {

             commanFragmentCallWithoutBackStack(new InvoiceMainFragment());

         }
         if (id == R.id.nav_gst) {
             commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());

         }
         if (id == R.id.nav_debit) {
             commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());

         }
         if (id == R.id.nav_v) {

             commanFragmentCallWithoutBackStack(new VenderListFragment());

         }
         if (id == R.id.nav_4) {
             commanFragmentCallWithoutBackStack(new NotificationFragment());

         }


         if (id == R.id.nav_5) {

             commanFragmentCallWithoutBackStack(new ReportFragment());


         }
         if (id == R.id.nav_logout) {

             SessionManager sessionManager = new SessionManager(InvoiceEsignActivity.this);
             sessionManager.logoutUser(InvoiceEsignActivity.this);
         }

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);
         return true;

     }*/
    // Calling of DIGIO for DOCS Signing
    public class getDocumentID extends AsyncTask<String, Void, String> {
        String myRes = null;

        String vId = "";

        public getDocumentID(String id) {
            this.vId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Uploading Document...");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String[] params) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000, TimeUnit.MINUTES)
                    .readTimeout(1000, TimeUnit.MINUTES)
                    .writeTimeout(1000, TimeUnit.MINUTES)
                    .build();

            Request request = new Request.Builder()
                    .url(invoice_pdf)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();


                byte[] data = response.body().bytes();
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);

                Mylogger.getInstance().Logit(TAG, base64);

                MediaType mediaType = MediaType.parse("application/json");

                JSONObject param = new JSONObject();
                JSONObject identifier = new JSONObject();
                JSONArray signers = new JSONArray();

                identifier.put(Constants.PARAM_IDENTIFIRE, mobile_no);
                identifier.put(Constants.PARAM_AADHAR_ID, uid);
                if (vId.length() > 0)
                    identifier.put(Constants.PARAM_VID, vId);
                signers.put(identifier);

                param.put(Constants.PARAM_FILE_NAME, "invoice.pdf");
                param.put(Constants.PARAM_COMMENT, "Please sign");
                param.put(Constants.PARAM_SIGNERS, signers);
                param.put(Constants.PARAM_FILE_DATA, base64);
                param.put(Constants.PARAM_EXPIRE_IN_DAY, 7);
                param.put(Constants.PARAM_NOTIFY_SIGNERS, true);


                Mylogger.getInstance().Logit(TAG, param.toString());
                RequestBody body = RequestBody.create(mediaType, param.toString());

                String ccid = Constants.DIGIO_CLIENT_ID + ":" + Constants.DIGIO_CLIENT_SECRET;

                byte[] cid = ccid.getBytes("UTF-8");
                String base642 = Base64.encodeToString(cid, Base64.NO_WRAP);

                Mylogger.getInstance().Logit(TAG, base642);

                Request requestLogin = new Request.Builder()
                        .url(Constants.DIGIO_BASE_URL + "v2/client/document/uploadpdf")
                        .post(body)
                        .addHeader("accept-language", "en-US,en;q=0.8")
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", "Basic " + base642)
                        .addHeader("accept", "*/*")
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();


                String tmp = responseLogin.body().string();
                myRes = tmp;
                Mylogger.getInstance().Logit(TAG, tmp);

                return myRes;

            } catch (Exception e) {
                e.printStackTrace();
                Mylogger.getInstance().Logit(TAG, e.getMessage());
            }


            return myRes;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            if (message != null) {
                Mylogger.getInstance().Logit(TAG, message);
                try {
                    Toast.makeText(InvoiceEsignActivity.this, mobile_no + "", Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(message);
                    documentId = jsonObject.getString("id");
                    //  sessionFP.startDigioEsign(documentId, mobile_no);
                    try {
                        digio.esign(documentId, mobile_no);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(InvoiceEsignActivity.this, "Timeout", Toast.LENGTH_LONG).show();
            }

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
                if(signed == 0){
                   requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-invoice")
                            .post(body)
                           .addHeader("Authorization","Bearer "+sessionManager.getToken())
                            .build();
                }
                else{
                    requestLogin = new Request.Builder()
                            .url(Constants.BaseURL + "get-single-signed-invoice")
                            .post(body)
                            .addHeader("Authorization","Bearer "+sessionManager.getToken())
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
                        mWebView.loadUrl( pdf);
                        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                        startActivity(browserIntent);*/
                        mWebView.loadData(result.getString("invoice_html"), "text/html; charset=utf-8", "UTF-8");
                        if (status.equals("1")) {

                            btn_agree.setVisibility(View.GONE);
                        }
                    } else {

                        Toast.makeText(InvoiceEsignActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
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
            return googleDocsUrl+Uri.encode(url);
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

        float bitmapRatio = (float)width / (float) height;
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
    protected void onResume() {
        super.onResume();
        final int color = 0x90000000;
        final Drawable drawable = new ColorDrawable(color);
        flImage.setForeground(drawable);
        findViewById(R.id.llButton).setVisibility(View.VISIBLE);
        findViewById(R.id.tverror).setVisibility(View.VISIBLE);
    }
}
