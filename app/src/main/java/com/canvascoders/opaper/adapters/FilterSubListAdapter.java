package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canvascoders.opaper.Beans.GetTaskCategoryListResponse.SubCatetegory;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;


import java.util.List;

public class FilterSubListAdapter extends RecyclerView.Adapter<FilterSubListAdapter.RecordHolder> {

    private List<SubCatetegory> maincaetgoryList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public FilterSubListAdapter(List<SubCatetegory> maincaetgoryList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.maincaetgoryList = maincaetgoryList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_sub_category, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, final int position) {
        holder.tvName.setText(maincaetgoryList.get(position).getValue());
        if (maincaetgoryList.get(position).isChecked()) {
            holder.cbBox.setChecked(true);
        } else {
            holder.cbBox.setChecked(false);
        }
        holder.cbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerViewClickListener.SingleClick("1", position);

                } else {
                    recyclerViewClickListener.SingleClick("0", position);
                }
            }
        });


    }

    public void updateList(List<SubCatetegory> list){
        maincaetgoryList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return maincaetgoryList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox cbBox;

        public RecordHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvSubCategory);
            cbBox = view.findViewById(R.id.cbMain);
            //  llMainList = view.findViewById(R.id.llMainCategory);
        }
    }

}
