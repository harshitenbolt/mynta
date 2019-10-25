
package com.canvascoders.opaper.Beans.GetGSTVerify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;
    @SerializedName("go_to_pan_screen")
    @Expose
    private Boolean goToPanScreen;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_address")
    @Expose
    private StoreAddress storeAddress;
    @SerializedName("pan_father_name")
    @Expose
    private String panFatherName;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("gst_pan_no")
    @Expose
    private String gstPanNo;
    @SerializedName("pan_url")
    @Expose
    private String panUrl;

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

    public Boolean getGoToPanScreen() {
        return goToPanScreen;
    }

    public void setGoToPanScreen(Boolean goToPanScreen) {
        this.goToPanScreen = goToPanScreen;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public StoreAddress getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(StoreAddress storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getPanFatherName() {
        return panFatherName;
    }

    public void setPanFatherName(String panFatherName) {
        this.panFatherName = panFatherName;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getGstPanNo() {
        return gstPanNo;
    }

    public void setGstPanNo(String gstPanNo) {
        this.gstPanNo = gstPanNo;
    }

    public String getPanUrl() {
        return panUrl;
    }

    public void setPanUrl(String panUrl) {
        this.panUrl = panUrl;
    }

}
