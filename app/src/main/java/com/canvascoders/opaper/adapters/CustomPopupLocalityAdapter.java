package com.canvascoders.opaper.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.ObjectPopup;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class CustomPopupLocalityAdapter extends RecyclerView.Adapter<CustomPopupLocalityAdapter.RecordHolder> {

    List<ObjectPopup> vendorTypeList = new ArrayList<>();
    Context context;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    String value = "1";
    RecyclerViewClickListener onTaskCompleted;
    String Type;

    public CustomPopupLocalityAdapter(List<ObjectPopup> vendorTypeList, Context context, RecyclerViewClickListener onTaskCompleted, String type) {
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

        holder.tvName.setText(vendorTypeList.get(position).getUserName());
        holder.cbSelect.setChecked(vendorTypeList.get(position).isSelected());
        holder.cbSelect.setTag(new Integer(position));

        //for default check in first item
        if (position == 0 && vendorTypeList.get(0).isSelected() && holder.cbSelect.isChecked()) {
            lastChecked = holder.cbSelect;
            lastCheckedPos = 0;
        }

        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();

                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        vendorTypeList.get(lastCheckedPos).setSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                   if(Type.equalsIgnoreCase("Locality")){
                        onTaskCompleted.SingleClick("Locality",position);
                    }


                } else {

                    lastChecked = null;
                    vendorTypeList.get(clickedPos).setSelected(cb.isChecked());
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
