package com.canvascoders.opaper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.RateListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.TaskCompletedFragment2;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.GPSTracker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.maps.model.Dash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditRateWhileResigAgreeActivity extends AppCompatActivity {
    private String TAG = "RateFragment";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    String processId;
    ArrayList<StoreTypeBean> rateTypeBeans;
    RateListAdapter rateListAdapter;
    VendorList vendor;
    RecyclerView recyclerView;
    GPSTracker gps;
    ImageView ivBack;
    Button btSkip, btn_next;
    String lattitude, longitude;
    private static Dialog dialog;
    ProgressDialog mPogress;
    String s = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rate_while_resig_agree);
        sessionManager = new SessionManager(this);
        vendor = (VendorList) getIntent().getSerializableExtra("data");
        processId = String.valueOf(vendor.getProccessId());
        rateTypeBeans = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equalsIgnoreCase("2")) {
                    setResult(RESULT_OK, null);
                    finish();
                } else {

                    finish();
                    //  startActivity(new Intent(EditRateWhileResigAgreeActivity.this, DashboardActivity.class));
                }

            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        init();

    }

    private void init() {
        btSkip = findViewById(R.id.btn_skip_to_addendum);
        btn_next = findViewById(R.id.btn_next);
        recyclerView = (RecyclerView) findViewById(R.id.rv_rate_type);
        LinearLayoutManager approvedLinearLayoutManager = new LinearLayoutManager(this);
        approvedLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(approvedLinearLayoutManager);
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getStoreListing(processId);
        } else {
            Constants.ShowNoInternet(this);
        }


        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    //ApiCallResendAgreement();
                } else {
                    Constants.ShowNoInternet(EditRateWhileResigAgreeActivity.this);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    checkAndPrepareObj();
                } else {
                    Constants.ShowNoInternet(EditRateWhileResigAgreeActivity.this);
                }

            }
        });


    }


    private void getStoreListing(String proccessId) {
        mProgressDialog.show();
        JsonObject dataObj = new JsonObject();
        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, proccessId);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.addProperty(Constants.PARAM_IS_DOC, "0");


        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).getStoreTypeListing2("Bearer " + sessionManager.getToken(), dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    try {
                        String res = response.body().toString();
                        Log.e("REsponse", "" + response.body().toString());

                        Log.e("REsponse code", "" + response.code());
                        Log.e("REspomse msg", "" + response.body().charStream().toString());

                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (response.code() == 200) {
                                Log.e("in JSON", "" + jsonObject.toString());

                                if (jsonObject.getString("responseCode").equalsIgnoreCase("200")) {
                                    JSONArray rateJsonArray = jsonObject.getJSONArray("data");
                                    Log.e("Found Stores", "" + rateJsonArray.length());
                                    ArrayList<StoreTypeBean> tempList = new ArrayList<>();
                                    boolean isSecondTime = false;
                                    for (int i = 0; i < rateJsonArray.length(); i++) {
                                        StoreTypeBean rateTypeBean = new StoreTypeBean(rateJsonArray.getJSONObject(i));
                                        tempList.add(rateTypeBean);
                                        if (rateTypeBean.getIsApproved().equalsIgnoreCase("1") ||
                                                rateTypeBean.getIsApproved().equalsIgnoreCase("2")) {
                                            isSecondTime = true;
                                        }
                                        if (rateTypeBean.getIsApproved().equalsIgnoreCase("1")) {
                                            // btn_skip_to_addendum.setVisibility(View.VISIBLE);
                                        }
//                                        rateTypeBeans.add(rateTypeBean);
                                    }
                                    rateTypeBeans.clear();
                                    rateTypeBeans.addAll(tempList);
//                                    if (isSecondTime) {
//                                        if (tempList.get(5).getIsApproved().equalsIgnoreCase("2") ||
//                                                tempList.get(5).getIsApproved().equalsIgnoreCase("1")) {
//
//                                            tempList.get(5).setSelected(true);
//                                            rateTypeBeans.add(tempList.get(5));
//                                        } else {
//                                            tempList.remove(5);
//                                            for (int i = 0; i < tempList.size(); i++) {
//                                                if (tempList.get(i).getIsApproved().equalsIgnoreCase("2") ||
//                                                        tempList.get(i).getIsApproved().equalsIgnoreCase("1"))
//                                                    tempList.get(i).setSelected(true);
//                                            }
//                                            rateTypeBeans.addAll(tempList);
//                                        }
//                                    } else {
//
//                                        rateTypeBeans.addAll(tempList);
//                                    }


                                    rateListAdapter = new RateListAdapter(rateTypeBeans, EditRateWhileResigAgreeActivity.this);
                                    recyclerView.setAdapter(rateListAdapter);
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {
                                    showAlert(jsonObject.getString("response"));
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                                    sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                                }
                            } else if (response.code() == 202) {
                                showAlert(response.message());
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                            } else if (response.code() == 411) {
                                sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                            } else {
                                Toast.makeText(EditRateWhileResigAgreeActivity.this, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(EditRateWhileResigAgreeActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        mProgressDialog.dismiss();
                        e.printStackTrace();
                    } catch (IOException e) {
                        mProgressDialog.dismiss();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditRateWhileResigAgreeActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void checkAndPrepareObj() {
        // mProgressDialog.show();
        JsonArray jsonArray = new JsonArray();
        // checking from neutral stores to get updated data.
        for (int i = 0; i < rateTypeBeans.size(); i++) {

            if (rateTypeBeans.get(i).isSelected() && !rateTypeBeans.get(i).getIsApproved().equalsIgnoreCase("1")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("store_type", rateTypeBeans.get(i).getStoreTypeId());
                if (!rateTypeBeans.get(i).getStoreType().contains(Constants.CAC_STORE)&& !rateTypeBeans.get(i).getStoreType().contains(Constants.ASSISTED)) {
                    if (rateTypeBeans.get(i).getRate() != null && rateTypeBeans.get(i).getRate().length() > 0) {
                        if (!rateTypeBeans.get(i).getRate().equalsIgnoreCase("0") && !rateTypeBeans.get(i).getRate().equalsIgnoreCase("0.0")) {
                            try {
//                        float rate = Float.parseFloat(rateTypeBeans.get(i).getRate());
//                        if(rate>0)
                                jsonObject.addProperty("rate", "" + rateTypeBeans.get(i).getRate());
                                //  mProgressDialog.dismiss();

//                        else
//                        {
//                            Toast.makeText(getContext(), "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(this, "Issue with Rate:" + rateTypeBeans.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "Issue with Rate:" + rateTypeBeans.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    jsonObject.addProperty("rate", "0");
                }
                jsonArray.add(jsonObject);
            }
        }

        if (jsonArray.size() <= 0) {
            Toast.makeText(this, "Nothing to Update or press Skip to sign addendum", Toast.LENGTH_LONG).show();
            return;
        }
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            submitStoreUpdateDetails(jsonArray);
        } else {
            Constants.ShowNoInternet(this);
        }

    }


    private void submitStoreUpdateDetails(JsonArray jsonArray) {
        mProgressDialog.show();

        gps = new GPSTracker(this);
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

        JsonObject dataObj = new JsonObject();

        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, processId);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.addProperty(Constants.PARAM_LATITUDE, lattitude);
        dataObj.addProperty(Constants.PARAM_LONGITUDE, longitude);
        dataObj.add(Constants.KEY_STORES, jsonArray);

        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).submitRateUpdate("Bearer " + sessionManager.getToken(), dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                Log.e("REsponse code", "" + response.code());
                Log.e("REspomse msg", "" + response.body().toString());
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    try {
                        String res = response.body().toString();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (response.code() == 200) {
                                if (jsonObject.getString("responseCode").equalsIgnoreCase("200")) {
                                   /* Intent i = new Intent(getActivity(), AgreementDetailActivity.class);
                                    startActivity(i);*/

                                    finish();
                                    Toast.makeText(EditRateWhileResigAgreeActivity.this, jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                                    // showAlert1(jsonObject.getString("response"));


                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {
                                    showAlert(jsonObject.getString("response"));
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                                } else {
                                    Toast.makeText(EditRateWhileResigAgreeActivity.this, jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                                }
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(EditRateWhileResigAgreeActivity.this);
                            } else if (response.code() == 202) {
                                showAlert(response.message());
                            } else {
                                Toast.makeText(EditRateWhileResigAgreeActivity.this, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(EditRateWhileResigAgreeActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /////
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(EditRateWhileResigAgreeActivity.this, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
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

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new Dialog(EditRateWhileResigAgreeActivity.this);
        dialog = new Dialog(EditRateWhileResigAgreeActivity.this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Approval Pending");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskCompletedFragment2 taskCompletedFragment2 = new TaskCompletedFragment2();
                taskCompletedFragment2.setMesssge(msg);
                commanFragmentCallWithoutBackStack(taskCompletedFragment2);

                s = "2";
                dialog.dismiss();

            }
        });

        dialog.setCancelable(false);

        dialog.show();


    }


    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.llMain, cFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (s.equalsIgnoreCase("2")) {
            setResult(RESULT_OK, null);
            finish();
        } else {

            finish();
            //  startActivity(new Intent(EditRateWhileResigAgreeActivity.this, DashboardActivity.class));
        }
    }
}
