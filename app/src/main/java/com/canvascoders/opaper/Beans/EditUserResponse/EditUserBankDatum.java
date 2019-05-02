package com.canvascoders.opaper.Beans.EditUserResponse;

public class EditUserBankDatum {
    private String id;

    private String payee_name;

    private String bank_name;

    private String updated_at;

    private String proccess_id;

    private String bank_ac;

    private String basic_details_id;

    private String created_at;

    private String bank_address;

    private String deleted;

    private String bank_branch_name;

    private String ifsc;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPayee_name ()
    {
        return payee_name;
    }

    public void setPayee_name (String payee_name)
    {
        this.payee_name = payee_name;
    }

    public String getBank_name ()
    {
        return bank_name;
    }

    public void setBank_name (String bank_name)
    {
        this.bank_name = bank_name;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getProccess_id ()
    {
        return proccess_id;
    }

    public void setProccess_id (String proccess_id)
    {
        this.proccess_id = proccess_id;
    }

    public String getBank_ac ()
    {
        return bank_ac;
    }

    public void setBank_ac (String bank_ac)
    {
        this.bank_ac = bank_ac;
    }

    public String getBasic_details_id ()
    {
        return basic_details_id;
    }

    public void setBasic_details_id (String basic_details_id)
    {
        this.basic_details_id = basic_details_id;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getBank_address ()
    {
        return bank_address;
    }

    public void setBank_address (String bank_address)
    {
        this.bank_address = bank_address;
    }

    public String getDeleted ()
    {
        return deleted;
    }

    public void setDeleted (String deleted)
    {
        this.deleted = deleted;
    }

    public String getBank_branch_name ()
    {
        return bank_branch_name;
    }

    public void setBank_branch_name (String bank_branch_name)
    {
        this.bank_branch_name = bank_branch_name;
    }

    public String getIfsc ()
    {
        return ifsc;
    }

    public void setIfsc (String ifsc)
    {
        this.ifsc = ifsc;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", payee_name = "+payee_name+", bank_name = "+bank_name+", updated_at = "+updated_at+", proccess_id = "+proccess_id+", bank_ac = "+bank_ac+", basic_details_id = "+basic_details_id+", created_at = "+created_at+", bank_address = "+bank_address+", deleted = "+deleted+", bank_branch_name = "+bank_branch_name+", ifsc = "+ifsc+"]";
    }
}
