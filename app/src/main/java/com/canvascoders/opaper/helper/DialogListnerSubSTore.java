package com.canvascoders.opaper.helper;

import com.canvascoders.opaper.Beans.SubStoreType;

import org.json.JSONObject;

import java.util.List;

public interface DialogListnerSubSTore {


    void onClickDetails(String name, String fathername, String dob, String id);
    void onStoreType(Integer positin, JSONObject jsonObject);
    void onStoreType1(Integer positin, JSONObject jsonObject, List<SubStoreType> subStoreTypeList);

    void onClickChequeDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress);
    void onClickAddressDetails(String accName, String payeename, String ifsc, String bankname, String BranchName, String bankAdress, String dc);

  //  void onClickReset();
}
