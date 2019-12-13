package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SubmitImageResponse.SubmitImageResponse;
import com.canvascoders.opaper.Beans.TaskList;
import com.canvascoders.opaper.Beans.VendorList;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.Screenshot.DragRectView;
import com.canvascoders.opaper.Screenshot.Screenshot;
import com.canvascoders.opaper.adapters.TaskListAdapter;
import com.canvascoders.opaper.adapters.VendorListOnboardedAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.EndlessRecyclerViewScrollListener;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.Mylogger;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    ImageView ivBack, ivSupport;
    Bitmap b, converted;
    ImageView imageView, ivHome;
    RelativeLayout rvMainWithRect, rvMain;
    Button ivSelect;
    FrameLayout flImage;
    ProgressDialog progressDialog;
    RecyclerView rvTaskList;
    TaskListAdapter taskListAdapter;
    List<TaskList> taskLists = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout, mSwipeRefreshLayoutPending;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String apiName = "task-list";
    private boolean onboard = true;
    private String apiNameSearch = "task-list";
    JSONObject object = new JSONObject();
    JSONObject objectSearch = new JSONObject();
    private boolean search = false;
    int page, page1, pageSearch = 1;
    String TAG = "TaskList";
    SessionManager sessionManager;
    LinearLayout llNoData, llNoDataPending;
    TextView tvComplted, tvPending, tvPaused;
    Drawable background, background1, backgroundpause;
    String attachment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        init();


        if (background1 instanceof ShapeDrawable) {
            ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof GradientDrawable) {
            ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (background1 instanceof ColorDrawable) {
            ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
        }

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
        }


        if (backgroundpause instanceof ShapeDrawable) {
            ((ShapeDrawable) backgroundpause).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (backgroundpause instanceof GradientDrawable) {
            ((GradientDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (backgroundpause instanceof ColorDrawable) {
            ((ColorDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
        }

    }

    private void init() {
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        tvComplted = findViewById(R.id.tvComplted);
        tvPending = findViewById(R.id.tvPending);
        tvPaused = findViewById(R.id.tvPaused);
        tvPaused.setOnClickListener(this);
        tvComplted.setOnClickListener(this);
        tvPending.setOnClickListener(this);

        //
        background = tvComplted.getBackground();
        background1 = tvPending.getBackground();
        backgroundpause = tvPaused.getBackground();


        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llNoData = findViewById(R.id.llNoData);
        llNoDataPending = findViewById(R.id.llNoDataPending);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_containerCompleted);
        mSwipeRefreshLayoutPending = (SwipeRefreshLayout) findViewById(R.id.swipe_containerPending);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayoutPending.setOnRefreshListener(this);
        // ivSearch = view.findViewById(R.id.ivSearch);
        rvTaskList = (RecyclerView) findViewById(R.id.rvTaskList);
        // rvTaskListPending = (RecyclerView) findViewById(R.id.rvTaskListPending);
        rvTaskList.setHasFixedSize(true);
        //  rvTaskListPending.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);


        rvTaskList.setLayoutManager(linearLayoutManager);
        //  rvTaskListPending.setLayoutManager(linearLayoutManager1);
        taskListAdapter = new TaskListAdapter(taskLists, TaskListActivity.this, this);

        rvTaskList.setAdapter(taskListAdapter);
        //   rvTaskListPending.setAdapter(taskListAdapter);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == true) {
                    // this condition is for not getting Next URL from API
                    if (!apiNameSearch.equalsIgnoreCase("")) ;
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    if (!apiName.equalsIgnoreCase("")) {
                        new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }
        };
        // rvTaskList.addOnScrollListener(scrollListener);
        rvTaskList.addOnScrollListener(scrollListener);
        try {
            object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
            object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
            //  object.put(Constants.PARAM_STATUS, "1");
        } catch (
                JSONException e) {

        }

        /////////////////////////// get onboard vender list/////////////////////////

        apiName = "task-list";
        onboard = true;
        progressDialog.setMessage("please wait loading tasks...");

        //    new GetVendorList(object.toString(), apiName).execute();


        ivSupport = findViewById(R.id.ivSupport);

        ivSupport.setOnClickListener(this);
        imageView = findViewById(R.id.ivImage);
        ivSelect = findViewById(R.id.ivSelect);
        rvMain = findViewById(R.id.rvMain);
        flImage = findViewById(R.id.flImage);
        rvMainWithRect = findViewById(R.id.rlWithMain);

        ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = 0x90000000;
                final Drawable drawable = new ColorDrawable(color);
                b = Screenshot.takescreenshotOfRootView(imageView);
                imageView.setImageBitmap(b);
                findViewById(R.id.rvCaptured).setVisibility(View.VISIBLE);
                rvMain.setVisibility(View.GONE);
                //ivSupport.setVisibility(View.GONE);
                flImage.setForeground(drawable);
                //  btn_next.setForeground(drawable);
               /* findViewById(R.id.btn_next).setVisibility(View.GONE);
                findViewById(R.id.scView).setVisibility(View.GONE);*/
            }
        });


        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  final int color = 0xFFFFFF;
                final Drawable drawable = new ColorDrawable(color);
                imageView.setImageResource(android.R.color.transparent);*/
                //  ivSupport.setVisibility(View.GONE);
                findViewById(R.id.rvCaptured).setVisibility(View.GONE);
                findViewById(R.id.rvMain).setVisibility(View.VISIBLE);

                // rvMain.setForeground(drawable);
             /* //  findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
              //  rvMain.setForeground(drawable);
              //  btn_next.setForeground(drawable);*/
            }
        });
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*View v1 = rvMainWithRect.getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = v1.getDrawingCache();
                BitmapDrawable drawable=new BitmapDrawable(bitmap);*/

                final int color = 0xFFFFFF;
                final Drawable drawable = new ColorDrawable(color);
                flImage.setForeground(drawable);
                findViewById(R.id.llButton).setVisibility(View.GONE);
                findViewById(R.id.tverror).setVisibility(View.GONE);
                Bitmap bitmap = viewToBitmap(rvMainWithRect);
                ApiCallSendImageSupport(bitmap);
                converted = getResizedBitmap(bitmap, 400);

                findViewById(R.id.rvCaptured).setVisibility(View.GONE);
                findViewById(R.id.rvMain).setVisibility(View.VISIBLE);
                findViewById(R.id.llButton).setVisibility(View.VISIBLE);
                findViewById(R.id.tverror).setVisibility(View.VISIBLE);
            }
        });
        final DragRectView view = (DragRectView) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new DragRectView.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    // view.setForeground(drawable);

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ivSupport:
                final int color = 0x90000000;
                final Drawable drawable = new ColorDrawable(color);
                b = Screenshot.takescreenshotOfRootView(imageView);
                imageView.setImageBitmap(b);
                findViewById(R.id.rvCaptured).setVisibility(View.VISIBLE);
                rvMain.setVisibility(View.GONE);
                ivSupport.setVisibility(View.GONE);
                flImage.setForeground(drawable);
                break;


            case R.id.tvComplted:
                object = new JSONObject();
                try {
                    object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                    object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                    object.put(Constants.PARAM_STATUS, "1");
                } catch (
                        JSONException e) {

                }


                page1 = 1;
                apiName = "task-list";
                new GetVendorList(object.toString(), apiName).execute();


                mSwipeRefreshLayoutPending.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                }


                if (backgroundpause instanceof ShapeDrawable) {
                    ((ShapeDrawable) backgroundpause).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (backgroundpause instanceof GradientDrawable) {
                    ((GradientDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (backgroundpause instanceof ColorDrawable) {
                    ((ColorDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                break;


            case R.id.tvPending:


                object = new JSONObject();
                try {
                    object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                    object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                    //   object.put(Constants.PARAM_STATUS, "0");
                } catch (
                        JSONException e) {

                }
                page1 = 1;
                apiName = "task-list";
                new GetVendorList(object.toString(), apiName).execute();

                mSwipeRefreshLayoutPending.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPending.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                }

                if (backgroundpause instanceof ShapeDrawable) {
                    ((ShapeDrawable) backgroundpause).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (backgroundpause instanceof GradientDrawable) {
                    ((GradientDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (backgroundpause instanceof ColorDrawable) {
                    ((ColorDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorBlack));
                }


                break;


            // fpr pause list
            case R.id.tvPaused:


                object = new JSONObject();
                try {
                    object.put(Constants.PARAM_TOKEN, sessionManager.getToken());
                    object.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
                    object.put(Constants.PARAM_IS_PAUSE, "1");
                } catch (
                        JSONException e) {

                }
                taskListAdapter = new TaskListAdapter(taskLists, TaskListActivity.this, this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvTaskList.setLayoutManager(linearLayoutManager);

                rvTaskList.setAdapter(taskListAdapter);
                scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                        // this condition  is for pagination in both with Search and without search

                        if (search == true) {
                            // this condition is for not getting Next URL from API
                            if (!apiNameSearch.equalsIgnoreCase("")) ;
                            new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                        } else {
                            if (!apiName.equalsIgnoreCase("")) {
                                new GetVendorList(object.toString(), apiName).execute();
                            }
                        }

                    }
                };
                // rvTaskList.addOnScrollListener(scrollListener);
                rvTaskList.addOnScrollListener(scrollListener);
                page1 = 1;
                apiName = "task-list";
                new GetVendorList(object.toString(), apiName).execute();

                mSwipeRefreshLayoutPending.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (background1 instanceof ShapeDrawable) {
                    ((ShapeDrawable) background1).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof GradientDrawable) {
                    ((GradientDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background1 instanceof ColorDrawable) {
                    ((ColorDrawable) background1).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvPending.setTextColor(getResources().getColor(R.color.colorBlack));
                }

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(ContextCompat.getColor(this, R.color.colorWhite));
                    tvComplted.setTextColor(getResources().getColor(R.color.colorBlack));
                }


                if (backgroundpause instanceof ShapeDrawable) {
                    ((ShapeDrawable) backgroundpause).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (backgroundpause instanceof GradientDrawable) {
                    ((GradientDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorWhite));
                } else if (backgroundpause instanceof ColorDrawable) {
                    ((ColorDrawable) backgroundpause).setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvPaused.setTextColor(getResources().getColor(R.color.colorWhite));
                }

                break;
        }

    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void SingleClick(String popup, int position) {

    }

    @Override
    public void onRefresh() {
        page1 = 1;
        //actv.getText().clear();
        taskListAdapter = new TaskListAdapter(taskLists, TaskListActivity.this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvTaskList.setLayoutManager(linearLayoutManager);

        rvTaskList.setAdapter(taskListAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == true) {
                    // this condition is for not getting Next URL from API
                    if (!apiNameSearch.equalsIgnoreCase("")) ;
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    if (!apiName.equalsIgnoreCase("")) {
                        new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }
        };
        // rvTaskList.addOnScrollListener(scrollListener);
        rvTaskList.addOnScrollListener(scrollListener);
        //  rvTaskListPending.setAdapter(taskListAdapter);
        apiName = "task-list";
        new GetVendorList(object.toString(), apiName).execute();

    }

    public class GetVendorList extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetVendorList(String jsonReq, String apiURL) {
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
            search = false;
            // if there will be page 1 so we have to clear our list sp this condition putted
            if (onboard == false && page == 1) {
                taskLists.clear();
                taskLists.clear();
            }
            if (onboard == true && page1 == 1) {
                taskLists.clear();
                taskLists.clear();
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
            mSwipeRefreshLayoutPending.setRefreshing(false);
            Mylogger.getInstance().Logit(TAG, message);
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        Toast.makeText(TaskListActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(TaskListActivity.this);
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

                            TaskList vendorList = gson.fromJson(o.toString(), TaskList.class);


                            TaskList vList = new TaskList();
                            vList.setId(vendorList.getId());
                            vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setType(vendorList.getType());
                            vList.setCompleteDatetime(vendorList.getCompleteDatetime());
                            vList.setStatus(vendorList.getStatus());
                            vList.setDueDate(vendorList.getDueDate());
                            vList.setDueTime(vendorList.getDueTime());
                            vList.setTaskStart(vendorList.getTaskStart());
                            vList.setStartTimer(vendorList.getStartTimer());
                            vList.setIsPause(vendorList.getIsPause());

                            page = 0;
                            page1 = 0;
                            taskLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well
                            // vendorLists1.add(vList);
                        }
                        if (taskLists.size() > 0) {
                            rvTaskList.getRecycledViewPool().clear();
                            taskListAdapter.notifyDataSetChanged();
                            rvTaskList.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            llNoDataPending.setVisibility(View.GONE);
                            //rvTaskListPending.setVisibility(View.VISIBLE);
                        } else {
                            rvTaskList.setVisibility(View.GONE);
                            // rvTaskListPending.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                            llNoDataPending.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
            taskListAdapter.notifyDataSetChanged();
        }
    }


    public class GetVendorListSearch extends AsyncTask<String, Void, String> {

        String jsonReq;
        String apiURL;

        public GetVendorListSearch(String jsonReq, String apiURL) {
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
            // if we are on 1st page of every new keyword it is mandatory to clear our list
            if (pageSearch == 1) {
                taskLists.clear();
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
            Mylogger.getInstance().Logit(TAG, message);
            if (message != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.has("response")) {
                        Toast.makeText(TaskListActivity.this, jsonObject.getString("response").toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.has("responseCode")) {
                        if (jsonObject.getString("responseCode").equalsIgnoreCase("411")) {
                            sessionManager.logoutUser(TaskListActivity.this);
                        }
                    }
                    {
                        String next_Url = jsonObject.getString("next_page_url");
                        if (!next_Url.equalsIgnoreCase("") && !next_Url.equalsIgnoreCase("null")) {
                            String[] separated = next_Url.split("api3/");
                            apiNameSearch = separated[1];

                        } else {
                            apiNameSearch = "";
                        }
                        //Log.e("URL", next_Url);
                        JSONArray result = jsonObject.getJSONArray("data");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject o = result.getJSONObject(i);

                            TaskList vendorList = gson.fromJson(o.toString(), TaskList.class);


                            TaskList vList = new TaskList();
                            vList.setId(vendorList.getId());
                            vList.setAgentId(vendorList.getAgentId());
                            vList.setMobileNo(vendorList.getMobileNo());
                            vList.setStoreName(vendorList.getStoreName());
                            vList.setCompleteDatetime(vendorList.getCompleteDatetime());
                            vList.setIsPause(vendorList.getIsPause());
                            vList.setType(vendorList.getType());
                            vList.setStatus(vendorList.getStatus());
                            vList.setDueDate(vendorList.getDueDate());
                            vList.setDueTime(vendorList.getDueTime());

                            page = 0;
                            page1 = 0;
                            taskLists.add(vList);
                            // we have to add data in  new vendorlist too to manage data well
                            // vendorLists1.add(vList);
                        }
                        if (taskLists.size() > 0) {
                            rvTaskList.getRecycledViewPool().clear();
                            taskListAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            taskListAdapter.notifyDataSetChanged();
        }

    }


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page1 = 1;
        //actv.getText().clear();
        taskListAdapter = new TaskListAdapter(taskLists, TaskListActivity.this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvTaskList.setLayoutManager(linearLayoutManager);

        rvTaskList.setAdapter(taskListAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                // this condition  is for pagination in both with Search and without search

                if (search == true) {
                    // this condition is for not getting Next URL from API
                    if (!apiNameSearch.equalsIgnoreCase("")) ;
                    new GetVendorListSearch(objectSearch.toString(), apiNameSearch).execute();
                } else {
                    if (!apiName.equalsIgnoreCase("")) {
                        new GetVendorList(object.toString(), apiName).execute();
                    }
                }

            }
        };
        // rvTaskList.addOnScrollListener(scrollListener);
        rvTaskList.addOnScrollListener(scrollListener);
        //  rvTaskListPending.setAdapter(taskListAdapter);
        apiName = "task-list";
        new GetVendorList(object.toString(), apiName).execute();

    }


    private void ApiCallSendImageSupport(Bitmap bitmap) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        MultipartBody.Part attachment_part = null;


        //Log.e("Id_done", "" + priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitSupportImage("Bearer " + sessionManager.getToken(), attachment_part).enqueue(new Callback<SubmitImageResponse>() {
            @Override
            public void onResponse(Call<SubmitImageResponse> call, retrofit2.Response<SubmitImageResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SubmitImageResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(TaskListActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    /*    Intent i = new Intent(TaskListActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());

                        i.putExtra(Constants.PARAM_SCREEN_NAME, "DashBoard");
                        startActivity(i);*/


                        Intent i = new Intent(TaskListActivity.this, GeneralSupportSubmitActivity.class);
                        i.putExtra("BitmapImage", submitReportResponse.getData().get(0).getAttachment());
                        i.putExtra(Constants.PARAM_ATTACHMENT,submitReportResponse.getData().get(0).getAttachment_name());
                        i.putExtra(Constants.PARAM_SCREEN_NAME, "tasklist");
                        startActivity(i);

                        // finish();
                    } else {
                        Toast.makeText(TaskListActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmitImageResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }
}
