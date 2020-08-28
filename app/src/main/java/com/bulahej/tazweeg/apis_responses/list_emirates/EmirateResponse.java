
package com.bulahej.tazweeg.apis_responses.list_emirates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmirateResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("States")
    @Expose
    private List<Emirate> emirates = null;

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

    public List<Emirate> getEmirates() {
        return emirates;
    }

    public void setEmirates(List<Emirate> emirates) {
        this.emirates = emirates;
    }

}
