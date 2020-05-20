package com.canvascoders.opaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.canvascoders.opaper.adapters.ReportAdapter;
import com.canvascoders.opaper.Beans.BillList;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportFragment extends Fragment {
    SessionManager sessionManager;
    String TAG = "ReportFragment";
    JSONObject object = new JSONObject();
    private ReportAdapter linearAdapter;
    private NetworkConnectivity networkConnectivity;
    private ArrayList<BillList> billLists = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerview;
    private RadioGroup rg;
    Context mcontext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report, container, false);

        mcontext = this.getActivity();

        DashboardActivity.settitle(Constants.TITLE_REPORT);

        intView();
        rg = (RadioGroup) view.findViewById(R.id.activity_course_rg);
        sessionManager = new SessionManager(mcontext);

        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setTitle("we are loading reports, please wait!");

        if (!networkConnectivity.isNetworkAvailable()) {

            // Display message in dialog box if you have not internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder.setMessage("You are offline,Please check your internet connection,then try again");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    getActivity().finish();
                    arg0.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {

            try {
                object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            } catch (JSONException e) {
            }


            new GetReports(object.toString(), "").execute();

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) view.findViewById(checkedId);

                if (rb.getId() == R.id.rb_in) {
                    new GetReports(object.toString(), "?report_type=daily").execute();

                } else if (rb.getId() == R.id.rb_ex) {
                    new GetReports(object.toString(), "?report_type=monthly").execute();

                } else {
                    new GetReports(object.toString(), "").execute();
                }
            }
        });

        return view;
    }

    private void intView() {
        networkConnectivity = new NetworkConnectivity(mcontext);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearAdapter = new ReportAdapter(billLists);
        recyclerview.setAdapter(linearAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            getActivity().finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    public class GetReports extends AsyncTask<String, Void, String> {
        String myRes;
        String s;
        String apinumber;

        public GetReports(String s, String apinumber) {
            this.s = s;
            this.apinumber = apinumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            billLists.clear();
        }

        @Override
        protected String doInBackground(String[] params) {

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");


                RequestBody body = RequestBody.create(mediaType, s);
                Mylogger.getInstance().Logit(TAG, Constants.BaseURL + "simple-report" + apinumber);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + "simple-report" + apinumber)
                        .post(body)
                        .addHeader("Authorization","Bearer "+sessionManager.getToken())
                        .build();

                Response responseLogin = client.newCall(requestLogin).execute();

                myRes = responseLogin.body().string();

                Mylogger.getInstance().Logit(TAG + "1", myRes);
                return myRes;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();

            try {

                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.getInt("responseCode") == 200) {
                        Mylogger.getInstance().Logit(TAG, message);

                        JSONObject result = jsonObject.getJSONArray("data").getJSONObject(0);
                        if (result.length() > 0) {
                            ImageView m = (ImageView) view.findViewById(R.id.imgnodata);
                            m.setVisibility(View.GONE);
                        } else {
                            ImageView m = (ImageView) view.findViewById(R.id.imgnodata);
                            m.setVisibility(View.VISIBLE);
                        }


                        //  Integer id;
                        String lmsId;
                        String enboltId;
                        String vendorId;
                        String csatCondition;
                        String billPeriod;
                        String toDate;


                        // id = o.getInt("id");
                        lmsId = result.getString("total_store");
                        enboltId = result.getString("total_completed_store");
                        vendorId = result.getString("total_inprogress_store");
                        csatCondition = result.getString("total_invoice");
                        billPeriod = result.getString("total_esign_done_invoice");
                        toDate = result.getString("total_esign_pending_invoice");

                        billLists.add(new BillList(0, lmsId, enboltId, vendorId, csatCondition, billPeriod, toDate, "", "", 0, ""));

                        linearAdapter.notifyDataSetChanged();
                    }else if(jsonObject.getInt("responseCode")==411){
                        sessionManager.logoutUser(getActivity());
                    }else {

                        ImageView m = (ImageView) view.findViewById(R.id.imgnodata);
                        m.setVisibility(View.VISIBLE);
                        Toast.makeText(mcontext, jsonObject.getString("message").toLowerCase(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(mcontext, jsonObject.getString("error").toLowerCase(), Toast.LENGTH_LONG).show();
                    sessionManager.logoutUser(mcontext);
                }
            } catch (
                    JSONException e)

            {
                e.printStackTrace();
            }


        }
    }
}
