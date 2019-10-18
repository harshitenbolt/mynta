
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreList {

    @SerializedName("Mensa - Delivery")
    @Expose
    private Integer mensaDelivery;
    @SerializedName("Marvel - DC")
    @Expose
    private Integer marvelDC;

    public Integer getMensaDelivery() {
        return mensaDelivery;
    }

    public void setMensaDelivery(Integer mensaDelivery) {
        this.mensaDelivery = mensaDelivery;
    }

    public Integer getMarvelDC() {
        return marvelDC;
    }

    public void setMarvelDC(Integer marvelDC) {
        this.marvelDC = marvelDC;
    }

}
