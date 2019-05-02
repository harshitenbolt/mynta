
package com.canvascoders.opaper.Beans.AadharVerifyResponse;

public class Aadharverify {

   /* private Integer responseCode;
    private String response;
    private String status;
    private List<AadharVerifyDatum> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AadharVerifyDatum> getData() {
        return data;
    }

    public void setData(List<AadharVerifyDatum> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

    private String response;

    private String responseCode;

    private String status;

    private AadharVerifyDatum[] data;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getResponseCode ()
    {
        return responseCode;
    }

    public void setResponseCode (String responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public AadharVerifyDatum[] getData ()
    {
        return data;
    }

    public void setData (AadharVerifyDatum[] data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", responseCode = "+responseCode+", status = "+status+", data = "+data+"]";
    }

}
