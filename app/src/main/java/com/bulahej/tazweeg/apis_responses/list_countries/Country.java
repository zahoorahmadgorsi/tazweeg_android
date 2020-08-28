
package com.bulahej.tazweeg.apis_responses.list_countries;

import android.content.Context;

import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Country implements Serializable {

    @SerializedName("countryId")
    @Expose
    private Integer countryId;

    @SerializedName("countryEN")
    @Expose
    private String countryEN;

    @SerializedName("countryAR")
    @Expose
    private String countryAR;

    @SerializedName("img")
    @Expose
    private String img;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("show")
    @Expose
    private Boolean show;

    public Country(Integer countryId, String countryEN, String countryAR, String img, String code) {
        this.countryId = countryId;
        this.countryEN = countryEN;
        this.countryAR = countryAR;
        this.img = img;
        this.code = code;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryEN() {
        return countryEN;
    }

    public void setCountryEN(String countryEN) {
        this.countryEN = countryEN;
    }

    public String getCountryAR() {
        return countryAR;
    }

    public void setCountryAR(String countryAR) {
        this.countryAR = countryAR;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code =  code;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getName(Context context) {
        if (Utilities.isRTL(context) )
            return countryAR;
        else
            return countryEN;
    }


}
