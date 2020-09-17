package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetVendorTypeDetails;
import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddDeliveryBoysActivity;
import com.canvascoders.opaper.activity.ChangeMobileActivity;
import com.canvascoders.opaper.adapters.CustomPopupAdapter;
import com.canvascoders.opaper.adapters.CustomPopupApproachAdapter;
import com.canvascoders.opaper.adapters.CustomPopupLocalityAdapter;
import com.canvascoders.opaper.adapters.CustomPopupShipmentAdapter;
import com.canvascoders.opaper.adapters.CustomPopupStoreTypeAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.canvascoders.opaper.utils.Constants.hideKeyboardwithoutPopulate;

public class InfoFragment extends Fragment implements View.OnClickListener, RecyclerViewClickListener /*implements View.OnClickListener*/ {

    private EditText etEmail, etStorename, etOwnerName, etStoreShopNo, etStoreStreet, etStoreLandmark, etStorePincode, etStoreCity, etStoreState, etRoute;
    private EditText etCurrentShopNo, etCurrentStreet, etCurrentLandmark, etCurrentPincode, etCurrentCity, etCurrentState, etPerShopNo, etPerStreet, etPerLandmark, etPerPincode, etPerCity, etPerState, edit_gstn;
    private EditText etLicenceNumeber;
    private TextView tvDOB;
    String stringDOB = "";
    private Toolbar toolbar;
    private String selectedString = "";
    private static Dialog dialog;
    private TextView tvTypeofVendor, tvStoreType, tvVendorTypeDetail, tvLocality, tvApproach, tvShipment;
    private TextView tvLanguage;
    private Switch switchGst, switchPartner;
    private String TAG = "InfoFragment";
    private ProgressDialog mProgressDialog;
    String validationapiUrl = "";
    private SessionManager sessionManager;
    private Button btn_next, btChangeChaque;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView tvGeneralInfo, tvAddressInfo;
    private LinearLayout llDOB;
    DatePicker datePicker;
    CustomPopupStoreTypeAdapter customPopupStoreTypeAdapter;
    CustomPopupLocalityAdapter customPopupLocalityAdapter;
    CustomPopupApproachAdapter customPopupApproachAdapter;
    CustomPopupShipmentAdapter customPopupShipmentAdapter;
    String email = "", storename = "", ownername = "", dateofbirth = "", storeaddress = "", storeaddress1 = "", storeaddresslandmark = "", storeaddressPincode = "", storeaddressCity = "", dcdata = "", storeaddressState = "", route = "";

    View view2, view3;
    LinearLayout llOwnerInfo, llGeneraInfo, llAddrssInfo;
    Button btNext;
    Switch ifgst;
    private String gstPath = "";
    RelativeLayout rvVendorType, rvStoreType, rvLocality, rvApproach, rvShipmentTransfer, rvVendorTypeDetail;
    int i = 0;
    boolean[] checkedItems;
    private String same_address = "0", pincode;
    boolean[] checkedStoreType;
    private String lattitude = "", longitude = "";
    GPSTracker gps;
    private TextView tvPartner;

    private String blockCharacterSet = "~#^|$%&*!'()*+-,./:;<=>?@[]";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private Spinner dc;
    private ScrollView scMain;

    private Spinner spLocality, spTypeofVendor, spTypeofVendorDetail, spApproach, spShipment, spLanguage;
    /*  private List<String> shipmentmodeltype = new ArrayList<>();
      private List<String> type_of_vendor = new ArrayList<>();
      private List<String> type_of_vendorDetail = new ArrayList<>();
      private List<String> locality = new ArrayList<>();
      private List<String> approach = new ArrayList<>();
  */
    private String isgsttn = "no";
    private static int IMAGE_SELCTION_CODE = 0;
    private static final int IMAGE_GST = 101;
    private String isPartnered = "no";
    private RelativeLayout rvSelectLanguage, rvSelectStoretype;
    private ArrayList<String> dcLists = new ArrayList<>();
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


    ArrayList<String> listLanaguage = new ArrayList<>();
    List<ObjectPopup> listVendorType = new ArrayList<>();
    List<ObjectPopup> listLocality = new ArrayList<>();
    List<ObjectPopup> listApproach = new ArrayList<>();
    List<ObjectPopup> listShipment = new ArrayList<>();
    String str_process_id;
    List<ObjectPopup> ListStore_type = new ArrayList<>();
    private AppCompatCheckBox cbSame;
    List<String> select_Store_type;
    List<String> listStoreType = new ArrayList<>();
    Context mcontext;
    Button btChangePan;
    View view;
    LinearLayout llGst;
    ImageView ivGst;
    private RequestPermissionHandler requestPermissionHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        requestPermissionHandler = new RequestPermissionHandler();


        mcontext = this.getActivity();
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Submitting Kirana details . Please wait ...");
        mProgressDialog.setCancelable(false);

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        OTPActivity.settitle(Constants.TITLE_VENDOR_DETAIL_MENSA);

        initView();
      /*  type_of_vendor = Arrays.asList(getResources().getStringArray(R.array.TypeofVendor));
        CustomAdapter<String> spTypeofVendorAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendor);
        spTypeofVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeofVendor.setAdapter(spTypeofVendorAdapter);*/

