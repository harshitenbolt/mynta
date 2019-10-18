
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreType {

    @SerializedName("Mensa - Delivery")
    @Expose
    private List<MensaDelivery> mensaDelivery = null;

    public List<MensaDelivery> getMensaDelivery() {
        return mensaDelivery;
    }

    public void setMensaDelivery(List<MensaDelivery> mensaDelivery) {
        this.mensaDelivery = mensaDelivery;
    }

}
