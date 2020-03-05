package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.canvascoders.opaper.R;

import org.w3c.dom.Text;

public class AddNewTaskActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBack;
    Spinner spReasons;
    EditText etTaskDetails;
    TextView tvSubmit, tvTaskStartDate, tvTaskStartTime, tvTaskEndDate, tvTaskEndtime;
    ImageView ivAddTaskImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spReasons = findViewById(R.id.spReasons);
        etTaskDetails = findViewById(R.id.etDescription);
        tvTaskStartDate = findViewById(R.id.tvPeakDate);
        tvTaskStartTime = findViewById(R.id.tvPeakHoursStart);
        tvTaskEndDate = findViewById(R.id.tvEndTaskDate);
        tvTaskEndtime = findViewById(R.id.tvEndTaskHour);
        ivAddTaskImage = findViewById(R.id.ivTaskImage);
        tvSubmit = findViewById(R.id.tvSubmnit);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
