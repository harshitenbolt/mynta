package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.canvascoders.opaper.Beans.GeneralSupportResponse.GeneralSupportResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.fragment.ExpiredAgreementVendorFragment;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.ImagePicker;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.fragment.InvoiceListingFragment;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.SupportFragment;
import com.canvascoders.opaper.Beans.NotificationResponse.NotificattionResponse;
import com.canvascoders.opaper.fragment.VendorInProgressList;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Button btn_onboard_vendor;
    Button btn_onboard_vendor_list;
    Button btn_onboard_invoice;
    Button btn_notification;
    Button btn_report;
    LinearLayout llOnboardNewVendor, llNotification, llLiveVendors, llInProgressVendors, llReports, llInvoice;
    LinearLayout llNavonBoardNewVendor, llNavAssessmnt, llNavLiveVendor, llNavInProgressVendor, llNavInvoice, llNavGSTInvoice, llNavDebitInvoice, llNavReport, llNavTask, llNavLogut, llNavSupports;

    private Menu menu;
    FrameLayout flImage;
    RelativeLayout rvMain;
    boolean enabled = false;
    TextView tvHeaderName, tvHeaderEmail;
    NavigationView navigationView;
    private ProgressDialog mProgressDialog;
    private String TAG = "DashboardActivity";
    private SessionManager sessionManager;
    private Integer newAppVesion = 0;
    private String attachment = "";
    private TextView tv_current_version;
    public static TextView tv_title, tvUsername;
    DrawerLayout drawer;
    ImageView ivSupport, imageView, ivHome;
    View v, vHeader;
    TextView tvInProgressVendorCount, tvLiveVendorCount, tvCountNotification, tvCountTasks;
    Button ivSelect;
    SwipeRefreshLayout swLayout;
    Bitmap b, converted;
    String screenOpenMobile = "";
    String mobileNo = "";
    RelativeLayout rvMainWithRect;
    RequestPermissionHandler requestPermissionHandler;
    ImageView ivNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DashboardActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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


        requestPermissionHandler = new RequestPermissionHandler();


        sessionManager = new SessionManager(DashboardActivity.this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        initView();


        //navigationView.setNavigationItemSelectedListener(this);
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getNotification();
        } else {
            Constants.ShowNoInternet(this);
        }

    }

    private void initView() {

        /*if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }*/
        tv_title = findViewById(R.id.tvTitle);
        settitle("Dashboard");

        mProgressDialog = new ProgressDialog(DashboardActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        tvInProgressVendorCount = findViewById(R.id.tvInProgressCount);
        tvLiveVendorCount = findViewById(R.id.tvLiveVendorCount);
        tvCountNotification = findViewById(R.id.tvCountNotification);
        tvCountTasks = findViewById(R.id.tvCountTasks);
        ivNotification = findViewById(R.id.ivNotification);
        ivNotification.setOnClickListener(this);
        llInProgressVendors = findViewById(R.id.llInProgressVendor);
        llInvoice = findViewById(R.id.llInvoice);

        llLiveVendors = findViewById(R.id.llLiveVendor);
        llNotification = findViewById(R.id.llNotification);
        llOnboardNewVendor = findViewById(R.id.llOnboardNewVendor);
        llReports = findViewById(R.id.llTasks);
        llOnboardNewVendor.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        llLiveVendors.setOnClickListener(this);
        llInProgressVendors.setOnClickListener(this);
        swLayout = findViewById(R.id.swLayout);
        swLayout.setOnRefreshListener(this);
        llReports.setOnClickListener(this);
        llInvoice.setOnClickListener(this);


        v = navigationView.findViewById(R.id.menufooter);
        vHeader = navigationView.findViewById(R.id.nav_headerLayout);
        tvHeaderName = vHeader.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = vHeader.findViewById(R.id.tvHeaderEmail);

        ivHome = vHeader.findViewById(R.id.ivHome);
        ivHome.setOnClickListener(this);

        tvHeaderEmail.setText(sessionManager.getEmail());
        tvHeaderName.setText(sessionManager.getName());


        llNavonBoardNewVendor = v.findViewById(R.id.llNavOnboardNewVendor);
        llNavAssessmnt = v.findViewById(R.id.llNavAssessmnt);
        llNavLiveVendor = v.findViewById(R.id.llNavLiveVendor);
        llNavInProgressVendor = v.findViewById(R.id.llNavInProgressVendor);
        llNavInvoice = v.findViewById(R.id.llNavInvoice);
        llNavGSTInvoice = v.findViewById(R.id.llNavGSTnote);
        llNavDebitInvoice = v.findViewById(R.id.llNavDebitNote);
        llNavReport = v.findViewById(R.id.llNavReports);
        llNavTask = v.findViewById(R.id.llNavTask);
        llNavLogut = v.findViewById(R.id.llNavLogout);
        llNavSupports = v.findViewById(R.id.llNavSuppors);


        llNavSupports.setOnClickListener(this);

        llNavonBoardNewVendor.setOnClickListener(this);
        llNavAssessmnt.setOnClickListener(this);
        llNavLiveVendor.setOnClickListener(this);
        llNavInProgressVendor.setOnClickListener(this);
        llNavInvoice.setOnClickListener(this);
        llNavGSTInvoice.setOnClickListener(this);
        llNavDebitInvoice.setOnClickListener(this);
        llNavReport.setOnClickListener(this);
        llNavLogut.setOnClickListener(this);
        llNavTask.setOnClickListener(this);
        llNavTask.setOnClickListener(this);


        getPermitionGrant();
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

        // if task assigned from task list
        if (getIntent().getExtras() != null) {
            //do here

            screenOpenMobile = getIntent().getExtras().getString("data");
            if (screenOpenMobile.equalsIgnoreCase("1")) {
                tv_title.setText("Mobile Verification");
                llOnboardNewVendor.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                mobileNo = getIntent().getExtras().getString("mobile_no");

                commanFragmentCallWithBackStackwithData(new MobileFragment());
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
                        Toast.makeText(DashboardActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(DashboardActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);


                        // finish();
                    } else {
                        Toast.makeText(DashboardActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = DashboardActivity.this.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llOnboardNewVendor:
                tv_title.setText("Mobile Verification");
                llOnboardNewVendor.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new MobileFragment());
                break;

            case R.id.llNotification:
                //tv_title.setText("Notifications");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
//                Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
//                startActivity(i1);
                commanFragmentCallWithBackStack(new ExpiredAgreementVendorFragment());

                //   commanFragmentCallWithBackStack(new NotificationFragment());
                break;

            case R.id.ivNotification:
                tv_title.setText("Notifications");

                Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(i1);
                // commanFragmentCallWithBackStack(new ExpiredAgreementVendorFragment());

                //   commanFragmentCallWithBackStack(new NotificationFragment());
                break;


            case R.id.llInProgressVendor:
                tv_title.setText("In Progress Vendor");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new VendorInProgressList());
                break;
            case R.id.ivHome:
                Intent i = new Intent(DashboardActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.llLiveVendor:
                tv_title.setText("Live Vendors");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new VendorOnboardedList());
                break;


            case R.id.llTasks:
                tv_title.setText("Tasks");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llInvoice.setBackgroundResource(0);
                /*commanFragmentCallWithBackStack(new ReportFragment());*/

                Intent i2 = new Intent(DashboardActivity.this, TaskListActivity.class);
                startActivity(i2);

                break;


            case R.id.llNavTask:
                tv_title.setText("Tasks");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llInvoice.setBackgroundResource(0);
                drawer.closeDrawer(GravityCompat.START);
                /*commanFragmentCallWithBackStack(new ReportFragment());*/

                Intent i3 = new Intent(DashboardActivity.this, TaskListActivity.class);
                startActivity(i3);
                break;
            case R.id.llInvoice:
                tv_title.setText("Invoices");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                commanFragmentCallWithBackStack(new InvoiceListingFragment());
                break;


            case R.id.llNavOnboardNewVendor:
                tv_title.setText("Mobile Verification");

                commanFragmentCallWithBackStack(new MobileFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.llNavAssessmnt:
                Intent i4 = new Intent(DashboardActivity.this, AssessmentScreenActivity.class);
                startActivity(i4);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavLiveVendor:
                tv_title.setText("Live Vendors");
                drawer.closeDrawer(GravityCompat.START);
                commanFragmentCallWithBackStack(new VendorOnboardedList());

                break;
            case R.id.llNavInProgressVendor:
                tv_title.setText("InProgress Vendors");
                commanFragmentCallWithBackStack(new VendorInProgressList());
                drawer.closeDrawer(GravityCompat.START);

                break;
            case R.id.llNavInvoice:

                /*commanFragmentCallWithBackStack(new InvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);*/
                tv_title.setText("Invoices");
                commanFragmentCallWithBackStack(new InvoiceListingFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavGSTnote:
                commanFragmentCallWithBackStack(new GSTInvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavDebitNote:
                tv_title.setText("Debit Invoice List");
                commanFragmentCallWithBackStack(new DebitInvoiceMainFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.llNavSuppors:
                commanFragmentCallWithBackStack(new SupportFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.llNavReports:
                commanFragmentCallWithBackStack(new ReportFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.llNavLogout:
                SessionManager sessionManager = new SessionManager(DashboardActivity.this);
                sessionManager.logoutUser(DashboardActivity.this);
                break;


        }

       /*
        if (v.getId() == R.id.btn_onboard_vendor_list) {

            commanFragmentCallWithBackStack(new VenderListFragment());
        //    commanFragmentCallWithBackStack(new VenderListFragment1());

        } *//*else if (v.getId() == R.id.btn_logout) {
            SessionManager sessionManager = new SessionManager(DashboardActivity.this);
            sessionManager.logoutUser(DashboardActivity.this);
        } *//*else if (v.getId() == R.id.btn_onboard_invoice) {

            commanFragmentCallWithBackStack(new InvoiceMainFragment());

        } else if (v.getId() == R.id.btn_report) {
            commanFragmentCallWithBackStack(new ReportFragment());

        } else if (v.getId() == R.id.btn_notification) {
            commanFragmentCallWithBackStack(new NotificationFragment());

        }*/
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {

            commanFragmentCallWithBackStack(new ProfileFragment());

        }
        if (id == R.id.nav_1) {
           /* Intent i = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();*/
            commanFragmentCallWithBackStack(new MobileFragment());
        }
        if (id == R.id.nav_2) {

            commanFragmentCallWithBackStack(new MobileFragment());

        }
        if (id == R.id.nav_3) {

            commanFragmentCallWithBackStack(new InvoiceMainFragment());

        }
        if (id == R.id.nav_gst) {
            commanFragmentCallWithoutBackStack(new GSTInvoiceMainFragment());

        }
        if (id == R.id.nav_debit) {
            commanFragmentCallWithoutBackStack(new DebitInvoiceMainFragment());

        }
        if (id == R.id.nav_v) {

            commanFragmentCallWithBackStack(new VendorOnboardedList());
            // commanFragmentCallWithBackStack(new VenderListFragment1());

        }

        if (id == R.id.nav_4) {
            //commanFragmentCallWithBackStack(new NotificationFragment());

            Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
            startActivity(i1);

        }
        if (id == R.id.nav_5) {

            commanFragmentCallWithBackStack(new ReportFragment());


        }
        if (id == R.id.nav_Support) {
            commanFragmentCallWithBackStack(new SupportFragment());
        }
        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(DashboardActivity.this);
            sessionManager.logoutUser(DashboardActivity.this);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    public void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public void unlockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public void getNotification() {

        swLayout.setRefreshing(false);
        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


            Mylogger.getInstance().Logit(TAG, "getUserInfo");


            mProgressDialog.setMessage("we are retrieving information, please wait!");
            mProgressDialog.show();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(6, TimeUnit.MINUTES);
            httpClient.writeTimeout(6, TimeUnit.MINUTES);
            httpClient.readTimeout(6, TimeUnit.MINUTES);
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BaseURL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            Call<NotificattionResponse> callUpload = retrofit.create(ApiInterface.class).getNotification("Bearer " + sessionManager.getToken(), params);
            callUpload.enqueue(new Callback<NotificattionResponse>() {
                @Override
                public void onResponse(Call<NotificattionResponse> call, retrofit2.Response<NotificattionResponse> response) {

                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    NotificattionResponse getnotificationdata = response.body();


                    if (getnotificationdata != null) {


                        Constants.showAdhar = String.valueOf(getnotificationdata.getShowAdharAPi());

                        if (getnotificationdata.getIsupdateavailable() != null) {
                            Log.e("Second Step", "" + getnotificationdata.getIsupdateavailable() + " " + getnotificationdata.getCount());
                        }
                    }
                    if (response.isSuccessful()) {
                        Log.e("First", "Step done");

                        if (getnotificationdata.getLiveVendorCount() == 0) {
                            tvLiveVendorCount.setVisibility(View.GONE);
                        } else {
                            tvLiveVendorCount.setVisibility(View.VISIBLE);
                        }
                        if (getnotificationdata.getInProgressCount() == 0) {
                            tvInProgressVendorCount.setVisibility(View.GONE);
                        } else {
                            tvInProgressVendorCount.setVisibility(View.VISIBLE);
                        }


                        if (getnotificationdata.getAgentNotificationCount() == 0) {
                            tvCountNotification.setVisibility(View.GONE);
                        } else {
                            tvCountNotification.setVisibility(View.GONE);
                        }
                        if (getnotificationdata.getTaskCount() == 0) {
                            tvCountTasks.setVisibility(View.GONE);
                        } else {
                            tvCountTasks.setVisibility(View.VISIBLE);
                        }


                        tvInProgressVendorCount.setText(String.valueOf(getnotificationdata.getInProgressCount()));
                        tvLiveVendorCount.setText(String.valueOf(getnotificationdata.getLiveVendorCount()));
                        //tvCountNotification.setText(String.valueOf(getnotificationdata.getAgentNotificationCount()));
                        tvCountTasks.setText(String.valueOf(getnotificationdata.getTaskCount()));


                        if (getnotificationdata.getResponse().equalsIgnoreCase("success")) {
                            Log.e("Sec", "Step done");

//                            if (getnotificationdata.getCount() > 0) {
//
//                            }
/*
                            if (getnotificationdata.getIsMobileVerify().equalsIgnoreCase("0")) {
                                Intent i = new Intent(DashboardActivity.this, ExecutiveMobileVerifyActivity.class);
                                startActivity(i);
                                finish();
                                sessionManager.createLogin(sessionManager.getAgentID(), sessionManager.getToken(), sessionManager.getName(), sessionManager.getEmail(), sessionManager.getRHID(), sessionManager.getEmpId(), getnotificationdata.getMobile(), ""*//*, getnotificationdata.getIsMobileVerify()*//*);

                            } else {*/
                            sessionManager.createLogin(sessionManager.getAgentID(), sessionManager.getToken(), sessionManager.getName(), sessionManager.getEmail(), sessionManager.getRHID(), sessionManager.getEmpId(), getnotificationdata.getMobile(), ""/*, getnotificationdata.getIsMobileVerify()*/);

                            if (getnotificationdata.getIsupdateavailable()) {
                                Log.e("Update available", "Step done");

                                newAppVesion = getnotificationdata.getVersion();
                                if (newAppVesion > Constants.APP_VERSION) {

//                                    commanFragmentCallWithoutBackStack(new UpdaterFragment());

                                    Intent update = new Intent(DashboardActivity.this, UpdaterActivity.class);
                                    startActivity(update);

                                    finish();
                                }


                                if (getnotificationdata.getNotice().equalsIgnoreCase("")) {

                                } else {
                                    DialogUtil.errorMessage(DashboardActivity.this, getnotificationdata.getNotice());
                                }
                                // lockDrawer();

                            } else {
                                unlockDrawer();


                            }
                        }
                    } else {
                        Toast.makeText(DashboardActivity.this, "#errorcode :- 2045 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<NotificattionResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(DashboardActivity.this, "#errorcode :- 2045 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Constants.ShowNoInternet(this);
        }

    }


    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            fragmentTransaction.replace(R.id.rvContentMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    public void commanFragmentCallWithBackStackwithData(Fragment fragment) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putString(Constants.KEY_EMP_MOBILE, mobileNo);


        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            fragmentTransaction.replace(R.id.rvContentMain, cFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            fragmentTransaction.replace(R.id.rvContentMain, cFragment);
            fragmentTransaction.commit();

        }
    }

    private void getPermitionGrant() {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void settitle(String title) {
        if (title != null) {
            tv_title.setText(title);
        }

    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).commit();
        if (fm.getBackStackEntryCount() > 0) {
            tv_title.setText(Constants.TITLE_DASHBOARD);
            getNotification();
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
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
    protected void onResume() {
        super.onResume();
        getNotification();
        tv_title.setText("Dashboard");
    }

    @Override
    public void onRefresh() {
        getNotification();
    }
}