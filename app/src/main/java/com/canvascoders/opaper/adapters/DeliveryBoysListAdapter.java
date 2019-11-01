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

import com.canvascoders.opaper.Beans.DelBoysResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class DeliveryBoysListAdapter extends RecyclerView.Adapter<DeliveryBoysListAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    String value = "1",flag;


    public DeliveryBoysListAdapter(List<Datum> moreitemList, Context context,String flag) {
        this.moreitemList = moreitemList;
        this.context = context;
        this.flag = flag;
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
        //holder.tvVehicle.setText(moreitemList.get(position).getVehicleForDelivery());

        Log.e("URL", "" + Constants.BaseImageURL + moreitemList.get(position).getImage());
        Glide.with(context).load(Constants.BaseImageURL+moreitemList.get(position).getImage()).placeholder(R.drawable.image_placeholder).into(holder.ivProfile);

        if(flag.equalsIgnoreCase("1")){
            holder.ivDelete.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivDelete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile,ivDelete;
        TextView tvName, tvMobile,tvRoute,tvVehicle;
        LinearLayout linear_item;

        public RecordHolder(View view) {
            super(view);
            ivProfile = view.findViewById(R.id.ivDelBoyImage);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvRoute = view.findViewById(R.id.tvRoute);
            ivDelete = view.findViewById(R.id.ivDelete);
       //     tvVehicle = view.findViewById(R.id.tvVehicle);
        }
    }
}
