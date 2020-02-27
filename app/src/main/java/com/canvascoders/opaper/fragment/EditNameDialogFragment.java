package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aadhaarapi.sdk.gateway_lib.qtActivity.AadhaarAPIActivity;
import com.canvascoders.opaper.Beans.TransactionIDResponse.TransactionIdResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.EditWhileOnBoarding.EditKycActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.ESIGN_ERROR;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.ESIGN_SUCCESS;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.QT_EMAIL;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.QT_REQUEST_TYPE;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.QT_RESULT;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.QT_SERVICE_TYPE;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.QT_TRANSACTION_ID;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtConstantUtils.REQUEST_AADHAARAPI;
import static com.aadhaarapi.sdk.gateway_lib.qtUtils.QtRequestType.ESIGN;

/**
 * Created by piyush on 07/02/2018.
 */

public class EditNameDialogFragment extends DialogFragment {

    public EditText edit_aname;
    public EditText edit_anumber;
    public EditText edit_ayear;
    public EditText edit_apincode;
    ImageView btn_close_dialog;
    private Button btnNext;
    private AadharVerificationFragment aadharVerificationFragment;

    private EditKycActivity editKycActivity;
    String str_name, str_uid, str_year, str_pincode;
    GPSTracker gps;
    String proccessId = "";
    ProgressDialog progressDialog;
    private String lattitude = "", longitude = "";
    SessionManager sessionManager;
    boolean fromedit = false;
    int withoutimagee = 0;

    public EditNameDialogFragment() {

    }

