package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.R;

import java.util.List;

public class ApprovedStoreListAdapter extends RecyclerView.Adapter<ApprovedStoreListAdapter.ItemHolder> {
    public List<StoreTypeBean> dataViews;
    Context mContext;

    public ApprovedStoreListAdapter(List<StoreTypeBean> dataViews, Context mContext) {
        this.dataViews = dataViews;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_approvedstore, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        StoreTypeBean store = dataViews.get(position);
        holder.tv_store_name.setText(store.getStoreType());
        holder.tv_store_amount.setText(store.getRate());
    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatCheckBox check_box_store;
        public AppCompatTextView tv_store_name, tv_store_amount;

        public ItemHolder(View itemView) {
            super(itemView);
            check_box_store = itemView.findViewById(R.id.check_box_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            tv_store_amount = itemView.findViewById(R.id.tv_store_amount);

        }
    }
}
