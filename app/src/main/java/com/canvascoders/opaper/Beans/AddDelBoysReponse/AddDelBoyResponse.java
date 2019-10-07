
package com.canvascoders.opaper.Beans.AddDelBoysReponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class AddDelBoyResponse {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @SerializedName("validation")
    @Expose
    private Validation validation;


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    @SerializedName("response")
    @Expose
    private String response;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }



    public class Validation {

        public String getCurrentaddress1() {
            return currentaddress1;
        }

        public void setCurrentaddress1(String currentaddress1) {
            this.currentaddress1 = currentaddress1;
        }

        public String getCurrentaddressLandmark() {
            return currentaddressLandmark;
        }

        public void setCurrentaddressLandmark(String currentaddressLandmark) {
            this.currentaddressLandmark = currentaddressLandmark;
        }

        public String getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(String currentAddress) {
            this.currentAddress = currentAddress;
        }

        public String getCurrentAddressPincode() {
            return currentAddressPincode;
        }

        public void setCurrentAddressPincode(String currentAddressPincode) {
            this.currentAddressPincode = currentAddressPincode;
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

        public String getPermanentResidentialAddressPincode() {
            return permanentResidentialAddressPincode;
        }

        public void setPermanentResidentialAddressPincode(String permanentResidentialAddressPincode) {
            this.permanentResidentialAddressPincode = permanentResidentialAddressPincode;
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

        @SerializedName("proccess_id")
        @Expose
        private String proccessId;
        @SerializedName("agent_id")
        @Expose
        private String agentId;
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("father_name")
        @Expose
        private String father_name;

        @SerializedName("current_residential_address")
        @Expose
        private String current_residential_address;


        @SerializedName("current_address1")
        @Expose
        private String currentaddress1;

        @SerializedName("current_address_landmark")
        @Expose
        private String currentaddressLandmark;

        @SerializedName("current_address")
        @Expose
        private String currentAddress;

        @SerializedName("current_address_picode")
        @Expose
        private String currentAddressPincode;

        @SerializedName("current_address_city")
        @Expose
        private String currentAddressCity;

        @SerializedName("current_address_state")
        @Expose
        private String currentAddressState;

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
        private String permanentResidentialAddressPincode;

        @SerializedName("permanent_residential_address_city")
        @Expose
        private String permanentResidentialAddressCity;

        @SerializedName("permanent_residential_address_state")
        @Expose
        private String permanentResidentialAddressState;




        @SerializedName("permanent_address")
        @Expose
        private String permanent_address;

        @SerializedName("dc")
        @Expose
        private String dc;

        public String getFather_name() {
            return father_name;
        }

        public void setFather_name(String father_name) {
            this.father_name = father_name;
        }

        public String getCurrent_residential_address() {
            return current_residential_address;
        }

        public void setCurrent_residential_address(String current_residential_address) {
            this.current_residential_address = current_residential_address;
        }

        public String getPermanent_address() {
            return permanent_address;
        }

        public void setPermanent_address(String permanent_address) {
            this.permanent_address = permanent_address;
        }

        public String getDc() {
            return dc;
        }

        public void setDc(String dc) {
            this.dc = dc;
        }

        public String getDriving_licence_num() {
            return driving_licence_num;
        }

        public void setDriving_licence_num(String driving_licence_num) {
            this.driving_licence_num = driving_licence_num;
        }

        public String getDriving_licence_dob() {
            return driving_licence_dob;
        }

        public void setDriving_licence_dob(String driving_licence_dob) {
            this.driving_licence_dob = driving_licence_dob;
        }

        public String getDriving_licence_image() {
            return driving_licence_image;
        }

        public void setDriving_licence_image(String driving_licence_image) {
            this.driving_licence_image = driving_licence_image;
        }

        public String getVehicle_for_delivery() {
            return vehicle_for_delivery;
        }

        public void setVehicle_for_delivery(String vehicle_for_delivery) {
            this.vehicle_for_delivery = vehicle_for_delivery;
        }

        public String getLanguages() {
            return languages;
        }

        public void setLanguages(String languages) {
            this.languages = languages;
        }

        @SerializedName("driving_licence_num")
        @Expose
        private String driving_licence_num;

        @SerializedName("driving_licence_dob")
        @Expose
        private String driving_licence_dob;

        @SerializedName("driving_licence_image")
        @Expose
        private String driving_licence_image;

        @SerializedName("vehicle_for_delivery")
        @Expose
        private String vehicle_for_delivery;

        @SerializedName("languages")
        @Expose
        private String languages;

        @SerializedName("phone_number")
        @Expose
        private String phoneNumber;
        @SerializedName("driving_licence")
        @Expose
        private String drivingLicence;
        @SerializedName("image")
        @Expose
        private String image;

        public String getRoute_number() {
            return route_number;
        }

        public void setRoute_number(String route_number) {
            this.route_number = route_number;
        }

        @SerializedName("route_number")
        @Expose
        private String route_number;


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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getDrivingLicence() {
            return drivingLicence;
        }

        public void setDrivingLicence(String drivingLicence) {
            this.drivingLicence = drivingLicence;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

}
