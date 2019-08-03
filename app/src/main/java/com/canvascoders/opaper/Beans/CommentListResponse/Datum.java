
package com.canvascoders.opaper.Beans.CommentListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("support_id")
    @Expose
    private Integer supportId;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("admin_id")
    @Expose
    private Object adminId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("delete")
    @Expose
    private String delete;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created_at_format")
    @Expose
    private String createdatformat;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getCreatedatformat() {

        return createdatformat;
    }

    public void setCreatedatformat(String createdatformat) {
        this.createdatformat = createdatformat;
    }

    @SerializedName("attachment_url")
    @Expose
    private String attachmentUrl;
    @SerializedName("admin_detail")
    @Expose
    private AdminDetail adminDetail;
    @SerializedName("agent_detail")
    @Expose
    private AgentDetail agentDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupportId() {
        return supportId;
    }

    public void setSupportId(Integer supportId) {
        this.supportId = supportId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Object getAdminId() {
        return adminId;
    }

    public void setAdminId(Object adminId) {
        this.adminId = adminId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
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

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public AdminDetail getAdminDetail() {
        return adminDetail;
    }

    public void setAdminDetail(AdminDetail adminDetail) {
        this.adminDetail = adminDetail;
    }

    public AgentDetail getAgentDetail() {
        return agentDetail;
    }

    public void setAgentDetail(AgentDetail agentDetail) {
        this.agentDetail = agentDetail;
    }

}
