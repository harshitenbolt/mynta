
package com.canvascoders.opaper.Beans.TaskDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Datum implements Serializable {

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
    private String adminId;
    @SerializedName("assign_by")
    @Expose
    private String assignBy;

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getStoreFullAddress() {
        return storeFullAddress;
    }

    public void setStoreFullAddress(String storeFullAddress) {
        this.storeFullAddress = storeFullAddress;
    }

    @SerializedName("due_time")
    @Expose
    private String dueTime;
    @SerializedName("store_full_address")
    @Expose
    private String storeFullAddress;

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    @SerializedName("complete_time")
    @Expose
    private String completeTime;


    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
    }

    @SerializedName("complete_date_time")
    @Expose
    private String completeDateTime;

    public String getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    @SerializedName("assign_time")
    @Expose
    private String assignTime;


    @SerializedName("assign_by_name")
    @Expose
    private String assign_by_name;

    public String getAssign_by_name() {
        return assign_by_name;
    }

    public void setAssign_by_name(String assign_by_name) {
        this.assign_by_name = assign_by_name;
    }

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

    public String getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(String startTimer) {
        this.startTimer = startTimer;
    }

    @SerializedName("start_timer")
    @Expose
    private String startTimer;


    public Integer getIsPause() {
        return isPause;
    }

    public void setIsPause(Integer isPause) {
        this.isPause = isPause;
    }

    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("is_pause")
    @Expose
    private Integer isPause;

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
    @SerializedName("process_detail")
    @Expose
    private ProcessDetail processDetail;
    @SerializedName("basic_detail")
    @Expose
    private BasicDetail basicDetail;

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

    public ProcessDetail getProcessDetail() {
        return processDetail;
    }

    public void setProcessDetail(ProcessDetail processDetail) {
        this.processDetail = processDetail;
    }

    public BasicDetail getBasicDetail() {
        return basicDetail;
    }

    public void setBasicDetail(BasicDetail basicDetail) {
        this.basicDetail = basicDetail;
    }


    @SerializedName("sub_task_reason")
    @Expose
    private List<SubTaskReason> subTaskReason = null;

    public List<SubTaskReason> getSubTaskReason() {
        return subTaskReason;
    }

    public void setSubTaskReason(List<SubTaskReason> subTaskReason) {
        this.subTaskReason = subTaskReason;
    }

    @SerializedName("sub_task_list")
    @Expose
    private List<SubTaskList> subTaskList = null;

    public List<SubTaskList> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTaskList> subTaskList) {
        this.subTaskList = subTaskList;
    }
}