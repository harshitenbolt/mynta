package com.canvascoders.opaper.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskList implements Serializable {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;

    public Integer getIsPause() {
        return isPause;
    }

    public void setIsPause(Integer isPause) {
        this.isPause = isPause;
    }

    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("is_pause")
    @Expose
    private Integer isPause;

    public String getCompleteDatetime() {
        return completeDatetime;
    }

    public void setCompleteDatetime(String completeDatetime) {
        this.completeDatetime = completeDatetime;
    }

    @SerializedName("complete_date_time")
    @Expose
    private String completeDatetime;


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @SerializedName("assign_by")
    @Expose
    private String assignBy;



    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("task_start")
    @Expose
    private String taskStart;
    @SerializedName("task_end")
    @Expose
    private String taskEnd;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("generate_by_system")
    @Expose
    private String generateBySystem;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("lms_id")
    @Expose
    private String lmsId;
    @SerializedName("lms_id1")
    @Expose
    private String lmsId1;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
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

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    @SerializedName("better_place_client_reference_id")
    @Expose
    private String betterPlaceClientReferenceId;
    @SerializedName("bgv_run")
    @Expose
    private String bgvRun;

    @SerializedName("due_time")
    @Expose
    private String dueTime;
    @SerializedName("start_timer")
    @Expose
    private String startTimer;

    public String getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(String startTimer) {
        this.startTimer = startTimer;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(String taskStart) {
        this.taskStart = taskStart;
    }

    public String getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(String taskEnd) {
        this.taskEnd = taskEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGenerateBySystem() {
        return generateBySystem;
    }

    public void setGenerateBySystem(String generateBySystem) {
        this.generateBySystem = generateBySystem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getLmsId() {
        return lmsId;
    }

    public void setLmsId(String lmsId) {
        this.lmsId = lmsId;
    }

    public String getLmsId1() {
        return lmsId1;
    }

    public void setLmsId1(String lmsId1) {
        this.lmsId1 = lmsId1;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

}
