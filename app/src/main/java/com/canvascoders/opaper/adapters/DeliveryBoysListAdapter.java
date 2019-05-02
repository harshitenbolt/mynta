package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.canvascoders.opaper.Beans.DelBoysResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class DeliveryBoysListAdapter extends RecyclerView.Adapter<DeliveryBoysListAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    String value = "1";


    public DeliveryBoysListAdapter(List<Datum> moreitemList, Context context) {
        this.moreitemList = moreitemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_del_boy, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvName.setText(moreitemList.get(position).getName());
        holder.tvMobile.setText(moreitemList.get(position).getPhoneNumber());
        holder.tvRoute.setText(moreitemList.get(position).getRouteNumber());
        holder.tvVehicle.setText(moreitemList.get(position).getVehicleForDelivery());

        Log.e("URL", "" + Constants.BaseImageURL + moreitemList.get(position).getImage());
        Glide.with(context).load(Constants.BaseImageURL+moreitemList.get(position).getImage()).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName, tvMobile,tvRoute,tvVehicle;
        LinearLayout linear_item;

        public RecordHolder(View view) {
            super(view);
            ivProfile = view.findViewById(R.id.ivDelBoyImage);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvRoute = view.findViewById(R.id.tvRoute);
            tvVehicle = view.findViewById(R.id.tvVehicle);
        }
    }
}
