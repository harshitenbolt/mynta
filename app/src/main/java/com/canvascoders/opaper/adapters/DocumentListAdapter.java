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
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.Result;
import com.canvascoders.opaper.Beans.SupportListResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.RecordHolder> {

    private List<Result> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public DocumentListAdapter(List<Result> moreitemList, Context context) {
        this.moreitemList = moreitemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.docsigned_list, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        if (moreitemList.get(position).getSignStatus() == 1) {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.ivStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.greencheck));
            } else {
                holder.ivStatus.setBackground(context.getResources().getDrawable(R.drawable.greencheck));
            }
        } else {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.ivStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.error));
            } else {
                holder.ivStatus.setBackground(context.getResources().getDrawable(R.drawable.error));
            }

        }
        holder.tvDocName.setText(moreitemList.get(position).getTitle());
        holder.tvDate.setText(moreitemList.get(position).getAgreementDate());

    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStatus;
        TextView tvDocName, tvDate, tvStatus, tvSubject;


        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            tvDocName = view.findViewById(R.id.tvDocName);
            //tvAddress = view.findViewById(R.id.tv_address);
            tvDate = view.findViewById(R.id.tvDate);
            ivStatus = view.findViewById(R.id.ivStatus);
        }
    }
}
