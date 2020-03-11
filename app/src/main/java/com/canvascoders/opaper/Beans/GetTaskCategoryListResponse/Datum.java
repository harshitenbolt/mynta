
package com.canvascoders.opaper.Beans.GetTaskCategoryListResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sub_catetegory")
    @Expose
    private List<SubCatetegory> subCatetegory = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SubCatetegory> getSubCatetegory() {
        return subCatetegory;
    }

    public void setSubCatetegory(List<SubCatetegory> subCatetegory) {
        this.subCatetegory = subCatetegory;
    }

}
