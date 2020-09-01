
package com.canvascoders.opaper.Beans.GetChequeOCRDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChequeDetail {

    @SerializedName("cheque_detail_id")
    @Expose
    private Integer chequeDetailId;
    @SerializedName("ifsc_code")
    @Expose
    private String ifscCode;
    @SerializedName("bank_account_number")
    @Expose
    private String bankAccountNumber;
    @SerializedName("bank_account_holder_name")
    @Expose
    private String bankAccountHolderName;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;

    public Integer getChequeDetailId() {
        return chequeDetailId;
    }

    public void setChequeDetailId(Integer chequeDetailId) {
        this.chequeDetailId = chequeDetailId;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
