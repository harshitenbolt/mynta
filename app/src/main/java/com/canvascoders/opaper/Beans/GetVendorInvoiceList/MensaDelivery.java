
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MensaDelivery {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_num")
    @Expose
    private String invoiceNum;
    @SerializedName("invoice_num_digit")
    @Expose
    private Integer invoiceNumDigit;
    @SerializedName("lms_id")
    @Expose
    private String lmsId;
    @SerializedName("enbolt_id")
    @Expose
    private String enboltId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("csat_condition")
    @Expose
    private String csatCondition;
    @SerializedName("bill_period")
    @Expose
    private String billPeriod;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("total_shipments")
    @Expose
    private String totalShipments;
    @SerializedName("delivered_shipments")
    @Expose
    private String deliveredShipments;
    @SerializedName("total_pickup_alloted")
    @Expose
    private String totalPickupAlloted;
    @SerializedName("total_shipments_alloted")
    @Expose
    private String totalShipmentsAlloted;
    @SerializedName("delivery_percentage")
    @Expose
    private String deliveryPercentage;
    @SerializedName("i_1")
    @Expose
    private String i1;
    @SerializedName("total_shipments_1")
    @Expose
    private String totalShipments1;
    @SerializedName("delivered_shipments_1")
    @Expose
    private String deliveredShipments1;
    @SerializedName("delivery_percentage_1")
    @Expose
    private String deliveryPercentage1;
    @SerializedName("attendance_criteria_fullfilled")
    @Expose
    private String attendanceCriteriaFullfilled;
    @SerializedName("i_2")
    @Expose
    private String i2;
    @SerializedName("i_3")
    @Expose
    private String i3;
    @SerializedName("debit")
    @Expose
    private Object debit;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("sum_name")
    @Expose
    private String sumName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("oracle_csv")
    @Expose
    private String oracleCsv;
    @SerializedName("download_date")
    @Expose
    private String downloadDate;
    @SerializedName("assign_agent_id")
    @Expose
    private Integer assignAgentId;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("invoice_created")
    @Expose
    private String invoiceCreated;
    @SerializedName("self_invoice_created")
    @Expose
    private String selfInvoiceCreated;
    @SerializedName("deduction_invoice_created")
    @Expose
    private String deductionInvoiceCreated;
    @SerializedName("esign_status")
    @Expose
    private String esignStatus;
    @SerializedName("gst_esign_status")
    @Expose
    private String gstEsignStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_approved")
    @Expose
    private String isApproved;
    @SerializedName("approved_by")
    @Expose
    private Integer approvedBy;
    @SerializedName("approved_by_role1")
    @Expose
    private String approvedByRole1;
    @SerializedName("approved_by_role1_time")
    @Expose
    private String approvedByRole1Time;
    @SerializedName("approved_by2")
    @Expose
    private Integer approvedBy2;
    @SerializedName("approved_by_role2")
    @Expose
    private String approvedByRole2;
    @SerializedName("approved_by_role2_time")
    @Expose
    private String approvedByRole2Time;
    @SerializedName("is_invoice_cancel_by_kirana")
    @Expose
    private Integer isInvoiceCancelByKirana;
    @SerializedName("rejected_msg")
    @Expose
    private Object rejectedMsg;
    @SerializedName("rejected_by")
    @Expose
    private Object rejectedBy;
    @SerializedName("rejected_role")
    @Expose
    private Object rejectedRole;
    @SerializedName("rejected_by_time")
    @Expose
    private Object rejectedByTime;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("cod_debit")
    @Expose
    private String codDebit;
    @SerializedName("loss_shipment_debit")
    @Expose
    private String lossShipmentDebit;
    @SerializedName("debit_esign_status")
    @Expose
    private String debitEsignStatus;
    @SerializedName("rate1")
    @Expose
    private String rate1;
    @SerializedName("if_gst")
    @Expose
    private String ifGst;
    @SerializedName("gstn")
    @Expose
    private String gstn;
    @SerializedName("invoice_type")
    @Expose
    private String invoiceType;
    @SerializedName("store_type")
    @Expose
    private String storeType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Integer getInvoiceNumDigit() {
        return invoiceNumDigit;
    }

    public void setInvoiceNumDigit(Integer invoiceNumDigit) {
        this.invoiceNumDigit = invoiceNumDigit;
    }

    public String getLmsId() {
        return lmsId;
    }

    public void setLmsId(String lmsId) {
        this.lmsId = lmsId;
    }

    public String getEnboltId() {
        return enboltId;
    }

    public void setEnboltId(String enboltId) {
        this.enboltId = enboltId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCsatCondition() {
        return csatCondition;
    }

    public void setCsatCondition(String csatCondition) {
        this.csatCondition = csatCondition;
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(String billPeriod) {
        this.billPeriod = billPeriod;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTotalShipments() {
        return totalShipments;
    }

    public void setTotalShipments(String totalShipments) {
        this.totalShipments = totalShipments;
    }

    public String getDeliveredShipments() {
        return deliveredShipments;
    }

    public void setDeliveredShipments(String deliveredShipments) {
        this.deliveredShipments = deliveredShipments;
    }

    public String getTotalPickupAlloted() {
        return totalPickupAlloted;
    }

    public void setTotalPickupAlloted(String totalPickupAlloted) {
        this.totalPickupAlloted = totalPickupAlloted;
    }

    public String getTotalShipmentsAlloted() {
        return totalShipmentsAlloted;
    }

    public void setTotalShipmentsAlloted(String totalShipmentsAlloted) {
        this.totalShipmentsAlloted = totalShipmentsAlloted;
    }

    public String getDeliveryPercentage() {
        return deliveryPercentage;
    }

    public void setDeliveryPercentage(String deliveryPercentage) {
        this.deliveryPercentage = deliveryPercentage;
    }

    public String getI1() {
        return i1;
    }

    public void setI1(String i1) {
        this.i1 = i1;
    }

    public String getTotalShipments1() {
        return totalShipments1;
    }

    public void setTotalShipments1(String totalShipments1) {
        this.totalShipments1 = totalShipments1;
    }

    public String getDeliveredShipments1() {
        return deliveredShipments1;
    }

    public void setDeliveredShipments1(String deliveredShipments1) {
        this.deliveredShipments1 = deliveredShipments1;
    }

    public String getDeliveryPercentage1() {
        return deliveryPercentage1;
    }

    public void setDeliveryPercentage1(String deliveryPercentage1) {
        this.deliveryPercentage1 = deliveryPercentage1;
    }

    public String getAttendanceCriteriaFullfilled() {
        return attendanceCriteriaFullfilled;
    }

    public void setAttendanceCriteriaFullfilled(String attendanceCriteriaFullfilled) {
        this.attendanceCriteriaFullfilled = attendanceCriteriaFullfilled;
    }

    public String getI2() {
        return i2;
    }

    public void setI2(String i2) {
        this.i2 = i2;
    }

    public String getI3() {
        return i3;
    }

    public void setI3(String i3) {
        this.i3 = i3;
    }

    public Object getDebit() {
        return debit;
    }

    public void setDebit(Object debit) {
        this.debit = debit;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSumName() {
        return sumName;
    }

    public void setSumName(String sumName) {
        this.sumName = sumName;
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

    public Integer getAssignAgentId() {
        return assignAgentId;
    }

    public void setAssignAgentId(Integer assignAgentId) {
        this.assignAgentId = assignAgentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getInvoiceCreated() {
        return invoiceCreated;
    }

    public void setInvoiceCreated(String invoiceCreated) {
        this.invoiceCreated = invoiceCreated;
    }

    public String getSelfInvoiceCreated() {
        return selfInvoiceCreated;
    }

    public void setSelfInvoiceCreated(String selfInvoiceCreated) {
        this.selfInvoiceCreated = selfInvoiceCreated;
    }

    public String getDeductionInvoiceCreated() {
        return deductionInvoiceCreated;
    }

    public void setDeductionInvoiceCreated(String deductionInvoiceCreated) {
        this.deductionInvoiceCreated = deductionInvoiceCreated;
    }

    public String getEsignStatus() {
        return esignStatus;
    }

    public void setEsignStatus(String esignStatus) {
        this.esignStatus = esignStatus;
    }

    public String getGstEsignStatus() {
        return gstEsignStatus;
    }

    public void setGstEsignStatus(String gstEsignStatus) {
        this.gstEsignStatus = gstEsignStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedByRole1() {
        return approvedByRole1;
    }

    public void setApprovedByRole1(String approvedByRole1) {
        this.approvedByRole1 = approvedByRole1;
    }

    public String getApprovedByRole1Time() {
        return approvedByRole1Time;
    }

    public void setApprovedByRole1Time(String approvedByRole1Time) {
        this.approvedByRole1Time = approvedByRole1Time;
    }

    public Integer getApprovedBy2() {
        return approvedBy2;
    }

    public void setApprovedBy2(Integer approvedBy2) {
        this.approvedBy2 = approvedBy2;
    }

    public String getApprovedByRole2() {
        return approvedByRole2;
    }

    public void setApprovedByRole2(String approvedByRole2) {
        this.approvedByRole2 = approvedByRole2;
    }

    public String getApprovedByRole2Time() {
        return approvedByRole2Time;
    }

    public void setApprovedByRole2Time(String approvedByRole2Time) {
        this.approvedByRole2Time = approvedByRole2Time;
    }

    public Integer getIsInvoiceCancelByKirana() {
        return isInvoiceCancelByKirana;
    }

    public void setIsInvoiceCancelByKirana(Integer isInvoiceCancelByKirana) {
        this.isInvoiceCancelByKirana = isInvoiceCancelByKirana;
    }

    public Object getRejectedMsg() {
        return rejectedMsg;
    }

    public void setRejectedMsg(Object rejectedMsg) {
        this.rejectedMsg = rejectedMsg;
    }

    public Object getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(Object rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public Object getRejectedRole() {
        return rejectedRole;
    }

    public void setRejectedRole(Object rejectedRole) {
        this.rejectedRole = rejectedRole;
    }

    public Object getRejectedByTime() {
        return rejectedByTime;
    }

    public void setRejectedByTime(Object rejectedByTime) {
        this.rejectedByTime = rejectedByTime;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCodDebit() {
        return codDebit;
    }

    public void setCodDebit(String codDebit) {
        this.codDebit = codDebit;
    }

    public String getLossShipmentDebit() {
        return lossShipmentDebit;
    }

    public void setLossShipmentDebit(String lossShipmentDebit) {
        this.lossShipmentDebit = lossShipmentDebit;
    }

    public String getDebitEsignStatus() {
        return debitEsignStatus;
    }

    public void setDebitEsignStatus(String debitEsignStatus) {
        this.debitEsignStatus = debitEsignStatus;
    }

    public String getRate1() {
        return rate1;
    }

    public void setRate1(String rate1) {
        this.rate1 = rate1;
    }

    public String getIfGst() {
        return ifGst;
    }

    public void setIfGst(String ifGst) {
        this.ifGst = ifGst;
    }

    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

}
