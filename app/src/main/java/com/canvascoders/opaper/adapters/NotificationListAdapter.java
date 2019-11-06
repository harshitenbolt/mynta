package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.NotificationList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.RecordHolder> {

    private List<NotificationList> notificationLists;
    Context context;
    String value = "1";
    RecyclerViewClickListener recyclerViewClickListener;


    public NotificationListAdapter(List<NotificationList> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {

        this.notificationLists = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;

    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification, parent, false);
        RecordHolder holder = new RecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvDescription.setText(notificationLists.get(position).getDescription());
        if (notificationLists.get(position).getIsRead().equalsIgnoreCase("1")) {
            boolean change = true;
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.btView.setVisibility(View.GONE);
            holder.tvTime.setText(notificationLists.get(position).getUpdatedAt());

            holder.cdMain.setBackgroundResource(R.color.colorlightGrey);

        } else {
            holder.btView.setVisibility(View.VISIBLE);
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setVisibility(View.INVISIBLE);
            holder.tvTime.setText(notificationLists.get(position).getUpdatedAt());
            holder.btView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btView.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.cdMain.setBackgroundColor(context.getResources().getColor(R.color.colorlightGrey));
                    recyclerViewClickListener.onClick(v, position);

                }
            });


            holder.cdMain.setBackgroundResource(R.color.colorWhite);
        }

    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage, ivStatusImage;
        TextView tvDescription, tvTime;
        LinearLayout llMain;
        Button btView;
        CardView cdMain;


        public RecordHolder(View view) {
            super(view);

            tvDescription = view.findViewById(R.id.tvNotificationDesc);
            btView = view.findViewById(R.id.btView);
            tvTime = view.findViewById(R.id.tvTime);
            llMain = view.findViewById(R.id.llBackground);
            cdMain = view.findViewById(R.id.cdMain);

            //     tvVehicle = view.findViewById(R.id.tvVehicle);
        }
    }

    public void addItems(List<NotificationList> postItems) {
        notificationLists.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {

        notificationLists.add(new NotificationList());
        notifyItemInserted(notificationLists.size() - 1);
    }


}
