
package com.canvascoders.opaper.Beans.CommentResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("support_sub")
    @Expose
    private SupportSub supportSub;

    public SupportSub getSupportSub() {
        return supportSub;
    }

    public void setSupportSub(SupportSub supportSub) {
        this.supportSub = supportSub;
    }

}
