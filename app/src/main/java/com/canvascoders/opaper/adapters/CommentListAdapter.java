package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
        if (moreitemList.get(position).getAgentDetail() != null) {
            holder.tvUser.setText(moreitemList.get(position).getAgentDetail().getName());
            // Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).into(holder.ivAttachment);
            Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).asBitmap().centerCrop().placeholder(R.drawable.image_placeholder).into(new BitmapImageViewTarget(holder.ivAttachment) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivAttachment.setImageDrawable(circularBitmapDrawable);
                }
            });

            holder.tvDateforAdmin.setVisibility(View.GONE);
            holder.tvDateforAgent.setVisibility(View.VISIBLE);
            holder.tvDateforAgent.setText(moreitemList.get(position).getCreatedatformat());
            holder.ivAttachment.setVisibility(View.VISIBLE);
            holder.ivAttachment2.setVisibility(View.GONE);
        } else {
            holder.tvUser.setText(moreitemList.get(position).getAdminDetail().getName());
            //Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).into(holder.ivAttachment2);
            Glide.with(context).load(moreitemList.get(position).getAttachmentUrl()).asBitmap().centerCrop().placeholder(R.drawable.image_placeholder).into(new BitmapImageViewTarget(holder.ivAttachment2) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivAttachment2.setImageDrawable(circularBitmapDrawable);
                }
            });
            holder.ivAttachment.setVisibility(View.GONE);
            holder.tvDateforAdmin.setVisibility(View.VISIBLE);
            holder.tvDateforAgent.setVisibility(View.GONE);
            holder.tvDateforAdmin.setText(moreitemList.get(position).getCreatedatformat());
            holder.ivAttachment2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivAttachment, ivAttachment2;
        TextView tvComment, tvUser, tvStatus;
        CardView cvMain;
        TextView tvDateforAgent, tvDateforAdmin;

        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvComment = view.findViewById(R.id.tvComment);
            // cvMain = view.findViewById(R.id.cvMain);
            //tvAddress = view.findViewById(R.id.tv_address);
            tvUser = view.findViewById(R.id.tvUsername);
            tvDateforAdmin = view.findViewById(R.id.tvDateforAdmin);
            tvDateforAgent = view.findViewById(R.id.tvDateforAgent);
            /*tvStatus = view.findViewById(R.id.tvStatus);

             */
            ivAttachment = view.findViewById(R.id.ivAttachment1);
            ivAttachment2 = view.findViewById(R.id.ivAttachment2);
        }
    }
}
