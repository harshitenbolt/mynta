package com.canvascoders.opaper.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.MensaAlteration;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.SubStoreType;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.EditKYCfromProfileActivity;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.helper.DialogListnerSubSTore;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.DialogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.canvascoders.opaper.utils.Constants.billList;
import static com.canvascoders.opaper.utils.Constants.dataRate;
import static com.canvascoders.opaper.utils.Constants.subStoreTypeList;

public class RateListAdapter extends RecyclerView.Adapter<RateListAdapter.ItemHolder> {

    public List<StoreTypeBean> dataViews;
    Map<String, String> mensaAlterationList;
    RecyclerViewClickListener recyclerViewClickListener;
    List<String> keysname = new ArrayList<>();
    List<String> valueName = new ArrayList<>();
    Context mContext;
    CustomPopupRateStoreTypeAdapter customPopupStoreTypeAdapter;
    CustomPopupRateSubStoreTypeAdapter customPopupSubStoreTypeAdapter;
    String Message = "";

    public RateListAdapter(List<StoreTypeBean> dataViews, Map<String, String> mensaAlterationList, Context mContext, RecyclerViewClickListener recyclerViewClickListener, String Message) {
        this.dataViews = subStoreTypeList;
        this.mContext = mContext;
        this.mensaAlterationList = mensaAlterationList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        keysname.addAll(mensaAlterationList.keySet());
        valueName.addAll(mensaAlterationList.values());
        this.Message = Message;
  //      this.activity = activity;
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

        if (store.getStoreTypeId() == 8) {
            holder.tv_store_name.setText("Marvel - DC 2.0");
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        }
        if (store.getStoreTypeId() == 4) {
            holder.edt_store_amount.setEnabled(true);
            holder.rvSeperateRight.setVisibility(View.VISIBLE);
            holder.vSeperate.setVisibility(View.VISIBLE);
        }

        if (store.getStoreTypeId() == 9) {
            holder.edt_store_amount.setText("     ");
            holder.edt_store_amount.setEnabled(false);
            holder.rvSeperateRight.setVisibility(View.GONE);
            holder.vSeperate.setVisibility(View.GONE);
        }

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
        }


        if (store.getStoreType().contains(Constants.RENTAL)) {
            holder.check_box_store.setEnabled(false);
        }


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


