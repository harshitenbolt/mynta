package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.Beans.GetVehicleTypes;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDeliveryBoySSActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etCurrentShopNo, etCurrentStreet, etCurrentLandmark, etCurrentPincode, etCurrentCity, etCurrentState, etPermShopNo, etPerStreet, etPerLandmark, etPerPincode, etPerCity, etPerState, etDexter, etRouteNo;
    Spinner spVehicleForDelivery, dc;
    ProgressDialog mProgressDialog;
    String str_process_id = "";
    Datum datum;
    Button btSubmit;
    String delivery_boy_id = "";
    private String lattitude = "", longitude = "", currentAdress = "", permAddress = "";
    GPSTracker gps;
    private CheckBox cbSame;
    private ArrayList<String> dcLists = new ArrayList<>();
    private SessionManager sessionManager;
    private RequestPermissionHandler requestPermissionHandler;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy_ss);
        requestPermissionHandler = new RequestPermissionHandler();
        mProgressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        delivery_boy_id = getIntent().getStringExtra("delivery_boy_id");
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            APiCallGetDeliveryBoyDeetails();
        } else {
            Constants.ShowNoInternet(EditDeliveryBoySSActivity.this);
        }

        init();
    }


    private void init() {
        etCurrentShopNo = findViewById(R.id.etCurrentShopNo);
        etCurrentStreet = findViewById(R.id.etCurrentStreet);
        etCurrentLandmark = findViewById(R.id.etCurrentLandmark);
        etCurrentPincode = findViewById(R.id.etCurrentPincode);
        etCurrentCity = findViewById(R.id.etCurrentCity);
        etCurrentState = findViewById(R.id.etCurrentState);
        etPermShopNo = findViewById(R.id.etPermShopNo);
        etPerStreet = findViewById(R.id.etPerStreet);
        etPerLandmark = findViewById(R.id.etPerLandmark);
        etPerPincode = findViewById(R.id.etPerPincode);
        etPerCity = findViewById(R.id.etPerCity);
        etPerState = findViewById(R.id.etPerState);
        etDexter = findViewById(R.id.etDexter);
        etRouteNo = findViewById(R.id.etRouteNo);
        spVehicleForDelivery = findViewById(R.id.spVehicleForDelivery);
        dc = findViewById(R.id.dc);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        cbSame = findViewById(R.id.cbSameAsAbove);

        etCurrentPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC(s.toString());
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoySSActivity.this);
                    }
                    //addDC(s.toString());
                }
            }
        });

        etPerPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC1(s.toString());
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoySSActivity.this);
                    }
                    //addDC(s.toString());
                }
            }
        });


    }


    private void APiCallGetDeliveryBoyDeetails() {

        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, delivery_boy_id);

        Call<DeliveryboyListResponse> call = ApiClient.getClient().create(ApiInterface.class).getDelivery_boys("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DeliveryboyListResponse>() {
            @Override
            public void onResponse(Call<DeliveryboyListResponse> call, Response<DeliveryboyListResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    DeliveryboyListResponse deliveryboyListResponse = response.body();
                    if (deliveryboyListResponse.getResponseCode() == 200) {
                        datum = deliveryboyListResponse.getData().get(0);
                        etCurrentShopNo.setText(datum.getCurrentAddress());
                        etCurrentStreet.setText(datum.getCurrentAddress1());
                        etCurrentLandmark.setText(datum.getCurrentAddressLandmark());
                        etCurrentPincode.setText(datum.getCurrentAddressPicode());
                        addDC(etCurrentPincode.getText().toString());
                        etCurrentCity.setText(datum.getCurrentAddressCity());
                        etCurrentState.setText(datum.getCurrentAddressState());

                        etPermShopNo.setText(datum.getPermanentResidentialAddress());
                        etPerStreet.setText(datum.getPermanentResidentialAddress1());
                        etPerLandmark.setText(datum.getPermanentResidentialAddressLandmark());
                        etPerPincode.setText(datum.getPermanentResidentialAddressPicode());
                        etPerCity.setText(datum.getPermanentResidentialAddressCity());
                        etPerState.setText(datum.getPermanentResidentialAddressState());
                        etDexter.setText(datum.getDexter());
                        etRouteNo.setText(datum.getRouteNumber());
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            APiCallgetvehicleNames();
                        } else {
                            Constants.ShowNoInternet(EditDeliveryBoySSActivity.this);
                        }


                        cbSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                if (b) {

                                    etPermShopNo.setText(etCurrentShopNo.getText().toString().trim());
                                    etPerStreet.setText(etCurrentStreet.getText().toString().trim());
                                    etPerLandmark.setText(etCurrentLandmark.getText().toString().trim());
                                    etPerPincode.setText(etCurrentPincode.getText().toString().trim());
                                    etPerCity.setText(etCurrentCity.getText().toString().trim());
                                    etPerState.setText(etCurrentState.getText().toString().trim());

                                } else {

                                }
                            }
                        });


                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryboyListResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void addDC(String pcode) {
        // state is DC and DC is state

        dcLists.clear();
        mProgressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }
                            etCurrentState.setText(getUserDetails.getData().get(i).getState());
                            etCurrentCity.setText(getUserDetails.getData().get(i).getCity());

                        }
                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoySSActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);


                        for (int i = 0; i < dcLists.size(); i++) {
                            if (!datum.getDc().equalsIgnoreCase("")) {
                                if (datum.getDc().equalsIgnoreCase(dcLists.get(i))) {
                                    dc.setSelection(i);
                                }
                            }
                        }

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditDeliveryBoySSActivity.this);
                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OldDC(String pcode) {
        // state is DC and DC is state

        dcLists.clear();
        mProgressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }
                            etCurrentState.setText(getUserDetails.getData().get(i).getState());
                            etCurrentCity.setText(getUserDetails.getData().get(i).getCity());

                        }
                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoySSActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);

                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditDeliveryBoySSActivity.this);
                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void addDC1(String pcode) {
        // state is DC and DC is state

        //dcLists.clear();
        mProgressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                          /*  for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }*/
                            etPerState.setText(getUserDetails.getData().get(i).getState());
                            etPerCity.setText(getUserDetails.getData().get(i).getCity());

                        }
                       /* CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoySSActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);*/

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditDeliveryBoySSActivity.this);
                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode :- 2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                if (validation()) {
                    ApiCallSubmit();
                }
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    class CustomAdapter<T> extends ArrayAdapter<T> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<T> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(12);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.rb_regular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

    private void APiCallgetvehicleNames() {
        mProgressDialog.show();
        Call<GetVehicleTypes> call = ApiClient.getClient().create(ApiInterface.class).getVehicleListing("Bearer " + sessionManager.getToken());
        call.enqueue(new Callback<GetVehicleTypes>() {
            @Override
            public void onResponse(Call<GetVehicleTypes> call, Response<GetVehicleTypes> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetVehicleTypes getVehicleTypes = response.body();
                    if (getVehicleTypes.getResponseCode() == 200) {
                        List<String> items = Arrays.asList(getVehicleTypes.getData().split("\\s*,\\s*"));
                        EditDeliveryBoySSActivity.CustomAdapter<String> spinnerArrayAdapter = new EditDeliveryBoySSActivity.CustomAdapter<String>(EditDeliveryBoySSActivity.this, android.R.layout.simple_spinner_item, items);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spVehicleForDelivery.setAdapter(spinnerArrayAdapter);
                        for (int i = 0; i < items.size(); i++) {
                            if (datum.getVehicleForDelivery().equalsIgnoreCase(items.get(i))) {
                                spVehicleForDelivery.setSelection(i);
                            }
                        }


                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, getVehicleTypes.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode 2116" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetVehicleTypes> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, "#errorcode 2116" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });


    }


    private boolean validation() {

        if (TextUtils.isEmpty(etCurrentShopNo.getText().toString())) {
            etCurrentShopNo.requestFocus();
            etCurrentShopNo.setError("Provide Number");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etCurrentStreet.getText().toString())) {
            etCurrentStreet.requestFocus();
            etCurrentStreet.setError("Provide Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentLandmark.getText().toString())) {
            etCurrentLandmark.requestFocus();
            etCurrentLandmark.setError("Provide Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etCurrentPincode.getText().toString())) {
            etCurrentPincode.requestFocus();
            etCurrentPincode.setError("Provide Pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (etCurrentPincode.getText().length() != 6) {
            etCurrentPincode.requestFocus();
            etCurrentPincode.setError("Provide Valid Pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentCity.getText().toString())) {
            etCurrentCity.requestFocus();
            etCurrentCity.setError("Provide City");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etCurrentState.getText().toString())) {
            etCurrentState.requestFocus();
            etCurrentState.setError("Provide State");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermShopNo.getText().toString())) {
            etPermShopNo.requestFocus();
            etPermShopNo.setError("Provide Number");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPerStreet.getText().toString())) {
            etPerStreet.requestFocus();
            etPerStreet.setError("Provide Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPerLandmark.getText().toString())) {
            etPerLandmark.requestFocus();
            etPerLandmark.setError("Provide Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPerPincode.getText().toString())) {
            etPerPincode.requestFocus();
            etPerPincode.setError("Provide Pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPerCity.getText().toString())) {
            etPerCity.requestFocus();
            etPerCity.setError("Provide City");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPerState.getText().toString())) {
            etPerState.requestFocus();
            etPerState.setError("Provide State");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etRouteNo.getText().toString())) {
            etRouteNo.requestFocus();
            etRouteNo.setError("Provide Route");
            // showMSG(false, "Provide Pincode");
            return false;
        }


        return true;
    }


    private void ApiCallSubmit() {
        Call<AddDelBoyResponse> callUpload = null;
        gps = new GPSTracker(EditDeliveryBoySSActivity.this);
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        mProgressDialog.show();
        MultipartBody.Part prof_image = null;
        MultipartBody.Part adhar_front_image = null, adhar_back_image = null, voter_front_image = null, voter_back_image = null;

        MultipartBody.Part license_image = null;
        String image = "image";
        String driving_licence = "driving_licence_image";

        //   list.add(shop_act_part);
        currentAdress = etCurrentShopNo.getText().toString() + " " + etCurrentStreet.getText().toString() + " " + etCurrentLandmark.getText().toString() + " " + etCurrentCity.getText().toString() + " " + etCurrentState.getText().toString() + " " + etCurrentPincode.getText().toString();
        permAddress = etPermShopNo.getText().toString() + " " + etPerStreet.getText().toString() + " " + etPerLandmark.getText().toString() + " " + etPerCity.getText().toString() + " " + etPerState.getText().toString() + " " + etPerPincode.getText().toString();


        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_CURRENT_RESIDENTIAL, currentAdress);
        params.put(Constants.PARAM_CURRENT_ADDRESS, etCurrentShopNo.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS1, etCurrentStreet.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_LANDMARK, etCurrentLandmark.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_CITY, etCurrentCity.getText().toString());
        params.put("dexter", etDexter.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_STATE, etCurrentState.getText().toString());
        params.put(Constants.PARAM_CURRENT_ADDRESS_PINCODE, etCurrentPincode.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS, etPermShopNo.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS1, etPerStreet.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_LANDMARK, etPerLandmark.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_CITY, etPerCity.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_STATE, etPerState.getText().toString());
        params.put(Constants.PARAM_PERMANENT_RESIDENTIAL_ADDRESS_PINCODE, etPerPincode.getText().toString());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, delivery_boy_id);
        params.put(Constants.PARAM_DRIVING_LICENCE_VEHICLE, spVehicleForDelivery.getSelectedItem().toString());
        params.put(Constants.PARAM_PERMANENT_ADDRESS, permAddress);
        params.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
        if (etRouteNo.getText().toString().contains("R-")) {
            params.put(Constants.ROUTE_NUMBER, etRouteNo.getText().toString());
        } else {
            params.put(Constants.ROUTE_NUMBER, "R-" + etRouteNo.getText().toString());
        }
        /*if(isUpdate.equalsIgnoreCase("1")){
            params.put(Constants.PARAM_IS_EDIT,"1");
        }*/

        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);

        callUpload = ApiClient.getClient().create(ApiInterface.class).edidelivery2("Bearer " + sessionManager.getToken(), params);

        // Log.e("image",list.toString());

        callUpload.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                try {
                    mProgressDialog.dismiss();
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoySSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();

                        finish();
                    } else if (addDelBoyResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditDeliveryBoySSActivity.this);
                    } else if (addDelBoyResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (addDelBoyResponse.getValidation() != null) {
                            AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                            if (validation.getImage() != null && validation.getImage().length() > 0) {
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
                                finish();
                            }

                            if (validation.getCurrent_residential_address() != null && validation.getCurrent_residential_address().length() > 0) {
                                etCurrentShopNo.setError(validation.getCurrent_residential_address());
                                etCurrentShopNo.requestFocus();
                            }
                            if (validation.getPermanent_address() != null && validation.getPermanent_address().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermShopNo.setError(validation.getPermanent_address());
                                etPermShopNo.requestFocus();

                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getRoute_number() != null && validation.getRoute_number().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etRouteNo.setError(validation.getRoute_number());
                                etRouteNo.requestFocus();

                            }


                            if (validation.getDriving_licence_image() != null && validation.getDriving_licence_image().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVehicle_for_delivery(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getDriving_licence_issue_date() != null && validation.getDriving_licence_issue_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getDriving_licence_issue_date(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getDriving_licence_till_date() != null && validation.getDriving_licence_till_date().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getDriving_licence_till_date(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getAadhaar_card_front() != null && validation.getAadhaar_card_front().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAadhaar_card_front(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_card_back() != null && validation.getAadhaar_card_back().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAadhaar_card_back(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_name() != null && validation.getAadhaar_name().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAadhaar_name(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_dob() != null && validation.getAadhaar_dob().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAadhaar_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getAadhaar_no() != null && validation.getAadhaar_no().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAadhaar_no(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_front_image() != null && validation.getVoter_front_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVoter_front_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_back_image() != null && validation.getVoter_back_image().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVoter_back_image(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoter_name() != null && validation.getVoter_name().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVoter_name(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getVoter_dob() != null && validation.getVoter_dob().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVoter_dob(), Toast.LENGTH_LONG).show();
                            }

                            if (validation.getVoterIdNum() != null && validation.getVoterIdNum().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getVoterIdNum(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoySSActivity.this, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditDeliveryBoySSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(EditDeliveryBoySSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(EditDeliveryBoySSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                        if (addDelBoyResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditDeliveryBoySSActivity.this);
                        } else {

                            Toast.makeText(EditDeliveryBoySSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoySSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoySSActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoySSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }


}
