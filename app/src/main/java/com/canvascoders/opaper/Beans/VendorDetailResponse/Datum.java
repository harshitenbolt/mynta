
package com.canvascoders.opaper.Beans.VendorDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lms_id")
    @Expose
    private String lmsId;
    @SerializedName("lms_id1")
    @Expose
    private Object lmsId1;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("supervisors_ids")
    @Expose
    private String supervisorsIds;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("kyc_type")
    @Expose
    private String kycType;
    @SerializedName("aadhaar_name")
    @Expose
    private Object aadhaarName;
    @SerializedName("aadhaar_dob")
    @Expose
    private Object aadhaarDob;
    @SerializedName("aadhaar_no")
    @Expose
    private Object aadhaarNo;
    @SerializedName("aadhaar_pincode")
    @Expose
    private Object aadhaarPincode;
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
    @SerializedName("dl_name")
    @Expose
    private Object dlName;
    @SerializedName("dl_father_name")
    @Expose
    private Object dlFatherName;
    @SerializedName("dl_dob")
    @Expose
    private Object dlDob;
    @SerializedName("dl_number")
    @Expose
    private Object dlNumber;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("nsdl_pan_name")
    @Expose
    private Object nsdlPanName;
    @SerializedName("user_entered_pan_name")
    @Expose
    private Object userEnteredPanName;
    @SerializedName("nsdl")
    @Expose
    private Object nsdl;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("oracle_csv")
    @Expose
    private String oracleCsv;
    @SerializedName("download_date")
    @Expose
    private String downloadDate;
    @SerializedName("franking_status")
    @Expose
    private String frankingStatus;
    @SerializedName("is_franking_download")
    @Expose
    private String isFrankingDownload;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("is_mass_upload")
    @Expose
    private String isMassUpload;
    @SerializedName("is_mass_doc_upload")
    @Expose
    private String isMassDocUpload;
    @SerializedName("is_upload")
    @Expose
    private String isUpload;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("without_aadhaar_verified")
    @Expose
    private String withoutAadhaarVerified;
    @SerializedName("bank_detail_updation_require")
    @Expose
    private String bankDetailUpdationRequire;
    @SerializedName("is_addendum_in_progress")
    @Expose
    private String isAddendumInProgress;
    @SerializedName("no_of_time_addendum_sign")
    @Expose
    private Integer noOfTimeAddendumSign;
    @SerializedName("no_of_time_annexure_sign")
    @Expose
    private Integer noOfTimeAnnexureSign;
    @SerializedName("status_change_time")
    @Expose
    private Object statusChangeTime;
    @SerializedName("is_agreement_updation_require")
    @Expose
    private String isAgreementUpdationRequire;
    @SerializedName("onboarding_start_date")
    @Expose
    private String onboardingStartDate;
    @SerializedName("onboarded_date")
    @Expose
    private String onboardedDate;
    @SerializedName("agreement_end_date")
    @Expose
    private String agreementEndDate;
    @SerializedName("is_vendor_type_company")
    @Expose
    private Integer isVendorTypeCompany;
    @SerializedName("assessment_tried")
    @Expose
    private Integer assessmentTried;
    @SerializedName("assessment_question")
    @Expose
    private Integer assessmentQuestion;
    @SerializedName("assessment_result")
    @Expose
    private Integer assessmentResult;
    @SerializedName("better_place_reference_id")
    @Expose
    private String betterPlaceReferenceId;
    @SerializedName("better_place_client_reference_id")
    @Expose
    private String betterPlaceClientReferenceId;
    @SerializedName("bgv_run")
    @Expose
    private String bgvRun;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("residential_address")
    @Expose
    private String residentialAddress;
    @SerializedName("residential_address1")
    @Expose
    private String residentialAddress1;
    @SerializedName("residential_address_landmark")
    @Expose
    private String residentialAddressLandmark;
    @SerializedName("residential_address_picode")
    @Expose
    private String residentialAddressPicode;
    @SerializedName("residential_address_city")
    @Expose
    private String residentialAddressCity;
    @SerializedName("residential_address_state")
    @Expose
    private String residentialAddressState;
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
    @SerializedName("permanent_address_city")
    @Expose
    private String permanentAddressCity;
    @SerializedName("permanent_address_state")
    @Expose
    private String permanentAddressState;
    @SerializedName("is_permanent_and_current_address")
    @Expose
    private String isPermanentAndCurrentAddress;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("store_address")
    @Expose
    private String storeAddress;
    @SerializedName("store_address1")
    @Expose
    private String storeAddress1;
    @SerializedName("store_address_landmark")
    @Expose
    private String storeAddressLandmark;
    @SerializedName("vendor_type")
    @Expose
    private String vendorType;
    @SerializedName("vendor_type_detail")
    @Expose
    private String vendorTypeDetail;
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
    @SerializedName("license_no")
    @Expose
    private String licenseNo;
    @SerializedName("if_gst")
    @Expose
    private String ifGst;
    @SerializedName("gstn")
    @Expose
    private Object gstn;
    @SerializedName("rate")
    @Expose
    private Object rate;
    @SerializedName("rate_approve_date")
    @Expose
    private String rateApproveDate;
    @SerializedName("approved_by")
    @Expose
    private Integer approvedBy;
    @SerializedName("is_rate_approved")
    @Expose
    private String isRateApproved;
    @SerializedName("if_shop_act")
    @Expose
    private Object ifShopAct;
    @SerializedName("franking")
    @Expose
    private Object franking;
    @SerializedName("zone")
    @Expose
    private Object zone;
    @SerializedName("store_type")
    @Expose
    private String storeType;
    @SerializedName("oracle_vendor_name")
    @Expose
    private Object oracleVendorName;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("expiration_date")
    @Expose
    private String expirationDate;
    @SerializedName("allow_edit")
    @Expose
    private String allowEdit;
    @SerializedName("is_add_delivery_boy")
    @Expose
    private String isAddDeliveryBoy;
    @SerializedName("expiration_date_format")
    @Expose
    private String expirationDateFormat;
    @SerializedName("is_update_gst")
    @Expose
    private String isUpdateGst;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLmsId() {
        return lmsId;
    }

    public void setLmsId(String lmsId) {
        this.lmsId = lmsId;
    }

    public Object getLmsId1() {
        return lmsId1;
    }

    public void setLmsId1(Object lmsId1) {
        this.lmsId1 = lmsId1;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getSupervisorsIds() {
        return supervisorsIds;
    }

    public void setSupervisorsIds(String supervisorsIds) {
        this.supervisorsIds = supervisorsIds;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

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

    public String getKycType() {
        return kycType;
    }

    public void setKycType(String kycType) {
        this.kycType = kycType;
    }

    public Object getAadhaarName() {
        return aadhaarName;
    }

    public void setAadhaarName(Object aadhaarName) {
        this.aadhaarName = aadhaarName;
    }

    public Object getAadhaarDob() {
        return aadhaarDob;
    }

    public void setAadhaarDob(Object aadhaarDob) {
        this.aadhaarDob = aadhaarDob;
    }

    public Object getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(Object aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public Object getAadhaarPincode() {
        return aadhaarPincode;
    }

    public void setAadhaarPincode(Object aadhaarPincode) {
        this.aadhaarPincode = aadhaarPincode;
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

    public Object getDlName() {
        return dlName;
    }

    public void setDlName(Object dlName) {
        this.dlName = dlName;
    }

    public Object getDlFatherName() {
        return dlFatherName;
    }

    public void setDlFatherName(Object dlFatherName) {
        this.dlFatherName = dlFatherName;
    }

    public Object getDlDob() {
        return dlDob;
    }

    public void setDlDob(Object dlDob) {
        this.dlDob = dlDob;
    }

    public Object getDlNumber() {
        return dlNumber;
    }

    public void setDlNumber(Object dlNumber) {
        this.dlNumber = dlNumber;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public Object getNsdlPanName() {
        return nsdlPanName;
    }

    public void setNsdlPanName(Object nsdlPanName) {
        this.nsdlPanName = nsdlPanName;
    }

    public Object getUserEnteredPanName() {
        return userEnteredPanName;
    }

    public void setUserEnteredPanName(Object userEnteredPanName) {
        this.userEnteredPanName = userEnteredPanName;
    }

    public Object getNsdl() {
        return nsdl;
    }

    public void setNsdl(Object nsdl) {
        this.nsdl = nsdl;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOracleCsv() {
        return oracleCsv;
    }

    public void setOracleCsv(String oracleCsv) {
        this.oracleCsv = oracleCsv;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getFrankingStatus() {
        return frankingStatus;
    }

    public void setFrankingStatus(String frankingStatus) {
        this.frankingStatus = frankingStatus;
    }

    public String getIsFrankingDownload() {
        return isFrankingDownload;
    }

    public void setIsFrankingDownload(String isFrankingDownload) {
        this.isFrankingDownload = isFrankingDownload;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIsMassUpload() {
        return isMassUpload;
    }

    public void setIsMassUpload(String isMassUpload) {
        this.isMassUpload = isMassUpload;
    }

    public String getIsMassDocUpload() {
        return isMassDocUpload;
    }

    public void setIsMassDocUpload(String isMassDocUpload) {
        this.isMassDocUpload = isMassDocUpload;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
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

    public String getWithoutAadhaarVerified() {
        return withoutAadhaarVerified;
    }

    public void setWithoutAadhaarVerified(String withoutAadhaarVerified) {
        this.withoutAadhaarVerified = withoutAadhaarVerified;
    }

    public String getBankDetailUpdationRequire() {
        return bankDetailUpdationRequire;
    }

    public void setBankDetailUpdationRequire(String bankDetailUpdationRequire) {
        this.bankDetailUpdationRequire = bankDetailUpdationRequire;
    }

    public String getIsAddendumInProgress() {
        return isAddendumInProgress;
    }

    public void setIsAddendumInProgress(String isAddendumInProgress) {
        this.isAddendumInProgress = isAddendumInProgress;
    }

    public Integer getNoOfTimeAddendumSign() {
        return noOfTimeAddendumSign;
    }

    public void setNoOfTimeAddendumSign(Integer noOfTimeAddendumSign) {
        this.noOfTimeAddendumSign = noOfTimeAddendumSign;
    }

    public Integer getNoOfTimeAnnexureSign() {
        return noOfTimeAnnexureSign;
    }

    public void setNoOfTimeAnnexureSign(Integer noOfTimeAnnexureSign) {
        this.noOfTimeAnnexureSign = noOfTimeAnnexureSign;
    }

    public Object getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(Object statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public String getIsAgreementUpdationRequire() {
        return isAgreementUpdationRequire;
    }

    public void setIsAgreementUpdationRequire(String isAgreementUpdationRequire) {
        this.isAgreementUpdationRequire = isAgreementUpdationRequire;
    }

    public String getOnboardingStartDate() {
        return onboardingStartDate;
    }

    public void setOnboardingStartDate(String onboardingStartDate) {
        this.onboardingStartDate = onboardingStartDate;
    }

    public String getOnboardedDate() {
        return onboardedDate;
    }

    public void setOnboardedDate(String onboardedDate) {
        this.onboardedDate = onboardedDate;
    }

    public String getAgreementEndDate() {
        return agreementEndDate;
    }

    public void setAgreementEndDate(String agreementEndDate) {
        this.agreementEndDate = agreementEndDate;
    }

    public Integer getIsVendorTypeCompany() {
        return isVendorTypeCompany;
    }

    public void setIsVendorTypeCompany(Integer isVendorTypeCompany) {
        this.isVendorTypeCompany = isVendorTypeCompany;
    }

    public Integer getAssessmentTried() {
        return assessmentTried;
    }

    public void setAssessmentTried(Integer assessmentTried) {
        this.assessmentTried = assessmentTried;
    }

    public Integer getAssessmentQuestion() {
        return assessmentQuestion;
    }

    public void setAssessmentQuestion(Integer assessmentQuestion) {
        this.assessmentQuestion = assessmentQuestion;
    }

    public Integer getAssessmentResult() {
        return assessmentResult;
    }

    public void setAssessmentResult(Integer assessmentResult) {
        this.assessmentResult = assessmentResult;
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

    public String getBgvRun() {
        return bgvRun;
    }

    public void setBgvRun(String bgvRun) {
        this.bgvRun = bgvRun;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getResidentialAddressPicode() {
        return residentialAddressPicode;
    }

    public void setResidentialAddressPicode(String residentialAddressPicode) {
        this.residentialAddressPicode = residentialAddressPicode;
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

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

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

    public String getIsPermanentAndCurrentAddress() {
        return isPermanentAndCurrentAddress;
    }

    public void setIsPermanentAndCurrentAddress(String isPermanentAndCurrentAddress) {
        this.isPermanentAndCurrentAddress = isPermanentAndCurrentAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

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

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getVendorTypeDetail() {
        return vendorTypeDetail;
    }

    public void setVendorTypeDetail(String vendorTypeDetail) {
        this.vendorTypeDetail = vendorTypeDetail;
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

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getIfGst() {
        return ifGst;
    }

    public void setIfGst(String ifGst) {
        this.ifGst = ifGst;
    }

    public Object getGstn() {
        return gstn;
    }

    public void setGstn(Object gstn) {
        this.gstn = gstn;
    }

    public Object getRate() {
        return rate;
    }

    public void setRate(Object rate) {
        this.rate = rate;
    }

    public String getRateApproveDate() {
        return rateApproveDate;
    }

    public void setRateApproveDate(String rateApproveDate) {
        this.rateApproveDate = rateApproveDate;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getIsRateApproved() {
        return isRateApproved;
    }

    public void setIsRateApproved(String isRateApproved) {
        this.isRateApproved = isRateApproved;
    }

    public Object getIfShopAct() {
        return ifShopAct;
    }

    public void setIfShopAct(Object ifShopAct) {
        this.ifShopAct = ifShopAct;
    }

    public Object getFranking() {
        return franking;
    }

    public void setFranking(Object franking) {
        this.franking = franking;
    }

    public Object getZone() {
        return zone;
    }

    public void setZone(Object zone) {
        this.zone = zone;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public Object getOracleVendorName() {
        return oracleVendorName;
    }

    public void setOracleVendorName(Object oracleVendorName) {
        this.oracleVendorName = oracleVendorName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(String allowEdit) {
        this.allowEdit = allowEdit;
    }

    public String getIsAddDeliveryBoy() {
        return isAddDeliveryBoy;
    }

    public void setIsAddDeliveryBoy(String isAddDeliveryBoy) {
        this.isAddDeliveryBoy = isAddDeliveryBoy;
    }

    public String getExpirationDateFormat() {
        return expirationDateFormat;
    }

    public void setExpirationDateFormat(String expirationDateFormat) {
        this.expirationDateFormat = expirationDateFormat;
    }

    public String getIsUpdateGst() {
        return isUpdateGst;
    }

    public void setIsUpdateGst(String isUpdateGst) {
        this.isUpdateGst = isUpdateGst;
    }

}
