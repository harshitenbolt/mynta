
package com.canvascoders.opaper.Beans.PancardVerifyResponse;

import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonDatum {

   /* private String proccessId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

    private String proccess_id;
    @SerializedName("delivery_boy_screen")
    @Expose
    private String delBoysCreen;

    public String getDelBoysCreen() {
        return delBoysCreen;
    }

    public void setDelBoysCreen(String delBoysCreen) {
        this.delBoysCreen = delBoysCreen;
    }

    public String getProccess_id ()
    {
        return proccess_id;
    }

    public void setProccess_id (String proccess_id)
    {
        this.proccess_id = proccess_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [proccess_id = "+proccess_id+"]";
    }

}
