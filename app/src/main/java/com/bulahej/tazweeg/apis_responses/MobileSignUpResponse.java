package com.bulahej.tazweeg.apis_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileSignUpResponse {
    @SerializedName("Status")
    @Expose
    private Integer Status;

    @SerializedName("Message")
    @Expose
    private SignUpResponseMessage message;

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public SignUpResponseMessage getMessage() {
        return message;
    }

    public void setMessage(SignUpResponseMessage message) {
        this.message = message;
    }
}

