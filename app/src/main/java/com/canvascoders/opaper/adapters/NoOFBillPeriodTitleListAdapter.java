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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoOFBillPeriodTitleListAdapter extends RecyclerView.Adapter<NoOFBillPeriodTitleListAdapter.ItemHolder> {

    Map<String, List<MensaDelivery>> titleList;
    Context mContext;
    String[] keys, value;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    List<String> keysname = new ArrayList<>();
    List<List<MensaDelivery>> valuename = new ArrayList<>();
    NoOFInvoicesTitleListAdapter noOFInvoicesTitleListAdapter;

    public NoOFBillPeriodTitleListAdapter(Map<String, List<MensaDelivery>> titleList, Context ctx) {
        this.titleList = titleList;
        mContext = ctx;
        keysname.addAll(titleList.keySet());
        valuename.addAll(titleList.values());
        Log.e("Perfect", titleList.toString());
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_store_title, parent, false);
        return new NoOFBillPeriodTitleListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        holder.tvTitle.setText(keysname.get(position).toString());
        Log.e("keysnamw_incoideTYpe", keysname.get(position));


        noOFInvoicesTitleListAdapter = new NoOFInvoicesTitleListAdapter(valuename.get(position), mContext);

        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        holder.rvInvoices.setLayoutManager(horizontalLayoutManager1);
        holder.rvInvoices.setAdapter(noOFInvoicesTitleListAdapter);


    }

    @Override
    public int getItemCount() {

        return titleList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvRateName, tvTitle, tvAccountNo, tvStauts;
        public LinearLayout llMain;
        View viewMain;
        RecyclerView rvInvoices;


        public ItemHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvStoreTitle);
            rvInvoices = itemView.findViewById(R.id.rvInvoicesList);

        }
    }


}
