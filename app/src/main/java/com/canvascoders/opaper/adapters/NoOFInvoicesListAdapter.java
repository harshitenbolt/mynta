package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.GetVendorInvoiceList.StoreList;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ApprovalRateDetail;
import com.canvascoders.opaper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoOFInvoicesListAdapter extends RecyclerView.Adapter<NoOFInvoicesListAdapter.ItemHolder> {

    public Map<String, Integer> bankDetailList;
    Context mContext;
    String[] keys, value;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    List<String> keysname = new ArrayList<>();
    List<Integer> valueName = new ArrayList<>();


    public NoOFInvoicesListAdapter(Map<String, Integer> dataViews, Context ctx) {
        this.bankDetailList = dataViews;
        mContext = ctx;
        keysname.addAll(dataViews.keySet());
        valueName.addAll(dataViews.values());

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_rate_list, parent, false);
        return new NoOFInvoicesListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvRateName.setText(keysname.get(position).toString());
        holder.tvRate.setText(String.valueOf(valueName.get(position)));

    }

    @Override
    public int getItemCount() {

        return bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvRateName, tvRate, tvAccountNo, tvStauts;
        public LinearLayout llMain;
        View viewMain;

        public ItemHolder(View itemView) {
            super(itemView);
            tvRateName = itemView.findViewById(R.id.tvRateName);
            tvRate = itemView.findViewById(R.id.tvRate);
            viewMain = itemView.findViewById(R.id.viewMain);

        }
    }


}
