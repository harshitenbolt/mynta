package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canvascoders.opaper.Beans.DetailsAssessDelBoyResponse.AssessmentTriedResult;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.Result;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.List;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.RecordHolder> {

    private List<AssessmentTriedResult> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public AssessmentListAdapter(List<AssessmentTriedResult> moreitemList, Context context) {
        this.moreitemList = moreitemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_list, parent, false);
        RecordHolder gvh = new RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {

        holder.tvDocName.setText(moreitemList.get(position).getLabel());
        holder.tvStatus.setText(moreitemList.get(position).getPercentage()+"");

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
           // tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvStatus);
        }
    }
}
