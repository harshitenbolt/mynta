
package com.canvascoders.opaper.Beans.AadharVerifyResponse;

public class AadharVerifyDatum {


    private String proccess_id;

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