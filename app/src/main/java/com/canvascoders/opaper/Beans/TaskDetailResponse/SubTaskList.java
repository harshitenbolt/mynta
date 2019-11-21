package com.canvascoders.opaper.Beans.TaskDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubTaskList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("task_id")
    @Expose
    private Integer taskId;
    @SerializedName("sub_task_reason_id")
    @Expose
    private Integer subTaskReasonId;
    @SerializedName("sub_task_reason_text")
    @Expose
    private String subTaskReasonText;
    @SerializedName("pause_time")
    @Expose
    private String pauseTime;
    @SerializedName("resume_time")
    @Expose
    private String resumeTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("attachment_file")
    @Expose
    private String attachmentFile;
    @SerializedName("status")
    @Expose
    private Integer status;
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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getSubTaskReasonId() {
        return subTaskReasonId;
    }

    public void setSubTaskReasonId(Integer subTaskReasonId) {
        this.subTaskReasonId = subTaskReasonId;
    }

    public String getSubTaskReasonText() {
        return subTaskReasonText;
    }

    public void setSubTaskReasonText(String subTaskReasonText) {
        this.subTaskReasonText = subTaskReasonText;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(String resumeTime) {
        this.resumeTime = resumeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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