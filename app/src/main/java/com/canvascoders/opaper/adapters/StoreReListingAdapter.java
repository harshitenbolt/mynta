package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class StoreReListingAdapter extends RecyclerView.Adapter<StoreReListingAdapter.ItemHolder> {

    public List<StoreTypeBean> dataViews;

    Context mContext;

    public StoreReListingAdapter(List<StoreTypeBean> dataViews, Context mContext) {
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

        if (!store.getStoreType().contains("Alteration") && !store.getStoreType().contains("Rental")) {
            holder.check_box_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.check_box_store.isChecked()) {
                        holder.edt_store_amount.setEnabled(true);
                        dataViews.get(position).setSelected(true);
                    } else {
                        holder.edt_store_amount.setEnabled(false);
                        dataViews.get(position).setSelected(false);
                    }
// remove other selected
                    for (int i = 0; i < dataViews.size(); i++) {
                        if (i != position)
                            if (dataViews.get(i).isSelected()) {
                                dataViews.get(i).setSelected(false);
                                dataViews.get(i).setRate("0.0");
                                notifyItemChanged(i);
                            }
                    }

                }
            });
            holder.check_box_store.setEnabled(true);
            holder.check_box_store.setChecked(store.isSelected());
            holder.edt_store_amount.setEnabled(store.isSelected());
            holder.edt_store_amount.setText(store.getRate());
        } else {
            holder.check_box_store.setEnabled(false);
            holder.edt_store_amount.setEnabled(false);
        }

        if (store.isSelected()) {

//                if (store.getIsApproved().equalsIgnoreCase("1")) {
//                    holder.check_box_store.setEnabled(false);
//                } else {
//                    holder.check_box_store.setEnabled(true);
//                    holder.check_box_store.setChecked(true);
//                    holder.edt_store_amount.setEnabled(true);
//                }

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
//                                                                           Toast.makeText(mContext, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        if (store.getIsApproved().equalsIgnoreCase("0"))  //  Not added now/ or pending
//
//        {

//        } else if (store.getIsApproved().equalsIgnoreCase("1"))
//
//        {  // Approved dont change anything
//            holder.check_box_store.setEnabled(false);
//            holder.edt_store_amount.setEnabled(false);
//            holder.check_box_store.setChecked(false);
//        } else if (store.getIsApproved().
//
//                equalsIgnoreCase("2"))
//
//        {  // Rejected
//            holder.check_box_store.setEnabled(true);
//            holder.check_box_store.setChecked(true);
//        }
        if (store.getStoreType().contains(Constants.CAC_STORE)) {
            holder.check_box_store.setChecked(true);
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


        public ItemHolder(View itemView) {
            super(itemView);
            check_box_store = itemView.findViewById(R.id.check_box_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            edt_store_amount = itemView.findViewById(R.id.edt_store_amount);
            edt_store_amount.setEnabled(false);
        }
    }
}
