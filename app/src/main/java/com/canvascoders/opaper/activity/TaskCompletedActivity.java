package com.canvascoders.opaper.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.canvascoders.opaper.R;


public class TaskCompletedActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivActionBarBack;
    private Button btn_onboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_completed3);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        initialize();

    }

    private void initialize() {
        ivActionBarBack = findViewById(R.id.iv_back_task_completed);
        ivActionBarBack.setOnClickListener(this);
        btn_onboard = findViewById(R.id.btn_onboard);
        btn_onboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_task_completed:
                finish();
                startActivity(new Intent(TaskCompletedActivity.this,DashboardActivity.class));
                break;
            case R.id.btn_onboard:
                finish();
                startActivity(new Intent(TaskCompletedActivity.this,DashboardActivity.class));
                break;

        }
    }
}
