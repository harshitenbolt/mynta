package com.canvascoders.opaper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;


import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.BillList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    //-------------------------------------------------------------------------
    public final static int APP_VERSION = 36; // need to set +1 here befor apk sent on last update before 942018 @ 12 version
    //-------------------------------------------------------------------------
    public final static String FORMAT = "format";
    public final static String CONTENT = "content";
    public final static String DATA = "data";
    public static final String KEY_DOCUMENT_ID = "document_id";
    public static final String KEY_STORES = "stores";
    public static final CharSequence CAC_STORE = "Bet - CAC";
    public static final CharSequence RENTAL = "Rental";
    public static final CharSequence ASSISTED = "Assisted";
    public static final String KEY_IS_EDIT_ADDENDUM_FLAG = "is_addendum_rate_edit";

    //Whole App Base URL for API manage n Call
    public static String Image_name;
   // public static String BaseURL = "https://myntraopaper.opaper.in/api3/";
         public static String BaseURL = "https://mystaging.opaper.in/api3/";
    // test server 20
    ///xfbd
    public final static String APP_NAME = "my_stage"; //stage
   // public final static String APP_NAME = "my_live"; //live
  //  public static String BaseImageURL = "https://myntraopaper.opaper.in"; // Also change when change to live one.
     public static String BaseImageURL = "https://mystaging.opaper.in"; // Also change when change to live one.

    public static String BaseURLOCR = "https://lynk.host/api/";

    // public static String BaseURL = "http://139.59.94.135/api3/";  // test server 2


    //public static String APKROOT = "https://myntraopaper.opaper.in/";

    //staging apk root
      public static String APKROOT = "https://mystaging.opaper.in/";


    public static String AADHAR_VID_URL = "https://resident.uidai.gov.in/web/resident/vidgeneration";
    public static String IMGEFORDIGIO = "http://139.59.59.181/app_logo.png";  // Image for Digio


    public static final String OCRMEREK = "https://api.merak.ai/v1/text-recognition/cheque/recognize/?key=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJlbWFpbCI6ImJpa2FzaC5taXNocmFAbXludHJhLmNvbSIsIm9yZ2FuaXphdGlvbl9pZCI6NCwiaWF0IjoxNTM3MzEwMDYyfQ.3jXCcyKnyXmnJIyvI1XoZ6ay5OKz8uBnQD_OUWywNG8";


    //Api's Called for Comment List
    public static String GET_COMMENT_LIST = "support-thread-detail";
    public static String showAdhar = "0";


    //Static Fixde Values
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_ADMIN = 1;
    public static final int VIEW_TYPE_ADMIN2 = 11;
    public static final int VIEW_TYPE_LOADING = 99;
    //
    public static boolean fromnotif = false;
    public static String ISFROMPUSHNOTIFICATION = "false";
    public static String UserName = "99filings";
    public static String PasswordORApiKey = "MEXTCHw3fOW5dduuvSiWHREwOGIwaPfYWjJgiOuC";
    public static String Email = "admin@signzy.com";

