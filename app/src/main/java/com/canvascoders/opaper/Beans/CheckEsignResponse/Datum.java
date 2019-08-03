
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