
package com.canvascoders.opaper.Beans.UserDetailTResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rh_id")
    @Expose
    private Integer rhId;
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getIsMobileVerify() {
        return isMobileVerify;
    }

    public void setIsMobileVerify(String isMobileVerify) {
        this.isMobileVerify = isMobileVerify;
    }

    @SerializedName("is_mobile_verify")
    @Expose
    private String isMobileVerify;

    @SerializedName("assign_user_id")
    @Expose
    private Object assignUserId;
    @SerializedName("remember_token")
    @Expose
    private Object rememberToken;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("cm_id")
    @Expose
    private Object cmId;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("token")
    @Expose
    private String token;

    /**
     * No args constructor for use in serialization
     */
    public UserDetailDatum() {
    }

    /**
     * @param dc
     * @param empId
     * @param agentId
     * @param rhId
     * @param deleted
     * @param fcmId
     * @param city
     * @param updatedAt
     * @param id
     * @param assignUserId
     * @param cmId
     * @param token
     * @param email
     * @param createdAt
     * @param name
     * @param mobile
     * @param rememberToken
     */
    public UserDetailDatum(Integer id, Integer rhId, String empId, String name, String email, String dc, String city, String mobile, Object assignUserId, Object rememberToken, String deleted, String createdAt, String updatedAt, Object cmId, String fcmId, Integer agentId, String token) {
        super();
        this.id = id;
        this.rhId = rhId;
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.dc = dc;
        this.city = city;
        this.mobile = mobile;
        this.assignUserId = assignUserId;
        this.rememberToken = rememberToken;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cmId = cmId;
        this.fcmId = fcmId;
        this.agentId = agentId;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRhId() {
        return rhId;
    }

    public void setRhId(Integer rhId) {
        this.rhId = rhId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Object assignUserId) {
        this.assignUserId = assignUserId;
    }

    public Object getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(Object rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public Object getCmId() {
        return cmId;
    }

    public void setCmId(Object cmId) {
        this.cmId = cmId;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
