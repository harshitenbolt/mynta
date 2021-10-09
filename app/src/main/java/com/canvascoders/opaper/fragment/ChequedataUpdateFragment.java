package com.canvascoders.opaper.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.canvascoders.opaper.Beans.BankDetailsResponse.BankDetailsResponse;
import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.canvascoders.opaper.Beans.GetChequeOCRDetails.GetOCRChequeDetails;
import com.canvascoders.opaper.Beans.PanCardOcrResponse.PanCardSubmitResponse;
import com.canvascoders.opaper.Beans.VendorBankDetail;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.CropImage2Activity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.TaskListActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.ImageUploadTask;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.OnTaskCompleted;
import com.canvascoders.opaper.utils.PostAsynchTask;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.utils.RequestPermissionHandler;
import com.canvascoders.opaper.utils.SessionManager;
//import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.canvascoders.opaper.activity.CropImage2Activity.KEY_SOURCE_URI;

public class ChequedataUpdateFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_CHEQUE = 101;
    private static final String IMAGE_DIRECTORY_NAME = "oppr";
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    ProgressDialog progressDialog;
    private String TAG = "ChequeUpload";
    private Button btn_next;
    VendorList vendor;
    String ifscCode = "";
    private static Dialog dialog;
    String accountNumber = "";
    String bank_name = "";
    String payeeName = "";
    String bank_branch = "";
    String branch_address = "";
    private String cancelChequeImagepath = "", imagecamera = "";
    private Uri imgURI;
    private ImageView btn_cheque_card;
    private ImageView btn_cheque_card_select;
    private SessionManager sessionManager;
    private boolean isPanSelected = true;
    private EditText edit_ac_no, edit_ac_name, edit_ifsc, edit_bank_name, edit_bank_branch_name, edit_bank_address;
    //private TextRecognizer detector;
    private RequestPermissionHandler requestPermissionHandler;
    private CheckBox cd_aggre_terms_condition;
    File auxFile = null;
    public static final int CROPPED_IMAGE = 5333;
    boolean captured = false;
    private AsyncTask mMyTask;
    //    public static final String OCRMEREK = "https://api.merak.ai/v1/text-recognition/cheque/recognize/?key=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImJpa2FzaC5taXNocmFAbXludHJhLmNvbSIsIm9yZ2FuaXphdGlvbl9pZCI6OSwiaWF0IjoxNTMxOTkwOTUzfQ.5EpThmiqO_iN9Bg6RBR-kKQch0QjvEmI-lNg2SQxi8k";
    String str_process_id;
    int request_id = 0;

    Context mcontext;
    private ImageView ivChequeImage, ivBack;
    TextView tvClickCheque;
    View view;
    String s = "1";


    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Mylogger.getInstance().Logit(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "O_" + System.currentTimeMillis() + ".jpeg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cheque, container, false);
        mcontext = this.getActivity();

        sessionManager = new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        // str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        //DashboardActivity.settitle(Constants.TITLE_CHEQUE);
        s = "1";
        requestPermissionHandler = new RequestPermissionHandler();
        /*vendor = (VendorList) getArguments().getSerializable("data");
        if (vendor != null) {


            str_process_id= vendor.getProccessId();
            Log.e("VId", "" + vendor.getVendorId() + " ::" + vendor.getName());
        }*/
        Bundle bundle = getArguments();
        if (bundle != null) {
            vendor = (VendorList) bundle.getSerializable("data");
            str_process_id = String.valueOf(vendor.getProccessId());
        }

        initView();
        // getUserBankDetails();
        getUserBankDetailsUpdated();
        return view;
    }

    private void getUserBankDetailsUpdated() {


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.KEY_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            progressDialog.dismiss();

            Call<BankDetailsResponse> call = ApiClient.getClient().create(ApiInterface.class).getBankkDetails("Bearer " + sessionManager.getToken(), params);
            call.enqueue(new Callback<BankDetailsResponse>() {
                @Override
                public void onResponse(Call<BankDetailsResponse> call, Response<BankDetailsResponse> response) {

                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Mylogger.getInstance().Logit(TAG, String.valueOf(response));
                        BankDetailsResponse checkEsignResponse = response.body();

                        if (checkEsignResponse.getResponseCode() == 200) {
                            progressDialog.dismiss();
                            Glide.with(getActivity()).load(checkEsignResponse.getData().get(0).getCancelledCheque())
                                    .into(ivChequeImage);

/*
                            final File file = new File(Environment.getExternalStorageDirectory(), vendorBankDetail.getCancelledCheque());
                            Uri uri = Uri.fromFile(file);
                            auxFile = new File(uri.toString());
                            Log.e("CHEQUE IMAGE", "" + auxFile.toString());*/
                        } else if (checkEsignResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        } else if (checkEsignResponse.getResponseCode() == 411) {
                            sessionManager.logoutUser(mcontext);
                        } else {
                            progressDialog.dismiss();
                            // Toast.makeText(ProcessInfoActivity.this, object.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "#errorcode 2062 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<BankDetailsResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "#errorcode 2062 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Mylogger.getInstance().Logit(TAG, t.toString());
                }
            });

        }

    }

    VendorBankDetail vendorBankDetail;

    private void getUserBankDetails() {
        HashMap<String, String> sendMap = new HashMap<>();
        sendMap.put(Constants.PARAM_PROCESS_ID, str_process_id);
        sendMap.put(Constants.PARAM_TOKEN, "Bearer " + sessionManager.getToken());

        new PostAsynchTask(sendMap, Constants.GET_BANK_DETAILS, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getInt("responseCode") == 200) {
                            JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                            vendorBankDetail = new VendorBankDetail(result);

                           /* edit_bank_name.setText(vendorBankDetail.getBankName());
                            edit_bank_branch_name.setText(vendorBankDetail.getBankBranchName());
                            edit_bank_address.setText(vendorBankDetail.getBankAddress());
                            edit_ac_no.setText(vendorBankDetail.getBankAc());
                            edit_ac_name.setText(vendorBankDetail.getPayeeName());
                            edit_ifsc.setText(vendorBankDetail.getIfsc());*/

                            Log.e("Image", vendorBankDetail.getCancelledCheque());
                            Glide.with(getActivity()).load(vendorBankDetail.getCancelledCheque())
                                    .into(btn_cheque_card);
                           /* mMyTask = new DownloadTask()
                                    .execute(stringToURL(vendorBankDetail.getCancelledCheque()
                                    ));*/
/*
                            Glide.with(ChequedataUpdateFragment.this)
                                    .load(vendorBankDetail.getCancelledCheque())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100,100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
                                            saveImage(resource);
                                        }
                                    });*/


                            final File file = new File(Environment.getExternalStorageDirectory(), vendorBankDetail.getCancelledCheque());
                            Uri uri = Uri.fromFile(file);
                            auxFile = new File(uri.toString());
                            Log.e("CHEQUE IMAGE", "" + auxFile.toString());
                        } else if (jsonObject.getInt("responseCode") == 405) {
                            sessionManager.logoutUser(mcontext);
                        } else if (jsonObject.getInt("responseCode") == 411) {
                            sessionManager.logoutUser(mcontext);
                        } else {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();
    }







   /* private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + "FILE_NAME" + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/YOUR_FOLDER_NAME");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(getActivity(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }*/

/*
    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
*/


    // Custom method to convert string to url
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images", MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "UniqueFileName" + ".jpg");

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }


    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) view.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) view.findViewById(R.id.txt_show_msg_fail);
        txt_show_msg_sucess.setVisibility(View.GONE);
        txt_show_msg_fail.setVisibility(View.GONE);
        if (b) {
            txt_show_msg_sucess.setText(msg);
            txt_show_msg_sucess.setVisibility(View.VISIBLE);
        } else {
            txt_show_msg_fail.setText(msg);
            txt_show_msg_fail.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                txt_show_msg_sucess.setVisibility(View.GONE);
                txt_show_msg_fail.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void initView() {
        btn_next = view.findViewById(R.id.btExtract);
        tvClickCheque = view.findViewById(R.id.tvClickCheque);
        btn_cheque_card_select = (ImageView) view.findViewById(R.id.tvClickChequeSelected);
        btn_cheque_card_select.setOnClickListener(this);
        ivChequeImage = view.findViewById(R.id.ivChequeImage);
        /*ivBack = getActivity().findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equalsIgnoreCase("1")) {
                    deleteImages();
                    getActivity().finish();
                }

            }
        });*/

        /*btn_cheque_card = (ImageView) view.findViewById(R.id.btn_cheque_cards);
        btn_cheque_card_select = (ImageView) view.findViewById(R.id.btn_cheque_cards_done);
        cd_aggre_terms_condition = view.findViewById(R.id.cbAgreeTC);
        edit_ac_no = (EditText) view.findViewById(R.id.edit_ac_no);
        edit_ac_name = (EditText) view.findViewById(R.id.edit_ac_name);
        edit_ifsc = (EditText) view.findViewById(R.id.edit_ifsc);
        edit_bank_name = (EditText) view.findViewById(R.id.edit_bank_name);
        edit_bank_branch_name = (EditText) view.findViewById(R.id.edit_bank_branch_name);
        edit_bank_address = (EditText) view.findViewById(R.id.edit_bank_address);*/


        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setCancelable(false);
        btn_next.setOnClickListener(this);
        tvClickCheque.setOnClickListener(this);
        btn_cheque_card_select.setOnClickListener(this);
        //detector = new TextRecognizer.Builder(mcontext).build();
        progressDialog = new ProgressDialog(mcontext);


    }/*  edit_bank_name.setEnabled(true);
        edit_bank_branch_name.setEnabled(true);
        edit_bank_address.setEnabled(true);
        edit_ac_no.setEnabled(true);
        edit_ac_name.setEnabled(true);
        edit_ifsc.setEnabled(true);

        btn_next.setOnClickListener(this);
        btn_cheque_card.setOnClickListener(this);
        detector = new TextRecognizer.Builder(mcontext).build();*/


   /* private void setButtonImage() {
        if (isPanSelected == true) {
            btn_next.setBackground(getResources().getDrawable(R.drawable.btn_active));
            btn_next.setEnabled(true);
            btn_next.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_cheque_card_select.setVisibility(View.VISIBLE);
        } else {
            btn_next.setBackground(getResources().getDrawable(R.drawable.btn_normal));
            btn_next.setEnabled(false);
            btn_next.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btn_cheque_card_select.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btExtract:
                //    Constants.hideKeyboardwithoutPopulate(getActivity());
                //if (validation()) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    if (validation()) {
                        DialogUtil.chequeDetail(getActivity(), accountNumber, payeeName, ifscCode, str_process_id, bank_name, bank_branch, branch_address, new DialogListner() {
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
                            public void onStoreType(Integer positin, JSONObject jsonObject) {

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {
                                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                                    storeCheque(accName, payeename, ifsc, bankname, BranchName, bankAdress);
                                } else {
                                    Constants.ShowNoInternet(getActivity());
                                }
                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }
                        });


                    }
                } else {
                    Constants.ShowNoInternet(getActivity());
                }

                break;
            case R.id.tvClickCheque:
                capture_cheque_image();
                break;
            case R.id.tvClickChequeSelected:
                capture_cheque_image();
                break;
        }

    }

    public void capture_cheque_image() {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {

                /*Intent intent = new Intent(getActivity(), MyCameraActivity.class);
                startActivityForResult(intent, IMAGE_CHEQUE);*/

                Intent chooseImageIntent = ImagePicker.getCameraIntent(getActivity());
                startActivityForResult(chooseImageIntent, IMAGE_CHEQUE);

            }

            @Override
            public void onFailed() {
                Toast.makeText(mcontext, "request permission failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validation() {
      /*  if (TextUtils.isEmpt0y(edit_ac_no.getText().toString())) {
            edit_ac_no.requestFocus();
            edit_ac_no.setError("Provide Accout Number");
            //showMSG(false, "Provide Accout Number");
            return false;
        }
        if (TextUtils.isEmpty(edit_ac_name.getText().toString())) {
            edit_ac_name.requestFocus();
            edit_ac_name.setError("Provide Accout Name");
           // showMSG(false, "Provide Accout Name");
            return false;
        }
        if (TextUtils.isEmpty(edit_ifsc.getText().toString())) {
            edit_ifsc.requestFocus();
            edit_ifsc.setError("Provide IFSCode");
           // showMSG(false, "Provide IFSCode");
            return false;
        }
        if (!cd_aggre_terms_condition.isChecked()) {
            showMSG(false, "Please verify all details with physical evidence.");
            return false;
        }
        if (!isPanSelected) {
            return false;
        }*/
        if (captured == false) {
            showMSG(false, "Please Upload Check Image...");
            return false;
        }

        if (!isPanSelected) {
            Toast.makeText(mcontext, "Please select Cheque Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == IMAGE_CHEQUE && resultCode == RESULT_OK) || (requestCode == CROPPED_IMAGE && resultCode == RESULT_OK)) {
            Log.e("Hello", "Done");
            if (requestCode == IMAGE_CHEQUE) {
                deleteImages();
                // CropImage.activity(Uri.fromFile(new File())).start(getContext(), this);
//                Constants.hideKeyboardwithoutPopulate(getActivity());

                Uri uri = ImagePicker.getPickImageResultUri(getActivity(), data);
                Intent intent = new Intent(getActivity(), CropImage2Activity.class);

                //intent.putExtra(KEY_SOURCE_URI, Uri.fromFile(new File(ImagePicker.getBitmapPath(bitmap, getActivity()))).toString());
                intent.putExtra(KEY_SOURCE_URI, uri.toString());
                startActivityForResult(intent, CROPPED_IMAGE);

            } else if (requestCode == CROPPED_IMAGE) {
                Log.e("Hello", "Done");
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(getActivity(), imgURI);
                Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                        .into(ivChequeImage);
                isPanSelected = true;
                tvClickCheque.setVisibility(View.GONE);
                btn_cheque_card_select.setVisibility(View.VISIBLE);
                /* CropImage.ActivityResult result = CropImage.getActivityResult(data);*/
/*
                Constants.hideKeyboardwithoutPopulate(getActivity());
                imgURI = Uri.parse(data.getStringExtra("uri"));
                cancelChequeImagepath = RealPathUtil.getPath(getActivity(), imgURI);
                tvClickCheque.setVisibility(View.GONE);
                btn_cheque_card_select.setVisibility(View.VISIBLE);
                //cancelChequeImagepath = imgURI.getPath();*/
                File casted_image6 = new File(imagecamera);
                if (casted_image6.exists()) {
                    casted_image6.delete();
                }

                captured = true;
                ChequeOCR();

             /*   HashMap<String, String> fileMap = new HashMap<>();
                fileMap.put("image", cancelChequeImagepath);
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setCancelable(false);
                pd.setTitle("Loading . . .");
                pd.show();
                new ImageUploadTask(Constants.OCRMEREK, null, fileMap, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String result) {
                        pd.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.optInt("success") == 1) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                if (dataObj != null) {
                                    ifscCode = dataObj.optString("ifsc_code");
                                    accountNumber = dataObj.optString("account_number");
                                    bank_name = dataObj.optString("bank_name");
                                    bank_branch = dataObj.optString("bank_branch");
                                    branch_address = dataObj.optString("branch_address");


                                    try {
                                        request_id = jsonObject.optInt("request_id");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    JSONArray payeeNames = dataObj.optJSONArray("payee_name");

                                    if (payeeNames != null && payeeNames.length() > 0) {
                                        payeeName = payeeNames.getString(0);

                                    }

                                }


                            } else {

                                Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cancelChequeImagepath = "";
                            Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.checkimage)
                                    .into(ivChequeImage);
                            isPanSelected = false;
                            tvClickCheque.setVisibility(View.VISIBLE);
                            btn_cheque_card_select.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "There is some issue retrieving data from cheque image, Reselect image or enter manually", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();*/

//                OCRGetDetails(imgURI);
                Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                        .into(ivChequeImage);
                isPanSelected = true;
            }
        }


    }


    public void storeCheque(String acname, String payeename, String ifsc, String bankname, String branchname, String branchAddress) {

        MultipartBody.Part cheque_image_part = null;


        Mylogger.getInstance().Logit(TAG, "getUserInfo");


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();

            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            params.put(Constants.PARAM_AC_NAME, "" + payeename);
            params.put(Constants.PARAM_BANK_AC, "" + acname);
            params.put(Constants.PARAM_IFSC, "" + ifsc);
            params.put(Constants.PARAM_REQUEST_ID, "" + request_id);


            if (!cancelChequeImagepath.equalsIgnoreCase("")) {
                File imagefile = new File(cancelChequeImagepath);
                Log.e("image_captured", imagefile.toString());
                cheque_image_part = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagefile));

                //   auxFile = null;

            } else if (auxFile != null) {


                //    cheque_image_part = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, auxFile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), auxFile));
                //  final File file = new File(mediaPath);
                Log.e("image_captured", auxFile.toString());
                RequestBody reqFile_userimage = RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), auxFile);

                cheque_image_part = MultipartBody.Part.createFormData(Constants.PARAM_CANCELLED_CHEQUE, auxFile.getName(), reqFile_userimage);
            }


            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            progressDialog.setMessage("Uploading cheque copy. Please wait...");
            progressDialog.show();

            Call<CommonResponse> callUpload = ApiClient.getClient().create(ApiInterface.class).getStoreChequeUpdated("Bearer " + sessionManager.getToken(), params, cheque_image_part);
            callUpload.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {

                        CommonResponse chequeDetail = response.body();

                        if (chequeDetail.getResponseCode() == 200) {
                            DialogUtil.dismiss();
                            showAlert(response.body().getResponse());
                        }
                        if (chequeDetail.getResponseCode() == 411) {
                            sessionManager.logoutUser(getActivity());
                        }
                        if (chequeDetail.getResponseCode() == 400) {
                            progressDialog.dismiss();
                            if (chequeDetail.getValidation() != null) {
                                Validation validation = chequeDetail.getValidation();
                                if (validation.getIfsc() != null && validation.getIfsc().length() > 0) {
                                    DialogUtil.etIfscCode.setError(validation.getIfsc());
                                    DialogUtil.etIfscCode.requestFocus();
                                    // return false;
                                }
                                if (validation.getBankAc() != null && validation.getBankAc().length() > 0) {
                                    DialogUtil.etChequeNumber.setError(validation.getBankAc());
                                    DialogUtil.etChequeNumber.requestFocus();

                                }
                                if (validation.getAcName() != null && validation.getAcName().length() > 0) {
                                    DialogUtil.etPayeeName.setError(validation.getAcName());
                                    DialogUtil.etPayeeName.requestFocus();

                                    // Toast.makeText(getActivity(),validation.getAgentId(),Toast.LENGTH_LONG).show();
                                }
                                if (validation.getProccessId() != null && validation.getProccessId().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getProccessId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getAgentId() != null && validation.getAgentId().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getAgentId(), Toast.LENGTH_LONG).show();
                                }
                                if (validation.getRequestId() != null && validation.getRequestId().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getRequestId(), Toast.LENGTH_LONG).show();

                                }
                                if (validation.getCancelledCheque() != null && validation.getCancelledCheque().length() > 0) {
                                    Toast.makeText(getActivity(), validation.getCancelledCheque(), Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(getActivity(), chequeDetail.getStatus(), Toast.LENGTH_LONG).show();
                            }

                            // ErrorResponsePanCard errorResponsePanCard = response.body();
                        } else {
                            Toast.makeText(mcontext, chequeDetail.getResponse(), Toast.LENGTH_LONG).show();

                            //Constants.showAlert(edit_ac_name.getRootView(), chequeDetail.getResponse(), true);
                        }

                    } else {
                        Toast.makeText(getActivity(), "#errorcode 2041 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "#errorcode 2041 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }

    private void showAlert(String msg) {
       /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Cheque Details");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

               *//* Intent data = new Intent();
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();*//*

                //  getActivity().onBackPressed();
                commanFragmentCallWithoutBackStack(new ChequeDataListingFragment(), vendor);
//                startActivity(new Intent(getActivity(),DashboardActivity.class));
            }
        });
        alertDialog.show();