//    public static String DIGIO_CLIENT_ID = "AIJZGP76FTA5PGED1RLPOFHXQF5FAKID"; // none production
//    public static String DIGIO_CLIENT_SECRET = "AHMJWDE2VTIIMHPZ64QA5QLETI1YPM9Q";    // none production

    //For Digio Client_id and Secret Code live
    public static String DIGIO_CLIENT_ID = "AIFKNWV8B3OSU21ABJQTGSZ5GSJOIVYY";
    public static String DIGIO_CLIENT_SECRET = "ZMQG9SCZ576KLDY4EQRH53CQ94C84P3N";
    public static String DIGIO_BASE_URL = "https://api.digio.in/";

    public static Pattern PAN_PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

    public static Pattern GST_PATTERN = Pattern.compile("[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}");
    //    "[0-9]{2}[A-Z,a-z]{5}[0-9]{4}[A-Z,a-z]{1}[0-9A-Z,a-z]{1}[z,Z]{1}[0-9A-Z,a-z]{1}"

    //OTP Tmeplate for SMS
    public static String OTP_TEMPLATE = "One Time Password is ";
    public static String OTP = "otp";
    public static String OTP_TEMPLATE1 = " ,Please use this OTP to complete the transaction. This is usable once and expire in 10 minutes. Please do not share this with anyone.";
    //SMS Getway
    public static String SMSSPI = "https://control.msg91.com/api/sendhttp.php?";  // msg
    public static String authkey = "127127AlTqjnGW757f20a0e";//Your authentication key for sms
    public static String sendrid = "Enbolt";
    public static String route = "4";

    public static String IFSC_FIND = "https://api.techm.co.in/api/v1/ifsc/";  // msg
    public static String PINCODE_API = "http://postalpincode.in/api/pincode/";  // msg

    //SingzyBaseURL for Aadhar and PAN Validation
    public static String singzyBaseURL = "https://signzy.tech/api/v2/";  //

    public static String GET_BANK_DETAILS = BaseURL + "get-bank-details";
    public static String BANK_DETAILS_UPDATION = BaseURL + "bank-details-updation";

    public static BillList billList = new BillList();


    //Key for Session Store
    public static String KEY_AGENTID = "userid";
    public static String KEY_AGENT_ID = "agent_id";
    public static String KEY_TOKEN = "tkn";
    public static String KEY_EMAIL = "email";
    public static String KEY_NAME = "name";
    public static String KEY_INVOICE_NUM = "invoice_num";
    public static String KEY_AADHAR_NAME = "uid_name";
    public static String KEY_RH_ID = "rh_id";
    public static String KEY_EMP_ID = "emp_id";

    public static String KEY_EMP_MOBILE = "mobile";
    public static String KEY_EMP_CITY = "city";
    public static String KEY_IS_MOBILE_VERIFY = "is_mobile_verify";
    public static String KEY_DC = "dc";
    public static String KEY_PASSWORD = "password";
    public static String KEY_FCM_ID = "FCMID";
    public static String KEY_PROCESS_ID = "proccess_id";
    public static String KEY_ID = "id";
    public static String KEY_UID = "uid";
    public static Uri KEY_PHOTO;
    public static String KEY_PINCODE = "pincode";
    public static String KEY_YEAR = "year";
    public static String KEY_INVOICE_ID = "invoice_id";
    public static String INVOICE_TYPE = "invoice_type";
    public static String INVOICE_NUMBER = "invoice_number";
    public static String SIGNED = "signed";
    public static String KEY_SUPPORT_ID = "support_id";
    public static String KEY_EDIT_DETAIL = "edit_detail";


    public static String dataRate[] = null;
    //-------------------------

    //For Vendor Mobile info
    public static String KEY_VENDOR_MOBILE = "vendor_mobile";

    public static String ValidationAPI1 = "submit-details-validation-1";
    public static String ValidationAPI2 = "submit-details-validation-2";
    public static String ValidationAPI3 = "submit-details-validation-3";


    //all api parameter
    public static String PARAM_EMAIL = "email";
    public static String PARAM_SCREEN_NAME = "screen_name";
    public static String PARAM_ATTACHMENT = "attachment";

    public static String PARAM_SUBJECT_NAME = "subject";

    public static String PARAM_DESCREPTION = "descreption";
    public static String PARAM_DESCRIPTION = "description";

    public static String PARAM_PRIORITY = "priority";
    public static String PARAM_ATTACHMENT_NAME = "attachment_name";


    public static String PARAM_EMAIL_ID = "email_id";
    public static String PARAM_ID = "id";
    public static String PARAM_MOBILE_NUMBER = "mobile_number";
    public static String PARAM_REMEMBER_TOKEN = "remember_token";
    public static String PARAM_PASSWORD = "password";
    public static String PARAM_FCM_ID = "fcm_id";
    public static String PARAM_DEVICE_INFO = "device_info";
    public static String PARAM_TOKEN = "token";
    public static String PARAM_AGENT_ID = "agent_id";

    public static String PARAM_IS_EXPIRED = "is_expired";

    public static String PARAM_EXPIRED_SOON = "expired_soon";
    public static String PARAM_IS_DOC = "is_doc";

    public static String PARAM_YEAR = "year";
    public static String PARAM_TASK_ID = "task_id";
    public static String PARAM_SUB_TASK_ID = "sub_task_id";
    public static String PARAM_IS_INVOICE = "is_invoice";


    public static String PARAM_STATUS = "status";
    public static String PARAM_SUB_TASK_REASON_ID = "sub_task_reason_id";
    public static String PARAM_SUB_TASK_REASON_TEXT = "sub_task_reason_text";
    public static String PARAM_IS_PAUSE = "is_pause";
    public static String PARAM_SEARCH = "search";
    public static String PARAM_ADUMDUM = "addendum";
    public static String PARAM_IMAGE = "image";
    public static String PARAM_DL_BACK_IMAGE = "driving_licence_back_image";

    public static String PARAM_BACKSIDE_IMAGE = "back_side_image";
    public static String PARAM_CURRENT_RESIDENTIAL = "current_residential_address";
    public static String PARAM_CURRENT_ADDRESS = "current_address";
    public static String PARAM_CURRENT_ADDRESS1 = "current_address1";

    public static String PARAM_CURRENT_ADDRESS_LANDMARK = "current_address_landmark";

    public static String PARAM_CURRENT_ADDRESS_PINCODE = "current_address_picode";

    public static String PARAM_CURRENT_ADDRESS_CITY = "current_address_city";
    public static String PARAM_CURRENT_ADDRESS_STATE = "current_address_state";
    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS = "permanent_residential_address";
    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS1 = "permanent_residential_address1";
    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS_LANDMARK = "permanent_residential_address_landmark";
    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS_PINCODE = "permanent_residential_address_picode";

    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS_CITY = "permanent_residential_address_city";
    public static String PARAM_PERMANENT_RESIDENTIAL_ADDRESS_STATE = "permanent_residential_address_state";

    public static String PARAM_DELIVERY_BOY_ID = "delivery_boy_id";
    public static String PARAM_PERMANENT_ADDRESS = "permanent_address";
    public static String PARAM_PERMANENT_ADDRESS1 = "permanent_address1";
    public static String PARAM_PERMANENT_ADDRESS_LANDMARK = "permanent_address_landmark";
    public static String PARAM_PERMANENT_ADDRESS_PINCODE = "permanent_address_picode";
    public static String PARAM_PERMANENT_ADDRESS_CITY = "permanent_address_city";
    public static String PARAM_PERMANENT_ADDRESS_STATE = "permanent_address_state";

    public static String PHONE_NUMBER = "phone_number";
    public static String PARAM_GENDER = "gender";

    public static String NAME = "name";
    public static String ROUTE_NUMBER = "route_number";
    public static String PARAM_BLOOD_GROUP = "blood_group";

    public static String PARAMS_INVOICE_TYPE = "invoice_type";
    public static String PARAMS_FROM_DATE = "from_date";
    public static String PARAMS_TO_DATE = "to_date";
    public static String PARAM_MOBILE_NO = "mobile_no";
    public static String PARAM_MOBILE = "mobile";
    public static String PARAM_DRIVING_LICENCE_NUM = "driving_licence_num";
    public static String PARAM_DRIVING_LICENCE_NUMBER = "driving_licence_number";
    public static String PARAM_DRIVING_LICENCE_DOB = "driving_licence_dob";
    public static String PARAM_DRIVING_LICENCE_VEHICLE = "vehicle_for_delivery";

    public static String PARAM_AADHAR_NO = "aadhaar_no";
    public static String PARAM_AADHAR_DOB = "aadhaar_dob";
    public static String PARAM_AADHAR_NAME = "aadhaar_name";


    public static String PARAM_VOTER_NO = "voter_id_num";


    public static String PARAM_PROCESS_ID = "proccess_id";
    public static String PARAM_UID = "uid";
    public static String PARAM_KYC_TYPE = "kyc_type";

    public static String PARAM_VOTER_NAME = "voter_name";
    public static String PARAM_VOTER_FATHER_NAME = "voter_father_name";
    public static String PARAM_VOTER_DOB = "voter_dob";
    public static String PARAM_VOTER_ID_NUM = "voter_id_num";
    public static String PARAM_VOTER_ID_NUMBER = "voter_id_number";
    public static String PARAM_BIRTH_DATE = "birth_date";
    public static String PARAM_VOTER_CARD_FRONT = "voter_card_front";
    public static String PARAM_VOTER_CARD_BACK = "voter_card_back";
    public static String PARAM_VOTER_ID_DETAIL_ID = "voter_id_detail_id";


    public static String PARAM_DL_DETAIL_ID = "driving_licence_detail_id";
    public static String PARAM_DL_NUMBER = "driving_licence_number";

    public static String PARAM_DL_NAME = "dl_name";
    public static String PARAM_DL_FATHER_NAME = "dl_father_name";
    public static String PARAM_DL_DOB = "dl_dob";
    public static String PARAM_DL_ID_NUM = "dl_number";
    public static String PARAM_DL_CARD_FRONT = "dl_card_front";
    public static String PARAM_DL_CARD_BACK = "dl_card_back";
    public static String PARAM_DRIVING_LICENCE_DETAIL = "driving_licence_detail";
    public static String PARAM_AADHAR_LICENCE_DETAIL = "aadhar_card_detail";
    public static String PARAM_VOTERID_DETAIL = "voter_id_detail";
    public static String PARAM_SUPPORT_ID = "support_id";
    public static String PARAM_NAME = "name";
    public static String PARAM_YEAR_OF_BIRTH = "yob";
    public static String PARAM_PINCODE = "pincode";
    public static String PARAM_STORE_FULL_ADDRESS = "store_full_address";
    public static String PARAM_AADHAR_FRONT = "adhar_card_front";
    public static String PARAM_AADHAR_BACK = "adhar_card_back";
    public static String PARAM_ESIGN_URL = "esign_url";
    public static String PARAM_IFSC = "ifsc";
    public static String PARAM_AC_NAME = "ac_name";
    public static String PARAM_BANK_AC = "bank_ac";
    public static String PARAM_CANCELLED_CHEQUE = "cancelled_cheque";
    public static String PARAM_IF_SHOP_ACT = "if_shop_act";
    public static String PARAM_SHOP_IMAGE = "shop_image";

    public static String PARAM_OWNER_IMAGE = "owner_image";
    public static String PARAM_FATHER_NAME = "father_name";
    public static String PARAM_IS_EDIT = "is_edit";
    public static String PARAM_IF_GST = "if_gst";
    public static String PARAM_GSTN = "gstn";
    public static String PARAM_STATE = "state";
    public static String PARAM_CITY = "city";
    public static String PARAM_DC = "dc";
    public static String PARAM_STORE_NAME = "store_name";
    public static String PARAM_STORE_ADDRESS = "store_address";
    public static String PARAM_STORE_ADDRESS1 = "store_address1";
    public static String PARAM_STORE_ADDRESS_LANDMARK = "store_address_landmark";

    public static String PARAM_LICENCE_NO = "license_no";
    public static String PARAM_OWNER_NAME = "owner_name";
    public static String PARAM_INVOICE_ID = "invoice_id";

    public static String PARAM_STORE_TYPE = "store_type";
    public static String PARAM_ENBOLT_ID = "enbolt_id";
    public static String PARAM_LONGITUDE = "longitude";

    public static String PARAM_IS_PAN_EXIST = "is_pan_exist";
    public static String PARAM_IS_BANK_EXIST = "is_bank_exist";


    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_PAN_NO = "pan_no";
    public static String PARAM_pan_matched_kiran_proccess_id = "pan_matched_kiran_proccess_id";

    public static String PARAM_GST_PAN_NO = "gst_pan_no";


    public static String PARAM_PARAM_BANK_NAME = "bank_name";

    public static String PARAM_BANK_BRANCH_NAME = "bank_branch_name";

    public static String PARAM_PAYEE_NAME = "payee_name";

    public static String PARAM_BANK_ADDRESS = "bank_address";


    public static String PARAM_PAN_NAME = "pan_name";
    public static String PARAM_PAN_CARD_FRONT = "pan_card_front";

    public static String PARAM_GST_CERTIFICATE = "gst_certificate_image";
    public static String PARAM_RATE = "rate";
    public static String PARAM_USER_NAME = "username";
    public static String PARAM_REQUEST_ID = "request_id";
    public static String PARAM_EDIT_DETAIL = "edit_detail";
    public static String PARAM_STORE_IMAGE = "store_image";
    public static String PARAM_BACK_ACCOUNT_NUMBER = "bank_account_number";
    public static String PARAM_BACK_ACCOUNT_HOLDER_NAME = "bank_account_holder_name";
    public static String PARAM_BACK_ACCOUNT_IFSC_CODE = "ifsc_code";


    public static String PARAM_IDENTIFIRE = "identifier";
    public static String PARAM_AADHAR_ID = "aadhaar_id";
    public static String PARAM_VID = "vid";
    public static String PARAM_FILE_NAME = "file_name";

    public static String PARAM_APPROVAL_GST_ID = "approval_gst_id";

    public static String PARAM_FILE_URL = "file_url";
    public static String PARAM_APP_NAME = "app_name";
    public static String PARAM_BACKSIDE_FILE_NAME = "back_side_file_name";
    public static String PARAM_BACKSIDE_FILE_URL = "back_side_file_url";


    public static String PARAM_PAN_CARD_DETAIL_ID = "pan_card_detail_id";
    public static String PARAM_PAN_CARD_NUMBER = "pan_card_number";
    public static String PARAM_DOB = "dob";
    public static String PARAM_ROUTE = "route";
    public static String PARAM_RESIDENTIAL_ADDRESS = "residential_address";
    public static String PARAM_RESIDENTIAL_ADDRESS1 = "residential_address1";
    public static String PARAM_RESIDENTIAL_LANDMARK = "residential_address_landmark";
    public static String PARAM_RESIDENTIAL_PINCODE = "residential_address_picode";
    public static String PARAM_RESIDENTIAL_CITY = "residential_address_city";
    public static String PARAM_RESIDENTIAL_STATE = "residential_address_state";


    public static String PARAM_VENDOR_TYPE = "vendor_type";
    public static String PARAM_TYPE = "type";
    public static String PARAM_VENDOR_TYPE_DETAIL = "vendor_type_detail";
    public static String PARAM_LOCALITY = "locality";
    public static String PARAM_APPROACH = "approach";
    public static String PARAM_LANGUAGES = "languages";
    public static String PARAM_DL_ISSUE_DATE = "driving_licence_issue_date";
    public static String PARAM_DL_VALID_TILL_DATE = "driving_licence_till_date";

    public static String PARAM_IS_ADD_FROM_PROFILE = "is_add_from_profile";
    public static String PARAM_SHIPMENT_TRANS = "shipment_transfer";
    public static String PARAM_PARTNER_WITH_OTHER = "partner_with_other_ecommerce";
    public static String PARAM_STORE_TYPE_CONFIG = "store_type_config";


    public static String PARAM_COMMENT = "comment";
    public static String PARAM_SIGNERS = "signers";
    public static String PARAM_FILE_DATA = "file_data";
    public static String PARAM_EXPIRE_IN_DAY = "expire_in_days";
    public static String PARAM_DISPLAY_ON_PAGE = "display_on_page";
    public static String PARAM_NOTIFY_SIGNERS = "notify_signers";

    public static String PARAM_SERVICE = "service";
    public static String PARAM_ITEMID = "itemId";
    public static String PARAM_ACCESSTOKEN = "accessToken";
    public static String PARAM_TASK = "task";
    public static String PARAM_ESSENTIALS = "essentials";


    // set title
    public static String TITLE_DASHBOARD = "Dashboard";
    public static String TITLE_MOBILE_AUTH = "Mobile Verification";
    public static String TITLE_SCANNER = "QR Code Scan";
    public static String TITLE_AGREEMENT = "Agreement Details";
    public static String TITLE_CHEQUE = "Cheque Verification";
    public static String TITLE_DOCUMENT = "Vendor Documents";
    public static String TITLE_ADD_DEL_BOY = "Add Delivery Boy";
    public static String TITLE_GST_DETAIL = "GST Details";
    public static String TITLE_VENDOR_DETAIL_MENSA = "MENSA Vendor Details";
    public static String TITLE_INVOICE_DETAIL = "Invoice Detail";
    public static String TITLE_INVOICE_LIST = "Invoice List";
    public static String TITLE_GST_INVOICE_LIST = "GST Invoice List";
    public static String TITLE_DEBIT_INVOICE_LIST = "Debit Invoice List";
    public static String TITLE_LOCATION = "Location";
    public static String TITLE_NOC_DETAIL = "Noc Details";
    public static String TITLE_NOTIFICATION = "Notification";
    public static String TITLE_PAN_CARD_VERIFICATION = "PAN Verification";
    public static String TITLE_PROFILE = "Profile";
    public static String TITLE_RATE_UPDATE = "Rate Update";
    public static String TITLE_DELIVERY_BOY = "Delivery boy";
    public static String TITLE_REPORT = "Reports";
    public static String TITLE_SUPPORT = "Support";
    public static String TITLE_VENDOR_LIST = "Vendor List";
    public static String TITLE_VENDOR_DETAIL = "Vendor Details";
    public static final String TITLE_BANK_DETAILS = "Bank Details";


    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        //String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        String expression =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,6})$";

        //String expression = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void showAlert(View v, String msg, boolean isValid) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        if (isValid) {
            sbView.setBackgroundColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.colorPrimary));
        } else {
            sbView.setBackgroundColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.colorRed));
        }
        TextView tv = (TextView) sbView.findViewById(R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void ShowNoInternet(final Context context) {
        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("No Internet Connection");
        alertDialogBuilder.setMessage("You are offline,Please check your internet connection,then try again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //appCompatActivity.finish();
                arg0.dismiss();


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    // url = file path or whatever suitable URL you want.

    public static String getMimeType(String url) {
        String type = "application/octact-stream";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


}
