package com.canvascoders.opaper.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class DeliveryBoysAdapter extends RecyclerView.Adapter<DeliveryBoysAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    String value = "1";
    boolean undoOn;
    RecyclerViewClickListener recyclerViewClickListener;


    public DeliveryBoysAdapter(List<Datum> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.moreitemList = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_delivery_boys, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvName.setText(moreitemList.get(position).getName());
        holder.tvAddress.setText(moreitemList.get(position).getRouteNumber());
        holder.tvMobile.setText(moreitemList.get(position).getPhoneNumber());
        Log.e("URL", "" + Constants.BaseImageURL + moreitemList.get(position).getImage());
        Glide.with(context).load(Constants.BaseImageURL + moreitemList.get(position).getImage()).placeholder(R.drawable.image_placeholder).into(holder.image_icon);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view, position);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onLongClick(view, position);
            }
        });
        holder.tvStatus.setText(moreitemList.get(position).getBoystatus());


    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView image_icon;
        TextView tvName, tvAddress, tvMobile, tvDelete, tvEdit, tvStatus;
        LinearLayout linear_item;

        public RecordHolder(View view) {
            super(view);
            image_icon = view.findViewById(R.id.ivDelBoyImage);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvAddress = view.findViewById(R.id.tvRoute);
            tvDelete = view.findViewById(R.id.tvDelete);
            tvEdit = view.findViewById(R.id.tvEdit);
            tvStatus = view.findViewById(R.id.tvStatus);
        }
    }


}
