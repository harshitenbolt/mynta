
package com.canvascoders.opaper.Beans.SubmitReportResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("support_type")
    @Expose
    private String supportType;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("proccess_id")
    @Expose
    private String proccessId;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("descreption")
    @Expose
    private String descreption;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("support_arise_from")
    @Expose
    private String supportAriseFrom;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
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
