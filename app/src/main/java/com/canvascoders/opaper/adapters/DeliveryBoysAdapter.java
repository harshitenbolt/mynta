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

import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class DeliveryBoysAdapter extends RecyclerView.Adapter<DeliveryBoysAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    String value = "1";


    public DeliveryBoysAdapter(List<Datum> moreitemList, Context context) {
        this.moreitemList = moreitemList;
        this.context = context;
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

        Log.e("URL", "" + Constants.BaseImageURL + moreitemList.get(position).getImage());
        Glide.with(context).load(Constants.BaseImageURL + moreitemList.get(position).getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image_icon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.image_icon.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView image_icon;
        TextView tvName, tvAddress;
        LinearLayout linear_item;

        public RecordHolder(View view) {
            super(view);
            image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvName = view.findViewById(R.id.tv_name);
            tvAddress = view.findViewById(R.id.tv_address);
        }
    }
}
