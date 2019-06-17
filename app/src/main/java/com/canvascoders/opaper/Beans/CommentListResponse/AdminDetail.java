package com.canvascoders.opaper.Beans.CommentListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
    @SerializedName("am_id")
    @Expose
    private Integer amId;
    @SerializedName("g_auth_secret_key")
    @Expose
    private Object gAuthSecretKey;
    @SerializedName("g_auth_enabled")
    @Expose
    private Integer gAuthEnabled;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("session_id")
    @Expose
    private String sessionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Integer getAmId() {
        return amId;
    }

    public void setAmId(Integer amId) {
        this.amId = amId;
    }

    public Object getGAuthSecretKey() {
        return gAuthSecretKey;
    }

    public void setGAuthSecretKey(Object gAuthSecretKey) {
        this.gAuthSecretKey = gAuthSecretKey;
    }

    public Integer getGAuthEnabled() {
        return gAuthEnabled;
    }

    public void setGAuthEnabled(Integer gAuthEnabled) {
        this.gAuthEnabled = gAuthEnabled;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
