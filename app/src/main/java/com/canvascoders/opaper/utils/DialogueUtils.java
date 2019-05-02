package com.canvascoders.opaper.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.fragment.ChequeUploadFragment;
import com.canvascoders.opaper.helper.DialogListner;

public class DialogueUtils {
    private static Dialog dialog;




    public static void failedPayment(Context mContext, DialogListner dialogListner) {
        TextView firstname, lastname, pickup_loca, drop_loca, start_time, start_dat,vehicle_nam,vehicle_col;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_failed_payment);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        dialog.show();
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListner.onClickPositive();
            }
        });
    }

    public static void successPayment(Context mContext, DialogListner dialogListner) {
        TextView firstname, lastname, pickup_loca, drop_loca, start_time, start_dat,vehicle_nam,vehicle_col;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_success_payment);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        dialog.show();
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListner.onClickPositive();
            }
        });
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
