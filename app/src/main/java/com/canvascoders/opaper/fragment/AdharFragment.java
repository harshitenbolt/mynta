package com.canvascoders.opaper.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canvascoders.opaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdharFragment extends Fragment {


    public AdharFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adhar, container, false);
    }

}
