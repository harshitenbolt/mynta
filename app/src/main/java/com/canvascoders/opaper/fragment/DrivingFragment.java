package com.canvascoders.opaper.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrivingFragment extends Fragment implements View.OnClickListener {


    View view;
    String Done = "";
    static String TAG = "DRIVIGNVerification";
    ProgressDialog mProgressDialog;
    public String name = "", fathername = "", dob = "", dlnumber = "";
    String VoteridDetailId, filename, fileUrl, backsideFileUrl, backsidefilename, dlIdDetailId;
    private TextView tvAdharFront, tvAdharBack, tvVoterFront, tvVoterBack, tvDlFront, tvDlBack;
    private ImageView ivAdharFront, ivAdharback, ivvoterFront, ivvoterFrontSelected, ivVoterBack, ivVoterBackSelected, ivDlBack, ivDlBackSelected, ivDlfront, ivDlfrontSelected;

    private ImageView ivAdharFrontSelected, ivAdharABackSelected, ivVoterFrontSelected, ivDlFrontSelected, ivAdharIamgeFront, ivAdharImageBack, ivVoterImageFront, ivVoterImageBack, ivDlImageFront, ivDlImageBack;
    private RequestPermissionHandler requestPermissionHandler;
    private static int IMAGE_SELCTION_CODE = 0;
    private static final int IMAGE_DRIVING_FRONT = 1021, IMAGE_DRIVING_BACK = 1022, CROPPED_IMAGE_DL_FRONT = 1023, CROPPED_IMAGE_DL_BACK = 1024;

    private String imagecamera = "", aadharImagepathFront = "", voterImagePathFront = "", voterImagePathBack = "", dlImagePathBack = "", dlImageOathFront = "";

    private Uri imgURI;
    Context context;

    public DrivingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_driving, container, false);
            context = this.getContext();
            initView();
            Log.e(TAG, "onCreateView");
        } else {
            onAttach(getContext());
        }

        return view;
    }

    private void initView() {

        tvDlFront = view.findViewById(R.id.tvDLFront);
        tvDlBack = view.findViewById(R.id.tvDlBackside);
        ivDlBackSelected = view.findViewById(R.id.ivCheckDlBack);
        ivDlBackSelected.setOnClickListener(this);
        ivDlFrontSelected = view.findViewById(R.id.ivCheckDlFront);
        ivDlFrontSelected.setOnClickListener(this);
        ivDlImageFront = view.findViewById(R.id.ivDlImageFront);
        ivDlImageBack = view.findViewById(R.id.ivDlImageBack);

        tvDlBack.setOnClickListener(this);
        tvDlFront.setOnClickListener(this);
        requestPermissionHandler = new RequestPermissionHandler();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void ApiCallGetDetailLicence(String drivingLicencePath) {
        // MultipartBody.Part voter_front_part = null;
        MultipartBody.Part driving_licence_part = null;

        Mylogger.getInstance().Logit(TAG, "getUserInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);


        File imagefile1 = new File(drivingLicencePath);
        driving_licence_part = MultipartBody.Part.createFormData(Constants.PARAM_IMAGE, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(drivingLicencePath)), imagefile1));

        Mylogger.getInstance().Logit(TAG, "getocUserInfo");
        mProgressDialog.setMessage("Fetching details. Please wait......");
        mProgressDialog.show();
        // hideKeyboardwithoutPopulateFragment();
        Call<DrivingLicenceDetailResponse> call = ApiClient.getClient2().create(ApiInterface.class).getDrivingLicenceDetail(params, driving_licence_part);
        call.enqueue(new Callback<DrivingLicenceDetailResponse>() {
            @Override
            public void onResponse(Call<DrivingLicenceDetailResponse> call, Response<DrivingLicenceDetailResponse> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        DrivingLicenceDetailResponse voterOCRGetDetaisResponse = response.body();
                        if (voterOCRGetDetaisResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            name = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getName();
                            fathername = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFatherName();
                            dob = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getBirthDate();
                            dlnumber = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceNumber();
                            dlIdDetailId = String.valueOf(voterOCRGetDetaisResponse.getDrivingLicenceDetail().getDrivingLicenceDetailId());
                            filename = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileName();
                            fileUrl = voterOCRGetDetaisResponse.getDrivingLicenceDetail().getFileUrl();

                            DialogUtil.DrivingDetail(getActivity(), name, fathername, dob, dlnumber, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                        // getBankDetails(mContext,s.toString(),processId);
                                        /*ApiCallSubmitOcr(name, fathername, dob, id, dlIdDetailId, filename, fileUrl);
                                        ApiCallSubmitKYC(name, fathername, dob, id);*/
                                    } else {
                                        Constants.ShowNoInternet(getActivity());
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
                            Toast.makeText(getActivity(), voterOCRGetDetaisResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            DialogUtil.DrivingDetail(getActivity(), name, fathername, dob, dlnumber, new DialogListner() {
                                @Override
                                public void onClickPositive() {

                                }

                                @Override
                                public void onClickNegative() {

                                }

                                @Override
                                public void onClickDetails(String name, String fathername, String dob, String id) {

                                    //ApiCallSubmitOcr(name,fathername,dob,id,dlIdDetailId,filename,fileUrl);

                                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                        //  ApiCallSubmitKYC(name, fathername, dob, id);
                                    } else {
                                        Constants.ShowNoInternet(getActivity());
                                    }


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

                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DrivingLicenceDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDLFront:
                capture_aadhar_front_and_back_image(1);
                break;
            case R.id.tvDlBackside:
                capture_aadhar_front_and_back_image(2);
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (Done.equalsIgnoreCase("")) {
            Log.e("123", Done);
        } else {
            Log.e("123", Done);
        }
    }


    private void capture_aadhar_front_and_back_image(int imageSide) {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (imageSide == 1) {
                    IMAGE_SELCTION_CODE = IMAGE_DRIVING_FRONT;
                    Intent intent1 = ImagePicker.getCameraIntent(context);
                    getParentFragment().startActivityForResult(intent1, IMAGE_DRIVING_FRONT);
                    //DrivingFragment.this.startActivityForResult(intent1, IMAGE_DRIVING_FRONT);

                } else if (imageSide == 2) {
                    IMAGE_SELCTION_CODE = IMAGE_DRIVING_BACK;
                    Intent intent2 = ImagePicker.getCameraIntent(context);
                    // DrivingFragment.this.startActivityForResult(intent1, IMAGE_DRIVING_BACK);
                    getParentFragment().startActivityForResult(intent2, IMAGE_DRIVING_BACK);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        Done = dlImageOathFront;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("KYC", "DRIVITNG");
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_DRIVING_FRONT) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
                imagecamera = ImagePicker.getBitmapPath(bitmap, getContext());
                Intent intent = new Intent(context, CropImage2Activity.class);
                intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(imagecamera)).toString());
                getParentFragment().startActivityForResult(intent, CROPPED_IMAGE_DL_FRONT);
                onDetach();
            }

            if (requestCode == CROPPED_IMAGE_DL_FRONT) {
                imgURI = Uri.parse(data.getStringExtra("uri"));
                dlImageOathFront = RealPathUtil.getPath(getContext(), imgURI);
                try {
                    Glide.with(context).load(dlImageOathFront).into(ivDlImageFront);
                    ivDlFrontSelected.setVisibility(View.VISIBLE);
                    tvDlFront.setVisibility(View.GONE);
                    File casted_image = new File(imagecamera);
                    if (casted_image.exists()) {
                        casted_image.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (requestCode == IMAGE_DRIVING_BACK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
                dlImagePathBack = ImagePicker.getBitmapPath(bitmap, getContext());
                Glide.with(getContext()).load(dlImagePathBack).into(ivDlImageBack);
                ivDlBackSelected.setVisibility(View.VISIBLE);
                tvDlBack.setVisibility(View.GONE);


                //setButtonImage();
            }
        }


    }


}
