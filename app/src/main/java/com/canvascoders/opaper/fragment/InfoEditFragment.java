package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.SessionManager;

import java.util.ArrayList;

public class InfoEditFragment extends Fragment /*implements View.OnClickListener */{

   // CustomAdapter<String> spinnerArrayAdapter;
    private Spinner dc;
    private EditText edit_fathername, edit_email, edit_storename, edit_storeaddress, edit_pincode, edit_gstn, edit_city, edit_state, edit_licenceno;
    private Toolbar toolbar;
    private SwitchCompat switchGst;
    private String TAG = "InfoEditFragment";
    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;
    private Button btn_next;
    private ArrayList<String> dcLists = new ArrayList<>();
    private String isgsttn = "no";
    Context mcontext;
    View view;
    String str_process_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

      /*  mcontext = this.getActivity();

        sessionManager = new SessionManager(mcontext);
        str_process_id = sessionManager.getData(Constants.KEY_PROCESS_ID);


      //  initView();
        AgreementDetailActivity.settitle(Constants.TITLE_VENDOR_DETAIL_MENSA);

        spinnerArrayAdapter = new CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, dcLists);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dc.setAdapter(spinnerArrayAdapter);
        getUserInfo();*/
        return view;
    }

   /* private void initView() {
        dc = (Spinner) view.findViewById(R.id.dc);
        btn_next = view.findViewById(R.id.btn_next);
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setTitle("Please wait updating vendor details");
        edit_fathername = (EditText) view.findViewById(R.id.edit_fathername);
        edit_email = (EditText) view.findViewById(R.id.edit_email);
        edit_storename = (EditText) view.findViewById(R.id.edit_storename);
        edit_storeaddress = (EditText) view.findViewById(R.id.edit_storeaddress);
        edit_pincode = (EditText) view.findViewById(R.id.edit_pincode);
        edit_gstn = (EditText) view.findViewById(R.id.edit_gstn);
        switchGst = (SwitchCompat) view.findViewById(R.id.switch_gst);
        edit_city = (EditText) view.findViewById(R.id.edit_city);
        edit_state = (EditText) view.findViewById(R.id.edit_state);
        edit_licenceno = (EditText) view.findViewById(R.id.edit_licenceno);

        switchGst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mylogger.getInstance().Logit(TAG, "" + isChecked);
                edit_gstn.setEnabled(isChecked);
                if (isChecked) {
                    isgsttn = "yes";
                } else {
                    isgsttn = "no";
                    edit_gstn.setText("");
                }
            }
        });
        btn_next.setOnClickListener(this);
    }
*/
 /*   @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Constants.hideKeyboardwithoutPopulate(getActivity());


            if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                if (validation()) bizDetailsSubmit(v);
            } else {
                Constants.ShowNoInternet(getActivity());
            }
        }
    }


    public void bizDetailsSubmit(final View v) {

        mProgressDialog.show();
        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_PROCESS_ID, str_process_id);
        user.addProperty(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_FATHER_NAME, "" + edit_fathername.getText());
        user.addProperty(Constants.PARAM_EMAIL, "" + edit_email.getText());
        user.addProperty(Constants.PARAM_IF_GST, isgsttn);
        user.addProperty(Constants.PARAM_GSTN, "" + edit_gstn.getText());
        user.addProperty(Constants.PARAM_PINCODE, "" + edit_pincode.getText());
        user.addProperty(Constants.PARAM_STATE, "" + edit_state.getText());
        user.addProperty(Constants.PARAM_CITY, "" + edit_city.getText());
        user.addProperty(Constants.PARAM_DC, "" + dc.getSelectedItem());
        user.addProperty(Constants.PARAM_STORE_NAME, "" + edit_storename.getText());
        user.addProperty(Constants.PARAM_STORE_ADDRESS, "" + edit_storeaddress.getText());
        user.addProperty(Constants.PARAM_LICENCE_NO, "" + edit_licenceno.getText());
        user.addProperty(Constants.PARAM_EDIT_DETAIL, "1");


        ApiClient.getClient().create(ApiInterface.class).submitBizDetails("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetUserDetailResponse getUserDetailResponse = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetailResponse.getResponse());
                    if (getUserDetailResponse.getResponseCode() == 200) {

//                        commanFragmentCallWithBackStack(new AgreementDetailActivity());

                        Intent i = new Intent(getActivity(), AgreementDetailActivity.class);
                        startActivity(i);
                        getActivity().finish();

                    } else {
                        showAlert(v, getUserDetailResponse.getResponse(), false);
                        if (getUserDetailResponse.getResponseCode() == 405) {
                            sessionManager.logoutUser(mcontext);
                        }
                    }
                } else {
                    showAlert(v, getString(R.string.something_went_wrong), false);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        AgreementDetailActivity.card_webview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AgreementDetailActivity.card_webview.setVisibility(View.INVISIBLE);
    }

    private void addDC(String pcode) {
        // state is DC and DC is state


        // dcLists.add("--Select DC--");
        mProgressDialog.show();

        JsonObject user = new JsonObject();
        user.addProperty(Constants.PARAM_TOKEN, sessionManager.getToken());
        user.addProperty(Constants.PARAM_PINCODE, pcode);
        ApiClient.getClient().create(ApiInterface.class).getDC("Bearer "+sessionManager.getToken(),user).enqueue(new Callback<GetDC>() {
            @Override
            public void onResponse(Call<GetDC> call, Response<GetDC> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetDC getUserDetails = response.body();
                    Mylogger.getInstance().Logit(TAG, getUserDetails.getResponse());
                    if (getUserDetails.getResponseCode() == 200) {
                        for (int i = 0; i < getUserDetails.getData().size(); i++) {
                            for (DC dc : getUserDetails.getData().get(i).getDc()) {
                                dcLists.add(dc.getDc());
                            }

                            edit_state.setText(getUserDetails.getData().get(i).getState());
                            edit_city.setText(getUserDetails.getData().get(i).getCity());
                        }

                        spinnerArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDC> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mcontext, t.getMessage().toLowerCase(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private boolean validation() {
        if (TextUtils.isEmpty(edit_email.getText().toString())) {
            edit_email.requestFocus();
            showMSG(false, "Provide Email");
            return false;
        }
        if (!Constants.isEmailValid(edit_email.getText().toString())) {
            //_editTextMobile.setError("Provide Valid email");
            showMSG(false, "Provide Valid Email");
            edit_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edit_storename.getText().toString())) {
            edit_storename.requestFocus();
            showMSG(false, "Provide Store name");
            return false;
        }
        if (TextUtils.isEmpty(edit_storeaddress.getText().toString())) {
            edit_storeaddress.requestFocus();
            showMSG(false, "Provide Store address");
            return false;
        }
        if (TextUtils.isEmpty(edit_pincode.getText().toString())) {
            edit_pincode.requestFocus();
            showMSG(false, "Provide Pincode");
            return false;
        }
        if (isgsttn.equalsIgnoreCase("yes")) {
            if (TextUtils.isEmpty(edit_gstn.getText().toString())) {
                edit_gstn.requestFocus();
                showMSG(false, "Provide GSTN number");
                return false;
            }
        }
        Matcher gstMatcher = Constants.GST_PATTERN.matcher(edit_gstn.getText().toString());
        if (isgsttn.equalsIgnoreCase("yes") && !gstMatcher.matches()) {
            showMSG(false, "Provide Valid GST No.");
            return false;
        }


        if (TextUtils.isEmpty(edit_licenceno.getText().toString())) {
            edit_licenceno.requestFocus();
            showMSG(false, "Provide Licence number");
            return false;
        }

        return true;
    }

    private void showMSG(boolean b, String msg) {
        final TextView txt_show_msg_sucess = (TextView) view.findViewById(R.id.txt_show_msg_sucess);
        final TextView txt_show_msg_fail = (TextView) view.findViewById(R.id.txt_show_msg_fail);
        txt_show_msg_sucess.setVisibility(View.GONE);
        txt_show_msg_fail.setVisibility(View.GONE);
        if (b) {
            txt_show_msg_sucess.setText(msg);
            txt_show_msg_sucess.setVisibility(View.VISIBLE);
        } else {
            txt_show_msg_fail.setText(msg);
            txt_show_msg_fail.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                txt_show_msg_sucess.setVisibility(View.GONE);
                txt_show_msg_fail.setVisibility(View.GONE);
            }
        }, 2000);
    }


    public void getUserInfo() {


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            params.put(Constants.PARAM_PROCESS_ID, str_process_id);
            Mylogger.getInstance().Logit(TAG, "getUserInfo");
            mProgressDialog.setMessage("we are retrieving information, please wait!");
            mProgressDialog.show();

            Call<ResponseBody> callUpload = ApiClient.getClient().create(ApiInterface.class).getDetails("Bearer "+sessionManager.getToken(),params);
            callUpload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    mProgressDialog.dismiss();
                    Mylogger.getInstance().Logit(TAG, response.raw().toString());
                    Log.e("dscahjfsd", "" + response.raw().toString());
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().toString();
                            Mylogger.getInstance().Logit(TAG, res);
                            if (!TextUtils.isEmpty(res)) {
                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (response.code() == 200) {
                                    if (response.code() == 200) {

                                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                                        //name.setText(result.getString("name").toString());
                                        edit_fathername.setText(result.getString("father_name").toString());
                                        edit_storename.setText(result.getString("store_name").toString());
                                        edit_storeaddress.setText(result.getString("store_address").toString());
                                        edit_pincode.setText(result.getString("pincode").toString());

                                        edit_state.setText(result.getString("state").toString());
                                        edit_city.setText(result.getString("city").toString());
                                        edit_licenceno.setText(result.getString("license_no").toString());
                                        //ed_state.setText(result.getString("state").toString());

                                        dcLists.add(result.getString("dc").toString());
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


                                        spinnerArrayAdapter.notifyDataSetChanged();
                                        addDC(edit_pincode.getText().toString());


                                    } else if (response.code() == 405) {
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


    class CustomAdapter<T> extends ArrayAdapter<T> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<T> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(12);
                Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.rb_regular);
                ((TextView) view).setTypeface(typeface);
            }
            return view;
        }
    }

    public void commanFragmentCallWithBackStack(Fragment fragment) {

        Fragment cFragment = fragment;

        if (cFragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_main, cFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }*/
}



