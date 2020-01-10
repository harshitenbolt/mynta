package com.canvascoders.opaper.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResignAgreementLinkFragment extends Fragment {
    Context mcontext;
    View view;
    TextView tvMessage, tvTitle;
    String message = "", title = "", proccess_id = "";
    SessionManager sessionManager;

    public ResignAgreementLinkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resign_agreement_link, container, false);
        sessionManager = new SessionManager(getActivity());
        mcontext = this.getActivity();

        tvMessage = view.findViewById(R.id.tvMessage);
        tvTitle = view.findViewById(R.id.tvTitle);
        if (message != null && message.length() > 0)
            tvMessage.setText(message);

        if (title != null && title.length() > 0)
            tvTitle.setText(title);

        Button btn_onboard = (Button) view.findViewById(R.id.btn_onboard);

        btn_onboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCallSendLink();
            }
        });
        return view;
    }

    private void ApiCallSendLink() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.KEY_PROCESS_ID, proccess_id);


        Call<ResendOTPResponse> call = ApiClient.getClient().create(ApiInterface.class).sendOTPResign("Bearer " + sessionManager.getToken(), params);

        call.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, Response<ResendOTPResponse> response) {
                if (response.isSuccessful()) {
                    ResendOTPResponse resendOTPResponse = response.body();
                    if (resendOTPResponse.getResponseCode() == 200) {
                        Toast.makeText(getActivity(), resendOTPResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), resendOTPResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "#errorcode 2064 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "#errorcode 2064 " + getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });


    }

    public void setMesssge(String string) {
        message = string;
    }

    public void setTitle(String string) {
        title = string;
    }

    public void setProcess(String string) {
        proccess_id = string;
    }

}
