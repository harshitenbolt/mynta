package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.Beans.GetStoreTypeResponse;
import com.canvascoders.opaper.Beans.SendOTPDelBoyResponse.SendOtpDelBoyresponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditDeliveryBoyFSActivity extends AppCompatActivity implements View.OnClickListener {
    Datum datum;
    private RequestPermissionHandler requestPermissionHandler;
    String str_process_id = "";
    ImageView ivProfile;
    String[] selectBloodGroupType = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    RadioGroup radioSexGroup;
    GPSTracker gps;
    private RadioButton radioSexButton;
    private String profileImagepath = "";
    private SessionManager sessionManager;
    private String lattitude = "", longitude = "", currentAdress = "", permAddress = "";
    Spinner spVehicleForDelivery, spBloodGroupType,spStoreType;
    EditText etName, etFatherName, etPhoneNumber, etOtp;
    private RelativeLayout rvLanguage;
    ArrayList<String> listLanaguage = new ArrayList<>();
    boolean[] checkedItems;
    Button btSubmit;
    Button btGetOtp;
    List<String> storeList = new ArrayList<>();
    private int PROFILEIMAGE = 200, LICENCEIMAGE = 300;
    CustomAdapter<String> spinnerArrayAdapter;
    private TextView tvLanguage;
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ProgressDialog mProgressDialog;
    String otp = "", mobile_number = "";
    String delivery_boy_id = "";
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy_fs);
        requestPermissionHandler = new RequestPermissionHandler();
        mProgressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        delivery_boy_id = getIntent().getStringExtra("delivery_boy_id");
        init();
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            APiCallGetDeliveryBoyDeetails();
            ApiCallgetSToreType();
        } else {
            Constants.ShowNoInternet(EditDeliveryBoyFSActivity.this);
        }


    }


    private void ApiCallgetSToreType() {
        mProgressDialog.show();
        Call<GetStoreTypeResponse> call = ApiClient.getClient().create(ApiInterface.class).getStoreTypeListforDl("Bearer " + sessionManager.getToken());
        call.enqueue(new Callback<GetStoreTypeResponse>() {
            @Override
            public void onResponse(Call<GetStoreTypeResponse> call, Response<GetStoreTypeResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetStoreTypeResponse getVehicleTypes = response.body();
                    if (getVehicleTypes.getResponseCode() == 200) {
                        storeList = getVehicleTypes.getData();
                        CustomAdapter<String> spinnnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoyFSActivity.this, android.R.layout.simple_spinner_item, storeList);
                        spinnnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spStoreType.setAdapter(spinnnerArrayAdapter);
                        spStoreType.setSelection(0);
                    } else {
                        Toast.makeText(EditDeliveryBoyFSActivity.this, getVehicleTypes.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode 2126" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetStoreTypeResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode 2126" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

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

                        if (datum.getImage() != null && !datum.getImage().equalsIgnoreCase("")) {
                            ivProfile.setPadding(0, 0, 0, 0);


                            // Bitmap bitmapFromUrl = getBitmapFromURL(Constants.BaseImageURL + datum.getImage());

                            new getBitmapFromURL().execute(Constants.BaseImageURL + datum.getImage());
                            // resizedBitmap = ImagePicker.getResizedBitmap(bitmapFromUrl, 400, 400);


                            Glide.with(EditDeliveryBoyFSActivity.this).load(Constants.BaseImageURL + datum.getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(EditDeliveryBoyFSActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivProfile.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                        if (!datum.getGender().equalsIgnoreCase("")) {
                            if (datum.getGender().equalsIgnoreCase("Female")) {
                                radioSexGroup.check(R.id.radioFemale);
                            } else {
                                radioSexGroup.check(R.id.radioMale);
                            }
                        }

                        etName.setText(datum.getName());
                        etFatherName.setText(datum.getFatherName());
                        if (datum.getBlood_group() != null && !datum.getBlood_group().equalsIgnoreCase("")) {
                            int spinnerPosition = spinnerArrayAdapter.getPosition(datum.getBlood_group());
                            spBloodGroupType.setSelection(spinnerPosition);
                        }
                        tvLanguage.setText(datum.getLanguages());
                        etPhoneNumber.setText(datum.getPhoneNumber());


                    } else {
                        Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryboyListResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void init() {


        mProgressDialog.setCancelable(false);
        ivProfile = findViewById(R.id.ivProfileImage);
        ivProfile.setOnClickListener(this);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        etName = findViewById(R.id.etName);
        etFatherName = findViewById(R.id.etFatherName);
        spBloodGroupType = findViewById(R.id.spBloodGroup);
        spStoreType = findViewById(R.id.spStoreType);
        List<String> stringList = new ArrayList<String>(Arrays.asList(selectBloodGroupType));

        spinnerArrayAdapter = new CustomAdapter<String>(EditDeliveryBoyFSActivity.this, android.R.layout.simple_spinner_item, stringList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGroupType.setAdapter(spinnerArrayAdapter);
        spBloodGroupType.setSelection(0);
        rvLanguage = findViewById(R.id.rvSelectLanguage);
        rvLanguage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        checkedItems = new boolean[select_language.length];
        tvLanguage = findViewById(R.id.tvLanguage);
        etOtp = findViewById(R.id.etOtp);
        etPhoneNumber = findViewById(R.id.etPhone);
        btGetOtp = findViewById(R.id.btGetOtp);

        btGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        APiCallCheckMobileNumber();
                    } else {
                        Constants.ShowNoInternet(EditDeliveryBoyFSActivity.this);
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

            case R.id.btSubmit:
                if (validation()) {
                    ApiCallSubmit();
                }

                break;


            case R.id.ivProfileImage:
                captureImage(1);
                break;
            case R.id.ivBack:
                finish();
                break;
        }

    }

    private boolean validation() {
        if (profileImagepath.equals("")) {
            Toast.makeText(this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
            return false;
        }
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
            etPhoneNumber.setError("Provide Phone number");
            //showMSG(false, "Provide Store address");
            return false;
        }


        if (!etPhoneNumber.getText().toString().equalsIgnoreCase(datum.getPhoneNumber())) {
            if (TextUtils.isEmpty(etOtp.getText().toString())) {
                etOtp.requestFocus();
                etOtp.setError("Provide OTP");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (!etOtp.getText().toString().equalsIgnoreCase(otp)) {
                etOtp.requestFocus();
                etOtp.setError("Provide Valid OTP");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (!etPhoneNumber.getText().toString().equalsIgnoreCase(mobile_number)) {
                etPhoneNumber.requestFocus();
                etPhoneNumber.setError("Provide Same mobile number");
                // showMSG(false, "Provide Pincode");
                return false;
            }
        }
        return true;
    }


    private boolean isValid() {
        if (etPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            etPhoneNumber.setError("Please provide mobile number");
            etPhoneNumber.requestFocus();
            return false;
        }

        if (etPhoneNumber.getText().toString().equalsIgnoreCase(datum.getPhoneNumber())) {
            Toast.makeText(EditDeliveryBoyFSActivity.this, "Please enter another mobile number", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etPhoneNumber.length() < 10) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError("Provide Valid number");
            //showMSG(false, "Provide Store address");
            return false;
        }
        return true;
    }


    private void APiCallCheckMobileNumber() {
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString());
        // params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());


        Call<SendOtpDelBoyresponse> call = ApiClient.getClient().create(ApiInterface.class).deliveryBoysSendOTP("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<SendOtpDelBoyresponse>() {
            @Override
            public void onResponse(Call<SendOtpDelBoyresponse> call, Response<SendOtpDelBoyresponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    SendOtpDelBoyresponse sendOtpDelBoyresponse = response.body();
                    if (sendOtpDelBoyresponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoyFSActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_SHORT).show();
                        otp = sendOtpDelBoyresponse.getData().get(0).getOtp();
                        mobile_number = etPhoneNumber.getText().toString();
                    } else {
                        if (sendOtpDelBoyresponse.getResponseCode() == 400) {
                            if (!sendOtpDelBoyresponse.getValidation().getPhoneNumber().equalsIgnoreCase("") && sendOtpDelBoyresponse.getValidation().getPhoneNumber() != null) {
                                btGetOtp.setError(sendOtpDelBoyresponse.getValidation().getPhoneNumber());
                                Toast.makeText(EditDeliveryBoyFSActivity.this, sendOtpDelBoyresponse.getValidation().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                //btGetOtp.requestFocus();
                            } else {
                                Toast.makeText(EditDeliveryBoyFSActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(EditDeliveryBoyFSActivity.this, sendOtpDelBoyresponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode 2076 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendOtpDelBoyresponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyFSActivity.this, "#errorcode 2076 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }

    private class getBitmapFromURL extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... urls) {

            try {
                String s = urls[0];
                java.net.URL url = new java.net.URL(s);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                profileImagepath = ImagePicker.getBitmapPath(myBitmap, EditDeliveryBoyFSActivity.this);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
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

    private void ApiCallSubmit() {
        Call<AddDelBoyResponse> callUpload = null;
        gps = new GPSTracker(EditDeliveryBoyFSActivity.this);
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

        File imagefile1 = new File(profileImagepath);
        prof_image = MultipartBody.Part.createFormData(image, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(profileImagepath)), imagefile1));
        //   list.add(shop_act_part);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
// find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.NAME, etName.getText().toString().trim());
        params.put(Constants.PARAM_FATHER_NAME, etFatherName.getText().toString().trim());
        params.put(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString().trim());
        params.put(Constants.PARAM_BLOOD_GROUP, spBloodGroupType.getSelectedItem().toString());
        params.put(Constants.PARAM_STORE_TYPE, spStoreType.getSelectedItem().toString());
        params.put(Constants.PARAM_DELIVERY_BOY_ID, delivery_boy_id);
        params.put(Constants.PARAM_LANGUAGES, tvLanguage.getText().toString().trim());
        params.put(Constants.PARAM_LATITUDE, lattitude);
        params.put(Constants.PARAM_LONGITUDE, longitude);
        params.put(Constants.PARAM_GENDER, radioSexButton.getText().toString());

        callUpload = ApiClient.getClient().create(ApiInterface.class).edidelivery1("Bearer " + sessionManager.getToken(), params, prof_image);


        // Log.e("image",list.toString());

        callUpload.enqueue(new Callback<AddDelBoyResponse>() {
            @Override
            public void onResponse(Call<AddDelBoyResponse> call, Response<AddDelBoyResponse> response) {
                try {
                    mProgressDialog.dismiss();
                    AddDelBoyResponse addDelBoyResponse = response.body();
                    if (addDelBoyResponse.getResponseCode() == 200) {
                        Toast.makeText(EditDeliveryBoyFSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        deleteImages();
                        finish();
                    } else if (addDelBoyResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditDeliveryBoyFSActivity.this);
                    } else if (addDelBoyResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (addDelBoyResponse.getValidation() != null) {
                            AddDelBoyResponse.Validation validation = addDelBoyResponse.getValidation();
                            if (validation.getImage() != null && validation.getImage().length() > 0) {
                                Toast.makeText(EditDeliveryBoyFSActivity.this, validation.getImage(), Toast.LENGTH_LONG).show();
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
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyFSActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getGender() != null && validation.getGender().length() > 0) {
                                //Toast.makeText(EditPanCardActivity,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyFSActivity.this, validation.getGender(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyFSActivity.this, validation.getProccessId(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditDeliveryBoyFSActivity.this, validation.getAgentId(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditDeliveryBoyFSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(EditDeliveryBoyFSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();


                        }

                    } else {
                        Toast.makeText(EditDeliveryBoyFSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_LONG).show();
                        if (addDelBoyResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditDeliveryBoyFSActivity.this);
                        } else {

                            Toast.makeText(EditDeliveryBoyFSActivity.this, addDelBoyResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(EditDeliveryBoyFSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    //  Toast.makeText(EditDeliveryBoyFSActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditDeliveryBoyFSActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deleteImages() {

        File casted_image6 = new File(profileImagepath);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PROFILEIMAGE) {
               /* Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                profileImagepath = ImagePicker.getBitmapPath(bitmap, this);
*/
                Uri uri = ImagePicker.getPickImageResultUri(this, data);
                //  Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                profileImagepath = ImagePicker.getPathFromUri( this,uri); // ImageUtils.getInstant().getImageUri(getActivity(), photo);


                ivProfile.setPadding(0, 0, 0, 0);// ImageUtils.getInstant().getImageUri(getActivity(), photo);

                Glide.with(this).load(profileImagepath).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(EditDeliveryBoyFSActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }
        }
    }

    private void captureImage(int i) {
        if (i == 1) {
            Intent chooseImageIntent = ImagePicker.getCameraIntent(this);
            startActivityForResult(chooseImageIntent, PROFILEIMAGE);
        }

    }


}
