package com.canvascoders.opaper.Beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorInvoiceList implements Serializable {

    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_address")
    @Expose
    private String storeAddress;
    @SerializedName("no_of_invoice")
    @Expose
    private Integer noOfInvoice;
    @SerializedName("bill_period")
    @Expose
    private String billPeriod;

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Integer getNoOfInvoice() {
        return noOfInvoice;
    }

    public void setNoOfInvoice(Integer noOfInvoice) {
        this.noOfInvoice = noOfInvoice;
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(String billPeriod) {
        this.billPeriod = billPeriod;
    }

    /**
     * No args constructor for use in serialization
     */
    public VendorInvoiceList() {
    }

    public VendorInvoiceList(Integer proccessId, String mobileNo, String name, String storeName, String storeAddress, Integer noOfInvoice, String billPeriod) {
        this.proccessId = proccessId;
        this.mobileNo = mobileNo;
        this.name = name;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.noOfInvoice = noOfInvoice;
        this.billPeriod = billPeriod;
    }


}


