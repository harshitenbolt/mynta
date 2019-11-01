package com.canvascoders.opaper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.GetGstListing.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class GSTListAdapter extends RecyclerView.Adapter<GSTListAdapter.ItemHolder> {

    public List<Datum> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public GSTListAdapter(Context ctx, List<Datum> dataViews) {
        this.bankDetailList = dataViews;
        mContext = ctx;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gst_cheque, parent, false);
        return new GSTListAdapter.ItemHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        Datum bankDetail = bankDetailList.get(position);
        holder.tvStoreName.setText("" + bankDetail.getStoreName());
        holder.tvGSTNumber.setText("" + bankDetail.getGstn());
        holder.tvStatus.setText("" + bankDetail.getStatus());
        Glide.with(mContext).load(Constants.BaseImageURL + bankDetail.getGstCertificateImage()).placeholder(R.drawable.checkimage).into(holder.ivGSTImage);

        if (bankDetail.getStatus().equalsIgnoreCase("Pending")) {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.colorRed));

        } else if (bankDetail.getStatus().equalsIgnoreCase("Active")) {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

        } else {
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.colorYellow));

        }


    }

    @Override
    public int getItemCount() {

        return bankDetailList == null ? 0 : bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvStoreName, tvGSTNumber, tvStatus, tvAddress;
        public LinearLayout llMain;
        TextView tvComment;
        ImageView ivGSTImage;


        public ItemHolder(View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvGSTNumber = itemView.findViewById(R.id.tvGSTNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivGSTImage = itemView.findViewById(R.id.ivGSTImage);

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
            fragmentTransaction.add(R.id.llMain, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
}
