package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.verifylocation.GetLocationResponse;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.LOCATION_SERVICE;
import static com.canvascoders.opaper.utils.Constants.showAlert;


public class MapsCurrentPlaceFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MapsCurrentPlaceFragment.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static LatLng mDefaultLocation = new LatLng(12.972442, 77.580643);
    // Used for selecting the current place.
    private final int mMaxEntries = 5;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    boolean enabled = false;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private String[] mLikelyPlaceNames = new String[mMaxEntries];
    private String[] mLikelyPlaceAddresses = new String[mMaxEntries];
    private String[] mLikelyPlaceAttributions = new String[mMaxEntries];
    private LatLng[] mLikelyPlaceLatLngs = new LatLng[mMaxEntries];
    private ProgressDialog mProgressDialog;
    Context mcontext;
    View view;
    SupportMapFragment mapFragment = null;
    String str_process_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_locate_me, container, false);


        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        }
        mcontext = this.getActivity();
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("getting Location...");
        progressDialog.setCancelable(false);
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Verifying Location...");
        sessionManager = new SessionManager(mcontext);


        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        mGoogleApiClient = new GoogleApiClient.Builder(mcontext)
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        OTPActivity.settitle(Constants.TITLE_LOCATION);

        createLocationRequest();

        Button button = (Button) view.findViewById(R.id.btn_locateme);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    locatioUpdate(v);
                } else {
                    Constants.ShowNoInternet(getActivity());
                }

            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager service = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
        enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled)
            enabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!enabled) {
            new AlertDialog.Builder(getActivity())
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
                            commanFragmentCallWithoutBackStack(new MobileFragment());
                        }
                    }).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.

        if (mMap != null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
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

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
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
                        (FrameLayout) view.findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
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
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMap != null)
            mMap = null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(2); // 10 meters
    }

    /**
     * Handles the result of the request for location permissions.
     */
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
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Toast.makeText(mcontext, "location changed", Toast.LENGTH_LONG).show();
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

    private void locatioUpdate(final View v) {
        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_PROCESS_ID, str_process_id);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.addProperty(Constants.PARAM_LONGITUDE, String.valueOf(mDefaultLocation.longitude));
        user.addProperty(Constants.PARAM_LATITUDE, String.valueOf(mDefaultLocation.latitude));
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());

        ApiClient.getClient().create(ApiInterface.class).verifyLocation("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetLocationResponse>() {
            @Override
            public void onResponse(Call<GetLocationResponse> call, retrofit2.Response<GetLocationResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetLocationResponse getLocationResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getLocationResponse.getResponse());
                    if (getLocationResponse.getResponseCode() == 200) {

                        commanFragmentCallWithoutBackStack(new AadharVerificationFragment());

                        /*Intent i = new Intent(mcontext, AadharVerificationFragment.class);
                        startActivity(i);
                        getActivity().finish();*/

                    }
                    if (getLocationResponse.getResponseCode() == 400) {

                        if (getLocationResponse.getValidation() != null) {
                            Validation validation = getLocationResponse.getValidation();
                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();
                            }
                            if (validation.getLongitude() != null && validation.getLongitude().length() > 0) {
                                Toast.makeText(getActivity(), validation.getLongitude(), Toast.LENGTH_LONG).show();
                            }
                            if (validation.getLatitude() != null && validation.getLatitude().length() > 0) {
                                Toast.makeText(getActivity(), validation.getLatitude(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity(), getLocationResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }

                        }
                        else{ Toast.makeText(getActivity(), getLocationResponse.getResponse(), Toast.LENGTH_LONG).show();

                        }


                    } else {
                        showAlert(v, getLocationResponse.getResponse(), false);
                        if (getLocationResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        }
                    }
                } else {
                    showAlert(v, getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetLocationResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
            Log.e("Call", "Destroi");
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


}
