
package com.canvascoders.opaper.Beans.ErrorResponsePan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Validation {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("pan_card_front")
    @Expose
    private String panCardFront;

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanCardFront() {
        return panCardFront;
    }

    public void setPanCardFront(String panCardFront) {
        this.panCardFront = panCardFront;
    }



    // error response for cheque upload
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("bank_ac")
    @Expose
    private String bankAc;
    @SerializedName("ac_name")
    @Expose
    private String acName;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("cancelled_cheque")
    @Expose
    private String cancelledCheque;

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBankAc() {
        return bankAc;
    }

    public void setBankAc(String bankAc) {
        this.bankAc = bankAc;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCancelledCheque() {
        return cancelledCheque;
    }

    public void setCancelledCheque(String cancelledCheque) {
        this.cancelledCheque = cancelledCheque;
    }

    // error response for vendor details validation...


    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("residential_address")
    @Expose
    private String residentialAddress;

    public String getResidentialAddress1() {
        return residentialAddress1;
    }

    public void setResidentialAddress1(String residentialAddress1) {
        this.residentialAddress1 = residentialAddress1;
    }

    public String getResidentialAddressLandmark() {
        return residentialAddressLandmark;
    }

    public void setResidentialAddressLandmark(String residentialAddressLandmark) {
        this.residentialAddressLandmark = residentialAddressLandmark;
    }

    public String getResidentialAddresspicode() {
        return residentialAddresspicode;
    }

    public void setResidentialAddresspicode(String residentialAddresspicode) {
        this.residentialAddresspicode = residentialAddresspicode;
    }

    public String getResidentialAddressCity() {
        return residentialAddressCity;
    }

    public void setResidentialAddressCity(String residentialAddressCity) {
        this.residentialAddressCity = residentialAddressCity;
    }

    public String getResidentialAddressState() {
        return residentialAddressState;
    }

    public void setResidentialAddressState(String residentialAddressState) {
        this.residentialAddressState = residentialAddressState;
    }

    @SerializedName("residential_address1")
    @Expose
    private String residentialAddress1;
    @SerializedName("residential_address_landmark")
    @Expose
    private String residentialAddressLandmark;
    @SerializedName("residential_address_picode")
    @Expose
    private String residentialAddresspicode;
    @SerializedName("residential_address_city")
    @Expose
    private String residentialAddressCity;

    @SerializedName("residential_address_state")
    @Expose
    private String residentialAddressState;






    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    @SerializedName("permanent_address")
    @Expose
    private String permanentAddress;
    @SerializedName("permanent_address1")
    @Expose
    private String permanentAddress1;
    @SerializedName("permanent_address_landmark")
    @Expose
    private String permanentAddressLandmark;
    @SerializedName("permanent_address_picode")
    @Expose
    private String permanentAddressPicode;

    public String getPermanentAddress1() {
        return permanentAddress1;
    }

    public void setPermanentAddress1(String permanentAddress1) {
        this.permanentAddress1 = permanentAddress1;
    }

    public String getPermanentAddressLandmark() {
        return permanentAddressLandmark;
    }

    public void setPermanentAddressLandmark(String permanentAddressLandmark) {
        this.permanentAddressLandmark = permanentAddressLandmark;
    }

    public String getPermanentAddressPicode() {
        return permanentAddressPicode;
    }

    public void setPermanentAddressPicode(String permanentAddressPicode) {
        this.permanentAddressPicode = permanentAddressPicode;
    }

    public String getPermanentAddressCity() {
        return permanentAddressCity;
    }

    public void setPermanentAddressCity(String permanentAddressCity) {
        this.permanentAddressCity = permanentAddressCity;
    }

    public String getPermanentAddressState() {
        return permanentAddressState;
    }

    public void setPermanentAddressState(String permanentAddressState) {
        this.permanentAddressState = permanentAddressState;
    }

    @SerializedName("permanent_address_city")
    @Expose
    private String permanentAddressCity;
    @SerializedName("permanent_address_state")
    @Expose
    private String permanentAddressState;
    @SerializedName("vendor_type")
    @Expose
    private String vendorType;
    @SerializedName("vendor_type_detail")
    @Expose
    private String vendorTypeDetail;

    public String getVendorTypeDetail() {
        return vendorTypeDetail;
    }

    public void setVendorTypeDetail(String vendorTypeDetail) {
        this.vendorTypeDetail = vendorTypeDetail;
    }

    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("approach")
    @Expose
    private String approach;
    @SerializedName("languages")
    @Expose
    private String languages;
    @SerializedName("shipment_transfer")
    @Expose
    private String shipmentTransfer;
    @SerializedName("partner_with_other_ecommerce")
    @Expose
    private String partnerWithOtherEcommerce;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("if_gst")
    @Expose
    private String ifGst;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("store_address")
    @Expose
    private String storeAddress;

    @SerializedName("store_address1")
    @Expose
    private String storeAddress1;
    @SerializedName("store_address_landmark")
    @Expose
    private String storeAddressLandmark;


    public String getStoreAddress1() {
        return storeAddress1;
    }

    public void setStoreAddress1(String storeAddress1) {
        this.storeAddress1 = storeAddress1;
    }

    public String getStoreAddressLandmark() {
        return storeAddressLandmark;
    }

    public void setStoreAddressLandmark(String storeAddressLandmark) {
        this.storeAddressLandmark = storeAddressLandmark;
    }

    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("store_type_config")
    @Expose
    private String StoreTypeConfig;

    public String getStoreTypeConfig() {
        return StoreTypeConfig;
    }

    public void setStoreTypeConfig(String storeTypeConfig) {
        StoreTypeConfig = storeTypeConfig;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getShipmentTransfer() {
        return shipmentTransfer;
    }

    public void setShipmentTransfer(String shipmentTransfer) {
        this.shipmentTransfer = shipmentTransfer;
    }

    public String getPartnerWithOtherEcommerce() {
        return partnerWithOtherEcommerce;
    }

    public void setPartnerWithOtherEcommerce(String partnerWithOtherEcommerce) {
        this.partnerWithOtherEcommerce = partnerWithOtherEcommerce;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getIfGst() {
        return ifGst;
    }

    public void setIfGst(String ifGst) {
        this.ifGst = ifGst;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }




    @SerializedName("adhar_card_front")
    @Expose
    private String adharCardFront;
    @SerializedName("adhar_card_back")
    @Expose
    private String adharCardBack;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("yob")
    @Expose
    private String yob;



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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }



    // validation Driving id

    @SerializedName("dl_card_front")
    @Expose
    private String dlCardFront;
    @SerializedName("dl_card_back")
    @Expose
    private String dlCardBack;
    @SerializedName("dl_name")
    @Expose
    private String dlName;
    @SerializedName("dl_father_name")
    @Expose
    private String dlFatherName;
    @SerializedName("dl_dob")
    @Expose
    private String dlDob;
    @SerializedName("dl_number")
    @Expose
    private String dlNumber;

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

    public String getDlName() {
        return dlName;
    }

    public void setDlName(String dlName) {
        this.dlName = dlName;
    }

    public String getDlFatherName() {
        return dlFatherName;
    }

    public void setDlFatherName(String dlFatherName) {
        this.dlFatherName = dlFatherName;
    }

    public String getDlDob() {
        return dlDob;
    }

    public void setDlDob(String dlDob) {
        this.dlDob = dlDob;
    }

    public String getDlNumber() {
        return dlNumber;
    }

    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }



    // voter Id

    @SerializedName("voter_card_front")
    @Expose
    private String voterCardFront;
    @SerializedName("voter_card_back")
    @Expose
    private String voterCardBack;
    @SerializedName("voter_name")
    @Expose
    private String voterName;
    @SerializedName("voter_father_name")
    @Expose
    private String voterFatherName;
    @SerializedName("voter_dob")
    @Expose
    private String voterDob;
    @SerializedName("voter_id_num")
    @Expose

    private String voterIdNum;

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

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterFatherName() {
        return voterFatherName;
    }

    public void setVoterFatherName(String voterFatherName) {
        this.voterFatherName = voterFatherName;
    }

    public String getVoterDob() {
        return voterDob;
    }

    public void setVoterDob(String voterDob) {
        this.voterDob = voterDob;
    }

    public String getVoterIdNum() {
        return voterIdNum;
    }

    public void setVoterIdNum(String voterIdNum) {
        this.voterIdNum = voterIdNum;
    }


    //location base validation
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @SerializedName("latitude")
    @Expose
    private String latitude;


    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    @SerializedName("gstn")
    @Expose
    private String gstn;


    public String getGstCertificateImage() {
        return gstCertificateImage;
    }

    public void setGstCertificateImage(String gstCertificateImage) {
        this.gstCertificateImage = gstCertificateImage;
    }

    @SerializedName("gst_certificate_image")
    @Expose
    private String gstCertificateImage;




}
