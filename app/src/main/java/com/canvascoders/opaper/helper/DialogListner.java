package com.canvascoders.opaper.helper;

import org.json.JSONObject;

public interface DialogListner {


    void onClickPositive();

    void onClickNegative();
    void onClickDetails(String name,String fathername,String dob,String id );
    void onStoreType(Integer positin, JSONObject jsonObject);

    void onClickChequeDetails(String accName,String payeename,String ifsc,String bankname,String BranchName,String bankAdress);
    void onClickAddressDetails(String accName,String payeename,String ifsc,String bankname,String BranchName,String bankAdress,String dc);

  //  void onClickReset();
}
