package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.ImageUtils;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.adapters.MyAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class DocUploadFragment extends Fragment implements View.OnClickListener {

    private String TAG = "DocUpload";
    private Button btn_next;
    private static final int IMAGE_SHPO_ACT = 103;
    private static final int IMAGE_SHOP_IMG = 105, IMAGE_OWNER_IMG = 106;
    private static int IMAGE_SELCTED_IMG = 0;
    private Uri imgURI;
    private String shopImg = "";
    private String ownerImg = "";

    private ArrayList<String> shopActImage = new ArrayList<>();
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    private SessionManager sessionManager;
    private String shop_act = "shop_act";
    //private PermissionUtil.PermissionRequestObject mALLPermissionRequest;

    ImageView img_doc_upload_2, ivImage2Selected, ivOwnerImage, ivOwnerImageSelected;
    SwitchCompat switch_shopact;
    private static ViewPager mPager;
    private MyAdapter myAdapter;
    RelativeLayout img_select;
    TextView tv_pick_image;
    TextView tv_pick_image2;
    Context mcontext;
    View view;
    GPSTracker gps;
    private String lattitude="",longitude="";
    ProgressDialog progressDialog;
    private RequestPermissionHandler requestPermissionHandler;
    String str_process_id, delBoyScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doc_upload, container, false);

        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);

        OTPActivity.settitle(Constants.TITLE_DOCUMENT);

        requestPermissionHandler = new RequestPermissionHandler();

        initView();

        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setCancelable(false);

        return view;
    }


    private void initView() {
        btn_next = view.findViewById(R.id.btn_next);
        switch_shopact = (SwitchCompat) view.findViewById(R.id.switch_shopact);
        img_doc_upload_2 = view.findViewById(R.id.img_doc_upload_2);
        ivOwnerImage = view.findViewById(R.id.ivOwnerImage);
        ivOwnerImage.setOnClickListener(this);
        ivOwnerImageSelected = view.findViewById(R.id.ivOwnerImageSelected);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        myAdapter = new MyAdapter(mcontext, shopActImage);
        mPager.setAdapter(myAdapter);

        img_select = (RelativeLayout) view.findViewById(R.id.img_select);
        tv_pick_image = (TextView) view.findViewById(R.id.pick_image);
        tv_pick_image2 = (TextView) view.findViewById(R.id.pick_image2);
        btn_next.setOnClickListener(this);

        ivImage2Selected = view.findViewById(R.id.ivImage2Selected);

        tv_pick_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                capture_document_front_and_back_image(1);
            }
        });

        img_doc_upload_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                capture_document_front_and_back_image(2);

            }
        });

        switch_shopact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);
                    }
                    shop_act = "shop_act";
                    switch_shopact.setText("Choose shop act image");
                } else {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);
                    }
                    shop_act = "bill_proof[]";
                    switch_shopact.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                }
            }
        });
        setButtonImage();
    }

