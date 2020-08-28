
package com.bulahej.tazweeg.apis_responses.registration_form_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {

    @SerializedName("Status")
    @Expose
    private Integer status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Values")
    @Expose
    private List<Spinners> dropDowns= null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Spinners> getDropDowns() {
        return dropDowns;
    }

    public void setDropDowns(List<Spinners> dropDowns) {
        this.dropDowns = dropDowns;
    }

}
