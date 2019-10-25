
package com.canvascoders.opaper.Beans.GetGSTVerify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreAddress {

    @SerializedName("bnm")
    @Expose
    private String bnm;
    @SerializedName("st")
    @Expose
    private String st;
    @SerializedName("loc")
    @Expose
    private String loc;
    @SerializedName("bno")
    @Expose
    private String bno;
    @SerializedName("stcd")
    @Expose
    private String stcd;
    @SerializedName("dst")
    @Expose
    private String dst;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pncd")
    @Expose
    private String pncd;

    public String getBnm() {
        return bnm;
    }

    public void setBnm(String bnm) {
        this.bnm = bnm;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getBno() {
        return bno;
    }

    public void setBno(String bno) {
        this.bno = bno;
    }

    public String getStcd() {
        return stcd;
    }

    public void setStcd(String stcd) {
        this.stcd = stcd;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPncd() {
        return pncd;
    }

    public void setPncd(String pncd) {
        this.pncd = pncd;
    }

}
