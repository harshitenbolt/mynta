package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.GetVendorInvoiceList.Datum;
import com.canvascoders.opaper.Beans.GetVendorInvoiceList.MensaDelivery;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.InvoiceDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoOFBillPeriodListAdapter extends RecyclerView.Adapter<NoOFBillPeriodListAdapter.ItemHolder> {

    Map<String, Map<String, List<MensaDelivery>>> titleList;
    Context mContext;
    String[] keys, value;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    List<String> keysname = new ArrayList<>();
    List<Map<String, List<MensaDelivery>>> valuename = new ArrayList<>();

    NoOFBillPeriodTitleListAdapter noOFBillPeriodTitleListAdapter;


    List<Map<String, MensaDelivery>> subtitleList = new ArrayList<>();

    // Map<String, List<Map<String, MensaDelivery>>>

    public NoOFBillPeriodListAdapter(Map<String, Map<String, List<MensaDelivery>>> titleList, Context ctx) {
        this.titleList = titleList;
        mContext = ctx;
        keysname.addAll(titleList.keySet());
        valuename.addAll(titleList.values());
        Log.e("Perfect", titleList.toString());
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_period, parent, false);
        return new NoOFBillPeriodListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvTitle.setText(keysname.get(position).toString());


        noOFBillPeriodTitleListAdapter = new NoOFBillPeriodTitleListAdapter(valuename.get(position), mContext);
        Log.e("DataDone", valuename.get(position).toString());

        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        holder.rvTitles.setLayoutManager(horizontalLayoutManager1);
        holder.rvTitles.setAdapter(noOFBillPeriodTitleListAdapter);


    }

    @Override
    public int getItemCount() {

        return titleList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvRateName, tvTitle, tvAccountNo, tvStauts;
        public LinearLayout llMain;
        View viewMain;
        RecyclerView rvTitles;

        public ItemHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            rvTitles = itemView.findViewById(R.id.rvTitles);

        }
    }


}
