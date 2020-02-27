package com.canvascoders.opaper.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddDeliveryBoysActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.EditGSTActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.fragment.PanVerificationFragment;
import com.canvascoders.opaper.helper.DialogListner;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogUtil {
    private static Dialog dialog;
    static Boolean selected = false;
    public static EditText etDlName, etFathername, etDlDob, etDlNumber, etCity, etState;
    private static SessionManager sessionManager;
    //pan widgets
    public static EditText etPanName, etStoreName, etPanFatherName, etPanNumber, etChequeNumber, etPayeeName, etIfscCode, etBankName, etBranchName, etBankAddress;
    static DatePicker datePicker;
    static boolean individua = true;
    //voter widgets
    public static EditText etVotername, etVoterFatherName, etVoterDateofBirth, etVoterIdNumber;
    public static LinearLayout llStoreDetails;
    Button btSubmit;
    static Context context;
    static ProgressDialog progressDialog;
    ImageView ivClose;
    public static Spinner dc;
    private static ArrayList<String> dcLists = new ArrayList<>();

    static String order_type = "1";
    private static final String TAG = "DialogUtil";


    public static void AdharDetail(Context mContext, String name, String year, String pincode, String udi, final DialogListner dialogInterface) {
        EditText etName, etYear, etPicnode, etUDID;
        Button btSubmit;
        ImageView ivClose;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogueaadhar_detail);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        etName = dialog.findViewById(R.id.etAdharName);
        etYear = dialog.findViewById(R.id.etYearofBirth);
        etPicnode = dialog.findViewById(R.id.etPincode);
        etUDID = dialog.findViewById(R.id.etAdharNumber);
        ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        etName.setText(name);
        etYear.setText(year);
        etPicnode.setText(pincode);
        etUDID.setText(udi);
        btSubmit = dialog.findViewById(R.id.btSubmitAdharDet);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.onClickDetails(etName.getText().toString(), etYear.getText().toString(), etPicnode.getText().toString(), etUDID.getText().toString());

            }
        });

        dialog.show();
    }


    public static void VoterDetail(Context mContext, String name, String id, String fathername, String birthdate, final DialogListner dialogInterface) {

        Button btSubmit;
        ImageView ivClose;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguevoter_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        etVotername = dialog.findViewById(R.id.etVotername);
        etVoterFatherName = dialog.findViewById(R.id.etVoterFathername);
        etVoterDateofBirth = dialog.findViewById(R.id.etDateofBorthvoter);
        etVoterIdNumber = dialog.findViewById(R.id.etVoterNumber);
        etVotername.setText(name);
        etVoterFatherName.setText(fathername);
        etVoterIdNumber.setText(id);
        etVoterDateofBirth.setText(birthdate);
        ivClose = dialog.findViewById(R.id.ivClose);

        etVoterDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                Date today1 = new Date();
                Calendar c3 = Calendar.getInstance();
                c3.setTime(today1);
                c3.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate1 = c3.getTime().getTime(); //
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker = new DatePicker(mContext);
                                datePicker.init(year, monthOfYear + 1, dayOfMonth, null);


                                String monthString = String.valueOf(monthOfYear + 1);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }


                                //logic for add 0 in string if date digit is on 1 only
                                String daysString = String.valueOf(dayOfMonth);
                                if (daysString.length() == 1) {
                                    daysString = "0" + daysString;
                                }

                                etVoterDateofBirth.setText(year + "-" + monthString + "-" + daysString);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.getDatePicker().setMaxDate(minDate1);
                if (datePicker != null) {
                    datePickerDialog1.updateDate(datePicker.getYear(), datePicker.getMonth() - 1, datePicker.getDayOfMonth());

                }
                datePickerDialog1.show();

            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitVoterdetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogInterface.cancel();
                if (isValid(v)) {
                    dialogInterface.onClickDetails(etVotername.getText().toString(), etVoterFatherName.getText().toString(), etVoterDateofBirth.getText().toString(), etVoterIdNumber.getText().toString());
                }
            }

            private boolean isValid(View v) {
                if (etVotername.getText().toString().equalsIgnoreCase("")) {
                    etVotername.setError("Provide name");
                    return false;
                }
                if (etVoterFatherName.getText().toString().equalsIgnoreCase("")) {
                    etVoterFatherName.setError("Provide father name");
                    return false;
                }
                if (etVoterDateofBirth.getText().toString().equalsIgnoreCase("")) {
                    etVoterDateofBirth.setError("Provide Date of Birth");
                    return false;
                }
                if (etVoterIdNumber.getText().toString().equalsIgnoreCase("")) {
                    etVoterIdNumber.setError("Provide Voter ID Number");
                    return false;
                }
                return true;
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void GSTDetails(Context mContext, String name, String id, String fathername, String birthdate, final DialogListner dialogInterface) {

        Button btSubmit;
        ImageView ivClose;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguevoter_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        etVotername = dialog.findViewById(R.id.etVotername);
        etVoterFatherName = dialog.findViewById(R.id.etVoterFathername);
        etVoterDateofBirth = dialog.findViewById(R.id.etDateofBorthvoter);
        etVoterIdNumber = dialog.findViewById(R.id.etVoterNumber);
        etVotername.setText(name);
        etVoterFatherName.setText(fathername);
        etVoterIdNumber.setText(id);
        etVoterDateofBirth.setText(birthdate);
        ivClose = dialog.findViewById(R.id.ivClose);

        etVoterDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                Date today1 = new Date();
                Calendar c3 = Calendar.getInstance();
                c3.setTime(today1);
                c3.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate1 = c3.getTime().getTime(); //
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker = new DatePicker(mContext);
                                datePicker.init(year, monthOfYear + 1, dayOfMonth, null);


                                String monthString = String.valueOf(monthOfYear + 1);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }


                                //logic for add 0 in string if date digit is on 1 only
                                String daysString = String.valueOf(dayOfMonth);
                                if (daysString.length() == 1) {
                                    daysString = "0" + daysString;
                                }

                                etVoterDateofBirth.setText(year + "-" + monthString + "-" + daysString);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.getDatePicker().setMaxDate(minDate1);
                if (datePicker != null) {
                    datePickerDialog1.updateDate(datePicker.getYear(), datePicker.getMonth() - 1, datePicker.getDayOfMonth());

                }
                datePickerDialog1.show();

            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitVoterdetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogInterface.cancel();
                if (isValid(v)) {
                    dialogInterface.onClickDetails(etVotername.getText().toString(), etVoterFatherName.getText().toString(), etVoterDateofBirth.getText().toString(), etVoterIdNumber.getText().toString());
                }
            }

            private boolean isValid(View v) {
                if (etVotername.getText().toString().equalsIgnoreCase("")) {
                    etVotername.setError("Provide name");
                    return false;
                }
                if (etVoterFatherName.getText().toString().equalsIgnoreCase("")) {
                    etVoterFatherName.setError("Provide father name");
                    return false;
                }
                if (etVoterDateofBirth.getText().toString().equalsIgnoreCase("")) {
                    etVoterDateofBirth.setError("Provide Date of Birth");
                    return false;
                }
                if (etVoterIdNumber.getText().toString().equalsIgnoreCase("")) {
                    etVoterIdNumber.setError("Provide Voter ID Number");
                    return false;
                }
                return true;
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /*public static void PauseDetails(Context mContext, String name, String id, String fathername, String birthdate, final DialogListner dialogInterface) {

        Button btSubmit;
        EditText etDescription;
        ImageView ivClose, ivIssueImage;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguetask_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        etDescription = dialog.findViewById(R.id.etDescription);
        ivClose = dialog.findViewById(R.id.ivClose);
        ivIssueImage = dialog.findViewById(R.id.ivIssueImage);
        ivIssueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;


                Intent chooseImageIntent = ImagePicker.getCameraIntent(mContext);
                mContext.startActivity(chooseImageIntent, IMAGE_SHOP_IMG);
            }
        });

        btSubmit = dialog.findViewById(R.id.btSubmitReasonDetails);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogInterface.cancel();
                dialogInterface.onClickDetails("", "", "", "");

               *//* if (isValid(v)) {
                    dialogInterface.onClickDetails(etVotername.getText().toString(), etVoterFatherName.getText().toString(), etVoterDateofBirth.getText().toString(), etVoterIdNumber.getText().toString());
                }*//*
            }

            private boolean isValid(View v) {
                if (etVotername.getText().toString().equalsIgnoreCase("")) {
                    etVotername.setError("Provide name");
                    return false;
                }
                if (etVoterFatherName.getText().toString().equalsIgnoreCase("")) {
                    etVoterFatherName.setError("Provide father name");
                    return false;
                }
                if (etVoterDateofBirth.getText().toString().equalsIgnoreCase("")) {
                    etVoterDateofBirth.setError("Provide Date of Birth");
                    return false;
                }
                if (etVoterIdNumber.getText().toString().equalsIgnoreCase("")) {
                    etVoterIdNumber.setError("Provide Voter ID Number");
                    return false;
                }
                return true;
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
*/

    public static void DrivingDetail(Context mContext, String name, String fathername, String birthdate, String id, final DialogListner dialogInterface) {

        Button btSubmit;
        ImageView ivClose;


        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguedl_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.ivClose);
        etDlName = dialog.findViewById(R.id.etDlName);
        etDlName.setText(name);
        etFathername = dialog.findViewById(R.id.etDlFatherName);
        etDlDob = dialog.findViewById(R.id.etDlDateofBirth);
        etDlNumber = dialog.findViewById(R.id.etDlNumber);
        etFathername.setText(fathername);
        etDlDob.setText(birthdate);
        etDlNumber.setText(id);
        etDlDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c2 = Calendar.getInstance();
                mYear = c2.get(Calendar.YEAR);
                mMonth = c2.get(Calendar.MONTH);
                mDay = c2.get(Calendar.DAY_OF_MONTH);

                Date today1 = new Date();
                Calendar c3 = Calendar.getInstance();
                c3.setTime(today1);
                c3.add(Calendar.YEAR, -18); // Subtract 18 year
                long minDate1 = c3.getTime().getTime(); //
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(mContext,
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

                                etDlDob.setText(year + "-" + monthString + "-" + daysString);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.getDatePicker().setMaxDate(minDate1);
                datePickerDialog1.show();
            }
        });
        ivClose = dialog.findViewById(R.id.ivClose);

        btSubmit = dialog.findViewById(R.id.btSubmitDlDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            //hideKeyboardwithoutPopulate(mContext);
                                            if (isValid(v)) {
                                                dialogInterface.onClickDetails(etDlName.getText().toString(), etFathername.getText().toString(), etDlDob.getText().toString(), etDlNumber.getText().toString());
                                            }
                                        }

                                        private boolean isValid(View v) {
                                            if (etDlName.getText().toString().equalsIgnoreCase("")) {
                                                etDlName.setError("Provide name");
                                                return false;
                                            }
                                            if (etFathername.getText().toString().equalsIgnoreCase("")) {
                                                etFathername.setError("Provide father name");
                                                return false;
                                            }
                                            if (etDlDob.getText().toString().equalsIgnoreCase("")) {
                                                etDlDob.setError("Provide Date of Birth");
                                                return false;
                                            }
                                            if (etDlNumber.getText().toString().equalsIgnoreCase("")) {
                                                etDlNumber.setError("Provide Driving Licence Number");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    //driving Licence Detail

    public static void PanDetail(Context mContext, String name, String fathername, String pannumber, final DialogListner dialogInterface) {

        ImageView ivClose;
        CheckBox cbMain;
        Button btSubmit;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguepan_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.ivClose);
        cbMain = dialog.findViewById(R.id.cbAgreeTC);
        etPanName = dialog.findViewById(R.id.etPanName);
        etPanName.setText(name);
        etPanFatherName = dialog.findViewById(R.id.etFatherName);
        etPanNumber = dialog.findViewById(R.id.etPanNumber);
        ivClose = dialog.findViewById(R.id.ivClose);
        etPanFatherName.setText(fathername);
        etPanNumber.setText(pannumber);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitDlDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (validation(v)) {
                                                //dialog.dismiss();
                                                dialogInterface.onClickDetails(etPanName.getText().toString(), etPanFatherName.getText().toString(), "", etPanNumber.getText().toString());
                                            }
                                        }

                                        private boolean validation(View v) {
                                            if (etPanName.getText().toString().equalsIgnoreCase("")) {
                                                etPanName.setError("Provide name");
                                                etPanName.requestFocus();
                                                return false;
                                            }
                                            if (etPanFatherName.getText().toString().equalsIgnoreCase("")) {
                                                etPanFatherName.setError("Provide Father name");
                                                etPanFatherName.requestFocus();
                                                return false;
                                            }
                                            Matcher matcher = Constants.PAN_PATTERN.matcher(etPanNumber.getText().toString());
                                            if (TextUtils.isEmpty(etPanNumber.getText().toString()) || etPanNumber.getText().toString().length() < 5) {
                                                etPanNumber.setError("Provide Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            } else if (!matcher.matches()) {
                                                etPanNumber.setError("Provide Valid Pan Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            }
                                            if (!cbMain.isChecked()) {
                                                cbMain.setError("Please verify all details with physical evidence.");
                                                //showMSG(false, "Please verify all details with physical evidence.");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );


        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PanVerificationFragment.llGoback.setEnabled(true);
            }
        }, 2000);

    }


    public static void PanDetail2(Context mContext, String name, String fathername, String pannumber, boolean individual, final DialogListner dialogInterface) {

        ImageView ivClose;
        Button btSubmit;
        CheckBox cbMain;

        individua = individual;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguepan_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.ivClose);
        etPanName = dialog.findViewById(R.id.etPanName);
        llStoreDetails = dialog.findViewById(R.id.llStoreDetails);
        etStoreName = dialog.findViewById(R.id.etStoreName);
        cbMain = dialog.findViewById(R.id.cbAgreeTC);
        etPanName.setText(name);
        etPanFatherName = dialog.findViewById(R.id.etFatherName);
        etPanNumber = dialog.findViewById(R.id.etPanNumber);
        ivClose = dialog.findViewById(R.id.ivClose);
        etPanFatherName.setText(fathername);
        etPanNumber.setText(pannumber);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 3) {
                    char first = s.toString().charAt(3);
                    String word = String.valueOf(first);
                    if (word.equalsIgnoreCase("P")) {
                        individua = true;
                    } else {
                        individua = false;
                    }

                    if (individua) {
                        etStoreName.setVisibility(View.GONE);
                        llStoreDetails.setVisibility(View.GONE);
                    } else {
                        etStoreName.setVisibility(View.VISIBLE);
                        llStoreDetails.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (individua) {
            etStoreName.setVisibility(View.GONE);
            llStoreDetails.setVisibility(View.GONE);
        } else {
            etStoreName.setVisibility(View.VISIBLE);
            llStoreDetails.setVisibility(View.VISIBLE);
        }

        btSubmit = dialog.findViewById(R.id.btSubmitDlDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (validation(v)) {
                                                //dialog.dismiss();
                                                if (individua) {
                                                    dialogInterface.onClickDetails(etPanName.getText().toString(), etPanFatherName.getText().toString(), "", etPanNumber.getText().toString());

                                                } else {
                                                    dialogInterface.onClickDetails(etPanName.getText().toString(), etPanFatherName.getText().toString(), etStoreName.getText().toString(), etPanNumber.getText().toString());

                                                }
                                            }
                                        }

                                        private boolean validation(View v) {
                                            if (etPanName.getText().toString().equalsIgnoreCase("")) {
                                                etPanName.setError("Provide name");
                                                etPanName.requestFocus();
                                                return false;
                                            }
                                            if (etPanFatherName.getText().toString().equalsIgnoreCase("")) {
                                                etPanFatherName.setError("Provide Father name");
                                                etPanFatherName.requestFocus();
                                                return false;
                                            }
                                            Matcher matcher = Constants.PAN_PATTERN.matcher(etPanNumber.getText().toString());
                                            if (TextUtils.isEmpty(etPanNumber.getText().toString()) || etPanNumber.getText().toString().length() < 5) {
                                                etPanNumber.setError("Provide Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            } else if (!matcher.matches()) {
                                                etPanNumber.setError("Provide Valid Pan Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            }
                                            if (!cbMain.isChecked()) {
                                                cbMain.setError("Please verify all details with physical evidence.");
                                                //showMSG(false, "Please verify all details with physical evidence.");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );


        dialog.show();


    }


    public static void PanDetailGST(Context mContext, String name, String fathername, String pannumber, final DialogListner dialogInterface) {

        ImageView ivClose;
        Button btSubmit;
        CheckBox cbMain;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguepan_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.ivClose);
        etPanName = dialog.findViewById(R.id.etPanName);
        llStoreDetails = dialog.findViewById(R.id.llStoreDetails);
        etStoreName = dialog.findViewById(R.id.etStoreName);
        cbMain = dialog.findViewById(R.id.cbAgreeTC);
        etPanName.setText(name);
        etPanFatherName = dialog.findViewById(R.id.etFatherName);
        etPanNumber = dialog.findViewById(R.id.etPanNumber);
        ivClose = dialog.findViewById(R.id.ivClose);
        etPanFatherName.setText(fathername);
        etPanNumber.setText(pannumber);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitDlDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (validation(v)) {
                                                //dialog.dismiss();
                                                dialogInterface.onClickDetails(etPanName.getText().toString(), etPanFatherName.getText().toString(), etStoreName.getText().toString(), etPanNumber.getText().toString());


                                            }
                                        }

                                        private boolean validation(View v) {
                                            if (etPanName.getText().toString().equalsIgnoreCase("")) {
                                                etPanName.setError("Provide name");
                                                etPanName.requestFocus();
                                                return false;
                                            }
                                            if (etPanFatherName.getText().toString().equalsIgnoreCase("")) {
                                                etPanFatherName.setError("Provide Father name");
                                                etPanFatherName.requestFocus();
                                                return false;
                                            }
                                            Matcher matcher = Constants.PAN_PATTERN.matcher(etPanNumber.getText().toString());
                                            if (TextUtils.isEmpty(etPanNumber.getText().toString()) || etPanNumber.getText().toString().length() < 5) {
                                                etPanNumber.setError("Provide Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            } else if (!matcher.matches()) {
                                                etPanNumber.setError("Provide Valid Pan Number");
                                                etPanNumber.requestFocus();
                                                return false;
                                            }
                                            if (!cbMain.isChecked()) {
                                                cbMain.setError("Please verify all details with physical evidence.");
                                                //showMSG(false, "Please verify all details with physical evidence.");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );


        dialog.show();


    }


    public static void chequeDetail(Context mContext, String AccNumber, String payeename, String ifsccode, String processId, String bank_name, String branch_name, String branch_address, final DialogListner dialogInterface) {

        ImageView ivClose;
        CheckBox cbMain;
        Button btSubmit;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_cheque_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        etChequeNumber = dialog.findViewById(R.id.etAccountNum);
        etPayeeName = dialog.findViewById(R.id.etPayeeName);
        etIfscCode = dialog.findViewById(R.id.etIfscCode);
        etBankName = dialog.findViewById(R.id.etBankName);
        etBranchName = dialog.findViewById(R.id.etBranchName);
        etBankAddress = dialog.findViewById(R.id.etBankAddress);
        cbMain = dialog.findViewById(R.id.cbAgreeTC);
        ivClose = dialog.findViewById(R.id.ivClose);
        etChequeNumber.setText(AccNumber);
        etPayeeName.setText(payeename);
        etIfscCode.setText(ifsccode);
        etBankName.setText(bank_name);
        etBranchName.setText(branch_name);
        etBankAddress.setText(branch_address);
        etIfscCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 5) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        getBankDetails(mContext, s.toString(), processId);
                    } else {
                        Constants.ShowNoInternet(mContext);
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitChequeDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (validation(v)) {
                                                //dialog.dismiss();
                                                dialogInterface.onClickChequeDetails(etChequeNumber.getText().toString(), etPayeeName.getText().toString(), etIfscCode.getText().toString(), etBankName.getText().toString(), etBranchName.getText().toString(), etBankAddress.getText().toString());
                                            }
                                        }

                                        private boolean validation(View v) {
                                            if (etChequeNumber.getText().toString().equalsIgnoreCase("")) {
                                                etChequeNumber.setError("Provide Account Number");
                                                etChequeNumber.requestFocus();
                                                return false;
                                            }
                                            if (etPayeeName.getText().toString().equalsIgnoreCase("")) {
                                                etPayeeName.setError("Provide Payee name");
                                                etPayeeName.requestFocus();
                                                return false;
                                            }
                                            if (etIfscCode.getText().toString().equalsIgnoreCase("")) {
                                                etIfscCode.setError("Provide IFSC Code");
                                                etIfscCode.requestFocus();
                                                return false;
                                            }
                                            if (etBankName.getText().toString().equalsIgnoreCase("")) {
                                                etBankName.setError("Provide Bank Code");
                                                etBankName.requestFocus();
                                                return false;
                                            }
                                            if (etBankAddress.getText().toString().equalsIgnoreCase("")) {
                                                etBankAddress.setError("Provide Bank Adress");
                                                etBankAddress.requestFocus();
                                                return false;
                                            }
                                            if (etBranchName.getText().toString().equalsIgnoreCase("")) {
                                                etBranchName.setError("Provide Branch Name");
                                                etBranchName.requestFocus();
                                                return false;
                                            }
                                            if (!cbMain.isChecked()) {
                                                cbMain.setError("Please verify all details with physical evidence.");
                                                //showMSG(false, "Please verify all details with physical evidence.");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );


        dialog.show();
    }


    public static void addressDetails(Context mContext, String shopnumber, String streetname, String landmark, String pincode, String city, String state, final DialogListner dialogInterface) {

        ImageView ivClose;
        CheckBox cbMain;
        Button btSubmit;
        EditText etShopNo, etStreetName, etLandmark, etPincode;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_gst_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        etShopNo = dialog.findViewById(R.id.etShopHouseNo);
        etStreetName = dialog.findViewById(R.id.etStreetName);
        etLandmark = dialog.findViewById(R.id.etLandmark);
        etPincode = dialog.findViewById(R.id.etStorePincode);
        etCity = dialog.findViewById(R.id.etStoreCity);
        etState = dialog.findViewById(R.id.etStoreState);
        cbMain = dialog.findViewById(R.id.cbAgreeTC);
        ivClose = dialog.findViewById(R.id.ivClose);
        dc = dialog.findViewById(R.id.dc);
        etShopNo.setText(shopnumber);
        etStreetName.setText(streetname);
        etLandmark.setText(landmark);
        etPincode.setText(pincode);
        addDC(mContext, pincode);
        /*etCity.setText(city);
        etState.setText(state);
        */
        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                        addDC(mContext, s.toString());
                    } else {
                        Constants.ShowNoInternet(mContext);
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btSubmit = dialog.findViewById(R.id.btSubmitGSTDetail);
        btSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (validation(v)) {
                                                //dialog.dismiss();
                                                dialogInterface.onClickAddressDetails(etShopNo.getText().toString(), etStreetName.getText().toString(), etLandmark.getText().toString(), etPincode.getText().toString(), etCity.getText().toString(), etState.getText().toString(), "" + dc.getSelectedItem());
                                                dialog.dismiss();
                                            }
                                        }

                                        private boolean validation(View v) {
                                            if (etShopNo.getText().toString().equalsIgnoreCase("")) {
                                                etShopNo.setError("Provide Shop Number");
                                                etShopNo.requestFocus();
                                                return false;
                                            }
                                            if (etStreetName.getText().toString().equalsIgnoreCase("")) {
                                                etStreetName.setError("Provide Street name");
                                                etStreetName.requestFocus();
                                                return false;
                                            }
                                            if (etLandmark.getText().toString().equalsIgnoreCase("")) {
                                                etLandmark.setError("Provide Landmark");
                                                etLandmark.requestFocus();
                                                return false;
                                            }
                                            if (etPincode.getText().toString().equalsIgnoreCase("")) {
                                                etPincode.setError("Provide Pincode");
                                                etPincode.requestFocus();
                                                return false;
                                            }
                                            if (etCity.getText().toString().equalsIgnoreCase("")) {
                                                etCity.setError("Provide City");
                                                etCity.requestFocus();
                                                return false;
                                            }
                                            if (etState.getText().toString().equalsIgnoreCase("")) {
                                                etState.setError("Provide State Name");
                                                etState.requestFocus();
                                                return false;
                                            }
                                            if (!cbMain.isChecked()) {
                                                cbMain.setError("Please verify all details with physical evidence.");
                                                //showMSG(false, "Please verify all details with physical evidence.");
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
        );


        dialog.show();
    }

    private static void addDC(Context context, String pcode) {
        // state is DC and DC is state
        /*progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);*/
        sessionManager = new SessionManager(context);
        dcLists.clear();
        //progressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer " + sessionManager.getToken(), user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                // progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();

                    if (getUserDetails.getResponseCode() == 200) {

                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }
                            etCity.setText(getUserDetails.getData().get(i).getCity());
                            etState.setText(getUserDetails.getData().get(i).getState());
                        }


                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(context, android.R.layout.simple_spinner_item, dcLists);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dc.setAdapter(spinnerArrayAdapter);
                        dc.setSelection(0);

                    } else if (getUserDetails.getResponseCode() == 405) {
                        sessionManager.logoutUser(context);
                    } else {
                        Toast.makeText(context, getUserDetails.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "#errorcode :- 2032 " + context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
//                progressDialog.dismiss();
                Toast.makeText(context, "#errorcode :- 2032 " + context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //Toast.makeText(context, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean validation(String type) {
        return true;
    }


    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public static void errorMessage(Context mContext, String title, String message, final DialogInterface dialogInterface) {
        TextView tvMessage, tvTitle;
        Button btSubmit;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoguedl_failure);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        btSubmit = dialog.findViewById(R.id.btSubmitAdharDet);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static void getBankDetails(Context context, String ifsc, String str_process_id) {

        sessionManager = new SessionManager(context);
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

                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.has("responseCode")) {
                                if (jsonObject.getInt("responseCode") == 200) {
                                    JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                    etBankName.setText(result.getString("bank_name").toString());
                                    etBranchName.setText(result.getString("bank_branch_name").toString());
                                    etBankAddress.setText(result.getString("bank_address").toString());
                                } else if (jsonObject.getInt("responseCode") == 405) {
                                    sessionManager.logoutUser(context);
                                } else if (jsonObject.getInt("responseCode") == 411) {
                                    sessionManager.logoutUser(context);
                                } else {
                                }
                            }
                        } else {
                            Toast.makeText(context, "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "#errorcode 2053" + context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "#errorcode 2053" + context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }


    static class CustomAdapter<T> extends ArrayAdapter<T> {
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
}
