package com.canvascoders.opaper.fragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canvascoders.opaper.R;

public class WaitingApprovalFragment extends Fragment {

    Context mcontext;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_waiting_approval, container, false);

        mcontext = this.getActivity();


        return view;
    }


}
