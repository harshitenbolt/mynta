package com.canvascoders.opaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;

public class TaskProccessDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    ImageView ivEditLocation, ivEditKyc, ivEditPan, ivEditBank, ivEditStoreDetails, ivOwnerDetails, ivGstInformation, ivDeliveryDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_proccess_detail);
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.iv_back_process);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivEditLocation = findViewById(R.id.ivEditLocation);
        ivEditKyc = findViewById(R.id.ivEditKYC);
        ivEditPan = findViewById(R.id.ivEditPan);
        ivEditBank = findViewById(R.id.ivEditCheque);
        ivEditStoreDetails = findViewById(R.id.ivEditStoreDetails);
        ivOwnerDetails = findViewById(R.id.ivEditOwnerDetails);
        ivGstInformation = findViewById(R.id.ivEditGSTDetails);
        ivDeliveryDetails = findViewById(R.id.ivDeliveryDetails);
        ivEditLocation.setOnClickListener(this);
        ivEditKyc.setOnClickListener(this);
        ivEditPan.setOnClickListener(this);
        ivEditBank.setOnClickListener(this);
        ivEditStoreDetails.setOnClickListener(this);
        ivOwnerDetails.setOnClickListener(this);
        ivGstInformation.setOnClickListener(this);
        ivDeliveryDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivEditLocation:
                Intent i = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i.putExtra(Constants.DATA, "Location");
                i.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i);
                break;
            case R.id.ivEditKYC:
                Intent i1 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i1.putExtra(Constants.DATA, "KYC");
                i1.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i1);
                break;
            case R.id.ivEditPan:
                Intent i2 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i2.putExtra(Constants.DATA, "PAN");
                i2.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i2);
                break;
            case R.id.ivEditCheque:
                Intent i3 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i3.putExtra(Constants.DATA, "CHEQUE");
                i3.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i3);
                break;
            case R.id.ivEditStoreDetails:
                Intent i4 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i4.putExtra(Constants.DATA, "STORE");
                i4.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i4);
                break;
            case R.id.ivEditOwnerDetails:
                Intent i5 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i5.putExtra(Constants.DATA, "OWNER");
                i5.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i5);
                break;
            case R.id.ivEditGSTDetails:
                Intent i6 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i6.putExtra(Constants.DATA, "GST");
                i6.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i6);
                break;
            case R.id.ivDeliveryDetails:
                Intent i7 = new Intent(TaskProccessDetailActivity.this, EditFunctionalityKiranaActivity.class);
                i7.putExtra(Constants.DATA, "DELIVERY");
                i7.putExtra(Constants.KEY_EMP_MOBILE, "");
                startActivity(i7);
                break;


        }

    }
}
