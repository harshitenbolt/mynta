package com.canvascoders.opaper.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.MensaAlteration;
import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.canvascoders.opaper.utils.Constants.dataRate;

public class StoreReListingAdapter extends RecyclerView.Adapter<StoreReListingAdapter.ItemHolder> {

    public List<StoreTypeBean> dataViews;

    Context mContext;
    List<MensaAlteration> mensaAlterationList;
    List<String> listStoreType = new ArrayList<>();
    CustomPopupRateStoreTypeAdapter customPopupStoreTypeAdapter;
    RecyclerViewClickListener recyclerViewClickListener;

    boolean[] checkedStoreType;


    public StoreReListingAdapter(List<StoreTypeBean> dataViews, List<MensaAlteration> mensaAlterationList, Context mContext, RecyclerViewClickListener recyclerViewClickListener) {
        this.dataViews = dataViews;
        this.mContext = mContext;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.mensaAlterationList = mensaAlterationList;
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

        if (store.getStoreType().contains(Constants.ASSISTED)) {
            holder.edt_store_amount.setHint("");
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        } else if (store.getStoreType().contains("Mensa Bet - CAC")) {
            holder.edt_store_amount.setHint("");
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        } else if (store.getStoreType().contains("Mensa - Alteration")) {
            holder.edt_store_amount.setHint("");
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        } else {
            holder.rvSeperateRight.setVisibility(View.VISIBLE);
            holder.vSeperate.setVisibility(View.VISIBLE);
        }


        if (store.getStoreType().contains(Constants.CAC_STORE)) {
            holder.check_box_store.setChecked(true);
            holder.check_box_store.setEnabled(true);
            holder.edt_store_amount.setText("10%/3% GMV");
            holder.edt_store_amount.setEnabled(false);
        }


        holder.check_box_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (store.getStoreType().contains(Constants.ASSISTED)) {
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);
                } else if (store.getStoreType().contains("Mensa Bet - CAC")) {
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);
                } else if (store.getStoreType().contains("Mensa - Alteration")) {
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);

                    if (holder.check_box_store.isChecked()) {
                        //dialogbox opeb
                        TextView tvtitleStoreType;
                        RecyclerView rvItems1;
                        Button btSubmit1;
                        ImageView ivClose1;
                        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView1 = inflater1.inflate(R.layout.dialogue_popup_list, null);
                        mBuilder2.setView(dialogView1);
                        tvtitleStoreType = dialogView1.findViewById(R.id.tvTitleListPopup);
                        tvtitleStoreType.setText("Mensa Alteration Type");
                        rvItems1 = dialogView1.findViewById(R.id.rvListPopup);
                        btSubmit1 = dialogView1.findViewById(R.id.btSubmitDetail);

                        customPopupStoreTypeAdapter = new CustomPopupRateStoreTypeAdapter(mensaAlterationList, mContext, "StoreType", this);

                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

                        rvItems1.setLayoutManager(horizontalLayoutManager1);

                        rvItems1.setAdapter(customPopupStoreTypeAdapter);
                        AlertDialog mDialog1 = mBuilder2.create();
                        mDialog1.show();

                        btSubmit1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // tvVendorTypeDetail.setText(selectedString);
                                String str = "";
                                if (dataRate != null) {
                                    str = TextUtils.join(",", dataRate);
                                    Log.e("itemlist", str);
                                    mDialog1.dismiss();
                                } else {
                                    holder.check_box_store.setChecked(false);
                                    str = "";
                                    mDialog1.dismiss();
                                }
                                recyclerViewClickListener.SingleClick(str, position);


                            }
                        });
                        ivClose1 = dialogView1.findViewById(R.id.ivClose);
                        ivClose1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String str = "";
                                if (dataRate != null) {
                                    str = TextUtils.join(",", dataRate);
                                    Log.e("itemlist", str);
                                    mDialog1.dismiss();
                                } else {
                                    holder.check_box_store.setChecked(false);
                                    str = "";
                                    mDialog1.dismiss();
                                }
                                holder.check_box_store.setChecked(false);
                            }
                        });
                    }


                } else {
                    holder.rvSeperateRight.setVisibility(View.VISIBLE);
                    holder.vSeperate.setVisibility(View.VISIBLE);
                }


                if (holder.check_box_store.isChecked()) {
                    holder.edt_store_amount.setEnabled(true);
                    dataViews.get(position).setSelected(true);
                } else {
                    holder.edt_store_amount.setEnabled(false);
                    dataViews.get(position).setSelected(false);
                }


                if (store.getStoreType().contains(Constants.CAC_STORE)) {
                       /* holder.check_box_store.setChecked(true);
                        holder.check_box_store.setEnabled(true);
                        holder.edt_store_amount.setText("10%/3% GMV");*/
                    holder.edt_store_amount.setEnabled(false);
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
            /*holder.check_box_store.setChecked(true);
            holder.check_box_store.setEnabled(true);*/
            holder.edt_store_amount.setText("10%/3% GMV");
            holder.edt_store_amount.setEnabled(false);
        }


        if (store.getStoreType().contains(Constants.RENTAL)) {
            holder.check_box_store.setEnabled(false);
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
            vSeperate = itemView.findViewById(R.id.viewSeperate);
            rvSeperateRight = itemView.findViewById(R.id.rvRightMain);
            check_box_store = itemView.findViewById(R.id.check_box_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            edt_store_amount = itemView.findViewById(R.id.edt_store_amount);
            edt_store_amount.setEnabled(false);
        }
    }
}
