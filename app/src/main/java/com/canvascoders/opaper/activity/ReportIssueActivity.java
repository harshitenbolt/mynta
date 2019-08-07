package com.canvascoders.opaper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.canvascoders.opaper.Beans.SubmitReportResponse.SubmitReportResponse;
import com.canvascoders.opaper.Beans.SupportSubjectResponse.Datum;
import com.canvascoders.opaper.Beans.SupportSubjectResponse.SupportSubjectResponse;
import com.canvascoders.opaper.Beans.dc.DC;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.fragment.InfoFragment;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueActivity extends AppCompatActivity {

    ImageView imageView, ivBack;
    private ArrayList<String> subjectList = new ArrayList<>();
    private List<String> priorityList = new ArrayList<>();
    String ScreenName = "", enbolt_id = "",storename="",invoice_num="";
    Spinner subject, priority;
    String priority_id;
    private SessionManager sessionManager;
    private TextView tvEnbolt, tvAgent,tvInvoiceNo;
    private Button btSubmit;
    private String attachment = "", invoiceId = "";
    ProgressDialog progressDialog;
    Bitmap bitmap;
    EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        init();
    }

    private void init() {
        etDescription = findViewById(R.id.etDescription);
        imageView = findViewById(R.id.issue);
        tvInvoiceNo = findViewById(R.id.tvInvoiceNum);
        subject = findViewById(R.id.snSubject);
        priority = findViewById(R.id.snPriority);
        tvAgent = findViewById(R.id.tvAgent);
        ivBack = findViewById(R.id.iv_back);
        tvEnbolt = findViewById(R.id.tvEnbolt);
        btSubmit = findViewById(R.id.btSubmit);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btSubmit.setEnabled(true);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppApplication.networkConnectivity.isNetworkAvailable()) {
                    ApiCallSubmitReport();
                } else {
                    Constants.ShowNoInternet(ReportIssueActivity.this);
                }

            }
        });

        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        ScreenName = intent.getStringExtra(Constants.PARAM_SCREEN_NAME);
        enbolt_id = intent.getStringExtra(Constants.KEY_PROCESS_ID);
        invoiceId = intent.getStringExtra(Constants.KEY_INVOICE_ID);
        storename = intent.getStringExtra(Constants.KEY_NAME);
        invoice_num = intent.getStringExtra(Constants.KEY_INVOICE_NUM);
        Log.e("invoice_num",invoice_num);
        imageView.setImageBitmap(bitmap);
        tvEnbolt.setText(storename);
        tvInvoiceNo.setText(invoice_num);
        tvAgent.setText(sessionManager.getAgentID());
       /* findViewById(R.id.btn_next).setEnabled(true);
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportIssueActivity.this, "Report submitted", Toast.LENGTH_SHORT).show();
            }
        });*/
        priorityList = Arrays.asList(getResources().getStringArray(R.array.Priority));
        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(ReportIssueActivity.this, android.R.layout.simple_spinner_item, priorityList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(spinnerArrayAdapter);
        priority.setSelection(0);
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority_id = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (AppApplication.networkConnectivity.isNetworkAvailable()) {
            ApiCallGetSubject();
        } else {
            Constants.ShowNoInternet(ReportIssueActivity.this);
        }

    }

    private void ApiCallSubmitReport() {
        progressDialog.show();
        MultipartBody.Part attachment_part = null;

        Map<String, String> param = new HashMap<>();

        param.put(Constants.PARAM_AGENT_ID, sessionManager.getAgentID());
        param.put(Constants.PARAM_PROCESS_ID, enbolt_id);
        param.put(Constants.PARAM_DESCRIPTION, etDescription.getText().toString());
        param.put(Constants.PARAM_SUBJECT_NAME, ScreenName);
        param.put(Constants.PARAM_INVOICE_ID, invoiceId);
        param.put(Constants.PARAM_PRIORITY, "" + priority_id);
        Log.e("Id_done", "" +priority_id);

        attachment = ImagePicker.getBitmapPath(bitmap, this);
        File imagefile1 = new File(attachment);
        attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(attachment)), imagefile1));
        ApiClient.getClient().create(ApiInterface.class).submitReportResponse("Bearer " + sessionManager.getToken(), param, attachment_part).enqueue(new Callback<SubmitReportResponse>() {
            @Override
            public void onResponse(Call<SubmitReportResponse> call, Response<SubmitReportResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SubmitReportResponse submitReportResponse = response.body();
                    if (submitReportResponse.getResponseCode() == 200) {
                        Toast.makeText(ReportIssueActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ReportIssueActivity.this, submitReportResponse.getResponse(), Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<SubmitReportResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    private void ApiCallGetSubject() {
        Map<String, String> param = new HashMap<>();
        param.put(Constants.PARAM_SCREEN_NAME, ScreenName);
        ApiClient.getClient().create(ApiInterface.class).getSubject("Bearer " + sessionManager.getToken(), param).enqueue(new Callback<SupportSubjectResponse>() {
            @Override
            public void onResponse(Call<SupportSubjectResponse> call, Response<SupportSubjectResponse> response) {
                if (response.isSuccessful()) {
                    SupportSubjectResponse supportSubjectResponse = response.body();
                    if (supportSubjectResponse.getResponseCode() == 200) {
                        for (int i = 0; i < supportSubjectResponse.getData().size(); i++) {
                            subjectList.add(supportSubjectResponse.getData().get(i).getScreenName());
                        }
                        CustomAdapter<String> spinnerArrayAdapter = new CustomAdapter<String>(ReportIssueActivity.this, android.R.layout.simple_spinner_item, subjectList);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(spinnerArrayAdapter);
                        subject.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<SupportSubjectResponse> call, Throwable t) {


            }
        });
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


}
