package com.canvascoders.opaper.adapters;


import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Beans.BillList;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.viewholder.LoadingHolder;

import java.util.List;


/**
 * Created by Nikhil on 1/10/2017.
 */
public class ReportAdapter extends RecyclerView.Adapter {

    List<BillList> dataViews;

    public ReportAdapter(List<BillList> dataViews) {
        this.dataViews = dataViews;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
            return new ItemHolder(view);
        } else if (viewType == Constants.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((ItemHolder) holder).bt1.setText(String.valueOf(dataViews.get(position).getProccess_id()));
        ((ItemHolder) holder).t2.setText(String.valueOf(dataViews.get(position).getStore_name()));
        ((ItemHolder) holder).t3.setText(String.valueOf(dataViews.get(position).getBill_period()));
        ((ItemHolder) holder).bt4.setText(String.valueOf(dataViews.get(position).getDc()));
        ((ItemHolder) holder).t5.setText(String.valueOf(dataViews.get(position).getStore_address()));
        ((ItemHolder) holder).t6.setText(String.valueOf(dataViews.get(position).getAmt()));


    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataViews.get(position) == null ? Constants.VIEW_TYPE_LOADING : Constants.VIEW_TYPE_ITEM;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5, t6, t7;
        Button bt1,bt4;

        public ItemHolder(View itemView) {
            super(itemView);
            this.bt1 = (Button) itemView.findViewById(R.id.txt1);
            this.t2 = (TextView) itemView.findViewById(R.id.txt2);
            this.t3 = (TextView) itemView.findViewById(R.id.txt3);
            this.bt4 = (Button) itemView.findViewById(R.id.txt4);
            this.t5 = (TextView) itemView.findViewById(R.id.txt5);
            this.t6 = (TextView) itemView.findViewById(R.id.txt6);
            this.t7 = (TextView) itemView.findViewById(R.id.txt7);


        }
    }
}
