package com.canvascoders.opaper.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetVendorTypeDetails;
import com.canvascoders.opaper.Beans.LanguageModel;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.MyLanguageAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.utils.Constants.showAlert;

public class InfoFragment extends Fragment implements View.OnClickListener {

    private EditText etOwnerName, edit_fathername, edit_email, etRoute, etResidentialAddress,etResidentialStreet,etResidentialLandmark,etResidentialPincode,etResidentialCity,etResidentialState,etPermententAdd,etPermanentStreet,etPermanentLandmark,etPermanentPincode,etPermanentCity,etPermanentState, edit_storename, edit_storeaddress,etStreet,etLandmark, edit_pincode, edit_gstn, edit_city, edit_state, edit_licenceno;
    private Toolbar toolbar;
    private TextView tvLanguage, tvDob,tvStoreType;
    private SwitchCompat switchGst, switchPartner;
    private String TAG = "InfoFragment";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    private Button btn_next, btChangeChaque;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private LinearLayout llDOB;
    boolean[] checkedItems;
    private String same_address = "0",pincode;
    boolean[] checkedStoreType;
    private String lattitude="",longitude="";
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
    private Spinner dc, spLocality, spTypeofVendor,spTypeofVendorDetail, spApproach, spShipment, spLanguage;
    private List<String> shipmentmodeltype = new ArrayList<>();
    private List<String> type_of_vendor = new ArrayList<>();
    private List<String> type_of_vendorDetail = new ArrayList<>();
    private List<String> locality = new ArrayList<>();
    private List<String> approach = new ArrayList<>();
    private String isgsttn = "no";
    private String isPartnered = "no";
    private RelativeLayout rvSelectLanguage,rvSelectStoretype;
    private ArrayList<String> dcLists = new ArrayList<>();
    String[] select_language = {
            "English", "Assamese", "Bengali", "Gujarati", "Hindi",
            "Kannada", "Kashmiri", "Konkani", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Sanskrit", "Sindhi", "Tamil", "Telugu", "Urdu", "Bodo", "Santhali", "Maithili", "Dogri"};
    ArrayList<String> listLanaguage = new ArrayList<>();
    ArrayList<String> listVendorType = new ArrayList<>();
    String str_process_id;
    List<String> select_Store_type;
    private AppCompatCheckBox chk_cur_addr;
    Context mcontext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        OTPActivity.settitle(Constants.TITLE_VENDOR_DETAIL_MENSA);

        initView();
        type_of_vendor = Arrays.asList(getResources().getStringArray(R.array.TypeofVendor));
        CustomAdapter<String> spTypeofVendorAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendor);
        spTypeofVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeofVendor.setAdapter(spTypeofVendorAdapter);

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

