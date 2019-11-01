package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.VendorListSearch;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.fragment.MobileFragment;
import com.canvascoders.opaper.fragment.VenderListFragment;
import com.canvascoders.opaper.fragment.VenderListFragment1;
import com.canvascoders.opaper.utils.CustomFilter;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.viewholder.LoadingHolder;

import java.util.List;

/**
 * Created by Nikhil on 1/10/2017.
 */
public class VendorAdapter extends RecyclerView.Adapter implements Filterable {

    public List<VendorList> dataViews;
    public List<VendorListSearch> dataViews1;
    List<VendorList> filter_dataviewList;
    List<VendorListSearch> filter_dataviewList1;
    Context mContext;
    int inWhichScreen;
    VenderListFragment vlActivity;
    VenderListFragment1 vlActivity1;
    CustomFilter customFilter;


    public VendorAdapter(VenderListFragment vActivity, List<VendorList> dataViews) {
        this.dataViews = dataViews;
        vlActivity = vActivity;
        this.filter_dataviewList = dataViews;
    }
    /*public VendorAdapter(VenderListFragment1 vActivity, List<VendorListSearch> dataViews,Context context) {
        this.dataViews1 = dataViews;
        vlActivity1 = vActivity;
        this.filter_dataviewList1 = dataViews;
    }*/

    public void setInWhichScreen(int inWhichScreen) {
        this.inWhichScreen = inWhichScreen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        if (viewType == Constants.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_row, parent, false);
            return new ItemHolder(view);
        } else if (viewType == Constants.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final VendorList vendorList = dataViews.get(position);


        if (vendorList != null) {
            ItemHolder itemHolder = (ItemHolder) holder;

            if (!TextUtils.isEmpty(vendorList.getStoreName())) {
                itemHolder.txt_store_name.setText(vendorList.getStoreName());
            }

            itemHolder.txt_phone_number.setText(vendorList.getMobileNo());

            if (!TextUtils.isEmpty(vendorList.getAadhaarPincode())) {
                itemHolder.txt_pin_code.setText(vendorList.getAadhaarPincode());
            } else {
                itemHolder.txt_pin_code.setText("Not Available");
            }

            itemHolder.txt_phone_number2.setText(vendorList.getMobileNo());
            itemHolder.txt_status.setText("Resume");

            if(vendorList.getIsAgreementUpdationRequire().equalsIgnoreCase("1")){
                itemHolder.cdMain.setBackgroundResource(R.color.color12);
            }

            if (!TextUtils.isEmpty(vendorList.getMobileVerify()) && !vendorList.getMobileVerify().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getMobileVerify());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color1));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color1));
            } else if (!TextUtils.isEmpty(vendorList.getLocationVerify()) && !vendorList.getLocationVerify().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getLocationVerify());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color2));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color2));
            } else if (!TextUtils.isEmpty(vendorList.getAadhaarVerify()) && !vendorList.getAadhaarVerify().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getAadhaarVerify());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color3));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color3));
            }
            else if (!TextUtils.isEmpty(vendorList.getNoc()) && !vendorList.getNoc().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getNoc());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color3));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color3));
            }else if (!TextUtils.isEmpty(vendorList.getPanVerify()) && !vendorList.getPanVerify().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getPanVerify());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color4));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color4));
            } else if (!TextUtils.isEmpty(vendorList.getChequeVerify()) && !vendorList.getChequeVerify().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getChequeVerify());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color5));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color5));
            } else if (!TextUtils.isEmpty(vendorList.getFillDetails()) && !vendorList.getFillDetails().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getFillDetails());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color6));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color6));
            } else if (!TextUtils.isEmpty(vendorList.getUploadFiles()) && !vendorList.getUploadFiles().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getUploadFiles());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color7));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color7));
            } else if (!TextUtils.isEmpty(vendorList.getRateSendForApproval()) && !vendorList.getRateSendForApproval().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getRateSendForApproval());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color8));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color8));
            } else if (!TextUtils.isEmpty(vendorList.getAgreement()) && !vendorList.getAgreement().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getAgreement());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color9));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color9));
            } else if (!TextUtils.isEmpty(vendorList.getGstdeclaration()) && !vendorList.getGstdeclaration().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getGstdeclaration());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color10));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color10));
            } else if (!TextUtils.isEmpty(vendorList.getVendorSendForApproval()) && !vendorList.getVendorSendForApproval().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getVendorSendForApproval());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color11));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color11));
            }
            else if (!TextUtils.isEmpty(vendorList.getRate()) && !vendorList.getRate().equalsIgnoreCase("0")) {
                itemHolder.txt_reason.setText(" " + vendorList.getRate());
                itemHolder.txt_reason.setTextColor(mContext.getResources().getColor(R.color.color7));
                tintViewDrawable(itemHolder.txt_reason, mContext.getResources().getColor(R.color.color7));
            }


            Mylogger.getInstance().Logit("img", vendorList.getShopImage());

            if (inWhichScreen == 1) {
                itemHolder.linear_one.setVisibility(View.VISIBLE);
                itemHolder.linear_two.setVisibility(View.GONE);
                itemHolder.img_view_vendor.setVisibility(View.VISIBLE);
                Log.e("Image", "" + vendorList.getShopImage());
                Glide.with(mContext).load(vendorList.getShopImage())
                        .placeholder(R.drawable.store_place)
                        .error(R.drawable.store_place)
                        .fallback(R.drawable.store_place)
                        .into(itemHolder.img_view_vendor);

                itemHolder.linear_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (vendorList.getBankDetailUpdationRequired().equalsIgnoreCase("1")) {
                        vlActivity.sendOTP(vendorList.getProccessId());
//                        }
                    }
                });
                Log.e("UPDATEIOM", "" + vendorList.getBankDetailUpdationRequired());
