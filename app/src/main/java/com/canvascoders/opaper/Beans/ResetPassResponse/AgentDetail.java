
package com.canvascoders.opaper.Beans.ResetPassResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgentDetail {

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
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("assign_user_id")
    @Expose
    private Object assignUserId;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
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
    private Integer cmId;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
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

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

}
