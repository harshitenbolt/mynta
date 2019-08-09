package com.canvascoders.opaper.fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.CheckEsignResponse.CheckEsignResponse;
import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.Beans.SupportListResponse.SupportListResponse;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.adapters.RateListAdapter;
import com.canvascoders.opaper.adapters.SupportListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class SupportFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    View v;
    Datum datum;
    private TextView tvOnBoarding, tvPayment;
    Drawable background, background1;
    private RecyclerView rvSupport;
    private SessionManager sessionManager;

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_support, container, false);
        mContext = this.getActivity();
        DashboardActivity.settitle(Constants.TITLE_SUPPORT);
        init();
        commanFragmentCallWithBackStack(new OnboardingSupportFragment());
        if (background1 instanceof ShapeDrawable) {
            ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof GradientDrawable) {
            ((GradientDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof ColorDrawable) {
            ((ColorDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
        }

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
        }


        return v;
    }

    private void init() {
        tvPayment = v.findViewById(R.id.btPayment);
        tvOnBoarding = v.findViewById(R.id.btOnboarding);
        tvOnBoarding.setOnClickListener(this);
        tvPayment.setOnClickListener(this);
        sessionManager = new SessionManager(getActivity());
        //
        background = tvPayment.getBackground();
        background1 = tvOnBoarding.getBackground();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btPayment:
                commanFragmentCallWithBackStack(new PaymentSupportFragment());

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorBlack));
                }


                break;


            case R.id.btOnboarding:
                commanFragmentCallWithBackStack(new OnboardingSupportFragment());


                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    tvOnBoarding.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvPayment.setTextColor(getResources().getColor(R.color.colorBlack));
                }

                break;
        }


    }


    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flSupport, cFragment);
            // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


}
