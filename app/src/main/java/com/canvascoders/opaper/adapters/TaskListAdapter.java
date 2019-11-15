package com.canvascoders.opaper.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.canvascoders.opaper.Beans.TaskList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.TaskDetailActivity;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.RecordHolder> {

    private List<TaskList> vendorLists;
    Context context;
    String value = "1";
    RecyclerViewClickListener recyclerViewClickListener;


    public TaskListAdapter(List<TaskList> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.vendorLists = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task_list, parent, false);
        RecordHolder holder = new RecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvTitle.setText(vendorLists.get(position).getType());
        holder.tvMobile.setText(vendorLists.get(position).getMobileNo());

        holder.tvStartName.setText("Due Date :- " + vendorLists.get(position).getDueDate());
        holder.tvStoreName.setText(vendorLists.get(position).getStoreName());


        if (vendorLists.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText("Pending");
        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText("Completed");
        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setText("In progress");
        } else if (vendorLists.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tvStatus.setText("Re-opened");
        }


        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TaskDetailActivity.class);
                i.putExtra(Constants.KEY_ID, vendorLists.get(position).getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage, ivStatusImage;
        TextView tvStoreName, tvTitle, tvMobile, tvStatus, tvStartName;
        LinearLayout llMain;
        CardView cvMain;

        public RecordHolder(View view) {
            super(view);

            tvStoreName = view.findViewById(R.id.tvStoreName);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvStartName = view.findViewById(R.id.tvDueDate);
            cvMain = view.findViewById(R.id.cvMain);
            //     tvVehicle = view.findViewById(R.id.tvVehicle);
        }
    }


    public void commanFragmentCallWithBackStack(Fragment fragment, String mobileno) {

        Fragment cFragment = fragment;

        if (cFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMP_MOBILE, mobileno);

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }


}