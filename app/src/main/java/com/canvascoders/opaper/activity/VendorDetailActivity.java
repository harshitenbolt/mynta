package com.canvascoders.opaper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.Result;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.SignedDocDetailResponse;
import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.Beans.VendorDetailResponse.VendorDetailResponse;
import com.canvascoders.opaper.Beans.VendorList;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditKycActivity;
import com.canvascoders.opaper.adapters.DocumentListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.ChequeDataListingFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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

public class VendorDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int CHEQUE_UPDATE_INTENT = 1001;
    VendorList vendor;
    private static final int DEFAULT_ZOOM = 15;
    TextView tv_ShopName, tv_Mobile, tv_Name, tvProcessId;
    ImageView ivVendorImage;
    Button btn_update_cheque, btn_update_store_details;
    LinearLayout linear_check;
    Button tvUpdateMobile, tvResendAgree, tvUpdatePan;
    TextView tvTitle, tvExpand;
    GoogleMap googleMap;
    String isUpdationRequired, allowDEdit, allowEditDelBoy, allowGSTIMAGE;
    SessionManager sessionManager;
    TextView tvAgreementExpirationDate;
    String process_id, attachment = "";
    DocumentListAdapter documentListAdapter;
    Context mcontext;
    private static Double mDefaultLatitude = 12.972442;
    private static Double mDefaultLongitude = 77.580643;
    LinearLayout llEdit;
    CardView cdEdit;
    private List<Result> docList = new ArrayList<>();
    GoogleMap map;
    private static Dialog dialog;
    ProgressDialog mProgress;
    CardView cvPan, cvMobile, cvCheque, cvRate, cvResignAgreement, cvDelBoy, cvAddGSTIMAGE;
    private ImageView ivBack, ivSupport;
    RecyclerView rvDocuments;
    AppCompatSpinner spYear;
    ProgressBar pbBar;
    LinearLayout llDisable, llDelBoy;
    CardView cvGST,cvKYC;
    LinearLayout llEnable;
    private TextView tvStoreName, tvAddress, tvNoData;
    boolean expand = false;
    Bitmap b, converted;
    RelativeLayout rvMainWithRect, rvMain;
    ImageView imageView, ivHome;
    Button ivSelect;
    FrameLayout flImage;
    String str_proccess = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail2);
        str_proccess = String.valueOf(getIntent().getIntExtra("data", 0));
        mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
        mProgress.setMessage("Please wait...");
        sessionManager = new SessionManager(this);
        ApiCallGetDetailofVendor();


    }

    private void ApiCallGetDetailofVendor() {
        mProgress.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, str_proccess);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).vendorDetailResponse("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<VendorDetailResponse>() {
            @Override
            public void onResponse(Call<VendorDetailResponse> call, Response<VendorDetailResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    VendorDetailResponse vendorDetailResponse = response.body();
                    vendor = vendorDetailResponse.getData().get(0);
                    init();
                } else {
                    Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2017 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<VendorDetailResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2017 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }


    private void ApiCallResendAgreement() {

        mProgress.show();
        ApiClient.getClient().create(ApiInterface.class).ResignAgreement("Bearer " + sessionManager.getToken(), process_id).enqueue(new Callback<ResignAgreementResponse>() {
            @Override
            public void onResponse(Call<ResignAgreementResponse> call, Response<ResignAgreementResponse> response) {
                if (response.isSuccessful()) {
                    mProgress.dismiss();
                    ResignAgreementResponse resignAgreementResponse = response.body();
                    if (resignAgreementResponse.getResponseCode() == 200) {
                        Toast.makeText(VendorDetailActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(VendorDetailActivity.this);
                    } else {
                        Toast.makeText(VendorDetailActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VendorDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResignAgreementResponse> call, Throwable t) {
                mProgress.dismiss();

            }
        });
    }


    private void APiCallGST(String gst) {
        mProgress.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(vendor.getProccessId()));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, gst);

        mProgress.show();
        ApiClient.getClient().create(ApiInterface.class).gstUpdate("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    mProgress.dismiss();

                    CommonResponse resignAgreementResponse = response.body();
                    if (resignAgreementResponse.getResponseCode() == 200) {
                        dialog.dismiss();

                        Toast.makeText(VendorDetailActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 400) {
                        Toast.makeText(VendorDetailActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else if (resignAgreementResponse.getResponseCode() == 411) {
                        dialog.dismiss();
                        sessionManager.logoutUser(VendorDetailActivity.this);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(VendorDetailActivity.this, resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2046 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dialog.dismiss();
                mProgress.dismiss();
                Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2046 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }


    private void ApiCallSignedDocList(String year) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(vendor.getProccessId()));
        params.put(Constants.PARAM_YEAR, year);

        showWait();
        ApiClient.getClient().create(ApiInterface.class).signedDocDetailResponse("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<SignedDocDetailResponse>() {
            @Override
            public void onResponse(Call<SignedDocDetailResponse> call, Response<SignedDocDetailResponse> response) {

                if (response.isSuccessful()) {
                    removeWait();
                    try {

                        SignedDocDetailResponse signedDocDetailResponse = response.body();
                        if (signedDocDetailResponse.getResponse().equalsIgnoreCase("success")) {
                            docList.clear();

                            if (signedDocDetailResponse.getResult() != null && response.body().getResult().size() > 0) {

                                docList.addAll(signedDocDetailResponse.getResult());
                                documentListAdapter = new DocumentListAdapter(docList, VendorDetailActivity.this);
                                rvDocuments.setAdapter(documentListAdapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VendorDetailActivity.this);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                rvDocuments.setLayoutManager(linearLayoutManager);
                                rvDocuments.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                            } else {
                                rvDocuments.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(VendorDetailActivity.this, "#errorcode:- 2018" + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


                    }
                } else {
                    Toast.makeText(VendorDetailActivity.this, "#errorcode:- 2018" + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SignedDocDetailResponse> call, Throwable t) {
                removeWait();
                Toast.makeText(VendorDetailActivity.this, "#errorcode:- 2018" + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


            }
        });
    }

    public MarkerOptions getMarker(int drawableId) {
        return new MarkerOptions()
                .position(new LatLng(Double.valueOf(vendor.getLatitude()), Double.valueOf(vendor.getLongitude())))
                .icon(BitmapDescriptorFactory.fromResource(drawableId));
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHEQUE_UPDATE_INTENT && resultCode == RESULT_OK) {
        }
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Hello","hello");

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equalsIgnoreCase("1")) {
                    finish();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }*/

    public void commanFragmentCallWithBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendor);

        if (cFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.rvMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

   /* public void commanFragmentCallWithoutBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", vendor);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.commit();

        }
    }*/

    @Override
    public void onResume() {

        super.onResume();
        ApiCallGetDetailofVendor();
       // init();
    }

    private void init() {
        linear_check = findViewById(R.id.linear_cheq);
        tv_ShopName = findViewById(R.id.tv_shop_name);
        tvStoreName = findViewById(R.id.tvStoreName);
        tv_Mobile = findViewById(R.id.tv_mobile);
        tv_Name = findViewById(R.id.tv_name);
        pbBar = findViewById(R.id.mProgress);
        llDisable = findViewById(R.id.llExapandDisable);
        llDelBoy = findViewById(R.id.llDelBoy);
        ivSupport = findViewById(R.id.ivSupport);
        rvDocuments = findViewById(R.id.rvDocuments);
        ivVendorImage = findViewById(R.id.iv_vendor_image);
        btn_update_cheque = findViewById(R.id.btn_update_cheque);
        tvTitle = findViewById(R.id.tv_title_Process);
        tvAddress = findViewById(R.id.tvAddress);
        cvPan = findViewById(R.id.cdUpdatePan);
        cvMobile = findViewById(R.id.cvMobileNo);
        cvCheque = findViewById(R.id.cvCheque);
        cvRate = findViewById(R.id.cvRate);
        cvGST = findViewById(R.id.cvGST);
        cvKYC = findViewById(R.id.cvKYC);
        cvResignAgreement = findViewById(R.id.cvResign);
        cvDelBoy = findViewById(R.id.cvDelBoy);
        cvAddGSTIMAGE = findViewById(R.id.cvGSTImage);
        spYear = findViewById(R.id.spYear);


        tvNoData = findViewById(R.id.tvNodata);


        tvProcessId = findViewById(R.id.tvProcessId);
        llEdit = findViewById(R.id.llExapand);
        cdEdit = findViewById(R.id.cvEdit);
        tvExpand = findViewById(R.id.tvExpand);


        // support System...

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
                ApiCallSendImageSupport(bitmap);
                converted = getResizedBitmap(bitmap, 400);

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


        //  vendor = (VendorList) getIntent().getSerializableExtra("data");


        ivBack = findViewById(R.id.iv_back_process);
        tvAgreementExpirationDate = findViewById(R.id.tvAgreementExpirationDate);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (vendor != null) {
            tvTitle.setText(vendor.getStoreName());
            tv_ShopName.setText(vendor.getStoreName());
            tvStoreName.setText(vendor.getStoreName());
            tv_Name.setText(vendor.getName());
            tv_Mobile.setText(vendor.getMobileNo());
            if (!vendor.getLatitude().equalsIgnoreCase(null)) {
                mDefaultLatitude = Double.valueOf(vendor.getLatitude());
                mDefaultLongitude = Double.valueOf(vendor.getLongitude());
            }
            tvAgreementExpirationDate.setText(vendor.getExpirationDate());
            process_id = String.valueOf(vendor.getProccessId());
            tvProcessId.setText("Id:" + vendor.getProccessId());
            isUpdationRequired = vendor.getIsAgreementUpdationRequire();
            tvAddress.setText(vendor.getStoreAddress() + "," + vendor.getStoreAddress1() + "," + vendor.getStoreAddressLandmark() + "," + vendor.getCity() + "," + vendor.getState());


            if (isUpdationRequired.equalsIgnoreCase("1")) {
                cvResignAgreement.setVisibility(View.VISIBLE);
            } else {
                cvResignAgreement.setVisibility(View.GONE);
            }
            allowDEdit = vendor.getAllowedit();
            allowEditDelBoy = vendor.getIsAddDeliveryBoy();
            allowGSTIMAGE = vendor.getGSTImageRequire();

            if (allowDEdit.equalsIgnoreCase("1")) {
                if (vendor.getIsUpdateGst().equalsIgnoreCase("1")) {
                    cvGST.setVisibility(View.VISIBLE);
                } else {
                    cvGST.setVisibility(View.GONE);
                }
                llEdit.setVisibility(View.VISIBLE);
                // cdEdit.setVisibility(View.VISIBLE);
                llDisable.setVisibility(View.GONE);
                //linear_check.setVisibility(View.VISIBLE);
            } else {
                llDisable.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
            }

            if (allowEditDelBoy.equalsIgnoreCase("1")) {
                cdEdit.setVisibility(View.VISIBLE);
                llDelBoy.setVisibility(View.VISIBLE);
                cvDelBoy.setVisibility(View.VISIBLE);

                //linear_check.setVisibility(View.VISIBLE);
                //  btn_update_delivery_boy.setVisibility(View.VISIBLE);
            } else {
                cvDelBoy.setVisibility(View.GONE);
                llDelBoy.setVisibility(View.GONE);
            }


            if (allowGSTIMAGE.equalsIgnoreCase("1")) {
                cdEdit.setVisibility(View.VISIBLE);
                llDelBoy.setVisibility(View.VISIBLE);
                cvAddGSTIMAGE.setVisibility(View.VISIBLE);

            } else {
                cvAddGSTIMAGE.setVisibility(View.GONE);
                // llDelBoy.setVisibility(View.GONE);
            }

            Glide.with(this).load(Constants.BaseImageURL + vendor.getShopImage())
                    .placeholder(R.drawable.store_place).into(ivVendorImage);


            ArrayList<String> years = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 2017; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
            spYear.setAdapter(adapter);
            spYear.setSelection(years.size() - 1);


            ApiCallSignedDocList(String.valueOf(thisYear));


            spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    ApiCallSignedDocList(spYear.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        cvResignAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(VendorDetailActivity.this);
                }*/
                Intent i = new Intent(VendorDetailActivity.this, ResignAgreementActivity.class);
                i.putExtra("data", String.valueOf(vendor.getProccessId()));
                startActivity(i);
                finish();


            }
        });

        cvCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commanFragmentCallWithBackStack(new ChequedataUpdateFragment(),vendor);
                commanFragmentCallWithBackStack(new ChequeDataListingFragment(), vendor);
            }
        });



        cvKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commanFragmentCallWithBackStack(new ChequedataUpdateFragment(),vendor);
                Intent i = new Intent(VendorDetailActivity.this, EditKYCfromProfileActivity.class);
                i.putExtra(Constants.KEY_PROCESS_ID, String.valueOf(vendor.getProccessId()));
                startActivity(i);
                finish();
            }
        });

        Log.e("datagot",vendor.getPanNo());
        String letter = Character.toString(vendor.getPanNo().charAt(3));
        if (letter.equalsIgnoreCase("F") || letter.equalsIgnoreCase("C")) {
            cvPan.setVisibility(View.GONE);
        } else {
            cvPan.setVisibility(View.GONE);
        }
        cvPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VendorDetailActivity.this, EditPanCardActivity.class);
                i.putExtra("data", String.valueOf(vendor.getProccessId()));
                startActivity(i);

            }
        });

        cvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VendorDetailActivity.this, ChangeMobileActivity.class);
                i.putExtra(Constants.KEY_VENDOR_MOBILE, String.valueOf(vendor.getProccessId()));
                startActivity(i);
            }
        });

        // changes on 28/01
        cvDelBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(VendorDetailActivity.this, com.canvascoders.opaper.activity.AddDeliveryBoysActivity.class);
                myIntent.putExtra("data", String.valueOf(vendor.getProccessId()));
                myIntent.putExtra(Constants.KEY_EDIT_DETAIL, "0");
                startActivity(myIntent);
            }
        });
        cvAddGSTIMAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(VendorDetailActivity.this, com.canvascoders.opaper.activity.AddGstImageActivity.class);
                myIntent.putExtra("data", String.valueOf(vendor.getProccessId()));
                // myIntent.putExtra("store_name", vendor.getStoreName());
                startActivity(myIntent);
            }
        });
        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(VendorDetailActivity.this, StoreTypeListingActivity.class);
                myIntent.putExtra("data", String.valueOf(vendor.getProccessId()));
                startActivity(myIntent);
            }
        });

        cvGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(VendorDetailActivity.this, GstListingActivity.class);
                myIntent.putExtra("data", String.valueOf(vendor.getProccessId()));
                startActivity(myIntent);


                //aiya karvani

                //showAlert(vendor.getGstn());
            }
        });


