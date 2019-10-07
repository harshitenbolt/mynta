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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.canvascoders.opaper.fragment.VendorInProgressList;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.model.Dash;

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

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Button btn_onboard_vendor;
    Button btn_onboard_vendor_list;
    Button btn_onboard_invoice;
    Button btn_notification;
    Button btn_report;
    LinearLayout llOnboardNewVendor, llNotification, llLiveVendors, llInProgressVendors, llReports, llInvoice;
    LinearLayout llNavonBoardNewVendor, llNavLiveVendor, llNavInProgressVendor, llNavInvoice, llNavGSTInvoice, llNavDebitInvoice, llNavReport, llNavLogut, llNavSupports;

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
    private TextView tv_current_version;
    static TextView tv_title, tvUsername;
    DrawerLayout drawer;
    ImageView ivSupport, imageView, ivHome;
    View v, vHeader;
    TextView tvInProgressVendorCount, tvLiveVendorCount;
    Button ivSelect;
    SwipeRefreshLayout swLayout;
    Bitmap b, converted;
    RelativeLayout rvMainWithRect;
    RequestPermissionHandler requestPermissionHandler;


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


        tv_title = findViewById(R.id.tvTitle);
        settitle("Dashboard");

        mProgressDialog = new ProgressDialog(DashboardActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait authenticating store executive");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        tvInProgressVendorCount = findViewById(R.id.tvInProgressCount);
        tvLiveVendorCount = findViewById(R.id.tvLiveVendorCount);


        llInProgressVendors = findViewById(R.id.llInProgressVendor);
        llInvoice = findViewById(R.id.llInvoice);

        llLiveVendors = findViewById(R.id.llLiveVendor);
        llNotification = findViewById(R.id.llNotification);
        llOnboardNewVendor = findViewById(R.id.llOnboardNewVendor);
        llReports = findViewById(R.id.llReports);
        llOnboardNewVendor.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        llLiveVendors.setOnClickListener(this);
        llInProgressVendors.setOnClickListener(this);
        swLayout =findViewById(R.id.swLayout);
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
        llNavLiveVendor = v.findViewById(R.id.llNavLiveVendor);
        llNavInProgressVendor = v.findViewById(R.id.llNavInProgressVendor);
        llNavInvoice = v.findViewById(R.id.llNavInvoice);
        llNavGSTInvoice = v.findViewById(R.id.llNavGSTnote);
        llNavDebitInvoice = v.findViewById(R.id.llNavDebitNote);
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
                converted = getResizedBitmap(bitmap, 400);
                Intent i = new Intent(DashboardActivity.this, GeneralSupportSubmitActivity.class);
                i.putExtra("BitmapImage", converted);
                i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
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


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llOnboardNewVendor:
                tv_title.setText("Mobile Authentication");
                llOnboardNewVendor.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new MobileFragment());
                break;

            case R.id.llNotification:
                tv_title.setText("Notifications");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new NotificationFragment());
                break;


            case R.id.llInProgressVendor:
                tv_title.setText("Notifications");
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


            case R.id.llReports:
                tv_title.setText("Reports");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                llInvoice.setBackgroundResource(0);
                commanFragmentCallWithBackStack(new ReportFragment());
                break;
            case R.id.llInvoice:
                tv_title.setText("Invoices");
                llOnboardNewVendor.setBackgroundResource(0);
                llNotification.setBackgroundResource(0);
                llLiveVendors.setBackgroundResource(0);
                llInProgressVendors.setBackgroundResource(0);
                llReports.setBackgroundResource(0);
                llInvoice.setBackgroundResource(R.drawable.rounded_corner_bordercolor_green);
                commanFragmentCallWithBackStack(new InvoiceMainFragment());
                break;


            case R.id.llNavOnboardNewVendor:
                tv_title.setText("Mobile Authentication");

                commanFragmentCallWithBackStack(new MobileFragment());
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

                commanFragmentCallWithBackStack(new InvoiceMainFragment());
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
            commanFragmentCallWithBackStack(new NotificationFragment());

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
            Call<NotificattionResponse> callUpload = retrofit.create(ApiInterface.class).getNotification("Bearer " + sessionManager.getToken(), params);
            callUpload.enqueue(new Callback<NotificattionResponse>() {
                @Override
                public void onResponse(Call<NotificattionResponse> call, retrofit2.Response<NotificattionResponse> response) {

                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    NotificattionResponse getnotificationdata = response.body();

                    if (getnotificationdata != null) {


                        Constants.showAdhar= String.valueOf(getnotificationdata.getShowAdharAPi());

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
                        tvInProgressVendorCount.setText(String.valueOf(getnotificationdata.getInProgressCount()));
                        tvLiveVendorCount.setText(String.valueOf(getnotificationdata.getLiveVendorCount()));

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

                            } else {
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
        //tv_title.setText("Dashboard");
    }

    @Override
    public void onRefresh() {
        getNotification();
    }
}