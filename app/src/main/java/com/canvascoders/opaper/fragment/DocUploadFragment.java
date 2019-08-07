package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.CustomPopupAdapter;
import com.canvascoders.opaper.adapters.MyAdapterforRecycler;
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
import java.util.List;
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
    private static final int IMAGE_SHPO_ACT_MULTIPLE = 104;
    private static final int IMAGE_SHOP_IMG = 105, IMAGE_OWNER_IMG = 106;
    private static int IMAGE_SELCTED_IMG = 0;
    private Uri imgURI;
    private String shopImg = "";
    private String ownerImg = "";
    private String storeImg = "";

    private ArrayList<String> shopActImage = new ArrayList<>();
    private List<String> billImages = new ArrayList<>();
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    private SessionManager sessionManager;
    private String shop_act = "shop_act";
    RecyclerView rvImages;
    //private PermissionUtil.PermissionRequestObject mALLPermissionRequest;

    ImageView img_doc_upload_2, ivImage2Selected;

    private Switch switch_shopact;

    private static Dialog dialog;
    private static ViewPager mPager;

    private MyAdapter myAdapter;
    private MyAdapterforRecycler myAdapterforRecycler;

    RelativeLayout img_select;
    TextView tv_pick_image;

    TextView tv_pick_image2;

    Context mcontext;
    View view;
    String text = "Electricity Bill/Water Bill/Store Agreement";
    GPSTracker gps;
    private String lattitude = "", longitude = "";
    ProgressDialog progressDialog;
    private RequestPermissionHandler requestPermissionHandler;
    String str_process_id, delBoyScreen;

    ImageView ivShopImageSingle, ivAddressProofSelected, ivStoreImage, ivOwnerImage, ivOwnerImageSelected, ivStoreImageSelected, ivShopImage;

    Button btSubmit;
    int side = 1;


    TextView tvTitleAddressProof;

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
        btSubmit = view.findViewById(R.id.btUpload);
        btSubmit.setOnClickListener(this);
        switch_shopact = (Switch) view.findViewById(R.id.switch_shopact);
        ivOwnerImageSelected = view.findViewById(R.id.ivOwnerImageSelected);
        ivStoreImageSelected = view.findViewById(R.id.ivStoreImageSelected);
        ivOwnerImageSelected.setOnClickListener(this);
        ivStoreImageSelected.setOnClickListener(this);
        ivOwnerImage = view.findViewById(R.id.ivOwnerImage);
        ivStoreImage = view.findViewById(R.id.ivStoreImage);
        ivAddressProofSelected = view.findViewById(R.id.ivAddressProofSelected);
        ivStoreImage.setOnClickListener(this);
        ivOwnerImage.setOnClickListener(this);
        ivShopImage = view.findViewById(R.id.ivCaptureImage);
        ivShopImage.setOnClickListener(this);


        mPager = (ViewPager) view.findViewById(R.id.pager);
        rvImages = view.findViewById(R.id.rvImageBillsMultiple);

        myAdapter = new MyAdapter(mcontext, shopActImage);
        myAdapterforRecycler = new MyAdapterforRecycler(mcontext, shopActImage);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvImages.setLayoutManager(horizontalLayoutManager);

        rvImages.setAdapter(myAdapterforRecycler);
        mPager.setAdapter(myAdapter);


        tvTitleAddressProof = view.findViewById(R.id.tvShopActTitle);
        //  ivShopImageSingle = view.findViewById(R.id.ivShopImageSingle);


      /*  switch_shopact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               *//* if (isChecked) {
                    if (!billImages.isEmpty()) {
                        billImages.clear();
                        for (int i = 0; i < billImages.size(); i++) {
                            File casted_image3 = new File(billImages.get(i));
                            if (casted_image3.exists()) {
                                casted_image3.delete();
                            }

                        }
                        myAdapter = new MyAdapter(mcontext, billImages);

                    }
                    shop_act = "shop_act";
                    side = 1;
                    ivAddressProofSelected.setVisibility(View.GONE);
                    tvTitleAddressProof.setText("Choose shop act image");
                    rvImageListBills.setVisibility(View.GONE);
                    ivShopImageSingle.setVisibility(View.VISIBLE);

                } else {
                    if (!billImages.isEmpty()) {
                        billImages.clear();
                        myAdapter = new MyAdapter(mcontext, billImages);
                        //  mPager.setAdapter(myAdapter);
                    }
                    side = 2;
                    ivAddressProofSelected.setVisibility(View.GONE);
                    shop_act = "bill_proof[]";
                    storeImg = "";
                    Glide.with(mcontext).load(storeImg).into(ivShopImageSingle);
                    rvImageListBills.setVisibility(View.VISIBLE);
                    ivShopImageSingle.setVisibility(View.GONE);
                    tvTitleAddressProof.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                }*//*

                if (isChecked) {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);
                    }
                    shop_act = "shop_act";
                    tvTitleAddressProof.setText("Choose shop act image");
                } else {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);
                    }
                    shop_act = "bill_proof[]";
                    tvTitleAddressProof.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                }

                *//*if (isChecked) {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();

                        // mPager.setAdapter(myAdapter);
                    }
                    shop_act = "shop_act";
                    // switch_shopact.setText("Choose shop act image");

*//**//*
                    if (!billImages.isEmpty()) {
                        billImages.clear();
                        for (int i = 0; i < billImages.size(); i++) {
                            File casted_image3 = new File(billImages.get(i));
                            if (casted_image3.exists()) {
                                casted_image3.delete();
                            }

                        }
                        myAdapter = new MyAdapter(mcontext, billImages);

                    }*//**//*
                    shop_act = "shop_act";
                    side = 1;
                    ivAddressProofSelected.setVisibility(View.GONE);
                    tvTitleAddressProof.setText("Choose shop act image");
                    rvImageListBills.setVisibility(View.GONE);
                    ivShopImageSingle.setVisibility(View.VISIBLE);
                } else {


                    side = 2;
                    ivAddressProofSelected.setVisibility(View.GONE);
                    shop_act = "bill_proof[]";
                    storeImg = "";
                    Glide.with(mcontext).load(storeImg).into(ivShopImageSingle);
                    rvImageListBills.setVisibility(View.VISIBLE);
                    ivShopImageSingle.setVisibility(View.GONE);
                    tvTitleAddressProof.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        // mPager.setAdapter(myAdapter);
                    }

                }*//*
            }
        });*/


       /* img_doc_upload_2 = view.findViewById(R.id.img_doc_upload_2);
        ivOwnerImage = view.findViewById(R.id.ivOwnerImage);
        ivOwnerImage.setOnClickListener(this);
        ivOwnerImageSelected = view.findViewById(R.id.ivOwnerImageSelected);*/
      /*  mPager = (ViewPager) view.findViewById(R.id.pager);
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
        setButtonImage();*/

        switch_shopact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);

                        myAdapterforRecycler = new MyAdapterforRecycler(mcontext, shopActImage);
                        rvImages.setAdapter(myAdapterforRecycler);
                    }
                    shop_act = "shop_act";
                    tvTitleAddressProof.setText("Choose shop act image");
                } else {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(mcontext, shopActImage);
                        mPager.setAdapter(myAdapter);
                        myAdapterforRecycler = new MyAdapterforRecycler(mcontext, shopActImage);
                        rvImages.setAdapter(myAdapterforRecycler);
                    }
                    shop_act = "bill_proof[]";
                    tvTitleAddressProof.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                }
            }
        });

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

               /* if (side_of_document == 1) {


                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);


                    if (switch_shopact.isChecked()) {
                        if (shopActImage.size() > 0) {
                            shopActImage.clear();
                            myAdapter = new MyAdapter(mcontext, shopActImage);
                            rvImageListBills.setAdapter(myAdapter);
                            //mPager.setAdapter(myAdapter);
                        }
                        ivShopImageSingle.setVisibility(View.VISIBLE);

                    }

                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;
                }*/


                if (side_of_document == 1) {

                    if (switch_shopact.isChecked()) {
                        if (shopActImage.size() > 0) {
                            shopActImage.clear();
                            myAdapter = new MyAdapter(mcontext, shopActImage);
                            mPager.setAdapter(myAdapter);
                            myAdapterforRecycler = new MyAdapterforRecycler(mcontext, shopActImage);
                            rvImages.setAdapter(myAdapterforRecycler);
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


                if (side_of_document == 3) {

                    IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;


                    Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);
                }


                if (side_of_document == 4) {

                    IMAGE_SELCTED_IMG = IMAGE_OWNER_IMG;

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

   /* private void setButtonImage() {
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

    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCaptureImage:
                capture_document_front_and_back_image(1);
                break;

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
            case R.id.btUpload:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation()) {
                        uploadDOCS();
                    }

                } else {
                    Constants.ShowNoInternet(mcontext);
                }
                break;


        }
       /* if (v.getId() == R.id.btn_next) {

        }
*/
      /*  if (v.getId() == R.id.ivOwnerImage) {
            capture_document_front_and_back_image(3);
        }*/

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

        MultipartBody.Part owner_img_part = null;


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_IF_SHOP_ACT, String.valueOf(switch_shopact.isChecked()));
            params.put(Constants.PARAM_LATITUDE, lattitude);
            params.put(Constants.PARAM_LONGITUDE, longitude);



            /*if (side == 2) {
                for (int i = 0; i < billImages.size(); i++) {
                    File imagefile1 = new File(billImages.get(i));
                    shop_act_part = MultipartBody.Part.createFormData(shop_act, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(billImages.get(i))), imagefile1));

                }

            } else {
                File imagefile1 = new File(storeImg);
                shop_act_part = MultipartBody.Part.createFormData(shop_act, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(storeImg)), imagefile1));

            }*/

            File imagefile = new File(shopImg);
            shop_image_part = MultipartBody.Part.createFormData(Constants.PARAM_SHOP_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopImg)), imagefile));

            File imagefile2 = new File(ownerImg);
            owner_img_part = MultipartBody.Part.createFormData(Constants.PARAM_OWNER_IMAGE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(ownerImg)), imagefile2));

            MultipartBody.Part shop_act_part[] = new MultipartBody.Part[shopActImage.size()];
            ;

            for (int i = 0; i < shopActImage.size(); i++) {
                File imagefile1 = new File(shopActImage.get(i));
                Log.e("bill_proof", shopActImage.get(i));
                shop_act_part[i] = MultipartBody.Part.createFormData(shop_act, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(shopActImage.get(i))), imagefile1));

            }


            Mylogger.getInstance().Logit(TAG, "getUserInfo");

            progressDialog.setMessage("Please Wait Uploading User Documents...");
            progressDialog.show();
            Log.e("sizeof_doc", String.valueOf(shopActImage.size()));
            Log.e("sizeof_doc", String.valueOf(shopActImage.size()));


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

                            deleteImages();

                            str_process_id = String.valueOf(getdocumentdetail.getData().get(0).getProccess_id());
                            delBoyScreen = getdocumentdetail.getData().get(0).getDelBoysCreen();
                            showAlert(response.body().getResponse(), delBoyScreen);

                        } else if (getdocumentdetail.getResponseCode() == 411) {
                            sessionManager.logoutUser(getActivity());
                        } else {

                            Toast.makeText(getActivity(), getdocumentdetail.getResponse(), Toast.LENGTH_LONG).show();

                        }

                    } else {
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


        Button btSubmit;
        TextView tvMessage, tvTitle;


        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(mcontext);
        dialog = new Dialog(getActivity(), R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Document Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delBoyScreen.equalsIgnoreCase("1")) {
                    //   commanFragmentCallWithoutBackStack(new RateFragment());
                    commanFragmentCallWithoutBackStack(new DeliveryBoyFragment());

                } else {
                    commanFragmentCallWithoutBackStack(new RateFragment());
                }
                dialog.dismiss();

            }
        });

        dialog.setCancelable(false);

        dialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == IMAGE_SHPO_ACT && resultCode == RESULT_OK) || (requestCode == IMAGE_SHOP_IMG && resultCode == RESULT_OK) || (requestCode == IMAGE_OWNER_IMG && resultCode == RESULT_OK) || (requestCode == IMAGE_SHPO_ACT_MULTIPLE && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {
                /*if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT) {

                 *//* Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                storeImg = ImagePicker.getBitmapPath(bitmap, getActivity()); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                Glide.with(getActivity()).load(storeImg).into(ivShopImageSingle);
                Log.e("aadharcard", "back image" + storeImg);


                for (int i = 0; i < billImages.size(); i++) {
                    File casted_image3 = new File(billImages.get(i));
                    if (casted_image3.exists()) {
                        casted_image3.delete();
                    }

                }*//*

                ivAddressProofSelected.setVisibility(View.VISIBLE);
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, getActivity());
                shopActImage.add(shoap_act_image_path);
                // rvImageListBills.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                rvImageListBills.setAdapter(myAdapter);
               // myAdapter = new MyAdapter(mcontext, shopActImage);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                rvImageListBills.setLayoutManager(horizontalLayoutManager);

                rvImageListBills.setAdapter(myAdapter);
                ivAddressProofSelected.setVisibility(View.VISIBLE);





//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    imgURI = ImageUtils.getInstant().getImageUri(getActivity(), photo);
//                    //imgURI = data.getData();
//                    String shoap_act_image_path = ImageUtils.getInstant().getRealPathFromURI(mcontext, imgURI);
//                    Log.e("aadharcard", "front image" + shoap_act_image_path);
//                    shopActImage.add(shoap_act_image_path);
//                    myAdapter.notifyDataSetChanged();
            }*/
                if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT) {

                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, getActivity());

                    shopActImage.add(shoap_act_image_path);


                    Log.e("size", String.valueOf(shopActImage.size()));
                    myAdapterforRecycler.notifyDataSetChanged();
                    myAdapter.notifyDataSetChanged();

//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    imgURI = ImageUtils.getInstant().getImageUri(getActivity(), photo);
//                    //imgURI = data.getData();
//                    String shoap_act_image_path = ImageUtils.getInstant().getRealPathFromURI(mcontext, imgURI);
//                    Log.e("aadharcard", "front image" + shoap_act_image_path);
//                    shopActImage.add(shoap_act_image_path);
//                    myAdapter.notifyDataSetChanged();
                }

           /* if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT_MULTIPLE) {
                File casted_image3 = new File(storeImg);
                if (casted_image3.exists()) {
                    casted_image3.delete();
                }
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, getActivity());

                billImages.add(shoap_act_image_path);
//                myAdapter.notifyDataSetChanged();
                storeImg = "";
                rvImageListBills.setVisibility(View.VISIBLE);
                myAdapter = new MyAdapter(mcontext, shopActImage);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                rvImageListBills.setLayoutManager(horizontalLayoutManager);

                rvImageListBills.setAdapter(myAdapter);
                ivAddressProofSelected.setVisibility(View.VISIBLE);
            }*/


                if (IMAGE_SELCTED_IMG == IMAGE_SHOP_IMG) {
                    File casted_image6 = new File(shopImg);
                    if (casted_image6.exists()) {
                        casted_image6.delete();
                    }

                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    shopImg = ImagePicker.getBitmapPath(bitmap, getActivity()); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                    Glide.with(getActivity()).load(shopImg).into(ivStoreImage);
                    Log.e("aadharcard", "back image" + shopImg);
                    ivStoreImageSelected.setVisibility(View.VISIBLE);

                }


                if (IMAGE_SELCTED_IMG == IMAGE_OWNER_IMG) {
                    File casted_image2 = new File(ownerImg);
                    if (casted_image2.exists()) {
                        casted_image2.delete();
                    }
                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    ownerImg = ImagePicker.getBitmapPath(bitmap, getActivity()); // ImageUtils.getInstant().getImageUri(getActivity(), photo);
                    Glide.with(getActivity()).load(ownerImg).into(ivOwnerImage);
                    Log.e("aadharcard", "back image" + ownerImg);
                    ivOwnerImageSelected.setVisibility(View.VISIBLE);
                }
            }


            //setButtonImage();
        }

    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
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

       /* if (shopImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload Store Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }*/
        if (ownerImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload owner Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (shopImg.equals("")) {
            Toast.makeText(mcontext, "Please Upload Store Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }
        if (shopActImage.size() == 0) {
            Toast.makeText(mcontext, "Please Upload Shop Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        return true;
    }


    @Override
    public void onResume() {
     /*   if (!AppApplication.networkConnectivity.isNetworkAvailable()) {
            Constants.ShowNoInternet(mcontext);
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    //deleteImages();

                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });*/


        super.onResume();
    }

    private void deleteImages() {

        File casted_image = new File(storeImg);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(shopImg);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }


        File casted_image2 = new File(ownerImg);
        if (casted_image2.exists()) {
            casted_image2.delete();
        }

    }
}
