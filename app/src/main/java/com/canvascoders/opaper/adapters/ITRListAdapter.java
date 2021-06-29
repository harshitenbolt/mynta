package com.canvascoders.opaper.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ApprovalRateDetail;
import com.canvascoders.opaper.Beans.getITrResponse.Datum;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.helper.DialogListner;
import com.canvascoders.opaper.helper.RecyclerViewClickListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ITRListAdapter extends RecyclerView.Adapter<ITRListAdapter.ItemHolder> {

    public List<Datum> bankDetailList;
    Context mContext;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    DialogListner recyclerViewClickListener;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public ITRListAdapter(Context ctx, List<Datum> dataViews, DialogListner recyclerViewClickListener) {
        this.bankDetailList = dataViews;
        mContext = ctx;
        this.recyclerViewClickListener = recyclerViewClickListener;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_itr, parent, false);
        return new ITRListAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        holder.tvITRYear.setText("FY:- " + bankDetailList.get(position).getFinancialYear() + "  (AY :- " + bankDetailList.get(position).getAssessmentYear() + ")");
        holder.etITRNumber.setText(bankDetailList.get(position).getItrNumber());
        holder.ivAddImageITR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(holder.etITRNumber.getText()) && !TextUtils.isEmpty(holder.tvItRDate.getText())) {
                    recyclerViewClickListener.onClickChequeDetails(holder.etITRNumber.getText().toString(), holder.tvItRDate.getText().toString(), String.valueOf(position), "", "", "");
                    holder.etITRNumber.setError(null);
                } else {
                    holder.etITRNumber.setError("Provide ITR Number and Date");
                }

            }
        });


        holder.etITRNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bankDetailList.get(position).setItrNumber(holder.etITRNumber.getText().toString());
            }
        });

        // if (bankDetailList.get(position).isSelectedImage()) {
        Glide.with(mContext).load(bankDetailList.get(position).getImage()).placeholder(R.drawable.ic_add_img)
                .into(holder.ivAddImageITR);


//        holder.tvStauts.setText(bankDetail.getStatus());
        holder.tvItRDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c.setTime(today);
                long minDate = c.getTime().getTime(); //
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                String monthString = String.valueOf(monthOfYear + 1);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }


                                //logic for add 0 in string if date digit is on 1 only
                                String daysString = String.valueOf(dayOfMonth);
                                if (daysString.length() == 1) {
                                    daysString = "0" + daysString;
                                }

                                holder.tvItRDate.setText(year + "-" + monthString + "-" + daysString);
                                bankDetailList.get(position).setDateofITR(holder.tvItRDate.getText().toString());
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return bankDetailList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tvITRYear, tvItRDate;
        public EditText etITRNumber;
        ImageView ivAddImageITR;

        public ItemHolder(View itemView) {
            super(itemView);
            tvITRYear = itemView.findViewById(R.id.tvFinancialYear);
            tvItRDate = itemView.findViewById(R.id.tvItRDate);
            etITRNumber = itemView.findViewById(R.id.etITRNumber);
            ivAddImageITR = itemView.findViewById(R.id.ivAddImageITR);
        }
    }


}
