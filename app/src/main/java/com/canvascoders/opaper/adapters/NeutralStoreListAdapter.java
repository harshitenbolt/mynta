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
import android.widget.Toast;

import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.R;

import java.util.List;

public class NeutralStoreListAdapter extends RecyclerView.Adapter<NeutralStoreListAdapter.ItemHolder> {

    public List<StoreTypeBean> dataViews;

    Context mContext;

    public NeutralStoreListAdapter(List<StoreTypeBean> dataViews, Context mContext) {
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

//        if (store.getIsApproved().equalsIgnoreCase("0"))  //  Not added now/ or pending
//        {
        holder.check_box_store.setEnabled(true);
        holder.check_box_store.setChecked(store.isSelected());
        holder.edt_store_amount.setEnabled(store.isSelected());
        holder.edt_store_amount.setText(store.getRate());
//        }
//        else if (store.getIsApproved().equalsIgnoreCase("1")) {  // Approved dont change anything
//            holder.check_box_store.setEnabled(false);
//            holder.edt_store_amount.setEnabled(false);
//            holder.check_box_store.setChecked(false);
//        }
//        else if (store.getIsApproved().equalsIgnoreCase("2")) {  // Rejected
//            holder.check_box_store.setEnabled(true);
//            holder.check_box_store.setChecked(true);
//        }

        holder.check_box_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.check_box_store.isChecked()) {
                    holder.edt_store_amount.setEnabled(true);
                    dataViews.get(position).setSelected(true);
                    for (int i = 0; i < dataViews.size(); i++) {
                        if (i != position) {
                            dataViews.get(i).setSelected(false);
                            dataViews.get(i).setRate("0");
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    holder.edt_store_amount.setEnabled(false);
                    dataViews.get(position).setSelected(false);

                }
            }
        });

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
                    Toast.makeText(mContext, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