        return view;
    }


    private void initView() {
        dc = (Spinner) view.findViewById(R.id.dc);
        tvLanguage = view.findViewById(R.id.tvLanguage);
        tvStoreType = view.findViewById(R.id.tvStoreType);
        tvLanguage.setOnClickListener(this);
        llDOB = view.findViewById(R.id.llDOB);
        llDOB.setOnClickListener(this);
        tvDob = view.findViewById(R.id.tvDOB);
        rvSelectLanguage = view.findViewById(R.id.rvSelectLanguage);
        rvSelectLanguage.setOnClickListener(this);
        rvSelectStoretype = view.findViewById(R.id.rvSelectStoretype);
        rvSelectStoretype.setOnClickListener(this);
        spTypeofVendor = (Spinner) view.findViewById(R.id.snTypeofVendor);
        spTypeofVendorDetail = (Spinner) view.findViewById(R.id.snTypeofVendorDetail);

        spTypeofVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spTypeofVendor.getSelectedItem().equals("Physical Shop")){
                    type_of_vendorDetail = Arrays.asList(getResources().getStringArray(R.array.PhysicalShop));
                    CustomAdapter<String> spTypeofVendorAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendorDetail);
                    spTypeofVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTypeofVendorDetail.setAdapter(spTypeofVendorAdapter);
                }
                else if(spTypeofVendor.getSelectedItem().equals("Individual")){
                    type_of_vendorDetail = Arrays.asList(getResources().getStringArray(R.array.Individual));
                    CustomAdapter<String> spTypeofVendorAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendorDetail);
                    spTypeofVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTypeofVendorDetail.setAdapter(spTypeofVendorAdapter);

                }
                else if (spTypeofVendor.getSelectedItem().equals("Company/Firm")){
                    type_of_vendorDetail = Arrays.asList(getResources().getStringArray(R.array.Company));
                    CustomAdapter<String> spTypeofVendorAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendorDetail);
                    spTypeofVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTypeofVendorDetail.setAdapter(spTypeofVendorAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



       spLocality = (Spinner) view.findViewById(R.id.snLocality);
        spApproach = (Spinner) view.findViewById(R.id.snApproach);
        spShipment = (Spinner) view.findViewById(R.id.snShipment);
        // spLanguage = (Spinner)view.findViewById(R.id.snLanguage);
        // select_language = getResources().getStringArray(R.array.shopping_item);
        checkedItems = new boolean[select_language.length];



        chk_cur_addr = (AppCompatCheckBox) view.findViewById(R.id.chk_cur_addr);


        btn_next = view.findViewById(R.id.btn_next);
        btChangeChaque = view.findViewById(R.id.btEditCheque);
        btChangeChaque.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Please wait submitting vendor details");
        mProgressDialog.setCancelable(false);
        etOwnerName = view.findViewById(R.id.etOwnerName);
        etOwnerName.setFilters(new InputFilter[]{filter});
        edit_fathername = (EditText) view.findViewById(R.id.edit_fathername);
        edit_fathername.setFilters(new InputFilter[]{filter});
        edit_email = (EditText) view.findViewById(R.id.edit_email);
        etRoute = view.findViewById(R.id.etRoute);
        etResidentialAddress = view.findViewById(R.id.etResidential);
        etResidentialStreet = view.findViewById(R.id.etResidentialStreet);
        etResidentialLandmark = view.findViewById(R.id.etResidentialLandmark);
        etResidentialPincode = view.findViewById(R.id.etResidentialPincode);
        etResidentialCity = view.findViewById(R.id.etResidentialCity);
        etResidentialState = view.findViewById(R.id.etResidentialState);
        etPermententAdd = view.findViewById(R.id.etPermanent);
        etPermanentStreet = view.findViewById(R.id.etPermanentStreet);
        etPermanentLandmark = view.findViewById(R.id.etPermanentLandmark);
        etPermanentPincode = view.findViewById(R.id.etPermanentPincode);
        etPermanentCity = view.findViewById(R.id.etPermanentCity);
        etPermanentState = view.findViewById(R.id.etPermanentState);

        chk_cur_addr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    same_address = "1";
                    etPermententAdd.setText(etResidentialAddress.getText().toString().trim());
                    etPermanentStreet.setText(etResidentialStreet.getText().toString().trim());
                    etPermanentLandmark.setText(etResidentialLandmark.getText().toString().trim());
                    etPermanentPincode.setText(etResidentialPincode.getText().toString().trim());
                    etPermanentCity.setText(etResidentialCity.getText().toString().trim());
                    etPermanentState.setText(etResidentialState.getText().toString().trim());

                } else {
                    same_address = "0";
                }
            }
        });

        edit_storename = (EditText) view.findViewById(R.id.edit_storename);
        edit_storename.setFilters(new InputFilter[]{filter});
        edit_storeaddress = (EditText) view.findViewById(R.id.edit_storeaddress);
        etStreet = view.findViewById(R.id.etStreet);
        etLandmark = view.findViewById(R.id.etLandmark);
        edit_pincode = (EditText) view.findViewById(R.id.edit_pincode);
        edit_gstn = (EditText) view.findViewById(R.id.edit_gstn);
        tvPartner = (TextView) view.findViewById(R.id.tvPartnerWith);
        switchGst = (SwitchCompat) view.findViewById(R.id.switch_gst);
        switchPartner = (SwitchCompat) view.findViewById(R.id.switch_partner);
        edit_city = (EditText) view.findViewById(R.id.edit_city);
        edit_state = (EditText) view.findViewById(R.id.edit_state);
        edit_licenceno = (EditText) view.findViewById(R.id.edit_licenceno);

        switchGst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                edit_gstn.setEnabled(isChecked);
                if (isChecked) {
                    isgsttn = "yes";
                } else {
                    isgsttn = "no";
                    edit_gstn.setHint("GSTN");
                }
            }
        });

        switchPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                tvPartner.setEnabled(isChecked);
                if (isChecked) {
                    isPartnered = "yes";
                } else {
                    isPartnered = "no";

                }
            }
        });
        btn_next.setOnClickListener(this);
        edit_pincode.addTextChangedListener(new TextWatcher() {
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
                    addDC(s.toString(),pincode);
                }
            }
        });


        etPermanentPincode.addTextChangedListener(new TextWatcher() {
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
                    addDC(s.toString(),pincode);
                }
            }
        });


        etResidentialPincode.addTextChangedListener(new TextWatcher() {
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
                    addDC(s.toString(),pincode);
                }
            }
        });




        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetVendorType();
        }
        else{
            Constants.ShowNoInternet(getActivity());
        }
    }

    private void ApiCallGetVendorType() {
        Call<GetVendorTypeDetails> call = ApiClient.getClient().create(ApiInterface.class).getVendorType("Bearer "+sessionManager.getToken(),str_process_id);
        call.enqueue(new Callback<GetVendorTypeDetails>() {
            @Override
            public void onResponse(Call<GetVendorTypeDetails> call, Response<GetVendorTypeDetails> response) {
                if(response.isSuccessful()){
                    try {
                        GetVendorTypeDetails getVendorTypeDetails = response.body();
                        if (getVendorTypeDetails.getResponseCode() == 200) {
                           /* type_of_vendor.addAll(getVendorTypeDetails.getData());
                            CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, type_of_vendor);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTypeofVendor.setAdapter(spinnerArrayAdapter);
                            spTypeofVendor.setSelection(0);*/
                            select_Store_type=getVendorTypeDetails.getStoreTypeConfig();
                            checkedStoreType = new boolean[select_Store_type.size()];
                        }
                        else if (getVendorTypeDetails.getResponseCode()==411){
                            sessionManager.logoutUser(mcontext);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetVendorTypeDetails> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Constants.hideKeyboardwithoutPopulate(getActivity());


            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                if (validation()) bizDetailsSubmit(v);
            } else {
                Constants.ShowNoInternet(getActivity());
            }

        }


        if (v.getId() == R.id.rvSelectLanguage) {


            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
        }

        if(v.getId()== R.id.rvSelectStoretype){

            String[] arr = new String[select_Store_type.size()];
            for(int i=0 ; i< select_Store_type.size();i++){
                arr[i] = select_Store_type.get(i);
            }
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Select Store Type");
            mBuilder.setMultiChoiceItems(arr, checkedStoreType, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b) {
                        listVendorType.add(select_Store_type.get(i));
                    } else {
                        listVendorType.remove(select_Store_type.get(i));
                    }
                }
            });
            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    String item = "";
                    for (int i = 0; i < listVendorType.size(); i++) {
                        item = item + listVendorType.get(i);
                        if (i != listVendorType.size() - 1) {
                            item = item + ",";
                        }
                    }
                    if (listVendorType.size() == 0) {
                        tvStoreType.setText("Select Store Type");
                    } else {
                        tvStoreType.setText(item);
                    }
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }

        if (v.getId() == R.id.llDOB) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

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

                            tvDob.setText(year + "-" + monthString + "-" + daysString);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(minDate);
            datePickerDialog.show();
        }

        if (v.getId() == R.id.btEditCheque) {
            commanFragmentCallWithoutBackStack2(new PanVerificationFragment());
        }
    }





    private boolean validation() {
        if (TextUtils.isEmpty(edit_email.getText().toString())) {
            edit_email.requestFocus();
            edit_email.setError("Provide Email");
            // showMSG(false, "dProvide Email");
            return false;
        }
        if (!Constants.isEmailValid(edit_email.getText().toString())) {
            //_editTextMobile.setError("Provide Valid email");
            // showMSG(false, "Provide Valid Email");
            edit_email.setError("Provide valid email");
            edit_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edit_storename.getText().toString())) {
            edit_storename.requestFocus();
            //showMSG(false, "Provide Store name");
            edit_storename.setError("Provide store name");
            return false;
        }
        if (TextUtils.isEmpty(etOwnerName.getText().toString())) {
            etOwnerName.requestFocus();
            etOwnerName.setError("Provide Owner Name");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (tvDob.getText().equals("Date of Birth")) {
            Toast.makeText(getActivity(), "Provide Date of Birth", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(edit_storeaddress.getText().toString())) {
            edit_storeaddress.requestFocus();
            edit_storeaddress.setError("Provide store address");
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etStreet.getText().toString())) {
            etStreet.requestFocus();
            etStreet.setError("Provide store street");
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(etLandmark.getText().toString())) {
            etLandmark.requestFocus();
            etLandmark.setError("Provide landmark");
            //showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(edit_pincode.getText().toString())) {
            edit_pincode.requestFocus();
            edit_pincode.setError("Provide pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(edit_city.getText().toString())) {
            edit_city.requestFocus();
            edit_city.setError("Provide City");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(edit_state.getText().toString())) {
            edit_state.requestFocus();
            edit_state.setError("Provide State");
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (TextUtils.isEmpty(etRoute.getText().toString())) {
            etRoute.requestFocus();
            etRoute.setError("Provide Route");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etResidentialAddress.getText().toString())) {
            etResidentialAddress.requestFocus();
            etResidentialAddress.setError("Provide Residential Address");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etResidentialStreet.getText().toString())) {
            etResidentialStreet.requestFocus();
            etResidentialStreet.setError("Provide Residential Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etResidentialLandmark.getText().toString())) {
            etResidentialLandmark.requestFocus();
            etResidentialLandmark.setError("Provide Residential Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etResidentialPincode.getText().toString())) {
            etResidentialPincode.requestFocus();
            etResidentialPincode.setError("Provide Residential pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etResidentialCity.getText().toString())) {
            etResidentialCity.requestFocus();
            etResidentialCity.setError("Provide Residential City");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etResidentialState.getText().toString())) {
            etResidentialState.requestFocus();
            etResidentialState.setError("Provide Residential State");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (TextUtils.isEmpty(etPermententAdd.getText().toString())) {
            etPermententAdd.requestFocus();
            etPermententAdd.setError("Provide Permanent Address");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermanentStreet.getText().toString())) {
            etPermanentStreet.requestFocus();
            etPermanentStreet.setError("Provide Permanent Address Street");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermanentLandmark.getText().toString())) {
            etPermanentLandmark.requestFocus();
            etPermanentLandmark.setError("Provide Permanent Address Landmark");
            // showMSG(false, "Provide Pincode");
            return false;
        }


        if (TextUtils.isEmpty(etPermanentPincode.getText().toString())) {
            etPermanentPincode.requestFocus();
            etPermanentPincode.setError("Provide Permanent Address Pincode");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermanentCity.getText().toString())) {
            etPermanentCity.requestFocus();
            etPermanentCity.setError("Provide Permanent Address City");
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (TextUtils.isEmpty(etPermanentState.getText().toString())) {
            etPermanentState.requestFocus();
            etPermanentState.setError("Provide Permanent Address State");
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (spShipment.getSelectedItem().equals("Shipment Transfer")) {
            spShipment.requestFocus();
            Toast.makeText(getActivity(), "Select Shipment Transfer Type", Toast.LENGTH_SHORT).show();
            return false;
        }
      /*  if (TextUtils.isEmpty(edit_licenceno.getText().toString())) {
            edit_licenceno.requestFocus();
            edit_licenceno.setError("Provide Licence Number");
            // showMSG(false, "Provide Pincode");
            return false;
        }*/
        if (spTypeofVendor.getSelectedItem().equals("--Type of Vendor--")) {
            spTypeofVendor.requestFocus();
            Toast.makeText(getActivity(), "Select type of vendor", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (spLocality.getSelectedItem().equals("--Locality--")) {
            spLocality.requestFocus();
            Toast.makeText(getActivity(), "Select Locality", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (spApproach.getSelectedItem().equals("--Approach--")) {
            spApproach.requestFocus();
            Toast.makeText(getActivity(), "Select Approach", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        if (listLanaguage.size() == 0) {
            spApproach.requestFocus();
            Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (listVendorType.size() == 0) {

            Toast.makeText(getActivity(), "Please Select Store Type", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
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
            return false;
        }


        return true;
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) view.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) view.findViewById(R.id.txt_show_msg_fail);
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


    public void bizDetailsSubmit(final View v) {
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
        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_PROCESS_ID, str_process_id);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_EMAIL, "" + edit_email.getText());
        user.addProperty(Constants.PARAM_IF_GST, isgsttn);
        user.addProperty(Constants.PARAM_GSTN, "" + edit_gstn.getText());
        user.addProperty(Constants.PARAM_PINCODE, "" + edit_pincode.getText());
        user.addProperty(Constants.PARAM_DC, "" + dc.getSelectedItem());
        user.addProperty(Constants.PARAM_STATE, "" + edit_state.getText());
        user.addProperty(Constants.PARAM_CITY, "" + edit_city.getText());
        user.addProperty(Constants.PARAM_STORE_NAME, "" + edit_storename.getText());
        user.addProperty(Constants.PARAM_STORE_ADDRESS, "" + edit_storeaddress.getText());
        user.addProperty(Constants.PARAM_STORE_ADDRESS1, "" + etStreet.getText());
        user.addProperty(Constants.PARAM_STORE_ADDRESS_LANDMARK,""+etLandmark.getText());
       /* user.addProperty(Constants.PARAM_LATITUDE, "" + lattitude);
        user.addProperty(Constants.PARAM_LONGITUDE,""+longitude);*/

        if(!TextUtils.isEmpty(edit_licenceno.getText().toString())){
            user.addProperty(Constants.PARAM_LICENCE_NO, "" + edit_licenceno.getText());
        }
        else{
            user.addProperty(Constants.PARAM_LICENCE_NO, "");
        }

        //new Update
        user.addProperty(Constants.PARAM_OWNER_NAME, "" + etOwnerName.getText());
        user.addProperty(Constants.PARAM_DOB, "" + tvDob.getText());
        user.addProperty(Constants.PARAM_ROUTE, "" + etRoute.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_ADDRESS, "" + etResidentialAddress.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_ADDRESS1, "" + etResidentialStreet.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_LANDMARK, "" + etResidentialLandmark.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_CITY, "" + etResidentialCity.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_PINCODE, "" + etResidentialPincode.getText());
        user.addProperty(Constants.PARAM_RESIDENTIAL_STATE, "" + etResidentialState.getText());




        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS, "" + etPermententAdd.getText());
        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS1, "" + etPermanentStreet.getText());
        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_LANDMARK, "" + etPermanentLandmark.getText());
        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_PINCODE, "" + etPermanentPincode.getText());
        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_CITY, "" + etPermanentCity.getText());
        user.addProperty(Constants.PARAM_PERMANENT_ADDRESS_STATE, "" + etPermanentState.getText());



        user.addProperty(Constants.PARAM_VENDOR_TYPE, "" + spTypeofVendor.getSelectedItem());
        user.addProperty(Constants.PARAM_VENDOR_TYPE_DETAIL, "" + spTypeofVendorDetail.getSelectedItem());
        user.addProperty(Constants.PARAM_LOCALITY, "" + spLocality.getSelectedItem());
        user.addProperty(Constants.PARAM_APPROACH, "" + spApproach.getSelectedItem());
        user.addProperty(Constants.PARAM_LANGUAGES, "" + tvLanguage.getText());
        user.addProperty(Constants.PARAM_SHIPMENT_TRANS, "" + spShipment.getSelectedItem());
        user.addProperty(Constants.PARAM_PARTNER_WITH_OTHER, "" + isPartnered);
        user.addProperty(Constants.PARAM_STORE_TYPE_CONFIG, "" + tvStoreType.getText());


        Log.e("User Date", "Edit info" + user);
        Log.e("User Date", "Edit info" + str_process_id + "   " + sessionManager.getAgentID());


        ApiClient.getClient().create(ApiInterface.class).submitBizDetails("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());
                        commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    }
                    if (getUserDetailResponse.getResponseCode()==411){
                        sessionManager.logoutUser(getActivity());
                    }

                    if (getUserDetailResponse.getResponseCode() == 400) {

                        mProgressDialog.dismiss();
                        if (getUserDetailResponse.getValidation() != null) {
                            Validation validation = getUserDetailResponse.getValidation();
                            if (validation.getOwnerName() != null && validation.getOwnerName().length() > 0) {
                                etOwnerName.setError(validation.getOwnerName());
                                etOwnerName.requestFocus();
                                // return false;
                            }
                            if (validation.getDob() != null && validation.getDob().length() > 0) {
                                tvDob.setError(validation.getOwnerName());
                                tvDob.requestFocus();
                                // return false;
                            }
                            if (validation.getVendorTypeDetail() != null && validation.getVendorTypeDetail().length() > 0) {
                              Toast.makeText(getActivity(),validation.getVendorTypeDetail(),Toast.LENGTH_LONG).show();
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
                                edit_storeaddress.setError(validation.getStoreAddress());
                                edit_storeaddress.requestFocus();

                            }
                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStreet.setError(validation.getStoreAddress1());
                                etStreet.requestFocus();

                            }
                            if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etLandmark.setError(validation.getStoreAddressLandmark());
                                etLandmark.requestFocus();
                            }
                            if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                edit_pincode.setError(validation.getPincode());
                                edit_pincode.requestFocus();

                            }
                            if (validation.getCity() != null && validation.getCity().length() > 0) {

                                edit_city.setError(validation.getCity());
                                edit_city.requestFocus();
                            }
                            if (validation.getState() != null && validation.getState().length() > 0) {
                                edit_state.setError(validation.getState());
                                edit_state.requestFocus();
                            }
                            if (validation.getResidentialAddress() != null && validation.getResidentialAddress().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialAddress.setError(validation.getResidentialAddress());
                                etResidentialAddress.requestFocus();
                            }
                            if (validation.getResidentialAddress1() != null && validation.getResidentialAddress1().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialStreet.setError(validation.getResidentialAddress1());
                                etResidentialStreet.requestFocus();
                            }
                            if (validation.getResidentialAddressLandmark() != null && validation.getResidentialAddressLandmark().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialLandmark.setError(validation.getResidentialAddressLandmark());
                                etResidentialLandmark.requestFocus();
                            }
                            if (validation.getResidentialAddresspicode() != null && validation.getResidentialAddresspicode().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialPincode.setError(validation.getResidentialAddresspicode());
                                etResidentialPincode.requestFocus();
                            }
                            if (validation.getResidentialAddressCity() != null && validation.getResidentialAddressCity().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialCity.setError(validation.getResidentialAddressCity());
                                etResidentialCity.requestFocus();
                            }
                            if (validation.getResidentialAddressState() != null && validation.getResidentialAddressState().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialState.setError(validation.getResidentialAddressState());
                                etResidentialState.requestFocus();
                            }

                            if (validation.getPermanentAddress() != null && validation.getPermanentAddress().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermententAdd.setError(validation.getPermanentAddress());
                                etPermententAdd.requestFocus();
                            }

                            if (validation.getPermanentAddress1() != null && validation.getPermanentAddress1().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentStreet.setError(validation.getPermanentAddress1());
                                etPermanentStreet.requestFocus();
                            }
                            if (validation.getPermanentAddressLandmark() != null && validation.getPermanentAddressLandmark().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentLandmark.setError(validation.getPermanentAddressLandmark());
                                etPermanentLandmark.requestFocus();
                            }
                            if (validation.getPermanentAddressPicode() != null && validation.getPermanentAddressPicode().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentPincode.setError(validation.getPermanentAddressPicode());
                                etPermanentPincode.requestFocus();
                            }
                            if (validation.getPermanentAddressCity() != null && validation.getPermanentAddressCity().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentCity.setError(validation.getPermanentAddressCity());
                                etPermanentCity.requestFocus();
                            }
                            if (validation.getPermanentAddressState() != null && validation.getPermanentAddressState().length() > 0) {
                                //Toast.makeText(getActivity(),validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentState.setError(validation.getPermanentAddressState());
                                etPermanentState.requestFocus();
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
                                edit_storename.setError(validation.getStoreName());
                                edit_storename.requestFocus();
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
                                edit_email.setError(validation.getEmail());
                                edit_email.requestFocus();
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
                            }
                             else {
                                showAlert(v, getUserDetailResponse.getResponse(), false);
                            }

                        } else {
                            showAlert(v, getUserDetailResponse.getResponse(), false);
                        }

                    } else {
                        showAlert(v, getUserDetailResponse.getResponse(), true);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        }
                    }
                } else {
                    showAlert(v, getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void addDC(String pcode,String pincodenumber) {
        // state is DC and DC is state
        if(pincodenumber.equalsIgnoreCase("1")){
            dcLists.clear();
        }

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
                        if(pincodenumber.equalsIgnoreCase("1")) {
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                    dcLists.add(dc.getDc());
                                }
                                edit_state.setText(getUserDetails.getData().get(i).getState());
                                edit_city.setText(getUserDetails.getData().get(i).getCity());
                            }

                            CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, dcLists);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dc.setAdapter(spinnerArrayAdapter);
                            dc.setSelection(0);
                        }
                        if(pincodenumber.equalsIgnoreCase("2")){
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                etPermanentState.setText(getUserDetails.getData().get(i).getState());
                                etPermanentCity.setText(getUserDetails.getData().get(i).getCity());
                            }
                        }
                        if(pincodenumber.equalsIgnoreCase("3")){
                            for (int i = 0; i < getUserDetails.getData().size(); i++) {
                                etResidentialState.setText(getUserDetails.getData().get(i).getState());
                                etResidentialCity.setText(getUserDetails.getData().get(i).getCity());
                            }
                        }

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(mcontext);
                    } else {
                        Toast.makeText(mcontext, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
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
                ((TextView) view).setTextSize(12);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.rb_regular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.commit();

        }
    }

    public void commanFragmentCallWithoutBackStack2(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMP_MOBILE, "is_edit");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.commit();

        }
    }
}



