
package com.canvascoders.opaper.Beans.GetTrackingDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocUpload {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("gst_certificate_image")
    @Expose
    private String gstCertificateImage;
    @SerializedName("adhar_card_front")
    @Expose
    private String adharCardFront;
    @SerializedName("adhar_card_back")
    @Expose
    private String adharCardBack;
    @SerializedName("voter_card_front")
    @Expose
    private String voterCardFront;
    @SerializedName("voter_card_back")
    @Expose
    private String voterCardBack;
    @SerializedName("dl_card_front")
    @Expose
    private String dlCardFront;
    @SerializedName("dl_card_back")
    @Expose
    private String dlCardBack;
    @SerializedName("if_shop_act")
    @Expose
    private String ifShopAct;
    @SerializedName("cancelled_cheque")
    @Expose
    private String cancelledCheque;
    @SerializedName("noc")
    @Expose
    private String noc;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("owner_image")
    @Expose
    private String ownerImage;

    @SerializedName("msme_registration_cert")
    @Expose
    private String msme_registration_cert;

    public String getMsme_registration_cert() {
        return msme_registration_cert;
    }

    public void setMsme_registration_cert(String msme_registration_cert) {
        this.msme_registration_cert = msme_registration_cert;
    }

    @SerializedName("mass_upload")
    @Expose
    private String massUpload;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted")
    @Expose
    private String deleted;

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

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getGstCertificateImage() {
        return gstCertificateImage;
    }

    public void setGstCertificateImage(String gstCertificateImage) {
        this.gstCertificateImage = gstCertificateImage;
    }

    public String getAdharCardFront() {
        return adharCardFront;
    }

    public void setAdharCardFront(String adharCardFront) {
        this.adharCardFront = adharCardFront;
    }

    public String getAdharCardBack() {
        return adharCardBack;
    }

    public void setAdharCardBack(String adharCardBack) {
        this.adharCardBack = adharCardBack;
    }

    public String getVoterCardFront() {
        return voterCardFront;
    }

    public void setVoterCardFront(String voterCardFront) {
        this.voterCardFront = voterCardFront;
    }

    public String getVoterCardBack() {
        return voterCardBack;
    }

    public void setVoterCardBack(String voterCardBack) {
        this.voterCardBack = voterCardBack;
    }

    public String getDlCardFront() {
        return dlCardFront;
    }

    public void setDlCardFront(String dlCardFront) {
        this.dlCardFront = dlCardFront;
    }

    public String getDlCardBack() {
        return dlCardBack;
    }

    public void setDlCardBack(String dlCardBack) {
        this.dlCardBack = dlCardBack;
    }

    public String getIfShopAct() {
        return ifShopAct;
    }

    public void setIfShopAct(String ifShopAct) {
        this.ifShopAct = ifShopAct;
    }

    public String getCancelledCheque() {
        return cancelledCheque;
    }

    public void setCancelledCheque(String cancelledCheque) {
        this.cancelledCheque = cancelledCheque;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getMassUpload() {
        return massUpload;
    }

    public void setMassUpload(String massUpload) {
        this.massUpload = massUpload;
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

}
