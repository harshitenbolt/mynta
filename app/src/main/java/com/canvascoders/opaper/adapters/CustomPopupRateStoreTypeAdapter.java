package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canvascoders.opaper.Beans.MensaAlteration;
import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.canvascoders.opaper.utils.Constants.dataRate;

public class CustomPopupRateStoreTypeAdapter extends RecyclerView.Adapter<CustomPopupRateStoreTypeAdapter.RecordHolder> {

    List<MensaAlteration> vendorTypeList = new ArrayList<>();
    Context context;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    String value = "1";
    RecyclerViewClickListener onTaskCompleted;
    List<MensaAlteration> itemidlist = new ArrayList<>();
    List<String> titlelist = new ArrayList<>();
    String Type;

    public CustomPopupRateStoreTypeAdapter(List<MensaAlteration> vendorTypeList, Context context, String type, View.OnClickListener recyclerViewClickListener) {
        this.vendorTypeList = vendorTypeList;
        this.context = context;
        this.onTaskCompleted = onTaskCompleted;
        this.Type = type;
    }


    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popup_list, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {

        holder.tvName.setText(vendorTypeList.get(position).getSubStoreType());
        //
        // holder.cbSelect.setChecked(vendorTypeList.get(position).isSelected());
        holder.cbSelect.setTag(new Integer(position));

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

        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CheckBox cb = (CheckBox) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();

                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        // lastChecked.setChecked(false);
                        vendorTypeList.get(lastCheckedPos).setSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                    itemidlist.add(vendorTypeList.get(clickedPos));

                    String data = "";
                    titlelist = new ArrayList<>();
                    for (int i = 0; i < itemidlist.size(); i++) {
                        data = itemidlist.get(i).getSubStoreType() + "," + data;
                        titlelist.add(itemidlist.get(i).getSubStoreTypeId());

                    }
                    Log.e("itemid+select", vendorTypeList.get(clickedPos).getSubStoreType());


                } else {

                    lastChecked = null;
                    itemidlist.remove(vendorTypeList.get(clickedPos));
                    titlelist.remove(vendorTypeList.get(clickedPos));
                    vendorTypeList.get(clickedPos).setSelected(cb.isChecked());
                    titlelist = new ArrayList<>();
                    for (int i = 0; i < itemidlist.size(); i++) {
                        //   data = itemidlist.get(i).getSubStoreType()+","+data ;
                        titlelist.add(itemidlist.get(i).getSubStoreTypeId());

                    }
                    Log.e("itemid+deselect", vendorTypeList.get(clickedPos).getSubStoreType());
                }


                if (titlelist.size() > 0) {
                    String[] title_list = titlelist.toArray(new String[0]);
                    dataRate = title_list;
                    Log.e("itemid", Arrays.toString(title_list));
                } else {
                    dataRate = null;
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorTypeList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        TextView tvName;
        LinearLayout linear_item;

        public RecordHolder(View view) {
            super(view);
            cbSelect = view.findViewById(R.id.cbListItem);
            tvName = view.findViewById(R.id.tvContent);

        }
    }
}
