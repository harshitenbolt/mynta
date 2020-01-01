package com.canvascoders.opaper.adapters;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.DeliveryBoyList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;

import java.util.List;

public class DeliveryBoysAssessmentListAdapter extends RecyclerView.Adapter<DeliveryBoysAssessmentListAdapter.RecordHolder> {

    private List<DeliveryBoyList> vendorLists;
    Context context;
    String value = "1";
    RecyclerViewClickListener recyclerViewClickListener;


    public DeliveryBoysAssessmentListAdapter(List<DeliveryBoyList> moreitemList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.vendorLists = moreitemList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deliveryboy_assessment_list, parent, false);
        RecordHolder holder = new RecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        holder.tvName.setText(vendorLists.get(position).getName());
        holder.tvMobile.setText(vendorLists.get(position).getPhoneNumber());
        holder.tvRoute.setText(vendorLists.get(position).getCurrentAddressState());
        // holder.tvLiveFrom.setText(vendorLists.get(position).getRateApproveDate());
        holder.tvStoreName.setText(vendorLists.get(position).getStoreName());
        //  holder.tvStatus.setText(vendorLists.get(position).getStatus());


        if (vendorLists.get(position).getAssessmentVerify() == 0 && vendorLists.get(position).getAssessmentTried() == 0) {
            holder.tvStatus.setText("PENDING");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorYellow));

        } else {
            if (vendorLists.get(position).getAssessmentVerify() == 1) {
                holder.tvStatus.setText("PASS");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.tvStatus.setText("FAIL");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            }
        }

        Log.e("URL", "" + Constants.BaseImageURL + vendorLists.get(position).getPhoneNumber());
        Glide.with(context).load(Constants.BaseImageURL + vendorLists.get(position).getImage()).placeholder(R.drawable.image_placeholder).into(holder.ivStoreImage);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerViewClickListener.SingleClick("", vendorLists.get(position).getProccessId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage, ivStatusImage;
        TextView tvStoreName, tvName, tvMobile, tvRoute, tvLiveFrom, tvStatus;
        LinearLayout llMain;

        public RecordHolder(View view) {
            super(view);
            llMain = view.findViewById(R.id.llMain);
            ivStoreImage = view.findViewById(R.id.ivStoreImage);
            tvName = view.findViewById(R.id.tvName);
            tvStoreName = view.findViewById(R.id.tvStoreName);
            tvLiveFrom = view.findViewById(R.id.tvLiveFrom);
            tvMobile = view.findViewById(R.id.tvMobile);
            ivStatusImage = view.findViewById(R.id.ivStatus);
            tvRoute = view.findViewById(R.id.tvRoute);
            tvStatus = view.findViewById(R.id.tvStatus);
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
