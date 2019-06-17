package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.canvascoders.opaper.Beans.CommentListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.RecordHolder> {

    private List<Datum> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;


    public CommentListAdapter(List<Datum> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.moreitemList = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvComment.setText(moreitemList.get(position).getComment());
        if(moreitemList.get(position).getAgentDetail()!=null){
            holder.tvUser.setText("By "+moreitemList.get(position).getAgentDetail().getName());
            Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).into(holder.ivAttachment);
            holder.ivAttachment2.setVisibility(View.GONE);
        }
        else{
            holder.tvUser.setText("By " + moreitemList.get(position).getAdminDetail().getName());
            Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).into(holder.ivAttachment2);
            holder.ivAttachment.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivAttachment,ivAttachment2;
        TextView tvComment, tvUser, tvStatus;
        CardView cvMain;

        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvComment = view.findViewById(R.id.tvComment);
           // cvMain = view.findViewById(R.id.cvMain);
            //tvAddress = view.findViewById(R.id.tv_address);
            tvUser = view.findViewById(R.id.tvUsername);
            /*tvStatus = view.findViewById(R.id.tvStatus);
            */
            ivAttachment = view.findViewById(R.id.ivAttachment1);
            ivAttachment2 = view.findViewById(R.id.ivAttachment2);
        }
    }
}