        holder.check_box_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                if (store.getStoreType().contains(Constants.ASSISTED)) {
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);
                }

                if (store.getStoreType().contains("Mensa Bet - CAC")) {
                    holder.edt_store_amount.setHint("");
                    holder.edt_store_amount.setText("     ");
                    holder.edt_store_amount.setEnabled(false);
                    holder.rvSeperateRight.setVisibility(View.GONE);
                    holder.vSeperate.setVisibility(View.GONE);
                }*/
                if(holder.check_box_store.isChecked()) {

                    if (store.getStoreTypeId() == 3) {
                        holder.edt_store_amount.setHint("");
                        holder.edt_store_amount.setText("     ");
                        holder.edt_store_amount.setEnabled(false);
                        //holder.edt_store_amount.setFocusable(false);
                        holder.rvSeperateRight.setVisibility(View.GONE);
                        holder.vSeperate.setVisibility(View.GONE);
                        //dialogbox opeb
                        if (holder.check_box_store.isChecked()) {
                            TextView tvtitleStoreType;
                            RecyclerView rvItems1;
                            Button btSubmit1;
                            Dialog dialog;
                            ImageView ivClose1;
                            AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                            dialog = new Dialog(mContext, R.style.DialogSlideAnim);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.dialogue_popup_list);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(true);

                            tvtitleStoreType = dialog.findViewById(R.id.tvTitleListPopup);
                            tvtitleStoreType.setText("Mensa Alteration Type");
                            rvItems1 = dialog.findViewById(R.id.rvListPopup);
                            btSubmit1 = dialog.findViewById(R.id.btSubmitDetail);
                            List<MensaAlteration> mensaAlterations = new ArrayList<>();
                            for (int i = 0; i < keysname.size(); i++) {
                                MensaAlteration mensaAlteration = new MensaAlteration(false, keysname.get(i), valueName.get(i));
                                mensaAlterations.add(mensaAlteration);
                            }
                            customPopupStoreTypeAdapter = new CustomPopupRateStoreTypeAdapter(mensaAlterations, mContext, "StoreType", this);

                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

                            rvItems1.setLayoutManager(horizontalLayoutManager1);

                            rvItems1.setAdapter(customPopupStoreTypeAdapter);

                            btSubmit1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // tvVendorTypeDetail.setText(selectedString);
                                    String str = "";
                                    if (dataRate != null) {
                                        Log.e("suaave", dataRate.toString());
                                        str = TextUtils.join(",", dataRate);
                                        Log.e("itemlist", str);
                                        dialog.dismiss();
                                    } else {
                                        holder.check_box_store.setChecked(false);
                                        dataViews.get(position).setSelected(false);
                                        str = "";
                                        dialog.dismiss();
                                    }
                                    recyclerViewClickListener.SingleClick(str, position);
                                   // hideKeyboard(activity);
                                }
                            });
                            ivClose1 = dialog.findViewById(R.id.ivClose);
                            ivClose1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String str = "";
                                    if (dataRate != null) {
                                        Log.e("suaave", dataRate.toString());
                                        str = TextUtils.join(",", dataRate);
                                        Log.e("itemlist", str);
                                        dialog.dismiss();
                                    } else {
                                        holder.check_box_store.setChecked(false);
                                        str = "";
                                        dataViews.get(position).setSelected(false);
                                        dialog.dismiss();
                                    }
                                    holder.check_box_store.setChecked(false);
                                    dataViews.get(position).setSelected(false);
                                }
                            });
                            dialog.show();
                        }

                    } else if (store.getStoreTypeId() == 101) {


                        DialogUtil.SubStoreType(mContext, store.getJsonArray(), "allchecked", position,"yes", new DialogListnerSubSTore() {


                            @Override
                            public void onClickDetails(String position, String fathername, String dob, String id) {
                          /*  ApiCallSubmitOcr(name, fathername, dob, id, voterDetailsId, filename, fileUrl);

                            ApiCallSubmitKYC(name, fathername, dob, id);*/


                                holder.check_box_store.setChecked(false);
                                dataViews.get(Integer.parseInt(position)).setSelected(false);
                                DialogUtil.dismiss();
                            }

                            @Override
                            public void onStoreType(Integer positin, JSONObject jsonObject) {


                            }

                            @Override
                            public void onStoreType1(Integer positin, JSONObject jsonObject, List<SubStoreType> subStoreTypeList) {

                                String datashow = "";
                                for (int i = 0; i < subStoreTypeList.size(); i++) {
                                    datashow = datashow + subStoreTypeList.get(i).getStoreType() + ":" + subStoreTypeList.get(i).getRate() + "\n";
                                }
                                holder.tvSubType.setText(datashow);
                                dataViews.get(position).setSelected(true);
                                dataViews.get(position).setList(subStoreTypeList);
                                DialogUtil.dismiss();

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }

                        });

/*

                    if (holder.check_box_store.isChecked()) {
                        TextView tvtitleStoreType;
                        RecyclerView rvItems1;
                        Button btSubmit1;
                        Dialog dialog;
                        ImageView ivClose1;
                        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialogue_popup_list);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(true);

                        tvtitleStoreType = dialog.findViewById(R.id.tvTitleListPopup);
                        tvtitleStoreType.setText("Sub Store");
                        rvItems1 = dialog.findViewById(R.id.rvListPopup);
                        btSubmit1 = dialog.findViewById(R.id.btSubmitDetail);


                        List<SubStoreType> subStoreTypeList=new ArrayList<>();
                        JSONArray jArray = (JSONArray) store.getJsonArray();
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    Gson gson = new Gson();
                                    SubStoreType obj = gson.fromJson(jArray.getJSONObject(i).toString(), SubStoreType.class);
                                    subStoreTypeList.add(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        customPopupSubStoreTypeAdapter = new CustomPopupRateSubStoreTypeAdapter(subStoreTypeList, mContext, "StoreType", this, "allchecked");

                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

                        rvItems1.setLayoutManager(horizontalLayoutManager1);

                        rvItems1.setAdapter(customPopupSubStoreTypeAdapter);

                        btSubmit1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // tvVendorTypeDetail.setText(selectedString);
                                String str = "";
                                if (dataRate != null) {
                                    String showData = "";
                                    Log.e("suaave", dataRate.toString());
                                    str = TextUtils.join(",", dataRate);
                                    Log.e("itemlist", str);

                                } else {
                                    holder.check_box_store.setChecked(false);
                                    dataViews.get(position).setSelected(false);
                                    str = "";
                                    dialog.dismiss();
                                }
                                recyclerViewClickListener.SingleClick(str, position);
                                hideKeyboard(activity);
                            }
                        });
                        ivClose1 = dialog.findViewById(R.id.ivClose);
                        ivClose1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String str = "";
                                if (dataRate != null) {
                                    Log.e("suaave", dataRate.toString());
                                    str = TextUtils.join(",", dataRate);
                                    Log.e("itemlist", str);
                                    dialog.dismiss();
                                } else {
                                    holder.check_box_store.setChecked(false);
                                    str = "";
                                    dataViews.get(position).setSelected(false);
                                    dialog.dismiss();
                                }
                                holder.check_box_store.setChecked(false);
                                dataViews.get(position).setSelected(false);

                            }
                        });


                        dialog.show();*/


                    } else if (store.getStoreTypeId() == 102) {


                        DialogUtil.SubStoreType(mContext, store.getJsonArray(), "allchecked", position,"yes", new DialogListnerSubSTore() {


                            @Override
                            public void onClickDetails(String position, String fathername, String dob, String id) {
                          /*  ApiCallSubmitOcr(name, fathername, dob, id, voterDetailsId, filename, fileUrl);

                            ApiCallSubmitKYC(name, fathername, dob, id);*/


                                holder.check_box_store.setChecked(false);
                                dataViews.get(Integer.parseInt(position)).setSelected(false);
                                DialogUtil.dismiss();
                            }

                            @Override
                            public void onStoreType(Integer positin, JSONObject jsonObject) {


                            }

                            @Override
                            public void onStoreType1(Integer positin, JSONObject jsonObject, List<SubStoreType> subStoreTypeList) {

                                String datashow = "";
                                for (int i = 0; i < subStoreTypeList.size(); i++) {
                                    datashow = datashow + subStoreTypeList.get(i).getStoreType() + ":" + subStoreTypeList.get(i).getRate() + "\n";
                                }
                                holder.tvSubType.setText(datashow);
                                dataViews.get(position).setSelected(true);
                                dataViews.get(position).setList(subStoreTypeList);
                                DialogUtil.dismiss();

                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }

                        });
                    } else {


                        DialogUtil.SubStoreType(mContext, store.getJsonArray(), "", position,"yes", new DialogListnerSubSTore() {


                            @Override
                            public void onClickDetails(String positon, String fathername, String dob, String id) {
                          /*  ApiCallSubmitOcr(name, fathername, dob, id, voterDetailsId, filename, fileUrl);

                            ApiCallSubmitKYC(name, fathername, dob, id);*/
                         /*   holder.check_box_store.setChecked(false);
                            dataViews.get(Integer.parseInt(positon)).setSelected(false);
                            DialogUtil.dismiss();*/
                            }

                            @Override
                            public void onStoreType(Integer positin, JSONObject jsonObject) {
                                // Log.e("finallyinRate", jsonObject.toString());
                                dataViews.get(position).setSelected(false);
                                holder.check_box_store.setChecked(false);
                                DialogUtil.dismiss();
                            }

                            @Override
                            public void onStoreType1(Integer positin, JSONObject jsonObject, List<SubStoreType> subStoreTypeList) {

                                String datashow = "";
                                for (int i = 0; i < subStoreTypeList.size(); i++) {
                                    if (subStoreTypeList.get(i).isSelected()) {
                                        datashow = datashow + subStoreTypeList.get(i).getStoreType() + ":" + subStoreTypeList.get(i).getRate() + "\n";
                                        dataViews.get(position).setSelected(true);
                                    }
                                }
                                holder.tvSubType.setText(datashow);
                                dataViews.get(position).setList(subStoreTypeList);
                                DialogUtil.dismiss();
                            }

                            @Override
                            public void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress) {

                            }

                            @Override
                            public void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc) {

                            }

                        });

                    }
                }

                if (holder.check_box_store.isChecked()) {
                   /* if (!store.getStoreType().contains(Constants.CAC_STORE))
                        holder.edt_store_amount.setEnabled(true);*/
                    dataViews.get(position).setSelected(true);
                } else {
                 //   holder.edt_store_amount.setEnabled(false);
                    dataViews.get(position).setSelected(false);
                    holder.tvSubType.setText("");
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

/*
                if (store.getStoreTypeId() == 8) {
                    if (holder.check_box_store.isChecked()) {
                        TextView tvMessage;
                        Button btSubmit1;
                        Dialog dialog;

                        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialogue_message);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(true);

                        tvMessage = dialog.findViewById(R.id.tvMessage);
                        tvMessage.setText(Message);
                        btSubmit1 = dialog.findViewById(R.id.btSubmit);
                        btSubmit1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // tvVendorTypeDetail.setText(selectedString);
                                for (int i = 0; i < dataViews.size(); i++) {
                                    if (dataViews.get(i).getStoreTypeId() == 4) {
                                        dataViews.get(i).setSelected(false);
                                        dataViews.get(i).setRate("0");
                                        // holder.check_box_store.setChecked(false);
                                    } else {

                                    }
                                }
                                int notifyPosition = 0;
                                for (int i = 0; i < dataViews.size(); i++) {

                                    if (dataViews.get(i).getStoreTypeId() == 4) {
                                        notifyPosition = i;
                                    }
                                }
                                notifyItemChanged(notifyPosition);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }

                }
                if (store.getStoreTypeId() == 4) {
                    for (int i = 0; i < dataViews.size(); i++) {
                        if (dataViews.get(i).getStoreTypeId() == 8) {
                            dataViews.get(i).setSelected(false);
                            dataViews.get(i).setRate("0");
                            // holder.check_box_store.setChecked(false);
                        } else {

                        }
                    }

                    int notifyPosition = 0;
                    for (int i = 0; i < dataViews.size(); i++) {

                        if (dataViews.get(i).getStoreTypeId() == 8) {
                            notifyPosition = i;
                        }
                    }
                    notifyItemChanged(notifyPosition);
                   // notifyItemChanged(notifyPosition);
                }*/

            }
        });


        if (store.getStoreType().contains(Constants.CAC_STORE)) {
            holder.check_box_store.setChecked(false);
            holder.check_box_store.setEnabled(true);
            holder.edt_store_amount.setText("10%/3% GMV");
            holder.edt_store_amount.setEnabled(false);
        }

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
        TextView tvSubType;

        public ItemHolder(View itemView) {
            super(itemView);
            tvSubType = itemView.findViewById(R.id.tvSubType);
            check_box_store = itemView.findViewById(R.id.check_box_store);
            tv_store_name = itemView.findViewById(R.id.tv_store_name);
            edt_store_amount = itemView.findViewById(R.id.edt_store_amount);
            vSeperate = itemView.findViewById(R.id.viewSeperate);
            rvSeperateRight = itemView.findViewById(R.id.rvRightMain);
            edt_store_amount.setEnabled(false);
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}