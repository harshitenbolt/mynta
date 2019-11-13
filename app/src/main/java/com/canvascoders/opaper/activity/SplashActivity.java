package com.canvascoders.opaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    String FCMID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);


        sessionManager = new SessionManager(SplashActivity.this);
        Mylogger.getInstance().setEnable(true);


        FirebaseApp.initializeApp(this);


        FCMID = FirebaseInstanceId.getInstance().getToken();
        Log.e("MyFirebaseIIDService", "" + FCMID);
        sessionManager.saveData(Constants.KEY_FCM_ID, FCMID);

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 2000);

    }

//    // variables to store extracted xml data
//    String uid, name, gender, yearOfBirth, careOf, villageTehsil, postOffice, district, state, postCode;
//
//    /**
//     * process xml string received from aadhaar card QR code
//     *
//     * @param scanData
//     */
//    protected void processScannedData1(String scanData) {
//        Log.d("Rajdeol", scanData);
//        XmlPullParserFactory pullParserFactory;
//
//        try {
//            // init the parserfactory
//            pullParserFactory = XmlPullParserFactory.newInstance();
//            // get the parser
//            XmlPullParser parser = pullParserFactory.newPullParser();
//
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(new StringReader(scanData));
//
//            // parse the XML
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_DOCUMENT) {
//                    Log.d("Rajdeol", "Start document");
//                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
//                    // extract data from tag
//                    //uid
//                    uid = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
//                    //name
//                    name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);
//                    //gender
//                    gender = parser.getAttributeValue(null, DataAttributes.AADHAR_GENDER_ATTR);
//                    // year of birth
//                    yearOfBirth = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
//                    // care of
//                    careOf = parser.getAttributeValue(null, DataAttributes.AADHAR_CO_ATTR);
//                    // village Tehsil
//                    villageTehsil = parser.getAttributeValue(null, DataAttributes.AADHAR_VTC_ATTR);
//                    // Post Office
//                    postOffice = parser.getAttributeValue(null, DataAttributes.AADHAR_PO_ATTR);
//                    // district
//                    district = parser.getAttributeValue(null, DataAttributes.AADHAR_DIST_ATTR);
//                    // state
//                    state = parser.getAttributeValue(null, DataAttributes.AADHAR_STATE_ATTR);
//                    // Post Code
//                    postCode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);
//
//                } else if (eventType == XmlPullParser.END_TAG) {
//                    Log.d("Rajdeol", "End tag " + parser.getName());
//
//                } else if (eventType == XmlPullParser.TEXT) {
//                    Log.d("Rajdeol", "Text " + parser.getText());
//
//                }
//                // update eventType
//                eventType = parser.next();
//            }
//
//            // display the data on screen
//            // displayScannedData();
//            Log.d("uid", uid);
//            Log.d("name", name);
//            Log.d("gender", gender);
//            Log.d("yearOfBirth", yearOfBirth);
//            Log.d("careOf", careOf);
//            Log.d("villageTehsil", villageTehsil);
//            Log.d("postOffice", postOffice);
//            Log.d("district", district);
//            Log.d("state", state);
//            Log.d("postCode", postCode);
//
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }// EO function


    @Override
    protected void onResume() {
        super.onResume();
    }
}
