package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.GetVendorInvoiceList.MensaDelivery;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.InvoiceWebViewActivity;
import com.canvascoders.opaper.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoOFInvoicesTitleListAdapter extends RecyclerView.Adapter<NoOFInvoicesTitleListAdapter.ItemHolder> {

    List<MensaDelivery> titleList;
    Context mContext;
    String[] keys, value;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    List<String> keysname = new ArrayList<>();


    public NoOFInvoicesTitleListAdapter(List<MensaDelivery> titleList, Context ctx) {
        this.titleList = titleList;
        mContext = ctx;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_invoices, parent, false);
        return new NoOFInvoicesTitleListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvTitle.setText(titleList.get(position).getInvoiceNum());
        holder.tvInvoiceType.setText(titleList.get(position).getInvoiceType());

        if (titleList.get(position).getInvoiceType().equalsIgnoreCase("tax-gst")) {


            if (titleList.get(position).getGstEsignStatus().equalsIgnoreCase("1")) {
                holder.tvStatus.setText("Signed");
                holder.ivStatus.setImageResource(R.drawable.sign);
                holder.ivEye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(mContext, InvoiceWebViewActivity.class);
                        i.putExtra(Constants.KEY_INVOICE_ID, titleList.get(position).getId());
                        i.putExtra(Constants.INVOICE_NUMBER, titleList.get(position).getInvoiceType());
                        mContext.startActivity(i);

                    }
                });
                //signed
            } else {
                holder.tvStatus.setText("Pending");
                holder.ivStatus.setImageResource(R.drawable.caution);
                holder.ivEye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, InvoiceWebViewActivity.class);
                        i.putExtra(Constants.KEY_INVOICE_ID, titleList.get(position).getId());
                        i.putExtra(Constants.INVOICE_TYPE, titleList.get(position).getInvoiceType());
                        i.putExtra(Constants.INVOICE_NUMBER,titleList.get(position).getGstEsignStatus());
                        mContext.startActivity(i);
                    }
                });
            }


        } else {
            holder.tvStatus.setText("N/A");
            holder.ivStatus.setVisibility(View.GONE);


        }


    }

    @Override
    public int getItemCount() {

        return titleList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvInvoiceType, tvTitle, tvAccountNo, tvStatus;
        public LinearLayout llMain;
        ImageView ivStatus, ivEye;

        public ItemHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvInvoiceNum);
            tvInvoiceType = itemView.findViewById(R.id.tvInvoiceType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            ivEye = itemView.findViewById(R.id.ivEye);

        }
    }


}
