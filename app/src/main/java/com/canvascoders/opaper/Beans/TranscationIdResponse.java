
package com.canvascoders.opaper.Beans;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranscationIdResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("docs")
    @Expose
    private List<String> docs = null;
    @SerializedName("request version")
    @Expose
    private String requestVersion;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }

    public String getRequestVersion() {
        return requestVersion;
    }

    public void setRequestVersion(String requestVersion) {
        this.requestVersion = requestVersion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
