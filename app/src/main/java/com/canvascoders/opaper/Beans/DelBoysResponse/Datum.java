
package com.canvascoders.opaper.Beans.DelBoysResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

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
    @SerializedName("permanent_address")
    @Expose
    private String permanentAddress;
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

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    @SerializedName("store_type")
    @Expose
    private String store_type;

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

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
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

}
