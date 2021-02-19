package com.canvascoders.opaper.Beans;

import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreTypeBean {
    List<SubStoreType> listGot = new ArrayList<>();

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    JSONArray jsonArray = new JSONArray();

    @SerializedName("store_type")
    @Expose
    private String storeType;
    @SerializedName("rate")
    @Expose
    private String rate = "0";
    @SerializedName("is_approved")
    @Expose
    private String isApproved;
    @SerializedName("store_type_id")
    @Expose
    private Integer storeTypeId;
    private boolean selected = false;

    public StoreTypeBean(JSONObject o) {

        try {
            this.setIsApproved(o.getString("is_approved"));
            this.setStoreType(o.getString("store_type"));
            if (o.getString("rate") != null && o.getString("rate").length() > 0)
                this.setRate(o.getString("rate"));
            else
                this.setRate("0");
            this.setStoreTypeId(o.getInt("store_type_id"));
          /*  List<SubStoreType> list = new ArrayList<>();
            JSONObject jsonArraysubType = new JSONObject(o.getJSONObject("sub_store"));
            for (int i = 0; i < jsonArraysubType.length(); i++) {
                SubStoreType rateTypeBean = new SubStoreType(jsonArraysubType.getJSONObject(i));
                list.add(rateTypeBean);
                if (rateTypeBean.getIsApproved().equalsIgnoreCase("1") || rateTypeBean.getIsApproved().equalsIgnoreCase("2")) {
                    //     isSecondTime = true;
                }
                if (rateTypeBean.getIsApproved().equalsIgnoreCase("1")) {
                    //   btn_skip_to_addendum.setVisibility(View.VISIBLE);
                }
//                                        rateTypeBeans.add(rateTypeBean);
            }
            listGot.clear();
            listGot.addAll(list);*/
            /*ArrayList<String> listdata = new ArrayList<String>();
            JSONArray jArray = (JSONArray)jsonObject;
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    listdata.add(jArray.getString(i));
                }
            }*/
            this.setJsonArray(o.getJSONArray("sub_store"));
            Log.e("odatafound", String.valueOf(o.getJSONArray("sub_store")));
           // this.setSubStoreTypeList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    @SerializedName("Mensa - Alteration")
    @Expose
    private List<MensaAlteration> mensaAlteration = null;

    public List<SubStoreType> getSubStoreTypeList() {
        return subStoreTypeList;
    }

    public void setSubStoreTypeList(List<SubStoreType> subStoreTypeList) {
        this.subStoreTypeList = subStoreTypeList;
    }

    @SerializedName("sub_store")
    @Expose
    private List<SubStoreType> subStoreTypeList = null;


    public List<MensaAlteration> getMensaAlteration() {
        return mensaAlteration;
    }

    public void setMensaAlteration(List<MensaAlteration> mensaAlteration) {
        this.mensaAlteration = mensaAlteration;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(Integer storeTypeId) {
        this.storeTypeId = storeTypeId;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
