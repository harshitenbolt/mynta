package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.RateTypeBean;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.R;

import com.canvascoders.opaper.activity.ProcessInfoActivity;
import com.canvascoders.opaper.adapters.RateListAdapter;
import com.canvascoders.opaper.adapters.RejectedStoreListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.activity.AgreementDetailActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
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

import static com.canvascoders.opaper.utils.Constants.showAlert;

public class RateFragment extends Fragment implements View.OnClickListener {

    private String TAG = "RateFragment";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    private FloatingActionButton btn_next;
    private Button btn_skip_to_addendum;
    //    private EditText edit_rate;
//    private Toolbar toolbar;
    private TextView txt_notice;
    Context mcontext;
    View view;
    String str_process_id;
    RateListAdapter rateListAdapter;
    ArrayList<StoreTypeBean> rateTypeBeans;
    RecyclerView recyclerView;
    VendorList vendor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rate_new, container, false);

        mcontext = this.getActivity();
        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        initView();
        OTPActivity.settitle(Constants.TITLE_RATE_UPDATE);
        return view;

    }

    private void initView() {

        rateTypeBeans = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_rate_type);
        btn_next = (FloatingActionButton) view.findViewById(R.id.btn_next);
        txt_notice = (TextView) view.findViewById(R.id.txt_notice);
//        edit_rate = (EditText) view.findViewById(R.id.edit_rate);
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait submitting Rate details");
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        btn_next.setOnClickListener(this);

        btn_skip_to_addendum = view.findViewById(R.id.btn_skip_to_addendum);
        btn_skip_to_addendum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkipToAgreementDialog();
            }
        });

        LinearLayoutManager approvedLinearLayoutManager = new LinearLayoutManager(getContext());
        approvedLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(approvedLinearLayoutManager);


        getStoreListing(str_process_id);
//        edit_rate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() > 0 && Integer.parseInt(edit_rate.getText().toString()) <= 18) {
//                    btn_next.setVisibility(View.VISIBLE);
//                } else {
//                    btn_next.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    private void showSkipToAgreementDialog() {

        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder.setTitle("Approval Pending");
        alertDialogBuilder.setMessage("Are you sure? You want to proceed to Agreement ?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
               /* Intent i = new Intent(getActivity(), AgreementDetailActivity.class);
                startActivity(i);*/
                Intent i = new Intent(getActivity(), ProcessInfoActivity.class);
                startActivity(i);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void getStoreListing(String proccessId) {
        JsonObject dataObj = new JsonObject();
        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, str_process_id);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());


        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).getStoreTypeListing("Bearer "+sessionManager.getToken(),dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
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
                                            btn_skip_to_addendum.setVisibility(View.VISIBLE);
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


                                    rateListAdapter = new RateListAdapter(rateTypeBeans, getContext());
                                    recyclerView.setAdapter(rateListAdapter);
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {
                                    showDialogApproval(jsonObject.getString("response"));
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(getContext());
                                }
                            } else if (response.code() == 202) {
                                showDialogApproval(response.message());
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(getContext());
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getContext(), "Server not responding", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
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
                checkAndPrepareObj();
            } else {
                Constants.ShowNoInternet(mcontext);
            }

        }

    }

    private void checkAndPrepareObj() {
        JsonArray jsonArray = new JsonArray();
        // checking from neutral stores to get updated data.
        for (int i = 0; i < rateTypeBeans.size(); i++) {

            if (rateTypeBeans.get(i).isSelected() && !rateTypeBeans.get(i).getIsApproved().equalsIgnoreCase("1")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("store_type", rateTypeBeans.get(i).getStoreTypeId());
                if (!rateTypeBeans.get(i).getStoreType().contains(Constants.CAC_STORE)) {
                    if (rateTypeBeans.get(i).getRate() != null && rateTypeBeans.get(i).getRate().length() > 0)
                        try {
//                        float rate = Float.parseFloat(rateTypeBeans.get(i).getRate());
//                        if(rate>0)
                            jsonObject.addProperty("rate", "" + rateTypeBeans.get(i).getRate());
//                        else
//                        {
//                            Toast.makeText(getContext(), "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    else {
                        Toast.makeText(mcontext, "Issue with Rate:" + rateTypeBeans.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    jsonObject.addProperty("rate", "0");
                }
                jsonArray.add(jsonObject);
            }
        }

        if (jsonArray.size() <= 0) {
            Toast.makeText(mcontext, "Nothing to Update or press Skip to sign addendum", Toast.LENGTH_LONG).show();
            return;
        }
        submitStoreUpdateDetails(jsonArray);
    }

    private void submitStoreUpdateDetails(JsonArray jsonArray) {
        JsonObject dataObj = new JsonObject();

        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, str_process_id);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.add(Constants.KEY_STORES, jsonArray);

        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).setStoreTypeListing("Bearer "+sessionManager.getToken(),dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                Log.e("REsponse code", "" + response.code());
                Log.e("REspomse msg", "" + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().toString();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (response.code() == 200) {
                                if (jsonObject.getString("responseCode").equalsIgnoreCase("200")) {
                                   /* Intent i = new Intent(getActivity(), AgreementDetailActivity.class);
                                    startActivity(i);*/
                                    Intent i = new Intent(getActivity(),ProcessInfoActivity.class);
                                    startActivity(i);
                                    getActivity().finish();

                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {
                                    showDialogApproval(jsonObject.getString("response"));
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(mcontext);
                                }
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(mcontext);
                            } else if (response.code() == 202) {
                                showDialogApproval(response.message());
                            } else {
                                Toast.makeText(mcontext, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
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
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showDialogApproval(String Msg) {
        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder.setTitle("Approval Pending");
        alertDialogBuilder.setMessage(Msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                TaskCompletedFragment2 taskCompletedFragment2 = new TaskCompletedFragment2();
                taskCompletedFragment2.setMesssge(Msg);
                commanFragmentCallWithoutBackStack(taskCompletedFragment2);
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    public void rateDetailsSubmit(final View v) {
//
//        mProgressDialog.show();
//        JsonObject user = new JsonObject();
//        user.addProperty(Constants.PARAM_PROCESS_ID, str_process_id);
//        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
//        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
//        user.addProperty(Constants.PARAM_RATE, edit_rate.getText().toString().trim());
//
//
//        ApiClient.getClient().create(ApiInterface.class).submitRate(user).enqueue(new Callback<GetUserDetailResponse>() {
//            @Override
//            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
//                mProgressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    GetUserDetailResponse getUserDetailResponse = response.body();
//                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
//
//                    if (getUserDetailResponse.getResponseCode() == 200) {
//                        Mylogger.getInstance().Logit(TAG, "" + getUserDetailResponse.getData().get(0).getProccessId());
//                        str_process_id = getUserDetailResponse.getData().get(0).getProccessId();
//
//                        Intent i = new Intent(getActivity(), AgreementDetailActivity.class);
//                        startActivity(i);
//                        getActivity().finish();
//
//                    } else if (getUserDetailResponse.getResponseCode() == 405) {
//                        sessionManager.logoutUser(mcontext);
//                    } else if (getUserDetailResponse.getResponseCode() == 202) {
//
//
//                        txt_notice.setText(getUserDetailResponse.getResponse());
//
//                        commanFragmentCallWithoutBackStack(new TaskCompletedFragment2());
//
//                    } else {
//                        Toast.makeText(mcontext, getUserDetailResponse.getResponse(), Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    showAlert(v, getString(R.string.something_went_wrong), false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
//                mProgressDialog.dismiss();
//                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }


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
}
