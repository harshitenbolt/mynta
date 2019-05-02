package com.canvascoders.opaper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.activity.DashboardActivity;

public class NotificationFragment extends Fragment {


    Context mcontext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        mcontext = this.getActivity();

        DashboardActivity.settitle(Constants.TITLE_NOTIFICATION);

        return view;
    }


}
