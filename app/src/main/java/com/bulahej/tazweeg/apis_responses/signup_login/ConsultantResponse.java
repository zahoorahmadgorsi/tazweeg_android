
package com.bulahej.tazweeg.apis_responses.signup_login;

import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConsultantResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("User")
    @Expose
    private List<User> consultants = null;

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

    public List<User> getConsultants() {
        return consultants;
    }

    public void setConsultants(List<User> consultants) {
        this.consultants = consultants;
    }

}
