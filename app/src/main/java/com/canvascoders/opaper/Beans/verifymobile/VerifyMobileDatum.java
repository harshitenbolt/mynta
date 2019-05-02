
package com.canvascoders.opaper.Beans.verifymobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyMobileDatum {

    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("screen")
    @Expose
    private Integer screen;
    @SerializedName("bank")
    @Expose
    private Integer bank;

    /**
     * No args constructor for use in serialization
     * 
     */
    public VerifyMobileDatum() {
    }

    /**
     * 
     * @param screen
     * @param bank
     * @param proccessId
     */
    public VerifyMobileDatum(Integer proccessId, Integer screen, Integer bank) {
        super();
        this.proccessId = proccessId;
        this.screen = screen;
        this.bank = bank;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

}
