package com.canvascoders.opaper.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.GetAgentDetailResponse.GetAgentDetailResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;
import static com.canvascoders.opaper.fragment.PanVerificationFragment.CROPPED_IMAGE;
import static com.canvascoders.opaper.utils.Constants.showAlert;

public class AddNewDeliveryBoy extends AppCompatActivity implements View.OnClickListener {

    private EditText etPincode,etName, etFatherName, etPhoneNumber, etCurrentAddress, etPermanentAddress, etRoute, etDrivingNumber, etDob, etVehicle;
    private ImageView ivProfile, ivDriving_Licence,ivBack;
    private String profileImagepath = "", licenceImagePath = "";
    private int PROFILEIMAGE = 200, LICENCEIMAGE = 300;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addDelBoy;
    private RelativeLayout rvLanguage;
    private ProgressDialog mProgressDialog;
    Spinner dc;
    String str_process_id;
    private ArrayList<String> dcLists = new ArrayList<>();
    private SessionManager sessionManager;
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ArrayList<String> listLanaguage = new ArrayList<>();
    boolean[] checkedItems;
    private TextView tvLanguage, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_delivery_boy);
        init();
    }

    private void init() {
        dc = (Spinner)findViewById(R.id.dc);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        sessionManager = new SessionManager(this);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        addDelBoy = findViewById(R.id.btSubmitDelBoy);
        addDelBoy.setOnClickListener(this);
        etName = findViewById(R.id.etName);
        etPincode = findViewById(R.id.etPincode);
        etFatherName = findViewById(R.id.etFatherName);
        etPhoneNumber = findViewById(R.id.etPhone);
        etCurrentAddress = findViewById(R.id.etCurrentAdd);
        etPermanentAddress = findViewById(R.id.etPermAdd);
        etRoute = findViewById(R.id.etRouteNo);
        etDrivingNumber = findViewById(R.id.etDrivingLicenceNum);
        etVehicle = findViewById(R.id.etVehicle);
        dob = findViewById(R.id.tvDrivingLicenceDOB);
        dob.setOnClickListener(this);

        ivProfile = findViewById(R.id.ivProfileImage);
        ivDriving_Licence = findViewById(R.id.ivDrivingLicence);
        ivDriving_Licence.setOnClickListener(this);
        rvLanguage = findViewById(R.id.rvSelectLanguage);
        rvLanguage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        checkedItems = new boolean[select_language.length];
        tvLanguage = findViewById(R.id.tvLanguage);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);


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
                    addDC(s.toString());
                }
            }
        });

        //ApiCallGetDc();
    }

   /* private void ApiCallGetDc() {

        Call<GetAgentDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).getAgentDetails("Bearer " + sessionManager.getToken(), sessionManager.getAgentID());
        call.enqueue(new Callback<GetAgentDetailResponse>() {
            @Override
            public void onResponse(Call<GetAgentDetailResponse> call, Response<GetAgentDetailResponse> response) {
                try {

                    GetAgentDetailResponse getAgentDetailResponse = response.body();
                    if (getAgentDetailResponse.getResponseCode() == 200) {
                        dc = getAgentDetailResponse.getData().get(0).getDc();
                        tvDc.setText(dc);
                    } else {
                        Toast.makeText(AddNewDeliveryBoy.this, getAgentDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(AddNewDeliveryBoy.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetAgentDetailResponse> call, Throwable t) {
                Toast.makeText(AddNewDeliveryBoy.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/


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

                       CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(AddNewDeliveryBoy.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(AddNewDeliveryBoy.this);
                    } else {
                        Toast.makeText(AddNewDeliveryBoy.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddNewDeliveryBoy.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfileImage:
                captureImage(1);
                break;
            case R.id.ivDrivingLicence:
                captureImage(2);
                break;

            case R.id.rvSelectLanguage:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();


                break;
            case R.id.tvDrivingLicenceDOB:
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

                                dob.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();

            case R.id.btSubmitDelBoy:
                Constants.hideKeyboardwithoutPopulate(this);


                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation()) ApiCallSubmit();
                } else {
                    Constants.ShowNoInternet(this);
                }

                break;
            case R.id.ivBack:
                finish();
                break;

        }

    }

    private void ApiCallSubmit() {
        mProgressDialog.show();
        MultipartBody.Part prof_image = null;
        MultipartBody.Part license_image = null;
        String image = "image";
        String driving_licence = "driving_licence_image";

        File imagefile1 = new File(profileImagepath);
        prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
        //   list.add(shop_act_part);


        File imagefile2 = new File(licenceImagePath);
        license_image = MultipartBody.Part.createFormData(driving_licence, imagefile2.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(licenceImagePath)), imagefile2));

        // Log.e("image",list.toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.NAME, etName.getText().toString().trim());
        params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
        params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString().trim());
        params.put(Constants.PARAM_CURRENT_RESIDENTIAL, etCurrentAddress.getText().toString().trim());
        params.put(Constants.PARAM_PERMANENT_ADDRESS, etPermanentAddress.getText().toString().trim());
        params.put(Constants.PARAM_DC,""+dc.getSelectedItem());
        params.put(Constants.ROUTE_NUMBER, etRoute.getText().toString().trim());
        params.put(Constants.PARAM_DRIVING_LICENCE_NUM, etDrivingNumber.getText().toString().trim());
        params.put(Constants.PARAM_DRIVING_LICENCE_DOB, dob.getText().toString().trim());
        params.put(Constants.PARAM_DRIVING_LICENCE_VEHICLE, etVehicle.getText().toString().trim());
        params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());

        Call<AddDelBoyResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).addDelBoys("Bearer " + sessionManager.getToken(), params, prof_image, license_image);
        callUpload.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                try {
                    mProgressDialog.dismiss();
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(AddNewDeliveryBoy.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (addDelBoyResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (addDelBoyResponse.getValidation() != null) {
                            AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                            if (validation.getImage() != null && validation.getImage().length() > 0) {
                                Toast.makeText(AddNewDeliveryBoy.this, validation.getImage(), Toast.LENGTH_LONG).show();
                                 finish();
                            }
                            if (validation.getName() != null && validation.getName().length() > 0) {
                                etName.setError(validation.getName());
                                etName.requestFocus();
                            }
                            if (validation.getFather_name() != null && validation.getFather_name().length() > 0) {
                                etFatherName.setError(validation.getName());
                                etFatherName.requestFocus();
                            }
                            if (validation.getPhoneNumber() != null && validation.getPhoneNumber().length() > 0) {

                                etPhoneNumber.setError(validation.getPhoneNumber());
                                etPhoneNumber.requestFocus();
                            }
                            if (validation.getCurrent_residential_address() != null && validation.getCurrent_residential_address().length() > 0) {
                                etCurrentAddress.setError(validation.getCurrent_residential_address());
                                etCurrentAddress.requestFocus();
                            }
                            if (validation.getPermanent_address() != null && validation.getPermanent_address().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentAddress.setError(validation.getPermanent_address());
                                etPermanentAddress.requestFocus();

                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(AddNewDeliveryBoy.this, validation.getDc(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getRoute_number() != null && validation.getRoute_number().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etRoute.setError(validation.getRoute_number());
                                etRoute.requestFocus();

                            }
                            if (validation.getDriving_licence_num() != null && validation.getDriving_licence_num().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etDrivingNumber.setError(validation.getDriving_licence_num());
                                etDrivingNumber.requestFocus();
                            }
                            if (validation.getDriving_licence_dob() != null && validation.getDriving_licence_dob().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etDob.setError(validation.getDriving_licence_dob());
                                etDob.requestFocus();
                            }
                            if (validation.getDriving_licence_image() != null && validation.getDriving_licence_image().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(AddNewDeliveryBoy.this, validation.getDriving_licence_image(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getVehicle_for_delivery() != null && validation.getVehicle_for_delivery().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etVehicle.setError(validation.getDriving_licence_dob());
                                etVehicle.requestFocus();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(AddNewDeliveryBoy.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AddNewDeliveryBoy.this, addDelBoyResponse.getResponse(),Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(AddNewDeliveryBoy.this, addDelBoyResponse.getResponse(),Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(AddNewDeliveryBoy.this, addDelBoyResponse.getResponse(),Toast.LENGTH_LONG).show();
                        if (addDelBoyResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(AddNewDeliveryBoy.this);
                        }
                    else {

                        Toast.makeText(AddNewDeliveryBoy.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                    }
                }catch (Exception e){
                    Toast.makeText(AddNewDeliveryBoy.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });

    }

    private void captureImage(int i) {
        if (i == 1) {
            Intent chooseImageIntent = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent, PROFILEIMAGE);
        }
        if (i == 2) {
            Intent chooseImageIntent = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent, LICENCEIMAGE);
        }
    }


    private boolean validation() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.requestFocus();
            etName.setError("Provide Name");

            // showMSG(false, "dProvide Email");
            return false;
        }


        if (TextUtils.isEmpty(etFatherName.getText().toString())) {
            etFatherName.requestFocus();
            //showMSG(false, "Provide Store name");
            etFatherName.setError("Provide Father name");
            return false;
        }
        if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError("Provide store address");
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etCurrentAddress.getText().toString())) {
            etCurrentAddress.requestFocus();
            etCurrentAddress.setError("Provide pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPincode.getText().toString())) {
            etPincode.requestFocus();
            etPincode.setError("Provide pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermanentAddress.getText().toString())) {
            etPermanentAddress.requestFocus();
            etPermanentAddress.setError("Provide permanent address");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (dob.getText().equals("Date of Birth")) {
            Toast.makeText(this, "Provide Date of Birth", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etRoute.getText().toString())) {
            etRoute.requestFocus();
            etRoute.setError("Provide Route");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etDrivingNumber.getText().toString())) {
            etDrivingNumber.requestFocus();
            etDrivingNumber.setError("Provide Driving Licence Number");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etVehicle.getText().toString())) {
            etVehicle.requestFocus();
            etVehicle.setError("Provide Vehicle Name");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (listLanaguage.size() == 0) {

            Toast.makeText(this, "Please Select Language", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (profileImagepath.equals("")) {
            Toast.makeText(this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (licenceImagePath.equals("")) {
            Toast.makeText(this, "Please Select Licence Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LICENCEIMAGE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                licenceImagePath = ImagePicker.getBitmapPath(bitmap, this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(this).load(licenceImagePath).into(ivDriving_Licence);

            }
            if (requestCode == PROFILEIMAGE) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                profileImagepath = ImagePicker.getBitmapPath(bitmap, this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(this).load(profileImagepath).into(ivProfile);

            }
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

}