//                if (vendorList.getBankDetailUpdationRequired() != null && vendorList.getBankDetailUpdationRequired().equalsIgnoreCase("1")) {
//                    itemHolder.tvReason.setVisibility(View.VISIBLE);
//                    itemHolder.tvReason.setTextColor(mContext.getResources().getColor(R.color.color1));
//                    tintViewDrawable(itemHolder.tvReason, mContext.getResources().getColor(R.color.color1));
//                } else {
                    itemHolder.tvReason.setVisibility(View.GONE);
//                }
            } else {
                itemHolder.linear_one.setVisibility(View.GONE);
                itemHolder.linear_two.setVisibility(View.VISIBLE);
                itemHolder.img_view_vendor.setVisibility(View.GONE);
                itemHolder.img_view_vendor.setImageResource(R.drawable.store_place);
            }

            itemHolder.txt_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    commanFragmentCallWithBackStack(new MobileFragment(), vendorList.getMobileNo());

                }
            });

        }

    }

    private void tintViewDrawable(TextView view, int color) {

    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataViews.get(position) == null ? Constants.VIEW_TYPE_LOADING : Constants.VIEW_TYPE_ITEM;
    }

    @Override
    public Filter getFilter() {

        customFilter = new CustomFilter(filter_dataviewList, this);
        customFilter.setInWhichScreen(inWhichScreen);

        return customFilter;

    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView img_view_vendor;
        public AppCompatTextView txt_store_name, txt_phone_number, txt_pin_code, txt_phone_number2, txt_status;
        public LinearLayoutCompat linear_one, linear_two;
        public AppCompatTextView txt_reason, tvReason;
        public CardView cdMain;


        public ItemHolder(View itemView) {
            super(itemView);
            linear_one = itemView.findViewById(R.id.linear_one);
            linear_two = itemView.findViewById(R.id.linear_two);
            img_view_vendor = itemView.findViewById(R.id.img_view_vendor);
            txt_store_name = itemView.findViewById(R.id.txt_store_name);
            cdMain = itemView.findViewById(R.id.cvMain);
            txt_phone_number = itemView.findViewById(R.id.txt_phone_number);
            txt_pin_code = itemView.findViewById(R.id.txt_pin_code);
            txt_phone_number2 = itemView.findViewById(R.id.txt_phone_number2);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_reason = itemView.findViewById(R.id.txt_reason);
            tvReason = itemView.findViewById(R.id.tvReason);
        }
    }

    public void commanFragmentCallWithBackStack(Fragment fragment, String mobileno) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMP_MOBILE, mobileno);

            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
}
