package com.canvascoders.opaper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.BankDetailResp;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class ChequeListAdapter extends RecyclerView.Adapter<ChequeListAdapter.ItemHolder> {

    public List<BankDetailResp.BankDetail> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public ChequeListAdapter(Context ctx, List<BankDetailResp.BankDetail> dataViews) {
        this.bankDetailList = dataViews;
        mContext = ctx;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_raw_cheque, parent, false);
        return new ChequeListAdapter.ItemHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        BankDetailResp.BankDetail bankDetail = bankDetailList.get(position);
        holder.tvAccountNo.setText("" + bankDetail.getBankAc());
        holder.tvBankName.setText("" + bankDetail.getBankName());
        holder.tvPayeeName.setText("" + bankDetail.getPayeeName());

        if (bankDetail.getStatus().equalsIgnoreCase("0")) {
            holder.tvStauts.setText("pending");
            holder.tvStauts.setTextColor(mContext.getResources().getColor(R.color.colorYellow));
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStauts.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_yellow));
            } else {
                holder.tvStauts.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_yellow));
            }
        } else if (bankDetail.getStatus().equalsIgnoreCase("1")) {
            holder.tvStauts.setText("Disabled");
            holder.tvStauts.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
            holder.llMain.setBackgroundColor(R.color.color12);
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStauts.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_blue));
            } else {
                holder.tvStauts.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_blue));
            }

        } else if (bankDetail.getStatus().equalsIgnoreCase("2")) {
            holder.tvStauts.setText("Rejected");
            holder.tvStauts.setTextColor(mContext.getResources().getColor(R.color.color1));
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStauts.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_red));
            } else {
                holder.tvStauts.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_red));
            }
        } else {
            holder.tvStauts.setText("Active");
            holder.tvStauts.setTextColor(mContext.getResources().getColor(R.color.color5));
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStauts.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_green));
            } else {
                holder.tvStauts.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_green));
            }
        }
//        holder.tvStauts.setText(bankDetail.getStatus());
    }

    @Override
    public int getItemCount() {

        return bankDetailList == null ? 0 : bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvPayeeName, tvBankName, tvAccountNo, tvStauts;
        public LinearLayout llMain;

        public ItemHolder(View itemView) {
            super(itemView);
            tvPayeeName = itemView.findViewById(R.id.tvPayeeName);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNumber);
            tvStauts = itemView.findViewById(R.id.tvStauts);
            llMain = itemView.findViewById(R.id.llMain);
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
