
package com.canvascoders.opaper.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranscationId1Response {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("env")
    @Expose
    private String env;
    @SerializedName("response_url")
    @Expose
    private String responseUrl;
    @SerializedName("face_match")
    @Expose
    private String faceMatch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public String getFaceMatch() {
        return faceMatch;
    }

    public void setFaceMatch(String faceMatch) {
        this.faceMatch = faceMatch;
    }

}
