
package com.canvascoders.opaper.Beans.PauseTaskResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("sub_task_reason_id")
    @Expose
    private String subTaskReasonId;
    @SerializedName("sub_task_reason_text")
    @Expose
    private String subTaskReasonText;
    @SerializedName("pause_time")
    @Expose
    private String pauseTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSubTaskReasonId() {
        return subTaskReasonId;
    }

    public void setSubTaskReasonId(String subTaskReasonId) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