*/

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
        tvTitle.setText("Cheque Details");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                submitocr(accountNumber, payeeName, ifscCode);
                commanFragmentCallWithoutBackStack(new ChequeDataListingFragment(), vendor);

            }
        });

        dialog.setCancelable(false);

        dialog.show();
    }


    private void submitocr(String accNumber, String name, String ifsc) {
        HashMap<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_BACK_ACCOUNT_NUMBER, accNumber);
        param.put(Constants.PARAM_BACK_ACCOUNT_HOLDER_NAME, name);
        param.put(Constants.PARAM_BACK_ACCOUNT_IFSC_CODE, ifsc);
        param.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        param.put(Constants.PARAM_PROCESS_ID, str_process_id);

        Call<PanCardSubmitResponse> call = ApiClient.getClient2().create(ApiInterface.class).SubmitChequeOCR(param);
        call.enqueue(new Callback<PanCardSubmitResponse>() {
            @Override
            public void onResponse(Call<PanCardSubmitResponse> call, retrofit2.Response<PanCardSubmitResponse> response) {
                if (response.isSuccessful()) {
                    PanCardSubmitResponse panCardDetail = response.body();
                    if (panCardDetail.getStatus().equalsIgnoreCase("success")) {
                        // Toast.makeText(getActivity(), panCardDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), panCardDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode :- 2027 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<PanCardSubmitResponse> call, Throwable t) {
                //    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "#errorcode :- 2027 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }


    public void getBankDetails(String ifsc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        params.put(Constants.PARAM_IFSC, ifsc);

        Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getBankDetailsFromIfsc("Bearer " + sessionManager.getToken(), params);
        callUpload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.has("responseCode")) {
                                if (jsonObject.getInt("responseCode") == 200) {
                                    JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                    edit_bank_name.setText(result.getString("bank_name").toString());
                                    edit_bank_branch_name.setText(result.getString("bank_branch_name").toString());
                                    edit_bank_address.setText(result.getString("bank_address").toString());
                                } else if (jsonObject.getInt("responseCode") == 405) {
                                    sessionManager.logoutUser(mcontext);
                                } else {
                                }
                            }
                        } else {
                            Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "#errorcode 2053" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    /* public void deleteFileFromMediaManager(Context context, String path) {
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
    public void commanFragmentCallWithBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendor);

        if (cFragment != null) {


            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();

        bundle.putSerializable("data", vendor);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.rvMain, cFragment);
            fragmentTransaction.commit();

        }
    }


 /*   @Override
    public void onResume() {

        if (!AppApplication.networkConnectivity.isNetworkAvailable()) {

            Constants.ShowNoInternet(mcontext);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    deleteImages();

                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });


        super.onResume();
    }*/

    private void deleteImages() {

        File casted_image = new File(cancelChequeImagepath);
        if (casted_image.exists()) {
            casted_image.delete();
        }

        File casted_image6 = new File(imagecamera);
        if (casted_image6.exists()) {
            casted_image6.delete();
        }

    }


    private void ChequeOCR() {
        progressDialog.setMessage("Please wait we are fetching details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        MultipartBody.Part typedFile = null;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_APP_NAME, Constants.APP_NAME);
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        File imagefile = new File(cancelChequeImagepath);
        typedFile = MultipartBody.Part.createFormData("image", imagefile.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(cancelChequeImagepath)), imagefile));//RequestBody.create(MediaType.parse("image"), new File(mProfileBitmapPath));

        Call<GetOCRChequeDetails> call = ApiClient.getClient2().create(ApiInterface.class).getChequeDetails(params, typedFile);
        call.enqueue(new Callback<GetOCRChequeDetails>() {
            @Override
            public void onResponse(Call<GetOCRChequeDetails> call, Response<GetOCRChequeDetails> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetOCRChequeDetails getOCRChequeDetails = response.body();
                    if (getOCRChequeDetails.getReSelectImage().equalsIgnoreCase("0")) {
                        ifscCode = getOCRChequeDetails.getChequeDetail().getIfscCode();
                        accountNumber = getOCRChequeDetails.getChequeDetail().getBankAccountNumber();

                        payeeName = getOCRChequeDetails.getChequeDetail().getBankAccountHolderName();
                    } else if (getOCRChequeDetails.getReSelectImage().equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), getOCRChequeDetails.getMessage(), Toast.LENGTH_SHORT).show();
                        cancelChequeImagepath = "";
                        Glide.with(getActivity()).load(cancelChequeImagepath).placeholder(R.drawable.placeholder)
                                .into(ivChequeImage);
                        isPanSelected = false;
                        btn_cheque_card.setVisibility(View.VISIBLE);
                        btn_cheque_card_select.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), getOCRChequeDetails.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2989 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOCRChequeDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "#errorcode 2989 " + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
