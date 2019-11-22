package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.PauseTaskResponse.PauseTaskResponse;
import com.canvascoders.opaper.Beans.TaskDetailResponse.Datum;
import com.canvascoders.opaper.Beans.TaskDetailResponse.SubTaskReason;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.api.ApiClient;
import com.canvascoders.opaper.api.ApiInterface;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.ImagePicker;
import com.canvascoders.opaper.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReasonPauseActivity extends AppCompatActivity implements View.OnClickListener {

    Button btSubmit;
    EditText etDescription;
    ImageView ivClose, ivIssueImage;
    Spinner spReasons;
    Datum data;
    List<SubTaskReason> subtaskReasonList = new ArrayList<>();
    List<String> subTaskReasonNameList = new ArrayList<>();
    String taskImage = "";
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    int taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reason_pause);
        sessionManager = new SessionManager(this);
        data = (Datum) getIntent().getSerializableExtra(Constants.DATA);
        taskid = getIntent().getIntExtra(Constants.PARAM_TASK_ID, 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        init();
    }

    private void init() {
        spReasons = findViewById(R.id.spReasons);
        ivIssueImage = findViewById(R.id.ivIssueImage);
        ivIssueImage.setOnClickListener(this);
        ivClose = findViewById(R.id.iv_back_process);
        ivClose.setOnClickListener(this);
        etDescription = findViewById(R.id.etDescription);
        btSubmit = findViewById(R.id.btSubmitReasonDetails);
        btSubmit.setOnClickListener(this);

        subtaskReasonList = data.getSubTaskReason();
        for (int i = 0; i < subtaskReasonList.size(); i++) {
            subTaskReasonNameList.add(subtaskReasonList.get(i).getName());
        }

        CustomAdapter<SubTaskReason> spinnerArrayAdapter = new CustomAdapter<SubTaskReason>(EditReasonPauseActivity.this, android.R.layout.simple_spinner_item, subtaskReasonList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReasons.setAdapter(spinnerArrayAdapter);
        spReasons.setSelection(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivIssueImage:
                Intent chooseImageIntent = ImagePicker.getCameraIntent(EditReasonPauseActivity.this);
                startActivityForResult(chooseImageIntent, 200);
                break;


            case R.id.btSubmitReasonDetails:
                if (isValid(v)) {
                    APiCallPauseList();
                }
                break;

            case R.id.iv_back_process:
                finish();
                break;
        }

    }


    class CustomAdapter<T> extends ArrayAdapter<SubTaskReason> {
        public CustomAdapter(Context context, int textViewResourceId,
                             List<SubTaskReason> objects) {

            super(context, textViewResourceId, objects);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SubTaskReason subTaskReason = getItem(position);

            TextView v = (TextView) super
                    .getView(position, convertView, parent);
            Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.monteregular);
            ((TextView) v).setTypeface(typeface);
            v.setTextSize(10);

            v.setText(subTaskReason.getName());
            return v;

        }

        public TextView getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
            SubTaskReason subTaskReason = getItem(position);
            TextView v = (TextView) super
                    .getView(position, convertView, parent);
            Typeface typeface = ResourcesCompat.getFont(parent.getContext(), R.font.montesemibold);
            ((TextView) v).setTypeface(typeface);
            v.setTextSize(15);
            v.setLines(2);
            v.setPadding(10, 10, 10, 10);
            v.setText(subTaskReason.getName());
            return v;
        }
    }


    private boolean isValid(View v) {
        if (etDescription.getText().toString().equalsIgnoreCase("")) {
            etDescription.setError("Provide Description");
            etDescription.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 200 && resultCode == RESULT_OK)) {

            if (resultCode == RESULT_OK) {


                Bitmap bitmap = ImagePicker.getImageFromResult(EditReasonPauseActivity.this, resultCode, data);
                // img_doc_upload_2.setImageBitmap(bitmap);
                taskImage = ImagePicker.getBitmapPath(bitmap, EditReasonPauseActivity.this); // ImageUtils.getInstant().getImageUri(getActivity(), photo);

                Glide.with(EditReasonPauseActivity.this).load(taskImage).into(ivIssueImage);
            }


            //setButtonImage();

        }

    }

    private void APiCallPauseList() {

        SubTaskReason subTaskReason = (SubTaskReason) spReasons.getSelectedItem();
        Call<PauseTaskResponse> call;
        MultipartBody.Part attachment_part = null;
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_TASK_ID, String.valueOf(taskid));
        params.put(Constants.PARAM_SUB_TASK_REASON_ID, String.valueOf(subTaskReason.getId()));
        params.put(Constants.PARAM_SUB_TASK_REASON_TEXT, subTaskReason.getName());
        params.put(Constants.PARAM_DESCRIPTION, etDescription.getText().toString());


        if (taskImage.equalsIgnoreCase("")) {
            call = ApiClient.getClient().create(ApiInterface.class).pauseTask("Bearer " + sessionManager.getToken(), params);

        } else {

            File imagefile1 = new File(taskImage);
            attachment_part = MultipartBody.Part.createFormData(Constants.PARAM_ATTACHMENT, imagefile1.getName(), RequestBody.create(MediaType.parse(Constants.getMimeType(taskImage)), imagefile1));

            call = ApiClient.getClient().create(ApiInterface.class).pauseTaskwithImage("Bearer " + sessionManager.getToken(), params, attachment_part);

        }
        call.enqueue(new Callback<PauseTaskResponse>() {
            @Override
            public void onResponse(Call<PauseTaskResponse> call, Response<PauseTaskResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    PauseTaskResponse pauseTaskResponse = response.body();
                    if (pauseTaskResponse.getResponseCode() == 200) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "stop");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } else {
                        Toast.makeText(EditReasonPauseActivity.this, pauseTaskResponse.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PauseTaskResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });


    }


}
