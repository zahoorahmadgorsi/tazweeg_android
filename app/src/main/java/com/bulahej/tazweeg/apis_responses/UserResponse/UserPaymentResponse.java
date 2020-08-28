package com.bulahej.tazweeg.apis_responses.UserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPaymentResponse {


    @SerializedName("Status")
    @Expose
    private Integer Status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("getUrl")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String phone) {
        url = phone;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        this.Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}