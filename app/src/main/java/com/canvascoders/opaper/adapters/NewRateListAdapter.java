package com.canvascoders.opaper.adapters;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ApprovalRateDetail;
import com.canvascoders.opaper.R;

import java.util.List;

public class NewRateListAdapter extends RecyclerView.Adapter<NewRateListAdapter.ItemHolder> {

    public List<ApprovalRateDetail> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public NewRateListAdapter(Context ctx, List<ApprovalRateDetail> dataViews) {
        this.bankDetailList = dataViews;
        mContext = ctx;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_rate_list, parent, false);
        return new NewRateListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvRateName.setText(bankDetailList.get(position).getStoreType());
        holder.tvRate.setText(mContext.getResources().getString(R.string.Rs) + " " + bankDetailList.get(position).getNewRate());
        if (position == bankDetailList.size() - 1) {
            holder.viewMain.setVisibility(View.GONE);
        }

//        holder.tvStauts.setText(bankDetail.getStatus());
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
