package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.fragment.InvoiceMainFragment;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.NotificationFragment;
import com.canvascoders.opaper.fragment.ProfileFragment;
import com.canvascoders.opaper.fragment.ReportFragment;
import com.canvascoders.opaper.fragment.SupportFragment;
import com.canvascoders.opaper.fragment.UpdaterFragment;
import com.canvascoders.opaper.fragment.VenderListFragment;
import com.canvascoders.opaper.Beans.NotificationResponse.NotificattionResponse;
import com.canvascoders.opaper.fragment.VenderListFragment1;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Button btn_onboard_vendor;
    Button btn_onboard_vendor_list;
    Button btn_onboard_invoice;
    Button btn_notification;
    Button btn_report;
    private Menu menu;
    boolean enabled = false;
    private ProgressDialog mProgressDialog;
    private String TAG = "DashboardActivity";
    private SessionManager sessionManager;
    private Integer newAppVesion = 0;
    private TextView tv_current_version;
    static TextView tv_title,tvUsername;
    DrawerLayout drawer;
    RequestPermissionHandler requestPermissionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tv_title);


        tv_title.setText(Constants.TITLE_DASHBOARD);

        requestPermissionHandler = new RequestPermissionHandler();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sessionManager = new SessionManager(DashboardActivity.this);
        initView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getNotification();
        } else {
            Constants.ShowNoInternet(this);
        }
      //  tv_current_version.setText("Version : " + Constants.APP_VERSION);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.tvHeaderName);
        nav_user.setText(sessionManager.getEmail());

    }

    private void initView() {

       // tv_current_version = (TextView) findViewById(R.id.tv_current_version);
        mProgressDialog = new ProgressDialog(DashboardActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait authenticating store executive");

        btn_onboard_vendor = (Button) findViewById(R.id.btn_onboard_vendor);
        btn_report = (Button) findViewById(R.id.btn_report);
        btn_onboard_invoice = (Button) findViewById(R.id.btn_onboard_invoice);
       // btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_onboard_vendor_list = (Button) findViewById(R.id.btn_onboard_vendor_list);
        btn_notification = (Button) findViewById(R.id.btn_notification);

        getPermitionGrant();


        //set listner
        btn_onboard_vendor.setOnClickListener(this);
       // btn_logout.setOnClickListener(this);
        btn_onboard_vendor_list.setOnClickListener(this);
        btn_onboard_invoice.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_notification.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_onboard_vendor) {

            commanFragmentCallWithBackStack(new MobileFragment());

        }
        if (v.getId() == R.id.btn_onboard_vendor_list) {

            commanFragmentCallWithBackStack(new VenderListFragment());
        //    commanFragmentCallWithBackStack(new VenderListFragment1());

        } /*else if (v.getId() == R.id.btn_logout) {
            SessionManager sessionManager = new SessionManager(DashboardActivity.this);
            sessionManager.logoutUser(DashboardActivity.this);
        } */else if (v.getId() == R.id.btn_onboard_invoice) {

            commanFragmentCallWithBackStack(new InvoiceMainFragment());

        } else if (v.getId() == R.id.btn_report) {
            commanFragmentCallWithBackStack(new ReportFragment());

        } else if (v.getId() == R.id.btn_notification) {
            commanFragmentCallWithBackStack(new NotificationFragment());

        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {

            commanFragmentCallWithBackStack(new ProfileFragment());

        }
        if (id == R.id.nav_1) {
            Intent i = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
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

            commanFragmentCallWithBackStack(new VenderListFragment());
           // commanFragmentCallWithBackStack(new VenderListFragment1());

        }

        if (id == R.id.nav_4) {
            commanFragmentCallWithBackStack(new NotificationFragment());

        }
        if (id == R.id.nav_5) {

            commanFragmentCallWithBackStack(new ReportFragment());


        }
        if(id == R.id.nav_Support){
            commanFragmentCallWithBackStack(new SupportFragment());
        }
        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(DashboardActivity.this);
            sessionManager.logoutUser(DashboardActivity.this);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


            Mylogger.getInstance().Logit(TAG, "getUserInfo");


            mProgressDialog.setMessage("Please wait getting details...");
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
            Call<NotificattionResponse> callUpload = retrofit.create(ApiInterface.class).getNotification("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<NotificattionResponse>() {
                @Override
                public void onResponse(Call<NotificattionResponse> call, retrofit2.Response<NotificattionResponse> response) {

                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    NotificattionResponse getnotificationdata = response.body();

                    if (getnotificationdata != null) {

                        if (getnotificationdata.getIsupdateavailable() != null) {
                            Log.e("Second Step", "" + getnotificationdata.getIsupdateavailable() + " " + getnotificationdata.getCount());
                        }
                    }
                    if (response.isSuccessful()) {
                        Log.e("First", "Step done");

                        if (getnotificationdata.getResponse().equalsIgnoreCase("success")) {
                            Log.e("Sec", "Step done");

//                            if (getnotificationdata.getCount() > 0) {
//
//                            }

                            if (getnotificationdata.getIsupdateavailable()) {
                                Log.e("Update available", "Step done");

                                newAppVesion = getnotificationdata.getVersion();
                                if (newAppVesion > Constants.APP_VERSION) {

//                                    commanFragmentCallWithoutBackStack(new UpdaterFragment());

                                    Intent update = new Intent(DashboardActivity.this, UpdaterActivity.class);
                                    startActivity(update);

                                    finish();
                                }
                               // lockDrawer();

                            }
                            else{
                                unlockDrawer();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificattionResponse> call, Throwable t) {
                    mProgressDialog.dismiss();

                }
            });
        } else {
            Constants.ShowNoInternet(getApplicationContext());
        }

    }





    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, cFragment);
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
            fragmentTransaction.replace(R.id.content_main, cFragment);
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
        if (fm.getBackStackEntryCount() > 0) {
            tv_title.setText(Constants.TITLE_DASHBOARD);
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


}