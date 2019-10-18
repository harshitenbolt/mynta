
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Datum {

    public Map<String, Duration> durationMap;


    public Map<String, Duration> getDurationMap() {
        return durationMap;
    }

    public void setDurationMap(Map<String, Duration> durationMap) {
        this.durationMap = durationMap;
    }



}
