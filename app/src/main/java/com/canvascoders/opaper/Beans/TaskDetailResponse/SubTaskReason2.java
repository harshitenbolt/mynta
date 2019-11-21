package com.canvascoders.opaper.Beans.TaskDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubTaskReason2 {
    private int _id;
    private String _name;

    public SubTaskReason2() {
        this._id = 0;
        this._name = "";
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return this._id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

}