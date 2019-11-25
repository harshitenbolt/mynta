package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GetGstListing.Datum;
import com.canvascoders.opaper.Beans.GetGstListing.GetGstListing;
import com.canvascoders.opaper.Beans.VendorDetailResponse.VendorDetailResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.GSTListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GstListingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUpdateGst;
    RecyclerView rvGSTl;
    private ImageView ivBack;
    GSTListAdapter gstListAdapter;
    VendorList vendor;
    String proccess_id = "";
    ProgressDialog progressDialog;
    List<Datum> list = new ArrayList<>();
    SessionManager sessionManager;
    TextView tvNodate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_listing);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        proccess_id = getIntent().getStringExtra("data");
        ApiCallGetDetailofVendor();
        init();
    }

    private void init() {

        btnUpdateGst = findViewById(R.id.btnSubmit);
        btnUpdateGst.setOnClickListener(this);
        rvGSTl = findViewById(R.id.rvGSTDetails);
        ivBack = findViewById(R.id.iv_back_process);
        tvNodate = findViewById(R.id.tvNodata);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ApiCallGetGSt();
    }


    private void ApiCallGetDetailofVendor() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_PROCESS_ID, proccess_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).vendorDetailResponse("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<VendorDetailResponse>() {
            @Override
            public void onResponse(Call<VendorDetailResponse> call, Response<VendorDetailResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    VendorDetailResponse vendorDetailResponse = response.body();
                    vendor = vendorDetailResponse.getData().get(0);
                    init();
                }

            }

            @Override
            public void onFailure(Call<VendorDetailResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }


    private void ApiCallGetGSt() {
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, String.valueOf(vendor));
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        Call<GetGstListing> call = ApiClient.getClient().create(ApiInterface.class).getgstListing("Bearer " + sessionManager.getToken(), params);

        call.enqueue(new Callback<GetGstListing>() {
            @Override
            public void onResponse(Call<GetGstListing> call, Response<GetGstListing> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetGstListing getGstListing = response.body();
                    if (getGstListing.getResponseCode() == 200) {
                        list.clear();
                        list.addAll(getGstListing.getData().get(0));
                        if (list.size() > 0) {
                            gstListAdapter = new GSTListAdapter(GstListingActivity.this, list);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GstListingActivity.this, RecyclerView.VERTICAL, false);

                            rvGSTl.setLayoutManager(horizontalLayoutManager);
                            rvGSTl.setAdapter(gstListAdapter);
                            tvNodate.setVisibility(View.GONE);
                        } else {
                            Intent i = new Intent(GstListingActivity.this, EditGSTActivity.class);
                            i.putExtra("data", vendor.getProccessId());
                            startActivity(i);
                            tvNodate.setVisibility(View.VISIBLE);
                        }

                    } else if (getGstListing.getResponseCode() == 403) {
                        Intent i = new Intent(GstListingActivity.this, EditGSTActivity.class);
                        i.putExtra("data", vendor.getProccessId());
                        startActivity(i);
                        finish();
                        tvNodate.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(GstListingActivity.this, getGstListing.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetGstListing> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(GstListingActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                Intent i = new Intent(GstListingActivity.this, EditGSTActivity.class);
                i.putExtra("data", vendor.getProccessId());
                startActivity(i);
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        ApiCallGetDetailofVendor();
        ApiCallGetGSt();

    }
}
