package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.canvascoders.opaper.Beans.MensaAlteration;
import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.Beans.StoreTypeBean;
import com.canvascoders.opaper.Beans.SubStoreType;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.StoreTypeListingActivity;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.canvascoders.opaper.utils.Constants.dataRate;

public class CustomPopupRateSubStoreTypeAdapter extends RecyclerView.Adapter<CustomPopupRateSubStoreTypeAdapter.ItemHolder> {

    List<SubStoreType> vendorTypeList = new ArrayList<>();
    Context context;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    String value = "1";
    RecyclerViewClickListener onTaskCompleted;
    List<MensaAlteration> itemidlist = new ArrayList<>();
    List<String> titlelist = new ArrayList<>();
    List<MensaAlteration> finalData = new ArrayList<>();
    String Type;
    Context mContext;
    String allchecked = "";
    Integer storeSelcetd = 0;

    public CustomPopupRateSubStoreTypeAdapter(List<SubStoreType> vendorTypeList, Context context, String type, String allchecked) {
        this.vendorTypeList = vendorTypeList;
        this.context = context;
        this.onTaskCompleted = onTaskCompleted;
        this.Type = type;
        this.context = context;
        this.allchecked = allchecked;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtype_rate, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        dataRate = null;

        holder.tvName.setText("" + vendorTypeList.get(position).getStoreType());
        if (allchecked.equalsIgnoreCase("allchecked")) {
            holder.cbSelect.setChecked(true);
            holder.cbSelect.setClickable(false);
            holder.cbSelect.setEnabled(false);
            vendorTypeList.get(position).setSelected(true);
        }

        if (vendorTypeList.get(position).getStoreTypeId() == 10) {
            holder.edt_store_amount.setVisibility(View.GONE);
        } else if (vendorTypeList.get(position).getStoreTypeId() == 11) {
            holder.edt_store_amount.setVisibility(View.GONE);

        } else if (vendorTypeList.get(position).getStoreTypeId() == 9) {
            holder.edt_store_amount.setVisibility(View.GONE);

        } else if (vendorTypeList.get(position).getStoreTypeId() == 7) {
            holder.edt_store_amount.setVisibility(View.GONE);
        } else if (vendorTypeList.get(position).getStoreTypeId() == 8) {
            holder.edt_store_amount.setVisibility(View.GONE);
        } else {
            holder.edt_store_amount.setVisibility(View.VISIBLE);
        }
        // holder.edt_store_amount.setText(store.getRate());


        //
        // holder.cbSelect.setChecked(vendorTypeList.get(position).isSelected());
        //    holder.cbSelect.setTag(new Integer(position));

      /*  if (dataRate != null) {
            if (dataRate.toString().contains(String.valueOf(position + 1))) {

                holder.cbSelect.setChecked(true);
            } else {
                holder.cbSelect.setChecked(false);
            }
        }*/

        //for default check in first item
        /*if (position == 0 && vendorTypeList.get(0).isSelected() && holder.cbSelect.isChecked()) {
            lastChecked = holder.cbSelect;
            lastCheckedPos = 0;
        }*/

        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 0; i < vendorTypeList.size(); i++) {
            if (vendorTypeList.get(position).isSelected()) {
                String rate1 = "";
                if (vendorTypeList.get(position).getRate().equalsIgnoreCase("")) {
                    rate1 = "0";

                } else {
                    rate1 = vendorTypeList.get(position).getRate();
                }

                stringArrayList.add(vendorTypeList.get(position).getStoreTypeId() + ":" + rate1);
            }
        }
        String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
        dataRate = stringArray;


        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allchecked.equalsIgnoreCase("allchecked")) {
                    holder.edt_store_amount.setEnabled(true);
                    ArrayList<String> stringArrayList = new ArrayList<String>();
                    for (int i = 0; i < vendorTypeList.size(); i++) {
                        if (vendorTypeList.get(position).isSelected()) {
                            String rate1 = "";
                            if (vendorTypeList.get(position).getRate().equalsIgnoreCase("")) {
                                rate1 = "0";

                            } else {
                                rate1 = vendorTypeList.get(position).getRate();
                            }

                            stringArrayList.add(vendorTypeList.get(position).getSubStoreType() + ":" + rate1);
                        }
                    }
                    String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
                    dataRate = stringArray;

                } else {
                    if (holder.cbSelect.isChecked()) {
                        if (vendorTypeList.get(position).getStoreTypeId() == 8) {
                            vendorTypeList.get(0).setSelected(false);
                            notifyDataSetChanged();
                        }
                        if (vendorTypeList.get(position).getStoreTypeId() == 4) {
                            vendorTypeList.get(1).setSelected(false);
                            notifyDataSetChanged();
                        }
                        holder.edt_store_amount.setEnabled(true);
                        vendorTypeList.get(position).setSelected(true);

                    } else {
                        holder.edt_store_amount.setEnabled(false);
                        vendorTypeList.get(position).setSelected(false);
                        holder.edt_store_amount.setText("0");
                    }


                    ArrayList<String> stringArrayList = new ArrayList<String>();
                    for (int i = 0; i < vendorTypeList.size(); i++) {
                        if (vendorTypeList.get(position).isSelected()) {
                            String rate1 = "";
                            if (vendorTypeList.get(position).getRate().equalsIgnoreCase("")) {
                                rate1 = "0";

                            } else {
                                rate1 = vendorTypeList.get(position).getRate();
                            }

                            stringArrayList.add(vendorTypeList.get(position).getSubStoreType() + ":" + rate1);
                        }
                    }
                    String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
                    dataRate = stringArray;


                }

            }
        });


        //  holder.edt_store_amount.setText(store.getRate());




        /*holder.edt_store_amount.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );*/


        holder.edt_store_amount.addTextChangedListener(new

                                                               TextWatcher() {
                                                                   @Override
                                                                   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                   }

                                                                   @Override
                                                                   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                   }

                                                                   @Override
                                                                   public void afterTextChanged(Editable editable) {

                                                                       // SubStoreType.setRate(holder.edt_store_amount.getText() + "");

                                                                       try {
                                                                           float rate = Float.parseFloat(editable.toString());
                                                                           vendorTypeList.get(position).setRate("" + rate);
                                                                           Log.e("dataset", vendorTypeList.get(position).getRate());
                                                                           JsonArray jsonArray = new JsonArray();

                                                                           ArrayList<String> stringArrayList = new ArrayList<String>();
                                                                           for (int i = 0; i < vendorTypeList.size(); i++) {
                                                                               if (vendorTypeList.get(position).isSelected()) {
                                                                                   String rate1 = "";
                                                                                   if (vendorTypeList.get(position).getRate().equalsIgnoreCase("")) {
                                                                                       rate1 = "0";
                                                                                   } else {
                                                                                       rate1 = vendorTypeList.get(position).getRate();
                                                                                   }

                                                                                   stringArrayList.add(vendorTypeList.get(position).getIdType() + ":" + rate1);

                                                                               }
                                                                           }
                                                                           String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
                                                                           dataRate = stringArray;
                                                                       } catch (Exception e) {
                                                                           e.printStackTrace();
                                                                           Log.e("dataset", e.getMessage());
                                                                           if (e.getMessage().equalsIgnoreCase("empty String")) {
                                                                               vendorTypeList.get(position).setRate("");
                                                                           }
//                                                                           Toast.makeText(mContext, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                                                                       }

                                                                   }

                                                               });

      /*  JsonArray jsonArray = new JsonArray();

        // checking from neutral stores to get updated data.
        for (
                int i = 0; i < vendorTypeList.size(); i++) {
            if (SubStoreType.isSelected()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("store_type", obj.getSubStoreType());
           *//*     if (!neutralStoreList.get(i).getStoreType().contains(Constants.CAC_STORE) && !neutralStoreList.get(i).getStoreType().contains(Constants.ASSISTED)) {

                    if (!neutralStoreList.get(i).getStoreType().contains("Mensa - Alteration")) {

                        if (neutralStoreList.get(i).getRate() != null && neutralStoreList.get(i).getRate().length() > 0) {
                            try {
//                        float rate = Float.parseFloat(neutralStoreList.get(i).getRate());
//                        if (rate > 0)
                                if (neutralStoreList.get(i).getRate().equalsIgnoreCase("0") || neutralStoreList.get(i).getRate().equalsIgnoreCase("0.0")) {
                                    Toast.makeText(this, "Please enter valid rate", Toast.LENGTH_SHORT).show();
                                } else {
                                    jsonObject.addProperty("rate", "" + neutralStoreList.get(i).getRate());
                                }

//                        else {
//                            Toast.makeText(StoreTypeListingActivity.this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(StoreTypeListingActivity.this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                    jsonObject.addProperty("rate", neutralStoreList.get(i).getRate());
                        } else {
                            Toast.makeText(StoreTypeListingActivity.this, "Issue with rate:" + neutralStoreList.get(i).getStoreType(), Toast.LENGTH_LONG).show();
                            return;

                        }

                    } else {
                        jsonObject.addProperty("rate", "0");
                        jsonObject.addProperty("sub_store_type", alterationselected);
                    }
                } else {  // ITS s CAC store send rate "0"
                    jsonObject.addProperty("rate", "0");
                }*//*
                jsonArray.add(jsonObject);
            }

            //  String[] title_list = jsonArray.toArray(new String[0]);
            dataRate = jsonArray.toString();
            Log.e("itemid", dataRate);*/


        // checking from rejected stores to get updated data.


        if (vendorTypeList.get(position).isSelected()) {

//                if (store.getIsApproved().equalsIgnoreCase("1")) {
//                    holder.check_box_store.setEnabled(false);
//                } else {
//                    holder.check_box_store.setEnabled(true);
//                    holder.check_box_store.setChecked(true);
//                    holder.edt_store_amount.setEnabled(true);
//                }

            holder.cbSelect.setChecked(true);
        } else {
            // vendorTypeList.get(position).setSelected(false);
            holder.cbSelect.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return vendorTypeList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        CheckBox cbSelect;
        TextView tvName;
        LinearLayout linear_item;
        EditText edt_store_amount;

        public ItemHolder(View itemView) {
            super(itemView);
            cbSelect = itemView.findViewById(R.id.cbListItem);
            tvName = itemView.findViewById(R.id.tvContent);
            edt_store_amount = itemView.findViewById(R.id.edt_store_amount);
            // edt_store_amount.setEnabled(false);
        }
    }
}
