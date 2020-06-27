
package com.canvascoders.opaper.Beans.GetOldKYCResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lms_id")
    @Expose
    private String lmsId;
    @SerializedName("lms_id2")
    @Expose
    private String lmsId2;
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
    private String aadhaarName;
    @SerializedName("aadhaar_dob")
    @Expose
    private String aadhaarDob;
    @SerializedName("aadhaar_no")
    @Expose
    private String aadhaarNo;
    @SerializedName("aadhaar_pincode")
    @Expose
    private String aadhaarPincode;
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
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("nsdl_pan_name")
    @Expose
    private String nsdlPanName;
    @SerializedName("user_entered_pan_name")
    @Expose
    private String userEnteredPanName;
    @SerializedName("nsdl")
    @Expose
    private String nsdl;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("is_blacklisted")
    @Expose
    private Integer isBlacklisted;
    @SerializedName("blacklisted_date")
    @Expose
    private String blacklistedDate;
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
    @SerializedName("ecom_franking_status")
    @Expose
    private String ecomFrankingStatus;
    @SerializedName("is_ecom_franking_download")
    @Expose
    private String isEcomFrankingDownload;
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
    @SerializedName("ecom_no_of_time_annexure_sign")
    @Expose
    private Integer ecomNoOfTimeAnnexureSign;
    @SerializedName("status_change_time")
    @Expose
    private String statusChangeTime;
    @SerializedName("is_agreement_updation_require")
    @Expose
    private String isAgreementUpdationRequire;
    @SerializedName("onboarding_start_date")
    @Expose
    private String onboardingStartDate;
    @SerializedName("onboarded_date")
    @Expose
    private String onboardedDate;
    @SerializedName("ecom_onboarded_date")
    @Expose
    private String ecomOnboardedDate;
    @SerializedName("agreement_end_date")
    @Expose
    private String agreementEndDate;
    @SerializedName("ecom_agreement_end_date")
    @Expose
    private String ecomAgreementEndDate;
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
    @SerializedName("better_place_created_date")
    @Expose
    private String betterPlaceCreatedDate;
    @SerializedName("addressVerified")
    @Expose
    private String addressVerified;
    @SerializedName("addressVerified_at")
    @Expose
    private String addressVerifiedAt;
    @SerializedName("currentAddressVerified")
    @Expose
    private String currentAddressVerified;
    @SerializedName("currentAddressVerified_at")
    @Expose
    private String currentAddressVerifiedAt;
    @SerializedName("permanentAddressVerified")
    @Expose
    private String permanentAddressVerified;
    @SerializedName("permanentAddressVerified_at")
    @Expose
    private String permanentAddressVerifiedAt;
    @SerializedName("criminalVerified")
    @Expose
    private String criminalVerified;
    @SerializedName("criminalVerified_at")
    @Expose
    private String criminalVerifiedAt;
    @SerializedName("bgv_report_update_date")
    @Expose
    private String bgvReportUpdateDate;
    @SerializedName("bgv_run")
    @Expose
    private String bgvRun;
    @SerializedName("bgv_send_date")
    @Expose
    private String bgvSendDate;
    @SerializedName("is_new_agreement_signed")
    @Expose
    private String isNewAgreementSigned;
    @SerializedName("pan_matched_kiran_proccess_id")
    @Expose
    private Integer panMatchedKiranProccessId;
    @SerializedName("report_pancard")
    @Expose
    private String reportPancard;
    @SerializedName("report_pancard_date")
    @Expose
    private String reportPancardDate;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
    @SerializedName("franking_download_date")
    @Expose
    private String frankingDownloadDate;
    @SerializedName("better_place_duplication")
    @Expose
    private Integer betterPlaceDuplication;
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

    public String getLmsId2() {
        return lmsId2;
    }

    public void setLmsId2(String lmsId2) {
        this.lmsId2 = lmsId2;
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

    public String getAadhaarName() {
        return aadhaarName;
    }

    public void setAadhaarName(String aadhaarName) {
        this.aadhaarName = aadhaarName;
    }

    public String getAadhaarDob() {
        return aadhaarDob;
    }

    public void setAadhaarDob(String aadhaarDob) {
        this.aadhaarDob = aadhaarDob;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getAadhaarPincode() {
        return aadhaarPincode;
    }

    public void setAadhaarPincode(String aadhaarPincode) {
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

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getNsdlPanName() {
        return nsdlPanName;
    }

    public void setNsdlPanName(String nsdlPanName) {
        this.nsdlPanName = nsdlPanName;
    }

    public String getUserEnteredPanName() {
        return userEnteredPanName;
    }

    public void setUserEnteredPanName(String userEnteredPanName) {
        this.userEnteredPanName = userEnteredPanName;
    }

    public String getNsdl() {
        return nsdl;
    }

    public void setNsdl(String nsdl) {
        this.nsdl = nsdl;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Integer getIsBlacklisted() {
        return isBlacklisted;
    }

    public void setIsBlacklisted(Integer isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }

    public String getBlacklistedDate() {
        return blacklistedDate;
    }

    public void setBlacklistedDate(String blacklistedDate) {
        this.blacklistedDate = blacklistedDate;
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

    public String getEcomFrankingStatus() {
        return ecomFrankingStatus;
    }

    public void setEcomFrankingStatus(String ecomFrankingStatus) {
        this.ecomFrankingStatus = ecomFrankingStatus;
    }

    public String getIsEcomFrankingDownload() {
        return isEcomFrankingDownload;
    }

    public void setIsEcomFrankingDownload(String isEcomFrankingDownload) {
        this.isEcomFrankingDownload = isEcomFrankingDownload;
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

    public Integer getEcomNoOfTimeAnnexureSign() {
        return ecomNoOfTimeAnnexureSign;
    }

    public void setEcomNoOfTimeAnnexureSign(Integer ecomNoOfTimeAnnexureSign) {
        this.ecomNoOfTimeAnnexureSign = ecomNoOfTimeAnnexureSign;
    }

    public String getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(String statusChangeTime) {
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

    public String getEcomOnboardedDate() {
        return ecomOnboardedDate;
    }

    public void setEcomOnboardedDate(String ecomOnboardedDate) {
        this.ecomOnboardedDate = ecomOnboardedDate;
    }

    public String getAgreementEndDate() {
        return agreementEndDate;
    }

    public void setAgreementEndDate(String agreementEndDate) {
        this.agreementEndDate = agreementEndDate;
    }

    public String getEcomAgreementEndDate() {
        return ecomAgreementEndDate;
    }

    public void setEcomAgreementEndDate(String ecomAgreementEndDate) {
        this.ecomAgreementEndDate = ecomAgreementEndDate;
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

    public String getBetterPlaceCreatedDate() {
        return betterPlaceCreatedDate;
    }

    public void setBetterPlaceCreatedDate(String betterPlaceCreatedDate) {
        this.betterPlaceCreatedDate = betterPlaceCreatedDate;
    }

    public String getAddressVerified() {
        return addressVerified;
    }

    public void setAddressVerified(String addressVerified) {
        this.addressVerified = addressVerified;
    }

    public String getAddressVerifiedAt() {
        return addressVerifiedAt;
    }

    public void setAddressVerifiedAt(String addressVerifiedAt) {
        this.addressVerifiedAt = addressVerifiedAt;
    }

    public String getCurrentAddressVerified() {
        return currentAddressVerified;
    }

    public void setCurrentAddressVerified(String currentAddressVerified) {
        this.currentAddressVerified = currentAddressVerified;
    }

    public String getCurrentAddressVerifiedAt() {
        return currentAddressVerifiedAt;
    }

    public void setCurrentAddressVerifiedAt(String currentAddressVerifiedAt) {
        this.currentAddressVerifiedAt = currentAddressVerifiedAt;
    }

    public String getPermanentAddressVerified() {
        return permanentAddressVerified;
    }

    public void setPermanentAddressVerified(String permanentAddressVerified) {
        this.permanentAddressVerified = permanentAddressVerified;
    }

    public String getPermanentAddressVerifiedAt() {
        return permanentAddressVerifiedAt;
    }

    public void setPermanentAddressVerifiedAt(String permanentAddressVerifiedAt) {
        this.permanentAddressVerifiedAt = permanentAddressVerifiedAt;
    }

    public String getCriminalVerified() {
        return criminalVerified;
    }

    public void setCriminalVerified(String criminalVerified) {
        this.criminalVerified = criminalVerified;
    }

    public String getCriminalVerifiedAt() {
        return criminalVerifiedAt;
    }

    public void setCriminalVerifiedAt(String criminalVerifiedAt) {
        this.criminalVerifiedAt = criminalVerifiedAt;
    }

    public String getBgvReportUpdateDate() {
        return bgvReportUpdateDate;
    }

    public void setBgvReportUpdateDate(String bgvReportUpdateDate) {
        this.bgvReportUpdateDate = bgvReportUpdateDate;
    }

    public String getBgvRun() {
        return bgvRun;
    }

    public void setBgvRun(String bgvRun) {
        this.bgvRun = bgvRun;
    }

    public String getBgvSendDate() {
        return bgvSendDate;
    }

    public void setBgvSendDate(String bgvSendDate) {
        this.bgvSendDate = bgvSendDate;
    }

    public String getIsNewAgreementSigned() {
        return isNewAgreementSigned;
    }

    public void setIsNewAgreementSigned(String isNewAgreementSigned) {
        this.isNewAgreementSigned = isNewAgreementSigned;
    }

    public Integer getPanMatchedKiranProccessId() {
        return panMatchedKiranProccessId;
    }

    public void setPanMatchedKiranProccessId(Integer panMatchedKiranProccessId) {
        this.panMatchedKiranProccessId = panMatchedKiranProccessId;
    }

    public String getReportPancard() {
        return reportPancard;
    }

    public void setReportPancard(String reportPancard) {
        this.reportPancard = reportPancard;
    }

    public String getReportPancardDate() {
        return reportPancardDate;
    }

    public void setReportPancardDate(String reportPancardDate) {
        this.reportPancardDate = reportPancardDate;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getFrankingDownloadDate() {
        return frankingDownloadDate;
    }

    public void setFrankingDownloadDate(String frankingDownloadDate) {
        this.frankingDownloadDate = frankingDownloadDate;
    }

    public Integer getBetterPlaceDuplication() {
        return betterPlaceDuplication;
    }

    public void setBetterPlaceDuplication(Integer betterPlaceDuplication) {
        this.betterPlaceDuplication = betterPlaceDuplication;
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

}
