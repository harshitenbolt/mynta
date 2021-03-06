
package com.canvascoders.opaper.Beans;

import com.canvascoders.opaper.Beans.CommentListResponse.CommentListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private String status;

    private CommentListResponse commentListResponse;

    public CommentListResponse getCommentListResponse() {
        return commentListResponse;
    }

    public void setCommentListResponse(CommentListResponse commentListResponse) {
        this.commentListResponse = commentListResponse;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
