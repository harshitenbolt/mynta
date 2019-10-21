
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Datum {

    public Map<String, List<MensaDelivery> >durationMap;


    public Map<String, List<MensaDelivery>> getDurationMap() {
        return durationMap;
    }

    public void setDurationMap(Map<String, List<MensaDelivery>> durationMap) {
        this.durationMap = durationMap;
    }


}
