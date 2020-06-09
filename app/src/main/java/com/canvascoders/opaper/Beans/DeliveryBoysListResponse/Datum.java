
package com.canvascoders.opaper.Beans.DeliveryBoysListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("current_residential_address")
    @Expose
    private String currentResidentialAddress;
    @SerializedName("current_address")
    @Expose
    private String currentAddress;
    @SerializedName("current_address1")
    @Expose
    private String currentAddress1;
    @SerializedName("current_address_landmark")
    @Expose
    private String currentAddressLandmark;
    @SerializedName("current_address_picode")
    @Expose
    private String currentAddressPicode;
    @SerializedName("current_address_city")
    @Expose
    private String currentAddressCity;
    @SerializedName("current_address_state")
    @Expose
    private String currentAddressState;
    @SerializedName("permanent_address")
    @Expose
    private String permanentAddress;
    @SerializedName("permanent_residential_address")
    @Expose
    private String permanentResidentialAddress;
    @SerializedName("permanent_residential_address1")
    @Expose
    private String permanentResidentialAddress1;
    @SerializedName("permanent_residential_address_landmark")
    @Expose
    private String permanentResidentialAddressLandmark;
    @SerializedName("permanent_residential_address_picode")
    @Expose
    private String permanentResidentialAddressPicode;
    @SerializedName("permanent_residential_address_city")
    @Expose
    private String permanentResidentialAddressCity;
    @SerializedName("permanent_residential_address_state")
    @Expose
    private String permanentResidentialAddressState;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("boy_status")
    @Expose
    private String boystatus;

    public String getBoystatus() {
        return boystatus;
    }

    public void setBoystatus(String boystatus) {
        this.boystatus = boystatus;
    }

    @SerializedName("route_number")
    @Expose
    private String routeNumber;
    @SerializedName("driving_licence_num")
    @Expose
    private String drivingLicenceNum;
    @SerializedName("driving_licence_dob")
    @Expose
    private String drivingLicenceDob;
    @SerializedName("driving_licence_image")
    @Expose
    private String drivingLicenceImage;
    @SerializedName("vehicle_for_delivery")
    @Expose
    private String vehicleForDelivery;


    @SerializedName("driving_licence_till_date")
    @Expose
    private String driving_licence_till_date;

    public String getDriving_licence_issue_date() {
        return driving_licence_issue_date;
    }

    public void setDriving_licence_issue_date(String driving_licence_issue_date) {
        this.driving_licence_issue_date = driving_licence_issue_date;
    }

    @SerializedName("driving_licence_issue_date")
    @Expose
    private String driving_licence_issue_date;

    public String getDriving_licence_till_date() {
        return driving_licence_till_date;
    }

    public void setDriving_licence_till_date(String driving_licence_till_date) {
        this.driving_licence_till_date = driving_licence_till_date;
    }

    @SerializedName("languages")
    @Expose
    private String languages;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("better_place_reference_id")
    @Expose
    private String betterPlaceReferenceId;
    @SerializedName("better_place_client_reference_id")
    @Expose
    private String betterPlaceClientReferenceId;

    public String getKyc_type() {
        return kyc_type;
    }

    public void setKyc_type(String kyc_type) {
        this.kyc_type = kyc_type;
    }

    @SerializedName("kyc_type")
    @Expose
    private String kyc_type;

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

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentResidentialAddress() {
        return currentResidentialAddress;
    }

    public void setCurrentResidentialAddress(String currentResidentialAddress) {
        this.currentResidentialAddress = currentResidentialAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentAddress1() {
        return currentAddress1;
    }

    public void setCurrentAddress1(String currentAddress1) {
        this.currentAddress1 = currentAddress1;
    }

    public String getCurrentAddressLandmark() {
        return currentAddressLandmark;
    }

    public void setCurrentAddressLandmark(String currentAddressLandmark) {
        this.currentAddressLandmark = currentAddressLandmark;
    }

    public String getCurrentAddressPicode() {
        return currentAddressPicode;
    }

    public void setCurrentAddressPicode(String currentAddressPicode) {
        this.currentAddressPicode = currentAddressPicode;
    }

    public String getCurrentAddressCity() {
        return currentAddressCity;
    }

    public void setCurrentAddressCity(String currentAddressCity) {
        this.currentAddressCity = currentAddressCity;
    }

    public String getCurrentAddressState() {
        return currentAddressState;
    }

    public void setCurrentAddressState(String currentAddressState) {
        this.currentAddressState = currentAddressState;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentResidentialAddress() {
        return permanentResidentialAddress;
    }

    public void setPermanentResidentialAddress(String permanentResidentialAddress) {
        this.permanentResidentialAddress = permanentResidentialAddress;
    }

    public String getPermanentResidentialAddress1() {
        return permanentResidentialAddress1;
    }

    public void setPermanentResidentialAddress1(String permanentResidentialAddress1) {
        this.permanentResidentialAddress1 = permanentResidentialAddress1;
    }

    public String getPermanentResidentialAddressLandmark() {
        return permanentResidentialAddressLandmark;
    }

    public void setPermanentResidentialAddressLandmark(String permanentResidentialAddressLandmark) {
        this.permanentResidentialAddressLandmark = permanentResidentialAddressLandmark;
    }

    public String getPermanentResidentialAddressPicode() {
        return permanentResidentialAddressPicode;
    }

    public void setPermanentResidentialAddressPicode(String permanentResidentialAddressPicode) {
        this.permanentResidentialAddressPicode = permanentResidentialAddressPicode;
    }

    public String getPermanentResidentialAddressCity() {
        return permanentResidentialAddressCity;
    }

    public void setPermanentResidentialAddressCity(String permanentResidentialAddressCity) {
        this.permanentResidentialAddressCity = permanentResidentialAddressCity;
    }

    public String getPermanentResidentialAddressState() {
        return permanentResidentialAddressState;
    }

    public void setPermanentResidentialAddressState(String permanentResidentialAddressState) {
        this.permanentResidentialAddressState = permanentResidentialAddressState;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getDrivingLicenceNum() {
        return drivingLicenceNum;
    }

    public void setDrivingLicenceNum(String drivingLicenceNum) {
        this.drivingLicenceNum = drivingLicenceNum;
    }

    public String getDrivingLicenceDob() {
        return drivingLicenceDob;
    }

    public void setDrivingLicenceDob(String drivingLicenceDob) {
        this.drivingLicenceDob = drivingLicenceDob;
    }

    public String getDrivingLicenceImage() {
        return drivingLicenceImage;
    }

    public void setDrivingLicenceImage(String drivingLicenceImage) {
        this.drivingLicenceImage = drivingLicenceImage;
    }

    public String getVehicleForDelivery() {
        return vehicleForDelivery;
    }

    public void setVehicleForDelivery(String vehicleForDelivery) {
        this.vehicleForDelivery = vehicleForDelivery;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public String getBetterPlaceReferenceId() {
        return betterPlaceReferenceId;
    }

    public void setBetterPlaceReferenceId(String betterPlaceReferenceId) {
        this.betterPlaceReferenceId = betterPlaceReferenceId;
    }

    public String getBetterPlaceClientReferenceId() {
        return betterPlaceClientReferenceId;
    }

    public void setBetterPlaceClientReferenceId(String betterPlaceClientReferenceId) {
        this.betterPlaceClientReferenceId = betterPlaceClientReferenceId;
    }




    @SerializedName("voter_id")
    @Expose
    private String voterIdNum;

    @SerializedName("voter_name")
    @Expose
    private String voter_name;

    public String getVoterIdNum() {
        return voterIdNum;
    }

    public void setVoterIdNum(String voterIdNum) {
        this.voterIdNum = voterIdNum;
    }

    public String getVoter_name() {
        return voter_name;
    }

    public void setVoter_name(String voter_name) {
        this.voter_name = voter_name;
    }

    public String getVoter_dob() {
        return voter_dob;
    }

    public void setVoter_dob(String voter_dob) {
        this.voter_dob = voter_dob;
    }

    public String getVoter_front_image() {
        return voter_front_image;
    }

    public void setVoter_front_image(String voter_front_image) {
        this.voter_front_image = voter_front_image;
    }

    public String getVoter_back_image() {
        return voter_back_image;
    }

    public void setVoter_back_image(String voter_back_image) {
        this.voter_back_image = voter_back_image;
    }

    public String getAadhaar_no() {
        return aadhaar_no;
    }

    public void setAadhaar_no(String aadhaar_no) {
        this.aadhaar_no = aadhaar_no;
    }

    public String getAadhaar_name() {
        return aadhaar_name;
    }

    public void setAadhaar_name(String aadhaar_name) {
        this.aadhaar_name = aadhaar_name;
    }

    public String getAadhaar_dob() {
        return aadhaar_dob;
    }

    public void setAadhaar_dob(String aadhaar_dob) {
        this.aadhaar_dob = aadhaar_dob;
    }

    public String getAadhaar_card_front() {
        return aadhaar_card_front;
    }

    public void setAadhaar_card_front(String aadhaar_card_front) {
        this.aadhaar_card_front = aadhaar_card_front;
    }

    public String getAadhaar_card_back() {
        return aadhaar_card_back;
    }

    public void setAadhaar_card_back(String aadhaar_card_back) {
        this.aadhaar_card_back = aadhaar_card_back;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    @SerializedName("voter_dob")
    @Expose
    private String voter_dob;

    @SerializedName("voter_id_front_image")
    @Expose
    private String voter_front_image;

    @SerializedName("voter_id_back_image")
    @Expose
    private String voter_back_image;


    @SerializedName("aadhar_no")
    @Expose
    private String aadhaar_no;

    @SerializedName("aadhaar_name")
    @Expose
    private String aadhaar_name;

    @SerializedName("aadhaar_dob")
    @Expose
    private String aadhaar_dob;

    @SerializedName("aadhar_front_image")
    @Expose
    private String aadhaar_card_front;

    @SerializedName("aadhar_back_image")
    @Expose
    private String aadhaar_card_back;

    @SerializedName("blood_group")
    @Expose
    private String blood_group;


    public String getDexter() {
        return dexter;
    }

    public void setDexter(String dexter) {
        this.dexter = dexter;
    }

    @SerializedName("dexter")
    @Expose
    private String dexter;

}
