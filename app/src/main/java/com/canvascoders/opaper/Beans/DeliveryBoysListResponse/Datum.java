
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
}
