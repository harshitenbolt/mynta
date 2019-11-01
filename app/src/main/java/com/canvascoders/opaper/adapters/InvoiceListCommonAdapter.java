package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.VendorInvoiceList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class InvoiceListCommonAdapter extends RecyclerView.Adapter<InvoiceListCommonAdapter.RecordHolder> {

    private List<VendorInvoiceList> vendorLists;
    Context context;
    String value = "1";
    RecyclerViewClickListener recyclerViewClickListener;


    public InvoiceListCommonAdapter(List<VendorInvoiceList> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.vendorLists = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_invoice_list, parent, false);
        RecordHolder holder = new RecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {

        VendorInvoiceList vendorLists1 = vendorLists.get(position);
        holder.tvBillPeriod.setText(vendorLists1.getBillPeriod());
        holder.tvStoreAddress.setText(vendorLists1.getStoreAddress());
        holder.tvMobile.setText(vendorLists1.getMobileNo());
        holder.tvStoreName.setText(vendorLists1.getStoreName());
        holder.btNoInvoice.setText(String.valueOf(vendorLists1.getNoOfInvoice()));
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickListener.SingleClick("", vendorLists.get(position).getProccessId());
            }
        });
       /* holder.tvName.setText(vendorLists.get(position).getName());
        holder.tvMobile.setText(vendorLists.get(position).getMobileNo());
        holder.tvRoute.setText(vendorLists.get(position).getStoreAddress());
        holder.tvLiveFrom.setText(vendorLists.get(position).getRateApproveDate());
        holder.tvStoreName.setText(vendorLists.get(position).getStoreName());
        //  holder.tvStatus.setText(vendorLists.get(position).getStatus());



        if(vendorLists.get(position).getIsAgreementUpdationRequire().equalsIgnoreCase("1")){
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tvStatus.setText("Expired");
           // holder.ivStatusImage.setImageResource(R.drawable.deactivate);

        }
        else if (vendorLists.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvStatus.setText("Active");
            holder.ivStatusImage.setImageResource(R.drawable.active);
        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tvStatus.setText("Deactivated/Closed");
            holder.ivStatusImage.setImageResource(R.drawable.deactivate);

        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.tvStatus.setText("Payment Hold");
            holder.ivStatusImage.setImageResource(R.drawable.hold);
        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("4")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.tvStatus.setText("Black Listed");
            holder.ivStatusImage.setImageResource(R.drawable.blacklist);
        }
        Log.e("URL", "" + Constants.BaseImageURL + vendorLists.get(position).getShopImage());
        Glide.with(context).load(Constants.BaseImageURL + vendorLists.get(position).getShopImage()).placeholder(R.drawable.image_placeholder).into(holder.ivStoreImage);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });*/
    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage, ivStatusImage;
        TextView tvStoreName, tvName, tvMobile, tvStoreAddress, tvBillPeriod;
        LinearLayout llMain;
        TextView btNoInvoice;

        CardView cvMain;

        public RecordHolder(View view) {
            super(view);
            cvMain = view.findViewById(R.id.cvMain);
            tvStoreName = view.findViewById(R.id.tvStoreName);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvPhone);
            tvStoreAddress = view.findViewById(R.id.tvAddress);
            btNoInvoice = view.findViewById(R.id.btNoInvoice);
            tvBillPeriod = view.findViewById(R.id.tvLastInvoiceDate);
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
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }
}
