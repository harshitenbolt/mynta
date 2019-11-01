package com.canvascoders.opaper.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class RateListAdapter extends RecyclerView.Adapter<RateListAdapter.ItemHolder> {

    public List<StoreTypeBean> dataViews;

    Context mContext;

    public RateListAdapter(List<StoreTypeBean> dataViews, Context mContext) {
        this.dataViews = dataViews;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_store_list, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        StoreTypeBean store = dataViews.get(position);
        holder.tv_store_name.setText(store.getStoreType());
        holder.edt_store_amount.setText(store.getRate());
        holder.check_box_store.setEnabled(true);
        holder.check_box_store.setChecked(false);
        if(store.getStoreType().contains(Constants.ASSISTED)){
            holder.edt_store_amount.setHint("");
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        }




        if(store.getStoreType().contains(Constants.RENTAL)){
            holder.check_box_store.setEnabled(false);
        }


        holder.check_box_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(store.getStoreType().contains(Constants.ASSISTED)){
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);
                }

                if (holder.check_box_store.isChecked()) {
                    if (!store.getStoreType().contains(Constants.CAC_STORE))
                        holder.edt_store_amount.setEnabled(true);
                    dataViews.get(position).setSelected(true);
                } else {
                    holder.edt_store_amount.setEnabled(false);
                    dataViews.get(position).setSelected(false);
                }

// If the last one is selected
//                    if (dataViews.size() > 5 && position != 5 && dataViews.get(5).isSelected()) {
//                        dataViews.get(5).setSelected(false);
//                        dataViews.get(5).setRate("0");
//                        notifyItemChanged(5);
//                    }

                // If Third sel then deselct Fifth
                   /* if (position == 3) {
//                        if (dataViews.get(5).isSelected()) {
                        dataViews.get(5).setSelected(false);
                        dataViews.get(5).setRate("0");
                        notifyItemChanged(5);
//                        }
                    }*/
                //IF Fifth selected deselect third
                  /*  if (position == 5) {
//                        for (int i = 0; i <= 4; i++) {
//                        if (dataViews.get(3).isSelected()) {
                        dataViews.get(3).setSelected(false);
                        dataViews.get(3).setRate("0");
                        notifyItemChanged(3);
//                        }
//                        }
                    }*/
            }
        });


        if (store.isSelected()) {
            {
                if (store.getIsApproved().equalsIgnoreCase("1")) {
                    holder.check_box_store.setEnabled(false);
                } else {
                    holder.check_box_store.setEnabled(true);
                    holder.check_box_store.setChecked(true);
                    holder.edt_store_amount.setEnabled(true);
                }
            }
        } else {
            holder.check_box_store.setChecked(false);
        }

        holder.edt_store_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    float rate = Float.parseFloat(editable.toString());
                    dataViews.get(position).setRate("" + rate);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (store.getIsApproved().equalsIgnoreCase("0"))  //  Not added now/ or pending
        {
            if (!store.getStoreType().contains("Alteration") && !store.getStoreType().contains("Rental")) {
                holder.check_box_store.setEnabled(true);
                holder.check_box_store.setChecked(store.isSelected());
                holder.edt_store_amount.setEnabled(store.isSelected());
                holder.edt_store_amount.setText(store.getRate());
            }
        } else if (store.getIsApproved().equalsIgnoreCase("1")) {  // Approved dont change anything
            holder.check_box_store.setEnabled(false);
            holder.edt_store_amount.setEnabled(false);
            holder.check_box_store.setChecked(false);
        } else if (store.getIsApproved().equalsIgnoreCase("2")) {  // Rejected
            holder.check_box_store.setEnabled(true);
            holder.check_box_store.setChecked(true);
        }

        if (store.getStoreType().contains(Constants.CAC_STORE)) {
            holder.check_box_store.setChecked(false);
            holder.check_box_store.setEnabled(true);
            holder.edt_store_amount.setText("10%/3% GMV");
            holder.edt_store_amount.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public AppCompatCheckBox check_box_store;
        public AppCompatTextView tv_store_name;
        public AppCompatEditText edt_store_amount;
        public RelativeLayout rvSeperateRight;
        public View vSeperate;

        public ItemHolder(View itemView) {
            super(itemView);
            check_box_store = itemView.findViewById(R.id.check_box_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            edt_store_amount = itemView.findViewById(R.id.edt_store_amount);
            vSeperate = itemView.findViewById(R.id.viewSeperate);
            rvSeperateRight = itemView.findViewById(R.id.rvRightMain);
            edt_store_amount.setEnabled(false);
        }
    }
}