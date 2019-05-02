package com.canvascoders.opaper.Beans;


public class BillList {

    private Integer id;
    private String proccess_id;
    private String store_name;
    private String bill_period;
    private String dc;
    private String store_address;
    private String amt;
    private String mobile_no;
    private String updated_at;
    private String vendor_id;
    private int invoice_status ;

    public BillList() {
    }


    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public BillList(Integer id, String proccess_id, String store_name, String bill_period, String dc, String store_address, String amt, String mobile_no, String updated_at, int invoice_status, String vendor_id ) {
        this.id = id;
        this.proccess_id = proccess_id;
        this.vendor_id = vendor_id;
        this.store_name = store_name;
        this.bill_period = bill_period;
        this.dc = dc;
        this.store_address = store_address;
        this.amt = amt;
        this.mobile_no = mobile_no;
        this.updated_at = updated_at;
        this.invoice_status  = invoice_status ;

    }

    public int getInvoice_status() {
        return invoice_status;
    }

    public void setInvoice_status(int invoice_status) {
        this.invoice_status = invoice_status;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProccess_id() {
        return proccess_id;
    }

    public void setProccess_id(String proccess_id) {
        this.proccess_id = proccess_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBill_period() {
        return bill_period;
    }

    public void setBill_period(String bill_period) {
        this.bill_period = bill_period;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getAmt() {
        return amt;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
