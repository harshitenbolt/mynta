
package com.canvascoders.opaper.Beans.SupportListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("support_type")
    @Expose
    private Integer supportType;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("admin_id")
    @Expose
    private Object adminId;
    @SerializedName("invoice_id")
    @Expose
    private Integer invoiceId;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("subject")
    @Expose
    private Integer subject;
    @SerializedName("descreption")
    @Expose
    private String descreption;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("assign_to")
    @Expose
    private Integer assignTo;
    @SerializedName("support_arise_from")
    @Expose
    private String supportAriseFrom;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("delete")
    @Expose
    private String delete;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("attachment_url")
    @Expose
    private String attachmentUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupportType() {
        return supportType;
    }

    public void setSupportType(Integer supportType) {
        this.supportType = supportType;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public Object getAdminId() {
        return adminId;
    }

    public void setAdminId(Object adminId) {
        this.adminId = adminId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(Integer assignTo) {
        this.assignTo = assignTo;
    }

    public String getSupportAriseFrom() {
        return supportAriseFrom;
    }

    public void setSupportAriseFrom(String supportAriseFrom) {
        this.supportAriseFrom = supportAriseFrom;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
