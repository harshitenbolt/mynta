package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.AppApplication;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    AutoCompleteTextView text;
    private EditText edit_fathername, edit_email, edit_storename, edit_pincode, edit_gstn;
    private Toolbar toolbar;
    private SwitchCompat switchGst;
    private String TAG = "ProfileFragment";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    private ArrayList<DC> dcLists = new ArrayList<>();
    private String isgsttn = "no";
    String str_process_id;

    Context mcontext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);
        initView();
        text = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);

        DashboardActivity.settitle(Constants.TITLE_PROFILE);

        getUserInfo();
        return view;
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setTitle("Please wait updating vendor details");
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        edit_fathername = (EditText) view.findViewById(R.id.edit_fathername);
        edit_email = (EditText) view.findViewById(R.id.edit_email);
        edit_storename = (EditText) view.findViewById(R.id.edit_storename);
        edit_pincode = (EditText) view.findViewById(R.id.edit_pincode);
        edit_gstn = (EditText) view.findViewById(R.id.edit_gstn);
        switchGst = (SwitchCompat) view.findViewById(R.id.switch_gst);

        switchGst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                edit_gstn.setEnabled(isChecked);
                if (isChecked) {
                    isgsttn = "yes";
                } else {
                    isgsttn = "no";
                }
            }
        });
    }

    private void getUserInfo() {
        Mylogger.getInstance().Logit(TAG, "getUserInfo");
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("Fetching details. Please wait......");
            mProgressDialog.show();

            Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getDetails("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    mProgressDialog.dismiss();
                    Mylogger.getInstance().Logit(TAG, response.raw().toString());
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().toString();
                            Mylogger.getInstance().Logit(TAG, res);
                            if (!TextUtils.isEmpty(res)) {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.has("responseCode")) {
                                    if (jsonObject.getInt("responseCode") == 200) {
                                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                        //name.setText(result.getString("name").toString());
                                        edit_fathername.setText(result.getString("father_name").toString());
                                        edit_storename.setText(result.getString("store_name").toString());
                                        edit_pincode.setText(result.getString("pincode").toString());
                                        //ed_state.setText(result.getString("state").toString());

                                        text.setText(result.getString("dc").toString());
                                        if (result.isNull("email")) {
                                            edit_email.setText("");
                                        } else {
                                            edit_email.setText(result.getString("email") + "");
                                        }
                                        String ggg = result.getString("if_gst");
                                        if (ggg.equalsIgnoreCase("yes")) {
                                            edit_gstn.setEnabled(true);
                                            edit_gstn.setText(result.getString("gstn").toString());
                                            switchGst.setChecked(true);
                                        } else {
                                            edit_gstn.setEnabled(false);
                                            switchGst.setChecked(false);
                                        }


                                    } else if (jsonObject.getInt("responseCode") == 405) {
                                        sessionManager.logoutUser(mcontext);
                                    } else {
                                        Toast.makeText(mcontext, jsonObject.getString("response").toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mProgressDialog.dismiss();

                }
            });

        } else {
            Constants.ShowNoInternet(mcontext);
        }

    }
}



