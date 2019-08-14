package com.canvascoders.opaper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.BankDetailResp;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.AddendumDetail;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

import static android.content.Intent.ACTION_VIEW;

public class AdddendumListAdapter extends RecyclerView.Adapter<AdddendumListAdapter.ItemHolder> {

    public List<AddendumDetail> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    RecyclerViewClickListener recyclerViewClickListener1;

    public AdddendumListAdapter(Context ctx, List<AddendumDetail> dataViews, RecyclerViewClickListener recyclerViewClickListener){
        this.bankDetailList = dataViews;
        mContext = ctx;
        recyclerViewClickListener1 = recyclerViewClickListener;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addedndum_list, parent, false);
        return new AdddendumListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvAddedndum.setText("Addendum "+position+1);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener1.onClick(view,position);
            }
        });


    }

    @Override
    public int getItemCount() {

        return bankDetailList == null ? 0 : bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tvAddedndum;
        public LinearLayout llMain;

        public ItemHolder(View itemView) {
            super(itemView);
            tvAddedndum = itemView.findViewById(R.id.tvAddendum);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }


}
