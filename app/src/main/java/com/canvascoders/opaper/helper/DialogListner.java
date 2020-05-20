package com.canvascoders.opaper.helper;

public interface DialogListner {


    void onClickPositive();

    void onClickNegative();
    void onClickDetails(String name,String fathername,String dob,String id );

    void onClickChequeDetails(String accName,String payeename,String ifsc,String bankname,String BranchName,String bankAdress);
    void onClickAddressDetails(String accName,String payeename,String ifsc,String bankname,String BranchName,String bankAdress,String dc);

  //  void onClickReset();
}
