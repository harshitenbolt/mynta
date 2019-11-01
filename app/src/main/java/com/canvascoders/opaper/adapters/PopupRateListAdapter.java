package com.canvascoders.opaper.adapters;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canvascoders.opaper.Beans.SubmitPopValue;
import com.canvascoders.opaper.R;

import java.util.List;

public class PopupRateListAdapter extends RecyclerView.Adapter<PopupRateListAdapter.ItemHolder> {

    public List<SubmitPopValue> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    String main;

    public PopupRateListAdapter(Context ctx, List<SubmitPopValue> dataViews, String s) {
        this.bankDetailList = dataViews;
        mContext = ctx;
        main = s;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_rate_list, parent, false);
        return new PopupRateListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        if (main.equalsIgnoreCase("1")) {
            holder.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue));
        } else {
            holder.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorRateBackground));
        }
        holder.tvRateName.setText(bankDetailList.get(position).getStoreType());
        holder.tvRate.setText(mContext.getResources().getString(R.string.Rs) + " " + bankDetailList.get(position).getRate());

        if (position == bankDetailList.size() - 1) {
            holder.view.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {

        return bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvRateName, tvRate, tvAccountNo, tvStauts;
        public LinearLayout llMain;
        View view;

        public ItemHolder(View itemView) {
            super(itemView);
            tvRateName = itemView.findViewById(R.id.tvRateName);
            tvRate = itemView.findViewById(R.id.tvRate);
            llMain = itemView.findViewById(R.id.llMain);
            view = itemView.findViewById(R.id.viewBelow);

        }
    }


}
