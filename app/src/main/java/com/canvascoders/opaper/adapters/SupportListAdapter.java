package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
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

import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class SupportListAdapter extends RecyclerView.Adapter<SupportListAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;


    public SupportListAdapter(List<Datum> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.moreitemList = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.support_list, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvTicketName.setText(moreitemList.get(position).getTicketNumber());
        Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).into(holder.ivAttachment);
        holder.tvPriority.setText(moreitemList.get(position).getPriority());
        holder.tvStatus.setText(moreitemList.get(position).getStatus());
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivAttachment;
        TextView tvTicketName, tvPriority, tvStatus;
        CardView cvMain;

        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvTicketName = view.findViewById(R.id.tvTicketNo);
            cvMain = view.findViewById(R.id.cvMain);
            //tvAddress = view.findViewById(R.id.tv_address);
            tvPriority = view.findViewById(R.id.tvPriority);
            tvStatus = view.findViewById(R.id.tvStatus);
            ivAttachment = view.findViewById(R.id.ivAttachment);
        }
    }
}
