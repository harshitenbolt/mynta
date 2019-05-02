package com.canvascoders.opaper.Beans.EditUserResponse;

public class EditUserResponse {
    private String response;

    private String responseCode;

    private String status;

    private UserDetailResult[] data;

    private EditUserBankDatum[] bank;

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

    public UserDetailResult[] getData ()
    {
        return data;
    }

    public void setData (UserDetailResult[] data)
    {
        this.data = data;
    }

    public EditUserBankDatum[] getBank ()
    {
        return bank;
    }

    public void setBank (EditUserBankDatum[] bank)
    {
        this.bank = bank;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", responseCode = "+responseCode+", status = "+status+", data = "+data+", bank = "+bank+"]";
    }
}
