
package com.canvascoders.opaper.Beans.CheckEsignResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("if_gst")
    @Expose
    private String ifGst;
    @SerializedName("proccess_id")
    @Expose
    private String proccessId;
    @SerializedName("screen")
    @Expose
    private Integer screen;
    @SerializedName("screen_msg")
    @Expose
    private String screenMsg;

    public String getIf_msme() {
        return if_msme;
    }

    public void setIf_msme(String if_msme) {
        this.if_msme = if_msme;
    }

    @SerializedName("if_msme")
    @Expose
    private String if_msme;


    public boolean getIfEcom() {
        return ifEcom;
    }

    public void setIfEcom(boolean ifEcom) {
        this.ifEcom = ifEcom;
    }

    @SerializedName("if_ecom")
    @Expose
    private boolean ifEcom;

    public String getIfGst() {
        return ifGst;
    }

    public void setIfGst(String ifGst) {
        this.ifGst = ifGst;
    }

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public String getScreenMsg() {
        return screenMsg;
    }

    public void setScreenMsg(String screenMsg) {
        this.screenMsg = screenMsg;
    }

}