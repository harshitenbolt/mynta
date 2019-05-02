
package com.canvascoders.opaper.Beans.dc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("dc")
    @Expose
    private List<DC> dc;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum() {
    }

    /**
     * 
     * @param dc
     */
    public Datum(List<DC> dc,String city,String state) {
        super();
        this.dc = dc;
        this.city = city;
        this.state = state;
    }

    public List<DC> getDc() {
        return dc;
    }

    public void setDc(List<DC> dc) {
        this.dc = dc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
