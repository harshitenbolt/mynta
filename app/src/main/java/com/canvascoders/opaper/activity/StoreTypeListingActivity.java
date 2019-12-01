package com.canvascoders.opaper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.MensaAlteration;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.ApprovedStoreListAdapter;
import com.canvascoders.opaper.adapters.NeutralStoreListAdapter;
import com.canvascoders.opaper.adapters.NoOFInvoicesListAdapter;
import com.canvascoders.opaper.adapters.RejectedStoreListAdapter;
import com.canvascoders.opaper.adapters.StoreReListingAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.TaskCompletedFragment2;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class StoreTypeListingActivity extends AppCompatActivity implements RecyclerViewClickListener {  //implements NavigationView.OnNavigationItemSelectedListener

    NeutralStoreListAdapter neutralstoreListAdapter;
    RejectedStoreListAdapter rejectedStoreListAdapter;
    ApprovedStoreListAdapter approvedStoreListAdapter;

    StoreReListingAdapter storeReListingAdapter;
    private List<VendorList> vendorLists = new ArrayList<>();
    JSONObject object = new JSONObject();
    private String apiName = "completed-vendors";
    private ProgressDialog progressDialog;
    String TAG = "VendorLis";
    SessionManager sessionManager;
    static TextView tv_title;
    ImageView iv_back;

    String alterationselected = "";
    Toolbar toolbar;

    String vendor = "";
    ArrayList<StoreTypeBean> approvedStoreList;
    ArrayList<StoreTypeBean> rejectedStoreList;
    ArrayList<StoreTypeBean> neutralStoreList;
    private static Dialog dialog;
    Button btn_update_store_details, btn_skip_to_addendum;

    LinearLayout llApproved, llRejected, llNeutral;
    RecyclerView rv_approved, rv_rejected, rv_neutral;
    private ArrayList<MensaAlteration> mensaalterationList = new ArrayList<>();
    Boolean editFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storetype_main);

        vendor = getIntent().getStringExtra("data");
        editFlag = getIntent().getBooleanExtra("editFlag", false);
        init();

        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        } catch (JSONException e) {
        }
        apiName = "completed-vendors";
        progressDialog.setMessage("please wait ...");


//        rv_store_list.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rv_store_list.setLayoutManager(linearLayoutManager);


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            getStoreListing(vendor);
        } else {
            Constants.ShowNoInternet(this);
        }

    }


    public void init() {
        approvedStoreList = new ArrayList<>();
        rejectedStoreList = new ArrayList<>();
        neutralStoreList = new ArrayList<>();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llApproved = findViewById(R.id.llApproved);
        llRejected = findViewById(R.id.llRejected);
        llNeutral = findViewById(R.id.llNeutral);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv_approved = findViewById(R.id.rv_approved_store_list);
        rv_rejected = findViewById(R.id.rv_rejected);
        rv_neutral = findViewById(R.id.rv_neutral_store_list);

        rv_approved.setHasFixedSize(true);
        LinearLayoutManager approvedLinearLayoutManager = new LinearLayoutManager(this);
        approvedLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_approved.setLayoutManager(approvedLinearLayoutManager);

        rv_rejected.setHasFixedSize(true);
        LinearLayoutManager rejectedLinearLayoutManager = new LinearLayoutManager(this);
        rejectedLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_rejected.setLayoutManager(rejectedLinearLayoutManager);

        rv_neutral.setHasFixedSize(true);
        LinearLayoutManager neutralLinearLayoutManager = new LinearLayoutManager(this);
        neutralLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_neutral.setLayoutManager(neutralLinearLayoutManager);

        //
//        llNeutral.setVisibility(View.GONE);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle(Constants.TITLE_AGREEMENT);

        tv_title = findViewById(R.id.tv_title);
//        tv_title.setText(Constants.TITLE_AGREEMENT);

        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        btn_update_store_details = findViewById(R.id.btn_update_store_details);
        btn_update_store_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndPrepareObj();
            }
        });
        btn_skip_to_addendum = findViewById(R.id.btn_skip_to_addendum);
        btn_skip_to_addendum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddendum();
            }
        });

    }


    //    {"token":"p1Rdc","proccess_id":"305","agent_id":"3","stores":[{"store_type":"1","rate":"8"},{"store_type":"2","rate":"15"}]}
