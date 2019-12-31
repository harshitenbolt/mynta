package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.fragment.OnboardingSupportFragment;
import com.canvascoders.opaper.fragment.PaymentSupportFragment;
import com.canvascoders.opaper.utils.SessionManager;

public class AssessmentScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Datum datum;
    private TextView tvDeliveryBoy, tvKirana;
    Drawable background, background1;
    private RecyclerView rvSupport;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_screen);
        init();

        commanFragmentCallWithBackStack(new OnboardingSupportFragment());
        if (background1 instanceof ShapeDrawable) {
            ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof GradientDrawable) {
            ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof ColorDrawable) {
            ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
        }

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    private void init() {
        tvKirana = findViewById(R.id.tvKiranaAssessment);
        tvDeliveryBoy = findViewById(R.id.tvDeliveryAsssessment);
        tvDeliveryBoy.setOnClickListener(this);
        tvKirana.setOnClickListener(this);
        sessionManager = new SessionManager(this);
        //
        background = tvKirana.getBackground();
        background1 = tvDeliveryBoy.getBackground();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvKiranaAssessment:
                commanFragmentCallWithBackStack(new PaymentSupportFragment());

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorBlack));
                }


                break;


            case R.id.tvDeliveryAsssessment:
                commanFragmentCallWithBackStack(new OnboardingSupportFragment());


                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvDeliveryBoy.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvKirana.setTextColor(getResources().getColor(R.color.colorBlack));
                }

                break;



        }


    }

    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flAssessment, cFragment);
            // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}
