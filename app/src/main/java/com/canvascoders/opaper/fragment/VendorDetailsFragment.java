package com.canvascoders.opaper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddDeliveryBoysActivity;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.ChangeMobileActivity;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.activity.StoreTypeListingActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class VendorDetailsFragment extends Fragment {

    private static final int CHEQUE_UPDATE_INTENT = 1001;
    VendorList vendor;
    TextView tv_ShopName, tv_Mobile, tv_Name, tvProcessId;
    ImageView ivVendorImage;
    Button btn_update_cheque, btn_update_delivery_boy,btn_update_mobile,btn_update_store_details,btn_resend_agreement;
    LinearLayout linear_check;
    String isUpdationRequired,allowDEdit,allowEditDelBoy;
    SessionManager sessionManager;
    View view;
    String process_id;
    Context mcontext;
    LinearLayout llEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_vendor_detail, container, false);

        mcontext = this.getActivity();
        sessionManager = new SessionManager(mcontext);
        DashboardActivity.settitle(Constants.TITLE_VENDOR_DETAIL);
        linear_check = view.findViewById(R.id.linear_cheq);
        tv_ShopName = view.findViewById(R.id.tv_shop_name);
        tv_Mobile = view.findViewById(R.id.tv_mobile);
        tv_Name = view.findViewById(R.id.tv_name);
        ivVendorImage = view.findViewById(R.id.iv_vendor_image);
        btn_update_cheque = view.findViewById(R.id.btn_update_cheque);
        btn_update_mobile =view.findViewById(R.id.btUpdateMobile);
        btn_update_delivery_boy = view.findViewById(R.id.btn_update_delivery_boy);
        btn_resend_agreement = view.findViewById(R.id.btn_resend_agreement);
        btn_update_store_details = view.findViewById(R.id.btn_update_store_details);
        tvProcessId = view.findViewById(R.id.tvProcessId);
        llEdit = view.findViewById(R.id.llEdit);
        vendor = (VendorList) getArguments().getSerializable("data");

        if (vendor != null) {

            tv_ShopName.setText(vendor.getStoreName());
            tv_Name.setText(vendor.getName());
            tv_Mobile.setText(vendor.getMobileNo());
            process_id = String.valueOf(vendor.getProccessId());
            tvProcessId.setText("Id:" + vendor.getProccessId());
            isUpdationRequired = vendor.getIsAgreementUpdationRequire();
            if(isUpdationRequired.equalsIgnoreCase("1")){
                btn_resend_agreement.setVisibility(View.VISIBLE);
            }
            allowDEdit= vendor.getAllowedit();
            allowEditDelBoy= vendor.getIsAddDeliveryBoy();
            if(allowDEdit.equalsIgnoreCase("1")){
                llEdit.setVisibility(View.VISIBLE);
            }
            if(allowEditDelBoy.equalsIgnoreCase("1")){
                llEdit.setVisibility(View.VISIBLE);
                btn_update_delivery_boy.setVisibility(View.VISIBLE);
            }

            Glide.with(this).load(vendor.getShopImage())
                    .placeholder(R.drawable.store_place)
                    .error(R.drawable.store_place)
                    .fallback(R.drawable.store_place)
                    .into(ivVendorImage);
            Log.e("VId", "" + vendor.getVendorId() + " ::" + vendor.getName());
        }


        btn_resend_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallResendAgreement();
                }
                else{
                    Constants.ShowNoInternet(getActivity());
                }
            }
        });
//        if (vendor.getBankDetailUpdationRequired().equalsIgnoreCase("1")) {
//            btn_update_cheque.setVisibility(View.VISIBLE);
//            linear_check.setVisibility(View.VISIBLE);
//        } else {
//            btn_update_cheque.setVisibility(View.GONE);
//            linear_check.setVisibility(View.GONE);
//        }

        btn_update_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commanFragmentCallWithBackStack(new ChequedataUpdateFragment(),vendor);
                commanFragmentCallWithBackStack(new ChequeDataListingFragment(), vendor);
            }
        });

        btn_update_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ChangeMobileActivity.class);
                i.putExtra(Constants.KEY_VENDOR_MOBILE,vendor);
                startActivity(i);
            }
        });

        // changes on 28/01
        btn_update_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mcontext, AddDeliveryBoysActivity.class);
                myIntent.putExtra("data", vendor);
                startActivity(myIntent);
            }
        });
        btn_update_store_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, StoreTypeListingActivity.class);
                myIntent.putExtra("data", vendor);
                startActivity(myIntent);
            }
        });
        return view;
    }

    private void ApiCallResendAgreement() {
        ApiClient.getClient().create(ApiInterface.class).ResignAgreement("Bearer "+sessionManager.getToken(),process_id).enqueue(new Callback<ResignAgreementResponse>() {
            @Override
            public void onResponse(Call<ResignAgreementResponse> call, Response<ResignAgreementResponse> response) {
                if(response.isSuccessful()) {
                    ResignAgreementResponse resignAgreementResponse = response.body();
                    if(resignAgreementResponse.getResponseCode()==200){
                        Toast.makeText(getActivity(), resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                    else if (resignAgreementResponse.getResponseCode()==411){
                        sessionManager.logoutUser(getActivity());
                    }
                    else{
                        Toast.makeText(getActivity(), resignAgreementResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResignAgreementResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            DashboardActivity.settitle(Constants.TITLE_VENDOR_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHEQUE_UPDATE_INTENT && resultCode == RESULT_OK) {
        }
    }

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

   /* public void commanFragmentCallWithoutBackStack(Fragment fragment, VendorList vendor) {

        Fragment cFragment = fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", vendor);

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_main, cFragment);
            fragmentTransaction.commit();

        }
    }*/

    @Override
    public void onResume() {

        super.onResume();
        // Set title
        Log.e("Something","Title");
        mcontext = this.getActivity();
        DashboardActivity.settitle(Constants.TITLE_VENDOR_DETAIL);
    }
}
