package com.canvascoders.opaper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.DelBoysNextScreenResponse.DelBoysNextResponse;
import com.canvascoders.opaper.Beans.DelBoysResponse.Datum;
import com.canvascoders.opaper.Beans.DelBoysResponse.DelBoyResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.DeliveryBoysListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.RateFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelBoyListingActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btAddDelBoy, btSubmit;
    View view;
    private ProgressDialog mProgressDialog;
    Context mcontext;
    private static Dialog dialog;
    private SessionManager sessionManager;
    private String str_process_id;
    private RecyclerView rvDelBoysList;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    DeliveryBoysListAdapter deliveryBoysListAdapter;
    private List<Datum> delivery_boys_list = new ArrayList<>();
    private int DEL_BOY = 100;
    ImageView ivClose;
    private LinearLayout llNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_boy_listing);
        sessionManager = new SessionManager(this);
        str_process_id = getIntent().getStringExtra("Data");
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        btAddDelBoy = findViewById(R.id.btAddDelBoy);
        btAddDelBoy.setOnClickListener(this);
        ivClose = findViewById(R.id.iv_back_process);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* btSubmit = findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(this);*/
        rvDelBoysList = findViewById(R.id.rvDelBoyList);
        llNoData = findViewById(R.id.llNoData);

        ApiCallGetLists();
    }

    private void ApiCallGetLists() {

        delivery_boys_list.clear();

        mProgressDialog.setTitle("Fetching delivery boys...");
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        Call<DelBoyResponse> call = ApiClient.getClient().create(ApiInterface.class).getDelBoys("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DelBoyResponse>() {
            @Override
            public void onResponse(Call<DelBoyResponse> call, Response<DelBoyResponse> response) {
                DelBoyResponse deliveryBoysList = response.body();
                try {
                    if (deliveryBoysList.getResponseCode() == 200) {
                        delivery_boys_list.clear();
                        mProgressDialog.dismiss();
//                        Toast.makeText(AddDeliveryBoysActivity.this,deliveryBoysList.getResponse(),Toast.LENGTH_SHORT).show();
                        delivery_boys_list.addAll(deliveryBoysList.getData());
                        //  deliveryBoysListAdapter.notifyDataSetChanged();
                    } else if (deliveryBoysList.getResponseCode() == 411) {
                        sessionManager.logoutUser(DelBoyListingActivity.this);
                    } else {
                        mProgressDialog.dismiss();
                        //  Toast.makeText(getActivity(), deliveryBoysList.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                    if (delivery_boys_list.isEmpty()) {


                        llNoData.setVisibility(View.VISIBLE);


                    } else {
                        llNoData.setVisibility(View.GONE);

                    }
                    deliveryBoysListAdapter = new DeliveryBoysListAdapter(delivery_boys_list, DelBoyListingActivity.this, "1");

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DelBoyListingActivity.this, LinearLayoutManager.VERTICAL, false);

                    rvDelBoysList.setLayoutManager(horizontalLayoutManager);
                    rvDelBoysList.setAdapter(deliveryBoysListAdapter);
                    deliveryBoysListAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(DelBoyListingActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(DelBoyListingActivity.this, "#errorcode :- 2049 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
               /* if (delivery_boys_list.isEmpty()) {
                    tv_note.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    tv_note.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                }*/
                Log.e("something", t.getMessage());
                t.getMessage();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAddDelBoy:
                Intent i = new Intent(DelBoyListingActivity.this, AddNewDeliveryBoy.class);
                i.putExtra("Data", "1");
                i.putExtra("process", str_process_id);
                startActivityForResult(i, DEL_BOY);
                break;


          /*  case R.id.btSubmit:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSubmit();
                }
                else{
                    Constants.ShowNoInternet(mcontext);
                }
                break;*/
        }
    }

    private void ApiCallSubmit() {

        mProgressDialog.setTitle("Please wait");
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        Call<DelBoysNextResponse> call = ApiClient.getClient().create(ApiInterface.class).completeDelBoy("Bearer " + sessionManager.getToken(), params);
        call.enqueue(new Callback<DelBoysNextResponse>() {
            @Override
            public void onResponse(Call<DelBoysNextResponse> call, Response<DelBoysNextResponse> response) {
                mProgressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        DelBoysNextResponse delBoysNextResponse = response.body();
                        if (delBoysNextResponse.getResponseCode() == 200) {
                            showAlert(delBoysNextResponse.getResponse());
                        } else if (delBoysNextResponse.getResponseCode() == 411) {
                            sessionManager.logoutUser(DelBoyListingActivity.this);
                        } else {
                            Toast.makeText(DelBoyListingActivity.this, delBoysNextResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DelBoyListingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(DelBoyListingActivity.this, "#errorcode 2050 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DelBoysNextResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(DelBoyListingActivity.this, "#errorcode 2050 " + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEL_BOY) {
            ApiCallGetLists();
            //    OTPActivity.settitle(Constants.TITLE_ADD_DEL_BOY);

        }
    }



/*
    private void showAlert(String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Delivery Boys");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                commanFragmentCallWithoutBackStack(new RateFragment());
            }
        });
        alertDialog.show();




    }*/

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

        dialog = new Dialog(mcontext);
        dialog = new Dialog(DelBoyListingActivity.this, R.style.DialogLSideBelow);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        btSubmit = dialog.findViewById(R.id.btSubmit);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Delivery Boys");

        tvMessage.setText(msg);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commanFragmentCallWithoutBackStack(new RateFragment());

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
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }

}
