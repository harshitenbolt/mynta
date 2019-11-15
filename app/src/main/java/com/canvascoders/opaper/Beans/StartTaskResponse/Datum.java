
package com.canvascoders.opaper.Beans.StartTaskResponse;

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
    @SerializedName("type")
    @Expose
    private Object type;
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
    private Object taskEnd;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("remark")
    @Expose
    private Object remark;
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

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
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

    public Object getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Object taskEnd) {
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

}
