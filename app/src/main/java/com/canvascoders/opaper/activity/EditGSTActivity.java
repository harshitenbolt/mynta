package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.CheckGSTStatus.CheckGstStatus;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetGSTVerify.GetGSTVerify;
import com.canvascoders.opaper.Beans.GetGSTVerify.StoreAddress;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.VerifyGstResponse.VerifyGst;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.Beans.getMerakApiResponse.GetMerakResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.ImageUploadTask;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.OnTaskCompleted;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditGSTActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback,
        LocationListener, View.OnClickListener {

    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static LatLng mDefaultLocation = new LatLng(12.972442, 77.580643);
    private CameraPosition mCameraPosition;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    boolean isPanSelected = false;
    private Spinner dc;
    public static final int CROPPED_IMAGE = 5333, CROPPED_IMAGE_2 = 5335;
    private static final int IMAGE_SHOP_IMG = 105, IMAGE_OWNER_IMG = 106;
    private static final int IMAGE_PAN = 101;
    private static final int IMAGE_CHEQUE = 102;
    private static int IMAGE_SELCTED_IMG = 0;
    boolean enabled = false;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    SupportMapFragment mapFragment = null;
    private static final String TAG = EditGSTActivity.class.getSimpleName();
    private ImageView ivSearch, ivEdit, ivPanImageSelected;
    private EditText etGST, etStoreNAme, etStoreAddress;
    VendorList vendor;
    Button btNext1, btSubmitDCStoreImage, btExtractPan, btSendLink;
    ImageView ivPanImage, ivShopImageSingle, ivAddressProofSelected, ivStoreImage, ivGstImage, ivOwnerImageSelected, ivStoreImageSelected, ivShopImage;
    private TextView tvMessage;
    LocationRequest mLocationRequest;
    View view, view2, view3, view4;
    private String shopImg = "";
    private Uri imgURI;
    private String gstCertificate = "", panImagepath = "", cameraimage = "";
    String str_process_id;
    private ArrayList<String> dcLists = new ArrayList<>();
    RelativeLayout rvFirstScreen, rvSecondScreen, rvPanDetails, rvChequeDetails;
    private RequestPermissionHandler requestPermissionHandler;
    ImageView ivPanOldImage;
    String panName, panFatherName, panNumber;
    LinearLayout llWaitingApproval;
    boolean isPanScreen = false;
    LinearLayout llLinearView;
    String approvalGSTId = "";
    private TextView btn_cheque_card;
    private ImageView ivChequeImage;
    private ImageView btn_cheque_card_select;
    private Button btExtract;
    private String cancelChequeImagepath = "", imagecamera = "";
    String bank_name = "";
    String bank_branch = "";
    String branch_address = "";

    int request_id = 0;
    ImageView ivBack;

    private TextView tvPanClick, tvPanName, tvPanFatherName, tvPanNo;
    private TextView tvSkipCheque;
    String store_address, store_address1, store_address_landmark, store_city, store_state, store_pincode, store_full_address;
    String bankName, bank_branch_name, Ifsc, bank_ac, payee_name, bank_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gst);

        // vendor = (VendorList) getIntent().getSerializableExtra("data");
        str_process_id = getIntent().getStringExtra("data");
        Log.e("process_id", str_process_id);
        progressDialog = new ProgressDialog(this);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("getting Location...");
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(this);
        // etSearch =view.findViewById(R.id.etSearchPlace);


        // str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocationRequest();
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermissionHandler = new RequestPermissionHandler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        ivSearch = findViewById(R.id.ivSearch);
        ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(this);
        ivPanImage = findViewById(R.id.ivPanImage);
        tvPanClick = findViewById(R.id.ivPanCard);
        tvPanClick.setOnClickListener(this);

        etGST = findViewById(R.id.etGstNumber);
        etStoreNAme = findViewById(R.id.etStoreName);
        etStoreAddress = findViewById(R.id.etAddress);
        ivSearch.setOnClickListener(this);

        btNext1 = findViewById(R.id.btNext1);
        btSubmitDCStoreImage = findViewById(R.id.btSubmitDCStoreImage);
        btSubmitDCStoreImage.setOnClickListener(this);

        dc = findViewById(R.id.dc);
        btNext1.setOnClickListener(this);
        rvFirstScreen = findViewById(R.id.rvFirstScreen);
        rvSecondScreen = findViewById(R.id.rvSecondScreen);

        ivStoreImage = findViewById(R.id.ivStoreImage);
        ivStoreImage.setOnClickListener(this);

        ivGstImage = findViewById(R.id.ivOwnerImage);
        ivGstImage.setOnClickListener(this);


        ivOwnerImageSelected = findViewById(R.id.ivOwnerImageSelected);
        ivStoreImageSelected = findViewById(R.id.ivStoreImageSelected);
        ivOwnerImageSelected.setOnClickListener(this);
        ivStoreImageSelected.setOnClickListener(this);

        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        tvPanName = findViewById(R.id.tvPanName);
        tvPanFatherName = findViewById(R.id.tvPanFatherName);
        tvPanNo = findViewById(R.id.tvPanNo);

        rvPanDetails = findViewById(R.id.rvPanDetails);
        rvChequeDetails = findViewById(R.id.rvChequeDetails);

        ivPanImageSelected = findViewById(R.id.tvClickPanSelected);
        ivPanOldImage = findViewById(R.id.ivPanOldImage);
        btExtractPan = findViewById(R.id.btExtractPan);
        btExtractPan.setOnClickListener(this);
        btSendLink = findViewById(R.id.btSendLink);
        btSendLink.setOnClickListener(this);

        tvSkipCheque = findViewById(R.id.tvSkip);
        tvSkipCheque.setOnClickListener(this);
        llWaitingApproval = findViewById(R.id.llWaitingApproval);
        tvMessage = findViewById(R.id.tvMessage);
        llLinearView = findViewById(R.id.viewPage);

        //chequeImageFlow
        btExtract = findViewById(R.id.btExtract);
        btn_cheque_card = (TextView) findViewById(R.id.tvClickCheque);
        btn_cheque_card_select = (ImageView) findViewById(R.id.tvClickChequeSelected);
        btn_cheque_card_select.setOnClickListener(this);
        ivChequeImage = findViewById(R.id.ivChequeImage);
        btExtract.setOnClickListener(this);
        btn_cheque_card.setOnClickListener(this);

        etGST.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            // getBankDetails(mContext,s.toString(),processId);
            APiCallCheckGSTStatus();
        } else {
            Constants.ShowNoInternet(EditGSTActivity.this);
        }


    }

    private void APiCallCheckGSTStatus() {
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));

        Call<CheckGstStatus> call = ApiClient.getClient().create(ApiInterface.class).checkGSTStatus("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<CheckGstStatus>() {
            @Override
            public void onResponse(Call<CheckGstStatus> call, Response<CheckGstStatus> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    CheckGstStatus checkGstStatus = response.body();
                    if (checkGstStatus.getResponseCode() == 200) {
                        rvFirstScreen.setVisibility(View.VISIBLE);
                    } else if (checkGstStatus.getResponseCode() == 202) {
                        rvChequeDetails.setVisibility(View.GONE);
                        rvFirstScreen.setVisibility(View.GONE);
                        rvSecondScreen.setVisibility(View.GONE);
                        rvPanDetails.setVisibility(View.GONE);
                        llWaitingApproval.setVisibility(View.VISIBLE);
                        llLinearView.setVisibility(View.GONE);
                        tvMessage.setText(checkGstStatus.getResponse());
                        approvalGSTId = String.valueOf(checkGstStatus.getApprovalGSTId());
                        if (checkGstStatus.getSendLink() == true) {
                            btSendLink.setVisibility(View.VISIBLE);
                        } else {
                            btSendLink.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(EditGSTActivity.this, checkGstStatus.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2081 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CheckGstStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2081 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mMap != null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        //  mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        //Log.i(LOG_TAG, "Google Places API connected.");
    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                //  Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        if (enabled)
            getDeviceLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }


    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastKnownLocation != null)
                mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

            MarkerOptions marker = new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                    mLastKnownLocation.getLongitude())).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.ping));

            mMap.clear();
            mMap.addMarker(marker);
        } else {
            Mylogger.getInstance().Logit(TAG, "Current location is null. Using defaults.");


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            MarkerOptions marker = new MarkerOptions().position(new LatLng(mDefaultLocation.latitude,
                    mDefaultLocation.longitude)).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.ping));
            mMap.clear();
            mMap.addMarker(marker);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Mylogger.getInstance().Logit(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Mylogger.getInstance().Logit(TAG, "Play services connection suspended");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(2); // 10 meters
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
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Toast.makeText(this, "location changed", Toast.LENGTH_LONG).show();
            Mylogger.getInstance().Logit(TAG, "Current location is null. Using defaults.");
            mLastKnownLocation = location;
            mDefaultLocation = new LatLng(location.getLatitude(), location.getLongitude());
            // Constants.markerLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                if (validation(v)) {
                    APiCallVerifyGST();
                }
                break;
            case R.id.ivEdit:
                showAlert();

                break;
            case R.id.btNext1:
                if (TextUtils.isEmpty(etStoreNAme.getText().toString())) {
                    etGST.setError("Please Provide GST");
                    etGST.requestFocus();
                } else {
                    rvFirstScreen.setVisibility(View.GONE);
                    rvSecondScreen.setVisibility(View.VISIBLE);
                    view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                break;

            case R.id.btSubmitDCStoreImage:
                if (validation()) {
                    rvSecondScreen.setVisibility(View.GONE);
                    if (isPanScreen == true) {
                        rvPanDetails.setVisibility(View.VISIBLE);
                        view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                            // getBankDetails(mContext,s.toString(),processId);
                            ApiCallGetPanDetails();
                        } else {
                            Constants.ShowNoInternet(EditGSTActivity.this);
                        }
                    } else {
                        rvChequeDetails.setVisibility(View.VISIBLE);
                        view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    }

                }

                break;

            case R.id.btExtractPan:
                if (validationPan()) {
                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                        ExtractPanDetail();
                    } else {
                        Constants.ShowNoInternet(EditGSTActivity.this);
                    }
                }

                break;

          /*  case R.id.btSubmitDCStoreImage:
                if (validation()) {
                    rvSecondScreen.setVisibility(View.GONE);
                    rvPanDetails.setVisibility(View.VISIBLE);
                    view3.setBackgroundColor(R.color.colorPrimary);
                }

                break;*/


            case R.id.ivStoreImage:
                capture_document_front_and_back_image(3);
                break;
            case R.id.ivStoreImageSelected:
                capture_document_front_and_back_image(3);
                break;
            case R.id.ivOwnerImage:
                capture_document_front_and_back_image(4);
                break;
            case R.id.ivOwnerImageSelected:
                capture_document_front_and_back_image(4);
                break;
            case R.id.ivPanCard:
                capture_document_front_and_back_image(5);
                break;

            case R.id.tvClickCheque:
                capture_document_front_and_back_image(6);
                break;
            case R.id.tvClickChequeSelected:
                capture_document_front_and_back_image(6);
                break;

            case R.id.tvSkip:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (isPanScreen == true) {
                        ApiCallWithoutChequewithPan();
                    } else {
                        ApiCallWithoutChequewithoutPan();
                    }


                } else {
                    Constants.ShowNoInternet(EditGSTActivity.this);
                }


                break;
            case R.id.btSendLink:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSendLink();


                } else {
                    Constants.ShowNoInternet(EditGSTActivity.this);
                }


                break;


            case R.id.btExtract:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {

                    if (validationCheque())
                        CallMerekApi();

                    /*storeCheque()*/
                    ;/*{
                    DialogueUtils.failedPayment(getActivity(), new DialogListner() {
                        @Override
                        public void onClickPositive() {
                            DialogueUtils.dismiss();
                        }

                        @Override
                        public void onClickNegative() {
                            DialogueUtils.dismiss();
                        }
                    });

                }*///
               /* else{
                    DialogueUtils.successPayment(getActivity(), new DialogListner() {
                        @Override
                        public void onClickPositive() {
                            DialogueUtils.dismiss();
                        }

                        @Override
                        public void onClickNegative() {
                            DialogueUtils.dismiss();
                        }
                    });
                }*/


                } else {
                    Constants.ShowNoInternet(EditGSTActivity.this);
                }
                break;


        }

    }


    //cheque data

    public void CallMerekApi() {

        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put("image", cancelChequeImagepath);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading . . .");
        pd.show();
        Log.e("Merak", "API Called");
        new ImageUploadTask(Constants.OCRMEREK, null, fileMap, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                //Log.e("MERAK RESULT", tryCount + "::" + result);
                pd.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("success") == 1) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        if (dataObj != null) {
                            String ifsccode = "";
                            ifsccode = dataObj.optString("ifsc_code");
                            getBankDetails(ifsccode);
                            String accountNumber = dataObj.optString("account_number");
                            bank_name = dataObj.optString("bank_name");
                            bank_branch = dataObj.optString("bank_branch");
                            branch_address = dataObj.optString("branch_address");
                            String payeeName = "";
                            try {
                                request_id = jsonObject.optInt("request_id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            JSONArray payeeNames = dataObj.optJSONArray("payee_name");
                            if (payeeNames != null && payeeNames.length() > 0) {
                                payeeName = payeeNames.getString(0);
                                // edit_ac_name.setText(payeeName);
                            }


                            ApiCallGetMerakCount();

                            DialogUtil.chequeDetail(EditGSTActivity.this, accountNumber, payeeName, ifsccode, str_process_id, bank_name, bank_branch, branch_address, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                }

                                @Override
                                public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {
                                    //aiya karvanu
                                    //    storeCheque(accName, payeename, ifsc, bankname, BranchName, bankAdress);
                                    bankName = bankname;
                                    bank_branch_name = BranchName;
                                    Ifsc = ifsc;
                                    bank_ac = accName;
                                    payee_name = payeename;
                                    bank_address = bankAdress;


                                    if (isPanScreen == true) {
                                        ApiCallWithChequewithPan();

                                    } else {
                                        ApiCallWithChequewithoutPan();
                                    }


                                }

                                @Override
                                public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                }
                            });
                           /* edit_ac_no.setText(accountNumber);
                            edit_ifsc.setText(ifscCode);
                            edit_bank_name.setText(bank_name);
                            edit_bank_branch_name.setText(bank_branch);
                            edit_bank_address.setText(branch_address);*/


                        }
                    } else {
                        DialogUtil.chequeDetail(EditGSTActivity.this, "", "", "", str_process_id, "", "", "", new DialogListner() {
                            @Override
                            public void onClickPositive() {

                            }

                            @Override
                            public void onClickNegative() {

                            }

                            @Override
                            public void onClickDetails(String name, String fathername, String dob, String id) {

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                bankName = bankname;
                                bank_branch_name = BranchName;
                                Ifsc = ifsc;
                                bank_ac = accName;
                                payee_name = payeename;
                                bank_address = bankAdress;

                                if (isPanScreen == true) {
                                    ApiCallWithChequewithPan();
                                } else {
                                    ApiCallWithChequewithoutPan();
                                }

                                //    storeCheque(accName, payeename, ifsc, bankname, BranchName, bankAdress);
                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }
                        });
                        Toast.makeText(EditGSTActivity.this, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    cancelChequeImagepath = "";
                    Glide.with(EditGSTActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.checkimage)
                            .into(ivChequeImage);
                    isPanSelected = false;
                    btn_cheque_card.setVisibility(View.VISIBLE);
                    btn_cheque_card_select.setVisibility(View.GONE);
                    Toast.makeText(EditGSTActivity.this, "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();

                }

            }
        }).execute();

        Glide.with(EditGSTActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                .into(ivChequeImage);
        isPanSelected = true;

    }

    private boolean validationCheque() {
        if (!isPanSelected) {
            Toast.makeText(this, "Please select Cheque Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void ApiCallGetMerakCount() {
        Map<String, String> params = new HashMap<String, String>();

        // params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.DATA, "");
        Call<GetMerakResponse> callUpload = ApiClient.getClient2().create(ApiInterface.class).getMerakList(params);
        callUpload.enqueue(new Callback<GetMerakResponse>() {
            @Override
            public void onResponse(Call<GetMerakResponse> call, Response<GetMerakResponse> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<GetMerakResponse> call, Throwable t) {

            }
        });


    }


    private void ApiCallSendLink() {
        progressDialog.setMessage("Sending link...");
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_APPROVAL_GST_ID, approvalGSTId);
        Call<CheckGstStatus> call = ApiClient.getClient().create(ApiInterface.class).sendGStLink("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<CheckGstStatus>() {
            @Override
            public void onResponse(Call<CheckGstStatus> call, Response<CheckGstStatus> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    CheckGstStatus verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2082 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckGstStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2082 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void ApiCallWithoutChequewithPan() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        MultipartBody.Part attachment_pan = null;
        MultipartBody.Part attachment_gst = null;
        MultipartBody.Part attachment_store = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());
        params.put(Constants.PARAM_STORE_NAME, etStoreNAme.getText().toString());
        params.put(Constants.PARAM_STORE_ADDRESS, store_address);
        params.put(Constants.PARAM_STORE_ADDRESS1, store_address1);
        params.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, store_address_landmark);
        params.put(Constants.PARAM_CITY, store_city);
        params.put(Constants.PARAM_STATE, store_state);
        params.put(Constants.PARAM_PINCODE, store_pincode);
        params.put(Constants.PARAM_STORE_FULL_ADDRESS, store_full_address);
        params.put(Constants.PARAM_DC, String.valueOf(dc.getSelectedItem()));
        params.put(Constants.PARAM_PAN_NAME, panName);
        params.put(Constants.PARAM_FATHER_NAME, panFatherName);
        params.put(Constants.PARAM_PAN_NO, panNumber);
        params.put(Constants.PARAM_GST_PAN_NO, panNumber);
        params.put(Constants.PARAM_LATITUDE, String.valueOf(mDefaultLocation.latitude));
        params.put(Constants.PARAM_LONGITUDE, String.valueOf(mDefaultLocation.longitude));
        params.put(Constants.PARAM_IS_PAN_EXIST, "yes");
        params.put(Constants.PARAM_IS_BANK_EXIST, "no");

        File imagefile1 = new File(panImagepath);
        attachment_pan = MultipartBody.Part.createFormData(Constants.PARAM_PAN_CARD_FRONT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile1));

        File imagefile = new File(gstCertificate);
        attachment_gst = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstCertificate)), imagefile));

        File imageStore = new File(shopImg);
        attachment_store = MultipartBody.Part.createFormData(Constants.PARAM_STORE_IMAGE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imageStore));


        List<MultipartBody.Part> imagepart = new ArrayList<>();
        imagepart.add(attachment_pan);
        imagepart.add(attachment_gst);
        imagepart.add(attachment_store);


        Call<VerifyGst> call = ApiClient.getClient().create(ApiInterface.class).updateGst("Bearer " + sessionManager.getToken(), params, imagepart);
        call.enqueue(new Callback<VerifyGst>() {
            @Override
            public void onResponse(Call<VerifyGst> call, Response<VerifyGst> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VerifyGst verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {
                        //Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                        rvChequeDetails.setVisibility(View.GONE);
                        tvMessage.setText(verifyGst.getResponse());
                        // llWaitingApproval.setVisibility(View.VISIBLE);
                        APiCallCheckGSTStatus();
                    } else {
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<VerifyGst> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void ApiCallWithoutChequewithoutPan() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        MultipartBody.Part attachment_gst = null;
        MultipartBody.Part attachment_store = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());
        params.put(Constants.PARAM_STORE_NAME, etStoreNAme.getText().toString());
        params.put(Constants.PARAM_STORE_ADDRESS, store_address);
        params.put(Constants.PARAM_STORE_ADDRESS1, store_address1);
        params.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, store_address_landmark);
        params.put(Constants.PARAM_CITY, store_city);
        params.put(Constants.PARAM_STATE, store_state);
        params.put(Constants.PARAM_PINCODE, store_pincode);
        params.put(Constants.PARAM_STORE_FULL_ADDRESS, store_full_address);
        params.put(Constants.PARAM_DC, String.valueOf(dc.getSelectedItem()));
        params.put(Constants.PARAM_LATITUDE, String.valueOf(mDefaultLocation.latitude));
        params.put(Constants.PARAM_LONGITUDE, String.valueOf(mDefaultLocation.longitude));
        params.put(Constants.PARAM_IS_PAN_EXIST, "no");
        params.put(Constants.PARAM_IS_BANK_EXIST, "no");


        File imagefile = new File(gstCertificate);
        attachment_gst = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstCertificate)), imagefile));

        File imageStore = new File(shopImg);
        attachment_store = MultipartBody.Part.createFormData(Constants.PARAM_STORE_IMAGE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imageStore));


        List<MultipartBody.Part> imagepart = new ArrayList<>();
        imagepart.add(attachment_gst);
        imagepart.add(attachment_store);


        Call<VerifyGst> call = ApiClient.getClient().create(ApiInterface.class).updateGst("Bearer " + sessionManager.getToken(), params, imagepart);
        call.enqueue(new Callback<VerifyGst>() {
            @Override
            public void onResponse(Call<VerifyGst> call, Response<VerifyGst> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VerifyGst verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {
                        //Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                        rvChequeDetails.setVisibility(View.GONE);
                        tvMessage.setText(verifyGst.getResponse());
                        // llWaitingApproval.setVisibility(View.VISIBLE);
                        APiCallCheckGSTStatus();
                    } else {
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<VerifyGst> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                //   Toast.makeText(EditGSTActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });
    }


    private void ApiCallWithChequewithoutPan() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        MultipartBody.Part attachment_gst = null;
        MultipartBody.Part attachment_store = null;
        MultipartBody.Part attachment_cheque = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());
        params.put(Constants.PARAM_STORE_NAME, etStoreNAme.getText().toString());
        params.put(Constants.PARAM_STORE_ADDRESS, store_address);
        params.put(Constants.PARAM_STORE_ADDRESS1, store_address1);
        params.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, store_address_landmark);
        params.put(Constants.PARAM_CITY, store_city);
        params.put(Constants.PARAM_STATE, store_state);
        params.put(Constants.PARAM_PINCODE, store_pincode);
        params.put(Constants.PARAM_STORE_FULL_ADDRESS, store_full_address);
        params.put(Constants.PARAM_DC, String.valueOf(dc.getSelectedItem()));
        params.put(Constants.PARAM_LATITUDE, String.valueOf(mDefaultLocation.latitude));
        params.put(Constants.PARAM_LONGITUDE, String.valueOf(mDefaultLocation.longitude));
        params.put(Constants.PARAM_IS_PAN_EXIST, "no");
        params.put(Constants.PARAM_IS_BANK_EXIST, "yes");

        params.put(Constants.PARAM_PARAM_BANK_NAME, bankName);
        params.put(Constants.PARAM_BANK_BRANCH_NAME, bank_branch_name);
        params.put(Constants.PARAM_IFSC, Ifsc);
        params.put(Constants.PARAM_BANK_AC, bank_ac);
        params.put(Constants.PARAM_PAYEE_NAME, payee_name);
        params.put(Constants.PARAM_BANK_ADDRESS, bank_address);


        File imagefile = new File(gstCertificate);
        attachment_gst = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstCertificate)), imagefile));

        File imageStore = new File(shopImg);
        attachment_store = MultipartBody.Part.createFormData(Constants.PARAM_STORE_IMAGE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imageStore));


        File imagecheque = new File(cancelChequeImagepath);
        attachment_cheque = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagecheque));


        List<MultipartBody.Part> imagepart = new ArrayList<>();
        imagepart.add(attachment_gst);
        imagepart.add(attachment_store);
        imagepart.add(attachment_cheque);

        Call<VerifyGst> call = ApiClient.getClient().create(ApiInterface.class).updateGst("Bearer " + sessionManager.getToken(), params, imagepart);
        call.enqueue(new Callback<VerifyGst>() {
            @Override
            public void onResponse(Call<VerifyGst> call, Response<VerifyGst> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VerifyGst verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {
                        //Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                        rvChequeDetails.setVisibility(View.GONE);
                        tvMessage.setText(verifyGst.getResponse());
                        DialogUtil.dismiss();
                        // llWaitingApproval.setVisibility(View.VISIBLE);
                        APiCallCheckGSTStatus();
                    } else {
                        DialogUtil.dismiss();
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<VerifyGst> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();


            }
        });
    }


    private void ApiCallWithChequewithPan() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        MultipartBody.Part attachment_pan = null;
        MultipartBody.Part attachment_gst = null;
        MultipartBody.Part attachment_store = null;
        MultipartBody.Part attachment_cheque = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());
        params.put(Constants.PARAM_STORE_NAME, etStoreNAme.getText().toString());
        params.put(Constants.PARAM_STORE_ADDRESS, store_address);
        params.put(Constants.PARAM_STORE_ADDRESS1, store_address1);
        params.put(Constants.PARAM_STORE_ADDRESS_LANDMARK, store_address_landmark);
        params.put(Constants.PARAM_CITY, store_city);
        params.put(Constants.PARAM_STATE, store_state);
        params.put(Constants.PARAM_PINCODE, store_pincode);
        params.put(Constants.PARAM_STORE_FULL_ADDRESS, store_full_address);
        params.put(Constants.PARAM_DC, String.valueOf(dc.getSelectedItem()));
        params.put(Constants.PARAM_PAN_NAME, panName);
        params.put(Constants.PARAM_FATHER_NAME, panFatherName);
        params.put(Constants.PARAM_PAN_NO, panNumber);
        params.put(Constants.PARAM_GST_PAN_NO, panNumber);
        params.put(Constants.PARAM_LATITUDE, String.valueOf(mDefaultLocation.latitude));
        params.put(Constants.PARAM_LONGITUDE, String.valueOf(mDefaultLocation.longitude));
        params.put(Constants.PARAM_IS_PAN_EXIST, "yes");
        params.put(Constants.PARAM_IS_BANK_EXIST, "yes");

        params.put(Constants.PARAM_PARAM_BANK_NAME, bankName);
        params.put(Constants.PARAM_BANK_BRANCH_NAME, bank_branch_name);
        params.put(Constants.PARAM_IFSC, Ifsc);
        params.put(Constants.PARAM_BANK_AC, bank_ac);
        params.put(Constants.PARAM_PAYEE_NAME, payee_name);
        params.put(Constants.PARAM_BANK_ADDRESS, bank_address);


        File imagefile1 = new File(panImagepath);
        attachment_pan = MultipartBody.Part.createFormData(Constants.PARAM_PAN_CARD_FRONT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile1));

        File imagefile = new File(gstCertificate);
        attachment_gst = MultipartBody.Part.createFormData(Constants.PARAM_GST_CERTIFICATE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(gstCertificate)), imagefile));

        File imageStore = new File(shopImg);
        attachment_store = MultipartBody.Part.createFormData(Constants.PARAM_STORE_IMAGE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imageStore));

        File imagecheque = new File(cancelChequeImagepath);
        attachment_cheque = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, imageStore.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagecheque));


        List<MultipartBody.Part> imagepart = new ArrayList<>();
        imagepart.add(attachment_pan);
        imagepart.add(attachment_gst);
        imagepart.add(attachment_store);
        imagepart.add(attachment_cheque);


        Call<VerifyGst> call = ApiClient.getClient().create(ApiInterface.class).updateGst("Bearer " + sessionManager.getToken(), params, imagepart);
        call.enqueue(new Callback<VerifyGst>() {
            @Override
            public void onResponse(Call<VerifyGst> call, Response<VerifyGst> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VerifyGst verifyGst = response.body();
                    if (verifyGst.getResponseCode() == 200) {
                        //Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();
                        rvChequeDetails.setVisibility(View.GONE);
                        tvMessage.setText(verifyGst.getResponse());
                        DialogUtil.dismiss();
                        // llWaitingApproval.setVisibility(View.VISIBLE);
                        APiCallCheckGSTStatus();
                    } else {
                        DialogUtil.dismiss();
                        Toast.makeText(EditGSTActivity.this, verifyGst.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyGst> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2080 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean validation() {

       /* if (shopImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload Store Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }*/
        if (gstCertificate.equals("")) {
            Toast.makeText(this, "Please Upload GST Certificate", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (shopImg.equals("")) {
            Toast.makeText(this, "Please Upload Store Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }


        return true;
    }

    private boolean validationPan() {

        if (!isPanSelected) {
            Toast.makeText(EditGSTActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (panImagepath.equalsIgnoreCase("")) {
            Toast.makeText(EditGSTActivity.this, "Please select PAN Image", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private void capture_document_front_and_back_image(int side_of_document) {
        requestPermissionHandler.requestPermission(EditGSTActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {


                if (side_of_document == 3) {

                    IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditGSTActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);
                }


                if (side_of_document == 4) {

                    IMAGE_SELCTED_IMG = IMAGE_OWNER_IMG;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditGSTActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_OWNER_IMG);
                }

                if (side_of_document == 5) {
                    IMAGE_SELCTED_IMG = IMAGE_PAN;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditGSTActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_PAN);
                }
                if (side_of_document == 6) {
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditGSTActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_CHEQUE);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(EditGSTActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void APiCallVerifyGST() {
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(str_process_id));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_GSTN, etGST.getText().toString());

        Call<GetGSTVerify> call = ApiClient.getClient().create(ApiInterface.class).getGSTVerify("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<GetGSTVerify>() {
            @Override
            public void onResponse(Call<GetGSTVerify> call, Response<GetGSTVerify> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    GetGSTVerify getGSTVerify = response.body();
                    if (getGSTVerify.getResponseCode() == 200) {
                        Toast.makeText(EditGSTActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
                     /*   ivSearch.setBackgroundResource(0);
                        ivSearch.setImageResource(R.drawable.checked);*/
                        etStoreNAme.setText(getGSTVerify.getData().get(0).getStoreName());
                        StoreAddress storeAddress = getGSTVerify.getData().get(0).getStoreAddress();
                        etStoreAddress.setText(storeAddress.getBnm() + " " + storeAddress.getBno() + " " + storeAddress.getSt() + " " + storeAddress.getLoc() + " " + storeAddress.getCity() + " " + storeAddress.getDst() + " " + storeAddress.getStcd() + " " + storeAddress.getPncd());

                        store_address = storeAddress.getBnm() + " " + storeAddress.getBno();
                        store_address1 = storeAddress.getSt();
                        store_address_landmark = storeAddress.getLoc();
                        if (storeAddress.getCity().equalsIgnoreCase("")) {
                            store_city = storeAddress.getDst();
                        } else {
                            store_city = storeAddress.getCity();
                        }
                        store_state = storeAddress.getStcd();
                        store_pincode = storeAddress.getPncd();
                        store_full_address = etStoreAddress.getText().toString();
                        ivEdit.setVisibility(View.VISIBLE);
                        ivSearch.setVisibility(View.GONE);
                        etGST.setEnabled(false);
                        addDC(storeAddress.getPncd());

                        if (getGSTVerify.getData().get(0).getGoToPanScreen()) {
                            isPanScreen = true;
                        } else {
                            isPanScreen = false;
                            view3.setVisibility(View.GONE);
                        }


                        DialogUtil.addressDetails(EditGSTActivity.this, store_address, store_address1, store_address_landmark, store_pincode, store_city, store_state, new DialogListner() {
                            @Override
                            public void onClickPositive() {

                            }

                            @Override
                            public void onClickNegative() {

                            }

                            @Override
                            public void onClickDetails(String name, String fathername, String dob, String id) {

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {
                                // etCity.getText().toString(), etState.getText().toString(),""+dc.getSelectedItem());
                                store_address = accName;
                                store_address1 = payeename;
                                store_address_landmark = ifsc;
                                store_pincode = bankname;
                                store_city = BranchName;
                                store_state = bankAdress;
                                addDC(store_pincode);
                                store_full_address = store_address + " " + store_address1 + " " + store_address_landmark + " " + store_pincode + " " + store_city + " " + store_state;
                                etStoreAddress.setText(store_full_address);

                            }
                        });


                    } else if (getGSTVerify.getResponseCode() == 400) {
                        if (getGSTVerify.getValidation() != null) {
                            Validation validation = getGSTVerify.getValidation();
                            if (validation.getGstn() != null && validation.getGstn().length() > 0) {
                                etGST.setError(validation.getGstn());
                                etGST.requestFocus();
                                // return false;
                            } else {
                                Toast.makeText(EditGSTActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(EditGSTActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    } else if (getGSTVerify.getResponseCode() == 202) {
                        sessionManager.logoutUser(EditGSTActivity.this);
                    } else {
                        Toast.makeText(EditGSTActivity.this, getGSTVerify.getResponse(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetGSTVerify> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode 2079 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean validation(View v) {
        if (etGST.getText().toString().equalsIgnoreCase("")) {
            etGST.setError("Provide GST");
            etGST.requestFocus();
            return false;
        }
        Matcher gstMatcher = Constants.GST_PATTERN.matcher(etGST.getText().toString());
        if (!gstMatcher.matches()) {
            etGST.setError("Provide valid GST no.");
            //showMSG(false, "Provide Valid GST No.");
            etGST.requestFocus();
            return false;
        }
        return true;
    }

    private void showAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditGSTActivity.this);
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("Are you sure you want to Edit GST Number?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                etGST.setEnabled(true);
                etGST.getText().clear();
                etStoreAddress.getText().clear();
                etStoreNAme.getText().clear();
                ivSearch.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.GONE);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }


    private void addDC(String pcode) {
        // state is DC and DC is state

        dcLists.clear();
        progressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }

                        }

                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(EditGSTActivity.this, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(EditGSTActivity.this);
                    } else {
                        Toast.makeText(EditGSTActivity.this, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //      Toast.makeText(EditGSTActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (IMAGE_SELCTED_IMG == IMAGE_SHOP_IMG) {

                Bitmap bitmap = ImagePicker.getImageFromResult(EditGSTActivity.this, resultCode, data);
                shopImg = ImagePicker.getBitmapPath(bitmap, EditGSTActivity.this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(EditGSTActivity.this).load(shopImg).into(ivStoreImage);
                ivStoreImageSelected.setVisibility(View.VISIBLE);

            }


            if (IMAGE_SELCTED_IMG == IMAGE_OWNER_IMG) {
                Bitmap bitmap = ImagePicker.getImageFromResult(EditGSTActivity.this, resultCode, data);
                gstCertificate = ImagePicker.getBitmapPath(bitmap, EditGSTActivity.this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(EditGSTActivity.this).load(gstCertificate).into(ivGstImage);
                ivOwnerImageSelected.setVisibility(View.VISIBLE);
            }

            if (requestCode == IMAGE_PAN) {

//                Constants.hideKeyboardwithoutPopulate(EditPanCardActivity.this);
                Bitmap bitmap = ImagePicker.getImageFromResult(EditGSTActivity.this, resultCode, data);
                cameraimage = ImagePicker.getBitmapPath(bitmap, EditGSTActivity.this);
               /* Glide.with(getActivity()).load(panImagepath).into(btn_pan_card);
                isPanSelected = true;
                btn_pan_card_select.setVisibility(View.VISIBLE);
                Log.e("Pan image path", panImagepath);*/
                Intent intent = new Intent(EditGSTActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(cameraimage)).toString());
                startActivityForResult(intent, CROPPED_IMAGE);


                //new ResizeAsync().execute();
            }
            if (requestCode == CROPPED_IMAGE) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                panImagepath = RealPathUtil.getPath(EditGSTActivity.this, imgURI);

                try {
                    Glide.with(EditGSTActivity.this).load(imgURI).into(ivPanImage);
                    isPanSelected = true;
                    ivPanImageSelected.setVisibility(View.VISIBLE);

                    tvPanClick.setVisibility(View.GONE);
                    // ExtractPanDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            if (requestCode == IMAGE_CHEQUE) {

//                Constants.hideKeyboardwithoutPopulate(getActivity());
                Bitmap bitmap = ImagePicker.getImageFromResult(EditGSTActivity.this, resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, EditGSTActivity.this);

                Intent intent = new Intent(EditGSTActivity.this, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                startActivityForResult(intent, CROPPED_IMAGE_2);


            }
            if (requestCode == CROPPED_IMAGE_2) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(EditGSTActivity.this, imgURI);
                Glide.with(EditGSTActivity.this).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                        .into(ivChequeImage);
                isPanSelected = true;
                btn_cheque_card.setVisibility(View.GONE);
                btn_cheque_card_select.setVisibility(View.VISIBLE);

            }
        }
    }


    private void ApiCallGetPanDetails() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_PROCESS_ID, str_process_id);
        Call<UpdatePanDetailResponse> call = ApiClient.getClient().create(ApiInterface.class).pandetailResponse("Bearer " + sessionManager.getToken(), param);
        call.enqueue(new Callback<UpdatePanDetailResponse>() {
            @Override
            public void onResponse(Call<UpdatePanDetailResponse> call, retrofit2.Response<UpdatePanDetailResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    UpdatePanDetailResponse updatePanDetailResponse = response.body();
                    if (updatePanDetailResponse.getResponseCode() == 200) {
                        tvPanName.setText(updatePanDetailResponse.getData().get(0).getPanName());

                        tvPanFatherName.setText(updatePanDetailResponse.getData().get(0).getFatherName());
                        tvPanNo.setText(updatePanDetailResponse.getData().get(0).getPanNo());


                        Glide.with(EditGSTActivity.this).load(Constants.BaseImageURL + updatePanDetailResponse.getData().get(0).getPan()).into(ivPanOldImage);

                    } else {
                        Toast.makeText(EditGSTActivity.this, updatePanDetailResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePanDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditGSTActivity.this, "#errorcode :- 2047 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void ExtractPanDetail() {

        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        if (!TextUtils.isEmpty(panImagepath)) {

            progressDialog.setMessage("Extracting image..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            File imagefile = new File(panImagepath);
            typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(panImagepath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));

           /* Call<PanImageResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getPancardOcrUrl("Bearer "+sessionManager.getToken(),sessionManager.getToken(), str_process_id, typedFile);

            callUpload.enqueue(new Callback<PanImageResponse>() {
                @Override
                public void onResponse(Call<PanImageResponse> call, retrofit2.Response<PanImageResponse> response) {
                    mProgressDialog.dismiss();

                    if (response.isSuccessful()) {
                        PanImageResponse panImageResponse = response.body();
                        if (panImageResponse.getResponseCode() == 200) {
                            String imagePath = panImageResponse.getData().get(0).getPan_url();
                            if (!TextUtils.isEmpty(imagePath)) {
                                ExtractPanDetail extractPanDetail = new ExtractPanDetail();
                                extractPanDetail.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePath);
                            } else {

                            }
                        } else if (panImageResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        } else {
                            Toast.makeText(mcontext, panImageResponse.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PanImageResponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mcontext, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });*/


            Call<GetPanDetailsResponse> call = ApiClient.getClient2().create(ApiInterface.class).getPanDetails(params, typedFile);
            call.enqueue(new Callback<GetPanDetailsResponse>() {
                @Override
                public void onResponse(Call<GetPanDetailsResponse> call, retrofit2.Response<GetPanDetailsResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            // deleteImages();
                            GetPanDetailsResponse getPanDetailsResponse = response.body();
                            if (getPanDetailsResponse.getStatus().equalsIgnoreCase("success")) {
                                Toast.makeText(EditGSTActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();


                                DialogUtil.PanDetailGST(EditGSTActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), new DialogListner() {
                                    @Override
                                    public void onClickPositive() {

                                    }

                                    @Override
                                    public void onClickNegative() {

                                    }

                                    @Override
                                    public void onClickDetails(String name, String fathername, String storename, String id) {

                                        if (storename.equalsIgnoreCase("")) {
//                                            Constants.hideKeyboardwithoutPopulate(EditGSTActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                // UpadatePan(name, fathername, id, "");
                                                panName = name;
                                                panFatherName = fathername;
                                                panNumber = id;

                                                rvPanDetails.setVisibility(View.GONE);
                                                rvChequeDetails.setVisibility(View.VISIBLE);
                                                view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                DialogUtil.dismiss();


                                            } else {
                                                Constants.ShowNoInternet(EditGSTActivity.this);
                                            }

                                        } else {

                                            //      Constants.hideKeyboardwithoutPopulate(EditGSTActivity.this);
                                            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                                //  UpadatePan(name, fathername, id, storename);
                                            } else {
                                                Constants.ShowNoInternet(EditGSTActivity.this);
                                            }
                                        }


                                    }

                                    @Override
                                    public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                    }

                                    @Override
                                    public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                    }
                                });

                            } else {
                                if (getPanDetailsResponse.getReselectImage() != null && getPanDetailsResponse.getReselectImage().equalsIgnoreCase("1")) {
                                    panImagepath = "";
                                    Glide.with(EditGSTActivity.this).load(panImagepath).placeholder(R.drawable.pancard).into(ivPanImage);
                                    isPanSelected = false;
                                    ivPanImageSelected.setVisibility(View.GONE);
                                    tvPanClick.setVisibility(View.VISIBLE);

                                } else {


                                    Toast.makeText(EditGSTActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                    DialogUtil.PanDetailGST(EditGSTActivity.this, getPanDetailsResponse.getPanCardDetail().getName(), getPanDetailsResponse.getPanCardDetail().getFatherName(), getPanDetailsResponse.getPanCardDetail().getPanCardNumber(), new DialogListner() {
                                        @Override
                                        public void onClickPositive() {

                                        }

                                        @Override
                                        public void onClickNegative() {

                                        }

                                        @Override
                                        public void onClickDetails(String name, String fathername, String storename, String id) {

                                            panName = name;
                                            panFatherName = fathername;
                                            panNumber = id;
                                            rvPanDetails.setVisibility(View.GONE);
                                            rvChequeDetails.setVisibility(View.VISIBLE);
                                            view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                            DialogUtil.dismiss();

                                        }

                                        @Override
                                        public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                                        }

                                        @Override
                                        public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                                        }
                                    });
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditGSTActivity.this, "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetPanDetailsResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    //  Toast.makeText(EditGSTActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    Toast.makeText(EditGSTActivity.this, "#errorcode :- 2020 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();

                    // Toast.makeText(EditGSTActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    ///


    public void getBankDetails(String ifsc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_IFSC, ifsc);

        Log.e("DATA", "PID:" + str_process_id + " aID : " + sessionManager.getAgentID() + " IFSC:" + ifsc);
        Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getBankDetailsFromIfsc("Bearer " + sessionManager.getToken(), params);
        callUpload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.has("responseCode")) {
                                if (jsonObject.getInt("responseCode") == 200) {
                                    JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                    bank_name = result.getString("bank_name").toString();
                                    bank_branch = result.getString("bank_branch_name").toString();
                                    branch_address = result.getString("bank_address").toString();
                                } else if (jsonObject.getInt("responseCode") == 405) {
                                    sessionManager.logoutUser(EditGSTActivity.this);
                                } else if (jsonObject.getInt("responseCode") == 411) {
                                    sessionManager.logoutUser(EditGSTActivity.this);
                                } else {
                                }
                            }
                        } else {
                            Toast.makeText(EditGSTActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(EditGSTActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditGSTActivity.this, "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
