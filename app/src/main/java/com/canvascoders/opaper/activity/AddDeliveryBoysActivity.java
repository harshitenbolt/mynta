package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;


import com.canvascoders.opaper.Beans.DeleteDeliveryResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.DeliveryBoysAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeliveryBoysActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener {
    // after 23 version from new lappy

    Context context;
    private static int IMAGE_SELCTION_CODE = 0;
    private static final int IMAGE_LICENCE = 101;
    private static final int IMAGE_PROFILE = 1021;
    private RecyclerView rv_deliveryboy_list;
    private DeliveryBoysAdapter deliveryBoysAdapter;
    private List<Datum> delivery_boys_list = new ArrayList<>();
    private Button btn_add_deliveryboy, btnSubmit;
    private String profImage = "";
    private ImageView iv_image, iv_driving;
    private Uri imgURI;
    private Spinner dc;
    private ArrayList<String> dcLists = new ArrayList<>();
    AlertDialog b;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView tvDob, tvLanguage;
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ArrayList<String> listLanaguage = new ArrayList<>();
    boolean[] checkedItems;
    private ProgressDialog mProgressDialog;
    String name, address, phone, area;
    private String licenceImagePath = "";
    VendorList vendor;
    SessionManager sessionManager;
    TextView tv_note;
    ImageView iv_back;
    private String str_process_id;
    private RequestPermissionHandler requestPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boys);

        mProgressDialog = new ProgressDialog(AddDeliveryBoysActivity.this);
        mProgressDialog.setCancelable(false);
        sessionManager = new SessionManager(AddDeliveryBoysActivity.this);

        //  vendor = (VendorList) getIntent().getSerializableExtra("data");
        str_process_id = getIntent().getStringExtra("data");
        requestPermissionHandler = new RequestPermissionHandler();
        initalize();
    }

    private void initalize() {
        btn_add_deliveryboy = findViewById(R.id.btn_add_boy);
        btnSubmit = findViewById(R.id.btn_submit_final);
        btnSubmit.setOnClickListener(this);
        iv_image = findViewById(R.id.iv_back);
        iv_image.setOnClickListener(this);
        tv_note = findViewById(R.id.tv_nodata);
        iv_back = findViewById(R.id.iv_back);
        btn_add_deliveryboy.setOnClickListener(this);
        rv_deliveryboy_list = findViewById(R.id.rv_deliveryboylist);
        checkedItems = new boolean[select_language.length];

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            // getBankDetails(mContext,s.toString(),processId);
            ApiCallGetLists();
        } else {
            Constants.ShowNoInternet(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_boy:

                Intent i = new Intent(this, AddNewDeliveryBoy.class);
                i.putExtra("Data", str_process_id);
                startActivity(i);
                // addBoyDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit_final:
                // apiCalladdDelBoys();
                if (delivery_boys_list.isEmpty()) {
                    Toast.makeText(AddDeliveryBoysActivity.this, "Please Add Delivery Boys !!!", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                Log.e("Done", "Done");
                break;
        }
    }

    private void apiCalladdDelBoys(EditText et_name, EditText et_phone, EditText etFatherName, EditText etRoute, EditText etCurrentAdd, EditText etPermAdd, String selectedItem, EditText etDlNumber, TextView tvDob, EditText etVehicleDel, TextView tvLanguage) {

        MultipartBody.Part prof_image = null;
        MultipartBody.Part license_image = null;
        String image = "image";
        String driving_licence = "driving_licence_image";


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            File imagefile1 = new File(profImage);
            prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profImage)), imagefile1));
            //   list.add(shop_act_part);


            File imagefile2 = new File(licenceImagePath);
            license_image = MultipartBody.Part.createFormData(driving_licence, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePath)), imagefile2));

            // Log.e("image",list.toString());

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString().trim());
            params.put(Constants.NAME, et_name.getText().toString().trim());
            params.put(Constants.PHONE_NUMBER, et_phone.getText().toString().trim());
            params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
            params.put(Constants.PARAM_CURRENT_RESIDENTIAL, etCurrentAdd.getText().toString().trim());
            params.put(Constants.PARAM_PERMANENT_ADDRESS, etPermAdd.getText().toString().trim());
            params.put(Constants.PARAM_DC, selectedItem);
            params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString().trim());
            params.put(Constants.PARAM_DRIVING_LICENCE_NUM, etDlNumber.getText().toString().trim());
            params.put(Constants.PARAM_DRIVING_LICENCE_DOB, tvDob.getText().toString().trim());
            params.put(Constants.PARAM_DRIVING_LICENCE_VEHICLE, etVehicleDel.getText().toString().trim());
            params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());
            params.put(Constants.PARAM_IS_ADD_FROM_PROFILE, "1");

            // params.put("data", jsonArray.toString());


            Call<AddDelBoyResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, license_image);
            callUpload.enqueue(new Callback<AddDelBoyResponse>() {
                @Override
                public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()) {


                        AddDelBoyResponse addDelBoyResponse = response.body();
                        if (addDelBoyResponse.getResponseCode() == 200) {
                            b.dismiss();
                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                // getBankDetails(mContext,s.toString(),processId);
                                ApiCallGetLists();
                            } else {
                                Constants.ShowNoInternet(AddDeliveryBoysActivity.this);
                            }

                            Toast.makeText(AddDeliveryBoysActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                        if (addDelBoyResponse.getResponseCode() == 411) {
                            sessionManager.logoutUser(AddDeliveryBoysActivity.this);
                        }

                        if (addDelBoyResponse.getResponseCode() == 400) {

                            mProgressDialog.dismiss();
                            if (addDelBoyResponse.getValidation() != null) {
                                AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                                if (validation.getImage() != null && validation.getImage().length() > 0) {
                                    Toast.makeText(AddDeliveryBoysActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                if (validation.getName() != null && validation.getName().length() > 0) {
                                    et_name.setError(validation.getName());
                                    et_name.requestFocus();
                                }
                                if (validation.getFather_name() != null && validation.getFather_name().length() > 0) {
                                    etFatherName.setError(validation.getName());
                                    etFatherName.requestFocus();
                                }
                                if (validation.getPhoneNumber() != null && validation.getPhoneNumber().length() > 0) {

                                    et_phone.setError(validation.getPhoneNumber());
                                    et_phone.requestFocus();
                                }
                                if (validation.getCurrent_residential_address() != null && validation.getCurrent_residential_address().length() > 0) {
                                    etCurrentAdd.setError(validation.getCurrent_residential_address());
                                    etCurrentAdd.requestFocus();
                                }
                                if (validation.getPermanent_address() != null && validation.getPermanent_address().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPermAdd.setError(validation.getPermanent_address());
                                    etPermAdd.requestFocus();

                                }
                                if (validation.getDc() != null && validation.getDc().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(AddDeliveryBoysActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();


                                }
                                if (validation.getRoute_number() != null && validation.getRoute_number().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etRoute.setError(validation.getRoute_number());
                                    etRoute.requestFocus();

                                }
                                if (validation.getDriving_licence_num() != null && validation.getDriving_licence_num().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etDlNumber.setError(validation.getDriving_licence_num());
                                    etDlNumber.requestFocus();
                                }
                                if (validation.getDriving_licence_dob() != null && validation.getDriving_licence_dob().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    tvDob.setError(validation.getDriving_licence_dob());
                                    tvDob.requestFocus();
                                }
                                if (validation.getDriving_licence_image() != null && validation.getDriving_licence_image().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(AddDeliveryBoysActivity.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etVehicleDel.setError(validation.getDriving_licence_dob());
                                    etVehicleDel.requestFocus();
                                }
                                if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(AddDeliveryBoysActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddDeliveryBoysActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(AddDeliveryBoysActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                            }

                        } else {
                            Log.e("RESP", "" + addDelBoyResponse.getResponse().toString());
                            Toast.makeText(AddDeliveryBoysActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();

                        }

                    } else {
                        Toast.makeText(AddDeliveryBoysActivity.this, "#errorcode :- 2043 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddDeliveryBoysActivity.this,"#errorcode :- 2043 "+ getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    Log.e("something_error", t.getMessage());
                }
            });
        } else {
            Constants.ShowNoInternet(AddDeliveryBoysActivity.this);
        }
    }

    private void ApiCallGetLists() {
        mProgressDialog.setTitle("Fetching delivery boys...");
        mProgressDialog.show();
        delivery_boys_list.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());


        Call<DeliveryboyListResponse> call = ApiClient.getClient().create(ApiInterface.class).getDelivery_boys("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DeliveryboyListResponse>() {
            @Override
            public void onResponse(Call<DeliveryboyListResponse> call, Response<DeliveryboyListResponse> response) {
                delivery_boys_list.clear();
                DeliveryboyListResponse deliveryBoysList = response.body();
                try {
                    if (deliveryBoysList.getResponseCode() == 200) {
                        mProgressDialog.dismiss();
//                        Toast.makeText(AddDeliveryBoysActivity.this,deliveryBoysList.getResponse(),Toast.LENGTH_SHORT).show();
                        delivery_boys_list.addAll(deliveryBoysList.getData());
                    } else if (deliveryBoysList.getResponseCode() == 411) {
                        sessionManager.logoutUser(AddDeliveryBoysActivity.this);
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(AddDeliveryBoysActivity.this, deliveryBoysList.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                    if (delivery_boys_list.isEmpty()) {
                        tv_note.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.GONE);
                    } else {
                        tv_note.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                    deliveryBoysAdapter = new DeliveryBoysAdapter(delivery_boys_list, getApplicationContext(), AddDeliveryBoysActivity.this);

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

                    rv_deliveryboy_list.setLayoutManager(horizontalLayoutManager);
                    rv_deliveryboy_list.setAdapter(deliveryBoysAdapter);


                } catch (Exception e) {
                    mProgressDialog.dismiss();



                    Toast.makeText(AddDeliveryBoysActivity.this,"#errorcode :- 2049 "+getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeliveryboyListResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddDeliveryBoysActivity.this, "No data Found", Toast.LENGTH_LONG).show();
                if (delivery_boys_list.isEmpty()) {
                    tv_note.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    tv_note.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                Log.e("something", t.getMessage());
                Toast.makeText(AddDeliveryBoysActivity.this,"#errorcode :- 2049 "+getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show();

                t.getMessage();
            }
        });

    }

    private void addBoyDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_deliveryboy_dialogue, null);
        dialogBuilder.setView(dialogView);
        final LinearLayout btnbio, btnotp;
        EditText etFatherName, etPincode, etCurrentAdd, etPermAdd, etroute, etDlNumber, etDlDOB, etVehicleDel;


        RelativeLayout rvLanguage;
        AppCompatImageView btn_close = dialogView.findViewById(R.id.btn_close);
        // AppCompatTextView btn_upload = dialogView.findViewById(R.id.btn_upload_details);
        EditText et_name = dialogView.findViewById(R.id.edit_name_boy);
        // EditText et_address = dialogView.findViewById(R.id.edit_Address_boy);
        EditText et_phone = dialogView.findViewById(R.id.edit_phone_boy);
        etFatherName = dialogView.findViewById(R.id.etFatherName);
        etPincode = dialogView.findViewById(R.id.etPincode);
        etPincode.addTextChangedListener(new TextWatcher() {
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
                        Constants.ShowNoInternet(AddDeliveryBoysActivity.this);
                    }


                }
            }
        });
        etCurrentAdd = dialogView.findViewById(R.id.etCurrentAdd);
        etPermAdd = dialogView.findViewById(R.id.etPermanentAdd);
        etroute = dialogView.findViewById(R.id.etRouteNo);
        etDlNumber = dialogView.findViewById(R.id.etDrivingLicenceNum);
        rvLanguage = dialogView.findViewById(R.id.rvSelectLanguage);
        tvDob = dialogView.findViewById(R.id.tvDrivingLicenceDOB);
        tvLanguage = dialogView.findViewById(R.id.tvLanguage);
        dc = dialogView.findViewById(R.id.dc);
        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
        rvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLanguage();
            }
        });
        etVehicleDel = dialogView.findViewById(R.id.etVehicle);
        Button bt_upload = dialogView.findViewById(R.id.btn_upload_details);
        iv_driving = dialogView.findViewById(R.id.iv_upload_driving);
        iv_image = dialogView.findViewById(R.id.iv_prof_boy);

        b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_prof_image();
            }
        });

        iv_driving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_licence_image();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });


        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidate();
            }

            private void invalidate() {
                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Please enter name");
                    return;
                }
                if (etFatherName.getText().toString().isEmpty()) {
                    etFatherName.setError("Please enter father name");
                    return;
                }
                if (et_phone.getText().toString().length() < 10) {
                    et_phone.setError("please enter valid mobile");
                    return;
                }
                if (etCurrentAdd.getText().toString().isEmpty()) {
                    etCurrentAdd.setError("please enter current address");
                    return;
                }
                if (etPermAdd.getText().toString().isEmpty()) {
                    etPermAdd.setError("please enter Permanent address");
                    return;
                }
                if (etPincode.getText().toString().isEmpty()) {
                    etPincode.setError("please enter pincode");
                    return;
                }
                if (etroute.getText().toString().isEmpty()) {
                    etroute.setError("please enter Route");
                    return;
                }
                if (etDlNumber.getText().toString().isEmpty()) {
                    etDlNumber.setError("please enter Dl Number");
                    return;
                }
                if (etDlNumber.getText().toString().isEmpty()) {
                    etDlNumber.setError("please enter Dl Number");
                    return;
                }
                if (tvDob.getText().equals("Date of Birth")) {
                    Toast.makeText(AddDeliveryBoysActivity.this, "Provide Date of Birth", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return;
                }
                if (TextUtils.isEmpty(etVehicleDel.getText().toString())) {
                    etVehicleDel.requestFocus();
                    etVehicleDel.setError("Provide Vehicle Name");
                    // showMSG(false, "Provide Pincode");
                    return;
                }
                if (listLanaguage.size() == 0) {

                    Toast.makeText(AddDeliveryBoysActivity.this, "Please Select Language", Toast.LENGTH_SHORT).show();
                    // showMSG(false, "Provide Pincode");
                    return;
                }
                if (licenceImagePath.equalsIgnoreCase("")) {
                    Toast.makeText(dialogView.getContext(), "please upload driving licence", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (profImage.equalsIgnoreCase("")) {
                    Toast.makeText(AddDeliveryBoysActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String stringdc;
                    name = et_name.getText().toString();
                    stringdc = "" + dc.getSelectedItem();
                    phone = et_phone.getText().toString();
                    mProgressDialog.setTitle("Adding Delivery Boy");
                    mProgressDialog.show();
                    apiCalladdDelBoys(et_name, et_phone, etFatherName, etroute, etCurrentAdd, etPermAdd, stringdc, etDlNumber, tvDob, etVehicleDel, tvLanguage);

                }
            }
        });
    }


    private void selectLanguage() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        mBuilder.setTitle("Select Languagaes");
        mBuilder.setMultiChoiceItems(select_language, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    listLanaguage.add(select_language[i]);
                } else {
                    listLanaguage.remove(select_language[i]);
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < listLanaguage.size(); i++) {
                    item = item + listLanaguage.get(i);
                    if (i != listLanaguage.size() - 1) {
                        item = item + ",";
                    }
                }
                if (listLanaguage.size() == 0) {
                    tvLanguage.setText("Select Language");
                } else {
                    tvLanguage.setText(item);
                }
            }
        });
        androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Date today = new Date();
        Calendar c1 = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.YEAR, -18); // Subtract 18 year
        long minDate = c.getTime().getTime(); //
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        String monthString = String.valueOf(monthOfYear + 1);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }


                        //logic for add 0 in string if date digit is on 1 only
                        String daysString = String.valueOf(dayOfMonth);
                        if (daysString.length() == 1) {
                            daysString = "0" + daysString;
                        }

                        tvDob.setText(year + "-" + monthString + "-" + daysString);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(minDate);
        datePickerDialog.show();
    }
