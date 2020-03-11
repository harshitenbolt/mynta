package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.GetGstListing.GetGstListing;
import com.canvascoders.opaper.Beans.NotificationList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.Beans.ViewNotificationResponse.ViewNotificationResponse;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.NotificationListAdapter;
import com.canvascoders.opaper.adapters.VendorListOnboardedAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ImageView ivBack;
    RecyclerView rvNotificationLeave;
    SessionManager sessionManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    NotificationListAdapter notificationListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<NotificationList> notificationLists = new ArrayList<>();
    private String apiName = "notification-list";
    ProgressDialog progressDialog;
    JSONObject object = new JSONObject();
    private boolean onboard = true;
    int page = 1;
    String TAG = "Notification";
    LinearLayout llNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
    }

    private void init() {
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        ivBack = findViewById(R.id.iv_back);
        rvNotificationLeave = findViewById(R.id.rvNotificationList);
        ivBack.setOnClickListener(this);
        llNoData = findViewById(R.id.llNoData);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rvNotificationLeave.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvNotificationLeave.setLayoutManager(linearLayoutManager);
        notificationListAdapter = new NotificationListAdapter(notificationLists, NotificationActivity.this, this);

        rvNotificationLeave.setAdapter(notificationListAdapter);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search


                if (!apiName.equalsIgnoreCase("")) {
                    new GetNotificationList(object.toString(), apiName).execute();
                }

            }
        };
        rvNotificationLeave.addOnScrollListener(scrollListener);

        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        } catch (
                JSONException e) {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "notification-list";
        onboard = true;
        progressDialog.setMessage("please wait loading notifications...");

        new GetNotificationList(object.toString(), apiName).execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;


        }

    }

    @Override
    public void onClick(View view, int position) {

        String id = String.valueOf(notificationLists.get(position).getId());
        ApiCallViewNotification(id);


    }

    private void ApiCallViewNotification(String id) {
        progressDialog.setMessage("Please wait...");
        // progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_ID, id);

        Call<ViewNotificationResponse> call = ApiClient.getClient().create(ApiInterface.class).viewNotification("Bearer " + sessionManager.getToken(), params);

        call.enqueue(new Callback<ViewNotificationResponse>() {
            @Override
            public void onResponse(Call<ViewNotificationResponse> call, retrofit2.Response<ViewNotificationResponse> response) {
                //  progressDialog.dismiss();
                if (response.isSuccessful()) {
                    ViewNotificationResponse viewNotificationResponse = response.body();
                    if (viewNotificationResponse.getResponseCode() == 200) {

                    } else {

                    }
                }
                else{
                    Toast.makeText(NotificationActivity.this, "#errorcode 2084 "+getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewNotificationResponse> call, Throwable t) {
                //   progressDialog.dismiss();
                Toast.makeText(NotificationActivity.this, "#errorcode 2084 "+getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onLongClick(View view, int position,String data) {

    }

    @Override
    public void SingleClick(String popup, int position) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        //  actv.getText().clear();
        apiName = "notification-list";
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvNotificationLeave.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search


                if (!apiName.equalsIgnoreCase("")) {
                    new GetNotificationList(object.toString(), apiName).execute();
                }

            }
        };
        rvNotificationLeave.addOnScrollListener(scrollListener);

        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        } catch (
                JSONException e) {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "notification-list";
        onboard = true;
        progressDialog.setMessage("please wait loading notifications...");

        new GetNotificationList(object.toString(), apiName).execute();

    }


    public class GetNotificationList extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetNotificationList(String jsonReq, String apiURL) {
            this.jsonReq = jsonReq;
            this.apiURL = apiURL;

            Mylogger.getInstance().Logit("apiURL: ", apiURL);
            Mylogger.getInstance().Logit("jsonReq: ", jsonReq);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setCancelable(false);

            // if there will be page 1 so we have to clear our list sp this condition putted
            if (page == 1) {
                notificationLists.clear();
                // vendorLists1.clear();
            }

        }

        @Override
        protected String doInBackground(String[] params) {
            String myRes;
            try {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.MINUTES)
                        .readTimeout(1000, TimeUnit.MINUTES)
                        .writeTimeout(1000, TimeUnit.MINUTES)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonReq);
                Request requestLogin = new Request.Builder()
                        .url(Constants.BaseURL + apiURL)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
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
            mSwipeRefreshLayout.setRefreshing(false);
            Mylogger.getInstance().Logit(TAG, message);
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        Toast.makeText(NotificationActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(NotificationActivity.this);
                        }
                    }

                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        // if next page URL will get as a "" so this condition will help
                        if (!next_Url.equalsIgnoreCase("") && !next_Url.equalsIgnoreCase("null")) {
                            String[] separated = next_Url.split("api3/");
                            apiName = separated[1];
                        } else {
                            // and if no so we have to stop pagination so we declared apiname as blak string
                            apiName = "";
                        }
                        Log.e("URL", next_Url);
                        JSONArray result = jsonObject.getJSONArray("data");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject o = result.getJSONObject(i);

                            NotificationList notificationList = gson.fromJson(o.toString(), NotificationList.class);

                            Log.e("VENDOR", "" + notificationList.getDescription());

                            NotificationList vList = new NotificationList();
                            vList.setDescription(notificationList.getDescription());
                            vList.setIsRead(notificationList.getIsRead());
                            vList.setId(notificationList.getId());
                            vList.setUpdatedAt(notificationList.getUpdatedAt());
                            vList.setCreatedAt(notificationList.getCreatedAt());

                            page = 0;

                            notificationLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well

                        }
                        if (notificationLists.size() > 0) {
                            rvNotificationLeave.getRecycledViewPool().clear();
                            notificationListAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
            notificationListAdapter.notifyDataSetChanged();
        }
    }


}