    public static EditNameDialogFragment newInstance(AadharVerificationFragment aadharVerificationFragment, String ProcessId, boolean fromEdit) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        frag.aadharVerificationFragment = aadharVerificationFragment;
        frag.proccessId = ProcessId;
        frag.fromedit = fromEdit;
        return frag;
    }

    public static EditNameDialogFragment newInstance2(EditKycActivity aadharVerificationFragment, String ProcessId, boolean fromEdit, int withoutimage) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        frag.editKycActivity = aadharVerificationFragment;
        frag.proccessId = ProcessId;
        frag.fromedit = fromEdit;
        frag.withoutimagee = withoutimage;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_name, container);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Get field from view
        btnNext = (Button) view.findViewById(R.id.btn_next);
        edit_aname = (EditText) view.findViewById(R.id.edit_aname);
        edit_anumber = (EditText) view.findViewById(R.id.edit_anumber);
        edit_ayear = (EditText) view.findViewById(R.id.edit_ayear);
        edit_apincode = (EditText) view.findViewById(R.id.edit_apincode);
        btn_close_dialog = (ImageView) view.findViewById(R.id.btn_close_dialog);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        sessionManager = new SessionManager(getActivity());


        readBundle();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valication()) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {




                        /*AadharVerificationFragment.name = edit_aname.getText().toString().trim();
                        AadharVerificationFragment.udi = edit_anumber.getText().toString().trim();
                        AadharVerificationFragment.year = edit_ayear.getText().toString().trim();
                        AadharVerificationFragment.pincode = edit_apincode.getText().toString().trim();*/

                        str_name = edit_aname.getText().toString().trim();
                        str_uid = edit_anumber.getText().toString().trim();
                        str_year = edit_ayear.getText().toString().trim();
                        str_pincode = edit_apincode.getText().toString().trim();

                        sessionManager.saveData(Constants.KEY_AADHAR_NAME, str_name);
                        sessionManager.saveData(Constants.KEY_UID, str_uid);
                        sessionManager.saveData(Constants.KEY_YEAR, str_year);
                        sessionManager.saveData(Constants.KEY_PINCODE, str_pincode);

                        if (Constants.showAdhar.equalsIgnoreCase("1")) {
                            apiCallGetTrasactionId();
                        } else {

                            if (fromedit) {
                                if (withoutimagee == 0) {
                                    editKycActivity.storeAadhar();
                                } else {
                                    editKycActivity.storeAadharwithoutImage();
                                }
                            } else {
                                aadharVerificationFragment.storeAadhar();
                            }
                            // dismiss();
                        }

                        //
                        //

                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }
                }
            }


        });
        btn_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.saveData(Constants.KEY_AADHAR_NAME, "");
                sessionManager.saveData(Constants.KEY_UID, "");
                sessionManager.saveData(Constants.KEY_PINCODE, "");
                sessionManager.saveData(Constants.KEY_YEAR, "");
                dismiss();
            }
        });
    }


    private void apiCallGetTrasactionId() {
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data", "JVBERi0xLjYNJeLjz9MNCjI0IDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9GaXJzdCA0L0xlbmd0aCAyMTYvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjePI9RS8MwFIX/yn1bi9jepCQ6GYNpFBTEMsW97CVLbjWYNpImmz/fVsXXcw/f/c4SEFarepPTe4iFok8dU09DgtDBQx6TMwT74vaLTE7uSPDUdXM0Xe/73r1FnVwYYEtHR6d9WdY3kX4ipRMV6oojSmxQMoGyac5RLBAXf63p38aGA7XPorLewyvFcYaJile8rB+D/YcwiRdMMGScszO8/IW0MdhsaKKYGA46gXKTr/cUQVY4We/cYMNpnLVeXPJUXHs9fECr7kAFk+eZ5Xr9LcAAfKpQrA0KZW5kc3RyZWFtDWVuZG9iag0yNSAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNC9MZW5ndGggNDkvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjeslAwULCx0XfOL80rUTDU985MKY42NAIKBsXqh1QWpOoHJKanFtvZAQQYAN/6C60NCmVuZHN0cmVhbQ1lbmRvYmoNMjYgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0ZpcnN0IDkvTGVuZ3RoIDQyL04gMi9UeXBlL09ialN0bT4+c3RyZWFtDQpo3jJTMFAwVzC0ULCx0fcrzS2OBnENFIJi7eyAIsH6LnZ2AAEGAI2FCDcNCmVuZHN0cmVhbQ1lbmRvYmoNMjcgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0ZpcnN0IDUvTGVuZ3RoIDEyMC9OIDEvVHlwZS9PYmpTdG0+PnN0cmVhbQ0KaN4yNFIwULCx0XfOzytJzSspVjAyBgoE6TsX5Rc45VdEGwB5ZoZGCuaWRrH6vqkpmYkYogGJRUCdChZgfUGpxfmlRcmpxUAzA4ryk4NTS6L1A1zc9ENSK0pi7ez0g/JLEktSFQz0QyoLUoF601Pt7AACDADYoCeWDQplbmRzdHJlYW0NZW5kb2JqDTIgMCBvYmoNPDwvTGVuZ3RoIDM1MjUvU3VidHlwZS9YTUwvVHlwZS9NZXRhZGF0YT4+c3RyZWFtDQo8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjQtYzAwNSA3OC4xNDczMjYsIDIwMTIvMDgvMjMtMTM6MDM6MDMgICAgICAgICI+CiAgIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOnBkZj0iaHR0cDovL25zLmFkb2JlLmNvbS9wZGYvMS4zLyIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIgogICAgICAgICAgICB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIKICAgICAgICAgICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIj4KICAgICAgICAgPHBkZjpQcm9kdWNlcj5BY3JvYmF0IERpc3RpbGxlciA2LjAgKFdpbmRvd3MpPC9wZGY6UHJvZHVjZXI+CiAgICAgICAgIDx4bXA6Q3JlYXRlRGF0ZT4yMDA2LTAzLTA2VDE1OjA2OjMzLTA1OjAwPC94bXA6Q3JlYXRlRGF0ZT4KICAgICAgICAgPHhtcDpDcmVhdG9yVG9vbD5BZG9iZVBTNS5kbGwgVmVyc2lvbiA1LjIuMjwveG1wOkNyZWF0b3JUb29sPgogICAgICAgICA8eG1wOk1vZGlmeURhdGU+MjAxNi0wNy0xNVQxMDoxMjoyMSswODowMDwveG1wOk1vZGlmeURhdGU+CiAgICAgICAgIDx4bXA6TWV0YWRhdGFEYXRlPjIwMTYtMDctMTVUMTA6MTI6MjErMDg6MDA8L3htcDpNZXRhZGF0YURhdGU+CiAgICAgICAgIDx4bXBNTTpEb2N1bWVudElEPnV1aWQ6ZmYzZGNmZDEtMjNmYS00NzZmLTgzOWEtM2U1Y2FlMmRhMmViPC94bXBNTTpEb2N1bWVudElEPgogICAgICAgICA8eG1wTU06SW5zdGFuY2VJRD51dWlkOjM1OTM1MGIzLWFmNDAtNGQ4YS05ZDZjLTAzMTg2YjRmZmIzNjwveG1wTU06SW5zdGFuY2VJRD4KICAgICAgICAgPGRjOmZvcm1hdD5hcHBsaWNhdGlvbi9wZGY8L2RjOmZvcm1hdD4KICAgICAgICAgPGRjOnRpdGxlPgogICAgICAgICAgICA8cmRmOkFsdD4KICAgICAgICAgICAgICAgPHJkZjpsaSB4bWw6bGFuZz0ieC1kZWZhdWx0Ij5CbGFuayBQREYgRG9jdW1lbnQ8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6QWx0PgogICAgICAgICA8L2RjOnRpdGxlPgogICAgICAgICA8ZGM6Y3JlYXRvcj4KICAgICAgICAgICAgPHJkZjpTZXE+CiAgICAgICAgICAgICAgIDxyZGY6bGk+RGVwYXJ0bWVudCBvZiBKdXN0aWNlIChFeGVjdXRpdmUgT2ZmaWNlIG9mIEltbWlncmF0aW9uIFJldmlldyk8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L2RjOmNyZWF0b3I+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz4NCmVuZHN0cmVhbQ1lbmRvYmoNMTEgMCBvYmoNPDwvTWV0YWRhdGEgMiAwIFIvUGFnZUxhYmVscyA2IDAgUi9QYWdlcyA4IDAgUi9UeXBlL0NhdGFsb2c+Pg1lbmRvYmoNMjMgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAxMD4+c3RyZWFtDQpIiQIIMAAAAAABDQplbmRzdHJlYW0NZW5kb2JqDTI4IDAgb2JqDTw8L0RlY29kZVBhcm1zPDwvQ29sdW1ucyA0L1ByZWRpY3RvciAxMj4+L0ZpbHRlci9GbGF0ZURlY29kZS9JRFs8REI3Nzc1Q0NFMjI3RjZCMzBDNDQwREY0MjIxREMzOTA+PEJGQ0NDRjNGNTdGNjEzNEFCRDNDMDRBOUU0Q0ExMDZFPl0vSW5mbyA5IDAgUi9MZW5ndGggODAvUm9vdCAxMSAwIFIvU2l6ZSAyOS9UeXBlL1hSZWYvV1sxIDIgMV0+PnN0cmVhbQ0KaN5iYgACJjDByGzIwPT/73koF0wwMUiBWYxA4v9/EMHA9I/hBVCxoDOQeH8DxH2KrIMIglFwIpD1vh5IMJqBxPpArHYgwd/KABBgAP8bEC0NCmVuZHN0cmVhbQ1lbmRvYmoNc3RhcnR4cmVmDQo0NTc2DQolJUVPRg0K");
            jsonObject.put("type", "pdf");
            jsonObject.put("info", "This document is related to ths");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jsonObject", jsonObject.toString());
        param.put("document", jsonObject.toString());*/
        //  param.put("purpose", "enbolt");
        //  param.put("signerCity", "ahmedabad");
        param.put(Constants.PARAM_PROCESS_ID, proccessId);
        param.put("aadhar_no", edit_anumber.getText().toString());
        param.put("aadhar_name", edit_aname.getText().toString());
        ApiClient.getClient().create(ApiInterface.class).getTransacId("Bearer " + sessionManager.getToken(), param).enqueue(new Callback<TransactionIdResponse>() {
            @Override
            public void onResponse(Call<TransactionIdResponse> call, Response<TransactionIdResponse> response) {
                if (response.isSuccessful()) {

                    progressDialog.dismiss();

                    try {


                        TransactionIdResponse transcationIdResponse = response.body();
                        if (transcationIdResponse.getResponseCode() == 200) {
                            // AadharVerificationFragment.adharExtractMehots(transcationIdResponse.getData().get(0).getId());
                            Intent gatewayIntent = new Intent(getActivity(), AadhaarAPIActivity.class);
                            gatewayIntent.putExtra(QT_TRANSACTION_ID, transcationIdResponse.getData().get(0).getId());
                            gatewayIntent.putExtra(QT_EMAIL, ""); //Not Mandatory, can be added to pre-fill the Login Box
                            gatewayIntent.putExtra(QT_REQUEST_TYPE, ESIGN.getRequest());
                            startActivityForResult(gatewayIntent, REQUEST_AADHAARAPI);
                        } else {
                            Toast.makeText(getActivity(), transcationIdResponse.getData().get(0).getId(), Toast.LENGTH_LONG).show();


                        }


                    } catch (Exception e) {
                        Log.e("DoneDOnaNon", "DoneDOnaNon2");
                        Toast.makeText(getActivity(), "#errorcode 2077 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                } else {
                    Log.e("DoneDOnaNon", response.message());
                    Toast.makeText(getActivity(), "#errorcode 2077 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TransactionIdResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "#errorcode 2077 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


    }

    private void readBundle() {

        str_name = sessionManager.getData(Constants.KEY_AADHAR_NAME);
        str_uid = sessionManager.getData(Constants.KEY_UID);
        str_year = sessionManager.getData(Constants.KEY_YEAR);
        str_pincode = sessionManager.getData(Constants.KEY_PINCODE);

        edit_aname.setText(str_name);
        edit_anumber.setText(str_uid);
        edit_ayear.setText(str_year);
        edit_apincode.setText(str_pincode);
    }

    private boolean valication() {
        if (TextUtils.isEmpty(edit_aname.getText().toString())) {
            edit_aname.requestFocus();
            edit_aname.setError("Please enter Aadhar name");

            return false;
        }
        if (TextUtils.isEmpty(edit_anumber.getText().toString())) {
            edit_anumber.requestFocus();
            edit_anumber.setError("Please enter Aadhar number");

            return false;
        }
        if (TextUtils.isEmpty(edit_ayear.getText().toString())) {
            edit_ayear.requestFocus();
            edit_ayear.setError("Please enter year of birth");

            return false;
        }
        if (TextUtils.isEmpty(edit_apincode.getText().toString())) {
            edit_apincode.requestFocus();
            edit_apincode.setError("Please enter pincode");

            return false;
        }
        if (edit_anumber.getText().length() < 12) {
            edit_anumber.requestFocus();
            edit_anumber.setError("Please enter valid Aadhar number");

            return false;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AADHAARAPI && null != data) {
            String requestType = data.getStringExtra(QT_REQUEST_TYPE);
            String serviceType = data.getStringExtra(QT_SERVICE_TYPE);
            if (requestType.equalsIgnoreCase(ESIGN.getRequest())) {
//To handle the Success JSON response from SDK
                if (resultCode == ESIGN_SUCCESS) {

                    aadharVerificationFragment.storeAadhar();
                    //dismiss();

                    String responseString = data.getStringExtra(QT_RESULT); //handle
                }
// To handle the Error JSON response from SDK
                if (resultCode == ESIGN_ERROR) {
                    String errorString = data.getStringExtra(QT_RESULT); //handle

                    Log.i("SDK test error ", serviceType + " error: " + errorString); // can be removed
                }
            }
        }
    }
}