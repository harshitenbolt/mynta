
package com.canvascoders.opaper.Beans.DetailsAssessDelBoyResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssessmentTriedResult {

    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("label")
    @Expose
    private String label;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
