package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetVendorTypeDetails;
import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.adapters.CustomPopupAdapter;
import com.canvascoders.opaper.adapters.CustomPopupApproachAdapter;
import com.canvascoders.opaper.adapters.CustomPopupLocalityAdapter;
import com.canvascoders.opaper.adapters.CustomPopupShipmentAdapter;
import com.canvascoders.opaper.adapters.CustomPopupStoreTypeAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.fragment.PanVerificationFragment;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.hideKeyboardwithoutPopulate;

public class EditStoreInformationActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener {
    RequestPermissionHandler requestPermissionHandler;
    ProgressDialog mProgressDialog;
    SessionManager sessionManager;
    EditText edit_gstn, etStoreName, etStoreShopname, etStoreStreetName, etStoreLandmarl, etStorePincode, etStoreCity, etStoreState, etStoreRoute;
    List<String> select_Store_type;
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};

    String[] selectVendorType = {"Physical Shop", "Individual", "Company/Firm"};
    String[] physicalShopType = {"Kirana Store", "Mobile Shop", "Courier Booking Shop", "General Store", "Garment Shop", "Others", "Tailor Shop",
            "Stationery Shop", "Tours And Travels", "Cyber cafe", "Restaurant", "Medical Store", "Cosmetics Shop", "Automobile Shop", "Salon", "Dry Clean &amp; Laundry Shop",
            "Dairy shop", "Pan Shop", "Printing Shop", "Photo Studio", "Furniture Shop", "Footwear Shop", "REAL ESTATE OFFICE", "Bakery", "Finance enterprises",
            "Gym", "Electrical repair", "courier agency", "Jewellery shop", "Boutique", "Fancy store", "Cable network"};

    String[] individualType = {"Full Time Employed (Logistics)", "Full Time Employed (Others)", "Part Time", "Student", "Housewife", "Un-employed"};
    String[] companyType = {"Logistics Company/Firm", "Other Company/Firm"};


    String[] localityType = {"Mall", "Residential", "Commercial Market", "Tech Park", "Others"};
    String[] approachType = {"Wide for Truck", "Normal for Vehicle", "Narrow"};
    String[] shipmentType = {"Drop Model", "Self Pickup Model"};

    boolean[] checkedItems;
    private String same_address = "0", pincode;
    boolean[] checkedStoreType;
    RelativeLayout rvVendorType, rvStoreType, rvLocality, rvApproach, rvShipmentTransfer, rvVendorTypeDetail;
    private TextView tvTypeofVendor, tvStoreType, tvVendorTypeDetail, tvLocality, tvApproach, tvShipment;
    private Spinner dc;
    private String lattitude = "", longitude = "";
    List<String> listStoreType = new ArrayList<>();

    private ArrayList<String> dcLists = new ArrayList<>();
    private String TAG = "data";
    Switch ifgst;
    String shopImg = "";
    GPSTracker gps;
    Button btSubmit;
    private String isgsttn = "no";
    CustomPopupStoreTypeAdapter customPopupStoreTypeAdapter;
    CustomPopupLocalityAdapter customPopupLocalityAdapter;
    CustomPopupApproachAdapter customPopupApproachAdapter;
    CustomPopupShipmentAdapter customPopupShipmentAdapter;
    EditText etLicenceNumber;

    private Switch switchGst, switchPartner;
    private String isPartnered = "no";
    private String selectedString = "";
    EditText etRoute;
    List<ObjectPopup> listVendorType = new ArrayList<>();
    List<ObjectPopup> listLocality = new ArrayList<>();
    List<ObjectPopup> listApproach = new ArrayList<>();
    List<ObjectPopup> listShipment = new ArrayList<>();
    String str_process_id = "";
    List<ObjectPopup> ListStore_type = new ArrayList<>();
    ImageView ivStoreImage, ivStoreImageSelected;
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_information);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermissionHandler = new RequestPermissionHandler();
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Submitting Kirana details . Please wait ...");
        mProgressDialog.setCancelable(false);

        init();
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetVendorType();
        } else {
            Constants.ShowNoInternet(EditStoreInformationActivity.this);
        }


    }

    private void init() {
        etStoreName = findViewById(R.id.etStoreName);
        etStoreShopname = findViewById(R.id.etShopHouseNo);
        etStoreStreetName = findViewById(R.id.etStreetName);
        etStoreLandmarl = findViewById(R.id.etLandmark);
        etStorePincode = findViewById(R.id.etStorePincode);
        etStoreCity = findViewById(R.id.etStoreCity);
        etStoreState = findViewById(R.id.etStoreState);
        etStoreRoute = findViewById(R.id.etRouteNo);
        etLicenceNumber = findViewById(R.id.etLicenceNumber);

        rvVendorType = findViewById(R.id.rvTypeVendor);
        rvVendorType.setOnClickListener(this);
        rvStoreType = findViewById(R.id.rvStoreType);
        // etRoute = findViewById(R.id.etRouteNo);
        rvVendorTypeDetail = findViewById(R.id.rvVendorTypeDetail);
        rvApproach = findViewById(R.id.rvApproach);
        rvLocality = findViewById(R.id.rvLocality);

        rvShipmentTransfer = findViewById(R.id.rvShipment);

        tvTypeofVendor = findViewById(R.id.tvTypeofVendor);
        tvStoreType = findViewById(R.id.tvStoreType);
        tvVendorTypeDetail = findViewById(R.id.tvVendorTypeDetail);
        tvApproach = findViewById(R.id.tvApproach);
        tvShipment = findViewById(R.id.tvShipment);
        tvLocality = findViewById(R.id.tvLocality);

        dc = (Spinner) findViewById(R.id.dc);
        switchPartner = findViewById(R.id.sw_partner);

        ivStoreImageSelected = findViewById(R.id.ivStoreImageSelected);
        ivStoreImageSelected.setOnClickListener(this);

        btSubmit = findViewById(R.id.btNext);
        btSubmit.setOnClickListener(this);

        ivStoreImage = findViewById(R.id.ivStoreImage);
        ivStoreImage.setOnClickListener(this);
        switchPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                // tvPartner.setEnabled(isChecked);
                if (isChecked) {
                    isPartnered = "yes";
                } else {
                    isPartnered = "no";

                }
            }
        });
        rvShipmentTransfer.setOnClickListener(this);
        rvLocality.setOnClickListener(this);
        rvApproach.setOnClickListener(this);
        rvStoreType.setOnClickListener(this);
        rvVendorTypeDetail.setOnClickListener(this);

        /*ifgst = findViewById(R.id.switch_gst);
        ifgst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                edit_gstn.setEnabled(isChecked);
                if (isChecked) {
                    isgsttn = "yes";
                    isgsttn = "yes";
                    llGst.setVisibility(View.VISIBLE);
                } else {
                    isgsttn = "no";
                    edit_gstn.setHint("GSTN");
                    edit_gstn.setText("");
                    llGst.setVisibility(View.GONE);
                    gstPath = "";
                }
            }
        });
*/


        etStorePincode.addTextChangedListener(new TextWatcher() {
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
                        // getBankDetails(EditStoreInformationActivity.this,s.toString(),processId);
                        addDC(s.toString(), etStorePincode.getText().toString());
                    } else {
                        Constants.ShowNoInternet(EditStoreInformationActivity.this);
                    }

                }
            }
        });

    }


    private void ApiCallGetVendorType() {
        Call<GetVendorTypeDetails> call = ApiClient.getClient().create(ApiInterface.class).getVendorType("Bearer " + sessionManager.getToken(), str_process_id);
        call.enqueue(new Callback<GetVendorTypeDetails>() {
            @Override
            public void onResponse(Call<GetVendorTypeDetails> call, Response<GetVendorTypeDetails> response) {
                if (response.isSuccessful()) {
                    try {
                        GetVendorTypeDetails getVendorTypeDetails = response.body();
                        if (getVendorTypeDetails.getResponseCode() == 200) {
                           /* type_of_vendor.addAll(getVendorTypeDetails.getData());
                            CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditStoreInformationActivity.this, android.R.layout.simple_spinner_item, type_of_vendor);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTypeofVendor.setAdapter(spinnerArrayAdapter);
                            spTypeofVendor.setSelection(0);
                           */
                            select_Store_type = getVendorTypeDetails.getStoreTypeConfig();
                            checkedStoreType = new boolean[select_Store_type.size()];
                        } else if (getVendorTypeDetails.getResponseCode() == 411) {
                            sessionManager.logoutUser(EditStoreInformationActivity.this);
                        } else {
                            Toast.makeText(EditStoreInformationActivity.this, getVendorTypeDetails.getResponse(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditStoreInformationActivity.this, "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditStoreInformationActivity.this, "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetVendorTypeDetails> call, Throwable t) {
                Toast.makeText(EditStoreInformationActivity.this, "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNext:

                if (validation()) {

                    hideKeyboardwithoutPopulate(EditStoreInformationActivity.this);
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(EditStoreInformationActivity.this,s.toString(),processId);
                        bizDetailsSubmit(v);
                    } else {
                        Constants.ShowNoInternet(EditStoreInformationActivity.this);
                    }


                }
                break;

            case R.id.rvTypeVendor:

                TextView tvtitle;
                RecyclerView rvItems;
                ImageView ivClose;
                Button btSubmit;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditStoreInformationActivity.this, R.style.CustomDialog);

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogue_popup_list, null);
                mBuilder.setView(dialogView);
                tvtitle = dialogView.findViewById(R.id.tvTitleListPopup);
                tvtitle.setText("Types of Vendor");
                rvItems = dialogView.findViewById(R.id.rvListPopup);
                btSubmit = dialogView.findViewById(R.id.btSubmitDetail);

                listVendorType.clear();
                ListStore_type.clear();
                for (int i = 0; i < selectVendorType.length; i++) {
                    ObjectPopup objectPopup = new ObjectPopup(false, selectVendorType[i]);
                    listVendorType.add(objectPopup);
                }

                CustomPopupAdapter customPopupAdapter = new CustomPopupAdapter(listVendorType, EditStoreInformationActivity.this, EditStoreInformationActivity.this, "VendorType");

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditStoreInformationActivity.this, LinearLayoutManager.VERTICAL, false);

                rvItems.setLayoutManager(horizontalLayoutManager);

                rvItems.setAdapter(customPopupAdapter);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvTypeofVendor.setText(selectedString);

                        if (selectedString.equalsIgnoreCase("Physical Shop")) {
                            ListStore_type.clear();
                            ListStore_type = new ArrayList<>();
                            for (int i = 0; i < physicalShopType.length; i++) {
                                ObjectPopup objectPopup = new ObjectPopup(false, physicalShopType[i]);
                                ListStore_type.add(objectPopup);
                                tvVendorTypeDetail.setText("");
                            }
                        } else if (selectedString.equalsIgnoreCase("Individual")) {
                            ListStore_type.clear();
                            ListStore_type = new ArrayList<>();
                            for (int i = 0; i < individualType.length; i++) {
                                ObjectPopup objectPopup = new ObjectPopup(false, individualType[i]);
                                ListStore_type.add(objectPopup);
                                tvVendorTypeDetail.setText("");
                            }
                        } else if (selectedString.equalsIgnoreCase("Company/Firm")) {
                            ListStore_type.clear();
                            ListStore_type = new ArrayList<>();
                            for (int i = 0; i < companyType.length; i++) {
                                ObjectPopup objectPopup = new ObjectPopup(false, companyType[i]);
                                ListStore_type.add(objectPopup);
                                tvVendorTypeDetail.setText("");
                            }
                        }

                        mDialog.dismiss();

                    }
                });
                ivClose = dialogView.findViewById(R.id.ivClose);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });

                break;


            case R.id.rvVendorTypeDetail:

                TextView tvtitleStoreType;
                RecyclerView rvItems1;
                Button btSubmit1;
                ImageView ivClose1;
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(EditStoreInformationActivity.this, R.style.CustomDialog);

                LayoutInflater inflater1 = this.getLayoutInflater();
                View dialogView1 = inflater1.inflate(R.layout.dialogue_popup_list, null);
                mBuilder2.setView(dialogView1);
                tvtitleStoreType = dialogView1.findViewById(R.id.tvTitleListPopup);
                tvtitleStoreType.setText("Vendor Type Detail");
                rvItems1 = dialogView1.findViewById(R.id.rvListPopup);
                btSubmit1 = dialogView1.findViewById(R.id.btSubmitDetail);
                CustomPopupStoreTypeAdapter customPopupStoreTypeAdapter;
                customPopupStoreTypeAdapter = new CustomPopupStoreTypeAdapter(ListStore_type, EditStoreInformationActivity.this, EditStoreInformationActivity.this, "StoreType");

                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(EditStoreInformationActivity.this, RecyclerView.VERTICAL, false);

                rvItems1.setLayoutManager(horizontalLayoutManager1);

                rvItems1.setAdapter(customPopupStoreTypeAdapter);
                AlertDialog mDialog1 = mBuilder2.create();
                mDialog1.show();

                btSubmit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvVendorTypeDetail.setText(selectedString);


                        mDialog1.dismiss();
                        selectedString = "";

                    }
                });
                ivClose1 = dialogView1.findViewById(R.id.ivClose);
                ivClose1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog1.dismiss();
                    }
                });

                break;

            case R.id.rvLocality:

                TextView tvtitleLocality;
                RecyclerView rvItemsLocality;
                Button btSubmitLocality;
                ImageView ivCloseLocality;
                AlertDialog.Builder mBuilderLocality = new AlertDialog.Builder(EditStoreInformationActivity.this, R.style.CustomDialog);

                LayoutInflater inflaterLocality = this.getLayoutInflater();
                View dialogViewLocality = inflaterLocality.inflate(R.layout.dialogue_popup_list, null);
                mBuilderLocality.setView(dialogViewLocality);
                tvtitleLocality = dialogViewLocality.findViewById(R.id.tvTitleListPopup);
                tvtitleLocality.setText("Select Locality");
                rvItemsLocality = dialogViewLocality.findViewById(R.id.rvListPopup);
                btSubmitLocality = dialogViewLocality.findViewById(R.id.btSubmitDetail);
                listLocality.clear();
                for (int i = 0; i < localityType.length; i++) {
                    ObjectPopup objectPopup = new ObjectPopup(false, localityType[i]);
                    listLocality.add(objectPopup);
                }
                CustomPopupLocalityAdapter customPopupStoreTypeAdapter1;
                customPopupStoreTypeAdapter1 = new CustomPopupLocalityAdapter(listLocality, EditStoreInformationActivity.this, this, "Locality");

                LinearLayoutManager horizontalLayoutManagerLocality = new LinearLayoutManager(EditStoreInformationActivity.this, LinearLayoutManager.VERTICAL, false);

                rvItemsLocality.setLayoutManager(horizontalLayoutManagerLocality);

                rvItemsLocality.setAdapter(customPopupStoreTypeAdapter1);
                AlertDialog mDialogLocality = mBuilderLocality.create();
                mDialogLocality.show();

                btSubmitLocality.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvLocality.setText(selectedString);

                        mDialogLocality.dismiss();
                        selectedString = "";
                    }
                });
                ivCloseLocality = dialogViewLocality.findViewById(R.id.ivClose);
                ivCloseLocality.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogLocality.dismiss();
                    }
                });
                break;


            case R.id.rvApproach:

                TextView tvtitleApproach;
                RecyclerView rvItemsAproach;
                Button btSubmitAproach;
                ImageView ivCloseAproach;
                AlertDialog.Builder mBuilderAproach = new AlertDialog.Builder(EditStoreInformationActivity.this, R.style.CustomDialog);

                LayoutInflater inflaterAproach = this.getLayoutInflater();
                View dialogViewAproach = inflaterAproach.inflate(R.layout.dialogue_popup_list, null);

                mBuilderAproach.setView(dialogViewAproach);

                tvtitleApproach = dialogViewAproach.findViewById(R.id.tvTitleListPopup);
                tvtitleApproach.setText("Select Approach");
                rvItemsAproach = dialogViewAproach.findViewById(R.id.rvListPopup);
                btSubmitAproach = dialogViewAproach.findViewById(R.id.btSubmitDetail);
                listApproach.clear();
                for (int i = 0; i < approachType.length; i++) {
                    ObjectPopup objectPopup = new ObjectPopup(false, approachType[i]);
                    listApproach.add(objectPopup);
                }

                customPopupApproachAdapter = new CustomPopupApproachAdapter(listApproach, EditStoreInformationActivity.this, this, "Approach");

                LinearLayoutManager horizontalLayoutManagerAproach = new LinearLayoutManager(EditStoreInformationActivity.this, LinearLayoutManager.VERTICAL, false);

                rvItemsAproach.setLayoutManager(horizontalLayoutManagerAproach);

                rvItemsAproach.setAdapter(customPopupApproachAdapter);
                AlertDialog mDialogAproach = mBuilderAproach.create();
                mDialogAproach.show();

                btSubmitAproach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvApproach.setText(selectedString);


                        mDialogAproach.dismiss();
                    }
                });
                ivCloseAproach = dialogViewAproach.findViewById(R.id.ivClose);
                ivCloseAproach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogAproach.dismiss();
                    }
                });
                break;
            case R.id.rvShipment:

                TextView tvtitleShipment;
                RecyclerView rvItemsShipment;
                Button btSubmitShipment;
                ImageView ivCloseShipment;
                AlertDialog.Builder mBuilderShipment = new AlertDialog.Builder(EditStoreInformationActivity.this, R.style.CustomDialog);

                LayoutInflater inflaterShipment = this.getLayoutInflater();
                View dialogViewShipment = inflaterShipment.inflate(R.layout.dialogue_popup_list, null);
                mBuilderShipment.setView(dialogViewShipment);
                tvtitleShipment = dialogViewShipment.findViewById(R.id.tvTitleListPopup);
                tvtitleShipment.setText("Select Shipment");
                rvItemsShipment = dialogViewShipment.findViewById(R.id.rvListPopup);
                btSubmitShipment = dialogViewShipment.findViewById(R.id.btSubmitDetail);
                listShipment.clear();
                for (int i = 0; i < shipmentType.length; i++) {
                    ObjectPopup objectPopup = new ObjectPopup(false, shipmentType[i]);
                    listShipment.add(objectPopup);
                }

                customPopupShipmentAdapter = new CustomPopupShipmentAdapter(listShipment, EditStoreInformationActivity.this, this, "Shipment");

                LinearLayoutManager horizontalLayoutManagerShipment = new LinearLayoutManager(EditStoreInformationActivity.this, LinearLayoutManager.VERTICAL, false);

                rvItemsShipment.setLayoutManager(horizontalLayoutManagerShipment);

                rvItemsShipment.setAdapter(customPopupShipmentAdapter);
                AlertDialog mDialogShipment = mBuilderShipment.create();
                mDialogShipment.show();

                btSubmitShipment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvShipment.setText(selectedString);


                        mDialogShipment.dismiss();
                    }
                });
                ivCloseShipment = dialogViewShipment.findViewById(R.id.ivClose);
                ivCloseShipment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogShipment.dismiss();
                    }
                });


                break;


            case R.id.ivStoreImage:
                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditStoreInformationActivity.this);
                startActivityForResult(chooseImageIntent, 105);
                break;
            case R.id.ivStoreImageSelected:
                Intent chooseImageIntent1 = ImagePicker.getCameraIntent(EditStoreInformationActivity.this);
                startActivityForResult(chooseImageIntent1, 105);
                break;

            case R.id.rvStoreType:
                String[] arr = new String[select_Store_type.size()];
                for (int i = 0; i < select_Store_type.size(); i++) {
                    arr[i] = select_Store_type.get(i);
                }
                AlertDialog.Builder mBuilderStoreType = new AlertDialog.Builder(EditStoreInformationActivity.this);
                mBuilderStoreType.setTitle("Select Store Type");
                mBuilderStoreType.setMultiChoiceItems(arr, checkedStoreType, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            listStoreType.add(select_Store_type.get(i));
                        } else {
                            listStoreType.remove(select_Store_type.get(i));
                        }
                    }
                });
                mBuilderStoreType.setCancelable(false);
                mBuilderStoreType.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < listStoreType.size(); i++) {
                            item = item + listStoreType.get(i);
                            if (i != listStoreType.size() - 1) {
                                item = item + ",";
                            }
                        }
                        if (listStoreType.size() == 0) {
                            tvStoreType.setText("Select Store Type");
                        } else {
                            tvStoreType.setText(item);
                        }
                    }
                });
                AlertDialog mDialogStoreType = mBuilderStoreType.create();
                mDialogStoreType.show();
                break;


        }
    }

    private void addDC(String pcode, String pincodenumber) {
        // state is DC and DC is state
        if (pincodenumber.equalsIgnoreCase("1")) {
            dcLists.clear();
        }
        mProgressDialog.setMessage("Please wait...");
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
                    Mylogger.getInstance().Logit(TAG, getUserDetails.getResponse());
                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }
                            etStoreState.setText(getUserDetails.getData().get(i).getState());
                            etStoreCity.setText(getUserDetails.getData().get(i).getCity());
                        }

                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditStoreInformationActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);


                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditStoreInformationActivity.this);
                    } else {
                        Toast.makeText(EditStoreInformationActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditStoreInformationActivity.this, "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditStoreInformationActivity.this, "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                // Toast.makeText(EditStoreInformationActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

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
                ((TextView) view).setTextSize(10);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.monteregular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }


    private boolean validation() {

        if (TextUtils.isEmpty(etStoreName.getText().toString())) {

            //showMSG(false, "Provide Store name");
            etStoreName.setError("Provide store name");
            etStoreName.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(etStoreShopname.getText().toString())) {

            etStoreShopname.setError("Provide store address");
            etStoreShopname.requestFocus();
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etStoreStreetName.getText().toString())) {

            etStoreStreetName.setError("Provide store street");
            etStoreStreetName.requestFocus();
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etStoreLandmarl.getText().toString())) {

            etStoreLandmarl.setError("Provide landmark");
            etStoreLandmarl.requestFocus();
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etStorePincode.getText().toString())) {

            etStorePincode.setError("Provide pincode");
            etStorePincode.requestFocus();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etStoreCity.getText().toString())) {

            etStoreCity.setError("Provide City");
            etStoreCity.requestFocus();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etStoreState.getText().toString())) {

            etStoreState.setError("Provide State");
            etStoreState.requestFocus();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etStoreRoute.getText().toString())) {

            etStoreRoute.setError("Provide Route");
            etStoreRoute.requestFocus();
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (tvTypeofVendor.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(EditStoreInformationActivity.this, "Select Type of Vendor", Toast.LENGTH_LONG).show();
            return false;
        }

        if (tvVendorTypeDetail.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(EditStoreInformationActivity.this, "Select Vendor type Detail", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(tvVendorTypeDetail.getText().toString())) {
            Toast.makeText(EditStoreInformationActivity.this, "Select Vendor type Detail", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(tvStoreType.getText().toString())) {
              /*  tvStoreType.requestFocus();
                tvStoreType.setError("Provide Store Type");*/
            Toast.makeText(EditStoreInformationActivity.this, "Please Select Store Type", Toast.LENGTH_LONG).show();
            return false;
        }

        if (tvLocality.getText().equals("")) {
            //  tvLocality.requestFocus();
            Toast.makeText(EditStoreInformationActivity.this, "Select Locality", Toast.LENGTH_LONG).show();
            // showMSG(false, "Provide Pincode");
            //   tvStoreType.setError("Select Locality");
            return false;
        }
        if (tvApproach.getText().equals("")) {
               /* tvApproach.requestFocus();
                // Toast.makeText(EditStoreInformationActivity.this, "Select Approach", Toast.LENGTH_SHORT).show();
                tvApproach.setError("Select Approach");*/
            Toast.makeText(EditStoreInformationActivity.this, "Select Approach", Toast.LENGTH_LONG).show();

            return false;
        }


        if (tvShipment.getText().toString().equalsIgnoreCase("")) {
            // tvShipment.setError("Select Shipment Transfer");
            Toast.makeText(EditStoreInformationActivity.this, "Please Select Shipment Transfer", Toast.LENGTH_LONG).show();

            //    tvShipment.requestFocus();
            // showMSG(false, "Provide Pincode");
            return false;
        }


           /* if (etLicenceNumeber.getText().toString().equalsIgnoreCase("")) {
                etLicenceNumeber.setError("Provide Licence no.");
                etLicenceNumeber.requestFocus();
                return false;
            }*/
        if (tvStoreType.getText().toString().equalsIgnoreCase("Select Store Type")) {
            Toast.makeText(EditStoreInformationActivity.this, "Please Select Store Type", Toast.LENGTH_LONG).show();
            return false;
        }

        if (listStoreType.size() == 0) {
            Toast.makeText(EditStoreInformationActivity.this, "Please Select Store Type", Toast.LENGTH_LONG).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (shopImg.equalsIgnoreCase("")) {
            Toast.makeText(EditStoreInformationActivity.this, "Please Select image of Store ", Toast.LENGTH_LONG).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position,String data) {

    }

    @Override
    public void SingleClick(String popup, int position) {
        if (popup.equalsIgnoreCase("VendorType")) {
            selectedString = listVendorType.get(position).getUserName();
        } else if (popup.equalsIgnoreCase("StoreType")) {
            selectedString = ListStore_type.get(position).getUserName();
        } else if (popup.equalsIgnoreCase("Locality")) {
            selectedString = listLocality.get(position).getUserName();
        } else if (popup.equalsIgnoreCase("Approach")) {
            selectedString = listApproach.get(position).getUserName();
        } else if (popup.equalsIgnoreCase("Shipment")) {
            selectedString = listShipment.get(position).getUserName();
        }

    }


    // submit Details Last Api
    public void bizDetailsSubmit(final View v) {

        Call<GetUserDetailResponse> call;
        MultipartBody.Part typedFile = null;
        gps = new GPSTracker(EditStoreInformationActivity.this);
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
        mProgressDialog.setMessage("Submitting kirana details . Please wait ...");
        mProgressDialog.show();
        HashMap<String, String> user = new HashMap<>();

        user.put(Constants.PARAM_PROCESS_ID, str_process_id);
        user.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.put(Constants.PARAM_TOKEN, sessionManager.getToken());

        user.put(Constants.PARAM_PINCODE, "" + etStorePincode.getText());
        user.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
        user.put(Constants.PARAM_STATE, "" + etStoreState.getText());
        user.put(Constants.PARAM_CITY, "" + etStoreCity.getText());
        user.put(Constants.PARAM_STORE_NAME, "" + etStoreName.getText());
        user.put(Constants.PARAM_STORE_ADDRESS, "" + etStoreShopname.getText());
        user.put(Constants.PARAM_STORE_ADDRESS1, "" + etStoreStreetName.getText());
        user.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, "" + etStoreLandmarl.getText());
        user.put(Constants.PARAM_LATITUDE, "" + lattitude);
        user.put(Constants.PARAM_LONGITUDE, "" + longitude);
        user.put(Constants.PARAM_ROUTE, "R-" + etStoreRoute.getText());
        if (!TextUtils.isEmpty(etLicenceNumber.getText().toString())) {
            user.put(Constants.PARAM_LICENCE_NO, "" + etLicenceNumber.getText());
        } else {
            user.put(Constants.PARAM_LICENCE_NO, "");
        }


        user.put(Constants.PARAM_VENDOR_TYPE, "" + tvTypeofVendor.getText());
        user.put(Constants.PARAM_VENDOR_TYPE_DETAIL, "" + tvVendorTypeDetail.getText());
        user.put(Constants.PARAM_LOCALITY, "" + tvLocality.getText());
        user.put(Constants.PARAM_APPROACH, "" + tvApproach.getText());
        user.put(Constants.PARAM_SHIPMENT_TRANS, "" + tvShipment.getText());
        user.put(Constants.PARAM_PARTNER_WITH_OTHER, "" + isPartnered);
        user.put(Constants.PARAM_STORE_TYPE_CONFIG, "" + tvStoreType.getText());


        Log.e("User Date", "Edit info" + user);
        Log.e("User Date", "Edit info" + str_process_id + "   " + sessionManager.getAgentID());

        File imagefile = new File(shopImg);
        typedFile = MultipartBody.Part.createFormData(Constants.PARAM_SHOP_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));


        call = ApiClient.getClient().create(ApiInterface.class).submitStoreInfo("Bearer " + sessionManager.getToken(), user, typedFile);


        call.enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());

                        finish();

                        //   showAlert(getUserDetailResponse.getResponse());
                        // commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    }
                    if (getUserDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditStoreInformationActivity.this);
                    }

                    if (getUserDetailResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (getUserDetailResponse.getValidation() != null) {
                            Validation validation = getUserDetailResponse.getValidation();

                            if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                                etStoreName.setError(validation.getStoreName());
                                etStoreName.requestFocus();

                            }
                            if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                etStoreShopname.setError(validation.getRoute());
                                etStoreShopname.requestFocus();
                            }

                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStoreStreetName.setError(validation.getRoute());
                                etStoreStreetName.requestFocus();

                            }
                            if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                Toast.makeText(EditStoreInformationActivity.this, validation.getGstCertificateImage(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                            /*    edit_storeaddress.setError(validation.getStoreAddress());
                                edit_storeaddress.requestFocus();*/

                            }
                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                              /*  //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStreet.setError(validation.getStoreAddress1());
                                etStreet.requestFocus();

*/
                            }
                            if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etLandmark.setError(validation.getStoreAddressLandmark());
                                etLandmark.requestFocus();*/
                            }
                            if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* edit_pincode.setError(validation.getPincode());
                                edit_pincode.requestFocus();*//**/

                            }
                            if (validation.getCity() != null && validation.getCity().length() > 0) {

                               /* edit_city.setError(validation.getCity());
                                edit_city.requestFocus();*/
                            }
                            if (validation.getState() != null && validation.getState().length() > 0) {
                               /* edit_state.setError(validation.getState());
                                edit_state.requestFocus();*/
                            }

                            if (validation.getVendorType() != null && validation.getVendorType().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditStoreInformationActivity.this, validation.getVendorType(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLocality() != null && validation.getLocality().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditStoreInformationActivity.this, validation.getLocality(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getApproach() != null && validation.getApproach().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditStoreInformationActivity.this, validation.getApproach(), Toast.LENGTH_SHORT).show();
                            }

                            if (validation.getShipmentTransfer() != null && validation.getShipmentTransfer().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditStoreInformationActivity.this, validation.getShipmentTransfer(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getPartnerWithOtherEcommerce() != null && validation.getPartnerWithOtherEcommerce().length() > 0) {
                                //Toast.makeText(EditStoreInformationActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditStoreInformationActivity.this, validation.getPartnerWithOtherEcommerce(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                               /* edit_storename.setError(validation.getStoreName());
                                edit_storename.requestFocus();*/
                                // return false;
                            }

                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                Toast.makeText(EditStoreInformationActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getStoreTypeConfig() != null && validation.getStoreTypeConfig().length() > 0) {
                                Toast.makeText(EditStoreInformationActivity.this, validation.getStoreTypeConfig(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                Toast.makeText(EditStoreInformationActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();

                                // return false;
                            } else {
                                Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                            }

                        } else {
                            Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                        }

                    } else {
                        Constants.showAlert(v, getUserDetailResponse.getResponse(), true);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditStoreInformationActivity.this);
                        }
                    }
                } else {
                    Constants.showAlert(v, "#errorcode :- 2094 " + getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditStoreInformationActivity.this, "#errorcode :- 2094 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(EditStoreInformationActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 105) {

                Bitmap bitmap = ImagePicker.getImageFromResult(EditStoreInformationActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                shopImg = ImagePicker.getBitmapPath(bitmap, EditStoreInformationActivity.this); // ImageUtils.getInstant().getImageUri(EditStoreInformationActivity.this, photo);
                Glide.with(EditStoreInformationActivity.this).load(shopImg).into(ivStoreImage);
                Log.e("imageowner", "back image" + shopImg);
                ivStoreImageSelected.setVisibility(View.VISIBLE);
            }
        }
    }
}
