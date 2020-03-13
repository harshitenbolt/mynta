
package com.canvascoders.opaper.Beans.GetTaskEndResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

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
    @SerializedName("admin_id")
    @Expose
    private Object adminId;
    @SerializedName("assign_by")
    @Expose
    private Object assignBy;
    @SerializedName("code")
    @Expose
    private String code;
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
    private Object endDate;
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
    private Object remark;
    @SerializedName("redirect_screen_number")
    @Expose
    private Object redirectScreenNumber;
    @SerializedName("redirect_screen_params")
    @Expose
    private Object redirectScreenParams;
    @SerializedName("is_pause")
    @Expose
    private Integer isPause;
    @SerializedName("generate_by_system")
    @Expose
    private String generateBySystem;
    @SerializedName("is_approval_needed")
    @Expose
    private Integer isApprovalNeeded;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("approval_status")
    @Expose
    private Integer approvalStatus;
    @SerializedName("approval_by")
    @Expose
    private Object approvalBy;
    @SerializedName("approval_date")
    @Expose
    private Object approvalDate;
    @SerializedName("approval_remark")
    @Expose
    private Object approvalRemark;
    @SerializedName("approval_history")
    @Expose
    private Object approvalHistory;
    @SerializedName("task_history")
    @Expose
    private Object taskHistory;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("start_latitude")
    @Expose
    private String startLatitude;
    @SerializedName("start_longitude")
    @Expose
    private String startLongitude;
    @SerializedName("end_latitude")
    @Expose
    private String endLatitude;
    @SerializedName("end_longitude")
    @Expose
    private String endLongitude;
    @SerializedName("attachment_file")
    @Expose
    private String attachmentFile;
    @SerializedName("link_send_date")
    @Expose
    private Object linkSendDate;
    @SerializedName("is_push")
    @Expose
    private Integer isPush;
    @SerializedName("proof")
    @Expose
    private String proof;

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

    public Object getAdminId() {
        return adminId;
    }

    public void setAdminId(Object adminId) {
        this.adminId = adminId;
    }

    public Object getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(Object assignBy) {
        this.assignBy = assignBy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
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

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getRedirectScreenNumber() {
        return redirectScreenNumber;
    }

    public void setRedirectScreenNumber(Object redirectScreenNumber) {
        this.redirectScreenNumber = redirectScreenNumber;
    }

    public Object getRedirectScreenParams() {
        return redirectScreenParams;
    }

    public void setRedirectScreenParams(Object redirectScreenParams) {
        this.redirectScreenParams = redirectScreenParams;
    }

    public Integer getIsPause() {
        return isPause;
    }

    public void setIsPause(Integer isPause) {
        this.isPause = isPause;
    }

    public String getGenerateBySystem() {
        return generateBySystem;
    }

    public void setGenerateBySystem(String generateBySystem) {
        this.generateBySystem = generateBySystem;
    }

    public Integer getIsApprovalNeeded() {
        return isApprovalNeeded;
    }

    public void setIsApprovalNeeded(Integer isApprovalNeeded) {
        this.isApprovalNeeded = isApprovalNeeded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Object getApprovalBy() {
        return approvalBy;
    }

    public void setApprovalBy(Object approvalBy) {
        this.approvalBy = approvalBy;
    }

    public Object getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Object approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Object getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(Object approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    public Object getApprovalHistory() {
        return approvalHistory;
    }

    public void setApprovalHistory(Object approvalHistory) {
        this.approvalHistory = approvalHistory;
    }

    public Object getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(Object taskHistory) {
        this.taskHistory = taskHistory;
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

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public Object getLinkSendDate() {
        return linkSendDate;
    }

    public void setLinkSendDate(Object linkSendDate) {
        this.linkSendDate = linkSendDate;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

}
