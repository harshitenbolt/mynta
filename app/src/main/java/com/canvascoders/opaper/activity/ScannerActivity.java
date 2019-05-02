package com.canvascoders.opaper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DataAttributes;
import com.canvascoders.opaper.utils.Mylogger;
import com.google.gson.annotations.SerializedName;
import com.google.zxing.Result;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * Created by Nikhil on 17-Feb-17.
 */

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    String BFormate;
    String rawData;

    @Override

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("QR Code Scan");

        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, 100);

        }

        //String textParse = "</?xml version=\"1.0\" encoding=\"UTF-8\"?> <PrintLetterBarcodeData uid=\"652561415798\" name=\"Prajapati Nikhilkumar Jasvantlal \" gender=\"MALE\" yob=\"1988\" co=\"S/O: Prajapati Jasvantlal\" lm=\"null\" loc=\"null\" vtc=\"Fudeda\" po=\"Fudeda\" dist=\"Mahesana\" state=\"Gujarat\" pc=\"382860\"/>";
        // processScannedData1(textParse);
    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FORMAT, rawResult.getBarcodeFormat().toString());
        intent.putExtra(Constants.CONTENT, rawResult.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
        //Call back data to main activity

        /*Log.d("getBarcodeFormat", rawResult.getBarcodeFormat().toString());
        Log.d("getText", rawResult.getText());*/

//        processScannedData(rawResult.getText());
        /*if(!TextUtils.isEmpty(rawResult.getText())) {
            try {
               String xml = fixAadharXMLString(rawResult.getText());
                FilterXML(xml);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            *//*AadharDetail aadharDetail = AadharDAO.getAadharDetailFromXML(rawResult.getText());
            Log.d("getAadharNumber", aadharDetail.getAadharNumber());
            Log.d("getName", aadharDetail.getName());
            Log.d("getDob", aadharDetail.getDob());
            Log.d("getGender", aadharDetail.getGender());*//*
        }*/

//        processScannedData(temp);
//        BFormate = rawResult.getBarcodeFormat().toString();
//        rawData = rawResult.getText();
//        //  Toast.makeText(getApplicationContext(),rawResult.getText().toString(),Toast.LENGTH_LONG).show();
//        Log.d("resultdate", rawResult.getText().toString());
    }


    protected void processScannedData(String scanData) {

        Toast.makeText(getApplicationContext(), scanData, Toast.LENGTH_LONG).show();

        XmlPullParserFactory pullParserFactory;
        try {

            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));
            int eventType = parser.getEventType();

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String nametag = parser.getName();
                if (nametag.equalsIgnoreCase(DataAttributes.AADHAAR_DATA_TAG)) {
                    String udi = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    String name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);
                    String year = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    String pincode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);

                    Mylogger.getInstance().Logit("", "udi: "+udi);
                    Mylogger.getInstance().Logit("", "name: "+name);
                    Mylogger.getInstance().Logit("", "year: "+year);
                    Mylogger.getInstance().Logit("", "pincode: "+pincode);
                } else {
                    skip(parser);
                }
            }


            /*while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {

                   String udi = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    String name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);
                    String year = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    String pincode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);

                    Mylogger.getInstance().Logit("", "udi: "+udi);
                    Mylogger.getInstance().Logit("", "name: "+name);
                    Mylogger.getInstance().Logit("", "year: "+year);
                    Mylogger.getInstance().Logit("", "pincode: "+pincode);


                } else if (eventType == XmlPullParser.END_TAG) {
                } else if (eventType == XmlPullParser.TEXT) {
                }
                eventType = parser.next();
            }*/

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Mylogger.getInstance().Logit("XmlPullParserException", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Mylogger.getInstance().Logit("IOException", e.toString());
        } catch (SQLiteConstraintException e) {
            Mylogger.getInstance().Logit("SQLiteConstraintException", e.toString());
        }


    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    // variables to store extracted xml data
    String uid,name,gender,yearOfBirth,careOf,villageTehsil,postOffice,district,state,postCode;

    /**
     * process xml string received from aadhaar card QR code
     * @param scanData
     */
    protected void processScannedData1(String scanData){
        Log.d("Rajdeol",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                    // care of
                    careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                    // Post Office
                    postOffice = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                    // district
                    district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                    // state
                    state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                    // Post Code
                    postCode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);

                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol","Text "+parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen
            // displayScannedData();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function


    public static class AadharDAO {

        public static final int FATHER_SPOUSE_NAME_FIRST_PART_REMOVAL = 4;

        public static AadharDetail getAadharDetailFromXML(String xml) {
            xml = fixAadharXMLString(xml);
            XmlPullParserFactory xmlFactoryObject = null;
            AadharDetail aadharDetail = new AadharDetail();

            try {
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser aadharparser = xmlFactoryObject.newPullParser();
                aadharparser.setInput(new StringReader(xml));

                int event = aadharparser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = aadharparser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            break;

                        case XmlPullParser.END_TAG:
                            if (name != null && name.equals("PrintLetterBarcodeData")) {
                                aadharDetail.setAadharNumber(aadharparser.getAttributeValue(null, "uid"));
                                aadharDetail.setName(aadharparser.getAttributeValue(null, "name"));

                                String yob = aadharparser.getAttributeValue(null, "yob");

                                Calendar c = Calendar.getInstance();
                                c.set(Integer.parseInt(yob), 1, 1);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                aadharDetail.setDob(sdf.format(c.getTime()));

                                String gender = aadharparser.getAttributeValue(null, "gender");
                                if (Objects.equals(gender, "M") || Objects.equals(gender, "MALE"))
                                    gender = "Male";
                                else if (Objects.equals(gender, "F") || Objects.equals(gender, "FEMALE"))
                                    gender = "Female";

                                aadharDetail.setGender(gender);
                                Log.d("AadharDAO", "Gender: " + gender);

                                aadharDetail.setAddress(getAttributeValue(aadharparser, "house") +
                                        getAttributeValue(aadharparser, "street") +
                                        getAttributeValue(aadharparser, "lm") +
                                        getAttributeValue(aadharparser, "po") +
                                        getAttributeValue(aadharparser, "dist") +
                                        getAttributeValue(aadharparser, "subdist") +
                                        getAttributeValue(aadharparser, "state") +
                                        getAttributeValue(aadharparser, "pc")
                                );

                                aadharDetail.setSonOf(getAttributeValue(aadharparser, "co").trim().substring(FATHER_SPOUSE_NAME_FIRST_PART_REMOVAL));
                            }
                            break;
                    }
                    event = aadharparser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return aadharDetail;
        }

        private static String getAttributeValue(XmlPullParser aadharParser, String attributeName) {
            String value = aadharParser.getAttributeValue(null, attributeName);
            if (value == null)
                value = "";
            return value;
        }

        public static String fixAadharXMLString(String xml) {
            if (xml.startsWith("&lt;?xml")) {
                int firstDeclarationTagEnd = xml.indexOf("&gt;");
                return xml.substring(firstDeclarationTagEnd + 1);
            }
            return xml;
        }
    }

    public static class AadharDetail {

        private String name;
        private String address;
        private String gender;
        @SerializedName(value = "aadhar_no")
        private String aadharNumber;
        @SerializedName(value = "sonOf")
        private String sonOf;
        private String dob;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAadharNumber() {
            return aadharNumber;
        }

        public void setAadharNumber(String aadharNumber) {
            this.aadharNumber = aadharNumber;
        }

        public String getSonOf() {
            return sonOf;
        }

        public void setSonOf(String sonOf) {
            this.sonOf = sonOf;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }
    }


    public void FilterXML(String xmlAdhar) throws ParserConfigurationException, IOException, SAXException {
        String sStringToParse;

        // put your XML value into the sStringToParse variable
        sStringToParse = new String(xmlAdhar); //("<PrintLetterBarcodeData uid='741647613082' name='Pasikanti Enosh Kumar' gender='M' yob='1992' co='S/O Srinivas' house='SPLD-986' street='MALLARAM' loc='P V COLONY' vtc='Manuguru' po='P.V.Township' dist='Khammam' state='Andhra Pradesh' pc='507125'/>");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(sStringToParse.getBytes("utf-8")));
        NodeList nlRecords = doc.getElementsByTagName("PrintLetterBarcodeData");

        int num = nlRecords.getLength();

        for (int i = 0; i < num; i++) {
            Element node = (Element) nlRecords.item(i);

            System.out.println("List attributes for node: " + node.getNodeName());

            // get a map containing the attributes of this node
            NamedNodeMap attributes = node.getAttributes();

            // get the number of nodes in this map
            int numAttrs = attributes.getLength();

            for (int j = 0; j < numAttrs; j++) {
                Attr attr = (Attr) attributes.item(j);

                String attrName = attr.getNodeName();
                String attrValue = attr.getNodeValue();

                // Do your stuff here
                System.out.println("Found attribute: " + attrName + " with value: " + attrValue);

            }

        }
    }
}