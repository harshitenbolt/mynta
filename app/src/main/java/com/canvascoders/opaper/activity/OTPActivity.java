package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.Beans.verifymobile.GetMobileResponse;
import com.canvascoders.opaper.OtpView.PinView;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;

import com.canvascoders.opaper.fragment.DeliveryBoyFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.fragment.SupportFragment;
import com.canvascoders.opaper.fragment.VendorInProgressList;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.fragment.AadharVerificationFragment;
import com.canvascoders.opaper.fragment.ChequeUploadFragment;
import com.canvascoders.opaper.fragment.DocUploadFragment;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MapsCurrentPlaceFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.PanVerificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.RateFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.TaskCompletedFragment;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TextWatcher {


    private Button btn_next;
    private SessionManager sessionManager;
    private String TAG = "OTPActivity";
    private String otp, mobile;
    private TextView txt_mobile, tvChangeMobile;
    private ProgressDialog mProgressDialog;
    private TextView tv_resent_otp, tvTime;
    private Boolean resend = false;
    private int countDown = 30;
    static TextView tv_title;
    static String screenFinal = "Onboard Vendor";
    private Toolbar toolbar;
    private String processID;
    Bitmap b, converted;
    private PinView pvOTP;
    TextView tvHeaderName, tvHeaderEmail;
    private String lattitude = "", longitude = "";
    private int chqCount = 0;
    DrawerLayout drawer;
    Button ivSelect;
    ImageView ivSupport, imageView;
    LinearLayout llNavonBoardNewVendor, llNavLiveVendor, llNavInProgressVendor, llNavInvoice, llNavGSTInvoice, llNavDebitInvoice, llNavReport, llNavLogut, llNavSupports;

    NavigationView navigationView;
    String status_page;
    View v, vHeader;
    RelativeLayout rvMainWithRect;
    FrameLayout flImage;
    RelativeLayout rvMain;


    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_otp);
        sessionManager = new SessionManager(OTPActivity.this);
        otp = getIntent().getStringExtra("otp");
        mobile = getIntent().getStringExtra(Constants.KEY_EMP_MOBILE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        initView();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(OTPActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.iconnavigation);
        toggle.syncState();

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.hamburger, null);
                toolbar.setNavigationIcon(d);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btVerify:
                if (validate(v)) {
                    String userOTp = pvOTP.getText().toString();
                    if (userOTp.equals(otp)) {
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            GPSTracker gps = new GPSTracker(OTPActivity.this);
                            if (gps.canGetLocation()) {
                                Double lat = gps.getLatitude();
                                Double lng = gps.getLongitude();
                                lattitude = String.valueOf(gps.getLatitude());
                                longitude = String.valueOf(gps.getLongitude());
                                Log.e("Lattitude", lattitude);
                                Log.e("Longitude", longitude);


                            } else {

                                gps.showSettingsAlert();
                            }
                            getMobileDetails(v);
                        } else {
                            Constants.ShowNoInternet(this);
                        }
                    } else {

                        showMSG(false, "OTP not matched");
                    }
                }

                break;

            case R.id.tvChangeMobile: {
                commanFragmentCallWithoutBackStack2(new MobileFragment(), mobile);
            }
            break;

            case R.id.llNavOnboardNewVendor:
                tv_title.setText("Mobile Verification");

                commanFragmentCallWithoutBackStack(new MobileFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavLiveVendor:
                tv_title.setText("Live Vendors");
                drawer.closeDrawer(GravityCompat.START);
                commanFragmentCallWithoutBackStack(new VendorOnboardedList());

                break;
            case R.id.llNavInProgressVendor:
                tv_title.setText("InProgress Vendors");
                commanFragmentCallWithoutBackStack(new VendorInProgressList());
                drawer.closeDrawer(GravityCompat.START);

                break;
            case R.id.llNavInvoice:

                commanFragmentCallWithoutBackStack(new InvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavGSTnote:
                commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavDebitNote:
                commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavReports:
                commanFragmentCallWithoutBackStack(new ReportFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavLogout:
                SessionManager sessionManager = new SessionManager(OTPActivity.this);
                sessionManager.logoutUser(OTPActivity.this);
                break;
            case R.id.llNavSuppors:
                commanFragmentCallWithoutBackStack(new SupportFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;


        }
    }

    private void getMobileDetails(final View v) {
        hideKeyboardwithoutPopulate(this);
        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_MOBILE_NO, mobile);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_LATITUDE, lattitude);
        user.addProperty(Constants.PARAM_LONGITUDE, longitude);

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).verifyMobile("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetMobileResponse>() {
            @Override
            public void onResponse(Call<GetMobileResponse> call, Response<GetMobileResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetMobileResponse mobileResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, mobileResponse.getResponse());
                    if (mobileResponse.getResponseCode() == 200) {

                        Mylogger.getInstance().Logit(TAG, mobileResponse.getData().get(0).getScreen() + "");

                        sessionManager.saveData(Constants.KEY_EMP_MOBILE, mobile);
                        processID = String.valueOf(mobileResponse.getData().get(0).getProccessId());
                        sessionManager.saveData(Constants.KEY_PROCESS_ID, processID);
                        Log.e("PROCESS ID", "" + processID);
                        chqCount = mobileResponse.getData().get(0).getBank();
                        goTOScreen(mobileResponse.getData().get(0).getScreen());
                    } else if (mobileResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(OTPActivity.this);
                    } else {
                        showMSG(false, response.body().getResponse());
                        if (response.body().getResponseCode() == 405) {
                            sessionManager.logoutUser(OTPActivity.this);
                        }
                    }
                } else {
                    showMSG(false, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetMobileResponse> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void goTOScreen(Integer screenID) {
        Integer screen = screenID;

        Log.e("Screen id", String.valueOf(screen));

        Intent i;
        if (screenID == 1) {

            commanFragmentCallWithoutBackStack(new MapsCurrentPlaceFragment());

        } else if (screen == 2) {

            commanFragmentCallWithoutBackStack(new AadharVerificationFragment());


        } else if (screen == 3) {

            commanFragmentCallWithoutBackStack(new PanVerificationFragment());


        } else if (screen == 4) {

            commanFragmentCallWithoutBackStack(new ChequeUploadFragment());


        } else if (screen == 5) {

            commanFragmentCallWithoutBackStack(new InfoFragment());


        } else if (screen == 6) {


            commanFragmentCallWithoutBackStack(new DocUploadFragment());


        } else if (screen == 7) {
            commanFragmentCallWithoutBackStack(new DeliveryBoyFragment());
            //commanFragmentCallWithoutBackStack(new RateFragment());

            // commanFragmentCallWithoutBackStack(new DeliveryBoysDetFragment());


        } else if (screen == 8) {
            commanFragmentCallWithoutBackStack(new RateFragment());

//            commanFragmentCallWithoutBackStack(new AgreementDetailActivity());
           /* i = new Intent(OTPActivity.this, AgreementDetailActivity.class);
            startActivity(i);
            finish();*/
            /*i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            startActivity(i);
            finish();*/
           /* status_page= "AgreeMentVerification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
            startActivity(i);
            finish();*/
            //  commanFragmentCallWithoutBackStack(new RateFragment());

        } else if (screen == 9) {


            status_page = "AgreeMentVerification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process", status_page);
            startActivity(i);
            finish();
//            commanFragmentCallWithoutBackStack(new NocActivity());

            /*i = new Intent(OTPActivity.this, AgreementDetailActivity.class);
            startActivity(i);
            finish();*/


         /*   i = new Intent(OTPActivity.this, NocActivity.class);
            startActivity(i);
            finish();*/
           /* status_page= "NOC Verification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
            startActivity(i);
            finish();*/


        } else if (screen == 10) {

//            commanFragmentCallWithoutBackStack(new GstActivity());


           /* i = new Intent(OTPActivity.this, NocActivity.class);
            startActivity(i);
            finish();*/
            /*i = new Intent(OTPActivity.this, GstActivity.class);
            startActivity(i);
            finish();*/

            status_page = "NOC Verification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process", status_page);
            startActivity(i);
            finish();
            /*status_page= "GST signing process";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
            startActivity(i);
            finish();*/


        } else if (screen == 11) {


          /*  i = new Intent(OTPActivity.this, GstActivity.class);
            startActivity(i);
            finish();*/

            status_page = "GST signing process";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process", status_page);
            startActivity(i);
            finish();
            // commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        } else if (screen == 12) {

            commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
            //commanFragmentCallWithoutBackStack(new TaskCompletedFragment());

           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        } else if (screen == 13) {

            commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
            // commanFragmentCallWithoutBackStack(new TaskCompletedFragment());

           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        } else {
            Toast.makeText(getApplicationContext(), "" + screen, Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        pvOTP = findViewById(R.id.secondPinView);
        txt_mobile = findViewById(R.id.tvMobile);
        tvTime = findViewById(R.id.tvTimeofOTP);
        mProgressDialog = new ProgressDialog(OTPActivity.this);
        mProgressDialog.setMessage("Verifying Mobile Number...");
        mProgressDialog.setCancelable(false);
        tv_resent_otp = (TextView) findViewById(R.id.tvResend);
        tvChangeMobile = findViewById(R.id.tvChangeMobile);
        tvChangeMobile.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tvTitle);
        tv_title.setText(Constants.TITLE_MOBILE_AUTH);

        btn_next = findViewById(R.id.btVerify);
        txt_mobile.setText("+91 " + mobile);
        btn_next.setOnClickListener(this);

        tv_resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.length() < 10) {
                    showMSG(false, "Provide valid number");
                } else {
                    if (resend == false) {
                        new CountDownTimer(30000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                countDown--;
                                tvTime.setVisibility(View.VISIBLE);
                                tvTime.setText("Uploading cheque document. Please wait... in 00:" + countDown);
                            }

                            public void onFinish() {
                                resend = false;
                                countDown = 30;
                                tvTime.setVisibility(View.GONE);
                            }
                        }.start();
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            getOtp(v);
                        } else {
                            Constants.ShowNoInternet(OTPActivity.this);
                        }

                        resend = true;
                    } else {
                        showMSG(true, "Try after " + countDown + " Seconds");
                    }
                }
            }
        });


        v = navigationView.findViewById(R.id.menufooter);
        vHeader = navigationView.findViewById(R.id.nav_headerLayout);
        tvHeaderName = vHeader.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = vHeader.findViewById(R.id.tvHeaderEmail);

        tvHeaderEmail.setText(sessionManager.getEmail());
        tvHeaderName.setText(sessionManager.getName());


        llNavonBoardNewVendor = v.findViewById(R.id.llNavOnboardNewVendor);
        llNavLiveVendor = v.findViewById(R.id.llNavLiveVendor);
        llNavInProgressVendor = v.findViewById(R.id.llNavInProgressVendor);
        llNavInvoice = v.findViewById(R.id.llNavInvoice);
        llNavGSTInvoice = v.findViewById(R.id.llNavGSTnote);
        llNavDebitInvoice = v.findViewById(R.id.llNavReports);
        llNavReport = v.findViewById(R.id.llNavReports);
        llNavLogut = v.findViewById(R.id.llNavLogout);

        llNavSupports = v.findViewById(R.id.llNavSuppors);
        llNavSupports.setOnClickListener(this);

        llNavonBoardNewVendor.setOnClickListener(this);
        llNavLiveVendor.setOnClickListener(this);
        llNavInProgressVendor.setOnClickListener(this);
        llNavInvoice.setOnClickListener(this);
        llNavGSTInvoice.setOnClickListener(this);
        llNavDebitInvoice.setOnClickListener(this);
        llNavReport.setOnClickListener(this);
        llNavLogut.setOnClickListener(this);


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
                converted = getResizedBitmap(bitmap, 400);
                Intent i = new Intent(OTPActivity.this, GeneralSupportSubmitActivity.class);
                i.putExtra("BitmapImage", converted);
                i.putExtra(Constants.PARAM_SCREEN_NAME, screenFinal);
                startActivity(i);
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


        pvOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    if (validate(v)) {
                        String userOTp = pvOTP.getText().toString();
                        if (userOTp.equals(otp)) {
                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                GPSTracker gps = new GPSTracker(OTPActivity.this);
                                if (gps.canGetLocation()) {
                                    Double lat = gps.getLatitude();
                                    Double lng = gps.getLongitude();
                                    lattitude = String.valueOf(gps.getLatitude());
                                    longitude = String.valueOf(gps.getLongitude());
                                    Log.e("Lattitude", lattitude);
                                    Log.e("Longitude", longitude);


                                } else {

                                    gps.showSettingsAlert();
                                }
                                getMobileDetails(v);
                            } else {
                                Constants.ShowNoInternet(OTPActivity.this);
                            }
                        } else {

                            showMSG(false, "OTP not matched");
                        }
                    }
                }
                return false;
            }
        });

    }

    private boolean validate(View v) {
       /* if (TextUtils.isEmpty(edt_otp_1.getText().toString())) {
            edt_otp_1.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_2.getText().toString())) {
            edt_otp_2.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_3.getText().toString())) {
            edt_otp_3.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        } else if (TextUtils.isEmpty(edt_otp_4.getText().toString())) {
            edt_otp_4.requestFocus();
            showMSG(false, "Provide OTP");
            return false;
        }*/
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {

            commanFragmentCallWithoutBackStack(new ProfileFragment());

        }
        if (id == R.id.nav_1) {
            Intent i = new Intent(OTPActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_2) {

            commanFragmentCallWithoutBackStack(new MobileFragment());

        }
        if (id == R.id.nav_3) {

            commanFragmentCallWithoutBackStack(new InvoiceMainFragment());

        }
        if (id == R.id.nav_v) {

            commanFragmentCallWithoutBackStack(new VendorOnboardedList());


        }
        if (id == R.id.nav_4) {
            commanFragmentCallWithoutBackStack(new NotificationFragment());


        }
        if (id == R.id.nav_gst) {
            commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());

        }
        if (id == R.id.nav_debit) {
            commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());

        }
        if (id == R.id.nav_5) {

            commanFragmentCallWithoutBackStack(new ReportFragment());


        }
        if (id == R.id.nav_Support) {
            commanFragmentCallWithoutBackStack(new SupportFragment());
        }

        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(OTPActivity.this);
            sessionManager.logoutUser(OTPActivity.this);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
       /* if (charSequence == edt_otp_1.getEditableText() && after != 0) {
            edt_otp_2.requestFocus();
        } else if (charSequence == edt_otp_2.getEditableText() && after != 0) {
            edt_otp_3.requestFocus();
        } else if (charSequence == edt_otp_3.getEditableText() && after != 0) {
            edt_otp_4.requestFocus();
        } else if (charSequence == edt_otp_4.getEditableText() && after != 0) {
            hideKeyboardwithoutPopulate(OTPActivity.this);
        }*/
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void getOtp(final View v) {

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_MOBILE_NO, mobile.trim());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
                        // Toast.makeText(getApplicationContext(), getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        otp = getOTP.getData().get(0).getOtp();
                    } else {
                        showMSG(false, getOTP.getResponse());
                        if (getOTP.getResponseCode() == 405) {
                            sessionManager.logoutUser(OTPActivity.this);
                        } else if (getOTP.getResponseCode() == 411) {
                            sessionManager.logoutUser(OTPActivity.this);
                        }
                    }
                } else {
                    showMSG(false, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) findViewById(R.id.txt_show_msg_fail);
        txt_show_msg_sucess.setVisibility(View.GONE);
        txt_show_msg_fail.setVisibility(View.GONE);
        if (b) {
            txt_show_msg_sucess.setText(msg);
            txt_show_msg_sucess.setVisibility(View.VISIBLE);
        } else {
            txt_show_msg_fail.setText(msg);
            txt_show_msg_fail.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                txt_show_msg_sucess.setVisibility(View.GONE);
                txt_show_msg_fail.setVisibility(View.GONE);
            }
        }, 2000);
    }

    public static void settitle(String title) {
        if (title != null) {
            tv_title.setText(title);
        }

    }

    public static void setScreenname(String screen) {
        if (screen != null) {
            screenFinal = screen;
        }

    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;


        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();
        }
    }


    public void commanFragmentCallWithoutBackStack2(Fragment fragment, String vendor_mobile) {

        Fragment cFragment = fragment;

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.KEY_EMP_MOBILE, vendor_mobile);

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }

    /*  public void commanFragmentCallWithBackStack(Fragment fragment, String vendor_mobile) {

          Fragment cFragment = fragment;

          Bundle bundle = new Bundle();

          bundle.putSerializable(Constants.KEY_EMP_MOBILE, vendor_mobile);

          if (cFragment != null) {

              FragmentManager fragmentManager = getSupportFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragment.setArguments(bundle);
              fragmentTransaction.add(R.id.rvContentMain, cFragment);
              fragmentTransaction.addToBackStack(null);
              fragmentTransaction.commit();

          }
      }
  */
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            tv_title.setText(Constants.TITLE_MOBILE_AUTH);
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
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



}