        /*
        locality = Arrays.asList(getResources().getStringArray(R.array.Locality));
        CustomAdapter<String> spLocalityAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, locality);
        spLocalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocality.setAdapter(spLocalityAdapter);

        approach = Arrays.asList(getResources().getStringArray(R.array.Approach));
        CustomAdapter<String> spApproachAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, approach);
        spApproachAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spApproach.setAdapter(spApproachAdapter);

        shipmentmodeltype = Arrays.asList(getResources().getStringArray(R.array.shipment));
        CustomAdapter<String> shipmentAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, shipmentmodeltype);
        shipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShipment.setAdapter(shipmentAdapter);
*/
        Bundle bundle = this.getArguments();
        bundle = this.getArguments();
        if (bundle != null) {
            // String is_edit = bundle.getString(Constants.KEY_EMP_MOBILE);
            email = bundle.getString(Constants.KEY_EMAIL);
            storename = bundle.getString(Constants.KEY_STORES);
            ownername = bundle.getString(Constants.KEY_NAME);
            dateofbirth = bundle.getString(Constants.PARAM_BIRTH_DATE);
            storeaddress = bundle.getString(Constants.PARAM_STORE_ADDRESS);
            storeaddress1 = bundle.getString(Constants.PARAM_STORE_ADDRESS1);
            storeaddresslandmark = bundle.getString(Constants.PARAM_STORE_ADDRESS_LANDMARK);
            storeaddressPincode = bundle.getString(Constants.PARAM_PINCODE);
            storeaddressCity = bundle.getString(Constants.PARAM_CITY);
            storeaddressState = bundle.getString(Constants.PARAM_STATE);
            dcdata = bundle.getString(Constants.PARAM_DC);
            route = bundle.getString(Constants.PARAM_ROUTE);

            etEmail.setText(email);
            etStorename.setText(storename);
            etOwnerName.setText(ownername);
            tvDOB.setText(dateofbirth);
            etStoreShopNo.setText(storeaddress);
            etStoreStreet.setText(storeaddress1);
            etStoreLandmark.setText(storeaddresslandmark);
            etStorePincode.setText(storeaddressPincode);
            etStoreCity.setText(storeaddressCity);
            etStoreState.setText(storeaddressState);
            etRoute.setText(route);


        }
        getUserInfo();
        return view;
    }




    private void getUserInfo() {
        Mylogger.getInstance().Logit(TAG, "getUserInfo");
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("we are retrieving information, please wait!");
            mProgressDialog.show();

            Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getDetails("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    mProgressDialog.dismiss();
                    Mylogger.getInstance().Logit(TAG, response.raw().toString());
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().toString();
                            Mylogger.getInstance().Logit(TAG, res);
                            if (!TextUtils.isEmpty(res)) {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.has("responseCode")) {
                                    if (jsonObject.getInt("responseCode") == 200) {
                                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                        //name.setText(result.getString("name").toString());

                                        String pan = result.getString("pan_no").toString();
                                        String first = String.valueOf(pan.charAt(3));
                                        if (!first.equalsIgnoreCase("C") && !first.equalsIgnoreCase("F")) {
                                             etOwnerName.setText(result.getString("name").toString());
                                        }
                                        else{
                                            etOwnerName.setText("");
                                        }




                                    } else if (jsonObject.getInt("responseCode") == 405) {
                                        sessionManager.logoutUser(mcontext);
                                    } else {
                                        Toast.makeText(mcontext, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "#errorcode 2051 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "#errorcode 2051 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "#errorcode 2051 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "#errorcode 2051 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }

    private void initView() {

        // OTPActivity.settitle("Mensa Vednor Details");

        llGst = view.findViewById(R.id.llGST);
        btNext = view.findViewById(R.id.btNext);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        llOwnerInfo = view.findViewById(R.id.llOwnerInfo);
        llGeneraInfo = view.findViewById(R.id.llGeneralInfo);
        llAddrssInfo = view.findViewById(R.id.llAddressInfo);
        scMain = view.findViewById(R.id.scMain);
        rvVendorType = view.findViewById(R.id.rvTypeVendor);
        rvVendorType.setOnClickListener(this);
        rvStoreType = view.findViewById(R.id.rvStoreType);
        rvVendorTypeDetail = view.findViewById(R.id.rvVendorTypeDetail);
        rvApproach = view.findViewById(R.id.rvApproach);
        rvLocality = view.findViewById(R.id.rvLocality);
        rvSelectLanguage = view.findViewById(R.id.rvSelectLanguage);
        rvSelectLanguage.setOnClickListener(this);
        rvShipmentTransfer = view.findViewById(R.id.rvShipment);
        tvGeneralInfo = view.findViewById(R.id.tvGeneralInfo);
        tvAddressInfo = view.findViewById(R.id.tvAddressInfo);
        rvShipmentTransfer.setOnClickListener(this);
        rvLocality.setOnClickListener(this);
        rvApproach.setOnClickListener(this);

        btChangePan = view.findViewById(R.id.btChangePan);
        btChangePan.setOnClickListener(this);
        rvVendorType.setOnClickListener(this);
        rvStoreType.setOnClickListener(this);
        rvVendorTypeDetail.setOnClickListener(this);
        btNext.setOnClickListener(this);
        tvTypeofVendor = view.findViewById(R.id.tvTypeofVendor);
        tvStoreType = view.findViewById(R.id.tvStoreType);
        tvVendorTypeDetail = view.findViewById(R.id.tvVendorTypeDetail);
        tvApproach = view.findViewById(R.id.tvApproach);
        tvShipment = view.findViewById(R.id.tvShipment);
        tvLocality = view.findViewById(R.id.tvLocality);
        tvLanguage = view.findViewById(R.id.tvLanguage);


        etEmail = view.findViewById(R.id.etEmailInfo);
        etLicenceNumeber = view.findViewById(R.id.etLicenceNumber);
        etStorename = view.findViewById(R.id.etStoreName);
        etOwnerName = view.findViewById(R.id.etOwnerName);
        tvDOB = view.findViewById(R.id.etDateOfBirth);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c.setTime(today);
                c.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate = c.getTime().getTime(); //

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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

                                stringDOB = year + "-" + monthString + "-" + daysString;
                                tvDOB.setText(daysString + "-" + monthString + "-" + year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
            }
        });


        etStoreShopNo = view.findViewById(R.id.etShopHouseNo);
        etStoreStreet = view.findViewById(R.id.etStreetName);
        etStoreLandmark = view.findViewById(R.id.etLandmark);
        etStorePincode = view.findViewById(R.id.etStorePincode);
        etStoreCity = view.findViewById(R.id.etStoreCity);
        etStoreState = view.findViewById(R.id.etStoreState);
        etRoute = view.findViewById(R.id.etRouteNo);
        edit_gstn = view.findViewById(R.id.edit_gstn);

        dc = (Spinner) view.findViewById(R.id.dc);
        ifgst = view.findViewById(R.id.switch_gst);
        ifgst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                edit_gstn.setEnabled(isChecked);
                if (isChecked) {
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

        etStorePincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pincode = "1";
                if (s.length() == 6) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC(s.toString(), pincode);
                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }

                }
            }
        });


        etCurrentShopNo = view.findViewById(R.id.etCurrentShopNo);
        etCurrentStreet = view.findViewById(R.id.etCurrentStreet);
        etCurrentLandmark = view.findViewById(R.id.etCurrentLandmark);
        etCurrentPincode = view.findViewById(R.id.etCurrentPincode);
        etCurrentCity = view.findViewById(R.id.etCurrentCity);
        etCurrentState = view.findViewById(R.id.etCurrentState);
        cbSame = view.findViewById(R.id.cbSameAsAbove);
        etPerShopNo = view.findViewById(R.id.etPermShopNo);
        etPerStreet = view.findViewById(R.id.etPerStreet);
        etPerLandmark = view.findViewById(R.id.etPerLandmark);
        etPerPincode = view.findViewById(R.id.etPerPincode);
        etPerCity = view.findViewById(R.id.etPerCity);
        etPerState = view.findViewById(R.id.etPerState);
        switchPartner = view.findViewById(R.id.sw_partner);

        cbSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    same_address = "1";
                    etPerShopNo.setText(etCurrentShopNo.getText().toString().trim());
                    etPerStreet.setText(etCurrentStreet.getText().toString().trim());
                    etPerLandmark.setText(etCurrentLandmark.getText().toString().trim());
                    etPerPincode.setText(etCurrentPincode.getText().toString().trim());
                    etPerCity.setText(etCurrentCity.getText().toString().trim());
                    etPerState.setText(etCurrentState.getText().toString().trim());

                } else {
                    same_address = "0";
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
                pincode = "2";
                if (s.length() == 6) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC(s.toString(), pincode);
                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }
                    //addDC(s.toString(), pincode);
                }
            }
        });


        etCurrentPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pincode = "3";
                if (s.length() == 6) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        // getBankDetails(mContext,s.toString(),processId);
                        addDC(s.toString(), pincode);
                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }
                    //  addDC(s.toString(), pincode);
                }
            }
        });

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

        ivGst = view.findViewById(R.id.ivGSTImage);
        // iv_image = dialogView.findViewById(R.id.iv_prof_boy);
        ivGst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_gst_image();
            }
        });

        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetVendorType();
        } else {
            Constants.ShowNoInternet(getActivity());
        }




    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

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
                        if (pincodenumber.equalsIgnoreCase("1")) {
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                    dcLists.add(dc.getDc());
                                }
                                etStoreState.setText(getUserDetails.getData().get(i).getState());
                                etStoreCity.setText(getUserDetails.getData().get(i).getCity());
                            }

                            CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, dcLists);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dc.setAdapter(spinnerArrayAdapter);
                            dc.setSelection(0);
                        }
                        if (pincodenumber.equalsIgnoreCase("2")) {
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                etPerState.setText(getUserDetails.getData().get(i).getState());
                                etPerCity.setText(getUserDetails.getData().get(i).getCity());
                            }
                        }
                        if (pincodenumber.equalsIgnoreCase("3")) {
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                etCurrentState.setText(getUserDetails.getData().get(i).getState());
                                etCurrentCity.setText(getUserDetails.getData().get(i).getCity());
                            }
                        }

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(mcontext);
                    } else {
                        Toast.makeText(mcontext, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode :-2032 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                // Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btNext:
                if (i == 0) {
                    if (validation(1)) {

                        hideKeyboardwithoutPopulate(getActivity());
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            i = 1;
                            validationapiUrl = "1";
                            ApiCallValidationCheck(validationapiUrl, 1);
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }


                    }
                } else if (i == 1) {
                    if (validation(2)) {

                        hideKeyboardwithoutPopulate(getActivity());
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            i = 2;
                            validationapiUrl = "2";
                            ApiCallValidationCheck(validationapiUrl, 2);
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }

                    }
                } else {
                    if (validation(3)) {
                        // hideKeyboardwithoutPopulate(getActivity());

                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            bizDetailsSubmit(view);
                        } else {
                            Constants.ShowNoInternet(getActivity());
                        }


                    }
                }
                break;

            case R.id.rvTypeVendor:

                TextView tvtitle;
                RecyclerView rvItems;
                ImageView ivClose;
                Button btSubmit;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

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

                CustomPopupAdapter customPopupAdapter = new CustomPopupAdapter(listVendorType, mcontext, this, "VendorType");

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

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

                        selectedString = "";

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
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

                LayoutInflater inflater1 = this.getLayoutInflater();
                View dialogView1 = inflater1.inflate(R.layout.dialogue_popup_list, null);
                mBuilder2.setView(dialogView1);
                tvtitleStoreType = dialogView1.findViewById(R.id.tvTitleListPopup);
                tvtitleStoreType.setText("Vendor Type Detail");
                rvItems1 = dialogView1.findViewById(R.id.rvListPopup);
                btSubmit1 = dialogView1.findViewById(R.id.btSubmitDetail);
                CustomPopupStoreTypeAdapter customPopupStoreTypeAdapter;
                customPopupStoreTypeAdapter = new CustomPopupStoreTypeAdapter(ListStore_type, getActivity(), this, "StoreType");

                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

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
                AlertDialog.Builder mBuilderLocality = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

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
                customPopupStoreTypeAdapter1 = new CustomPopupLocalityAdapter(listLocality, mcontext, this, "Locality");

                LinearLayoutManager horizontalLayoutManagerLocality = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

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
                AlertDialog.Builder mBuilderAproach = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

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

                customPopupApproachAdapter = new CustomPopupApproachAdapter(listApproach, mcontext, this, "Approach");

                LinearLayoutManager horizontalLayoutManagerAproach = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                rvItemsAproach.setLayoutManager(horizontalLayoutManagerAproach);

                rvItemsAproach.setAdapter(customPopupApproachAdapter);
                AlertDialog mDialogAproach = mBuilderAproach.create();
                mDialogAproach.show();

                btSubmitAproach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvApproach.setText(selectedString);


                        mDialogAproach.dismiss();
                        selectedString = "";
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
                AlertDialog.Builder mBuilderShipment = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

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

                customPopupShipmentAdapter = new CustomPopupShipmentAdapter(listShipment, mcontext, this, "Shipment");

                LinearLayoutManager horizontalLayoutManagerShipment = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                rvItemsShipment.setLayoutManager(horizontalLayoutManagerShipment);

                rvItemsShipment.setAdapter(customPopupShipmentAdapter);
                AlertDialog mDialogShipment = mBuilderShipment.create();
                mDialogShipment.show();

                btSubmitShipment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvShipment.setText(selectedString);


                        mDialogShipment.dismiss();
                        selectedString = "";

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

            case R.id.rvSelectLanguage:

                AlertDialog.Builder mBuilderLanguage = new AlertDialog.Builder(getActivity());
                mBuilderLanguage.setTitle("Select Languagaes");
                mBuilderLanguage.setMultiChoiceItems(select_language, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            listLanaguage.add(select_language[i]);
                        } else {
                            listLanaguage.remove(select_language[i]);
                        }
                    }
                });
                mBuilderLanguage.setCancelable(false);
                mBuilderLanguage.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                AlertDialog mDialogLang = mBuilderLanguage.create();
                mDialogLang.show();

                break;


            case R.id.rvStoreType:
                String[] arr = new String[select_Store_type.size()];
                for (int i = 0; i < select_Store_type.size(); i++) {
                    arr[i] = select_Store_type.get(i);
                }
                AlertDialog.Builder mBuilderStoreType = new AlertDialog.Builder(getActivity());
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
            case R.id.btChangePan:
                commanFragmentCallWithoutBackStack2(new PanVerificationFragment());
                break;


        }

    }

    private void ApiCallValidationCheck(String apiname, final int i1) {
        mProgressDialog.setMessage("Submitting kirana details . Please wait ...");

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_PROCESS_ID, str_process_id);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        if (i1 == 1) {
            user.addProperty(Constants.PARAM_EMAIL, "" + etEmail.getText());
            user.addProperty(Constants.PARAM_STORE_NAME, "" + etStorename.getText());
            user.addProperty(Constants.PARAM_OWNER_NAME, "" + etOwnerName.getText());
            user.addProperty(Constants.PARAM_DOB, "" + stringDOB);
            user.addProperty(Constants.PARAM_STORE_ADDRESS, "" + etStoreShopNo.getText());
            user.addProperty(Constants.PARAM_STORE_ADDRESS1, "" + etStoreStreet.getText());
            user.addProperty(Constants.PARAM_STORE_ADDRESS_LANDMARK, "" + etStoreLandmark.getText());
            user.addProperty(Constants.PARAM_CITY, "" + etStoreCity.getText());
            user.addProperty(Constants.PARAM_STATE, "" + etStoreState.getText());
            user.addProperty(Constants.PARAM_PINCODE, "" + etStorePincode.getText());
            user.addProperty(Constants.PARAM_DC, "" + dc.getSelectedItem());
            user.addProperty(Constants.PARAM_ROUTE, "R-" + etRoute.getText());

        }
        if (i1 == 2) {
            user.addProperty(Constants.PARAM_RESIDENTIAL_ADDRESS, "" + etCurrentShopNo.getText());
            user.addProperty(Constants.PARAM_RESIDENTIAL_ADDRESS1, "" + etCurrentStreet.getText());
            user.addProperty(Constants.PARAM_RESIDENTIAL_LANDMARK, "" + etCurrentLandmark.getText());
            user.addProperty(Constants.PARAM_RESIDENTIAL_PINCODE, "" + etCurrentPincode.getText());
            user.addProperty(Constants.PARAM_RESIDENTIAL_CITY, "" + etCurrentCity.getText());
            user.addProperty(Constants.PARAM_RESIDENTIAL_STATE, "" + etCurrentState.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS, "" + etPerShopNo.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS1, "" + etPerStreet.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_LANDMARK, "" + etPerLandmark.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_PINCODE, "" + etPerPincode.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_CITY, "" + etPerCity.getText());
            user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_STATE, "" + etPerState.getText());
        }

        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).submitBizDetailsValid1("Bearer " + sessionManager.getToken(), apiname, user).enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        if (i1 == 1) {
                            llOwnerInfo.setVisibility(View.GONE);
                            view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            llAddrssInfo.setVisibility(View.VISIBLE);
                            // btChangePan.setVisibility(View.GONE);
                            focusOnView("1");
                        } else if (i1 == 2) {
                            view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            llAddrssInfo.setVisibility(View.GONE);
                            llOwnerInfo.setVisibility(View.GONE);
                            llGeneraInfo.setVisibility(View.VISIBLE);
                            focusOnView("2");
                        }

                    } else {

                        if (getUserDetailResponse.getResponseCode() == 400) {
                            if (i1 == 1) {
                                i = 0;
                            } else if (i1 == 2) {
                                i = 1;
                            }

                            mProgressDialog.dismiss();
                            if (getUserDetailResponse.getValidation() != null) {
                                Validation validation = getUserDetailResponse.getValidation();
                                if (validation.getOwnerName() != null && validation.getOwnerName().length() > 0) {
                                    etOwnerName.setError(validation.getOwnerName());
                                    etOwnerName.requestFocus();
                                    // return false;
                                }
                                if (validation.getDob() != null && validation.getDob().length() > 0) {
                                    tvDOB.setError(validation.getOwnerName());
                                    tvDOB.requestFocus();
                                    // return false;
                                }
                                if (validation.getVendorTypeDetail() != null && validation.getVendorTypeDetail().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getVendorTypeDetail(), Toast.LENGTH_LONG).show();
                                    // return false;
                                    // return false;
                                }

                                if (validation.getRoute() != null && validation.getRoute().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etRoute.setError(validation.getRoute());
                                    etRoute.requestFocus();

                                }
                                if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etStoreShopNo.setError(validation.getStoreAddress());
                                    etStoreShopNo.requestFocus();

                                }
                                if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etStoreStreet.setError(validation.getStoreAddress1());
                                    etStoreStreet.requestFocus();

                                }
                                if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etStoreLandmark.setError(validation.getStoreAddressLandmark());
                                    etStoreLandmark.requestFocus();
                                }
                                if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etStorePincode.setError(validation.getPincode());
                                    etStorePincode.requestFocus();

                                }
                                if (validation.getCity() != null && validation.getCity().length() > 0) {

                                    etStoreCity.setError(validation.getCity());
                                    etStoreCity.requestFocus();
                                }
                                if (validation.getState() != null && validation.getState().length() > 0) {
                                    etStoreState.setError(validation.getState());
                                    etStoreState.requestFocus();
                                }
                                if (validation.getResidentialAddress() != null && validation.getResidentialAddress().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentShopNo.setError(validation.getResidentialAddress());
                                    etCurrentShopNo.requestFocus();
                                }
                                if (validation.getResidentialAddress1() != null && validation.getResidentialAddress1().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentStreet.setError(validation.getResidentialAddress1());
                                    etCurrentStreet.requestFocus();
                                }
                                if (validation.getResidentialAddressLandmark() != null && validation.getResidentialAddressLandmark().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentLandmark.setError(validation.getResidentialAddressLandmark());
                                    etCurrentLandmark.requestFocus();
                                }
                                if (validation.getResidentialAddresspicode() != null && validation.getResidentialAddresspicode().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialPincode.setError(validation.getResidentialAddresspicode());
                                etResidentialPincode.requestFocus();*/
                                }
                                if (validation.getResidentialAddressCity() != null && validation.getResidentialAddressCity().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentCity.setError(validation.getResidentialAddressCity());
                                    etCurrentCity.requestFocus();
                                }
                                if (validation.getResidentialAddressState() != null && validation.getResidentialAddressState().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etCurrentState.setError(validation.getResidentialAddressState());
                                    etCurrentState.requestFocus();
                                }

                                if (validation.getPermanentAddress() != null && validation.getPermanentAddress().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerShopNo.setError(validation.getPermanentAddress());
                                    etPerShopNo.requestFocus();
                                }

                                if (validation.getPermanentAddress1() != null && validation.getPermanentAddress1().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerStreet.setError(validation.getPermanentAddress1());
                                    etPerStreet.requestFocus();
                                }
                                if (validation.getPermanentAddressLandmark() != null && validation.getPermanentAddressLandmark().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerLandmark.setError(validation.getPermanentAddressLandmark());
                                    etPerLandmark.requestFocus();
                                }
                                if (validation.getPermanentAddressPicode() != null && validation.getPermanentAddressPicode().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerPincode.setError(validation.getPermanentAddressPicode());
                                    etPerPincode.requestFocus();
                                }
                                if (validation.getPermanentAddressCity() != null && validation.getPermanentAddressCity().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerCity.setError(validation.getPermanentAddressCity());
                                    etPerCity.requestFocus();
                                }
                                if (validation.getPermanentAddressState() != null && validation.getPermanentAddressState().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    etPerState.setError(validation.getPermanentAddressState());
                                    etPerState.requestFocus();
                                }
                                if (validation.getVendorType() != null && validation.getVendorType().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getVendorType(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getLocality() != null && validation.getLocality().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getLocality(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getApproach() != null && validation.getApproach().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getApproach(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getShipmentTransfer() != null && validation.getShipmentTransfer().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getShipmentTransfer(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getPartnerWithOtherEcommerce() != null && validation.getPartnerWithOtherEcommerce().length() > 0) {
                                    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                    Toast.makeText(mcontext, validation.getPartnerWithOtherEcommerce(), Toast.LENGTH_SHORT).show();
                                }
                                if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                                    etStorename.setError(validation.getStoreName());
                                    etStorename.requestFocus();
                                    // return false;
                                }
                                if (validation.getIfGst() != null && validation.getIfGst().length() > 0) {
                                    edit_gstn.setError(validation.getIfGst());
                                    edit_gstn.requestFocus();
                                }
                                if (validation.getDc() != null && validation.getDc().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getDc(), Toast.LENGTH_LONG).show();
                                }


                                if (validation.getEmail() != null && validation.getEmail().length() > 0) {
                                    etEmail.setError(validation.getEmail());
                                    etEmail.requestFocus();
                                    // return false;
                                }

                                if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();

                                    // return false;
                                }
                                if (validation.getStoreTypeConfig() != null && validation.getStoreTypeConfig().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getStoreTypeConfig(), Toast.LENGTH_LONG).show();

                                    // return false;
                                }
                                if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();

                                    // return false;
                                } else {
                                    // showAlert(v, getUserDetailResponse.getResponse(), false);
                                }

                            } else {
                                // showAlert(v, getUserDetailResponse.getResponse(), false);
                            }
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2030" + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode :- 2030" + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }


    @Override
    public void onClick(View view, int position) {


    }

    @Override
    public void onLongClick(View view, int position, String data) {

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


    private boolean validation(int i) {
        if (i == 1) {
            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                etEmail.requestFocus();
                etEmail.setError("Provide Email");
                // showMSG(false, "dProvide Email");
                return false;
            }


            if (!Constants.isEmailValid(etEmail.getText().toString())) {
                //_editTextMobile.setError("Provide Valid email");
                // showMSG(false, "Provide Valid Email");
                etEmail.setError("Provide valid email");
                etEmail.requestFocus();
                return false;
            }


            if (TextUtils.isEmpty(etStorename.getText().toString())) {
                etStorename.requestFocus();
                //showMSG(false, "Provide Store name");
                etStorename.setError("Provide store name");
                return false;
            }
            if (!isFirstnameValid(etStorename.getText().toString())) {
                etStorename.requestFocus();
                //showMSG(false, "Provide Store name");
                etStorename.setError("Provide valid store name");
                return false;
            }
            if (TextUtils.isEmpty(etOwnerName.getText().toString())) {
                etOwnerName.requestFocus();
                etOwnerName.setError("Provide Owner Name");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (!isFirstnameValid(etOwnerName.getText().toString())) {
                etOwnerName.requestFocus();
                //showMSG(false, "Provide Store name");
                etOwnerName.setError("Provide valid owner name");
                return false;
            }
            if (tvDOB.getText().equals("Date of Birth")) {
                Toast.makeText(getActivity(), "Provide Date of Birth", Toast.LENGTH_SHORT).show();
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etStoreShopNo.getText().toString())) {
                etStoreShopNo.requestFocus();
                etStoreShopNo.setError("Provide store address");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(etStoreStreet.getText().toString())) {
                etStoreStreet.requestFocus();
                etStoreStreet.setError("Provide store street");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(etStoreLandmark.getText().toString())) {
                etStoreLandmark.requestFocus();
                etStoreLandmark.setError("Provide landmark");
                //showMSG(false, "Provide Store address");
                return false;
            }
            if (TextUtils.isEmpty(etStorePincode.getText().toString())) {
                etStorePincode.requestFocus();
                etStorePincode.setError("Provide pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etStoreCity.getText().toString())) {
                etStoreCity.requestFocus();
                etStoreCity.setError("Provide City");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etStoreState.getText().toString())) {
                etStoreState.requestFocus();
                etStoreState.setError("Provide State");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etRoute.getText().toString())) {
                etRoute.requestFocus();
                etRoute.setError("Provide Route");
                // showMSG(false, "Provide Pincode");
                return false;
            }

        } else if (i == 2) {
            if (TextUtils.isEmpty(etCurrentShopNo.getText().toString())) {
                etCurrentShopNo.requestFocus();
                etCurrentShopNo.setError("Provide Residential Address");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentStreet.getText().toString())) {
                etCurrentStreet.requestFocus();
                etCurrentStreet.setError("Provide Residential Street");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etCurrentLandmark.getText().toString())) {
                etCurrentLandmark.requestFocus();
                etCurrentLandmark.setError("Provide Residential Landmark");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentPincode.getText().toString())) {
                etCurrentPincode.requestFocus();
                etCurrentPincode.setError("Provide Residential pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentCity.getText().toString())) {
                etCurrentCity.requestFocus();
                etCurrentCity.setError("Provide Residential City");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etCurrentState.getText().toString())) {
                etCurrentState.requestFocus();
                etCurrentState.setError("Provide Residential State");
                // showMSG(false, "Provide Pincode");
                return false;
            }

            if (TextUtils.isEmpty(etPerShopNo.getText().toString())) {
                etPerShopNo.requestFocus();
                etPerShopNo.setError("Provide Permanent Address");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerStreet.getText().toString())) {
                etPerStreet.requestFocus();
                etPerStreet.setError("Provide Permanent Address Street");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerLandmark.getText().toString())) {
                etPerLandmark.requestFocus();
                etPerLandmark.setError("Provide Permanent Address Landmark");
                // showMSG(false, "Provide Pincode");
                return false;
            }


            if (TextUtils.isEmpty(etPerPincode.getText().toString())) {
                etPerPincode.requestFocus();
                etPerPincode.setError("Provide Permanent Address Pincode");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerCity.getText().toString())) {
                etPerCity.requestFocus();
                etPerCity.setError("Provide Permanent Address City");
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (TextUtils.isEmpty(etPerState.getText().toString())) {
                etPerState.requestFocus();
                etPerState.setError("Provide Permanent Address State");
                // showMSG(false, "Provide Pincode");
                return false;
            }


        } else {
            if (tvTypeofVendor.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Select Type of Vendor", Toast.LENGTH_LONG).show();
                return false;
            }

            if (tvVendorTypeDetail.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Select Vendor type Detail", Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(tvVendorTypeDetail.getText().toString())) {
                Toast.makeText(getActivity(), "Select Vendor type Detail", Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(tvStoreType.getText().toString())) {
              /*  tvStoreType.requestFocus();
                tvStoreType.setError("Provide Store Type");*/
                Toast.makeText(getActivity(), "Please Select Store Type", Toast.LENGTH_LONG).show();
                return false;
            }
            if (tvStoreType.getText().toString().equalsIgnoreCase("Select Store Type")) {
                Toast.makeText(getActivity(), "Please Select Store Type", Toast.LENGTH_LONG).show();
                return false;
            }
            if (tvLocality.getText().equals("")) {
                //  tvLocality.requestFocus();
                Toast.makeText(getActivity(), "Select Locality", Toast.LENGTH_LONG).show();
                // showMSG(false, "Provide Pincode");
                //   tvStoreType.setError("Select Locality");
                return false;
            }
            if (tvApproach.getText().equals("")) {
               /* tvApproach.requestFocus();
                // Toast.makeText(getActivity(), "Select Approach", Toast.LENGTH_SHORT).show();
                tvApproach.setError("Select Approach");*/
                Toast.makeText(getActivity(), "Select Approach", Toast.LENGTH_LONG).show();

                return false;
            }


            if (listLanaguage.size() == 0) {
                Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_LONG).show();
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (tvShipment.getText().toString().equalsIgnoreCase("")) {
                // tvShipment.setError("Select Shipment Transfer");
                Toast.makeText(getActivity(), "Please Select Shipment Transfer", Toast.LENGTH_LONG).show();

                //    tvShipment.requestFocus();
                // showMSG(false, "Provide Pincode");
                return false;
            }
            if (isgsttn.equalsIgnoreCase("yes")) {
                if (gstPath.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "please GST Certificate licence", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            if (isgsttn.equalsIgnoreCase("yes")) {
                if (TextUtils.isEmpty(edit_gstn.getText().toString())) {
                    edit_gstn.requestFocus();
                    edit_gstn.setError("Provide GSTN number");
                    // showMSG(false, "Provide GSTN number");
                    return false;
                }
            }
            Matcher gstMatcher = Constants.GST_PATTERN.matcher(edit_gstn.getText().toString());
            if (isgsttn.equalsIgnoreCase("yes") && !gstMatcher.matches()) {
                edit_gstn.setError("Provide valid GST no.");
                //showMSG(false, "Provide Valid GST No.");
                edit_gstn.requestFocus();
                return false;
            }
           /* if (etLicenceNumeber.getText().toString().equalsIgnoreCase("")) {
                etLicenceNumeber.setError("Provide Licence no.");
                etLicenceNumeber.requestFocus();
                return false;
            }*/


            if (listStoreType.size() == 0) {
                Toast.makeText(getActivity(), "Please Select Store Type", Toast.LENGTH_LONG).show();
                // showMSG(false, "Provide Pincode");
                return false;
            }
        }
        return true;
    }


    // get Vendor Type APi Call
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
                            CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendor);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTypeofVendor.setAdapter(spinnerArrayAdapter);
                            spTypeofVendor.setSelection(0);
                           */
                            select_Store_type = getVendorTypeDetails.getStoreTypeConfig();
                            checkedStoreType = new boolean[select_Store_type.size()];
                        } else if (getVendorTypeDetails.getResponseCode() == 411) {
                            sessionManager.logoutUser(mcontext);
                        } else {
                            Toast.makeText(getActivity(), getVendorTypeDetails.getResponse(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetVendorTypeDetails> call, Throwable t) {
                Toast.makeText(getActivity(), "#errorcode :- 2014 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }


    // submit Details Last Api
    public void bizDetailsSubmit(final View v) {

        Call<GetUserDetailResponse> call;
        MultipartBody.Part typedFile = null;
        gps = new GPSTracker(getActivity());
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
        user.put(Constants.PARAM_EMAIL, "" + etEmail.getText());
        user.put(Constants.PARAM_IF_GST, isgsttn);
        user.put(Constants.PARAM_GSTN, "" + edit_gstn.getText());
        user.put(Constants.PARAM_PINCODE, "" + etStorePincode.getText());
        user.put(Constants.PARAM_DC, "" + dc.getSelectedItem());
        user.put(Constants.PARAM_STATE, "" + etStoreState.getText());
        user.put(Constants.PARAM_CITY, "" + etStoreCity.getText());
        user.put(Constants.PARAM_STORE_NAME, "" + etStorename.getText());
        user.put(Constants.PARAM_STORE_ADDRESS, "" + etStoreShopNo.getText());
        user.put(Constants.PARAM_STORE_ADDRESS1, "" + etStoreStreet.getText());
        user.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, "" + etStoreLandmark.getText());
        user.put(Constants.PARAM_LATITUDE, "" + lattitude);
        user.put(Constants.PARAM_LONGITUDE, "" + longitude);

        if (!TextUtils.isEmpty(etLicenceNumeber.getText().toString())) {
            user.put(Constants.PARAM_LICENCE_NO, "" + etLicenceNumeber.getText());
        } else {
            user.put(Constants.PARAM_LICENCE_NO, "");
        }

        //new Update
        user.put(Constants.PARAM_OWNER_NAME, "" + etOwnerName.getText());
        user.put(Constants.PARAM_DOB, "" + stringDOB);
        user.put(Constants.PARAM_ROUTE, "R-" + etRoute.getText());
        user.put(Constants.PARAM_RESIDENTIAL_ADDRESS, "" + etCurrentShopNo.getText());
        user.put(Constants.PARAM_RESIDENTIAL_ADDRESS1, "" + etCurrentStreet.getText());
        user.put(Constants.PARAM_RESIDENTIAL_LANDMARK, "" + etCurrentLandmark.getText());
        user.put(Constants.PARAM_RESIDENTIAL_CITY, "" + etCurrentCity.getText());
        user.put(Constants.PARAM_RESIDENTIAL_PINCODE, "" + etCurrentPincode.getText());
        user.put(Constants.PARAM_RESIDENTIAL_STATE, "" + etCurrentState.getText());


        user.put(Constants.PARAM_PERMANENT_ADDRESS, "" + etPerShopNo.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS1, "" + etPerStreet.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_LANDMARK, "" + etPerLandmark.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_PINCODE, "" + etPerPincode.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_CITY, "" + etPerCity.getText());
        user.put(Constants.PARAM_PERMANENT_ADDRESS_STATE, "" + etPerState.getText());


        user.put(Constants.PARAM_VENDOR_TYPE, "" + tvTypeofVendor.getText());
        user.put(Constants.PARAM_VENDOR_TYPE_DETAIL, "" + tvVendorTypeDetail.getText());
        user.put(Constants.PARAM_LOCALITY, "" + tvLocality.getText());
        user.put(Constants.PARAM_APPROACH, "" + tvApproach.getText());
        user.put(Constants.PARAM_LANGUAGES, "" + tvLanguage.getText());
        user.put(Constants.PARAM_SHIPMENT_TRANS, "" + tvShipment.getText());
        user.put(Constants.PARAM_PARTNER_WITH_OTHER, "" + isPartnered);
        user.put(Constants.PARAM_STORE_TYPE_CONFIG, "" + tvStoreType.getText());


        Log.e("User Date", "Edit info" + user);
        Log.e("User Date", "Edit info" + str_process_id + "   " + sessionManager.getAgentID());


        if (isgsttn.equalsIgnoreCase("yes")) {
            File imagefile = new File(gstPath);
            typedFile = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstPath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));
            call = ApiClient.getClient().create(ApiInterface.class).submitBizDetailsGST("Bearer " + sessionManager.getToken(), user, typedFile);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).submitBizDetails("Bearer " + sessionManager.getToken(), user);

        }

        call.enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());

                        showAlert(getUserDetailResponse.getResponse());
                        // commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    }
                    if (getUserDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(getActivity());
                    }

                    if (getUserDetailResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (getUserDetailResponse.getValidation() != null) {
                            Validation validation = getUserDetailResponse.getValidation();
                            if (validation.getOwnerName() != null && validation.getOwnerName().length() > 0) {
                                etOwnerName.setError(validation.getOwnerName());
                                etOwnerName.requestFocus();
                                Toast.makeText(getActivity(), validation.getOwnerName(), Toast.LENGTH_SHORT).show();
                                // return false;
                            }
                            if (validation.getDob() != null && validation.getDob().length() > 0) {
                              /*  tvDob.setError(validation.getOwnerName());
                                tvDob.requestFocus();*/
                                // return false;
                            }
                            if (validation.getVendorTypeDetail() != null && validation.getVendorTypeDetail().length() > 0) {
                                Toast.makeText(getActivity(), validation.getVendorTypeDetail(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }

                            if (validation.getRoute() != null && validation.getRoute().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etRoute.setError(validation.getRoute());
                                etRoute.requestFocus();

                            }
                            if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                Toast.makeText(getActivity(), validation.getGstCertificateImage(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getGstn() != null && validation.getGstn().length() > 0) {
                                Toast.makeText(getActivity(), validation.getGstn(), Toast.LENGTH_LONG).show();
                                edit_gstn.setError(validation.getGstn());
                                edit_gstn.requestFocus();
                            }

                            if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                            /*    edit_storeaddress.setError(validation.getStoreAddress());
                                edit_storeaddress.requestFocus();*/

                            }
                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                              /*  //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStreet.setError(validation.getStoreAddress1());
                                etStreet.requestFocus();
*/
                                Toast.makeText(getActivity(), validation.getStoreAddress1(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etLandmark.setError(validation.getStoreAddressLandmark());
                                etLandmark.requestFocus();*/
                            }
                            if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
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
                            if (validation.getResidentialAddress() != null && validation.getResidentialAddress().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialAddress.setError(validation.getResidentialAddress());
                                etResidentialAddress.requestFocus();*/
                            }
                            if (validation.getResidentialAddress1() != null && validation.getResidentialAddress1().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etResidentialStreet.setError(validation.getResidentialAddress1());
                                etResidentialStreet.requestFocus();*/
                            }
                            if (validation.getResidentialAddressLandmark() != null && validation.getResidentialAddressLandmark().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialLandmark.setError(validation.getResidentialAddressLandmark());
                                etResidentialLandmark.requestFocus();*/
                            }
                            if (validation.getResidentialAddresspicode() != null && validation.getResidentialAddresspicode().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialPincode.setError(validation.getResidentialAddresspicode());
                                etResidentialPincode.requestFocus();*/
                            }
                            if (validation.getResidentialAddressCity() != null && validation.getResidentialAddressCity().length() > 0) {
                               /* //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialCity.setError(validation.getResidentialAddressCity());
                                etResidentialCity.requestFocus();*/
                            }
                            if (validation.getResidentialAddressState() != null && validation.getResidentialAddressState().length() > 0) {
                            /*    //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialState.setError(validation.getResidentialAddressState());
                                etResidentialState.requestFocus();*/
                            }

                            if (validation.getPermanentAddress() != null && validation.getPermanentAddress().length() > 0) {
                             /*   //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermententAdd.setError(validation.getPermanentAddress());
                                etPermententAdd.requestFocus();*/
                            }

                            if (validation.getPermanentAddress1() != null && validation.getPermanentAddress1().length() > 0) {
                              /*  //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentStreet.setError(validation.getPermanentAddress1());
                                etPermanentStreet.requestFocus();*/
                            }
                            if (validation.getPermanentAddressLandmark() != null && validation.getPermanentAddressLandmark().length() > 0) {
                             /*   //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentLandmark.setError(validation.getPermanentAddressLandmark());
                                etPermanentLandmark.requestFocus();*/
                            }
                            if (validation.getPermanentAddressPicode() != null && validation.getPermanentAddressPicode().length() > 0) {
                             /*   //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentPincode.setError(validation.getPermanentAddressPicode());
                                etPermanentPincode.requestFocus();*/
                            }
                            if (validation.getPermanentAddressCity() != null && validation.getPermanentAddressCity().length() > 0) {
                               /* //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentCity.setError(validation.getPermanentAddressCity());
                                etPermanentCity.requestFocus();*/
                            }
                            if (validation.getPermanentAddressState() != null && validation.getPermanentAddressState().length() > 0) {
                              /*  //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentState.setError(validation.getPermanentAddressState());
                                etPermanentState.requestFocus();*/
                            }
                            if (validation.getVendorType() != null && validation.getVendorType().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getVendorType(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLocality() != null && validation.getLocality().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getLocality(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getApproach() != null && validation.getApproach().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getApproach(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getShipmentTransfer() != null && validation.getShipmentTransfer().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getShipmentTransfer(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getPartnerWithOtherEcommerce() != null && validation.getPartnerWithOtherEcommerce().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(mcontext, validation.getPartnerWithOtherEcommerce(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                               /* edit_storename.setError(validation.getStoreName());
                                edit_storename.requestFocus();*/
                                // return false;
                            }
                            if (validation.getIfGst() != null && validation.getIfGst().length() > 0) {
                                edit_gstn.setError(validation.getIfGst());
                                edit_gstn.requestFocus();
                                Toast.makeText(getActivity(), validation.getIfGst(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                Toast.makeText(getActivity(), validation.getDc(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getEmail() != null && validation.getEmail().length() > 0) {
                              /*  edit_email.setError(validation.getEmail());
                                edit_email.requestFocus();
                                // return false;*/
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getStoreTypeConfig() != null && validation.getStoreTypeConfig().length() > 0) {
                                Toast.makeText(getActivity(), validation.getStoreTypeConfig(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();

                                // return false;
                            }/* else {
                                Toast.makeText(getActivity(), getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                                Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                            }*/

                        } else {
                            Toast.makeText(getActivity(), getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();

                            Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                        }

                    } else {
                        Toast.makeText(getActivity(), getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Constants.showAlert(v, getUserDetailResponse.getResponse(), true);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        }
                    }
                } else {
                    Constants.showAlert(v, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void commanFragmentCallWithoutBackStack2(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMP_MOBILE, "is_edit");
            bundle.putString(Constants.KEY_EMAIL, etEmail.getText().toString() + "");
            bundle.putString(Constants.KEY_STORES, etStorename.getText().toString() + "");
            bundle.putString(Constants.KEY_NAME, etOwnerName.getText().toString() + "");
            bundle.putString(Constants.PARAM_BIRTH_DATE, tvDOB.getText().toString() + "");
            bundle.putString(Constants.PARAM_STORE_ADDRESS, etStoreShopNo.getText().toString() + "");
            bundle.putString(Constants.PARAM_STORE_ADDRESS1, etStoreStreet.getText().toString() + "");
            bundle.putString(Constants.PARAM_STORE_ADDRESS_LANDMARK, etStoreLandmark.getText().toString() + "");
            bundle.putString(Constants.PARAM_PINCODE, etStorePincode.getText().toString() + "");
            bundle.putString(Constants.PARAM_CITY, etStoreCity.getText().toString() + "");
            bundle.putString(Constants.PARAM_STATE, etStoreState.getText().toString() + "");
            //   bundle.putString(Constants.PARAM_DC, dc.getSelectedItem().toString() + "");
            bundle.putString(Constants.PARAM_ROUTE, etRoute.getText().toString() + "");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }


    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    private void showAlert(String msg) {
        Button btSubmit;
        TextView tvMessage, tvTitle;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mcontext);
        dialog = new Dialog(getActivity(), R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Vendor Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commanFragmentCallWithoutBackStack(new DocUploadFragment());

            }
        });

        dialog.setCancelable(false);

        dialog.show();

    }


    private final void focusOnView(String selectedString) {
        scMain.post(new Runnable() {
            @Override
            public void run() {
                if (selectedString.equalsIgnoreCase("1")) {
                    scMain.scrollTo(0, tvAddressInfo.getBottom());
                } else {
                    scMain.scrollTo(0, tvGeneralInfo.getBottom());
                }

            }
        });
    }


    // capture GST Image
    private void capture_gst_image() {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                IMAGE_SELCTION_CODE = IMAGE_GST;
                Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                startActivityForResult(chooseImageIntent, IMAGE_GST);

            }

            @Override
            public void onFailed() {
                Toast.makeText(getActivity(), "request permission failed", Toast.LENGTH_SHORT).show();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == IMAGE_GST) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                gstPath = ImagePicker.getBitmapPath(bitmap, getActivity());
                Glide.with(getActivity()).load(gstPath).placeholder(R.drawable.placeholder)
                        .into(ivGst);
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


    public boolean isFirstnameValid(String text) {

        return text.matches("^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$");
    }


}



