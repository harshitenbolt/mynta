package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canvascoders.opaper.Beans.GetTaskCategoryListResponse.Datum;
import com.canvascoders.opaper.Beans.GetTaskCategoryListResponse.GetTasksTypeListing;
import com.canvascoders.opaper.Beans.GetTaskCategoryListResponse.SubCatetegory;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.adapters.FilterListmainAdapter;
import com.canvascoders.opaper.adapters.FilterSubListAdapter;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.VendorOnboardedList;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.SessionManager;
import com.google.android.gms.common.data.DataHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements RecyclerViewClickListener, View.OnClickListener {
    RecyclerView rvMainCategory, rvFilterSubCategory;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<Datum> categoryList = new ArrayList<>();
    List<SubCatetegory> valueList = new ArrayList<>();
    FilterListmainAdapter filterListmainAdapter;
    FilterSubListAdapter filterSubListAdapter;
    LinearLayout llSearch;
    String mainFiledname = "";
    Button btCancel, btApply;
    String setText = "";
    AutoCompleteTextView actv;
    TextView tvDataName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        init();
        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetCategoryList();
        } else {
            Constants.ShowNoInternet(FilterActivity.this);
        }
    }

    private void ApiCallGetCategoryList() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        ApiClient.getClient().create(ApiInterface.class).getTaskCategoryList("Bearer " + sessionManager.getToken(), params).enqueue(new Callback<GetTasksTypeListing>() {
            @Override
            public void onResponse(Call<GetTasksTypeListing> call, Response<GetTasksTypeListing> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetTasksTypeListing getTasksTypeListing = response.body();
                    if (getTasksTypeListing.getResponseCode() == 200) {
                        if (getTasksTypeListing.getData().size() > 0) {
                            categoryList.addAll(getTasksTypeListing.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilterActivity.this);
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            rvMainCategory.setLayoutManager(linearLayoutManager);
                            filterListmainAdapter = new FilterListmainAdapter(categoryList, FilterActivity.this, FilterActivity.this);
                            rvMainCategory.setAdapter(filterListmainAdapter);

                            valueList = new ArrayList<>();


                            if (categoryList.get(0).getSubCatetegory().size() > 0) {
                                valueList.addAll(categoryList.get(0).getSubCatetegory());
                                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FilterActivity.this);
                                linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
                                rvFilterSubCategory.setLayoutManager(linearLayoutManager1);
                                filterSubListAdapter = new FilterSubListAdapter(valueList, FilterActivity.this, FilterActivity.this);
                                rvFilterSubCategory.setAdapter(filterSubListAdapter);
                                filterSubListAdapter.notifyDataSetChanged();
                                mainFiledname = categoryList.get(0).getLabel();
                                //  Log.e("datadone", mainFiledname);
                            }

                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<GetTasksTypeListing> call, Throwable t) {

            }
        });
    }

    private void init() {
        rvMainCategory = findViewById(R.id.rvFilterMainCategory);
        rvFilterSubCategory = findViewById(R.id.rvFilterSubCategory);
        btCancel = findViewById(R.id.btCancel);
        btApply = findViewById(R.id.btApply);
        llSearch = findViewById(R.id.llSearch);
        btCancel.setOnClickListener(this);
        btApply.setOnClickListener(this);

        tvDataName = findViewById(R.id.tvDataName);
        actv = (AutoCompleteTextView) findViewById(R.id.etSearchPlace);

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this condition is for not contain any keyword in ediitext
                if (s.length() != 0) {

                    if (s.length() > 3) {
                        String regexStr = "^[0-9]*$";

                   /*     if (s.toString().matches(regexStr)) {
                            spinnerArrayAdapter = new VendorOnboardedList.CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, mobileList);
                        } else {
                            spinnerArrayAdapter = new VendorOnboardedList.CustomAdapter<String>(mcontext, android.R.layout.simple_spinner_item, nameList);

                        }
                        actv.setAdapter(spinnerArrayAdapter);//setting the adapter data into the AutoCompleteTextView
                       */
                        actv.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                actv.showDropDown();
                                return false;
                            }
                        });
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }


    void filter(String text) {
        List<SubCatetegory> temp = new ArrayList();
        for (SubCatetegory d : valueList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getValue().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        filterSubListAdapter.updateList(temp);
    }


    @Override
    public void onClick(View view, int position) {
        mainFiledname = categoryList.get(position).getLabel();
    }

    @Override
    public void onLongClick(View view, int position, String data) {
        if (data.equalsIgnoreCase("1")) {
            llSearch.setVisibility(View.VISIBLE);
        } else {
            llSearch.setVisibility(View.GONE);
        }
        valueList = new ArrayList<>();

        if (categoryList.get(position).getSubCatetegory().size() > 0) {
            valueList.addAll(categoryList.get(position).getSubCatetegory());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilterActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rvFilterSubCategory.setLayoutManager(linearLayoutManager);
            filterSubListAdapter = new FilterSubListAdapter(valueList, FilterActivity.this, FilterActivity.this);
            rvFilterSubCategory.setAdapter(filterSubListAdapter);
            filterSubListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void SingleClick(String popup, int position) {
        if (popup.equalsIgnoreCase("1")) {
            valueList.get(position).setChecked(true);
        } else {
            valueList.get(position).setChecked(false);
        }

        for (int i = 0; i < categoryList.size(); i++) {
            for (int i1 = 0; i1 < categoryList.get(i).getSubCatetegory().size(); i1++) {
                if (categoryList.get(i).getSubCatetegory().get(i1).isChecked()) {
                    setText = setText + "\n" + categoryList.get(i).getLabel() + " : " + categoryList.get(i).getSubCatetegory().get(i1).getValue();
                    //  jsonArray.put(jsonObject);
                }

            }

        }

        tvDataName.setText(setText);
        setText="";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;

            case R.id.btApply:
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < categoryList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    for (int i1 = 0; i1 < categoryList.get(i).getSubCatetegory().size(); i1++) {
                        if (categoryList.get(i).getSubCatetegory().get(i1).isChecked()) {
                            try {
                                jsonObject.put(categoryList.get(i).getType(), categoryList.get(i).getSubCatetegory().get(i1).getValueId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }

                        jsonObject = new JSONObject();
                    }

                }
                // Log.e("datagot", jsonArray.toString());
                Intent returnIntent1 = new Intent();
                returnIntent1.putExtra("result", jsonArray.toString());
                setResult(Activity.RESULT_OK, returnIntent1);
                finish();
                break;
        }

    }
}
