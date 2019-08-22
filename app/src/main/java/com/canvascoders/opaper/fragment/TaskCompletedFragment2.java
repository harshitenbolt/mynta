package com.canvascoders.opaper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;

public class TaskCompletedFragment2 extends Fragment {

    Context mcontext;
    View view;
    TextView tvMessage;
    String message = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_completed2, container, false);

        mcontext = this.getActivity();

        tvMessage = view.findViewById(R.id.tvMessage);

        if (message != null && message.length() > 0)
            tvMessage.setText(message);

        Button btn_onboard = (Button) view.findViewById(R.id.btn_onboard);

        btn_onboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DashboardActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void setMesssge(String string) {
        message = string;
    }








}