//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    private static File getOutputMediaFile(int type) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Mylogger.getInstance().Logit(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
//                        + IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "O_" + System.currentTimeMillis() + ".jpeg");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    private void capture_document_front_and_back_image(int side_of_document) {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                if (side_of_document == 1) {

                    if (switch_shopact.isChecked()) {
                        if (shopActImage.size() > 0) {
                            shopActImage.clear();
                            myAdapter = new MyAdapter(mcontext, shopActImage);
                            mPager.setAdapter(myAdapter);
                        }
                    }

                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;

                    /*Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);*/

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, IMAGE_SHPO_ACT);

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);
                }
                if (side_of_document == 2) {

                    IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;

                    /*Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);*/

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, IMAGE_SHOP_IMG);

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);
                }


                if (side_of_document == 3) {

                    IMAGE_SELCTED_IMG = IMAGE_OWNER_IMG;

                    /*Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);*/

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, IMAGE_SHOP_IMG);

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_OWNER_IMG);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(mcontext, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setButtonImage() {
        btn_next.setBackground(getResources().getDrawable(R.drawable.btn_normal));
        btn_next.setEnabled(false);
        btn_next.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        if (shopActImage.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(shopImg)) {
            return;
        }
        if (TextUtils.isEmpty(ownerImg)) {
            return;
        }

        btn_next.setBackground(getResources().getDrawable(R.drawable.btn_active));
        btn_next.setEnabled(true);
        btn_next.setTextColor(getResources().getColor(R.color.colorWhite));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                uploadDOCS();
            } else {
                Constants.ShowNoInternet(mcontext);
            }
        }

        if (v.getId() == R.id.ivOwnerImage) {
            capture_document_front_and_back_image(3);
        }

    }

    public void uploadDOCS() {

        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            Double lat = gps.getLatitude();
            Double lng = gps.getLongitude();
            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("Lattitude", lattitude);
            Log.e("Longitude", longitude);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        MultipartBody.Part shop_image_part = null;
        MultipartBody.Part shop_act_part = null;
        MultipartBody.Part owner_img_part = null;


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_IF_SHOP_ACT, String.valueOf(switch_shopact.isChecked()));
            params.put(Constants.PARAM_LATITUDE,lattitude);
            params.put(Constants.PARAM_LONGITUDE,longitude);


            File imagefile = new File(shopImg);
            shop_image_part = MultipartBody.Part.createFormData(Constants.PARAM_SHOP_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imagefile));

            File imagefile2 = new File(ownerImg);
            owner_img_part = MultipartBody.Part.createFormData(Constants.PARAM_OWNER_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(ownerImg)), imagefile2));


            for (int i = 0; i < shopActImage.size(); i++) {
                File imagefile1 = new File(shopActImage.get(i));
                shop_act_part = MultipartBody.Part.createFormData(shop_act, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopActImage.get(i))), imagefile1));

            }


            Mylogger.getInstance().Logit(TAG, "getUserInfo");

            progressDialog.setMessage("Please Wait Uploading User Documents...");
            progressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstoreDocument("Bearer " + sessionManager.getToken(), params, shop_image_part, shop_act_part, owner_img_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        CommonResponse getdocumentdetail = response.body();

                        if (getdocumentdetail.getResponseCode() == 200) {

                            str_process_id = String.valueOf(getdocumentdetail.getData().get(0).getProccess_id());
                            delBoyScreen = getdocumentdetail.getData().get(0).getDelBoysCreen();
                            showAlert(response.body().getResponse(), delBoyScreen);

                        }
                        else if (getdocumentdetail.getResponseCode()==411){
                            sessionManager.logoutUser(getActivity());
                        }else {

                           Toast.makeText(getActivity(),getdocumentdetail.getResponse(),Toast.LENGTH_LONG).show();

                        }

                    }
                    else{
                        Toast.makeText(mcontext, "Server Timeout", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }

    private void showAlert(String msg, String delBoyScreen) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Vendor Documents");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // commanFragmentCallWithoutBackStack(new RateFragment());
                if (delBoyScreen.equalsIgnoreCase("1")) {
                    commanFragmentCallWithoutBackStack(new DeliveryBoyFragment());
                } else {
                    commanFragmentCallWithoutBackStack(new RateFragment());
                }
            }
        });


        alertDialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == IMAGE_SHPO_ACT && resultCode == RESULT_OK) || (requestCode == IMAGE_SHOP_IMG && resultCode == RESULT_OK) || (requestCode == IMAGE_OWNER_IMG && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {
                if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT) {

                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, getActivity());
                    shopActImage.add(shoap_act_image_path);
                    myAdapter.notifyDataSetChanged();

//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    imgURI = ImageUtils.getInstant().getImageUri(getActivity(), photo);
//                    //imgURI = data.getData();
//                    String shoap_act_image_path = ImageUtils.getInstant().getRealPathFromURI(mcontext, imgURI);
//                    Log.e("aadharcard", "front image" + shoap_act_image_path);
//                    shopActImage.add(shoap_act_image_path);
//                    myAdapter.notifyDataSetChanged();
                }


                if (IMAGE_SELCTED_IMG == IMAGE_SHOP_IMG) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    shopImg = ImagePicker.getBitmapPath(bitmap, getActivity()); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                    Glide.with(getActivity()).load(shopImg).into(img_doc_upload_2);
                    Log.e("aadharcard", "back image" + shopImg);
                    ivImage2Selected.setVisibility(View.VISIBLE);
                }


                if (IMAGE_SELCTED_IMG == IMAGE_OWNER_IMG) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    ownerImg = ImagePicker.getBitmapPath(bitmap, getActivity()); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                    Glide.with(getActivity()).load(ownerImg).into(ivOwnerImage);
                    Log.e("aadharcard", "back image" + ownerImg);
                    ivOwnerImageSelected.setVisibility(View.VISIBLE);
                }
            }

            setButtonImage();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    /*public void deleteFileFromMediaManager(Context context, String path) {
        File file = new File(path);
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
                if (file.exists()) {
                    context.deleteFile(file.getName());
                    Log.e("Deleted File", String.valueOf(file));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }*/

    private boolean validation() {

        if (shopImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload Shop Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (ownerImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload owner Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (shopActImage.equals("")) {
            Toast.makeText(mcontext, "Please Upload Shop Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        return true;
    }
}
