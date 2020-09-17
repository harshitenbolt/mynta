package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.UserDetailTResponse.GetUserDetails;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.showAlert;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivity";
    private EditText edt_email_id, edt_password;
    private Button btn_login;
    private String longitude = "";
    private String lattitude = "";
    private String address = "";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    private TextView tvForgot;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean enabled = false;
    private boolean mLocationPermissionGranted;
    private ImageView ivShowPassword;
    GPSTracker gps;
    boolean showPassword = false;
    RequestPermissionHandler requestPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        gps = new GPSTracker(LoginActivity.this);
        initView();

        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                if (gps.canGetLocation()) {
                    Double lat = gps.getLatitude();
                    Double lng = gps.getLongitude();
                    lattitude = String.valueOf(gps.getLatitude());
                    longitude = String.valueOf(gps.getLongitude());
                    Log.e("Lattitude", lattitude);
                    Log.e("Longitude", longitude);
                    if (lat != 0.0) {
                        getAddress(lat, lng);
                    }


                } else {

                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings

                    gps.showSettingsAlert();
                }
            }

            @Override
            public void onFailed() {

            }
        });

        sessionManager = new SessionManager(LoginActivity.this);

    }

    private void initView() {

        requestPermissionHandler = new RequestPermissionHandler();
        tvForgot = findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(this);
        ivShowPassword = findViewById(R.id.ivShowPw);

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("Authenticating store executive. Please wait...");
        mProgressDialog.setCancelable(false);

        edt_email_id = (EditText) findViewById(R.id.etEmail);
        edt_password = (EditText) findViewById(R.id.etPassword);
        btn_login = (Button) findViewById(R.id.btLogin);

        edt_email_id.setTextSize(TypedValue.COMPLEX_UNIT_PX, 11f * AppApplication.dip);
        edt_password.setTextSize(TypedValue.COMPLEX_UNIT_PX, 11f * AppApplication.dip);
        btn_login.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14f * AppApplication.dip);

        btn_login.setOnClickListener(this);
        ivShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPassword == false) {
                    showPassword = true;
                    ivShowPassword.setImageResource(R.drawable.ic_eye);
                    edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                } else {
                    showPassword = false;
                    ivShowPassword.setImageResource(R.drawable.ic_eye_close);
                    edt_password.setInputType(129);

                }
            }
        });
    }


    public void getAddress(Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(LoginActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                address = add;
                Log.v("IGA", "Address" + add);
            }
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocationManager service = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled)
            enabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!enabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Device GPS is not enabled")
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(LoginActivity.this, "Please allow Location Permission", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        }
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);
            if (lat != 0.0) {
                getAddress(lat, lng);
            }


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }


    private boolean isValid(View v) {
        if (edt_email_id.getText().toString().length() == 0) {
            //_editTextMobile.setError("Provide Username");
            showAlert(v, "Provide Email", false);
            edt_email_id.requestFocus();
            tvForgot.setVisibility(View.VISIBLE);
            return false;
        }
        if (!Constants.isEmailValid(edt_email_id.getText().toString())) {
            //_editTextMobile.setError("Provide Valid email");
            showAlert(v, "Provide Valid email", false);
            edt_email_id.requestFocus();
            tvForgot.setVisibility(View.VISIBLE);
            return false;
        }
        if (edt_password.getText().toString().length() == 0) {
            //edt_password.setError("Provide Password");
            showAlert(v, "Provide Password", false);
            edt_password.requestFocus();
            tvForgot.setVisibility(View.VISIBLE);
            return false;
        }


        return true;
    }


    public void setLogin(final View v) {

        String Manufacturer_value = Build.MANUFACTURER;
        String Brand_value = Build.BRAND;
        String Model_value = Build.MODEL;
        String Board_value = Build.BOARD;
        String Hardware_value = Build.HARDWARE;
        String Serial_nO_value = Build.SERIAL;

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        String BootLoader_value = Build.BOOTLOADER;
        String User_value = Build.USER;
        String Host_value = Build.HOST;
        String Version = Build.VERSION.RELEASE;
        String API_level = Build.VERSION.SDK_INT + "";
        String Build_ID = Build.ID;
        String Build_Time = Build.TIME + "";
        String Fingerprint = Build.FINGERPRINT;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //   String imei= telephonyManager.getDeviceId();
        Log.e("Device_data", Manufacturer_value + Brand_value + Model_value + Board_value + Hardware_value + Serial_nO_value + android_id + BootLoader_value + API_level + Version + Build_ID + "   ");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Manufacturer_value", Manufacturer_value);
            jsonObject.put("Brand_value", Brand_value);
            jsonObject.put("Model_value", Model_value);
            jsonObject.put("Board_value", Board_value);
            jsonObject.put("Hardware_value", Hardware_value);
            jsonObject.put("Serial_nO_value", Serial_nO_value);
            jsonObject.put("android_id", android_id);
            jsonObject.put("API_level", API_level);
            jsonObject.put("Version", Version);
            jsonObject.put("Build_ID", Build_ID);
            //jsonObject.put("IMEI_No",imei);
            jsonObject.put("App_version", Constants.APP_VERSION);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String FCMID = null;


        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_EMAIL, edt_email_id.getText().toString().trim());
        user.addProperty(Constants.PARAM_PASSWORD, edt_password.getText().toString().trim());
        user.addProperty(Constants.PARAM_FCM_ID, sessionManager.getData(Constants.KEY_FCM_ID));
        user.addProperty(Constants.PARAM_DEVICE_INFO, jsonObject.toString());
        user.addProperty(Constants.PARAM_LATITUDE, lattitude);
        user.addProperty(Constants.PARAM_LONGITUDE, longitude);

        Log.e("FCMID", "FCMID" + sessionManager.getData(Constants.KEY_FCM_ID));
        Log.e("device_info", jsonObject.toString());
        ApiClient.getClient().create(ApiInterface.class).getUserSignin(user).enqueue(new Callback<GetUserDetails>() {
            @Override
            public void onResponse(Call<GetUserDetails> call, Response<GetUserDetails> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetails getUserDetails = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetails.getResponse());
                    if (getUserDetails.getResponseCode() == 200) {
                        //TODO  manage session here later
                        String agent_id;
                        String token;
                        String name;
                        String email;
                        String rh_id;
                        String emp_id;
                        String mobile;
                        String city;
                        String isMobileVerify;

                        agent_id = String.valueOf(getUserDetails.getData().get(0).getAgentId());
                        token = String.valueOf(getUserDetails.getData().get(0).getToken());
                        email = String.valueOf(getUserDetails.getData().get(0).getEmail());
                        name = String.valueOf(getUserDetails.getData().get(0).getName());
                        rh_id = String.valueOf(getUserDetails.getData().get(0).getRhId());
                        emp_id = getUserDetails.getData().get(0).getEmpId();
                        mobile = getUserDetails.getData().get(0).getMobile();
                        //isMobileVerify = getUserDetails.getData().get(0).getIsMobileVerify();
                        city = getUserDetails.getData().get(0).getCity();
                        sessionManager.createLogin(agent_id, token, name, email, rh_id, emp_id, mobile, city/*, isMobileVerify*/);

                        // if (isMobileVerify.equalsIgnoreCase("1")) {
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                       /* } else {
                            Intent i = new Intent(LoginActivity.this, ExecutiveMobileVerifyActivity.class);
                            startActivity(i);
                            finish();
                        }*/
                    } else {
                        showAlert(v, response.body().getResponse(), false);
                        tvForgot.setVisibility(View.VISIBLE);
                    }
                } else {
                    showAlert(v, "#errorcode :- 2011 " + getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetails> call, Throwable t) {
                mProgressDialog.dismiss();
                showAlert(v, "#errorcode   :- 2011 " + getString(R.string.something_went_wrong), false);
                // Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btLogin) {
            // Constants.hideKeyboardwithoutPopulate(LoginActivity.this);
            getPermitionGrant(v);
        }
        if (v.getId() == R.id.tvForgot) {
            Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
        }
    }


    private void getPermitionGrant(View v) {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (isValid(v)) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        gps = new GPSTracker(LoginActivity.this);
                        if (gps.canGetLocation()) {
                            Double lat = gps.getLatitude();
                            Double lng = gps.getLongitude();
                            lattitude = String.valueOf(gps.getLatitude());
                            longitude = String.valueOf(gps.getLongitude());
                            Log.e("Lattitude", lattitude);
                            Log.e("Longitude", longitude);
                            if (lat != 0.0) {
                                getAddress(lat, lng);
                            }

                            setLogin(v);

                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }


                    } else {
                        Constants.ShowNoInternet(LoginActivity.this);
                    }
                }
            }

            @Override
            public void onFailed() {

                Toast.makeText(getApplicationContext(), "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