//if you send rate update from profile then send one extra parameter with above array, it's name 'is_rate_update_from_profile' = 1
    private void checkAndPrepareObj() {
        JsonArray jsonArray = new JsonArray();

        // checking from neutral stores to get updated data.
        for (int i = 0; i < neutralStoreList.size(); i++) {
            if (neutralStoreList.get(i).isSelected()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("store_type", neutralStoreList.get(i).getStoreTypeId());
                if (!neutralStoreList.get(i).getStoreType().contains(Constants.CAC_STORE) && !neutralStoreList.get(i).getStoreType().contains(Constants.ASSISTED)) {

                    if (!neutralStoreList.get(i).getStoreType().contains("Mensa - Alteration")) {

                        if (neutralStoreList.get(i).getRate() != null && neutralStoreList.get(i).getRate().length() > 0) {
                            try {
//                        float rate = Float.parseFloat(neutralStoreList.get(i).getRate());
//                        if (rate > 0)
                                if (neutralStoreList.get(i).getRate().equalsIgnoreCase("0") || neutralStoreList.get(i).getRate().equalsIgnoreCase("0.0")) {
                                    Toast.makeText(this, "Please enter valid rate", Toast.LENGTH_SHORT).show();
                                } else {
                                    jsonObject.addProperty("rate", "" + neutralStoreList.get(i).getRate());
                                }

//                        else {
//                            Toast.makeText(StoreTypeListingActivity.this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(StoreTypeListingActivity.this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                    jsonObject.addProperty("rate", neutralStoreList.get(i).getRate());
                        } else {
                            Toast.makeText(StoreTypeListingActivity.this, "Issue with rate:" + neutralStoreList.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                            return;

                        }

                    } else {
                        jsonObject.addProperty("rate", "0");
                        jsonObject.addProperty("sub_store_type", alterationselected);
                    }
                } else {  // ITS s CAC store send rate "0"
                    jsonObject.addProperty("rate", "0");
                }
                jsonArray.add(jsonObject);
            }
        }

        // checking from rejected stores to get updated data.
        for (int i = 0; i < rejectedStoreList.size(); i++) {
            if (rejectedStoreList.get(i).isSelected()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("store_type", rejectedStoreList.get(i).getStoreTypeId());
                if (rejectedStoreList.get(i).getRate() != null && rejectedStoreList.get(i).getRate().length() > 0) {
                    if (rejectedStoreList.get(i).getRate().equals("0")) {
                        Constants.showAlert(getWindow().getDecorView(), "Please enter valid amount", false);
                        Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                    } else
                        jsonObject.addProperty("rate", rejectedStoreList.get(i).getRate());
                } else {
                    Toast.makeText(StoreTypeListingActivity.this, "Issue with rate:" + rejectedStoreList.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                    return;
                }
                jsonArray.add(jsonObject);
            }

        }


        if (jsonArray.size() <= 0) {
            Toast.makeText(this, "Nothing to Update or press Skip to sign adendum", Toast.LENGTH_LONG).show();
            return;
        }
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            submitStoreUpdateDetails(jsonArray);
        } else {
            Constants.ShowNoInternet(this);
        }

    }

    private void submitStoreUpdateDetails(JsonArray jsonArray) {
        progressDialog.show();
        JsonObject dataObj = new JsonObject();

        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, vendor);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        dataObj.addProperty("is_rate_update_from_profile", 1);
        dataObj.add(Constants.KEY_STORES, jsonArray);

        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).setStoreTypeListing("Bearer " + sessionManager.getToken(), dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().toString();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (response.code() == 200) {
                                if (jsonObject.getString("responseCode").equalsIgnoreCase("200")) {
                                    JSONObject dataObj = jsonObject.getJSONArray("data").getJSONObject(0);
                                    if (dataObj.has("next_screen")) {
                                        startAddendum();
                                    } else {
                                        showDialogApproval(jsonObject.getString("response"));
                                    }
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {

                                   /* Intent i = new Intent(StoreTypeListingActivity.this, AddendumSignPendingActivity.class);
                                    startActivity(i);*/
                                    showDialogApproval(jsonObject.optString("response"));
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(StoreTypeListingActivity.this);
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                                    sessionManager.logoutUser(StoreTypeListingActivity.this);
                                } else {
                                    showDialogApproval2(jsonObject.optString("response"));
                                }
                            } else if (response.code() == 202) {

                              /*  Intent i = new Intent(StoreTypeListingActivity.this, AddendumSignPendingActivity.class);
                                startActivity(i);*/

                                showDialogApproval(jsonObject.optString("message"));
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(StoreTypeListingActivity.this);
                            } else {
                                Toast.makeText(StoreTypeListingActivity.this, response.message(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(StoreTypeListingActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showDialogApproval(String Msg) {
        // Display message in dialog box if you have not internet connection
       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreTypeListingActivity.this);
        alertDialogBuilder.setTitle("Approval Pending");
        alertDialogBuilder.setMessage(Msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
                arg0.dismiss();
                lkdsajdsaj
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
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

        dialog = new Dialog(StoreTypeListingActivity.this);
        dialog = new Dialog(StoreTypeListingActivity.this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Approval pending");

        tvMessage.setText(Msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskCompletedFragment2 taskCompletedFragment2 = new TaskCompletedFragment2();
                taskCompletedFragment2.setMesssge(Msg);
                commanFragmentCallWithoutBackStack(taskCompletedFragment2);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);

        dialog.show();
    }


    public void showDialogApproval2(String Msg) {
        // Display message in dialog box if you have not internet connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreTypeListingActivity.this);
        alertDialogBuilder.setTitle("Error !!");
        alertDialogBuilder.setMessage(Msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void getStoreListing(String proccessId) {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        JsonObject dataObj = new JsonObject();
        dataObj.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        dataObj.addProperty(Constants.KEY_PROCESS_ID, vendor);
        dataObj.addProperty(Constants.KEY_AGENT_ID, sessionManager.getAgentID());
        if (editFlag) {
            dataObj.addProperty(Constants.KEY_IS_EDIT_ADDENDUM_FLAG, 1);
        }


        Retrofit retrofit = ApiClient.getClient();
        retrofit.create(ApiInterface.class).getOldStoreTypeListing("Bearer " + sessionManager.getToken(), dataObj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
/////
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().toString();
                        Mylogger.getInstance().Logit(TAG, res);
                        if (!TextUtils.isEmpty(res)) {
                            JSONObject jsonObject = new JSONObject(response.body().string());

                            if (response.code() == 200) {
                                Gson gson = new Gson();
                                if (jsonObject.getString("responseCode").equalsIgnoreCase("200")) {
                                    if (jsonObject.has("next_screen")) {
                                        Toast.makeText(StoreTypeListingActivity.this, jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                                        startAddendum();
                                    } else {
                                        JSONArray storeJsonArray = jsonObject.getJSONArray("data");
                                        JSONObject result = jsonObject.getJSONObject("Mensa - Alteration");

                                       /* for (int i = 0; i < result.length(); i++) {
                                            JSONObject o = result.getJSONObject(i);
                                            MensaAlteration mensalist = gson.fromJson(o.toString(), MensaAlteration.class);
                                            MensaAlteration mensaAlteration = new MensaAlteration(true, "", "");
                                            mensaAlteration.setSubStoreType(mensalist.getSubStoreType());
                                            mensaAlteration.setSubStoreTypeId(mensalist.getSubStoreTypeId());
                                            mensaalterationList.add(mensaAlteration);
                                        }*/
                                        //mensaJsonArray = get

                                        Map<String, String> yourHashMap = new Gson().fromJson(result.toString(), HashMap.class);



                                        Log.e("Found_Stores", "" + yourHashMap.toString());

                                        for (int i = 0; i < storeJsonArray.length(); i++) {
                                            StoreTypeBean storeTypeBean = new StoreTypeBean(storeJsonArray.getJSONObject(i));

                                            neutralStoreList.add(storeTypeBean);
//                                        if (storeTypeBean.getIsApproved().equalsIgnoreCase("0"))  //  Not added now/ or pending
//                                        {
//                                            neutralStoreList.add(storeTypeBean);
//                                        } else if (storeTypeBean.getIsApproved().equalsIgnoreCase("1")) {  // Approved dont change anything
//                                            approvedStoreList.add(storeTypeBean);
//                                        } else if (storeTypeBean.getIsApproved().equalsIgnoreCase("2")) {  // Rejected
//                                            rejectedStoreList.add(storeTypeBean);
//                                        }
                                        }

                                        if (neutralStoreList.size() > 0) {
                                            llNeutral.setVisibility(View.VISIBLE);
                                            storeReListingAdapter = new StoreReListingAdapter(neutralStoreList, yourHashMap, StoreTypeListingActivity.this, StoreTypeListingActivity.this);
                                            rv_neutral.setAdapter(storeReListingAdapter);
                                        }
//                                    if (rejectedStoreList.size() > 0) {
//                                        llRejected.setVisibility(View.VISIBLE);
//                                        rejectedStoreListAdapter = new RejectedStoreListAdapter(rejectedStoreList, StoreTypeListingActivity.this);
//                                        rv_rejected.setAdapter(rejectedStoreListAdapter);
//                                    }
//                                    if (approvedStoreList.size() > 0) {
//                                        llApproved.setVisibility(View.VISIBLE);
//                                        approvedStoreListAdapter = new ApprovedStoreListAdapter(approvedStoreList, StoreTypeListingActivity.this);
//                                        rv_approved.setAdapter(approvedStoreListAdapter);
//
//                                    }
                                    }
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("202")) {

                                    if (jsonObject.has("last_addendum_sign")) {
                                        Intent i = new Intent(StoreTypeListingActivity.this, AddendumSignPendingActivity.class);
                                        i.putExtra("data", vendor);
                                        i.putExtra("message", jsonObject.getString("response"));
                                        i.putExtra("count", jsonObject.getString("no_of_time_addendum_sign"));
                                        i.putExtra("is_esign", jsonObject.getString("is_esign"));
                                        i.putExtra("last_signed", jsonObject.getString("last_addendum_sign"));
                                        startActivity(i);
                                        finish();
                                    } else {
                                        showDialogApproval(jsonObject.optString("response"));
                                    }
                                    //   dialog.dismiss();


                                    //
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("405")) {
                                    sessionManager.logoutUser(StoreTypeListingActivity.this);
                                } else if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                                    sessionManager.logoutUser(StoreTypeListingActivity.this);
                                }

                            } else if (response.code() == 202) {
                                Intent i = new Intent(StoreTypeListingActivity.this, AddendumSignPendingActivity.class);
                                startActivity(i);

                                //showDialogApproval(jsonObject.optString("message"));
                            } else if (response.code() == 405) {
                                sessionManager.logoutUser(StoreTypeListingActivity.this);
                            } else {
                                Toast.makeText(StoreTypeListingActivity.this, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(StoreTypeListingActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startAddendum() {
        Intent intent = new Intent(StoreTypeListingActivity.this, AddendumEsignActivity.class);
        intent.putExtra("data", vendor);
        startActivity(intent);
        finish();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_profile) {
//
//            commanFragmentCallWithoutBackStack(new ProfileFragment());
//
//        }
//        if (id == R.id.nav_1) {
//            Intent i = new Intent(StoreTypeListingActivity.this, DashboardActivity.class);
//            startActivity(i);
//            finish();
//        }
//        if (id == R.id.nav_2) {
//
//            commanFragmentCallWithoutBackStack(new MobileFragment());
//
//        }
//        if (id == R.id.nav_3) {
//
//            commanFragmentCallWithoutBackStack(new InvoiceMainFragment());
//
//        }
//        if (id == R.id.nav_v) {
//
//            commanFragmentCallWithoutBackStack(new VenderListFragment());
//
//        }
//        if (id == R.id.nav_4) {
//            commanFragmentCallWithoutBackStack(new NotificationFragment());
//
//        }
//        if (id == R.id.nav_5) {
//
//            commanFragmentCallWithoutBackStack(new ReportFragment());
//
//
//        }
//        if (id == R.id.nav_logout) {
//
//            SessionManager sessionManager = new SessionManager(StoreTypeListingActivity.this);
//            sessionManager.logoutUser(StoreTypeListingActivity.this);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//
//    }


//    public void commanFragmentCallWithoutBackStack(Fragment fragment) {
//
//        Fragment cFragment = fragment;
//
//
//        if (cFragment != null) {
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.content_main, cFragment);
//            fragmentTransaction.commit();
//
//        }
//    }

    public void commanFragmentCallWithoutBackStack(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.llMain, cFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {
        alterationselected = popup;

    }
}
