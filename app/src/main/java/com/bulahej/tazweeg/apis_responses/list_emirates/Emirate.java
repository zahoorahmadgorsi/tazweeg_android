
package com.bulahej.tazweeg.apis_responses.list_emirates;

import android.content.Context;

import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Emirate {

    @SerializedName("stateId")
    @Expose
    private Integer stateId;

    @SerializedName("stateEN")
    @Expose
    private String stateEN;

    @SerializedName("stateAR")
    @Expose
    private String stateAR;

    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer id) {
        this.stateId = id;
    }

    public String getName(Context context) {
        if (Utilities.isRTL(context) )
            return stateAR;
        else
            return stateEN;
    }

    public void setName(String name) {
        this.stateEN = name;
    }

    public String getImageURL() {
        return imgUrl;
    }

    public void setImageURL(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
