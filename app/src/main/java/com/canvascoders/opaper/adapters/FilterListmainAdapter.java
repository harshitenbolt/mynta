package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canvascoders.opaper.Beans.GetTaskCategoryListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;


import java.util.List;

public class FilterListmainAdapter extends RecyclerView.Adapter<FilterListmainAdapter.RecordHolder> {

    private List<Datum> maincaetgoryList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public FilterListmainAdapter(List<Datum> maincaetgoryList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.maincaetgoryList = maincaetgoryList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_main_category, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordHolder holder, final int position) {
        holder.tvName.setText(maincaetgoryList.get(position).getLabel());
        holder.llMainList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maincaetgoryList.get(position).getLabel().equalsIgnoreCase("Search")){
                    recyclerViewClickListener.onLongClick(v, position,"1");
                }
                else{
                    recyclerViewClickListener.onLongClick(v, position,"");
                }
                recyclerViewClickListener.onClick(v,  position);
              /*  int color = Color.TRANSPARENT;
                Drawable background = holder.llMainList.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();*/
                //   holder.llMainList.setBackgroundColor(context.getResources().getColor(R.color.whitePrimary));
            }
        });


    }

    @Override
    public int getItemCount() {
        return maincaetgoryList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvAddressType, tvMakeDefault;
        Button btEdit, btRemove;
        LinearLayout llMainList;

        public RecordHolder(View view) {
            super(view);
            llMainList = view.findViewById(R.id.llMainCategory);
            tvName = view.findViewById(R.id.tvMainCategory);

        }
    }

}
