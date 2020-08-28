
package com.bulahej.tazweeg.apis_responses.list_countries;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public List<Country> getCountries() {
        return Countries;
    }

    public void setCountries(List<Country> countries) {
        Countries = countries;
    }

    @SerializedName("Status")
    @Expose
    private Integer Status;

    @SerializedName("Countries")
    @Expose
    private List<Country> Countries = null;


}
