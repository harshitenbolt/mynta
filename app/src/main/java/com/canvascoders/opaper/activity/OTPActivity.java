package com.canvascoders.opaper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.Beans.verifymobile.GetMobileResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DebitInvoiceMainFragment;

import com.canvascoders.opaper.fragment.DeliveryBoyFragment;
import com.canvascoders.opaper.fragment.GSTInvoiceMainFragment;
import com.canvascoders.opaper.utils.Constants;
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
import com.canvascoders.opaper.fragment.VenderListFragment;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TextWatcher {

    private EditText edt_otp_1;
    private EditText edt_otp_2;
    private EditText edt_otp_3;
    private EditText edt_otp_4;
    private FloatingActionButton btn_next;
    private SessionManager sessionManager;
    private String TAG = "OTPActivity";
    private String otp, mobile;
    private TextView txt_mobile;
    private ProgressDialog mProgressDialog;
    private TextView tv_resent_otp;
    private Boolean resend = false;
    private int countDown = 30;
    static TextView tv_title;
    private Toolbar toolbar;
    private String processID;
    private int chqCount = 0;
    String status_page;


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
        initView();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.tvHeaderName);
        nav_user.setText(sessionManager.getEmail());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (validate(v)) {
                String userOTp = edt_otp_1.getText().toString() + edt_otp_2.getText().toString() + edt_otp_3.getText().toString() + edt_otp_4.getText().toString();
                if (userOTp.equals(otp)) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        getMobileDetails(v);
                    } else {
                        Constants.ShowNoInternet(this);
                    }
                } else {
                    edt_otp_1.setText("");
                    edt_otp_2.setText("");
                    edt_otp_3.setText("");
                    edt_otp_4.setText("");
                    edt_otp_1.requestFocus();
                    showMSG(false, "OTP not matched");
                }
            }
        } else if (v.getId() == R.id.img_edit_mobile) {
            commanFragmentCallWithBackStack(new MobileFragment(), mobile);
        }
    }

    private void getMobileDetails(final View v) {

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_MOBILE_NO, mobile);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        Mylogger.getInstance().Logit(TAG, user.toString());
        ApiClient.getClient().create(ApiInterface.class).verifyMobile("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetMobileResponse>() {
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


            status_page= "AgreeMentVerification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
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

            status_page= "NOC Verification";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
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

            status_page= "GST signing process";
            i = new Intent(OTPActivity.this, ProcessInfoActivity.class);
            i.putExtra("page_process",status_page);
            startActivity(i);
            finish();
           // commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        }

        else if (screen == 12) {

            commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
            //commanFragmentCallWithoutBackStack(new TaskCompletedFragment());

           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        }
        else if (screen == 13) {

            commanFragmentCallWithoutBackStack(new TaskCompletedFragment());
           // commanFragmentCallWithoutBackStack(new TaskCompletedFragment());

           /* i = new Intent(OTPActivity.this, TaskCompletedFragment.class);
            startActivity(i);
            finish();*/
        }else {
            Toast.makeText(getApplicationContext(), "" + screen, Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        mProgressDialog = new ProgressDialog(OTPActivity.this);
        mProgressDialog.setMessage("Verifying Mobile Number...");
        tv_resent_otp = (TextView) findViewById(R.id.tv_resend);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(Constants.TITLE_MOBILE_AUTH);
        edt_otp_1 = (EditText) findViewById(R.id.edt_otp_1);
        edt_otp_2 = (EditText) findViewById(R.id.edt_otp_2);
        edt_otp_3 = (EditText) findViewById(R.id.edt_otp_3);
        edt_otp_4 = (EditText) findViewById(R.id.edt_otp_4);
        edt_otp_1.addTextChangedListener(this);
        edt_otp_2.addTextChangedListener(this);
        edt_otp_3.addTextChangedListener(this);
        edt_otp_4.addTextChangedListener(this);
        txt_mobile = (TextView) findViewById(R.id.tv_mobile);
        btn_next = (FloatingActionButton) findViewById(R.id.btn_next);
        txt_mobile.setText("+91 " + mobile);
        ImageView img_edit_mobile = (ImageView) findViewById(R.id.img_edit_mobile);
        img_edit_mobile.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        edt_otp_1.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String first = edt_otp_1.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && first.trim().length() == 0) {
                    edt_otp_1.setText("");
                    edt_otp_1.requestFocus();
                }
                return false;
            }
        });
        edt_otp_2.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String second = edt_otp_2.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && second.trim().length() == 0) {
                    edt_otp_2.setText("");
                    edt_otp_1.requestFocus();

                }
                return false;
            }
        });
        edt_otp_3.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String three = edt_otp_3.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && three.trim().length() == 0) {
                    edt_otp_3.setText("");
                    edt_otp_2.requestFocus();
                }
                return false;
            }
        });
        edt_otp_4.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String four = edt_otp_4.getText().toString().trim();
                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && four.trim().length() == 0) {
                    edt_otp_4.setText("");
                    edt_otp_3.requestFocus();
                }
                return false;
            }
        });
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
                                tv_resent_otp.setText("00:" + countDown);
                            }

                            public void onFinish() {
                                resend = false;
                                countDown = 30;
                                tv_resent_otp.setText("Resend code");
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
    }

    private boolean validate(View v) {
        if (TextUtils.isEmpty(edt_otp_1.getText().toString())) {
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
        }
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

            commanFragmentCallWithoutBackStack(new VenderListFragment());


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
        if (id == R.id.nav_logout) {

            SessionManager sessionManager = new SessionManager(OTPActivity.this);
            sessionManager.logoutUser(OTPActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (charSequence == edt_otp_1.getEditableText() && after != 0) {
            edt_otp_2.requestFocus();
        } else if (charSequence == edt_otp_2.getEditableText() && after != 0) {
            edt_otp_3.requestFocus();
        } else if (charSequence == edt_otp_3.getEditableText() && after != 0) {
            edt_otp_4.requestFocus();
        } else if (charSequence == edt_otp_4.getEditableText() && after != 0) {
            hideKeyboardwithoutPopulate(OTPActivity.this);
        }
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
        ApiClient.getClient().create(ApiInterface.class).sendOTP("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOTP getOTP = response.body();
                    if (getOTP.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "OTP is =>" + getOTP.getData().get(0).getOtp().toString());
                        Toast.makeText(getApplicationContext(), getOTP.getResponse(), Toast.LENGTH_LONG).show();
                        otp = getOTP.getData().get(0).getOtp();
                    } else {
                        showMSG(false, getOTP.getResponse());
                        if (getOTP.getResponseCode() == 405) {
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


    public void commanFragmentCallWithBackStack(Fragment fragment, String vendor_mobile) {

        Fragment cFragment = fragment;

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.KEY_EMP_MOBILE, vendor_mobile);

        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

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

}