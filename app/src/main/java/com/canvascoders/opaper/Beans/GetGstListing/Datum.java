
package com.canvascoders.opaper.Beans.GetGstListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gstn")
    @Expose
    private Integer gstn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("gst_certificate_image")
    @Expose
    private String gstCertificateImage;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("address")
    @Expose
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGstn() {
        return gstn;
    }

    public void setGstn(Integer gstn) {
        this.gstn = gstn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGstCertificateImage() {
        return gstCertificateImage;
    }

    public void setGstCertificateImage(String gstCertificateImage) {
        this.gstCertificateImage = gstCertificateImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
