package com.bulahej.tazweeg.apis_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponseMessage {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("pin")

    @Expose
    private Integer pin;

    @SerializedName("otpExpiry")
    @Expose
    private Integer otpExpiry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Integer otpExpiry) {
        this.otpExpiry = otpExpiry;
    }
}