//
//        drawMarker(new LatLng(Double.parseDouble(vendor.getLatitude()), Double.parseDouble(vendor.getLongitude())));

        //  getMarker()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(mDefaultLatitude, mDefaultLongitude);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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


    public void showWait() {
        runOnUiThread(() -> {
            if (pbBar != null)
                pbBar.setVisibility(View.VISIBLE);
        });
    }


    public void removeWait() {
        runOnUiThread(() -> {
            if (pbBar != null)
                pbBar.setVisibility(View.GONE);
        });
    }

    private void showAlert(String msg) {


        Button btSubmit;
        TextView tvMessage, tvTitle;
        EditText etGSt;
        ImageView ivClose;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(this);
        dialog = new Dialog(this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguepan_gst);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmitgstDetail);
        tvMessage = dialog.findViewById(R.id.tvGstNo);
        // tvTitle = dialog.findViewById(R.id.tvTitle);
        etGSt = dialog.findViewById(R.id.etGstNumber);
        ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // tvTitle.setText("Document Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation(view)) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        APiCallGST(etGSt.getText().toString());
                    } else {

                        Constants.ShowNoInternet(VendorDetailActivity.this);
                    }
                }

            }

            private boolean validation(View v) {
                if (etGSt.getText().toString().equalsIgnoreCase("")) {
                    etGSt.setError("Provide GST");
                    etGSt.requestFocus();
                    return false;
                }
                Matcher gstMatcher = Constants.GST_PATTERN.matcher(etGSt.getText().toString());
                if (!gstMatcher.matches()) {
                    etGSt.setError("Provide valid GST no.");
                    //showMSG(false, "Provide Valid GST No.");
                    etGSt.requestFocus();
                    return false;
                }
                return true;
            }
        });

        dialog.setCancelable(false);

        dialog.show();


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


    private void ApiCallSendImageSupport(Bitmap bitmap) {
        mProgress.setMessage("Please wait...");
        mProgress.show();

        MultipartBody.Part attachment_part = null;


        //Log.e("Id_done", "" + priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitSupportImage("Bearer " + sessionManager.getToken(), attachment_part).enqueue(new Callback<SubmitImageResponse>() {
            @Override
            public void onResponse(Call<SubmitImageResponse> call, Response<SubmitImageResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(VendorDetailActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(VendorDetailActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());

                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);*/
                        Intent i = new Intent(VendorDetailActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT, submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "VendorProfile");
                        startActivity(i);


                        // finish();
                    } else {
                        Toast.makeText(VendorDetailActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(VendorDetailActivity.this, "#errorcode :- 2038 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }


}