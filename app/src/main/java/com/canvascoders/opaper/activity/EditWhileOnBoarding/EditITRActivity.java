package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetParameterResponse;
import com.canvascoders.opaper.Beans.GetTrackingDetailResponse.GetTrackDetailsResponse;
import com.canvascoders.opaper.Beans.SendAgreementLinkResponse.GetAgreementLinkSend;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.Beans.getITrResponse.Datum;
import com.canvascoders.opaper.Beans.getITrResponse.GetITYears;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.activity.TaskDetailActivity;
import com.canvascoders.opaper.activity.TaskProccessDetailActivity;
import com.canvascoders.opaper.adapters.ITRListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.DocUploadFragment;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class EditITRActivity extends AppCompatActivity implements DialogListner, View.OnClickListener {
    RecyclerView rvItr;
    ImageView ivGst;
    private RequestPermissionHandler requestPermissionHandler;
    ITRListAdapter itrListAdapter;
    String dateofITR = "";
    ProgressDialog mprogressDialog;
    SessionManager sessionManager;
    ImageView ivBack;
    private String TAG = "InfoFragment";
    String str_process_id = "";
    String itrNumber = "";
    Integer position = 0;
    boolean isImage = false;
    List<Datum> dataList = new ArrayList<>();
    Button btSubmit, btSendLink;
    private static Dialog dialog;
    RelativeLayout llGeneralInfo, llSendMessageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itractivity);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermissionHandler = new RequestPermissionHandler();
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("Submitting GST details . Please wait ...");
        mprogressDialog.setCancelable(false);
        init();
    }

    private void init() {
        rvItr = findViewById(R.id.rvItr);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        llGeneralInfo = findViewById(R.id.llGeneralInfo);
        llSendMessageInfo = findViewById(R.id.llSendMessageInfo);
        btSendLink = findViewById(R.id.btSendLink);
        btSendLink.setOnClickListener(this);
        APiCallGetTrackDetails();
        getITRYears();


    }


    private void APiCallGetTrackDetails() {

        mprogressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).geTrackingDetails("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetTrackDetailsResponse>() {
            @Override
            public void onResponse(Call<GetTrackDetailsResponse> call, Response<GetTrackDetailsResponse> response) {
                mprogressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetTrackDetailsResponse getTrackDetailsResponse = response.body();
                    if (getTrackDetailsResponse.getResponseCode() == 200) {
                        //  Toast.makeText(TaskProccessDetailActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        //locatioon data

                        if (getTrackDetailsResponse.getData().get(0).getTrackDetail().getNoc() == 1) {
                            llGeneralInfo.setVisibility(View.GONE);
                            llSendMessageInfo.setVisibility(View.VISIBLE);
                        } else {
                            llGeneralInfo.setVisibility(View.VISIBLE);
                            llSendMessageInfo.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(EditITRActivity.this, getTrackDetailsResponse.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditITRActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTrackDetailsResponse> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(EditITRActivity.this, "#errorcode 2091 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getITRYears() {
        Mylogger.getInstance().Logit(TAG, "getUserInfo");
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mprogressDialog.setMessage("we are retrieving information, please wait!");
            mprogressDialog.show();

            Call<GetITYears> callUpload = ApiClient.getClient().create(ApiInterface.class).getITRYears("Bearer " + sessionManager.getToken(), params);
            callUpload.enqueue(new Callback<GetITYears>() {
                @Override
                public void onResponse(Call<GetITYears> call, Response<GetITYears> response) {
                    mprogressDialog.dismiss();
                    if (response.isSuccessful()) {
                        GetITYears getITYears = response.body();
                        if (getITYears.getResponseCode() == 200) {
                            if (getITYears.getData().size() > 0) {
                                dataList = getITYears.getData();
                                itrListAdapter = new ITRListAdapter(EditITRActivity.this, getITYears.getData(), EditITRActivity.this);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditITRActivity.this, LinearLayoutManager.VERTICAL, false);
                                rvItr.setLayoutManager(horizontalLayoutManager);
                                rvItr.setAdapter(itrListAdapter);
                            } else {
                                Toast.makeText(EditITRActivity.this, getITYears.getResponse(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditITRActivity.this, getITYears.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditITRActivity.this, "#errorcode 1456 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetITYears> call, Throwable t) {
                    mprogressDialog.dismiss();
                    Toast.makeText(EditITRActivity.this, "#errorcode 1456 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
            });


        } else {
            Constants.ShowNoInternet(EditITRActivity.this);
        }

    }


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
        itrNumber = accName;
        position = Integer.parseInt(ifsc);
        Intent chooseImageIntent = ImagePicker.getCameraIntent(EditITRActivity.this);
        startActivityForResult(chooseImageIntent, position);
    }

    @Override
    public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

    }

    public void ExtractITDetails() {

        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put("number_for_itr_filled", itrNumber);
        params.put("image_type", "itr");
        if (!TextUtils.isEmpty(dataList.get(position).getImage())) {

            mprogressDialog.setMessage("Extracting image...");
            mprogressDialog.setCancelable(false);
            mprogressDialog.show();

            File imagefile = new File(dataList.get(position).getImage());
            typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(dataList.get(position).getImage())), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));


            Call<GetParameterResponse> call = ApiClient.getClient2().create(ApiInterface.class).verifyITRNumber(params, typedFile);
            call.enqueue(new Callback<GetParameterResponse>() {
                @Override
                public void onResponse(Call<GetParameterResponse> call, retrofit2.Response<GetParameterResponse> response) {
                    mprogressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            // deleteImages();
                            GetParameterResponse getPanDetailsResponse = response.body();
                            if (getPanDetailsResponse.getStatus().equalsIgnoreCase("success")) {

                                Toast.makeText(EditITRActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                dataList.get(position).setItrNumber("");
                                itrListAdapter.notifyDataSetChanged();
                                Toast.makeText(EditITRActivity.this, getPanDetailsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditITRActivity.this, "#errorcode :- 2010 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetParameterResponse> call, Throwable t) {
                    mprogressDialog.dismiss();
                    Toast.makeText(EditITRActivity.this, "#errorcode :- 2010 NSDL error Contact administrator immediately", Toast.LENGTH_LONG).show();
                }
            });


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == position) {

                // Uri uri = ImagePicker.getPickImageResultUri(EditITRActivity.this, data);
               /* Uri uri = ImagePicker.getPickImageResultUri(EditITRActivity.this, data);

                String ImagePath = ImagePicker.getPathFromUri(EditITRActivity.this, uri);
                dataList.get(position).setSelectedImage(true);
                dataList.get(position).setImage(ImagePath);

                itrListAdapter.notifyDataSetChanged();*/

                Uri uri = ImagePicker.getPickImageResultUri(EditITRActivity.this, data);
                Intent intent = new Intent(EditITRActivity.this, CropImage2Activity.class);
                //Toast.makeText(EditITRActivity.this, imagecamera, Toast.LENGTH_SHORT).show();
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, position + 1234);
            }

            if (requestCode == position + 1234) {
                Uri imgURI = Uri.parse(data.getStringExtra("uri"));
                String aadharImagepathFront = RealPathUtil.getPath(EditITRActivity.this, imgURI);
                try {
                    dataList.get(position).setSelectedImage(true);
                    dataList.get(position).setImage(aadharImagepathFront);
                    itrListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ExtractITDetails();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                bizDetailsSubmit(v);
                break;
            case R.id.btSendLink:
                APiCallSendGenerateLink();
                break;
        }
    }

    private void APiCallSendGenerateLink() {
        mprogressDialog.show();

        Map<String, String> params = new HashMap<>();

        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);

        ApiClient.getClient().create(ApiInterface.class).sendLInk("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetAgreementLinkSend>() {
            @Override
            public void onResponse(Call<GetAgreementLinkSend> call, Response<GetAgreementLinkSend> response) {
                mprogressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetAgreementLinkSend getAgreementLinkSend = response.body();
                    if (getAgreementLinkSend.getResponseCode() == 200) {
                        Toast.makeText(EditITRActivity.this, getAgreementLinkSend.getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditITRActivity.this, getAgreementLinkSend.getResponse(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EditITRActivity.this, "#errorcode 2148 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetAgreementLinkSend> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(EditITRActivity.this, "#errorcode 2148 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });

    }


    // submit Details Last Api
    public void bizDetailsSubmit(final View v) {

        Call<GetUserDetailResponse> call;
        MultipartBody.Part typedFile = null;

        mprogressDialog.setMessage("Submitting kirana details . Please wait ...");
        mprogressDialog.show();
        HashMap<String, String> user = new HashMap<>();

        user.put(Constants.PARAM_PROCESS_ID, str_process_id);
        user.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.put(Constants.PARAM_TOKEN, sessionManager.getToken());


        Log.e("User Date", "Edit info" + user);
        Log.e("User Date", "Edit info" + str_process_id + "   " + sessionManager.getAgentID());

        MultipartBody.Part finalImages[] = new MultipartBody.Part[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelectedImage()) {
                isImage = true;
                user.put("financial_year[" + i + "]", "" + dataList.get(i).getFinancialYear());
                user.put("assessment_year[" + i + "]", "" + dataList.get(i).getAssessmentYear());
                user.put("itr_number[" + i + "]", "" + dataList.get(i).getItrNumber());
                user.put("itr_filling_date[" + i + "]", "" + dataList.get(i).getDateofITR());
                File imagefile1 = new File(dataList.get(i).getImage());
                finalImages[i] = MultipartBody.Part.createFormData("itr_doc[]", imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(dataList.get(i).getImage())), imagefile1));
            } else {
                // finalImages[i] = null;
            }
        }


        if (isImage) {
            call = ApiClient.getClient().create(ApiInterface.class).editsubmitBizDetailsITR("Bearer " + sessionManager.getToken(), user, finalImages);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).editsubmitBizDetails("Bearer " + sessionManager.getToken(), user);

        }

        call.enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mprogressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {
                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());

                        showAlert(getUserDetailResponse.getResponse());
                        // commanFragmentCallWithoutBackStack(new DocUploadFragment());
                    }
                    if (getUserDetailResponse.getResponseCode() == 411) {
                        sessionManager.logoutUser(EditITRActivity.this);
                    }

                    if (getUserDetailResponse.getResponseCode() == 400) {

                        mprogressDialog.dismiss();
                        if (getUserDetailResponse.getValidation() != null) {
                            Validation validation = getUserDetailResponse.getValidation();


                            if (validation.getAssessment_year() != null && validation.getAssessment_year().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getAssessment_year(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }

                            if (validation.getFinancial_year() != null && validation.getFinancial_year().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getFinancial_year(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }

                            if (validation.getItr_number() != null && validation.getItr_number().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getItr_number(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }

                            if (validation.getItr_doc() != null && validation.getItr_doc().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getItr_doc(), Toast.LENGTH_LONG).show();
                                // return false;
                                // return false;
                            }

                            if (validation.getRoute() != null && validation.getRoute().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                //    etRoute.setError(validation.getRoute());
                                //   etRoute.requestFocus();

                            }
                            if (validation.getGstCertificateImage() != null && validation.getGstCertificateImage().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getGstCertificateImage(), Toast.LENGTH_LONG).show();


                            }
                            if (validation.getGstn() != null && validation.getGstn().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getGstn(), Toast.LENGTH_LONG).show();
                                // edit_gstn.setError(validation.getGstn());
                                //   edit_gstn.requestFocus();
                            }

                            if (validation.getStoreAddress() != null && validation.getStoreAddress().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                            /*    edit_storeaddress.setError(validation.getStoreAddress());
                                edit_storeaddress.requestFocus();*/

                            }
                            if (validation.getStoreAddress1() != null && validation.getStoreAddress1().length() > 0) {
                              /*  //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etStreet.setError(validation.getStoreAddress1());
                                etStreet.requestFocus();
*/
                                Toast.makeText(EditITRActivity.this, validation.getStoreAddress1(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreAddressLandmark() != null && validation.getStoreAddressLandmark().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etLandmark.setError(validation.getStoreAddressLandmark());
                                etLandmark.requestFocus();*/
                            }
                            if (validation.getPincode() != null && validation.getPincode().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
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
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialAddress.setError(validation.getResidentialAddress());
                                etResidentialAddress.requestFocus();*/
                            }
                            if (validation.getResidentialAddress1() != null && validation.getResidentialAddress1().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                               /* etResidentialStreet.setError(validation.getResidentialAddress1());
                                etResidentialStreet.requestFocus();*/
                            }
                            if (validation.getResidentialAddressLandmark() != null && validation.getResidentialAddressLandmark().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialLandmark.setError(validation.getResidentialAddressLandmark());
                                etResidentialLandmark.requestFocus();*/
                            }
                            if (validation.getResidentialAddresspicode() != null && validation.getResidentialAddresspicode().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                              /*  etResidentialPincode.setError(validation.getResidentialAddresspicode());
                                etResidentialPincode.requestFocus();*/
                            }
                            if (validation.getResidentialAddressCity() != null && validation.getResidentialAddressCity().length() > 0) {
                               /* //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialCity.setError(validation.getResidentialAddressCity());
                                etResidentialCity.requestFocus();*/
                            }
                            if (validation.getResidentialAddressState() != null && validation.getResidentialAddressState().length() > 0) {
                            /*    //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etResidentialState.setError(validation.getResidentialAddressState());
                                etResidentialState.requestFocus();*/
                            }

                            if (validation.getPermanentAddress() != null && validation.getPermanentAddress().length() > 0) {
                             /*   //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermententAdd.setError(validation.getPermanentAddress());
                                etPermententAdd.requestFocus();*/
                            }

                            if (validation.getPermanentAddress1() != null && validation.getPermanentAddress1().length() > 0) {
                              /*  //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentStreet.setError(validation.getPermanentAddress1());
                                etPermanentStreet.requestFocus();*/
                            }
                            if (validation.getPermanentAddressLandmark() != null && validation.getPermanentAddressLandmark().length() > 0) {
                             /*   //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentLandmark.setError(validation.getPermanentAddressLandmark());
                                etPermanentLandmark.requestFocus();*/
                            }
                            if (validation.getPermanentAddressPicode() != null && validation.getPermanentAddressPicode().length() > 0) {
                             /*   //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentPincode.setError(validation.getPermanentAddressPicode());
                                etPermanentPincode.requestFocus();*/
                            }
                            if (validation.getPermanentAddressCity() != null && validation.getPermanentAddressCity().length() > 0) {
                               /* //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentCity.setError(validation.getPermanentAddressCity());
                                etPermanentCity.requestFocus();*/
                            }
                            if (validation.getPermanentAddressState() != null && validation.getPermanentAddressState().length() > 0) {
                              /*  //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                etPermanentState.setError(validation.getPermanentAddressState());
                                etPermanentState.requestFocus();*/
                            }
                            if (validation.getVendorType() != null && validation.getVendorType().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getVendorType(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLocality() != null && validation.getLocality().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getLocality(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getApproach() != null && validation.getApproach().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getApproach(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getLanguages() != null && validation.getLanguages().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getLanguages(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getShipmentTransfer() != null && validation.getShipmentTransfer().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getShipmentTransfer(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getPartnerWithOtherEcommerce() != null && validation.getPartnerWithOtherEcommerce().length() > 0) {
                                //Toast.makeText(EditITRActivity.this,validation.getPanCardFront(),Toast.LENGTH_LONG).show();
                                Toast.makeText(EditITRActivity.this, validation.getPartnerWithOtherEcommerce(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getStoreName() != null && validation.getStoreName().length() > 0) {
                               /* edit_storename.setError(validation.getStoreName());
                                edit_storename.requestFocus();*/
                                // return false;
                            }
                            if (validation.getIfGst() != null && validation.getIfGst().length() > 0) {
                                //  edit_gstn.setError(validation.getIfGst());
                                //    edit_gstn.requestFocus();
                                Toast.makeText(EditITRActivity.this, validation.getIfGst(), Toast.LENGTH_SHORT).show();
                            }
                            if (validation.getDc() != null && validation.getDc().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getDc(), Toast.LENGTH_LONG).show();
                            }


                            if (validation.getEmail() != null && validation.getEmail().length() > 0) {
                              /*  edit_email.setError(validation.getEmail());
                                edit_email.requestFocus();
                                // return false;*/
                            }

                            if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getProccessId(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getStoreTypeConfig() != null && validation.getStoreTypeConfig().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getStoreTypeConfig(), Toast.LENGTH_LONG).show();

                                // return false;
                            }
                            if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                Toast.makeText(EditITRActivity.this, validation.getAgentId(), Toast.LENGTH_LONG).show();

                                // return false;
                            }/* else {
                                Toast.makeText(EditITRActivity.this, getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                                Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                            }*/

                        } else {
                            Toast.makeText(EditITRActivity.this, getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();

                            Constants.showAlert(v, getUserDetailResponse.getResponse(), false);
                        }

                    } else {
                        Toast.makeText(EditITRActivity.this, getUserDetailResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        Constants.showAlert(v, getUserDetailResponse.getResponse(), true);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(EditITRActivity.this);
                        }
                    }
                } else {
                    Constants.showAlert(v, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(EditITRActivity.this, "#errorcode :- 2029 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                //   Toast.makeText(EditITRActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlert(String msg) {
        Button btSubmit;
        TextView tvMessage, tvTitle;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(EditITRActivity.this);
        dialog = new Dialog(EditITRActivity.this, R.style.DialogLSideBelow);
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
                //    commanFragmentCallWithoutBackStack(new DocUploadFragment());
                finish();
            }
        });

        dialog.setCancelable(false);

        dialog.show();

    }


}