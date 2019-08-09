
package com.canvascoders.opaper.Beans.ResignAgreeDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ESignDocumentDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("esign_doc")
    @Expose
    private String esignDoc;
    @SerializedName("noc")
    @Expose
    private String noc;
    @SerializedName("gstdeclaration")
    @Expose
    private String gstdeclaration;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("agreement_esign_url")
    @Expose
    private Object agreementEsignUrl;
    @SerializedName("noc_esign_url")
    @Expose
    private Object nocEsignUrl;
    @SerializedName("gst_esign_url")
    @Expose
    private Object gstEsignUrl;
    @SerializedName("agreement_esign_date")
    @Expose
    private Object agreementEsignDate;
    @SerializedName("noc_esign_date")
    @Expose
    private Object nocEsignDate;
    @SerializedName("gst_esign_date")
    @Expose
    private Object gstEsignDate;
    @SerializedName("esign_doc_old")
    @Expose
    private Object esignDocOld;
    @SerializedName("pan_esign")
    @Expose
    private Object panEsign;
    @SerializedName("pan_esign_date")
    @Expose
    private Object panEsignDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public String getEsignDoc() {
        return esignDoc;
    }

    public void setEsignDoc(String esignDoc) {
        this.esignDoc = esignDoc;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getGstdeclaration() {
        return gstdeclaration;
    }

    public void setGstdeclaration(String gstdeclaration) {
        this.gstdeclaration = gstdeclaration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Object getAgreementEsignUrl() {
        return agreementEsignUrl;
    }

    public void setAgreementEsignUrl(Object agreementEsignUrl) {
        this.agreementEsignUrl = agreementEsignUrl;
    }

    public Object getNocEsignUrl() {
        return nocEsignUrl;
    }

    public void setNocEsignUrl(Object nocEsignUrl) {
        this.nocEsignUrl = nocEsignUrl;
    }

    public Object getGstEsignUrl() {
        return gstEsignUrl;
    }

    public void setGstEsignUrl(Object gstEsignUrl) {
        this.gstEsignUrl = gstEsignUrl;
    }

    public Object getAgreementEsignDate() {
        return agreementEsignDate;
    }

    public void setAgreementEsignDate(Object agreementEsignDate) {
        this.agreementEsignDate = agreementEsignDate;
    }

    public Object getNocEsignDate() {
        return nocEsignDate;
    }

    public void setNocEsignDate(Object nocEsignDate) {
        this.nocEsignDate = nocEsignDate;
    }

    public Object getGstEsignDate() {
        return gstEsignDate;
    }

    public void setGstEsignDate(Object gstEsignDate) {
        this.gstEsignDate = gstEsignDate;
    }

    public Object getEsignDocOld() {
        return esignDocOld;
    }

    public void setEsignDocOld(Object esignDocOld) {
        this.esignDocOld = esignDocOld;
    }

    public Object getPanEsign() {
        return panEsign;
    }

    public void setPanEsign(Object panEsign) {
        this.panEsign = panEsign;
    }

    public Object getPanEsignDate() {
        return panEsignDate;
    }

    public void setPanEsignDate(Object panEsignDate) {
        this.panEsignDate = panEsignDate;
    }

}
