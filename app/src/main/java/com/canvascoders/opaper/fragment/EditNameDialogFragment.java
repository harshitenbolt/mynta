package com.canvascoders.opaper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.SessionManager;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.utils.Constants;

/**
 * Created by piyush on 07/02/2018.
 */

public class EditNameDialogFragment extends DialogFragment {

    EditText edit_aname, edit_anumber, edit_ayear, edit_apincode;
    ImageView btn_close_dialog;
    private Button btnNext;
    private AadharVerificationFragment aadharVerificationFragment;
    String str_name, str_uid, str_year, str_pincode;

    SessionManager sessionManager;

    public EditNameDialogFragment() {

    }

    public static EditNameDialogFragment newInstance(AadharVerificationFragment aadharVerificationFragment) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        frag.aadharVerificationFragment = aadharVerificationFragment;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_name, container);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Get field from view
        btnNext = (Button) view.findViewById(R.id.btn_next);
        edit_aname = (EditText) view.findViewById(R.id.edit_aname);
        edit_anumber = (EditText) view.findViewById(R.id.edit_anumber);
        edit_ayear = (EditText) view.findViewById(R.id.edit_ayear);
        edit_apincode = (EditText) view.findViewById(R.id.edit_apincode);
        btn_close_dialog = (ImageView) view.findViewById(R.id.btn_close_dialog);


        sessionManager = new SessionManager(getActivity());


        readBundle();



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valication()) {

                    if (AppApplication.networkConnectivity.isNetworkAvailable()) {


                        /*AadharVerificationFragment.name = edit_aname.getText().toString().trim();
                        AadharVerificationFragment.udi = edit_anumber.getText().toString().trim();
                        AadharVerificationFragment.year = edit_ayear.getText().toString().trim();
                        AadharVerificationFragment.pincode = edit_apincode.getText().toString().trim();*/

                        str_name = edit_aname.getText().toString().trim();
                        str_uid = edit_anumber.getText().toString().trim();
                        str_year = edit_ayear.getText().toString().trim();
                        str_pincode = edit_apincode.getText().toString().trim();

                        sessionManager.saveData(Constants.KEY_AADHAR_NAME, str_name);
                        sessionManager.saveData(Constants.KEY_UID, str_uid);
                        sessionManager.saveData(Constants.KEY_YEAR, str_year);
                        sessionManager.saveData(Constants.KEY_PINCODE, str_pincode);
                        aadharVerificationFragment.storeAadhar();
                        dismiss();

                    } else {
                        Constants.ShowNoInternet(getActivity());
                    }
                }
            }
        });
        btn_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.saveData(Constants.KEY_AADHAR_NAME, "");
                sessionManager.saveData(Constants.KEY_UID, "");
                sessionManager.saveData(Constants.KEY_PINCODE, "");
                sessionManager.saveData(Constants.KEY_YEAR, "");
                dismiss();
            }
        });
    }

    private void readBundle() {

        str_name = sessionManager.getData(Constants.KEY_AADHAR_NAME);
        str_uid = sessionManager.getData(Constants.KEY_UID);
        str_year = sessionManager.getData(Constants.KEY_YEAR);
        str_pincode = sessionManager.getData(Constants.KEY_PINCODE);

        edit_aname.setText(str_name);
        edit_anumber.setText(str_uid);
        edit_ayear.setText(str_year);
        edit_apincode.setText(str_pincode);
    }

    private boolean valication() {
        if (TextUtils.isEmpty(edit_aname.getText().toString())) {
            edit_aname.requestFocus();
            edit_aname.setError("Please enter Aadhar name");

            return false;
        }
        if (TextUtils.isEmpty(edit_anumber.getText().toString())) {
            edit_anumber.requestFocus();
            edit_anumber.setError("Please enter Aadhar number");

            return false;
        }
        if (TextUtils.isEmpty(edit_ayear.getText().toString())) {
            edit_ayear.requestFocus();
            edit_ayear.setError("Please enter year of birth");

            return false;
        }
        if (TextUtils.isEmpty(edit_apincode.getText().toString())) {
            edit_apincode.requestFocus();
            edit_apincode.setError("Please enter pincode");

            return false;
        }

        return true;
    }
}