
package com.canvascoders.opaper.Beans.ResignAgreeDetailResponse;

import java.util.List;

import com.canvascoders.opaper.Beans.BasicDetailRateDetailFromResign;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("resign_agreement_title")
    @Expose
    private String resignAgreementTitle;

    @SerializedName("show_agreement")
    @Expose
    private String ShowAgreement;

    public String getShowAgreement() {
        return ShowAgreement;
    }

    public void setShowAgreement(String showAgreement) {
        ShowAgreement = showAgreement;
    }

    @SerializedName("e_sign_document_detail")
    @Expose
    private ESignDocumentDetail eSignDocumentDetail;
    @SerializedName("addendum_detail")
    @Expose
    private List<AddendumDetail> addendumDetail = null;
    @SerializedName("basic_detail_rate_detail")
    @Expose
    private List<BasicDetailRateDetail> basicDetailRateDetail = null;
    @SerializedName("approval_rate_detail")
    @Expose
    private List<ApprovalRateDetail> approvalRateDetail = null;

    public String getResignAgreementTitle() {
        return resignAgreementTitle;
    }

    public void setResignAgreementTitle(String resignAgreementTitle) {
        this.resignAgreementTitle = resignAgreementTitle;
    }

    public ESignDocumentDetail getESignDocumentDetail() {
        return eSignDocumentDetail;
    }

    public void setESignDocumentDetail(ESignDocumentDetail eSignDocumentDetail) {
        this.eSignDocumentDetail = eSignDocumentDetail;
    }

    public List<AddendumDetail> getAddendumDetail() {
        return addendumDetail;
    }

    public void setAddendumDetail(List<AddendumDetail> addendumDetail) {
        this.addendumDetail = addendumDetail;
    }

    public List<BasicDetailRateDetail> getBasicDetailRateDetail() {
        return basicDetailRateDetail;
    }

    public void setBasicDetailRateDetail(List<BasicDetailRateDetail> basicDetailRateDetail) {
        this.basicDetailRateDetail = basicDetailRateDetail;
    }

    public List<ApprovalRateDetail> getApprovalRateDetail() {
        return approvalRateDetail;
    }

    public void setApprovalRateDetail(List<ApprovalRateDetail> approvalRateDetail) {
        this.approvalRateDetail = approvalRateDetail;
    }

    @SerializedName("basic_detail_rate_detail_from_resign")
    @Expose
    private List<BasicDetailRateDetailFromResign> basicDetailRateDetailFromResign = null;

    public List<BasicDetailRateDetailFromResign> getBasicDetailRateDetailFromResign() {
        return basicDetailRateDetailFromResign;
    }

    public void setBasicDetailRateDetailFromResign(List<BasicDetailRateDetailFromResign> basicDetailRateDetailFromResign) {
        this.basicDetailRateDetailFromResign = basicDetailRateDetailFromResign;
    }



}
