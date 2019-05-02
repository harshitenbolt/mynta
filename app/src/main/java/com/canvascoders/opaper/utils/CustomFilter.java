package com.canvascoders.opaper.utils;


import android.widget.Filter;

import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.adapters.VendorAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CustomFilter extends Filter {

    VendorAdapter adapter;
    ArrayList<VendorList> filterList=new ArrayList<>();
    int inWhichScreen;

    public CustomFilter(List<VendorList> filterList, VendorAdapter adapter) {
        this.adapter = adapter;
        this.filterList.addAll(filterList);

    }

    public void setInWhichScreen(int inWhichScreen) {
        this.inWhichScreen = inWhichScreen;
    }


    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED data
            ArrayList<VendorList> filteredPlayers = new ArrayList<>();

            if (inWhichScreen == 1) {
                for (int i = 0; i < filterList.size(); i++) {
                    //CHECK
                    if (filterList.get(i).getStoreName().toUpperCase().contains(constraint)) {
                        //ADD Vendor TO FILTERED Vendor
                        filteredPlayers.add(filterList.get(i));
                    }
                }
            }

            if (inWhichScreen == 2) {
                for (int i = 0; i < filterList.size(); i++) {
                    //CHECK
                    if (filterList.get(i).getMobileNo().toUpperCase().contains(constraint)) {
                        //ADD Vendor TO FILTERED Vendor
                        filteredPlayers.add(filterList.get(i));
                    }
                }

            }

            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        } else {
            results.count = filterList.size();
            results.values = filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.dataViews = ((ArrayList<VendorList>) results.values);
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}

