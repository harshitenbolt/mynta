
package com.canvascoders.opaper.Beans.GetTrackingDetailResponse;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("proccessDetail")
    @Expose
    private ProccessDetail proccessDetail;
    @SerializedName("proccessTrack")
    @Expose
    private ProccessTrack proccessTrack;
    @SerializedName("basicDetails")
    @Expose
    private BasicDetails basicDetails;


    @SerializedName("bankDetails")
    @Expose
    private List<BankDetails> bankDetails;

    public List<VendorRejection> getVendorRejection() {
        return vendorRejection;
    }

    public void setVendorRejection(List<VendorRejection> vendorRejection) {
        this.vendorRejection = vendorRejection;
    }

    @SerializedName("vendorRejection")
    @Expose
    private List<VendorRejection> vendorRejection;

    public List<CancelledCheque> getCancelledCheques() {
        return cancelledCheques;
    }

    public void setCancelledCheques(List<CancelledCheque> cancelledCheques) {
        this.cancelledCheques = cancelledCheques;
    }

    @SerializedName("cancelledCheque")
    @Expose
    private List<CancelledCheque> cancelledCheques;


    public List<BankDetails> getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(List<BankDetails> bankDetails) {
        this.bankDetails = bankDetails;
    }

    @SerializedName("basicDetailRate")
    @Expose
    private List<Object> basicDetailRate = null;
    @SerializedName("docUpload")
    @Expose
    private DocUpload docUpload;
    @SerializedName("deliveryBoysDetail")
    @Expose
    private List<Object> deliveryBoysDetail = null;
    @SerializedName("deliveryBoysDetailCount")
    @Expose
    private Integer deliveryBoysDetailCount;
    @SerializedName("shopActUpload")
    @Expose
    private List<Object> shopActUpload = null;
    @SerializedName("esignDocument")
    @Expose
    private List<Object> esignDocument = null;
    @SerializedName("trackDetail")
    @Expose
    private TrackDetail trackDetail;

    public ProccessDetail getProccessDetail() {
        return proccessDetail;
    }

    public void setProccessDetail(ProccessDetail proccessDetail) {
        this.proccessDetail = proccessDetail;
    }

    public ProccessTrack getProccessTrack() {
        return proccessTrack;
    }

    public void setProccessTrack(ProccessTrack proccessTrack) {
        this.proccessTrack = proccessTrack;
    }

    public BasicDetails getBasicDetails() {
        return basicDetails;
    }

    public void setBasicDetails(BasicDetails basicDetails) {
        this.basicDetails = basicDetails;
    }

    public List<Object> getBasicDetailRate() {
        return basicDetailRate;
    }

    public void setBasicDetailRate(List<Object> basicDetailRate) {
        this.basicDetailRate = basicDetailRate;
    }

    public DocUpload getDocUpload() {
        return docUpload;
    }

    public void setDocUpload(DocUpload docUpload) {
        this.docUpload = docUpload;
    }

    public List<Object> getDeliveryBoysDetail() {
        return deliveryBoysDetail;
    }

    public void setDeliveryBoysDetail(List<Object> deliveryBoysDetail) {
        this.deliveryBoysDetail = deliveryBoysDetail;
    }

    public Integer getDeliveryBoysDetailCount() {
        return deliveryBoysDetailCount;
    }

    public void setDeliveryBoysDetailCount(Integer deliveryBoysDetailCount) {
        this.deliveryBoysDetailCount = deliveryBoysDetailCount;
    }

    public List<Object> getShopActUpload() {
        return shopActUpload;
    }

    public void setShopActUpload(List<Object> shopActUpload) {
        this.shopActUpload = shopActUpload;
    }

    public List<Object> getEsignDocument() {
        return esignDocument;
    }

    public void setEsignDocument(List<Object> esignDocument) {
        this.esignDocument = esignDocument;
    }

    public TrackDetail getTrackDetail() {
        return trackDetail;
    }

    public void setTrackDetail(TrackDetail trackDetail) {
        this.trackDetail = trackDetail;
    }

}
