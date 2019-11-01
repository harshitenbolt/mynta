package com.canvascoders.opaper.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import java.util.List;


public class MyAdapterforRecycler extends RecyclerView.Adapter<MyAdapterforRecycler.RecordHolder> {
    private List<String> moreitemList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    public MyAdapterforRecycler(Context context, List<String> moreitemList) {
        this.moreitemList = moreitemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapterforRecycler.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false);
        MyAdapterforRecycler.RecordHolder gvh = new MyAdapterforRecycler.RecordHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterforRecycler.RecordHolder holder, int position) {

        Glide.with(context).load(moreitemList.get(position)).placeholder(R.drawable.image_placeholder).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return moreitemList.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvDocName, tvDate, tvStatus, tvSubject;


        public RecordHolder(View view) {
            super(view);
            //image_icon = view.findViewById(R.id.iv_rec_prof_boy);
            ivImage = view.findViewById(R.id.ivAttachment);
        }
    }
}