/*

    private void ApiCallDelSingle_boyAdd() {

    }
*/


    private void capture_licence_image() {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                IMAGE_SELCTION_CODE = IMAGE_LICENCE;
                Intent chooseImageIntent = ImagePicker.getCameraIntent(AddDeliveryBoysActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_LICENCE);

            }

            @Override
            public void onFailed() {
                Toast.makeText(AddDeliveryBoysActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    // capture profile image
    private void capture_prof_image() {
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                IMAGE_SELCTION_CODE = IMAGE_PROFILE;
                Intent chooseImageIntent = ImagePicker.getCameraIntent(AddDeliveryBoysActivity.this);
                startActivityForResult(chooseImageIntent, IMAGE_PROFILE);

            }

            @Override
            public void onFailed() {
                Toast.makeText(AddDeliveryBoysActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PROFILE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                profImage = ImagePicker.getBitmapPath(bitmap, this);
                Glide.with(this).load(profImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(AddDeliveryBoysActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_image.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            if (requestCode == IMAGE_LICENCE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                licenceImagePath = ImagePicker.getBitmapPath(bitmap, this);
                Glide.with(this).load(licenceImagePath).placeholder(R.drawable.placeholder)
                        .into(iv_driving);
            }
        }
       /* if (requestCode == IMAGE_LICENCE && resultCode == Activity.RESULT_OK) {
            CropImage.activity(Uri.fromFile(new File(data.getStringExtra(MyCameraActivity.FILEURI)))).start(this);
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                CallMerekApi(data);
            }
        }*/
    }

    private void CallMerekApi(Intent data) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        Constants.hideKeyboardwithoutPopulate(this);
        imgURI = result.getUri();
        licenceImagePath = imgURI.getPath();
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

                        }

                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(AddDeliveryBoysActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(AddDeliveryBoysActivity.this);
                    } else {
                        Toast.makeText(AddDeliveryBoysActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddDeliveryBoysActivity.this,"#errorcode :- 2032 "+ getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddDeliveryBoysActivity.this, "#errorcode :- 2032 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                //  Toast.makeText(AddDeliveryBoysActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        //Toast.makeText(this, "Done Delete", Toast.LENGTH_SHORT).show();


        deletePopup(String.valueOf(delivery_boys_list.get(position).getId()));

    }

    private void deletePopup(String deliveryBoyId) {

        // Display message in dialog box if you have not internet connection
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert !");
        alertDialogBuilder.setMessage("Are you sure you want to delete ?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

                ApiCallDelete(deliveryBoyId);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ApiCallDelete(String deliveryBoyId) {
        mProgressDialog.setTitle("Delete...");
        mProgressDialog.show();
        delivery_boys_list.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, deliveryBoyId);


        Call<DeleteDeliveryResponse> call = ApiClient.getClient().create(ApiInterface.class).deleteDeliveryBoy("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DeleteDeliveryResponse>() {
            @Override
            public void onResponse(Call<DeleteDeliveryResponse> call, Response<DeleteDeliveryResponse> response) {
                mProgressDialog.dismiss();
                if(response.isSuccessful()) {
                    DeleteDeliveryResponse deleteDeliveryResponse = response.body();

                    if (deleteDeliveryResponse.getResponseCode() == 200) {
                        Toast.makeText(AddDeliveryBoysActivity.this, deleteDeliveryResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        ApiCallGetLists();
                    } else {
                        Toast.makeText(AddDeliveryBoysActivity.this, deleteDeliveryResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(AddDeliveryBoysActivity.this, "#errorcode 2071 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<DeleteDeliveryResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddDeliveryBoysActivity.this, "#errorcode 2071 "+getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                t.getMessage();
            }
        });
    }

    @Override
    public void onLongClick(View view, int position) {
        //Toast.makeText(this, "Done Edit", Toast.LENGTH_SHORT).show();


        Intent i = new Intent(AddDeliveryBoysActivity.this, EditDeliveryBoyActivity.class);
        i.putExtra(Constants.DATA, delivery_boys_list.get(position));
        startActivity(i);
    }

    @Override
    public void SingleClick(String popup, int position) {
        //Toast.makeText(this, "Done Delete", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onResume() {
        super.onResume();
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            // getBankDetails(mContext,s.toString(),processId);
            ApiCallGetLists();
        } else {
            Constants.ShowNoInternet(this);
        }

    }
}
