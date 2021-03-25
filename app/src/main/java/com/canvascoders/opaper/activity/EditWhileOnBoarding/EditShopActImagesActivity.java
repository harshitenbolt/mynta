package com.canvascoders.opaper.activity.EditWhileOnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.adapters.MyAdapter;
import com.canvascoders.opaper.adapters.MyAdapterforRecycler;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditShopActImagesActivity extends AppCompatActivity implements View.OnClickListener {
    private String shop_act = "shop_act";
    private SessionManager sessionManager;
    String text = "Electricity Bill/Water Bill/Store Agreement";
    private Switch switch_shopact;
    Button btSubmit;
    private static ViewPager mPager;
    RecyclerView rvImages;
    private MyAdapter myAdapter;
    private MyAdapterforRecycler myAdapterforRecycler;
    TextView tvTitleAddressProof;
    private static int IMAGE_SELCTED_IMG = 0;
    private ArrayList<String> shopActImage = new ArrayList<>();
    private RequestPermissionHandler requestPermissionHandler;
    private static final int IMAGE_SHPO_ACT = 103;
    private static final int IMAGE_SHPO_ACT_MULTIPLE = 104;
    private static final int IMAGE_SHOP_IMG = 105, IMAGE_OWNER_IMG = 106;
    private String shopImg = "";
    private String ownerImg = "";
    private String lattitude = "", longitude = "";
    GPSTracker gps;
    private String storeImg = "";

    String str_process_id, delBoyScreen;
    private String TAG = "dgfsdfg";
    ProgressDialog progressDialog;
    ImageView ivBack;
    ImageView ivShopImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_act_images);
        requestPermissionHandler = new RequestPermissionHandler();
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra(Constants.KEY_PROCESS_ID);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        init();
    }

    private void init() {
        btSubmit = findViewById(R.id.btUpload);
        btSubmit.setOnClickListener(this);
        switch_shopact = (Switch) findViewById(R.id.switch_shopact);

        ivShopImage = findViewById(R.id.ivCaptureImage);
        ivShopImage.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        rvImages = findViewById(R.id.rvImageBillsMultiple);

        myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);
        myAdapterforRecycler = new MyAdapterforRecycler(EditShopActImagesActivity.this, shopActImage);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditShopActImagesActivity.this, RecyclerView.VERTICAL, false);
        rvImages.setLayoutManager(horizontalLayoutManager);

        rvImages.setAdapter(myAdapterforRecycler);
        mPager.setAdapter(myAdapter);


        tvTitleAddressProof = findViewById(R.id.tvShopActTitle);

        switch_shopact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);
                        mPager.setAdapter(myAdapter);

                        myAdapterforRecycler = new MyAdapterforRecycler(EditShopActImagesActivity.this, shopActImage);
                        rvImages.setAdapter(myAdapterforRecycler);
                    }
                    shop_act = "shop_act";
                    tvTitleAddressProof.setText("Choose shop act image");
                } else {
                    if (!shopActImage.isEmpty()) {
                        shopActImage.clear();
                        myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);
                        mPager.setAdapter(myAdapter);
                        myAdapterforRecycler = new MyAdapterforRecycler(EditShopActImagesActivity.this, shopActImage);
                        rvImages.setAdapter(myAdapterforRecycler);
                    }
                    shop_act = "bill_proof[]";
                    tvTitleAddressProof.setText("Electricity Bill/Water Bill/Store Rent Agreement");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCaptureImage:
                capture_document_front_and_back_image(1);
                break;
            case R.id.btUpload:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation()) {
                        uploadDOCS();
                    }

                } else {
                    Constants.ShowNoInternet(EditShopActImagesActivity.this);
                }
                break;
        }


    }


    public void uploadDOCS() {

        gps = new GPSTracker(EditShopActImagesActivity.this);
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


            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getstoreDocumentEdit("Bearer " + sessionManager.getToken(), params, shop_act_part);
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
                            //showAlert(response.body().getResponse(), delBoyScreen);
                            finish();

                        } else if (getdocumentdetail.getResponseCode() == 411) {
                            sessionManager.logoutUser(EditShopActImagesActivity.this);
                        } else {

                            Toast.makeText(EditShopActImagesActivity.this, getdocumentdetail.getResponse(), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(EditShopActImagesActivity.this, "#errorcode 2098 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                        // Toast.makeText(EditShopActImagesActivity.this, "Server Timeout", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditShopActImagesActivity.this, "#errorcode 2098 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Constants.ShowNoInternet(EditShopActImagesActivity.this);
        }

    }


    private boolean validation() {

       /* if (shopImg.equals("")) {
            Toast.makeText(EditShopActImagesActivity.this, "Please Upload Store Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }*/

        if (shopActImage.size() == 0) {
            Toast.makeText(EditShopActImagesActivity.this, "Please Upload Shop Image", Toast.LENGTH_SHORT).show();
            // showMSG(false, "Provide Pincode");
            return false;
        }

        return true;
    }


    private void capture_document_front_and_back_image(int side_of_document) {
        requestPermissionHandler.requestPermission(EditShopActImagesActivity.this, new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

               /* if (side_of_document == 1) {


                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;
                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditShopActImagesActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);


                    if (switch_shopact.isChecked()) {
                        if (shopActImage.size() > 0) {
                            shopActImage.clear();
                            myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);
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
                            // shopActImage.clear();
                            myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);
                            mPager.setAdapter(myAdapter);
                            myAdapterforRecycler = new MyAdapterforRecycler(EditShopActImagesActivity.this, shopActImage);
                            rvImages.setAdapter(myAdapterforRecycler);
                        }
                    }

                    IMAGE_SELCTED_IMG = IMAGE_SHPO_ACT;

                    /*Intent chooseImageIntent = ImagePicker.getCameraIntent(EditShopActImagesActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);*/

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, IMAGE_SHPO_ACT);

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditShopActImagesActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_SHPO_ACT);
                }


                if (side_of_document == 3) {

                    IMAGE_SELCTED_IMG = IMAGE_SHOP_IMG;


                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditShopActImagesActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_SHOP_IMG);
                }


                if (side_of_document == 4) {

                    IMAGE_SELCTED_IMG = IMAGE_OWNER_IMG;

                    Intent chooseImageIntent = ImagePicker.getCameraIntent(EditShopActImagesActivity.this);
                    startActivityForResult(chooseImageIntent, IMAGE_OWNER_IMG);
                }


            }

            @Override
            public void onFailed() {
                Toast.makeText(EditShopActImagesActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == IMAGE_SHPO_ACT && resultCode == RESULT_OK) || (requestCode == IMAGE_SHOP_IMG && resultCode == RESULT_OK) || (requestCode == IMAGE_OWNER_IMG && resultCode == RESULT_OK) || (requestCode == IMAGE_SHPO_ACT_MULTIPLE && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {

                if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT) {




                  //  Bitmap bitmap = ImagePicker.getImageFromResult(EditShopActImagesActivity.this, resultCode, data);
                   // String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, EditShopActImagesActivity.this);


                    Uri uri = ImagePicker.getPickImageResultUri(EditShopActImagesActivity.this, data);
                    String shoap_act_image_path  = ImagePicker.getPathFromUri(EditShopActImagesActivity.this,uri); // ImageUtils.getInstant().getImageUri(EditStoreInformationActivity.this, photo);


                    shopActImage.add(shoap_act_image_path);


                    Log.e("size", String.valueOf(shopActImage.size()));
                    myAdapterforRecycler.notifyDataSetChanged();
                    myAdapter.notifyDataSetChanged();

//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    imgURI = ImageUtils.getInstant().getImageUri(EditShopActImagesActivity.this, photo);
//                    //imgURI = data.getData();
//                    String shoap_act_image_path = ImageUtils.getInstant().getRealPathFromURI(EditShopActImagesActivity.this, imgURI);
//                    Log.e("aadharcard", "front image" + shoap_act_image_path);
//                    shopActImage.add(shoap_act_image_path);
//                    myAdapter.notifyDataSetChanged();
                }

           /* if (IMAGE_SELCTED_IMG == IMAGE_SHPO_ACT_MULTIPLE) {
                File casted_image3 = new File(storeImg);
                if (casted_image3.exists()) {
                    casted_image3.delete();
                }
                Bitmap bitmap = ImagePicker.getImageFromResult(EditShopActImagesActivity.this, resultCode, data);
                String shoap_act_image_path = ImagePicker.getBitmapPath(bitmap, EditShopActImagesActivity.this);

                billImages.add(shoap_act_image_path);
//                myAdapter.notifyDataSetChanged();
                storeImg = "";
                rvImageListBills.setVisibility(View.VISIBLE);
                myAdapter = new MyAdapter(EditShopActImagesActivity.this, shopActImage);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditShopActImagesActivity.this, LinearLayoutManager.HORIZONTAL, false);

                rvImageListBills.setLayoutManager(horizontalLayoutManager);

                rvImageListBills.setAdapter(myAdapter);
                ivAddressProofSelected.setVisibility(View.VISIBLE);
            }*/


                if (IMAGE_SELCTED_IMG == IMAGE_SHOP_IMG) {
                    File casted_image6 = new File(shopImg);
                    if (casted_image6.exists()) {
                        casted_image6.delete();
                    }

                  /*  Bitmap bitmap = ImagePicker.getImageFromResult(EditShopActImagesActivity.this, resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    shopImg = ImagePicker.getBitmapPath(bitmap, EditShopActImagesActivity.this); // ImageUtils.getInstant().getImageUri(EditShopActImagesActivity.this, photo);
                    Glide.with(EditShopActImagesActivity.this).load(shopImg).into(ivStoreImage);
                    Log.e("aadharcard", "back image" + shopImg);
                    ivStoreImageSelected.setVisibility(View.VISIBLE);*/

                }


                if (IMAGE_SELCTED_IMG == IMAGE_OWNER_IMG) {
                    File casted_image2 = new File(ownerImg);
                    if (casted_image2.exists()) {
                        casted_image2.delete();
                    }
                   /* Bitmap bitmap = ImagePicker.getImageFromResult(EditShopActImagesActivity.this, resultCode, data);
                    // img_doc_upload_2.setImageBitmap(bitmap);
                    ownerImg = ImagePicker.getBitmapPath(bitmap, EditShopActImagesActivity.this); // ImageUtils.getInstant().getImageUri(EditShopActImagesActivity.this, photo);
                    Glide.with(EditShopActImagesActivity.this).load(ownerImg).into(ivOwnerImage);
                    Log.e("aadharcard", "back image" + ownerImg);
                    ivOwnerImageSelected.setVisibility(View.VISIBLE);*/
                }
            }


            //setButtonImage();
        }

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
