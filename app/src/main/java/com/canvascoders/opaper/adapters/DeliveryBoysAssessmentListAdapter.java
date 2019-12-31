package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.List;

public class DeliveryBoysAssessmentListAdapter extends RecyclerView.Adapter<DeliveryBoysAssessmentListAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public DeliveryBoysAssessmentListAdapter(List<Datum> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
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
        holder.tvSubject.setText(moreitemList.get(position).getSubjectdesc());
        if (moreitemList.get(position).getPriority().equalsIgnoreCase("0")) {
            holder.tvPriority.setText("low");

        } else if (moreitemList.get(position).getPriority().equalsIgnoreCase("1")) {
            holder.tvPriority.setText("Medium");
            holder.tvPriority.setTextColor(context.getResources().getColor(R.color.colorBlue));
        } else if (moreitemList.get(position).getPriority().equalsIgnoreCase("2")) {
            holder.tvPriority.setText("High");
            holder.tvPriority.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        if (moreitemList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText("Reopen");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_green));
            } else {
                holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_green));
            }
        } else if (moreitemList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorYellow));
            holder.tvStatus.setText("pending");

            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_yellow));
            } else {
                holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_yellow));
            }


        } else if (moreitemList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setText("in-progress");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorBlue));

            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_blue));
            } else {
                holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_blue));
            }

        } else if (moreitemList.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tvStatus.setText("closed");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_red));
            } else {
                holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_circle_bordercolor_red));
            }
        }

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivAttachment;
        TextView tvTicketName, tvPriority, tvStatus, tvSubject;
        CardView cvMain;

        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvTicketName = view.findViewById(R.id.tvTicketNo);
            cvMain = view.findViewById(R.id.cvMain);
            //tvAddress = view.findViewById(R.id.tv_address);
            tvPriority = view.findViewById(R.id.tvPriority);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvSubject = view.findViewById(R.id.tvSubject);
            ivAttachment = view.findViewById(R.id.ivAttachment);
        }
    }
}
