package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class VendorListInProgressAdapter extends RecyclerView.Adapter<VendorListInProgressAdapter.RecordHolder> {

    private List<VendorList> vendorLists;
    Context context;
    String value = "1";
    RecyclerViewClickListener recyclerViewClickListener;


    public VendorListInProgressAdapter(List<VendorList> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.vendorLists = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorlist_inprogress_vendor, parent, false);
        RecordHolder holder = new RecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvName.setText(vendorLists.get(position).getName());
        holder.tvMobile.setText(vendorLists.get(position).getMobileNo());
        holder.tvRoute.setText(vendorLists.get(position).getStoreAddress());
        holder.tvLiveFrom.setText(vendorLists.get(position).getRateApproveDate());
        holder.tvStoreName.setText(vendorLists.get(position).getStoreName());
        Log.e("URL", "" + Constants.BaseImageURL + vendorLists.get(position).getShopImage());

        Glide.with(context).load(Constants.BaseImageURL + vendorLists.get(position).getShopImage()).placeholder(R.drawable.image_placeholder).into(holder.ivStoreImage);

        //holder.tvStatuswhileOnBoarding.setText(vendorLists.get(position).getStatus());
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commanFragmentCallWithBackStack(new MobileFragment(), vendorLists.get(position).getMobileNo());

            }
        });

        if (!TextUtils.isEmpty(vendorLists.get(position).getMobileVerify()) && !vendorLists.get(position).getMobileVerify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getMobileVerify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color1));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color1));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getLocationVerify()) && !vendorLists.get(position).getLocationVerify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getLocationVerify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color2));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color2));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getAadhaarVerify()) && !vendorLists.get(position).getAadhaarVerify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getAadhaarVerify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color3));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color3));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getNoc()) && !vendorLists.get(position).getNoc().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getNoc());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color3));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color3));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getPanVerify()) && !vendorLists.get(position).getPanVerify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getPanVerify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color4));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color4));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getChequeVerify()) && !vendorLists.get(position).getChequeVerify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getChequeVerify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color5));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color5));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getFillDetails()) && !vendorLists.get(position).getFillDetails().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getFillDetails());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color6));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color6));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getUploadFiles()) && !vendorLists.get(position).getUploadFiles().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getUploadFiles());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color7));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color7));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getDeliveryBoy()) && !vendorLists.get(position).getDeliveryBoy().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getDeliveryBoy());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color7));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color7));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getRateSendForApproval()) && !vendorLists.get(position).getRateSendForApproval().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getRateSendForApproval());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color8));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color8));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getAgreement()) && !vendorLists.get(position).getAgreement().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getAgreement());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color9));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color9));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getGstdeclaration()) && !vendorLists.get(position).getGstdeclaration().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getGstdeclaration());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color10));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color10));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getAssessmentverify()) && !vendorLists.get(position).getAssessmentverify().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getAssessmentverify());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color11));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color11));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getVendorSendForApproval()) && !vendorLists.get(position).getVendorSendForApproval().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getVendorSendForApproval());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color7));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color7));
        } else if (!TextUtils.isEmpty(vendorLists.get(position).getRate()) && !vendorLists.get(position).getRate().equalsIgnoreCase("0")) {
            holder.tvStatuswhileOnBoarding.setText(" " + vendorLists.get(position).getRate());
            holder.tvStatuswhileOnBoarding.setTextColor(context.getResources().getColor(R.color.color11));
            tintViewDrawable(holder.tvStatuswhileOnBoarding, context.getResources().getColor(R.color.color11));
        }
    }

    private void tintViewDrawable(TextView view, int color) {

    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage;
        TextView tvStoreName, tvName, tvMobile, tvRoute, tvLiveFrom, tvStatus, tvStatuswhileOnBoarding;
        LinearLayout llMain;

        public RecordHolder(View view) {
            super(view);
            llMain = view.findViewById(R.id.llMain);
            ivStoreImage = view.findViewById(R.id.ivStoreImage);
            tvName = view.findViewById(R.id.tvName);
            tvStoreName = view.findViewById(R.id.tvStoreName);
            tvLiveFrom = view.findViewById(R.id.tvLiveFrom);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvRoute = view.findViewById(R.id.tvRoute);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvStatuswhileOnBoarding = view.findViewById(R.id.tvStatuswhileBoarding);
            //     tvVehicle = view.findViewById(R.id.tvVehicle);
        }
    }


    public void commanFragmentCallWithBackStack(Fragment fragment, String mobileno) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMP_MOBILE, mobileno);

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.rvContentMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
}
