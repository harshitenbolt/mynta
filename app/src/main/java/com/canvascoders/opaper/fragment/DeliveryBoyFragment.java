package com.canvascoders.opaper.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.canvascoders.opaper.Beans.DelBoysNextScreenResponse.DelBoysNextResponse;
import com.canvascoders.opaper.Beans.DelBoysResponse.Datum;
import com.canvascoders.opaper.Beans.DelBoysResponse.DelBoyResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AddDeliveryBoysActivity;
import com.canvascoders.opaper.activity.AddNewDeliveryBoy;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.OTPActivity;
import com.canvascoders.opaper.adapters.DeliveryBoysAdapter;
import com.canvascoders.opaper.adapters.DeliveryBoysListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryBoyFragment extends Fragment implements View.OnClickListener {

    private Button btAddDelBoy,btSubmit;
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
    private LinearLayout llNoData;
    public DeliveryBoyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_boy, container, false);
        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        OTPActivity.settitle(Constants.TITLE_ADD_DEL_BOY);
        init();
        return  view;
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        btAddDelBoy = view.findViewById(R.id.btAddDelBoy);
        btAddDelBoy.setOnClickListener(this);
        btSubmit = view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);
        rvDelBoysList =view.findViewById(R.id.rvDelBoyList);
        llNoData =view.findViewById(R.id.llNoData);

        ApiCallGetLists();
    }

    private void ApiCallGetLists() {
        delivery_boys_list.clear();

        mProgressDialog.setTitle("Fetching Delivery Boys...");
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        Call<DelBoyResponse> call = ApiClient.getClient().create(ApiInterface.class).getDelBoys("Bearer "+sessionManager.getToken(),params);
        call.enqueue(new Callback<DelBoyResponse>() {
            @Override
            public void onResponse(Call<DelBoyResponse> call, Response<DelBoyResponse> response) {
                DelBoyResponse deliveryBoysList = response.body();
                try {
                    if (deliveryBoysList.getResponseCode() == 200) {
                        mProgressDialog.dismiss();
//                        Toast.makeText(AddDeliveryBoysActivity.this,deliveryBoysList.getResponse(),Toast.LENGTH_SHORT).show();
                        delivery_boys_list.addAll(deliveryBoysList.getData());
                    }
                    else if (deliveryBoysList.getResponseCode()==411){
                        sessionManager.logoutUser(getActivity());
                    }else {
                        mProgressDialog.dismiss();
                      //  Toast.makeText(getActivity(), deliveryBoysList.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                    if (delivery_boys_list.isEmpty()) {

                        btSubmit.setEnabled(false);
                        btSubmit.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            btSubmit.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_corner_button_grey) );
                        } else {
                            btSubmit.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.rounded_corner_button_grey));
                        }

                    } else {
                        llNoData.setVisibility(View.GONE);
                        btSubmit.setVisibility(View.VISIBLE);

                        btSubmit.setEnabled(true);
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            btSubmit.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary) );
                        } else {
                            btSubmit.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
                        }
                    }
                    deliveryBoysListAdapter = new DeliveryBoysListAdapter(delivery_boys_list, getActivity());

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                    rvDelBoysList.setLayoutManager(horizontalLayoutManager);
                    rvDelBoysList.setAdapter(deliveryBoysListAdapter);


                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DelBoyResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_LONG).show();
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
        switch (view.getId()){
            case R.id.btAddDelBoy:
                Intent i = new Intent(getActivity(),AddNewDeliveryBoy.class);
                startActivityForResult(i,DEL_BOY);
                break;


            case R.id.btSubmit:
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSubmit();
                }
                else{
                    Constants.ShowNoInternet(mcontext);
                }
                break;
        }
    }

    private void ApiCallSubmit() {

        mProgressDialog.setTitle("Please wait");
        mProgressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PARAM_PROCESS_ID, str_process_id);
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());

        Call<DelBoysNextResponse> call = ApiClient.getClient().create(ApiInterface.class).completeDelBoy("Bearer "+sessionManager.getToken(),params);
        call.enqueue(new Callback<DelBoysNextResponse>() {
            @Override
            public void onResponse(Call<DelBoysNextResponse> call, Response<DelBoysNextResponse> response) {
                mProgressDialog.dismiss();

                try{
                    if(response.isSuccessful()) {
                        DelBoysNextResponse delBoysNextResponse = response.body();
                        if(delBoysNextResponse.getResponseCode() == 200){
                            showAlert(delBoysNextResponse.getResponse());
                        }
                        else if (delBoysNextResponse.getResponseCode()==411){
                            sessionManager.logoutUser(getActivity());
                        }
                        else{
                            Toast.makeText(getActivity(), delBoysNextResponse.getResponse(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DelBoysNextResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DEL_BOY){
            ApiCallGetLists();
            OTPActivity.settitle(Constants.TITLE_ADD_DEL_BOY);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        OTPActivity.settitle(Constants.TITLE_ADD_DEL_BOY);
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
        dialog = new Dialog(getActivity(), R.style.DialogLSideBelow);
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

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.rvContentMainOTP, cFragment);
            fragmentTransaction.commit();

        }
    }
}
