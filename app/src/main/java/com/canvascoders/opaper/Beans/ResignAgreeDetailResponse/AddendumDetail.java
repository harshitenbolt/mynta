
package com.canvascoders.opaper.Beans.ResignAgreeDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddendumDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("is_esign")
    @Expose
    private String isEsign;
    @SerializedName("addendum")
    @Expose
    private String addendum;
    @SerializedName("addendum_sign_date")
    @Expose
    private Object addendumSignDate;
    @SerializedName("doc_id")
    @Expose
    private String docId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
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

    public String getIsEsign() {
        return isEsign;
    }

    public void setIsEsign(String isEsign) {
        this.isEsign = isEsign;
    }

    public String getAddendum() {
        return addendum;
    }

    public void setAddendum(String addendum) {
        this.addendum = addendum;
    }

    public Object getAddendumSignDate() {
        return addendumSignDate;
    }

    public void setAddendumSignDate(Object addendumSignDate) {
        this.addendumSignDate = addendumSignDate;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
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
