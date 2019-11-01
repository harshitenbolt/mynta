package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.canvascoders.opaper.R;

public class MobileChangedSuccessActivity extends AppCompatActivity {
    ImageView ivBack;
    String msg ="";
    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_changed_success);
        tvMsg = findViewById(R.id.tvMsg);

        msg = getIntent().getStringExtra("data");
        tvMsg.setText(msg);

        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
